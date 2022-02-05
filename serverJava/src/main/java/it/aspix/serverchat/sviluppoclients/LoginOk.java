package it.aspix.serverchat.sviluppoclients;

import java.io.IOException;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/****************************************************************************
 * Risponde sempre in maniera positiva alla richiesta di login
 ***************************************************************************/
@ServerEndpoint("/ok")
public class LoginOk {

    @OnOpen
    public void onOpen( Session session) throws IOException, EncodeException {
        System.out.println("loginok: si è connesso un nuovo client");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
        System.out.println("loginok messaggio: "+message);
        try {
            session.getBasicRemote().sendText("""
            		{
            			"tipo":"rispostaLogin",
            			"risultato":"ok"
            		}""");
        } catch (IOException e) {
        	System.out.println("errore:"+e.getLocalizedMessage());
            e.printStackTrace();
        } 
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // gestione degli errori
    }

}

