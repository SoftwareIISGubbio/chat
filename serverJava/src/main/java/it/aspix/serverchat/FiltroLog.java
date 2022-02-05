package it.aspix.serverchat;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

/****************************************************************************
 * Del tutto inutile per il funzionamento della chat.
 * Serve soltanto a stampare l'IP del client. 
 * TODO: verificare cosa succede una volta passati al protocollo WebSocket
 ***************************************************************************/
@WebFilter(description="Stampa alcune informazioni", urlPatterns={"*"})
public class FiltroLog implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("IP: "+request.getRemoteAddr());
		chain.doFilter(request,response);
	}
	
}
