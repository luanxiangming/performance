package net.unit8.jmeter.protocol.socket_io.util;

import java.util.HashMap;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.google.gson.JsonElement;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

public class SocketIOCallbackHandlerProxy implements IOCallback {
    public static class EventHandler {
        public void onDisconnect() {
            throw new IllegalStateException();
        };
        
        public void onConnect() {
            throw new IllegalStateException();
        }

        public void onMessage(String data, IOAcknowledge ack) {
            throw new IllegalStateException();
        }

        public void onMessage(JsonElement json, IOAcknowledge ack) {
            throw new IllegalStateException();
        }

        public void on(String event, IOAcknowledge ack, JsonElement... args) {
            throw new IllegalStateException();
        }

        public void onError(SocketIOException socketIOException) {
            throw new IllegalStateException();
        }
    }
    
    public enum Callback {
        DISCONNECT, CONNECT, MESSAGE_STR, MESSAGE_JSON, EVENT, ERROR;
    }
    
    protected static final Logger log = LoggingManager.getLoggerForClass();
    private HashMap<Callback, EventHandler> handlers = new HashMap<Callback, EventHandler>();
    
    public void setHandler(Callback callback, EventHandler handler) {
        handlers.put(callback, handler);
    }

    public void onDisconnect() {
        EventHandler handler = handlers.get(Callback.DISCONNECT);
        if (handler != null) {
            handler.onDisconnect();
        } else {
            log.warn("Unhandled event: DISCONNECT");
        }
    }

    public void onConnect() {
        EventHandler handler = handlers.get(Callback.CONNECT);
        if (handler != null) {
            handler.onConnect();
        } else {
            log.warn("Unhandled event: CONNECT");
        }
    }

    public void onMessage(String data, IOAcknowledge ack) {
        EventHandler handler = handlers.get(Callback.MESSAGE_STR);
        if (handler != null) {
            handler.onMessage(data, ack);
        } else {
            log.warn("Unhandled event: MESSAGE_STR: " + data);
        }
    }

    public void onMessage(JsonElement json, IOAcknowledge ack) {
        EventHandler handler = handlers.get(Callback.MESSAGE_JSON);
        if (handler != null) {
            handler.onMessage(json, ack);
        } else {
            log.warn("Unhandled event: MESSAGE_JSON: " + json.toString());
        }
    }

    public void on(String event, IOAcknowledge ack, JsonElement... args) {
        EventHandler handler = handlers.get(Callback.EVENT);
        if (handler != null) {
            handler.on(event, ack, args);
        } else {
            log.warn("Unhandled event: EVENT " + event + " ARGS " + args.toString());
        }
    }

    public void onError(SocketIOException socketIOException) {
        EventHandler handler = handlers.get(Callback.ERROR);
        if (handler != null) {
            handler.onError(socketIOException);
        } else {
            log.warn("Unhandled event: ERROR: " + socketIOException.toString());
        }
    }

}
