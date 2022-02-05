package it.aspix.clientchat.prove;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Builder;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;

import it.aspix.clientchat.Messaggio;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ClientCitazioni extends Application{

	ListView<String> lvMessaggi = new ListView<String>(); 
	
	String testi[] = {
			"If debugging is the process of removing software bugs, then programming must be the process of putting them in. - E. W. Dijkstra",
			"Hardware: The parts of a computer system that can be kicked - Jeff Pesis",
			"The Internet?  We are not interested in it - Bill Gates, 1993",
			"Never trust a computer you can't throw out a window - Steve Wozniak",
			"It's non that I'm so smart, it's just that I stay with problems longer - A.Einstein",
			"If you fall, I'll be there - floor",
			"It is practically impossible to teach good programming to students that have had a prior exposure to BASIC: as potential programmers they are mentally mutilated beyond hope of regeneration - Edsger W. Dijkstra"
	};
	int indice = 0;
	
	Jsonb gestorePerJson;
	WebSocket webSocket;

	@Override
	public void start(Stage ps) throws Exception {
		Scene scena = new Scene( lvMessaggi );
		ps.setScene(scena);
		ps.setTitle("citazioni");
		ps.show();
		
		// JSON
		gestorePerJson = JsonbBuilder.create();
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
		        
		        Messaggio m = gestorePerJson.fromJson(data.toString(), Messaggio.class);
		        
		        Platform.runLater( ()->{
		        	lvMessaggi.getItems().add(m.getNome()+": "+m.getTesto());
			    });
		        
		        return null;
		    }
		};
		// creazione del websocket
		webSocket = webSocketBuilder.buildAsync(uri, listener).get();
		// preparo il messaggio da inviare
		Messaggio m = new Messaggio();
		m.setTipo("login");
		m.setNome("robot");
		m.setPassword("robot");
		// invio il messaggio
		webSocket.sendText(gestorePerJson.toJson(m), true);
		
	    Timeline timeline = new Timeline(new KeyFrame(
	    	      Duration.seconds(2), // ogni quanto va chiamata la funzione
	    	      x -> mandaMessaggio()));
	    timeline.setCycleCount(100);
	    timeline.play();
	}

	private void mandaMessaggio() {
		Messaggio m = new Messaggio("messaggio",testi[indice]);
		m.setNome("robot");
		webSocket.sendText(gestorePerJson.toJson(m), true); // TODO: modifica dappertutto eventuali commenti, "last" è riferito al buffer non alla comunicazione
		System.out.println(indice);
		indice = (indice + 1) % testi.length;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
