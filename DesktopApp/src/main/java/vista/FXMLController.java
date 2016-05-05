/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.datapptgame.MetaMessage;
import com.sun.webkit.Timer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import static constantes.Constantes.*;
import eu.hansolo.enzo.notification.Notification;
import eu.hansolo.enzo.notification.Notification.Notifier;
import eu.hansolo.enzo.notification.NotificationEvent;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.DataContainer;
import com.mycompany.datapptgame.Fichas3;
import com.mycompany.datapptgame.Fichas5;
import com.mycompany.datapptgame.Fichas9;
import com.mycompany.datapptgame.GameType;
import com.mycompany.datapptgame.ModalidadJuego;
import com.mycompany.datapptgame.OpcionJuego;
import com.mycompany.datapptgame.Player;
import com.mycompany.datapptgame.RoundsNumber;
import com.mycompany.datapptgame.TypeMessage;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.prefs.Preferences;
import javafx.collections.ObservableMap;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import utilities.PreferencesManager;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.glassfish.tyrus.spi.ClientContainer;
import utilities.MyClientEndpoint;
import static utilities.UtilidadesJavaFX.*;

/**
 *
 * @author Victor
 */
@ClientEndpoint
public class FXMLController implements Initializable {

    private static DataContainer datos;
    private final String RUTA_IMAGENES = "imagenes";
    private MyClientEndpoint websocket;
    private static HashMap<Integer, String> comprobarGameType = new HashMap() {
        {
            put(1, "FXMLJuegoGame3.fxml");
            put(2, "FXMLJuegoGame5.fxml");
            put(4, "FXMLJuegoGame9.fxml");
        }
    };

    @FXML
    private ComboBox cbox;
    @FXML
    private Label resultJ1;
    @FXML
    private Label resultJ2;
    @FXML
    private Label winnerLabel;
    @FXML
    private ImageView resultImagenJ1Choosed;
    @FXML
    private ImageView resultImagenJ2Choosed;
    @FXML
    private Button buttonContinueResult;
    @FXML
    private VBox RadioGroup_Player_Normal;
    @FXML
    private VBox RadioGroup_Games_Normal;
    @FXML
    private VBox RadioGroup_Rounds_Normal;
    @FXML
    private TextField TxtFieldP1;
    @FXML
    private TextField TxtFieldP2;
    @FXML
    private TextField NumberRoundsCustom;
    @FXML
    private VBox RadioGroup_Games_Online;
    @FXML
    private VBox RadioGroup_Rounds_Online;
    @FXML
    private TextField TxtFieldP1Online;

