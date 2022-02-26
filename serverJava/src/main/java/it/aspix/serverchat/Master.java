package it.aspix.serverchat;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.websocket.Session;

/****************************************************************************
 * Mantiene un elenco di tutti i client connessi o offre alcuni metodi di utilità
 *
 * L'accesso all'array condiviso avviene usando synchronized.
 * Magari in un piccolo sistema di prova non è essenziale
 *
 ***************************************************************************/
public class Master {
    
    private static int contatore = 0;
    
    private static ArrayList<SessioneComunicazione> chatEndPoints = new ArrayList<>();
    
    public static void addClient(Session client) {
        SessioneComunicazione sc;
        synchronized(chatEndPoints) {
            sc = new SessioneComunicazione(client, ""+(contatore++));
            chatEndPoints.add( sc );
        }
        stampaMessaggio(sc, "si è connesso un nuovo client");
    }
    
    private static final SessioneComunicazione getClientBySession(Session s) {
        for(SessioneComunicazione sCom: chatEndPoints) {
            if(sCom.session==s) {
                return sCom;
            }
        }
        return null;
    }
    
    public static synchronized void removeClient(Session s) {
        synchronized(chatEndPoints) {
            SessioneComunicazione trovata = getClientBySession(s);
            if(trovata!=null) {
                chatEndPoints.remove( trovata );
                String msg = "un client si è disconnesso, "+switch( chatEndPoints.size() ) {
                    case 0 -> "non ne resta nessuno";
                    case 1 -> "resta "+chatEndPoints.get(0).userName;
                    case 2 -> "restano "+chatEndPoints.get(0).userName +" e "+ chatEndPoints.get(1).userName;
                    default -> "ne restano "+chatEndPoints.size();
                };
                stampaMessaggio("master", msg);
            }
        }
    }
    
    public static void setNameForClient(Session client, String name) {
        synchronized(chatEndPoints) {
            SessioneComunicazione trovata = getClientBySession(client);
            if(trovata!=null) {
                trovata.userName = name;
            }
        }
    }
    
    public static Messaggio getUserNames(){
        // non lo faccio in sezione critica, alla peggio mando il nome di uno sconnesso
        ArrayList<String> u = new ArrayList<String>();
        for(SessioneComunicazione cep: chatEndPoints) {
            u.add(cep.userName);
        }
        return new Messaggio(u);
    }
    
    public static synchronized void broadcast(String testo) {
        for(SessioneComunicazione endpoint: chatEndPoints) {
            try {
                // il controllo serve per evitare problemi con canali in via di chiusura
                if(endpoint.session.isOpen()) {
                    endpoint.session.getBasicRemote().sendText(testo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }           
        }
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
