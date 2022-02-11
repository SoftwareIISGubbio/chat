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

public class MessaggiPesanti {
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
		
		// do il login per buono e invio un messaggio pesante
		m = new Messaggio();
        m.setTipo("messaggio");
        m.setTesto("""
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi efficitur ultricies orci, eu vehicula erat fermentum vel. Nulla sem augue, vestibulum non quam ac, cursus bibendum sem. Aliquam erat volutpat. Aenean ac fermentum felis, non varius nisl. Quisque at sem bibendum, auctor purus dapibus, tincidunt quam. Suspendisse gravida massa a vestibulum condimentum. Ut eu molestie metus, sit amet rutrum ligula.
            Quisque congue porttitor urna nec auctor. Proin scelerisque eleifend tortor vitae dapibus. Sed vitae pharetra dolor. Duis ullamcorper posuere enim vel ultrices. Praesent eget posuere risus, quis feugiat ipsum. Mauris nec magna dictum, pulvinar libero lobortis, suscipit nisl. In sollicitudin nibh id placerat dictum.
            Praesent at eros sit amet est auctor lobortis eu cursus ex. Fusce egestas commodo dolor ac pretium. Curabitur vulputate neque sit amet ex porta, ut posuere tellus pulvinar. Duis consequat pellentesque feugiat. Nunc nec sem dui. Sed a lectus elit. In hac habitasse platea dictumst. Quisque ac libero non enim vehicula semper a sit amet lectus. Donec fermentum blandit leo id commodo. Nam faucibus urna convallis, efficitur orci a, aliquam enim. Donec bibendum nec tortor quis laoreet. Nulla augue eros, molestie quis sem ac, ultrices facilisis lorem. Phasellus placerat, augue molestie faucibus vulputate, lectus diam vehicula nisl, non tempor mi purus a arcu. Etiam id vestibulum quam. Proin turpis enim, viverra vehicula ex eu, aliquet commodo nisi.
            Cras a nunc purus. Sed rhoncus libero laoreet justo vestibulum, eget iaculis orci porttitor. Nunc mollis, sapien a elementum eleifend, ante metus eleifend erat, quis viverra mauris nulla ac felis. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aenean malesuada, magna eget condimentum pellentesque, mauris magna feugiat libero, et pretium erat est sit amet justo. Pellentesque eu nunc magna. Etiam mollis velit vel sapien imperdiet, quis feugiat mi sollicitudin. 
        """);
        // invio il messaggio
        webSocket.sendText(gestorePerJson.toJson(m), true);
        
		// NB: se qui il programma termina non fa in tempo a ricevere nulla!
		System.out.println("dormo 3 secondi");
		Thread.sleep(3000);
		System.out.println("fine.");
	}
}
