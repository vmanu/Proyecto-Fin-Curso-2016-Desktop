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
import javafx.fxml.FXMLLoader;
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

/**
 *
 * @author Victor
 */
public class MyClientEndpoint extends Endpoint {

    private Session session;
    private WebSocketContainer container;

    public MyClientEndpoint(final DataContainer datos) {
        try {
            //192.168.206.1 PORTATIL - 192.168.1.104 CASA
            URI uri = new URI("ws://192.168.1.104:8080/ServerPPTGame/ppt?user=" + datos.getNombreJ1());
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
                        datos.setChosen2(getEnumFromOrdinal(oj.getOpcion(), datos));
                        datos.setIdImagenPulsada2(gestionaPulsadoMaquina(datos.getChosen2(), datos));
                        if (datos.getChosen1() != null) {
                            //CARGAR EL FXML. TODO
                            ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLResult.fxml"), bundle);
                            changeSceneRoot(loader, DesktopApp.getStage());
                        }
                    }
                } else if (mt != null && mt.getType() == TypeMessage.DESCONEXION) {
//                        AlertDialog.Builder dialog = new AlertDialog.Builder(JuegoOnline.this);
//                        dialog.setTitle(R.string.dialogoTitle);
//                        dialog.setMessage(R.string.dialogoMessage);
//                        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                mWebSocketClient.close();
//                                finish();
//                            }
//                        });
//                        dialog.show();
//TODO: LLAMAR A LA FUNCION DEL ALERT QUE ESTÁ GENERADO
//                    ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
                    System.out.println("HA ENTRADO EN FIN DE CONEXION");
//                    showAlertFields(null, bundle.getString("FalloConexion"), bundle.getString("ErrorConexionTitle"), null);
                    
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
        session = sn;
    }

    @Override
    public void onError(Session session, Throwable thr) {
        super.onError(session, thr); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendMessage(MetaMessage message) {
        try {
            session.getBasicRemote().sendText(new ObjectMapper().writeValueAsString(message));
        } catch (IOException ex) {
            Logger.getLogger(MyClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
