package it.aspix.serverchat.sviluppoclients;

import java.io.IOException;

import it.aspix.serverchat.Messaggio;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/****************************************************************************
 * Effettua il login se nomeUtente=password
 ***************************************************************************/
@ServerEndpoint("/if")
public class LoginIf {

    @OnOpen
    public void onOpen( Session session) throws IOException, EncodeException {
        System.out.println("loginif: si è connesso un nuovo client");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
        System.out.println("loginif messaggio: "+message);
		JsonbConfig configurazione = new JsonbConfig().withFormatting(true);
		Jsonb gestorePerJson = JsonbBuilder.create(configurazione);
        try {
        	Messaggio messaggio = gestorePerJson.fromJson(message, Messaggio.class);
        	
        	System.out.println("tipo:"+messaggio.tipo);
        	System.out.println("nome:"+messaggio.nome);
        	System.out.println("pass:"+messaggio.password);
        	
        	String json;
        	if(messaggio.nome.equals(messaggio.password)) {
        	    json = gestorePerJson.toJson(new Messaggio("rispostaLogin","ok"));
        	} else {
        		json = gestorePerJson.toJson(new Messaggio("rispostaLogin","errore"));
        	}
    		session.getBasicRemote().sendText( json );	
        } catch (Exception e) {
        	System.out.println("errore:"+e.getLocalizedMessage());
            e.printStackTrace();
            Messaggio messaggio = new Messaggio("errore",e.getLocalizedMessage());
            session.getBasicRemote().sendText( gestorePerJson.toJson(messaggio) );
        } 
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // gestione degli errori
    }

}

