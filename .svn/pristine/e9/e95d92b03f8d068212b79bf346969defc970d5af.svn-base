package net.unit8.jmeter.protocol.socket_io.util.action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.socket.IOAcknowledge;
import io.socket.SocketIO;
import net.unit8.jmeter.protocol.socket_io.sampler.SocketIOSampler;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOAsyncActionBase;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOCallbackHandlerProxy;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOUserInfo;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOCallbackHandlerProxy.Callback;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOCallbackHandlerProxy.EventHandler;

public class CallAction extends SocketIOAsyncActionBase {

    public CallAction(int userIndex) {
        super(userIndex);
    }
    
    public static final String SEND_MESSAGE = "{\"event_id\":1,\"type\":\"call.call\",\"call_type\":1}";
    public static final String SEND_EVENT = "control";
    private int receivedNum;

    @Override
    public SocketIOCallbackData[] run(SocketIOCallbackData[] datas,
                                      SocketIOCallbackHandlerProxy[] handlers,
                                      SocketIOUserInfo[] users,
                                      SocketIO[] sockets,
                                      final SocketIOSampler sampler) throws Exception {
        final CallAction parent = this;
        final int userNum = users.length;
        int groupId = 0;
        receivedNum = 0;

        try {
            groupId = createOrGetGroupId(users, sampler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        for (int recvUserIndex = 0; recvUserIndex < users.length; recvUserIndex++) {
            final SocketIOCallbackData data = datas[recvUserIndex];
            if (recvUserIndex != userIndex) {
                handlers[recvUserIndex].setHandler(Callback.EVENT, new EventHandler() {
                    @Override
                    public void on(String event, IOAcknowledge ack, JsonElement... args) {
                        log.info("Event " + event + " with args " + args.toString());
                        synchronized (parent) {
                            if (event.equals(SEND_EVENT) &&
                                    args[0].getAsJsonObject().get("type").getAsString().equals("call.invite")) {
                                data.event = event;
                                data.eventAck = ack;
                                data.eventArgs = args;
                                receivedNum ++;
                                if (receivedNum == userNum - 1)
                                    parent.notify();
                            }
                        }
                    }
                });
            }
        }
        
        JsonParser parser = new JsonParser();
        JsonObject sendMessage = (JsonObject) parser.parse(SEND_MESSAGE);
        sendMessage.addProperty("user_id", groupId);
        sendMessage.addProperty("call_id", System.currentTimeMillis() / 1000L);
        
        sockets[userIndex].emit(SEND_EVENT, ack, sendMessage.toString());
        synchronized (parent) {
            try {
                wait(sampler.getConnectTimeout());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (receivedNum != userNum - 1) {
            throw new Exception("Call not received");
        }
        return datas;
    }

}
