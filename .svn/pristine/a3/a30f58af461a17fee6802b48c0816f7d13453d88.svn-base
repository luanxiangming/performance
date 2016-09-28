package net.unit8.jmeter.protocol.socket_io.control.gui;

import net.unit8.jmeter.protocol.socket_io.sampler.SocketIOSampler;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOActionElement;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOUserInfo;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.Functor;
import org.apache.log.Logger;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * GUI for SocketIOSampler
 *
 * @author jiaxiluo
 */
public class SocketIOSamplerGui extends AbstractSamplerGui {

    private static final long serialVersionUID = 1L;
    protected static final Logger log = LoggingManager.getLoggerForClass();

    private JTextField domain;
    private JTextField port;
    private JTextField path;
    private JTextField protocol;
    private JTextField loginPath;
    private JTextField connectTimeout;
    
    private SocketIOUserTablePanel userTablePanel;
    private SocketIOActionTablePanel actionTablePanel;
    protected String[] extraParameters = new String[] {};

    private boolean displayName = true;
    protected String samplerName = null;
    protected String samplerClass = "net.unit8.jmeter.protocol.socket_io.sampler.SocketIOSampler";
    private static final ResourceBundle resources;

    static {
        Locale loc = JMeterUtils.getLocale();
        resources = ResourceBundle.getBundle("net.unit8.jmeter.protocol.socket_io.sampler.SocketIOSampler" + "Resources", loc);
        log.info("Resource " + "net.unit8.jmeter.protocol.socket_io.sampler.SocketIOSampler" +" is loaded for locale " + loc);
    }

    public SocketIOSamplerGui() {
        this(true, null, null, null);
    }
    
    public SocketIOSamplerGui(String samplerName, String samplerClass, String[] extraParameters) {
        this(true, samplerName, samplerClass, extraParameters);
    }

    public SocketIOSamplerGui(boolean displayName, String samplerName, String samplerClass, String[] extraParameters) {
        this.displayName = displayName;
        if (extraParameters != null)
            this.extraParameters = extraParameters;
        if (samplerName != null)
            this.samplerName = samplerName;
        if (samplerClass != null)
            this.samplerClass = samplerClass;
        init();
    }

    public String getLabelResource() {
        throw new IllegalStateException("This shouldn't be called");
    }

