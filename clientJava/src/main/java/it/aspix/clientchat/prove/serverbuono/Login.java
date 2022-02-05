package it.aspix.clientchat.prove.serverbuono;

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

public class Login {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// JSON
		Jsonb gestorePerJson = JsonbBuilder.create();
		// connessione 
		HttpClient httpClient = HttpClient.newHttpClient();
		Builder webSocketBuilder = httpClient.newWebSocketBuilder();
		URI uri = URI.create("ws://localhost:8080/serverchat/chat");
		// ascoltatore 
		Listener listener = new WebSocket.Listener() {
			@Override
			public CompletionStage<Void> onText(WebSocket webSocket, CharSequence data, boolean last) {
		        webSocket.request(1); // se non lo metti non aspetta più messaggi
		        System.out.println("Arrivati dati -> " + data+ "["+last+"]");
		        return null;
		    }
		};
		// creazione del websocket
		WebSocket webSocket = webSocketBuilder.buildAsync(uri, listener).get();
		// preparo il messaggio da inviare
		Messaggio m = new Messaggio();
		m.setTipo("login");
		m.setNome("edoardo");
		m.setPassword("edoardo");
		// invio il messaggio
		webSocket.sendText(gestorePerJson.toJson(m), true);
		
		// NB: se qui il programma termina non fa in tempo a ricevere nulla!
		System.out.println("dormo 3 secondi");
		Thread.sleep(3000);
		System.out.println("fine.");
	}
}
