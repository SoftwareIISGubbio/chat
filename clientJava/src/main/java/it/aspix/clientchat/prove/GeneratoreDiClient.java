package it.aspix.clientchat.prove;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Builder;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import it.aspix.clientchat.Messaggio;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class GeneratoreDiClient {

	private static final int DIMENSIONE_POOL = 20;
	private static final int NUMERO_MESSAGGI = 4;
	private static final int PAUSA_TRA_MESSAGGI = 10; // millisecondi
	private static WebSocket clients[] = new WebSocket[DIMENSIONE_POOL];
	private static Jsonb gestorePerJson = JsonbBuilder.create();

	public static void main(String[] args) throws InterruptedException, ExecutionException {
	    // ascoltatore (buono per tutti) 
        Listener listener = new WebSocket.Listener() {
            
            StringBuffer buffer = new StringBuffer();
            
            @Override
            public CompletionStage<Void> onText(WebSocket webSocket, CharSequence data, boolean last) {
                webSocket.request(1); // se non lo metti non aspetta più messaggi

                // accodo i caratteri ricevuti al mio buffer
                buffer.append(data);
                        
                if( last ) {
                    String messaggioIntero = buffer.toString(); 
                    Messaggio m = gestorePerJson.fromJson(messaggioIntero, Messaggio.class);
                    
                    System.out.println( "["+m.getTipo()+"]" );
                    buffer = new StringBuffer();
                }
                return null;
            }
        };
        
        HttpClient httpClient = HttpClient.newHttpClient();
        Builder webSocketBuilder = httpClient.newWebSocketBuilder();
        URI uri = URI.create("ws://localhost:8080/serverchat/chat");
        
        // creo tutte le connessioni
        for(int i=0 ; i<DIMENSIONE_POOL ; i++ ) {
            // InterruptedException, ExecutionException
            clients[i] = webSocketBuilder.buildAsync(uri, listener).get();
        }
        
        // via con i login
        Messaggio mLogin = new Messaggio();
        for(int i=0 ; i<DIMENSIONE_POOL ; i++ ) {
            mLogin.setTipo("login");
            mLogin.setNome("robot"+i);
            mLogin.setPassword("robot"+i);
            // invio il messaggio
            clients[i].sendText(gestorePerJson.toJson(mLogin), true);
            Thread.sleep(10);
        }
        
        // invio dei messaggi
        Messaggio mTesto = new Messaggio();
        mTesto.setTipo("messaggio");
        for(int nm=0; nm<NUMERO_MESSAGGI; nm++) {
            for(int i=0 ; i<DIMENSIONE_POOL ; i++ ) {
                mTesto.setTesto("robot"+i+"-msg"+nm);
                clients[i].sendText(gestorePerJson.toJson(mTesto), true);
                Thread.sleep(PAUSA_TRA_MESSAGGI);
            }
        }
        
        Thread.sleep(1000);
        
        // chiudo esplicitamente
        for(int i=0 ; i<DIMENSIONE_POOL ; i++ ) {
            clients[i].sendClose(WebSocket.NORMAL_CLOSURE, "end of program");
        }
        
	}
}
