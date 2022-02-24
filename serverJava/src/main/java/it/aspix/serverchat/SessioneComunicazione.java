package it.aspix.serverchat;

import jakarta.websocket.Session;

public class SessioneComunicazione {
    Session session;
    String name;
    String userName;
    
    public SessioneComunicazione(Session session, String name) {
        super();
        this.session = session;
        this.name = name;
    }
}
