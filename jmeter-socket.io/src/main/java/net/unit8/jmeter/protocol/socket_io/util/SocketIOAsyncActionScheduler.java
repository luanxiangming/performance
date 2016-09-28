package net.unit8.jmeter.protocol.socket_io.util;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import io.socket.SocketIO;
import net.unit8.jmeter.protocol.socket_io.sampler.SocketIOSampler;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOAsyncActionBase.SocketIOCallbackData;

public class SocketIOAsyncActionScheduler {
    protected static final Logger log = LoggingManager.getLoggerForClass();
    private SocketIOAsyncActionBase[] actions = null;
    private SocketIOUserInfo[] users = null;
    private SocketIOCallbackHandlerProxy[] handlers = null;
    private SocketIO[] sockets = null;
    private SocketIOSampler sampler = null;
    
    public SocketIOAsyncActionScheduler(SocketIOUserInfo[] users,
                                        SocketIO[] sockets,
                                        SocketIOAsyncActionBase[] actions,
                                        SocketIOCallbackHandlerProxy[] handlers,
                                        SocketIOSampler sampler) throws Exception {
        if (users == null || actions == null || handlers == null || sampler == null)
            throw new Exception("SocketIOAsyncActionScheduler parameters should not be null.");
        if (handlers.length != users.length)
            throw new Exception("Length of handlers must equal to length of users.");
        if (sockets.length != users.length)
            throw new Exception("Length of sockets must equal to length of users.");
        this.actions = actions;
        this.users = users;
        this.handlers = handlers;
        this.sockets = sockets;
        this.sampler = sampler;
    }
    
    public boolean run() {
        SocketIOCallbackData[] datas = new SocketIOCallbackData[users.length];
        for (int i = 0; i < users.length; i++) {
            datas[i] = new SocketIOCallbackData();
        }

        for (SocketIOAsyncActionBase action : actions) {
            if (action != null) {
                try {
                    datas = action.run(datas, handlers, users, sockets, sampler);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(e.getMessage());
                    return false;
                }
            }
        }
        
        return true;
    }
}
