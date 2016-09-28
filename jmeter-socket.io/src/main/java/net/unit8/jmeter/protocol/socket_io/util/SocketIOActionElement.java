package net.unit8.jmeter.protocol.socket_io.util;

import java.io.Serializable;

import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;

public class SocketIOActionElement extends AbstractTestElement implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ACTION_NAME = "actionName"; // $NON-NLS-1$
    public static final String USER_INDEX = "userIndex"; // $NON-NLS-1$    

    public SocketIOActionElement() {
    }
    
    public SocketIOActionElement(String actionName, String userIndex) {
        if (actionName == null || userIndex == null){
            throw new IllegalArgumentException("Parameters must not be null");
        }
        setActionName(actionName);
        setUserIndex(userIndex);
    }

    public SocketIOActionElement(JMeterProperty actionName, JMeterProperty userIndex) {
        if (actionName == null || userIndex == null){
            throw new IllegalArgumentException("Parameters must not be null");
        }
        setProperty(ACTION_NAME, actionName);
        setProperty(USER_INDEX, userIndex);
    }

    protected void setProperty(String name, JMeterProperty prop) {
        JMeterProperty jmp = prop.clone();
        jmp.setName(name);
        setProperty(jmp);
    }

    /**
     * Copy Constructor.
     */
    public SocketIOActionElement(SocketIOActionElement action) {
        this(action.getActionName(), action.getUserIndex());
    }

    public void setActionName(String actionName) {
        setProperty(new StringProperty(ACTION_NAME, actionName));
    }

    public String getActionName() {
        return getPropertyAsString(ACTION_NAME);
    }

    public void setUserIndex(String userIndex) {
        setProperty(new StringProperty(USER_INDEX, userIndex));
    }

    public String getUserIndex() {
        return getPropertyAsString(USER_INDEX);
    }

    @Override
    public String toString() {
        return "actionName:'" + getActionName()
            + "'|userIndex:'" + getUserIndex()
            + "'|id:'" + super.toString() + "'";
    }
}
