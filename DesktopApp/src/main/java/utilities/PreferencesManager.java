/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import static constantes.ConstantesIdentificadores.*;
import static constantes.ConstantesStrings.*;
import java.util.prefs.Preferences;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import modelo.DataContainer;
import static constantes.conexion.ConstantesConexion.*;
import vista.FXMLController;
import static vista.FXMLController.getDatos;
import static vista.FXMLController.setLogueado;

/**
 * Gestiona las preferencias o ultimos usos de configuraciones relativas a las
 * partidas para recargarlas tal y como se aceptaron por ultima vez
 * @author Victor e Ivan
 */
public class PreferencesManager {

    /**
     * Guarda los valores de elección del jugador cuando el usuario se encuentra
     * en el menu Opciones Normal
     * @param roundsOption radioButton seleccionado relativo al numero de rondas
     * @param playerNumber radioButton seleccionado relativo al numero de jugadores
     * @param gameOption radioButton seleccionado relativo al tipo de juego
     * @param player1Name textField con el nombre del Jugador 1
     * @param player2Name textField con el nombre del Jugador 2
     * @param customRounds  textField con el numero de rondas (personalizado)
     */
    public static void setPreferencesNormal(String roundsOption, String playerNumber, String gameOption, String player1Name, String player2Name, String customRounds) {
        Preferences prefs = Preferences.userNodeForPackage(FXMLController.class);
        prefs.put(PREFERENCIAS_RONDAS_NORMAL, roundsOption);
        prefs.put(PREFERENCIAS_NUMERO_JUGADORES_NORMAL, playerNumber);
        prefs.put(PREFERENCIAS_JUEGOS_NORMAL, gameOption);
        prefs.put(PREFERENCIAS_NOMBRE_JUGADOR1_NORMAL, player1Name);
        if (playerNumber.equals(ID_RADIOBUTTON_2_PLAYERS)) {
            prefs.put(PREFERENCIAS_NOMBRE_JUGADOR2_NORMAL, player2Name);
        }
        if (roundsOption.equals(ID_RADIOBUTTON_ROUND_CUSTOMIZED)) {
            prefs.put(PREFERENCIAS_RONDAS_PERSONALIZADAS_NORMAL, customRounds);
        }
    }
    
    /**
     * Carga los valores de elección del jugador cuando el usuario se encuentra
     * en el menu Opciones Normal
     * @param nodoRound radioButtons elegibles que determinan el numero de rondas
     * @param nodoPlayers radioButtons elegibles que determinan el numero de jugadores
     * @param nodoGame radioButtons elegibles que determinan el tipo de juego
     * @param player1 textField que registra al Jugador 1
     * @param player2 textField que registra al Jugador 2
     * @param roundCustomed textField que registra el numero de rondas personalizadas
     */
    public static void getPreferencesNormal(ObservableList<Node> nodoRound, ObservableList<Node> nodoPlayers, ObservableList<Node> nodoGame, TextField player1, TextField player2, TextField roundCustomed) {
        Preferences prefs = Preferences.userNodeForPackage(FXMLController.class);
        String roundsOption, playerNumber;
        roundsOption = prefs.get(PREFERENCIAS_RONDAS_NORMAL, "");
        changeStateRadioButton(nodoRound, roundsOption);
        playerNumber = prefs.get(PREFERENCIAS_NUMERO_JUGADORES_NORMAL, "");
        changeStateRadioButton(nodoPlayers, playerNumber);
        changeStateRadioButton(nodoGame, prefs.get(PREFERENCIAS_JUEGOS_NORMAL, ""));
        player1.setText(prefs.get(PREFERENCIAS_NOMBRE_JUGADOR1_NORMAL, ""));
        player2.setText(prefs.get(PREFERENCIAS_NOMBRE_JUGADOR2_NORMAL, ""));
        if (playerNumber.equals(ID_RADIOBUTTON_2_PLAYERS)) {
            player2.setManaged(true);
            player2.setVisible(true);
            player1.setPrefWidth(230.0);
        }
        roundCustomed.setText(prefs.get(PREFERENCIAS_RONDAS_PERSONALIZADAS_NORMAL, ""));
        if (roundsOption.equals(ID_RADIOBUTTON_ROUND_CUSTOMIZED)) {
            roundCustomed.setManaged(true);
            roundCustomed.setVisible(true);
        }
    }
    
    /**
     * Gestiona el cambio de los botones cuando la opcion elegida requiere una
     * variacion en la seleccion del radioButton (nodo)
     * @param nodos radioButtons posibles de seleccion
     * @param valueToSet id del radioButton que debe ser seleccionado
     */
    private static void changeStateRadioButton(ObservableList<Node> nodos, String valueToSet){
        for(int i=0;i<nodos.size();i++){
            RadioButton button=(RadioButton)nodos.get(i);
            if(button.getId().equals(valueToSet)){
                i=nodos.size();
                button.setSelected(true);
            }
        }
    }

    /**
     * Guarda los valores de elección del jugador cuando el usuario se encuentra
     * en el menu Opciones Online
     * @param roundsOption radioButton seleccionado relativo al numero de rondas
     * @param gameOption radioButton seleccionado relativo al tipo de juego
     * @param player1Name textField con el nombre del Jugador 1 (local)
     */
    public static void setPreferencesOnline(String roundsOption, String gameOption/*, String player1Name*/) {
        Preferences prefs = Preferences.userNodeForPackage(FXMLController.class);
        prefs.put(PREFERENCIAS_RONDAS_ONLINE, roundsOption);
        prefs.put(PREFERENCIAS_JUEGOS_ONLINE, gameOption);
    }

    /**
     * Carga los valores de elección del jugador cuando el usuario se encuentra
     * en el menu Opciones Online
     * @param nodoRound radioButtons de Rondas que pueden ser seleccionados
     * @param nodoGame radioButtons de Tipo Juego que pueden ser seleccionados
     * @param player1 valor a poner en el textField que refiere al jugador 1 (local)
     */
    public static void getPreferencesOnline(ObservableList<Node> nodoRound, ObservableList<Node> nodoGame/*, TextField player1*/) {
        Preferences prefs = Preferences.userNodeForPackage(FXMLController.class);
        changeStateRadioButton(nodoRound, prefs.get(PREFERENCIAS_RONDAS_ONLINE, ""));
        changeStateRadioButton(nodoGame, prefs.get(PREFERENCIAS_JUEGOS_ONLINE, ""));
    }

    public static void setPreferencesLogin(boolean accepted, String nombre){
        Preferences prefs = Preferences.userNodeForPackage(FXMLController.class);
        prefs.putBoolean(PREFERENCIAS_CONECTADO, accepted);
        prefs.put(PREFERENCIAS_NOMBRE_CONECTADO, nombre);
    }
    
    public static void getPreferencesLogin(){
        Preferences prefs = Preferences.userNodeForPackage(FXMLController.class);
        setLogueado(prefs.getBoolean(PREFERENCIAS_CONECTADO, false));
        getDatos().setNombreJ1(prefs.get(PREFERENCIAS_NOMBRE_CONECTADO, ""));
    }
}
