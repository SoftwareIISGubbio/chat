package it.aspix.serverchat;

import java.io.IOException;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
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
 
    private boolean loginEffettuato = false;
    private static JsonbConfig configurazione = new JsonbConfig().withFormatting(false);
    private static Jsonb gestorePerJson = JsonbBuilder.create(configurazione);

    @OnOpen
    public void onOpen( Session s ) throws IOException, EncodeException {
        Master.addClient(s);
        Master.stampaMessaggio(s, "si è connesso un nuovo client");
    }

    @OnMessage
    public void onMessage(String message, Session s) {
        
        Master.stampaMessaggio(s,"messaggio: "+message);

        try {
        	Messaggio messaggio = gestorePerJson.fromJson(message, Messaggio.class);
    
            if(loginEffettuato) {
            	// TODO: un tantino troppo semplice?
            	Master.broadcast( gestorePerJson.toJson(messaggio) );
            } else {
                Master.stampaMessaggio(s,"attendo un messaggio di login");
            	if( "login".equals(messaggio.tipo)) {
            		if(messaggio.nome.equals(messaggio.password)) {
            		    Master.stampaMessaggio(s,"login OK!");
            			s.getBasicRemote().sendText( gestorePerJson.toJson(new Messaggio("rispostaLogin","ok")) );
            			loginEffettuato = true;
            			Master.setNameForClient(s, messaggio.nome);
            			Master.broadcast( gestorePerJson.toJson(Master.getUserNames()) );
                	} else {
                	    Master.stampaMessaggio(s,"login errato, chiudo");
                		s.getBasicRemote().sendText( gestorePerJson.toJson(new Messaggio("rispostaLogin","errore")) );
                		s.close();
                		Master.removeClient(s); 
                	}
            	} else {
            	    Master.stampaMessaggio(s,"e invece no, chiudo!");
            		Messaggio risposta = new Messaggio("errore","era atteso un messaggio di login");
            		
            		s.getBasicRemote().sendText( gestorePerJson.toJson(risposta) );
            		
            		s.close();
            		Master.removeClient(s);
            	}
            }
        }catch(JsonbException e) {
            System.out.println("ECC: "+e.getLocalizedMessage());
        }catch(IOException e) {
            System.out.println("ECC: "+e.getLocalizedMessage());
        }
        
    }

    @OnClose
    public void onClose(Session session){
        Master.removeClient(session);

        Master.broadcast( gestorePerJson.toJson( Master.getUserNames() ) );
    }

    @OnError
    public void onError(Session s, Throwable throwable) {
        Master.stampaMessaggio(s,throwable.getLocalizedMessage());
        Master.stampaMessaggio(s,"lo chiudo");
        onClose(s);
    }
    
}

