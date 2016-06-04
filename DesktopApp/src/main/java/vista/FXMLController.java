/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import com.mycompany.datapptgame.MetaMessage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import static constantes.ConstantesIdentificadores.*;
import eu.hansolo.enzo.notification.Notification;
import eu.hansolo.enzo.notification.Notification.Notifier;
import eu.hansolo.enzo.notification.NotificationEvent;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.DataContainer;
import com.mycompany.datapptgame.GameType;
import com.mycompany.datapptgame.ModalidadJuego;
import com.mycompany.datapptgame.OpcionJuego;
import com.mycompany.datapptgame.Player;
import com.mycompany.datapptgame.Result;
import com.mycompany.datapptgame.RoundsNumber;
import com.mycompany.datapptgame.TypeMessage;
import static constantes.ConstantesCadenasLookup.*;
import static constantes.ConstantesStrings.*;
import java.util.HashMap;
import utilities.PreferencesManager;
import javax.websocket.ClientEndpoint;
import utilities.MyClientEndpoint;
import static utilities.UtilidadesJavaFX.*;
import static vista.DesktopApp.getStage;

/**
 * Controlador de la interfaz grafica (eventos) datos conserva todos los datos
 * importantes para la funcionalidad de la aplicacion, especialmente los
 * resultados, configuraciones y caracteristicas relativas a la funcionalidad de
 * la partida/s websocket: se encarga de mantener las propiedades relativas a la
 * conexion de websockets segundoMensaje: encargado de la gestion (online) del
 * transito entre jugada y jugada, controlando el boton continue del menu de
 * resultado, que en caso de que esta variable valga false determina que puede
 * poner los valores a su estado inicial, si es true, mantendrá el valor de
 * chosen2 (jugador ajeno). Esto sirve para controlar que si el otro usuario ha
 * mandado ya su opcion, no borre dicha opcion y pueda determinar el resultado
 *
 * @author Victor e Ivan
 */
@ClientEndpoint
public class FXMLController implements Initializable {

    private static DataContainer datos;
    private static boolean logueado;
    private final String RUTA_IMAGENES = "imagenes";
    private static MyClientEndpoint websocket;
    private static boolean segundoMensaje;
    private static HashMap<Integer, String> comprobarGameType = new HashMap() {
        {
            put(1, "FXMLJuegoGame3.fxml");
            put(2, "FXMLJuegoGame5.fxml");
            put(4, "FXMLJuegoGame9.fxml");
        }
    };

    @FXML
    private Label resultJ1;
    @FXML
    private Label resultJ2;
    @FXML
    private Label winnerLabel;
    @FXML
    private Label putLoginName;
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