    @Override
    public String getStaticLabel() {
        if (samplerName == null)
            return getResString("socket_io_testing_title");
        return samplerName;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        domain.setText(element.getPropertyAsString(SocketIOSampler.DOMAIN));
        port.setText(element.getPropertyAsString(SocketIOSampler.PORT));
        protocol.setText(element.getPropertyAsString(SocketIOSampler.PROTOCOL));
        path.setText(element.getPropertyAsString(SocketIOSampler.PATH));
        connectTimeout.setText(element.getPropertyAsString(SocketIOSampler.CONNECT_TIMEOUT));
        loginPath.setText(element.getPropertyAsString(SocketIOSampler.LOGIN_PATH));
        userTablePanel.configure(element);
        actionTablePanel.configure(element);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TestElement createTestElement() {
        TestElement element = null;
        try {
            Class testElementClass = Class.forName(samplerClass);
            element = (TestElement) testElementClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        element.setName(getName());
        element.setProperty(TestElement.GUI_CLASS, this.getClass().getName());
        element.setProperty(TestElement.TEST_CLASS, samplerClass);

        modifyTestElement(element);
        return element;
    }

    public void modifyTestElement(TestElement element) {
        configureTestElement(element);
        element.setProperty(SocketIOSampler.DOMAIN, domain.getText());
        element.setProperty(SocketIOSampler.PATH, path.getText());
        element.setProperty(SocketIOSampler.PORT, port.getText());
        element.setProperty(SocketIOSampler.PROTOCOL, protocol.getText());
        element.setProperty(SocketIOSampler.LOGIN_PATH, loginPath.getText());
        element.setProperty(SocketIOSampler.CONNECT_TIMEOUT, connectTimeout.getText());
        userTablePanel.modifyTestElement(element);
        actionTablePanel.modifyTestElement(element);
    }

    private JPanel getDomainPanel() {
        domain = new JTextField(20);

        JLabel label = new JLabel(JMeterUtils.getResString("web_server_domain"));
        label.setLabelFor(domain);

        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(label, BorderLayout.WEST);
        panel.add(domain, BorderLayout.CENTER);
        return panel;
    }

    private JPanel getPortPanel() {
        port = new JTextField(4);

        JLabel label = new JLabel(JMeterUtils.getResString("web_server_port"));
        label.setLabelFor(port);

        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(label, BorderLayout.WEST);
        panel.add(port, BorderLayout.CENTER);

        return panel;
    }

    protected Component getProtocolAndPathPanel() {
        // PATH
        path = new JTextField(15);
        JLabel pathLabel = new JLabel(JMeterUtils.getResString("path"));
        pathLabel.setLabelFor(path);

        // PROTOCOL
        protocol = new JTextField(4);
        JLabel protocolLabel = new JLabel(JMeterUtils.getResString("protocol"));
        protocolLabel.setLabelFor(protocol);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(pathLabel);
        panel.add(path);
        panel.add(Box.createHorizontalStrut(5));

        panel.add(protocolLabel);
        panel.add(protocol);
        panel.add(Box.createHorizontalStrut(5));

        panel.setMinimumSize(panel.getPreferredSize());

        return panel;
    }

    private JPanel getLoginPathPanel() {
        loginPath = new JTextField(4);
  
        JLabel label = new JLabel(getResString("socket_io_login_path"));
        label.setLabelFor(loginPath);
  
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(label, BorderLayout.WEST);
        panel.add(loginPath, BorderLayout.CENTER);
  
        return panel;
    }

    private JPanel getConnectTimeoutPanel() {
        connectTimeout = new JTextField(10);

        JLabel label = new JLabel(getResString("socket_io_connect_timeout"));
        label.setLabelFor(connectTimeout);
  
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(label, BorderLayout.WEST);
        panel.add(connectTimeout, BorderLayout.CENTER);
  
        return panel;
    }
    
    public class UserTableModel extends ObjectTableModel {
        private static final long serialVersionUID = 1L;
        private ArrayList<ArrayList<String>> writeArgs;
        private ArrayList<Functor> writeFunctors;
        private ArrayList<String> headers;

        public UserTableModel(String[] headers, Class<?> _objClass,
                Functor[] readFunctors, Functor[] writeFunctors,
                ArrayList<ArrayList<String>> writeArgs2, Class<?>[] editorClasses) {
            super(headers, _objClass, readFunctors, writeFunctors, editorClasses);
            this.writeArgs = writeArgs2;
            this.writeFunctors = new ArrayList<Functor>(Arrays.asList(writeFunctors));
            this.headers = new ArrayList<String>(Arrays.asList(headers));
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public void setValueAt(Object cellValue, int row, int col) {
            ArrayList<Object> objects = (ArrayList<Object>) getObjectList();
            if (row < objects.size()) {
                Object value = objects.get(row);
                if (col < writeFunctors.size()) {
                    Functor setMethod = writeFunctors.get(col);
                    if (setMethod != null) {
                        ArrayList<Object> args = new ArrayList<Object>(writeArgs.get(col));
                        args.add(cellValue);
                        setMethod.invoke(value, args.toArray());
                        super.fireTableDataChanged();
                    }
                }
                else if(headers.size() == 1)
                {
                    objects.set(row,cellValue);
                }
            }
        }
    }
    
    private void initUserTable() {
        ArrayList<String> headers = new ArrayList<String>(
                Arrays.asList(new String[] {SocketIOUserInfo.USERNAME,
                                            SocketIOUserInfo.PASSWORD, 
                                            SocketIOUserInfo.DEVICE,
                                            SocketIOUserInfo.UUID,
                                            SocketIOUserInfo.VERSION
                }));
        ArrayList<Functor> getters = new ArrayList<Functor>(
                Arrays.asList(new Functor[] {new Functor("getUsername"), //$NON-NLS-1$
                                             new Functor("getPassword"), //$NON-NLS-1$
                                             new Functor("getDevice"), //$NON-NLS-1$
                                             new Functor("getUUID"), //$NON-NLS-1$
                                             new Functor("getVersion") //$NON-NLS-1$
                }));
        ArrayList<Functor> setters = new ArrayList<Functor>(
                Arrays.asList(new Functor[] {new Functor("setUsername"), //$NON-NLS-1$
                                             new Functor("setPassword"), //$NON-NLS-1$
                                             new Functor("setDevice"), //$NON-NLS-1$
                                             new Functor("setUUID"), //$NON-NLS-1$
                                             new Functor("setVersion") //$NON-NLS-1$
                }));
        ArrayList<ArrayList<String>> writeArgs = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < 5; i++) {
            writeArgs.add(new ArrayList<String>());
        }
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>(Arrays.asList(new Class<?>[] {String.class, String.class, String.class, String.class, String.class}));
        for (String param: extraParameters) {
            headers.add(param);
            getters.add(new Functor("getParameter", new Object[] {param})); //$NON-NLS-1$
            setters.add(new Functor("setParameter", new Class<?>[] {String.class, String.class}));
            writeArgs.add(new ArrayList<String>(Arrays.asList(new String[] {param}))); //$NON-NLS-1$
            classes.add(String.class);
        }
        
        userTablePanel = new SocketIOUserTablePanel(
                "Users", new UserTableModel(
                        headers.toArray(new String[] {}),
                        SocketIOUserInfo.class,
                        getters.toArray(new Functor[] {}),
                        setters.toArray(new Functor[] {}),
                        writeArgs,
                        classes.toArray(new Class<?>[] {}))
                );
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));

        if (displayName) {
            setBorder(makeBorder());
            add(makeTitlePanel(), BorderLayout.NORTH);
        }
        
        initUserTable();
        
        // Init action table.
        actionTablePanel = new SocketIOActionTablePanel("Actions", new ObjectTableModel(
                new String[] {SocketIOActionElement.ACTION_NAME,
                              SocketIOActionElement.USER_INDEX},
                SocketIOActionElement.class,
                new Functor[] {new Functor("getActionName"),
                               new Functor("getUserIndex")},
                new Functor[] {new Functor("setActionName"),
                               new Functor("setUserIndex")},
                new Class<?>[] {String.class, String.class})
        );

        // MAIN PANEL
        VerticalPanel mainPanel = new VerticalPanel();
        JPanel webRequestPanel = new HorizontalPanel();
        JPanel serverPanel = new JPanel();
        serverPanel.setLayout(new BoxLayout(serverPanel, BoxLayout.X_AXIS));
        serverPanel.add(getDomainPanel());
        serverPanel.add(getPortPanel());
        webRequestPanel.add(serverPanel, BorderLayout.NORTH);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.add(getProtocolAndPathPanel());
        northPanel.add(getConnectTimeoutPanel());
        webRequestPanel.add(northPanel, BorderLayout.CENTER);
        
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.add(getLoginPathPanel());
        loginPanel.add(userTablePanel);
        webRequestPanel.add(loginPanel, BorderLayout.SOUTH);
        

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(actionTablePanel);

        mainPanel.add(webRequestPanel);
        mainPanel.add(southPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Gets the resource string for this key.
     *
     * If the resource is not found, a warning is logged
     *
     * @param key
     *            the key in the resource file
     * @return the resource string if the key is found; otherwise, return
     *         "[res_key="+key+"]"
     */
    public static String getResString(String key) {
        return getResStringDefault(key, RES_KEY_PFX + key + "]");
    }

    public static final String RES_KEY_PFX = "[res_key=";

    /*
     * Helper method to do the actual work of fetching resources; allows
     * getResString(S,S) to be deprecated without affecting getResString(S);
     */
    private static String getResStringDefault(String key, String defaultValue) {
        if (key == null) {
            return null;
        }
        // Resource keys cannot contain spaces
        key = key.replace(' ', '_');
        key = key.toLowerCase(java.util.Locale.ENGLISH);
        String resString = null;
        try {
            resString = resources.getString(key);
        } catch (MissingResourceException mre) {
            log.warn("ERROR! Resource string not found: [" +
                    key + "]", mre);
            resString = defaultValue;
        }
        return resString;
    }
}
