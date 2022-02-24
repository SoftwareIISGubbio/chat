package it.aspix.clientchat.prove.serverbuono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Builder;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * I System.out.println sparsi nel programma sono ovviamente inutili, 
 * servono soltanto per stampare nella console dei messaggi informativi
 * 
 * Riferimenti
 * [1] Listener: https://docs.oracle.com/en/java/javase/17/docs/api/java.net.http/java/net/http/WebSocket.Listener.html
 * [2] StringBuffer: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/StringBuffer.html
 * [3] Charsequence: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/CharSequence.html
 */
public class MessaggiPesantiRicezione {
    
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		// creao un nuovo oggetto che è in grado di comunicare via HTTP
	    // una cosa simile a Button x = new Button();
		HttpClient httpClient = HttpClient.newHttpClient();
		
		// creo un nuovo oggetto che userò poi per aver un websocket 
		Builder webSocketBuilder = httpClient.newWebSocketBuilder();
		
		// per quanto ci riguarda è una solita URL (le URI sono un superinsieme delle URL)
		URI uri = URI.create("ws://localhost:8080/serverchat/lungo");
		
		// [1] crel l'ascoltatore, viene chiamato quando ci sono informazioni da elaborare
		// ha più metodi dei due scritti qui sotto
		Listener listener = new Listener() {
		    
		    // [2] uno StringBuffer è una specie di "stringa dinamica" a cui posso aggiungere caratteri
		    StringBuffer buffer = new StringBuffer();
		    
		    // quando arriva del testo viene chiamato questo metodo
		    // [3] "CharSequence" rappresenta una sequenza di caratteri, 
		    // ad esempio String è una sottoclasse di Charsequence 
			@Override
			public CompletionStage<Void> onText(WebSocket webSocket, CharSequence data, boolean last) {
			    // dichiaro che aspetto un ulteriore messaggio, se non lo faccio chiude il socket dopo il primo
		        webSocket.request(1); 
		        
		        // accodo i caratteri ricevutio al mio buffer
		        buffer.append(data);
		        
		        if( last ) {
		            // se è l'ultimo blocco di dati stampo sulla console il messaggio intero 
		            System.out.println("messaggio completato");
		            String messaggioIntero = buffer.toString(); 
		            System.out.println( messaggioIntero );
		        } else {
		            // è un pezzo di un messaggio più lungo... 
		            System.out.println("arrivati "+(data.length())+"caratteri, ne attendo di altri");
		        }
		        
		        // dichiaro di aver completato il mio lavoro su "Charsequence data"
		        return null;
		    }
			
			// in caso di problemi viene chiamato questo metodo 
			@Override
			public void onError(WebSocket webSocket, Throwable errore) {
			    errore.printStackTrace();
			}
		};
		// creazione del websocket
		WebSocket webSocket = webSocketBuilder.buildAsync(uri, listener).get();
		// invio il messaggio
		webSocket.sendText("ciao", true);

		// NB: se qui il programma termina non fa in tempo a ricevere nulla!
		// nei 3 secondi che questo Thread (quello principale del programma) dorme 
		// si spera che arrivino i dati dal server
		System.out.println("dormo 3 secondi");
		Thread.sleep(3000);
		System.out.println("termino il programma fine.");
	}
	
}
