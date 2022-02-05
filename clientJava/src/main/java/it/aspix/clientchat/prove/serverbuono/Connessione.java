package it.aspix.clientchat.prove.serverbuono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Builder;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class Connessione {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		HttpClient httpClient = HttpClient.newHttpClient();
		Builder webSocketBuilder = httpClient.newWebSocketBuilder();
		// URI uri = URI.create("ws://localhost:8080/serverchat/chat");
		URI uri = URI.create("ws://localhost:7000/ws/chat");
		
		Listener listener = new WebSocket.Listener() {
			// https://docs.oracle.com/en/java/javase/17/docs/api/java.net.http/java/net/http/WebSocket.Listener.html#onText(java.net.http.WebSocket,java.lang.CharSequence,boolean)
			@Override
			public CompletionStage<Void> onText(WebSocket webSocket, CharSequence data, boolean last) {
		        webSocket.request(1); // se non lo metti non aspetta più messaggi
		        System.out.println("Arrivati dati -> " + data+ "["+last+"]");
		        return null;
		        // la roba sotto mi pare esageratamente complicata
		        // return CompletableFuture.completedFuture(data).thenAccept( o -> System.out.println("dati: " + o );
		    }
		};
		
		WebSocket webSocket = webSocketBuilder.buildAsync(uri, listener).get();
		
		webSocket.sendText("ciao ", false);
		webSocket.sendText("mondo!", true);
		webSocket.sendText("With a wonderful message.", true);
		
		// NB: se qui il programma termina non fa in tempo a ricevere nulla!
		System.out.println("dormo 5 secondi");
		Thread.sleep(3000);
		System.out.println("fine.");
	}

}
