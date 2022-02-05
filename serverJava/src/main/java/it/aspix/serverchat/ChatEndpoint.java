package it.aspix.serverchat;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/****************************************************************************
 * FIXME: questo programma è una copia di echo
 ***************************************************************************/
@ServerEndpoint("/chat")
public class ChatEndpoint {
 
    private Session session; // per mandare messaggi agli altri: di sicuro si trovano soluzioni più pulite
    private boolean loginEffettuato = false;
    private String name;
    Jsonb gestorePerJson;
    
    private static int contatore = 0;
    private static Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen( Session session) throws IOException, EncodeException {
        this.session = session;
        this.name = ""+(contatore++);
        chatEndpoints.add(this);
        stampaMessaggio("si è connesso un nuovo client");
		JsonbConfig configurazione = new JsonbConfig().withFormatting(true);
		gestorePerJson = JsonbBuilder.create(configurazione);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
    	stampaMessaggio("messaggio: "+message);
        // FIXME: devi mettere un try
    	Messaggio messaggio = gestorePerJson.fromJson(message, Messaggio.class);

        if(loginEffettuato) {
        	// TODO: un tantino troppo semplice?
        	broadcast(message);
        } else {
        	stampaMessaggio("attendo un messaggio di login");
        	if( "login".equals(messaggio.tipo)) {
        		if(messaggio.nome.equals(messaggio.password)) {
        			stampaMessaggio("login OK!");
        			session.getBasicRemote().sendText( gestorePerJson.toJson(new Messaggio("rispostaLogin","ok")) );
        			loginEffettuato = true;
        			// TODO: invia lista aggiornata ai client
            	} else {
            		stampaMessaggio("login errato, chiudo");
            		session.getBasicRemote().sendText( gestorePerJson.toJson(new Messaggio("rispostaLogin","errore")) );
            		session.close();
            	}
        	} else {
        		stampaMessaggio("e invece no, chiudo!");
        		Messaggio risposta = new Messaggio("errore","era atteso un messaggio di login");
        		
        		session.getBasicRemote().sendText( gestorePerJson.toJson(risposta) );
        		
        		session.close();
        	}
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
    	stampaMessaggio("si è disconnesso");
        chatEndpoints.remove(this);
        // TODO: invia messaggio al client?
        // TODO: invia lista aggiornata ai client
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
    
    private void stampaMessaggio(String m) {
    	System.out.println("[%8s] %s".formatted(name, m));
    }
}