    /**
     * Gestiona los eventos del menu principal (los botones), tomará diferentes
     * acciones dependiendo del id del boton que lance el evento
     *
     * @param event utilizado para gestionar la obtencion del id del boton
     * pulsado y manejar los cambios de escena requerido
     */
    @FXML
    private void handleButtonsMenuPrincipalAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = null;
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
        switch (((Button) event.getSource()).getId()) {
            case ID_BOTON_PLAY_MENU_PRINCIPAL:
                loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_OPCIONES_NORMAL), bundle);
                break;
            case ID_BOTON_PLAY_ONLINE_MENU_PRINCIPAL:
                PreferencesManager.getPreferencesLogin();
                if (logueado) {
                    loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_ONLINE), bundle);
                } else {
                    loader = new FXMLLoader(getClass().getResource(ESCENA_LOGIN), bundle);
                }
                break;
            case ID_BOTON_RULES_MENU_PRINCIPAL:
                loader = new FXMLLoader(getClass().getResource(ESCENA_REGLAS), bundle);
                break;
            case ID_BOTON_RULES_GRAPHICALLY_MENU_PRINCIPAL:
                loader = new FXMLLoader(getClass().getResource(ESCENA_REGLAS_GRAFICAS), bundle);
                break;
            case ID_BOTON_DEVELOPERS_MENU_PRINCIPAL:
                loader = new FXMLLoader(getClass().getResource(ESCENA_DEVELOPER), bundle);
                break;
            default:
                loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_PPAL), bundle);
                break;
        }
        changeSceneRoot(loader, stage);
    }

    /**
     * Gestiona los eventos del boton back del los menus: Reglas, Reglas
     * graficas, Desarrolladores y Puntuaciones. Se gestionan en un solo metodo
     * ya que la funcionalidad es la misma
     *
     * @param event utilizado para gestionar la obtencion del id del boton
     * pulsado y manejar los cambios de escena requerido
     */
    @FXML
    private void handleButtonBackAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_PPAL), bundle);
        changeSceneRoot(loader, stage);
    }

    /**
     * Gestiona los eventos del menu principal (los botones), tomará diferentes
     * acciones dependiendo del id del boton que lance el evento
     *
     * @param event utilizado para gestionar la obtencion del id del boton
     * pulsado y manejar los cambios de escena requerido
     */
    @FXML
    private void handleButtonsMenuOnlineAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = null;
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
        switch (((Button) event.getSource()).getId()) {
            case ID_BOTON_PLAY_MENU_JUEGO_ONLINE:
                loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_OPCIONES_ONLINE), bundle);
                break;
            case ID_BOTON_SCORES_MENU_JUEGO_ONLINE:
                loader = new FXMLLoader(getClass().getResource(ESCENA_PUNTUACIONES), bundle);
                break;
            case ID_BOTON_BACK_MENU_JUEGO_ONLINE:
                loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_PPAL), bundle);
                break;
            default:
                loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_ONLINE), bundle);
                break;
        }
        changeSceneRoot(loader, stage);
    }

    /**
     * Gestiona el evento de cambio de valor de un radioButton si éste debiera
     * de generar ese cambio
     *
     * @param event
     */
    @FXML
    private void handleRadioButtonsAction(ActionEvent event) {
        String rb = ((Node) event.getSource()).getId();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        TextField txtField, txtField1, txtFieldRounds;
        switch (rb) {
            case ID_RADIOBUTTON_1_PLAYERS:
                txtField1 = (TextField) stage.getScene().lookup(CAMPO_JUGADOR_1);
                txtField = (TextField) stage.getScene().lookup(CAMPO_JUGADOR_2);
                txtField.setManaged(false);
                txtField.setVisible(false);
                txtField1.setPrefWidth(490.0);
                break;
            case ID_RADIOBUTTON_2_PLAYERS:
                txtField1 = (TextField) stage.getScene().lookup(CAMPO_JUGADOR_1);
                txtField = (TextField) stage.getScene().lookup(CAMPO_JUGADOR_2);
                txtField.setManaged(true);
                txtField.setVisible(true);
                txtField1.setPrefWidth(230.0);
                break;
            case ID_RADIOBUTTON_ROUND_CUSTOMIZED:
                txtFieldRounds = (TextField) stage.getScene().lookup(CAMPO_NUMERO_RONDAS_PERSONALIZADAS);
                txtFieldRounds.setManaged(true);
                txtFieldRounds.setVisible(true);
                break;
            default:
                txtFieldRounds = (TextField) stage.getScene().lookup(CAMPO_NUMERO_RONDAS_PERSONALIZADAS);
                txtFieldRounds.setManaged(false);
                txtFieldRounds.setVisible(false);
                break;
        }
    }

    /**
     * Gestiona las ejecuciones dependiendo del boton pulsado en el menú de
     * Opciones en el juego Online
     *
     * @param event
     */
    @FXML
    private void handleButtonsMenuOpcionesJuegoOnlineAction(ActionEvent event) {
        boolean cargarPantalla = true;
        String roundsOption = "", gameOption = "", player1Name = "";
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String boton = ((Button) event.getSource()).getId();
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
        FXMLLoader loader = null;
        if (boton.equals(ID_BOTON_PLAY_OPCIONES_MENU_ONLINE)) {
            if (((Button) stage.getScene().lookup("#" + ID_BOTON_RANDOMLY_OPCIONES_MENU_ONLINE)).isVisible()) {
                setVisibilitiesStateMenuOpcionesOnline(stage, false);
                cargarPantalla = false;
            } else {
                gameOption = getSelectedRadioButtonID(((ObservableList<Node>) ((VBox) stage.getScene().lookup(RADIOGROUP_JUEGOS_ONLINE)).getChildren()));
                switch (gameOption) {
                    case ID_RADIOBUTTON_GAME_OF_3:
                        loader = new FXMLLoader(getClass().getResource(ESCENA_JUEGO3), bundle);
                        datos.setFactorAlgoritmo(1);
                        break;
                    case ID_RADIOBUTTON_GAME_OF_5:
                        loader = new FXMLLoader(getClass().getResource(ESCENA_JUEGO5), bundle);
                        datos.setFactorAlgoritmo(2);
                        break;
                    case ID_RADIOBUTTON_GAME_OF_9:
                        loader = new FXMLLoader(getClass().getResource(ESCENA_JUEGO9), bundle);
                        datos.setFactorAlgoritmo(4);
                        break;
                }
                roundsOption = getSelectedRadioButtonID(((ObservableList<Node>) ((VBox) stage.getScene().lookup(RADIOGROUP_RONDAS_ONLINE)).getChildren()));
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
            }
        } else if (boton.equals(ID_BOTON_RANDOMLY_OPCIONES_MENU_ONLINE)) {
            //MAKE RANDOMLY THE SETTING OF THE GAME
            websocket=conectaWebsocket(datos, GameType.ANY, RoundsNumber.ANY);
        } else//BACK
         if (!((Button) stage.getScene().lookup("#" + ID_BOTON_RANDOMLY_OPCIONES_MENU_ONLINE)).isVisible()) {
                setVisibilitiesStateMenuOpcionesOnline(stage, true);
                cargarPantalla = false;
            } else {
                loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_ONLINE), bundle);
                datos = null;
            }
        if (cargarPantalla) {
            if (datos != null) {
                datos.setTurno(true);
                datos.setModalidadJuego(ModalidadJuego.ONLINE.ordinal());
            }
            PreferencesManager.setPreferencesOnline(roundsOption, gameOption/*, player1Name*/);
            changeSceneRoot(loader, stage);
        }
    }

    /**
     * Gestiona las ejecuciones segun el boton pulsado en el el Menú de Opciones
     * de Juego en Modo Local
     *
     * @param event
     */
    @FXML
    private void handleButtonsMenuOpcionesJuegoNormalAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String roundsOption = "", playerNumber = "", gameOption = "", player1Name = "", player2Name = "", customRounds = "";
        StringBuilder excepcion = new StringBuilder();
        boolean everythingOk = true;
        FXMLLoader loader = null;
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
        switch (((Node) event.getSource()).getId()) {
            case ID_BOTON_PLAY_OPCIONES_MENU_NORMAL:
                player1Name = ((TextField) stage.getScene().lookup(CAMPO_JUGADOR_1)).getText();
                gameOption = getSelectedRadioButtonID(((ObservableList<Node>) ((VBox) stage.getScene().lookup(RADIOGROUP_JUEGOS_NORMAL)).getChildren()));
                switch (gameOption) {
                    case ID_RADIOBUTTON_GAME_OF_3:
                        loader = new FXMLLoader(getClass().getResource(ESCENA_JUEGO3), bundle);
                        datos.setFactorAlgoritmo(1);
                        break;
                    case ID_RADIOBUTTON_GAME_OF_5:
                        loader = new FXMLLoader(getClass().getResource(ESCENA_JUEGO5), bundle);
                        datos.setFactorAlgoritmo(2);
                        break;
                    case ID_RADIOBUTTON_GAME_OF_9:
                        loader = new FXMLLoader(getClass().getResource(ESCENA_JUEGO9), bundle);
                        datos.setFactorAlgoritmo(4);
                        break;
                }
                roundsOption = getSelectedRadioButtonID(((ObservableList<Node>) ((VBox) stage.getScene().lookup(RADIOGROUP_RONDAS_NORMAL)).getChildren()));
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
                        customRounds = ((TextField) stage.getScene().lookup(CAMPO_NUMERO_RONDAS_PERSONALIZADAS)).getText();
                        if (customRounds != null && !customRounds.isEmpty()) {
                            try {
                                datos.setRoundsLimit(Integer.parseInt(customRounds));
                            } catch (NumberFormatException ex) {
                                everythingOk = false;
                                excepcion.append(bundle.getString(ROUND_FORMAT_FAILURE));
                            }
                        } else {
                            everythingOk = false;
                            excepcion.append(bundle.getString("NoRoundSetted"));
                        }
                        break;
                }
                if (!player1Name.isEmpty()) {
                    datos.setNombreJ1(((TextField) stage.getScene().lookup(CAMPO_JUGADOR_1)).getText());
                } else {
                    everythingOk = false;
                    excepcion.append(bundle.getString(JUGADOR1_NO_ESCRITO));
                }
                playerNumber = getSelectedRadioButtonID(((ObservableList<Node>) ((VBox) stage.getScene().lookup(RADIOGROUP_JUGADORES_NORMAL)).getChildren()));
                if (playerNumber.equals(ID_RADIOBUTTON_2_PLAYERS)) {
                    player2Name = ((TextField) stage.getScene().lookup(CAMPO_JUGADOR_2)).getText();
                    if (!player2Name.isEmpty()) {
                        datos.setNombreJ2(player2Name);
                        datos.setModalidadJuego(ModalidadJuego.DOS.ordinal());
                    } else {
                        everythingOk = false;
                        excepcion.append(bundle.getString(JUGADOR2_NO_ESCRITO));
                    }
                } else {
                    datos.setNombreJ2(MACHINE_NAME);
                    datos.setModalidadJuego(ModalidadJuego.UNO.ordinal());
                }
                break;
            case ID_BOTON_BACK_OPCIONES_MENU_NORMAL:
                loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_PPAL), bundle);
                datos = null;
                break;
            default:
                loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_OPCIONES_NORMAL), bundle);
                break;
        }
        if (everythingOk) {
            changeSceneRoot(loader, stage);
            if (((Node) event.getSource()).getId() == ID_BOTON_PLAY_OPCIONES_MENU_NORMAL) {
                datos.setTurno(true);
                notificacionToast(bundle.getString(TURNO).replace("X", datos.getNombreJ1()));
                PreferencesManager.setPreferencesNormal(roundsOption, playerNumber, gameOption, player1Name, player2Name, customRounds);
            }
        } else {
            showAlertFieldsWithExpandable(bundle, excepcion.toString());
        }
    }

    /**
     * Metodo que gestiona la precarga de la escena y donde se comprueba
     * diferentes estados y condiciones para adaptar
     * comportamientos/visualizaciones en esa nueva pantalla
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String urlComprobar = url.getPath().substring(url.getPath().lastIndexOf("/") + 1);
        if (datos == null) {
            datos = new DataContainer();
        }
        if (urlComprobar.equals("FXMLResult.fxml")) {
            comunEvaluacionGanador(datos, rb);
            resultImagenJ1Choosed.setImage(new Image(datos.getIdImagenPulsada1()));
            resultImagenJ2Choosed.setImage(new Image(datos.getIdImagenPulsada2()));
            resultJ1.setText(datos.getNombreJ1() + rb.getString(WON) + datos.getVictoriesP1());
            resultJ2.setText(datos.getNombreJ2() + rb.getString(WON) + datos.getVictoriesP2());
            if (datos.rondasFinalizadas()) {
                buttonContinueResult.setDisable(true);
            }
        }
        if (urlComprobar.equals("FXMLMenuOpcionesNormal.fxml")) {
            PreferencesManager.getPreferencesNormal(RadioGroup_Rounds_Normal.getChildren(), RadioGroup_Player_Normal.getChildren(), RadioGroup_Games_Normal.getChildren(), TxtFieldP1, TxtFieldP2, NumberRoundsCustom);
        }
        if (urlComprobar.equals("FXMLMenuJuegoOnline.fxml")) {
            putLoginName.setText(datos.getNombreJ1());
        }
        if (urlComprobar.equals("FXMLMenuOpcionesJuegoOnline.fxml")) {
            putLoginName.setText(datos.getNombreJ1());
            PreferencesManager.getPreferencesOnline(RadioGroup_Rounds_Online.getChildren(), RadioGroup_Games_Online.getChildren(), TxtFieldP1Online);
        }
        System.out.println("modalidad juego: " + datos.getModalidadJuego() + " y online vale: " + ModalidadJuego.ONLINE.ordinal());
        if ((websocket == null || (websocket!=null && !websocket.isOpen())) && urlComprobar.equals(comprobarGameType.get(datos.getFactorAlgoritmo())) && datos.getRoundsCounter() == 0 && datos.getModalidadJuego() == ModalidadJuego.ONLINE.ordinal()) {
            System.out.println("ENTRO EN LA CREACION DEL WEBSOCKET EN INITIALIZE");
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
            websocket=conectaWebsocket(datos, obtenerGameTypeFromFactor.get(datos.getFactorAlgoritmo()), obtenerNumberOfRounds.get(datos.getRoundsLimit()));
            System.out.println("WEBSOCKET AHORA VALE: "+websocket);
        }

    }

    public static DataContainer getDatos() {
        return datos;
    }

    public static boolean getLogueado() {
        return logueado;
    }

    public static void changeLogueado() {
        logueado = !logueado;
    }

    public static void setLogueado(boolean nuevo) {
        logueado = nuevo;
    }

    /**
     * Obtiene a partir de una lista de nodos (RadioButtons), cual de los radios
     * están checkeados y devuelve su ID
     *
     * @param lista
     * @return
     */
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

    /**
     * Gestiona la visualizacion de nodos
     *
     * @param stage
     * @param visibilityButton
     */
    private void setVisibilitiesStateMenuOpcionesOnline(Stage stage, boolean visibilityButton) {
        ObservableList<Node> nodos = ((ObservableList<Node>) ((VBox) stage.getScene().lookup(VERTICAL_BOX_OPCIONES_ONLINE)).getChildren());
        for (int i = 1; i < nodos.size(); i++) {
            nodos.get(i).setVisible(!visibilityButton);
        }
        ((Button) stage.getScene().lookup("#" + ID_BOTON_RANDOMLY_OPCIONES_MENU_ONLINE)).setVisible(visibilityButton);
        ((Button) stage.getScene().lookup("#" + ID_BOTON_RANDOMLY_OPCIONES_MENU_ONLINE)).setManaged(visibilityButton);
    }

    /**
     * Llama al showAlertFields, asignandole ciertos valores a los diferentes
     * campos
     *
     * @param bundle
     * @param excepcion
     */
    private void showAlertFieldsWithExpandable(ResourceBundle bundle, String excepcion) {
        showAlertFields(excepcion, bundle.getString(WRONG_FIELDS), bundle.getString(WARNING), bundle.getString(WARNING_ARE));
    }

    /**
     * Genera un toast con el nombre del jugador al que le corresponde el turno
     *
     * @param mensaje
     */
    private void notificacionToast(String mensaje) {
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
    }

    /**
     * Gestiona el cambio de color de los iconos de opciones del juego a azul,
     * indicando así que el turno corresponde al segundo jugador
     *
     * @param event
     */
    private void cambiaAzul(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switch (datos.getFactorAlgoritmo()) {
            case 1:
                //SET IMAGENES PARA 3 y cambiar visibilidad de los Opciones
                ((Node) stage.getScene().lookup(IMAGEN_ELEGIDA_JUGADOR_1_JUEGO_3)).setVisible(true);
                ((Node) stage.getScene().lookup(IMAGEN_ELEGIDA_JUGADOR_2_JUEGO_3)).setVisible(false);
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_PIEDRA_3)).setImage(new Image(IMAGEN_PIEDRA_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_PAPEL_3)).setImage(new Image(IMAGEN_PAPEL_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_TIJERA_3)).setImage(new Image(IMAGEN_TIJERA_AZUL));
                break;
            case 2:
                //SET IMAGENES PARA 5 y cambiar visibilidad de los Opciones
                ((Node) stage.getScene().lookup(IMAGEN_ELEGIDA_JUGADOR_1_JUEGO_5)).setVisible(true);
                ((Node) stage.getScene().lookup(IMAGEN_ELEGIDA_JUGADOR_2_JUEGO_5)).setVisible(false);
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_PIEDRA_5)).setImage(new Image(IMAGEN_PIEDRA_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_PAPEL_5)).setImage(new Image(IMAGEN_PAPEL_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_TIJERA_5)).setImage(new Image(IMAGEN_TIJERA_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_SPOCK)).setImage(new Image(IMAGEN_SPOCK_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_LAGARTO)).setImage(new Image(IMAGEN_LAGARTO_AZUL));
                break;
            case 4:
                //SET IMAGENES PARA 9 y cambiar visibilidad de los Opciones
                ((Node) stage.getScene().lookup(IMAGEN_ELEGIDA_JUGADOR_1_JUEGO_9)).setVisible(true);
                ((Node) stage.getScene().lookup(IMAGEN_ELEGIDA_JUGADOR_2_JUEGO_9)).setVisible(false);
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_PIEDRA_9)).setImage(new Image(IMAGEN_PIEDRA_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_PAPEL_5)).setImage(new Image(IMAGEN_PAPEL_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_TIJERA_5)).setImage(new Image(IMAGEN_TIJERA_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_AGUA)).setImage(new Image(IMAGEN_AGUA_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_VIENTO)).setImage(new Image(IMAGEN_VIENTO_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_ARMA)).setImage(new Image(IMAGEN_ARMA_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_HUMANO)).setImage(new Image(IMAGEN_HUMANO_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_ESPONJA)).setImage(new Image(IMAGEN_ESPONJA_AZUL));
                ((ImageView) stage.getScene().lookup(IMAGENBUTTON_FUEGO)).setImage(new Image(IMAGEN_FUEGO_AZUL));
                break;
        }
    }

    /**
     * Gestiona la funcionalidad de evaluación del resultado de la partida e
     * imprime los cambios requeridos a mostrar en la pantalla de FXMLResult,
     * además de sumar victoria si corresponde
     *
     * @param datos
     * @param online
     * @param rb
     */
    private void comunEvaluacionGanador(DataContainer datos, ResourceBundle rb) {
        switch (logicaJuego(datos.getChosen2().ordinal(), datos, false)) {
            case 0:
                //empata
                winnerLabel.setText(rb.getString(DRAW));
                break;
            case 1:
                //gana chosen1 (Gana Jugador 1)
                winnerLabel.setText(datos.getNombreJ1() + rb.getString(WINS));
                datos.sumaVictoriasP1();
                break;
            case 2:
                //gana chosen (Gana Jugador 2)
                winnerLabel.setText(datos.getNombreJ2() + rb.getString(WINS));
                datos.sumaVictoriasP2();
                break;
        }
    }

    /**
     * Devuelve el resultado del juego
     *
     * @param chosen
     * @param datos
     * @return
     */
    private int logicaJuego(int chosen, DataContainer datos, boolean soloEvalua) {
        int res = 1;//Se cambia de 0 a 1 para evitar el if posterior
        if (chosen == datos.getOrdinalChosen1()) {
            //EMPATA
            res = 0;
        } else {
            boolean ganaChosen = false;
            if (!soloEvalua) {
                datos.avanzaRonda();
            }
            for (int j = ((chosen + 1) % ((datos.getFactorAlgoritmo() * 2) + 1)), i = 0; i < (datos.getFactorAlgoritmo()) && !ganaChosen; i++, j = ((j + 1) % ((datos.getFactorAlgoritmo() * 2) + 1))) {
                if (datos.getOrdinalChosen1() == j) {
                    //CHOSEN GANA
                    ganaChosen = true;
                    res = 2;
                }
            }
        }
        return res;
    }

    /**
     * Gestiona la pulsion sobre una de las opciones (piedra, papel, tijera...)
     * en el juego
     *
     * @param event
     */
    @FXML
    private void gestionaJuego(MouseEvent event) {
        Node nodo = (Node) event.getSource();
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
        Enum chosen = datos.getMapFichas().get(nodo.getId());
        if (datos.isTurno() && chosen != null) {
            datos.setChosen1(chosen);
            String fullURL = ((Image) ((ImageView) nodo).getImage()).impl_getUrl();
            datos.setIdImagenPulsada1(RUTA_IMAGENES.concat(fullURL.substring(fullURL.lastIndexOf("/"))));
            if (datos.getModalidadJuego() == ModalidadJuego.DOS.ordinal()) {
                cambiaAzul(event);
                datos.cambiaTurno();
                //TOSTADA INDICANDO TURNO SEGUNDO JUGADOR (CON NOMBRE DE JUGADOR)
                notificacionToast(bundle.getString(TURNO).replace("X", datos.getNombreJ2()));
            } else//JUEGA MAQUINA O ONLINE
            {
                if (datos.getModalidadJuego() == ModalidadJuego.UNO.ordinal()) {
                    datos.setChosen2(getEnumFromOrdinal((int) (Math.random() * (((datos.getFactorAlgoritmo()) * 2) + 1)), datos));
                    datos.setIdImagenPulsada2(datos.getMapFichasMaquina().get(datos.getChosen2().toString()));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(ESCENA_RESULTADOS), bundle);
                    changeSceneRoot(loader, stage);
                } else {
                    //JUEGO ONLINE
                    MetaMessage msg = new MetaMessage();
                    msg.setType(TypeMessage.PARTIDA);
                    OpcionJuego oj = new OpcionJuego();
                    oj.setOpcion(datos.getChosen1().ordinal());
                    if (datos.getChosen2() != null) {
                        int res = logicaJuego(datos.getChosen2().ordinal(), datos, true);
                        oj.setResult(res == 0 ? Result.EMPATA : (res == 1 ? Result.GANA : Result.PIERDE));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(ESCENA_RESULTADOS), bundle);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ESCENA_RESULTADOS), bundle);
            changeSceneRoot(loader, stage);
            datos.cambiaTurno();
        }
    }

    /**
     * Gestiona los botones del FXMLResult
     *
     * @param event
     */
    @FXML
    private void gestionaResultButtons(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = null;
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
        if (((Button) event.getSource()).getText().equals(bundle.getString(CONTINUE))) {
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
            if (datos.getModalidadJuego() == ModalidadJuego.ONLINE.ordinal() && !segundoMensaje) {
                datos.setChosen2(null);
                datos.setChosen1(null);
            } else if (segundoMensaje) {
                cambiaSegundoMensaje();
            }
            notificacionToast(bundle.getString(TURNO).replace("X", datos.getNombreJ1()));
        } else {
            loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_PPAL), bundle);
            if (datos.getModalidadJuego() == ModalidadJuego.ONLINE.ordinal()) {
                websocket.closeWebsocket();
            }
            datos.setValoresIniciales();
        }
        changeSceneRoot(loader, stage);
    }

    /**
     * Gestiona el cambio de valor de la variable encargada de notificar si ha
     * habido comunicacion previa del usuario online, para evitar errores por
     * borrar el contenido de la elección del jugador online conservando dicha
     * elección
     */
    public static void cambiaSegundoMensaje() {
        segundoMensaje = !segundoMensaje;
    }

    @FXML
    private void handleOnMouseOver(MouseEvent event) {
        gestionPunteroRatonOver();
    }

    @FXML
    private void handleOnMouseOut(MouseEvent event) {
        gestionPunteroRatonOut();
    }

    @FXML
    private void handleCloseSession(MouseEvent event) {
        PreferencesManager.setPreferencesLogin(false, "");
        logueado = false;
        datos.setNombreJ1("");
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
        FXMLLoader loader = loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_PPAL), bundle);
        changeSceneRoot(loader, getStage());
    }
}
