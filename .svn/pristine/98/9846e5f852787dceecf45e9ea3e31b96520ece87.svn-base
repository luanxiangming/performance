package net.unit8.jmeter.protocol.socket_io.util.action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.socket.IOAcknowledge;
import io.socket.SocketIO;
import net.unit8.jmeter.protocol.socket_io.sampler.SocketIOSampler;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOAsyncActionBase;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOCallbackHandlerProxy;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOCallbackHandlerProxy.Callback;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOCallbackHandlerProxy.EventHandler;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOUserInfo;

public class TextMessageAction extends SocketIOAsyncActionBase {

    public TextMessageAction(int userIndex) {
        super(userIndex);
    }

    public static final String SEND_MESSAGE = "{\"event_id\":1,\"type\":\"chat\",\"recv_type\":0}";
    public static final String SEND_EVENT = "text";
    private boolean isOK = false;

    @Override
    public SocketIOCallbackData[] run(SocketIOCallbackData[] datas,
                                      SocketIOCallbackHandlerProxy[] handlers,
                                      SocketIOUserInfo[] users,
                                      SocketIO[] sockets,
                                      final SocketIOSampler sampler) throws Exception {
        final TextMessageAction parent = this;
        
        int recvUserIndex = (userIndex + 1) % users.length;
        handlers[recvUserIndex].setHandler(Callback.EVENT, new EventHandler() {
            @Override
            public void on(String event, IOAcknowledge ack, JsonElement... args) {
                log.info("Event " + event + " with args " + args.toString());
                synchronized (parent) {
                    if (event.equals("text")) {
                        isOK = true;
                        parent.notify();
                    }
                }
            }
        });
        
        JsonObject sendMessage = (JsonObject) parser.parse(SEND_MESSAGE);
        int sendUID = Integer.parseInt(users[userIndex].uid);
        int recvUID = Integer.parseInt(users[recvUserIndex].uid);
        sendMessage.addProperty("user_id", recvUID);
        sendMessage.addProperty("text", String.format("Test Message from user %d to %d", sendUID, recvUID));
        
        sockets[userIndex].emit(SEND_EVENT, ack, sendMessage.toString());
        synchronized (parent) {
            try {
                wait(sampler.getConnectTimeout());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!isOK) {
            throw new Exception("No response");
        }
        return datas;
    }

}
