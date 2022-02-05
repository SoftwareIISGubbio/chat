package it.aspix.serverchat.prove;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** solo per dire che esiste e per verificare che il container sia partito */

@WebServlet("/servlet")
public class ProvaServlet extends HttpServlet{
	
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    	response.setContentType("text/plain");
        response.getWriter().println("Hello");
    }

}
