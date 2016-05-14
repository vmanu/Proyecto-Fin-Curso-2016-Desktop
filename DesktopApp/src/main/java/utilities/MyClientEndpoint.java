/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.datapptgame.MetaMessage;
import com.mycompany.datapptgame.OpcionJuego;
import com.mycompany.datapptgame.TypeMessage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import modelo.DataContainer;
import static utilities.UtilidadesJavaFX.*;
import vista.DesktopApp;
import vista.FXMLController;

/**
 * Gestiona la conexion Websocket
 *
 * @author Victor e Ivan
 */
public class MyClientEndpoint extends Endpoint {

    private Session session;
    private WebSocketContainer container;

    /**
     * Constructor del websocket y gestion del onMessage de éste
     *
     * @param datos necesario para la concatenacion del nombre del jugador a la
     * ruta y otras utilizaciones en el on message
     */
    public MyClientEndpoint(final DataContainer datos) {
        try {
            //192.168.206.1 PORTATIL - 192.168.1.104 CASA - SERVIDOR (DEFINITIVO) ws://servidor-pptgame.rhcloud.com:8000/ServerPPTGame/ppt?user=
            URI uri = new URI("ws://localhost:8080/ServerPPTGame/ppt?user=" + datos.getNombreJ1());
            connectToWebSocket(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(MyClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String t) {
                System.out.println("onMessage: " + t);
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                MetaMessage mt = null;
                try {
                    mt = mapper.readValue(t, new TypeReference<MetaMessage>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mt != null && mt.getType() == TypeMessage.RESPUESTA) {
                    OpcionJuego oj = null;
                    try {
                        oj = mapper.readValue(mapper.writeValueAsString(mt.getContent()), new TypeReference<OpcionJuego>() {
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (oj != null) {
                        boolean eraNull = datos.getChosen2() == null;
                        datos.setChosen2(getEnumFromOrdinal(oj.getOpcion(), datos));
                        datos.setIdImagenPulsada2(gestionaPulsadoMaquina(datos.getChosen2(), datos));
                        if (datos.getChosen1() != null) {
                            //CARGAR EL FXML. TODO
                            //DesktopApp.getStage().hide();
                            ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLResult.fxml"), bundle);
                            changeSceneRoot(loader, DesktopApp.getStage());
                        } else if (!eraNull) {
                            FXMLController.cambiaSegundoMensaje();
                        }
                    }
                } else if (mt != null && mt.getType() == TypeMessage.DESCONEXION && !datos.rondasFinalizadas()) {
                    UtilidadesJavaFX.shootAlert();
                } else if (mt != null && mt.getType() == TypeMessage.NOMBRE) {
                    try {
                        datos.setNombreJ2((String) mapper.readValue(mapper.writeValueAsString(mt.getContent()), new TypeReference<String>() {
                        }));
                        //tarea.notify();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    /**
     * Constructor del container del websocket
     *
     * @param uri direccion a la que conectarse
     */
    private void connectToWebSocket(URI uri) {
        container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, uri);
        } catch (DeploymentException | IOException ex) {
            //LOGGER.log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }

    /**
     * Gestiona el evento onOpen del websocket, ejecutado al abrir la conexion
     * de websocket, se asigna la session para poder cerrarla cuando sea
     * requerido
     *
     * @param sn
     * @param ec
     */
    @Override
    public void onOpen(Session sn, EndpointConfig ec) {
        System.out.println("ENTRAMOS EN EL ON_OPEN");
        session = sn;
    }

    /**
     * Gestiona el evento onError del websocket
     *
     * @param session
     * @param thr
     */
    @Override
    public void onError(Session session, Throwable thr) {
        super.onError(session, thr); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Gestiona el evento onClose del websocket
     *
     * @param session
     * @param closeReason
     */
    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Método encargado del envio de mensajes a través de websockets
     *
     * @param message
     */
    public void sendMessage(MetaMessage message) {
        boolean sal = false;
        while (!sal) {
            if (session != null && session.isOpen()) {
                try {
                    System.out.println("EN SEND MESSAGE EL MENSAJE ES: "+new ObjectMapper().writeValueAsString(message));
                    session.getBasicRemote().sendText(new ObjectMapper().writeValueAsString(message));
                } catch (IOException ex) {
                    Logger.getLogger(MyClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                }
                sal = true;
            } else {
                Logger.getLogger("NO CONECTADO AUN");
            }
        }
    }

    public void closeWebsocket() {
        try {
            session.close();
        } catch (IOException ex) {
            Logger.getLogger(MyClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
