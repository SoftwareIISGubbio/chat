package it.aspix.clientchat.prove.servizisviluppo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Builder;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class LoginIf {
	public static void main(String x[]) throws InterruptedException, ExecutionException {
		Listener listener = new WebSocket.Listener() {
			@Override
			public CompletionStage<Void> onText(WebSocket webSocket, CharSequence data, boolean last) {
				// FIXME: non tiene in considerazione "last"
				webSocket.request(1); // se non lo metti non aspetta più messaggi
				String messaggio = data.toString();
				System.out.println(messaggio);
				return null;
		    }
		};
		
		// imposto i parametri di connessione
		HttpClient httpClient = HttpClient.newHttpClient();
		Builder webSocketBuilder = httpClient.newWebSocketBuilder();
		URI uri = URI.create("ws://localhost:8080/serverchat/if");
		
		WebSocket webSocket = webSocketBuilder.buildAsync(uri, listener).get();
		
		webSocket.sendText("""
				{
					"tipo": "login",
					"nome": "utente",
					"password": "utente"
				}
				""", true); // true soltanto perché è un test
		
		// attenzione che se nonn metti quello soitto il programma termina 
		// prima che arrivi la risposta
		Thread.sleep(1000);
	}
	
}
