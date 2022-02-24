package it.aspix.serverchat;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.websocket.Session;

public class Master {
    
    private static int contatore = 0;
    
    private static ArrayList<SessioneComunicazione> chatEndPoints = new ArrayList<>();
    
    public static synchronized void addClient(Session client) {
        SessioneComunicazione sc = new SessioneComunicazione(client, ""+(contatore++));
        chatEndPoints.add( sc );
        stampaMessaggio(sc, "si è connesso un nuovo client");
    }
    
    public static synchronized void removeClient(Session s) {
        SessioneComunicazione trovata = null;
        for(SessioneComunicazione sCom: chatEndPoints) {
            if(sCom.session==s) {
                trovata = sCom;
            }
        }
        if(trovata!=null) {
            chatEndPoints.remove( trovata );
            stampaMessaggio("master", "si è disconnesso, ne restano "+chatEndPoints.size());
        }
    }
    
    public static synchronized void setNameForClient(Session client, String name) {
        SessioneComunicazione trovata = null;
        for(SessioneComunicazione sCom: chatEndPoints) {
            if(sCom.session==client) {
                trovata = sCom;
            }
        }
        if(trovata!=null) {
            trovata.userName = name;
        }
    }
    
    public static Messaggio getUserNames(){
        ArrayList<String> u = new ArrayList<String>();
        for(SessioneComunicazione cep: chatEndPoints) {
            u.add(cep.userName);
        }
        return new Messaggio(u);
    }
    
    public static synchronized void broadcast(String testo) {
        chatEndPoints.forEach(endpoint -> {
            try {
                endpoint.session.getBasicRemote().sendText(testo);
            } catch (IOException e) {
                e.printStackTrace();
            }           
        });
    }
    
    private static void stampaMessaggio(String chi, String m) {
        System.out.println("[%8s] %s".formatted(chi, m));
    }
    
    
    private static void stampaMessaggio(SessioneComunicazione sc, String m) {
        stampaMessaggio(sc.name, m);
    }
    
    public static void stampaMessaggio(Session s, String m) {
        SessioneComunicazione trovata = null;
        for(SessioneComunicazione sCom: chatEndPoints) {
            if(sCom.session==s) {
                trovata = sCom;
            }
        }
        if(trovata!=null) {
            stampaMessaggio(trovata.name, m);
        }
    }
}