    @FXML
    private void handleButtonsMenuPrincipalAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = null;
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
        switch (((Button) event.getSource()).getId()) {
            case ID_BOTON_PLAY_MENU_PRINCIPAL:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuJuegoNormal.fxml"), bundle);
                break;
            case ID_BOTON_PLAY_ONLINE_MENU_PRINCIPAL:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuJuegoOnline.fxml"), bundle);
                break;
            case ID_BOTON_RULES_MENU_PRINCIPAL:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuRules.fxml"), bundle);
                break;
            case ID_BOTON_RULES_GRAPHICALLY_MENU_PRINCIPAL:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLRulesGraphic.fxml"), bundle);
                break;
            case ID_BOTON_DEVELOPERS_MENU_PRINCIPAL:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuDevelopers.fxml"), bundle);
                break;
            default:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuPrincipal.fxml"), bundle);
                break;
        }
        changeSceneRoot(loader, stage);
    }

    @FXML
    private void handleButtonBackAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuPrincipal.fxml"), bundle);
        changeSceneRoot(loader, stage);
    }

    @FXML
    private void handleButtonsMenuNormalAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = null;
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
        switch (((Button) event.getSource()).getId()) {
            case ID_BOTON_PLAY_MENU_JUEGO_NORMAL:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuOpcionesNormal.fxml"), bundle);
                break;
            case ID_BOTON_SCORES_MENU_JUEGO_NORMAL:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLScores.fxml"), bundle);
                break;
            case ID_BOTON_BACKUP_MENU_JUEGO_NORMAL:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuJuegoNormal.fxml"), bundle);
                break;
            case ID_BOTON_BACK_MENU_JUEGO_NORMAL:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuPrincipal.fxml"), bundle);
                break;
            default:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuJuegoNormal.fxml"), bundle);
                break;
        }
        changeSceneRoot(loader, stage);
    }

    @FXML
    private void handleButtonsMenuOnlineAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = null;
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
        switch (((Button) event.getSource()).getId()) {
            case ID_BOTON_PLAY_MENU_JUEGO_ONLINE:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuOpcionesJuegoOnline.fxml"), bundle);
                break;
            case ID_BOTON_SCORES_MENU_JUEGO_ONLINE:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuJuegoOnline.fxml"), bundle);
                break;
            case ID_BOTON_BACK_MENU_JUEGO_ONLINE:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuPrincipal.fxml"), bundle);
                break;
            default:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuJuegoOnline.fxml"), bundle);
                break;
        }
        changeSceneRoot(loader, stage);
    }

    @FXML
    private void handleRadioButtonsAction(ActionEvent event) {
        String rb = ((Node) event.getSource()).getId();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        TextField txtField, txtField1, txtFieldRounds;
        switch (rb) {
            case ID_RADIOBUTTON_1_PLAYERS:
                txtField1 = (TextField) stage.getScene().lookup("#TxtFieldP1");
                txtField = (TextField) stage.getScene().lookup("#TxtFieldP2");
                txtField.setManaged(false);
                txtField.setVisible(false);
                txtField1.setPrefWidth(490.0);
                break;
            case ID_RADIOBUTTON_2_PLAYERS:
                txtField1 = (TextField) stage.getScene().lookup("#TxtFieldP1");
                txtField = (TextField) stage.getScene().lookup("#TxtFieldP2");
                txtField.setManaged(true);
                txtField.setVisible(true);
                txtField1.setPrefWidth(230.0);
                break;
            case ID_RADIOBUTTON_ROUND_CUSTOMIZED:
                txtFieldRounds = (TextField) stage.getScene().lookup("#NumberRoundsCustom");
                txtFieldRounds.setManaged(true);
                txtFieldRounds.setVisible(true);
                break;
            default:
                txtFieldRounds = (TextField) stage.getScene().lookup("#NumberRoundsCustom");
                txtFieldRounds.setManaged(false);
                txtFieldRounds.setVisible(false);
                break;
        }
    }

    @FXML
    private void handleButtonsMenuOpcionesJuegoOnlineAction(ActionEvent event) {
        boolean cargarPantalla = true;
        String roundsOption = "", gameOption = "", player1Name = "";
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String boton = ((Button) event.getSource()).getId();
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
        FXMLLoader loader = loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuJuegoNormal.fxml"), bundle);
        if (boton.equals(ID_BOTON_PLAY_OPCIONES_MENU_ONLINE)) {
            if (((Button) stage.getScene().lookup("#" + ID_BOTON_RANDOMLY_OPCIONES_MENU_ONLINE)).isVisible()) {
                setVisibilitiesStateMenuOpcionesOnline(stage, false);
                cargarPantalla = false;
            } else {
                gameOption = getSelectedRadioButtonID(((ObservableList<Node>) ((VBox) stage.getScene().lookup("#RadioGroup_Games_Online")).getChildren()));
                switch (gameOption) {
                    case ID_RADIOBUTTON_GAME_OF_3:
                        loader = new FXMLLoader(getClass().getResource("/fxml/FXMLJuegoGame3.fxml"), bundle);
                        datos.setFactorAlgoritmo(1);
                        break;
                    case ID_RADIOBUTTON_GAME_OF_5:
                        loader = new FXMLLoader(getClass().getResource("/fxml/FXMLJuegoGame5.fxml"), bundle);
                        datos.setFactorAlgoritmo(2);
                        break;
                    case ID_RADIOBUTTON_GAME_OF_9:
                        loader = new FXMLLoader(getClass().getResource("/fxml/FXMLJuegoGame9.fxml"), bundle);
                        datos.setFactorAlgoritmo(4);
                        break;
                }
                roundsOption = getSelectedRadioButtonID(((ObservableList<Node>) ((VBox) stage.getScene().lookup("#RadioGroup_Rounds_Online")).getChildren()));
                switch (roundsOption) {
                    case ID_RADIOBUTTON_ROUND_OF_1:
                        datos.setRoundsLimit(1);
                        break;
                    case ID_RADIOBUTTON_ROUND_OF_3:
                        datos.setRoundsLimit(3);
                        break;
                    case ID_RADIOBUTTON_ROUND_OF_5:
                        datos.setRoundsLimit(5);
                        break;
                }
                player1Name = ((TextField) stage.getScene().lookup("#TxtFieldP1Online")).getText();
                if (!player1Name.isEmpty()) {
                    datos.setNombreJ1(((TextField) stage.getScene().lookup("#TxtFieldP1Online")).getText());
                } else {
                    cargarPantalla = false;
                    showAlertFieldsWithExpandable(bundle, bundle.getString("P1NoSetted"));
                }
            }
        } else if (boton.equals(ID_BOTON_RANDOMLY_OPCIONES_MENU_ONLINE)) {
            //MAKE RANDOMLY THE SETTING OF THE GAME
        } else //BACK
         if (!((Button) stage.getScene().lookup("#" + ID_BOTON_RANDOMLY_OPCIONES_MENU_ONLINE)).isVisible()) {
                setVisibilitiesStateMenuOpcionesOnline(stage, true);
                cargarPantalla = false;
            } else {
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuJuegoOnline.fxml"), bundle);
                datos = null;
            }
        if (cargarPantalla) {
            changeSceneRoot(loader, stage);
            datos.setTurno(true);
            PreferencesManager.setPreferencesOnline(roundsOption, gameOption, player1Name);
            datos.setModalidadJuego(ModalidadJuego.ONLINE.ordinal());
        }
    }

    @FXML
    private void handleButtonsMenuOpcionesJuegoNormalAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String roundsOption = "", playerNumber = "", gameOption = "", player1Name = "", player2Name = "", customRounds = "";
        StringBuilder excepcion = new StringBuilder();
        boolean everythingOk = true;
        FXMLLoader loader = null;
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
        switch (((Node) event.getSource()).getId()) {
            case ID_BOTON_PLAY_OPCIONES_MENU_NORMAL:
                player1Name = ((TextField) stage.getScene().lookup("#TxtFieldP1")).getText();
                gameOption = getSelectedRadioButtonID(((ObservableList<Node>) ((VBox) stage.getScene().lookup("#RadioGroup_Games_Normal")).getChildren()));
                switch (gameOption) {
                    case ID_RADIOBUTTON_GAME_OF_3:
                        loader = new FXMLLoader(getClass().getResource("/fxml/FXMLJuegoGame3.fxml"), bundle);
                        datos.setFactorAlgoritmo(1);
                        break;
                    case ID_RADIOBUTTON_GAME_OF_5:
                        loader = new FXMLLoader(getClass().getResource("/fxml/FXMLJuegoGame5.fxml"), bundle);
                        datos.setFactorAlgoritmo(2);
                        break;
                    case ID_RADIOBUTTON_GAME_OF_9:
                        loader = new FXMLLoader(getClass().getResource("/fxml/FXMLJuegoGame9.fxml"), bundle);
                        datos.setFactorAlgoritmo(4);
                        break;
                }
                roundsOption = getSelectedRadioButtonID(((ObservableList<Node>) ((VBox) stage.getScene().lookup("#RadioGroup_Rounds_Normal")).getChildren()));
                switch (roundsOption) {
                    case ID_RADIOBUTTON_ROUND_OF_1:
                        datos.setRoundsLimit(1);
                        break;
                    case ID_RADIOBUTTON_ROUND_OF_3:
                        datos.setRoundsLimit(3);
                        break;
                    case ID_RADIOBUTTON_ROUND_OF_5:
                        datos.setRoundsLimit(5);
                        break;
                    case ID_RADIOBUTTON_ROUND_CUSTOMIZED:
                        customRounds = ((TextField) stage.getScene().lookup("#NumberRoundsCustom")).getText();
                        if (customRounds != null && !customRounds.isEmpty()) {
                            try {
                                datos.setRoundsLimit(Integer.parseInt(customRounds));
                            } catch (NumberFormatException ex) {
                                everythingOk = false;
                                excepcion.append(bundle.getString("RoundFormatFail"));
                            }
                        } else {
                            everythingOk = false;
                            excepcion.append(bundle.getString("NoRoundSetted"));
                        }
                        break;
                }
                if (!player1Name.isEmpty()) {
                    datos.setNombreJ1(((TextField) stage.getScene().lookup("#TxtFieldP1")).getText());
                } else {
                    everythingOk = false;
                    excepcion.append(bundle.getString("P1NoSetted"));
                }
                playerNumber = getSelectedRadioButtonID(((ObservableList<Node>) ((VBox) stage.getScene().lookup("#RadioGroup_Player_Normal")).getChildren()));
                if (playerNumber.equals(ID_RADIOBUTTON_2_PLAYERS)) {
                    player2Name = ((TextField) stage.getScene().lookup("#TxtFieldP2")).getText();
                    if (!player2Name.isEmpty()) {
                        datos.setNombreJ2(player2Name);
                        datos.setModalidadJuego(ModalidadJuego.DOS.ordinal());
                    } else {
                        everythingOk = false;
                        excepcion.append(bundle.getString("P2NoSetted"));
                    }
                } else {
                    datos.setNombreJ2("CPU");
                    datos.setModalidadJuego(ModalidadJuego.UNO.ordinal());
                }
                break;
            case ID_BOTON_BACK_OPCIONES_MENU_NORMAL:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuJuegoNormal.fxml"), bundle);
                datos = null;
                break;
            default:
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuOpcionesJuegoNormal.fxml"), bundle);
                break;
        }
        if (everythingOk) {
            changeSceneRoot(loader, stage);
            if (((Node) event.getSource()).getId() == ID_BOTON_PLAY_OPCIONES_MENU_NORMAL) {
                datos.setTurno(true);
                notificacionToast(datos.getNombreJ1() + bundle.getString("Turno"));
                PreferencesManager.setPreferencesNormal(roundsOption, playerNumber, gameOption, player1Name, player2Name, customRounds);
            }
        } else {
            showAlertFieldsWithExpandable(bundle, excepcion.toString());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String urlComprobar = url.getPath().substring(url.getPath().lastIndexOf("/") + 1);
//        if (comprobarGameType == null) {
//            comprobarGameType = new HashMap();
//            comprobarGameType.put(1, "FXMLJuegoGame3.fxml");
//            comprobarGameType.put(1, "FXMLJuegoGame5.fxml");
//            comprobarGameType.put(1, "FXMLJuegoGame9.fxml");
//        }
        if (datos == null) {
            datos = new DataContainer();
        }
        if (urlComprobar.equals("FXMLScores.fxml")) {
            //ENTRA EN SCORES
            List<String> list = new ArrayList<String>();
            list.add(rb.getString("VictoriesOption"));
            list.add(rb.getString("RoundsOption"));
            list.add(rb.getString("AverageOption"));
            ObservableList obList = FXCollections.observableList(list);
            cbox.getItems().clear();
            cbox.setItems(obList);
            cbox.setValue(list.get(0));
        }
        //TODO: PONER UN IF CONDICIONANDO LA VISTA DEL RESULT A EL NUMERO DE PARTIDAS JUGADAS Y TAMBIEN MOSTRANDO LAS IMAGENES CORRECTAS
        if (urlComprobar.equals("FXMLResult.fxml")) {
            comunEvaluacionGanador(datos, false, rb);
            resultImagenJ1Choosed.setImage(new Image(datos.getIdImagenPulsada1()));
            resultImagenJ2Choosed.setImage(new Image(datos.getIdImagenPulsada2()));
            resultJ1.setText(datos.getNombreJ1() + rb.getString("won") + datos.getVictoriesP1());
            resultJ2.setText(datos.getNombreJ2() + rb.getString("won") + datos.getVictoriesP2());
            if (datos.rondasFinalizadas()) {
                buttonContinueResult.setDisable(true);
            }
        }
        if (urlComprobar.equals("FXMLMenuOpcionesNormal.fxml")) {
            PreferencesManager.getPreferencesNormal(RadioGroup_Rounds_Normal.getChildren(), RadioGroup_Player_Normal.getChildren(), RadioGroup_Games_Normal.getChildren(), TxtFieldP1, TxtFieldP2, NumberRoundsCustom);
        }
        if (urlComprobar.equals("FXMLMenuOpcionesJuegoOnline.fxml")) {
            PreferencesManager.getPreferencesOnline(RadioGroup_Rounds_Online.getChildren(), RadioGroup_Games_Online.getChildren(), TxtFieldP1Online);
        }
        if (urlComprobar.equals(comprobarGameType.get(datos.getFactorAlgoritmo()))) {
            HashMap<Integer, GameType> obtenerGameTypeFromFactor = new HashMap();
            obtenerGameTypeFromFactor.put(1, GameType.JUEGO3);
            obtenerGameTypeFromFactor.put(2, GameType.JUEGO5);
            obtenerGameTypeFromFactor.put(4, GameType.JUEGO9);
            obtenerGameTypeFromFactor.put(-1, GameType.ANY);
            HashMap<Integer, RoundsNumber> obtenerNumberOfRounds = new HashMap();
            obtenerNumberOfRounds.put(1, RoundsNumber.UNA);
            obtenerNumberOfRounds.put(3, RoundsNumber.TRES);
            obtenerNumberOfRounds.put(5, RoundsNumber.CINCO);
            obtenerNumberOfRounds.put(-1, RoundsNumber.ANY);
            websocket = new MyClientEndpoint(datos);
            Player player = new Player(datos.getNombreJ1(), obtenerGameTypeFromFactor.get(datos.getFactorAlgoritmo()), obtenerNumberOfRounds.get(datos.getRoundsLimit()), false, 0);
            MetaMessage msg = new MetaMessage();
            msg.setType(TypeMessage.CONEXION);
            msg.setContent(player);
            websocket.sendMessage(msg);
        }
    }

    public static String getSelectedRadioButtonID(ObservableList<Node> lista) {
        String devuelve = "";
        for (Node nodo : lista) {
            RadioButton rb = (RadioButton) nodo;
            if (rb.isSelected()) {
                devuelve = rb.getId();
                break;
            }
        }
        return devuelve;
    }

