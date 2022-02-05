package it.aspix.serverchat.prove;

import java.io.IOException;

import it.aspix.serverchat.Messaggio;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** stampa un Messaggio nel formato JSON */

@WebServlet("/json")
public class ProvaJson extends HttpServlet{
	
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    	response.setContentType("application/json");
        Messaggio m = new Messaggio();
        m.setTipo("rispostaLogin");
        m.setRisultato("ok");
        
		JsonbConfig configurazione = new JsonbConfig().withFormatting(true);
		Jsonb gestorePerJson = JsonbBuilder.create(configurazione);
		String testoJson = gestorePerJson.toJson(m);
        response.getWriter().println( testoJson );
    }

}
