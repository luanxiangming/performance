package net.unit8.jmeter.protocol.socket_io.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;

public class SocketIOUserInfo extends AbstractTestElement implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String USERNAME = "username"; // $NON-NLS-1$
    public static final String PASSWORD = "password"; // $NON-NLS-1$
    public static final String DEVICE = "device"; // $NON-NLS-1$
    public static final String UUID = "uuid"; // $NON-NLS-1$
    public static final String VERSION = "version"; // $NON-NLS-1$

    protected HashMap<String, String> extraParameters = new HashMap<String, String>();
    
    public String token;
    public String uid;

    public SocketIOUserInfo() {
    }
    
    public SocketIOUserInfo(String username, String password, String device, String uuid, String version) {
        if (username == null || password == null || device == null || uuid == null || version == null){
            throw new IllegalArgumentException("Parameters must not be null");
        }
        setUsername(username);
        setPassword(password);
        setDevice(device);
        setUUID(uuid);
        setVersion(version);
    }


    public SocketIOUserInfo(String username, String password, String device, String uuid, String version, HashMap<String, String> extraParameters) {
        if (username == null || password == null || device == null || uuid == null || version == null){
            throw new IllegalArgumentException("Parameters must not be null");
        }
        setUsername(username);
        setPassword(password);
        setDevice(device);
        setUUID(uuid);
        setVersion(version);
        setExtraParameters(extraParameters);
    }


    public SocketIOUserInfo(JMeterProperty username, JMeterProperty password, JMeterProperty device, JMeterProperty uuid, JMeterProperty version, HashMap<String, JMeterProperty> extraParameters) {
        if (username == null || password == null || device == null || uuid == null || version == null){
            throw new IllegalArgumentException("Parameters must not be null");
        }
        setProperty(USERNAME, username);
        setProperty(PASSWORD, password);
        setProperty(DEVICE, device);
        setProperty(UUID, uuid);
        setProperty(VERSION, version);
        setExtraParametersProperty(extraParameters);
    }

    protected void setProperty(String name, JMeterProperty prop) {
        JMeterProperty jmp = prop.clone();
        jmp.setName(name);
        setProperty(jmp);
    }
    
    protected void setExtraParametersProperty(HashMap<String, JMeterProperty> extraParameters) {
        for (Map.Entry<String, JMeterProperty> entry: extraParameters.entrySet()) {
            setProperty(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Copy Constructor.
     */
    public SocketIOUserInfo(SocketIOUserInfo userInfo) {
        this(userInfo.getUsername(), userInfo.getPassword(), userInfo.getDevice(), userInfo.getUUID(), userInfo.getVersion(), userInfo.getExtraArgs());
    }


    public void setUsername(String username) {
        setProperty(new StringProperty(USERNAME, username));
    }


    public String getUsername() {
        return getPropertyAsString(USERNAME);
    }


    public void setPassword(String password) {
        setProperty(new StringProperty(PASSWORD, password));
    }


    public String getPassword() {
        return getPropertyAsString(PASSWORD);
    }


    public void setDevice(String device) {
        setProperty(new StringProperty(DEVICE, device));
    }


    public String getDevice() {
        return getPropertyAsString(DEVICE);
    }

    
    public void setUUID(String uuid) {
        setProperty(new StringProperty(UUID, uuid));
    }


    public String getUUID() {
        return getPropertyAsString(UUID);
    }
    
    
    public void setVersion(String version) {
        setProperty(new StringProperty(VERSION, version));
    }


    public String getVersion() {
        return getPropertyAsString(VERSION);
    }
    
    public void setParameter(String key, String value) {
        extraParameters.put(key, value);
        setProperty(new StringProperty(key, value));
    }
    
    public String getParameter(String key) {
        return getPropertyAsString(key);
    }
    
    public void setExtraParameters(HashMap<String, String> extraParameters) {
        this.extraParameters = extraParameters;
        for (Map.Entry<String, String> entry: extraParameters.entrySet()) {
            setProperty(new StringProperty(entry.getKey(), entry.getValue()));
        }
    }
    
    public HashMap<String, String> getExtraArgs() {
        return extraParameters;
    }
    
    public String getLoginQueryParameter() {
        return "username=" + getUsername() + "&password=" + getPassword() + "&device=" + getDevice() + "&uuid=" + getUUID() + "&version=" + getVersion();
    }

    public String getSocketQueryParameter() {
        return "token=" + token + "&uid=" + uid + "&version=" + getVersion();
    }


    @Override
    public String toString() {
        return "username:'" + getUsername()
            + "'|password:'" + getPassword()
            + "'|device:'" + getDevice()
            + "'|uuid:'" + getUUID()
            + "'|version:'" + getVersion()
            + "'|token:'" + token
            + "'|uid:'" + uid + "'";
    }
}