//    private void changeSceneRoot(FXMLLoader loader, Stage stage) {
//        Parent root = null;
//        try {
//            root = loader.load();
//        } catch (IOException ex) {
//            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (root != null) {
//            stage.getScene().setRoot(root);
//        }
//    }

    private void setVisibilitiesStateMenuOpcionesOnline(Stage stage, boolean visibilityButton) {
        ObservableList<Node> nodos = ((ObservableList<Node>) ((VBox) stage.getScene().lookup("#VBoxParentOnlineOptions")).getChildren());
        for (int i = 1; i < nodos.size(); i++) {
            nodos.get(i).setVisible(!visibilityButton);
        }
        ((Button) stage.getScene().lookup("#" + ID_BOTON_RANDOMLY_OPCIONES_MENU_ONLINE)).setVisible(visibilityButton);
        ((Button) stage.getScene().lookup("#" + ID_BOTON_RANDOMLY_OPCIONES_MENU_ONLINE)).setManaged(visibilityButton);
    }

    private void showAlertFieldsWithExpandable(ResourceBundle bundle, String excepcion) {
        showAlertFields(excepcion, bundle.getString("HaveWrongFields"), bundle.getString("Warning"), bundle.getString("TheWarningsAre"));
    }

    private void notificacionToast(String mensaje) {
        ///////////////////////////////////////////
        Notification info = new Notification("", mensaje);
        // Show the custom notification
        EventHandler<NotificationEvent> handler = new EventHandler<NotificationEvent>() {

            @Override
            public void handle(NotificationEvent event) {
                ((Notifier) event.getSource()).INSTANCE.stop();
            }
        };
        Notifier.INSTANCE.setOnHideNotification(handler);
        Notifier.INSTANCE.setPopupLifetime(Duration.seconds(2));
        Notifier.INSTANCE.notify(info);
        // Show a predefined Warning notification
//        Notifier.INSTANCE.notifyWarning("Warning","This is a warning");
    }

    private void cambiaAzul(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switch (datos.getFactorAlgoritmo()) {
            case 1:
                //SET IMAGENES PARA 3 y cambiar visibilidad de los Opciones
                ((Node) stage.getScene().lookup("#ImagenJ1ChoosedG3")).setVisible(true);
                ((Node) stage.getScene().lookup("#ImagenJ2ChoosedG3")).setVisible(false);
                ((ImageView) stage.getScene().lookup("#piedra3")).setImage(new Image("imagenes/piedraazul.png"));
                ((ImageView) stage.getScene().lookup("#papel3")).setImage(new Image("imagenes/papelazul.png"));
                ((ImageView) stage.getScene().lookup("#tijera3")).setImage(new Image("imagenes/tijerasazul.png"));
                break;
            case 2:
                //SET IMAGENES PARA 5 y cambiar visibilidad de los Opciones
                ((Node) stage.getScene().lookup("#ImagenJ1ChoosedG5")).setVisible(true);
                ((Node) stage.getScene().lookup("#ImagenJ2ChoosedG5")).setVisible(false);
                ((ImageView) stage.getScene().lookup("#piedra5")).setImage(new Image("imagenes/piedraazul.png"));
                ((ImageView) stage.getScene().lookup("#papel5")).setImage(new Image("imagenes/papelazul.png"));
                ((ImageView) stage.getScene().lookup("#tijera5")).setImage(new Image("imagenes/tijerasazul.png"));
                ((ImageView) stage.getScene().lookup("#spock5")).setImage(new Image("imagenes/spockazul.png"));
                ((ImageView) stage.getScene().lookup("#lagarto5")).setImage(new Image("imagenes/lizardazul.png"));
                break;
            case 4:
                //SET IMAGENES PARA 9 y cambiar visibilidad de los Opciones
                ((Node) stage.getScene().lookup("#ImagenJ1ChoosedG9")).setVisible(true);
                ((Node) stage.getScene().lookup("#ImagenJ2ChoosedG9")).setVisible(false);
                ((ImageView) stage.getScene().lookup("#piedra9")).setImage(new Image("imagenes/piedraazul.png"));
                ((ImageView) stage.getScene().lookup("#papel9")).setImage(new Image("imagenes/papelazul.png"));
                ((ImageView) stage.getScene().lookup("#tijera9")).setImage(new Image("imagenes/tijerasazul.png"));
                ((ImageView) stage.getScene().lookup("#agua9")).setImage(new Image("imagenes/waterazul.png"));
                ((ImageView) stage.getScene().lookup("#aire9")).setImage(new Image("imagenes/windazul.png"));
                ((ImageView) stage.getScene().lookup("#pistola9")).setImage(new Image("imagenes/gunazul.png"));
                ((ImageView) stage.getScene().lookup("#humano9")).setImage(new Image("imagenes/humanazul.png"));
                ((ImageView) stage.getScene().lookup("#esponja9")).setImage(new Image("imagenes/spongeazul.png"));
                ((ImageView) stage.getScene().lookup("#fuego9")).setImage(new Image("imagenes/fireazul.png"));
                break;
        }
    }

    private void comunEvaluacionGanador(DataContainer datos, boolean online, ResourceBundle rb) {
        switch (logicaJuego(datos.getChosen2().ordinal(), datos)) {
            case 0:
                //empata
                winnerLabel.setText(rb.getString("Draw"));
                break;
            case 1:
                //gana chosen1 (Gana Jugador 1)
                winnerLabel.setText(datos.getNombreJ1() + rb.getString("wins"));
                datos.sumaVictoriasP1();
                break;
            case 2:
                //gana chosen (Gana Jugador 2)
                winnerLabel.setText(datos.getNombreJ2() + rb.getString("wins"));
                datos.sumaVictoriasP2();
                break;
        }
    }

    private int logicaJuego(int chosen, DataContainer datos) {
        int res = 0;
        if (chosen == datos.getOrdinalChosen1()) {
            //EMPATA
            res = 0;
        } else {
            boolean ganaChosen = false;
            datos.avanzaRonda();
            for (int j = ((chosen + 1) % ((datos.getFactorAlgoritmo() * 2) + 1)), i = 0; i < (datos.getFactorAlgoritmo()) && !ganaChosen; i++, j = ((j + 1) % ((datos.getFactorAlgoritmo() * 2) + 1))) {
                if (datos.getOrdinalChosen1() == j) {
                    //CHOSEN GANA
                    ganaChosen = true;
                    res = 2;
                }
            }
            if (!ganaChosen) {
                //GANA CHOSEN1
                res = 1;
            }
        }
        return res;
    }

    @FXML
    private void gestionaJuego(MouseEvent event) {
        Node nodo = (Node) event.getSource();
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
        Enum chosen = datos.getMapFichas().get(nodo.getId());
        if (datos.isTurno() && chosen != null) {
            datos.setChosen1(chosen);
            String fullURL = ((Image) ((ImageView) nodo).getImage()).impl_getUrl();
            datos.setIdImagenPulsada1(RUTA_IMAGENES.concat(fullURL.substring(fullURL.lastIndexOf("/"))));
            if (datos.getModalidadJuego() == ModalidadJuego.DOS.ordinal()) {
                cambiaAzul(event);
                datos.cambiaTurno();
                //TOSTADA INDICANDO TURNO SEGUNDO JUGADOR (CON NOMBRE DE JUGADOR)
                notificacionToast(datos.getNombreJ2() + bundle.getString("Turno"));
            } else//JUEGA MAQUINA
            {
                if (datos.getModalidadJuego() == ModalidadJuego.UNO.ordinal()) {
                    datos.setChosen2(getEnumFromOrdinal((int) (Math.random() * (((datos.getFactorAlgoritmo()) * 2) + 1)), datos));
                    datos.setIdImagenPulsada2(datos.getMapFichasMaquina().get(datos.getChosen2().toString()));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLResult.fxml"), bundle);
                    changeSceneRoot(loader, stage);

                    //comunEvaluacionGanador(datos.getChosen2(), datos, false);
                    //datos.setJugando(false);
                    //datos.setIdImagenPulsada2(gestionaPulsadoMaquina(datos.getChosen2(), datos));
                    /*((ImageView) activity.findViewById(R.id.player2Muestra)).setImageResource(datos.getIdImagenPulsada2());//Posiblemente para borrar, pasar al onload de la vista de RESULT*/
                } else {
                    //JUEGO ONLINE
                    MetaMessage msg = new MetaMessage();
                    msg.setType(TypeMessage.PARTIDA);
                    OpcionJuego oj = new OpcionJuego();
                    oj.setOpcion(datos.getChosen1().ordinal());
                    if (datos.getChosen2() != null) {
                        //((ImageView) activity.findViewById(R.id.player2Muestra)).setImageResource(datos.getIdImagenPulsada2());
                        //comunEvaluacionGanador(datos.getChosen2(), false, activity, datos, true);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLResult.fxml"), bundle);
                        changeSceneRoot(loader, stage);
                    }
                    msg.setContent(oj);
                    websocket.sendMessage(msg);
                }
            }
        } else if (!datos.isTurno() && chosen != null) {
            datos.setChosen2(chosen);
            String fullURL = ((Image) ((ImageView) nodo).getImage()).impl_getUrl();
            datos.setIdImagenPulsada2(RUTA_IMAGENES.concat(fullURL.substring(fullURL.lastIndexOf("/"))));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLResult.fxml"), bundle);
            changeSceneRoot(loader, stage);
            datos.cambiaTurno();
            //cambiaRojo(activity, datos);
            //datos.setJugando(false);
            //((ImageView) activity.findViewById(R.id.player2Muestra)).setImageResource(datos.getIdImagenPulsada2());
//         comunEvaluacionGanador(datos.getChosen2(), datos, false);
//         datos.cambiaTurno();
        } //        return msg;
    }

    @FXML
    private void gestionaResultButtons(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = null;
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
        if (((Button) event.getSource()).getText().equals(bundle.getString("Continue"))) {
            switch (datos.getFactorAlgoritmo()) {
                case 1:
                    loader = new FXMLLoader(getClass().getResource("/fxml/FXMLJuegoGame3.fxml"), bundle);
                    break;
                case 2:
                    loader = new FXMLLoader(getClass().getResource("/fxml/FXMLJuegoGame5.fxml"), bundle);
                    break;
                case 4:
                    loader = new FXMLLoader(getClass().getResource("/fxml/FXMLJuegoGame9.fxml"), bundle);
                    break;
            }
        } else {
            if (datos.getModalidadJuego() != ModalidadJuego.ONLINE.ordinal()) {
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuJuegoNormal.fxml"), bundle);
            } else {
                loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuJuegoOnline.fxml"), bundle);
            }
            datos.setValoresIniciales();
        }
        changeSceneRoot(loader, stage);
    }
}
