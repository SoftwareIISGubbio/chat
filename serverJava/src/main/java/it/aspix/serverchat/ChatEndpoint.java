package it.aspix.serverchat;

import java.io.IOException;
import java.util.ArrayList;
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
    private String userName;
    private static JsonbConfig configurazione = new JsonbConfig().withFormatting(true);
    private static Jsonb gestorePerJson = JsonbBuilder.create(configurazione);
    
    private static int contatore = 0;
    private static Set<ChatEndpoint> chatEndPoints = new CopyOnWriteArraySet<>();
    
    private static Messaggio userNames(){
        ArrayList<String> u = new ArrayList<String>();
        for(ChatEndpoint cep: chatEndPoints) {
            u.add(cep.userName);
        }
        System.out.println("======================="+u.size());
        return new Messaggio(u);
    }

    @OnOpen
    public void onOpen( Session session) throws IOException, EncodeException {
        this.session = session;
        this.name = ""+(contatore++);
        chatEndPoints.add(this);
        stampaMessaggio("si è connesso un nuovo client");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
    	stampaMessaggio("messaggio: "+message);
        // FIXME: devi mettere un try
    	Messaggio messaggio = gestorePerJson.fromJson(message, Messaggio.class);

        if(loginEffettuato) {
        	// TODO: un tantino troppo semplice?
        	broadcast(messaggio);
        } else {
        	stampaMessaggio("attendo un messaggio di login");
        	if( "login".equals(messaggio.tipo)) {
        		if(messaggio.nome.equals(messaggio.password)) {
        			stampaMessaggio("login OK!");
        			session.getBasicRemote().sendText( gestorePerJson.toJson(new Messaggio("rispostaLogin","ok")) );
        			loginEffettuato = true;
        			userName = messaggio.nome;
        			// invia lista aggiornata ai client
        			broadcast( userNames() );
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
        chatEndPoints.remove(this);
        stampaMessaggio("si è disconnesso, ne restano "+chatEndPoints.size());
        // TODO: invia messaggio al client?
        broadcast( userNames() );
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        stampaMessaggio(throwable.getLocalizedMessage());
    }

    private static void broadcast(Messaggio message) {
        
        String messaggio = gestorePerJson.toJson(message);
        
        chatEndPoints.forEach(endpoint -> {
            try {
                endpoint.session.getBasicRemote().sendText(messaggio);
            } catch (IOException e) {
                e.printStackTrace();
            }           
        });
    }
    
    private void stampaMessaggio(String m) {
    	System.out.println("[%8s] %s".formatted(name, m));
    }
}

