package it.aspix.clientchat.prove.serializzazione;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

/****************************************************************************
 * Semplice prova per serializzare/deserializzare un messaggio usando
 * JSON-B
 ***************************************************************************/
public class SerializzaDeserializza {

	public static void main(String[] args) {
		// creo un oggetto di prova
		Citazione messaggio = new Citazione();
		messaggio.setAutore( "Lev Tolstoj" );
		messaggio.setFrase( "Tutti pensano a cambiare il mondo, ma nessuno pensa a cambiare se stesso." );
		messaggio.setSecolo( 19 );
		
		// creo l'oggetto che userò per gestire la conversione da/verso JSON
		// possono essere impostati dei parametridi configurazine ma non è
		// necessario farlo, in c aso basta chiamare create() senza parametri
		JsonbConfig configurazione = new JsonbConfig().withFormatting(true);
		Jsonb gestorePerJson = JsonbBuilder.create(configurazione);
		
		// oggetto Java -> JSON (serializzazione)
		System.out.println( messaggio.toString() );
		String testoJson = gestorePerJson.toJson(messaggio);
		System.out.println( testoJson );
		
		// JSON -> oggetto Java (deserializzazione)
		// per questo modo di scrivere le stringhe su più righe vedi
		// https://docs.oracle.com/en/java/javase/15/text-blocks/index.html
		// da notare la proprietà "scadenza" che nell'oggetto Citazione non esiste 
		testoJson = """
				{
				  "autore": "Lao Tzu",
				  "frase": "Fa più rumore un albero che cade di una foresta che cresce.",
				  "scadenza": -6
				}""";
		System.out.println( testoJson );
		messaggio = gestorePerJson.fromJson(testoJson, Citazione.class);
		System.out.println( messaggio );
	}

}
