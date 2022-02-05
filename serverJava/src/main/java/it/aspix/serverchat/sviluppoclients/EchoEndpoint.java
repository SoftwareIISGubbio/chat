package it.aspix.serverchat.sviluppoclients;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/****************************************************************************
 * Le informazioni iniziali vengono da
 * https://www.baeldung.com/java-websockets
 ***************************************************************************/
@ServerEndpoint("/echo")
public class EchoEndpoint {
 
    private Session session;
    private static Set<EchoEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen( Session session) throws IOException, EncodeException {
        this.session = session;
        chatEndpoints.add(this);
        System.out.println("si è connesso un nuovo client");
        String message = "Connected!";
        broadcast(message);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
        System.out.println("messaggio: "+message);
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        System.out.println("si è Disconnesso un client");
        chatEndpoints.remove(this);
        String message = "Disconnected!";
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // gestione degli errori
    }

    private static void broadcast(String message) {
        chatEndpoints.forEach(endpoint -> {
            try {
                endpoint.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }           
        });
    }
}

