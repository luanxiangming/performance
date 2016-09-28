package net.unit8.jmeter.protocol.socket_io.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.testelement.property.TestElementProperty;

public class SocketIOUserInfos extends ConfigTestElement implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SOCKET_IO_USERINFOS = "UserInfos.users"; //$NON-NLS-1$


    public SocketIOUserInfos() {
        setProperty(new CollectionProperty(SOCKET_IO_USERINFOS, new ArrayList<SocketIOUserInfo>()));
    }

    public CollectionProperty getSocketIOUserInfosCollection() {
        return (CollectionProperty) getProperty(SOCKET_IO_USERINFOS);
    }

    @Override
    public void clear() {
        super.clear();
        setProperty(new CollectionProperty(SOCKET_IO_USERINFOS, new ArrayList<SocketIOUserInfo>()));
    }

    public void setSocketIOUserInfos(List<SocketIOUserInfo> users) {
        setProperty(new CollectionProperty(SOCKET_IO_USERINFOS, users));
    }

    public void addSocketIOUserInfo(String username, String password, String device, String uuid, String version) {
        addSocketIOUserInfo(new SocketIOUserInfo(username, password, device, uuid, version));
    }
    
    public void addSocketIOUserInfo(String username, String password, String device, String uuid, String version, HashMap<String, String> extraArgs) {
        addSocketIOUserInfo(new SocketIOUserInfo(username, password, device, uuid, version, extraArgs));
    }

    public void addSocketIOUserInfo(SocketIOUserInfo user) {
        TestElementProperty newSocketIOUserInfo = new TestElementProperty(user.getUsername(), user);
        if (isRunningVersion()) {
            this.setTemporary(newSocketIOUserInfo);
        }
        getSocketIOUserInfosCollection().addItem(newSocketIOUserInfo);
    }

    public void addSocketIOUserInfo() {
        addSocketIOUserInfo(new SocketIOUserInfo());
    }

    public PropertyIterator iterator() {
        return getSocketIOUserInfosCollection().iterator();
    }

    public SocketIOUserInfo[] asArray(){
        CollectionProperty props = getSocketIOUserInfosCollection();
        final int size = props.size();
        SocketIOUserInfo[] args = new SocketIOUserInfo[size];
        for(int i=0; i<size; i++){
            args[i]=(SocketIOUserInfo) props.get(i).getObjectValue();
        }
        return args;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        PropertyIterator iter = getSocketIOUserInfosCollection().iterator();
        while (iter.hasNext()) {
            SocketIOUserInfo user = (SocketIOUserInfo) iter.next().getObjectValue();
            str.append(user.toString());
            if (iter.hasNext()) {
                str.append("\n"); //$NON-NLS-1$
            }
        }
        return str.toString();
    }

    public void removeSocketIOUserInfo(int row) {
        if (row < getSocketIOUserInfoCount()) {
            getSocketIOUserInfosCollection().remove(row);
        }
    }

    public void removeSocketIOUserInfo(SocketIOUserInfo user) {
        PropertyIterator iter = getSocketIOUserInfosCollection().iterator();
        while (iter.hasNext()) {
            SocketIOUserInfo item = (SocketIOUserInfo) iter.next().getObjectValue();
            if (user.equals(item)) {
                iter.remove();
            }
        }
    }

    public void removeSocketIOUserInfo(String username) {
        PropertyIterator iter = getSocketIOUserInfosCollection().iterator();
        while (iter.hasNext()) {
            SocketIOUserInfo user = (SocketIOUserInfo) iter.next().getObjectValue();
            if (user.getUsername().equals(username)) {
                iter.remove();
            }
        }
    }

    public void removeAllSocketIOUserInfos() {
        getSocketIOUserInfosCollection().clear();
    }

    public void addEmptySocketIOUserInfo() {
        addSocketIOUserInfo(new SocketIOUserInfo());
    }

    public int getSocketIOUserInfoCount() {
        return getSocketIOUserInfosCollection().size();
    }

    public SocketIOUserInfo getSocketIOUserInfo(int row) {
        SocketIOUserInfo user = null;
        if (row < getSocketIOUserInfoCount()) {
            user = (SocketIOUserInfo) getSocketIOUserInfosCollection().get(row).getObjectValue();
        }
        return user;
    }
}
