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
import com.mycompany.datapptgame.GameType;
import com.mycompany.datapptgame.MetaMessage;
import com.mycompany.datapptgame.ModalidadJuego;
import com.mycompany.datapptgame.OpcionJuego;
import com.mycompany.datapptgame.Player;
import com.mycompany.datapptgame.RoundsNumber;
import com.mycompany.datapptgame.TypeMessage;
import static constantes.ConstantesStrings.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import static constantes.conexion.ConstantesConexion.*;
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
            URI uri = new URI(SERVICIO_WEBSOCKET + datos.getNombreJ1());
            connectToWebSocket(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(MyClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String t) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                MetaMessage mt = null;
                try {
                    mt = mapper.readValue(t, new TypeReference<MetaMessage>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mt != null) {
                    if (mt.getType() == TypeMessage.RESPUESTA) {
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
                                ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
                                FXMLLoader loader = new FXMLLoader(getClass().getResource(ESCENA_RESULTADOS), bundle);
                                changeSceneRoot(loader, DesktopApp.getStage());
                            } else if (!eraNull) {
                                FXMLController.cambiaSegundoMensaje();
                            }
                        }
                    } else if (mt.getType() == TypeMessage.DESCONEXION && !datos.rondasFinalizadas()) {
                        shootAlert(FALLO_CONEXION, FALLO_CONEXION_TITLE, true);
                        closeWebsocket();
                    } else if (mt.getType() == TypeMessage.NOMBRE) {
                        try {
                            datos.setNombreJ2((String) mapper.readValue(mapper.writeValueAsString(mt.getContent()), new TypeReference<String>() {
                            }));
                            FXMLLoader loader = null;
                            ResourceBundle bundle=ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
                            switch (datos.getFactorAlgoritmo()) {
                                case 1:
                                    loader = new FXMLLoader(getClass().getResource(ESCENA_JUEGO3), bundle);
                                    break;
                                case 2:
                                    loader = new FXMLLoader(getClass().getResource(ESCENA_JUEGO5), bundle);
                                    break;
                                case 4:
                                    loader = new FXMLLoader(getClass().getResource(ESCENA_JUEGO9), bundle);
                                    break;
                            }
                            changeSceneRoot(loader, DesktopApp.getStage());
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            //CASO CONFIGURACION (POR RANDOM)
                            Player p = mapper.readValue(mapper.writeValueAsString(mt.getContent()), new TypeReference<Player>() {
                            });
                            datos.setRoundsLimit(p.getNumberOfRounds() == RoundsNumber.UNA ? 1 : (p.getNumberOfRounds() == RoundsNumber.TRES ? 3 : 5));
                            datos.setFactorAlgoritmo(p.getTipoJuego() == GameType.JUEGO3 ? 1 : (p.getTipoJuego() == GameType.JUEGO5 ? 2 : 4));
                            datos.setTurno(true);
                            datos.setModalidadJuego(ModalidadJuego.ONLINE.ordinal());
                            ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
                            FXMLLoader loader = new FXMLLoader(datos.getFactorAlgoritmo() == 1 ? getClass().getResource(ESCENA_JUEGO3) : (datos.getFactorAlgoritmo() == 2 ? getClass().getResource(ESCENA_JUEGO5) : getClass().getResource(ESCENA_JUEGO9)), bundle);
                            changeSceneRoot(loader, DesktopApp.getStage());
                        } catch (IOException ex) {
                            Logger.getLogger(MyClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
        session = sn;
    }

    public boolean isOpen() {
        return session.isOpen();
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
