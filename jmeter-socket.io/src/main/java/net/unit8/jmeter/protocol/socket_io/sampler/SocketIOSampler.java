package net.unit8.jmeter.protocol.socket_io.sampler;

import net.unit8.jmeter.protocol.socket_io.util.SocketIOActionElement;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOActionElements;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOAsyncActionBase;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOAsyncActionScheduler;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOCallbackHandlerProxy;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOCallbackHandlerProxy.Callback;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOCallbackHandlerProxy.EventHandler;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOUserInfo;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOUserInfos;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.*;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;
import org.eclipse.jetty.util.ConcurrentHashSet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * The sampler for SocketIO.

 * @author jiaxiluo
 */
public class SocketIOSampler extends AbstractSampler implements TestStateListener {
    static {
        disableSSLVerification();
    }

    private static final long serialVersionUID = 1L;
    protected static final Logger log = LoggingManager.getLoggerForClass();

    protected Set<String> applicableConfigClasses = new HashSet<String>(
            Arrays.asList(new String[]{
                    "net.unit8.jmeter.protocol.socket_io.control.gui.SocketIOSamplerGui",
                    "org.apache.jmeter.config.gui.SimpleConfigGui"}));

    private static final String DEFAULT_PROTOCOL = "ws";
    private static final int UNSPECIFIED_PORT = 0;
    private static final String UNSPECIFIED_PORT_AS_STRING = "0";
    private static final int URL_UNSPECIFIED_PORT = -1;

    private SocketIO[] sockets;
    private int connectedSockets = 0;
    private static final ConcurrentHashSet<SocketIO> samplerConnections = new ConcurrentHashSet<SocketIO>();
    private SocketIOCallbackHandlerProxy[] handlers;
    private SocketIOAsyncActionScheduler scheduler;

    private boolean initialized = false;
    private boolean logined = false;
    private String responseMessage;

    public static final String DOMAIN = "SocketIOSampler.domain";
    public static final String PORT = "SocketIOSampler.port";
    public static final String PATH = "SocketIOSampler.path";
    public static final String PROTOCOL = "SocketIOSampler.protocol";
    public static final String CONNECT_TIMEOUT = "SocketIOSampler.connectTimeout";;
    public static final String ACK_MESSAGE = "SocketIOSampler.ackMessage";
    public static final String ACK_TIMEOUT = "SocketIOSampler.ackTimeout";
    public static final String RECV_MESSAGE = "SocketIOSampler.recvMessage";
    public static final String RECV_TIMEOUT = "SocketIOSampler.recvTimeout";
    public static final String LOGIN_PATH = "SocketIOSampler.loginPath";
    private static final String USER_INFOS = "SocketIOSampler.userInfos";
    private static final String ACTION_ELEMENTS = "SocketIOSampler.actionElements";
    
    public SocketIOSampler() {
        this(null);
    }
    
    public SocketIOSampler(Set<String> applicableConfigClasses) {
        if (applicableConfigClasses != null)
            this.applicableConfigClasses = applicableConfigClasses;
    }

    public void initialize() throws Exception {
        final SocketIOSampler parent = this;
        final String threadName = JMeterContextService.getContext().getThread().getThreadName();

        if (!initialized) {
            SocketIOUserInfo[] users = getUserInfos();
            sockets = new SocketIO[users.length];
            SocketIOAsyncActionBase[] actions = getActions();
            handlers = new SocketIOCallbackHandlerProxy[users.length];
            for (int i = 0; i < users.length; i++) {
                handlers[i] = new SocketIOCallbackHandlerProxy();
            }
            scheduler = new SocketIOAsyncActionScheduler(users, sockets, actions, handlers, this);
            login(users);

            if (logined) {
                for (int i = 0; i < users.length; i++) {
                    URI uri = getSocketURI();
                    log.info("Handshake URI: " + uri.toString());
                    sockets[i] = new SocketIO(uri.toURL());
                    sockets[i].setQueryString(users[i].getSocketQueryParameter());
                    
                    handlers[i].setHandler(Callback.CONNECT, new EventHandler() {
                        @Override
                        public void onConnect() {
                            log.info("Connect " + threadName);
                            synchronized (parent) {
                                connectedSockets ++;
                                if (connectedSockets == getUserInfos().length) {
                                    initialized = true;
                                    parent.notify();
                                }
                            }
                        }
                    });
                    
                    handlers[i].setHandler(Callback.DISCONNECT, new EventHandler() {
                        @Override
                        public void onDisconnect() {
                            log.info("Disconnect " + threadName);
                            synchronized (parent) {
                                connectedSockets --;
                                initialized = false;
                            }
                        }
                    });
                    
                    handlers[i].setHandler(Callback.ERROR, new EventHandler() {
                        @Override
                        public void onError(SocketIOException socketIOException) {
                            log.info("an Error occured" + threadName);
                            socketIOException.printStackTrace();
                        }
                    });
                    
                    sockets[i].addHeader("Cookie", "token=" + users[i].token).connect(handlers[i]);
                    synchronized (parent) {
                        if (initialized == false)
                            wait(getConnectTimeout());
                    }

                    samplerConnections.add(sockets[i]);
                }
            }
        }
    }

