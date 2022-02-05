package it.aspix.clientchat.prove.fx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FinestraSecondaria extends Stage{

	public FinestraSecondaria(){
		Button pChiudi = new Button("chiudimi");
		BorderPane pannello = new BorderPane();
		pannello.setCenter( pChiudi );
		Scene scena = new Scene(pannello,300,200);
		setTitle("Hello");
		setScene(scena);
		
		pChiudi.setOnAction( e -> azioneChiudi() );
	}

	private void azioneChiudi() {
		this.close();
	}
	
}
