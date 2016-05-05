/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.datapptgame.MetaMessage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import modelo.DataContainer;

/**
 *
 * @author Victor
 */
public class MyClientEndpoint extends Endpoint{
    private Session session;
    private WebSocketContainer container;
    
    public MyClientEndpoint(final DataContainer datos){
        try {
            //192.168.206.1 PORTATIL - 192.168.1.104 CASA
            URI uri =new URI("ws://192.168.206.1:8080/ServerPPTGame/ppt?user="+datos.getNombreJ1());
            connectToWebSocket(uri);
            session.addMessageHandler(new MessageHandler.Whole<String>() {

                @Override
                public void onMessage(String t) {
                    //TODO SOME THINGS
                }
            });
        } catch (URISyntaxException ex) {
            Logger.getLogger(MyClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void connectToWebSocket(URI uri) {
        container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, uri);
        } catch (DeploymentException | IOException ex) {
            //LOGGER.log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }
    
    @Override
    public void onOpen(Session sn, EndpointConfig ec) {
        session=sn;
    }

    @Override
    public void onError(Session session, Throwable thr) {
        super.onError(session, thr); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void sendMessage(MetaMessage message){
        try {
            session.getBasicRemote().sendText(new ObjectMapper().writeValueAsString(message));
        } catch (IOException ex) {
            Logger.getLogger(MyClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
