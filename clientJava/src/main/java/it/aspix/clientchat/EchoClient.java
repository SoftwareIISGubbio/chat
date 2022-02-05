package it.aspix.clientchat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Builder;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/****************************************************************************
 * Invia messaggi al serve e stampa quelli ricevuti in una lista
 ***************************************************************************/
public class EchoClient extends Application{
	
	TextField input = new TextField();
	ListView<String> lista = new ListView<>();
	WebSocket webSocket;

	@Override
	public void start(Stage finestra) throws Exception {
		BorderPane pannello = new BorderPane();
		
		pannello.setCenter( lista );
		pannello.setBottom(input);
		
		Scene scena = new Scene(pannello,300,300);
		finestra.setTitle("Echo");
		finestra.setScene(scena);
		finestra.show();
		
		input.setOnAction( e -> invia() );
		
		// imposto i parametri di connessione
		HttpClient httpClient = HttpClient.newHttpClient();
		Builder webSocketBuilder = httpClient.newWebSocketBuilder();
		URI uri = URI.create("ws://localhost:8080/serverchat/ok");
		
		Listener listener = new WebSocket.Listener() {
			@Override
			public CompletionStage<Void> onText(WebSocket webSocket, CharSequence data, boolean last) {
				// FIXME: non tiene in considerazione "last"
				webSocket.request(1); // se non lo metti non aspetta più messaggi
				String messaggio = data.toString();
				Platform.runLater( () -> {
					lista.getItems().add(messaggio);
				});
				return null;
		    }
		};
		
		webSocket = webSocketBuilder.buildAsync(uri, listener).get();
	}
	
	private void invia() {
		webSocket.sendText(input.getText(), true);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
