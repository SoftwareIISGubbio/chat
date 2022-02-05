package it.aspix.clientchat.prove.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HelloFX extends Application{

	@Override
	public void start(Stage finestra) throws Exception {
		BorderPane pannello = new BorderPane();
		pannello.setCenter( new Label("ciao mondo!") );
		Scene scena = new Scene(pannello,300,200);
		finestra.setTitle("Hello");
		finestra.setScene(scena);
		finestra.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
