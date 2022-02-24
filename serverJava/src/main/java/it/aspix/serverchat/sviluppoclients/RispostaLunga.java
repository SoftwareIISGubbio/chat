package it.aspix.serverchat.sviluppoclients;

import java.io.IOException;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/****************************************************************************
 * Risponde sempre in maniera positiva alla richiesta di login
 ***************************************************************************/
@ServerEndpoint("/lungo")
public class RispostaLunga {

    @OnOpen
    public void onOpen( Session session) throws IOException, EncodeException {
        System.out.println("{endpoint /lungo}: si è connesso un nuovo client");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
        System.out.println("{endpoint /lungo}: "+message);
        String messaggio = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi efficitur ultricies orci, eu vehicula erat fermentum vel. Nulla sem augue, vestibulum non quam ac, cursus bibendum sem. Aliquam erat volutpat. Aenean ac fermentum felis, non varius nisl. Quisque at sem bibendum, auctor purus dapibus, tincidunt quam. Suspendisse gravida massa a vestibulum condimentum. Ut eu molestie metus, sit amet rutrum ligula.
                Quisque congue porttitor urna nec auctor. Proin scelerisque eleifend tortor vitae dapibus. Sed vitae pharetra dolor. Duis ullamcorper posuere enim vel ultrices. Praesent eget posuere risus, quis feugiat ipsum. Mauris nec magna dictum, pulvinar libero lobortis, suscipit nisl. In sollicitudin nibh id placerat dictum.
                Praesent at eros sit amet est auctor lobortis eu cursus ex. Fusce egestas commodo dolor ac pretium. Curabitur vulputate neque sit amet ex porta, ut posuere tellus pulvinar. Duis consequat pellentesque feugiat. Nunc nec sem dui. Sed a lectus elit. In hac habitasse platea dictumst. Quisque ac libero non enim vehicula semper a sit amet lectus. Donec fermentum blandit leo id commodo. Nam faucibus urna convallis, efficitur orci a, aliquam enim. Donec bibendum nec tortor quis laoreet. Nulla augue eros, molestie quis sem ac, ultrices facilisis lorem. Phasellus placerat, augue molestie faucibus vulputate, lectus diam vehicula nisl, non tempor mi purus a arcu. Etiam id vestibulum quam. Proin turpis enim, viverra vehicula ex eu, aliquet commodo nisi.
                Cras a nunc purus. Sed rhoncus libero laoreet justo vestibulum, eget iaculis orci porttitor. Nunc mollis, sapien a elementum eleifend, ante metus eleifend erat, quis viverra mauris nulla ac felis. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aenean malesuada, magna eget condimentum pellentesque, mauris magna feugiat libero, et pretium erat est sit amet justo. Pellentesque eu nunc magna. Etiam mollis velit vel sapien imperdiet, quis feugiat mi sollicitudin. 
                FINE""";
        int lunghezza=messaggio.length();
        String p1 =  messaggio.substring(0, lunghezza/3+33);
        String p2 =  messaggio.substring(lunghezza/3+33, lunghezza/3*2);
        String p3 =  messaggio.substring(lunghezza/3*2);
        try {
            session.getBasicRemote().sendText( p1, false );
            session.getBasicRemote().sendText( p2, false );
            session.getBasicRemote().sendText( p3, true );
        } catch (IOException e) {
        	System.out.println("errore:"+e.getLocalizedMessage());
            e.printStackTrace();
        } 
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // gestione degli errori
    }

}

