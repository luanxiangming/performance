package net.unit8.jmeter.protocol.socket_io.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.testelement.property.TestElementProperty;

public class SocketIOActionElements extends ConfigTestElement implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SOCKET_IO_ACTION_ELEMENTS = "ActionElements.actions"; //$NON-NLS-1$

    public SocketIOActionElements() {
        setProperty(new CollectionProperty(SOCKET_IO_ACTION_ELEMENTS, new ArrayList<SocketIOActionElement>()));
    }

    public CollectionProperty getSocketIOActionElementsCollection() {
        return (CollectionProperty) getProperty(SOCKET_IO_ACTION_ELEMENTS);
    }

    @Override
    public void clear() {
        super.clear();
        setProperty(new CollectionProperty(SOCKET_IO_ACTION_ELEMENTS, new ArrayList<SocketIOActionElement>()));
    }

    public void setSocketIOActionElements(List<SocketIOActionElement> actions) {
        setProperty(new CollectionProperty(SOCKET_IO_ACTION_ELEMENTS, actions));
    }

    public void addSocketIOActionElement(String actionName, String userIndex) {
        addSocketIOActionElement(new SocketIOActionElement(actionName, userIndex));
    }

    public void addSocketIOActionElement(SocketIOActionElement action) {
        TestElementProperty newSocketIOActionElement = new TestElementProperty(action.toString(), action);
        if (isRunningVersion()) {
            this.setTemporary(newSocketIOActionElement);
        }
        getSocketIOActionElementsCollection().addItem(newSocketIOActionElement);
    }

    public void addSocketIOActionElement() {
        addSocketIOActionElement(new SocketIOActionElement());
    }

    public PropertyIterator iterator() {
        return getSocketIOActionElementsCollection().iterator();
    }

    public SocketIOActionElement[] asArray(){
        CollectionProperty props = getSocketIOActionElementsCollection();
        final int size = props.size();
        SocketIOActionElement[] args = new SocketIOActionElement[size];
        for(int i = 0; i < size; i++){
            args[i] = (SocketIOActionElement) props.get(i).getObjectValue();
        }
        return args;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        PropertyIterator iter = getSocketIOActionElementsCollection().iterator();
        while (iter.hasNext()) {
            SocketIOActionElement action = (SocketIOActionElement) iter.next().getObjectValue();
            str.append(action.toString());
            if (iter.hasNext()) {
                str.append("\n"); //$NON-NLS-1$
            }
        }
        return str.toString();
    }

    public void removeSocketIOActionElement(int row) {
        if (row < getSocketIOActionElementCount()) {
            getSocketIOActionElementsCollection().remove(row);
        }
    }

    public void removeSocketIOActionElement(SocketIOActionElement action) {
        PropertyIterator iter = getSocketIOActionElementsCollection().iterator();
        while (iter.hasNext()) {
            SocketIOActionElement item = (SocketIOActionElement) iter.next().getObjectValue();
            if (action.equals(item)) {
                iter.remove();
            }
        }
    }

    public void removeAllSocketIOActionElements() {
        getSocketIOActionElementsCollection().clear();
    }

    public void addEmptySocketIOActionElement() {
        addSocketIOActionElement(new SocketIOActionElement());
    }

    public int getSocketIOActionElementCount() {
        return getSocketIOActionElementsCollection().size();
    }

    public SocketIOActionElement getSocketIOActionElement(int row) {
        SocketIOActionElement action = null;
        if (row < getSocketIOActionElementCount()) {
            action = (SocketIOActionElement) getSocketIOActionElementsCollection().get(row).getObjectValue();
        }
        return action;
    }
}