    public SampleResult sample(Entry entry) {
        SampleResult res = new SampleResult();
        res.setSampleLabel(getName());

        if (!initialized) {
            try {
                initialize();
            } catch (Exception e) {
                res.setResponseMessage(e.getMessage());
                res.setSuccessful(false);
                e.printStackTrace();
                return res;
            }
        }
        try {
            ensureConnected();
            doSample(scheduler, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
    private void ensureConnected() throws Exception {
        for (SocketIO socket: sockets) {
            if (!socket.isConnected()) {
                initialize();
                ensureConnected();
                break;
            }
        }
    }

    protected void doSample(SocketIOAsyncActionScheduler scheduler, SampleResult res) {
        res.sampleStart();
        if (scheduler.run()) {
            res.setResponseCodeOK();
            res.setSuccessful(true);
        } else {
            res.setResponseCode("204");
            res.setSuccessful(false);
        }
        res.sampleEnd();
    }
    
    protected void login(SocketIOUserInfo[] users) {
        if (!logined) {
            try {
                URL url = getLoginURI().toURL();
                for (int i = 0; i < users.length; i++) {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    wr.writeBytes(users[i].getLoginQueryParameter());
                    wr.flush();
                    wr.close();
    
                    int responseCode = connection.getResponseCode();
                    log.info("Login with response code " + responseCode);
    
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
    
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
    
                    JsonParser parser = new JsonParser();
                    String responseStr = response.toString();
                    JsonObject json = (JsonObject) parser.parse(responseStr);
                    JsonObject result = json.get("result").getAsJsonObject();
                    users[i].token = result.get("token").getAsString();
                    users[i].uid = result.get("user").getAsJsonObject().get("user_id").getAsString();
                    logined = true;
                    log.info("token=" + users[i].token + " uid=" + users[i].uid);
                }
            } catch (final Exception e) {
                e.printStackTrace();
                logined = false;
            }
        }
    }
    
    public static String makePostConnection(URL url, String query, String cookie) throws Exception {
        disableSSLVerification();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        if (cookie != null) {
            connection.setRequestProperty("Cookie", cookie);
        }
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(query);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
    
    public static void disableSSLVerification() { 
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            SocketIO.setDefaultSSLSocketFactory(sc);

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected SocketIOAsyncActionBase[] getActions() {
        SocketIOActionElement[] elements = getActionElements();
        SocketIOAsyncActionBase[] actions = new SocketIOAsyncActionBase[elements.length];
        for (int i = 0; i < elements.length; i++) {
            Class actionClass;
            try {
                actionClass = Class.forName("net.unit8.jmeter.protocol.socket_io.util.action."
                                                  + elements[i].getActionName()
                                                  + "Action");
                actions[i] = (SocketIOAsyncActionBase) actionClass.getConstructor(int.class)
                        .newInstance(Integer.parseInt(elements[i].getUserIndex()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return actions;
    }
    
    private void setUserInfosObject(SocketIOUserInfos userInfos) {
        setProperty(new TestElementProperty(USER_INFOS, userInfos));
    }
    
    private SocketIOUserInfos getUserInfosObject() {
        return (SocketIOUserInfos) getProperty(USER_INFOS).getObjectValue();
    }
    
    public SocketIOUserInfo[] getUserInfos() {
        final SocketIOUserInfos userInfos = getUserInfosObject();
        return userInfos == null ? new SocketIOUserInfo[] {} : userInfos.asArray();
    }
    
    public void setUserInfos(SocketIOUserInfo[] users) {
        SocketIOUserInfos userInfos = new SocketIOUserInfos();
        for (SocketIOUserInfo user: users) {
            userInfos.addSocketIOUserInfo(user);
        }
        setUserInfosObject(userInfos);
    }
    
    private void setActionElementsObject(SocketIOActionElements actions) {
        setProperty(new TestElementProperty(ACTION_ELEMENTS, actions));
    }
    
    private SocketIOActionElements getActionElementsObject() {
        return (SocketIOActionElements) getProperty(ACTION_ELEMENTS).getObjectValue();
    }
    
    public SocketIOActionElement[] getActionElements() {
        final SocketIOActionElements actions = getActionElementsObject();
        return actions == null ? new SocketIOActionElement[] {} : actions.asArray();
    }
    
    public void setActionElements(SocketIOActionElement[] actions) {
        SocketIOActionElements actionElements = new SocketIOActionElements();
        for (SocketIOActionElement action: actions) {
            actionElements.addSocketIOActionElement(action);
        }
        setActionElementsObject(actionElements);
    }
    
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public void setName(String name) {
        if (name != null)
            setProperty(TestElement.NAME, name);
    }

    @Override
    public String getName() {
        return getPropertyAsString(TestElement.NAME);
    }

    @Override
    public void setComment(String comment){
        setProperty(new StringProperty(TestElement.COMMENTS, comment));
    }

    @Override
    public String getComment(){
        return getProperty(TestElement.COMMENTS).getStringValue();
    }
    
    public URI getLoginURI() throws URISyntaxException {
        return getURI(getLoginPath());
    }
    
    public URI getSocketURI() throws URISyntaxException {
        return getURI(getPath());
    }

    public URI getURI(String path) throws URISyntaxException {
        return getURI(path, null);
    }
    
    public URI getURI(String path, String query) throws URISyntaxException {
        String domain = this.getDomain();
        String protocol = this.getProtocol();

        // HTTP URLs must be absolute, allow file to be relative
        if (!path.startsWith("/")){
            path = "/" + path;
        }

        if (isProtocolDefaultPort()) {
            return new URI(protocol, null, domain, -1, path, query, null);
        }
        return new URI(protocol, null, domain, getPort(), path, query, null);
    }

    public void setPath(String path) {
        setProperty(PATH, path);
    }

    public String getPath() {
        String p = getPropertyAsString(PATH);
        return encodeSpaces(p);
    }
    
    public void setLoginPath(String path) {
        setProperty(LOGIN_PATH, path);
    }
  
    public String getLoginPath() {
        String p = getPropertyAsString(LOGIN_PATH);
        return encodeSpaces(p);
    }

    public void setPort(int value) {
        setProperty(new IntegerProperty(PORT, value));
    }

    public static int getDefaultPort(String protocol,int port){
        if (port==URL_UNSPECIFIED_PORT){
            return
                    protocol.equalsIgnoreCase(HTTPConstants.PROTOCOL_HTTP)  ? HTTPConstants.DEFAULT_HTTP_PORT :
                            protocol.equalsIgnoreCase(HTTPConstants.PROTOCOL_HTTPS) ? HTTPConstants.DEFAULT_HTTPS_PORT :
                                    port;
        }
        return port;
    }

    /**
     * Get the port number from the port string, allowing for trailing blanks.
     *
     * @return port number or UNSPECIFIED_PORT (== 0)
     */
    public int getPortIfSpecified() {
        String port_s = getPropertyAsString(PORT, UNSPECIFIED_PORT_AS_STRING);
        try {
            return Integer.parseInt(port_s.trim());
        } catch (NumberFormatException e) {
            return UNSPECIFIED_PORT;
        }
    }

    /**
     * Tell whether the default port for the specified protocol is used
     *
     * @return true if the default port number for the protocol is used, false otherwise
     */
    public boolean isProtocolDefaultPort() {
        final int port = getPortIfSpecified();
        final String protocol = getProtocol();
        return port == UNSPECIFIED_PORT ||
                ("ws".equalsIgnoreCase(protocol) && port == HTTPConstants.DEFAULT_HTTP_PORT) ||
                ("wss".equalsIgnoreCase(protocol) && port == HTTPConstants.DEFAULT_HTTPS_PORT);
    }

    public int getPort() {
        final int port = getPortIfSpecified();
        if (port == UNSPECIFIED_PORT) {
            String prot = getProtocol();
            if ("wss".equalsIgnoreCase(prot)) {
                return HTTPConstants.DEFAULT_HTTPS_PORT;
            }
            if (!"ws".equalsIgnoreCase(prot)) {
                log.warn("Unexpected protocol: "+prot);
            }
            return HTTPConstants.DEFAULT_HTTP_PORT;
        }
        return port;
    }

    public void setDomain(String value) {
        setProperty(DOMAIN, value);
    }

    public String getDomain() {
        return getPropertyAsString(DOMAIN);
    }

    public void setProtocol(String value) {
        setProperty(PROTOCOL, value.toLowerCase(java.util.Locale.ENGLISH));
    }

    public String getProtocol() {
        String protocol = getPropertyAsString(PROTOCOL);
        if (protocol == null || protocol.length() == 0 ) {
            return DEFAULT_PROTOCOL;
        }
        return protocol;
    }
    
    public void setConnectTimeout(long value) {
        setProperty(new LongProperty(CONNECT_TIMEOUT, value));
    }
  
    public long getConnectTimeout() {
        return getPropertyAsLong(CONNECT_TIMEOUT, 20000L);
    }

    protected String encodeSpaces(String path) {
        return JOrphanUtils.replaceAllChars(path, ' ', "%20");
    }

    public void testStarted() {
        testStarted("");
    }

    public void testStarted(String host) {
    }

    public void testEnded() {
        testEnded("");
    }

    public void testEnded(String host) {
        try {
            for(SocketIO socket : samplerConnections) {
                socket.disconnect();
            }
        } catch (Exception e) {
            log.error("sampler error when close.", e);
        }
    }

    /**
     * @see org.apache.jmeter.samplers.AbstractSampler#applies(org.apache.jmeter.config.ConfigTestElement)
     */
    @Override
    public boolean applies(ConfigTestElement configElement) {
        String guiClass = configElement.getProperty(TestElement.GUI_CLASS).getStringValue();
        return applicableConfigClasses.contains(guiClass);
    }

}