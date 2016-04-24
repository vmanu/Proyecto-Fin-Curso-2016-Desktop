/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import static constantes.Constantes.*;
import java.util.prefs.Preferences;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vista.FXMLController;
import static vista.FXMLController.getSelectedRadioButtonID;

/**
 *
 * @author Victor
 */
public class PreferencesManager {

    public static void setPreferencesNormal(String roundsOption, String playerNumber, String gameOption, String player1Name, String player2Name, String customRounds) {
        Preferences prefs = Preferences.userNodeForPackage(FXMLController.class);
        prefs.put("roundsNormal", roundsOption);
        prefs.put("playerNumber", playerNumber);
        prefs.put("GameOptionNormal", gameOption);
        prefs.put("playerJ1Name", player1Name);
        if (playerNumber.equals(ID_RADIOBUTTON_2_PLAYERS)) {
            prefs.put("playerJ2Name", player2Name);
        }
        if (roundsOption.equals(ID_RADIOBUTTON_ROUND_CUSTOMIZED)) {
            prefs.put("roundCustomized", customRounds);
        }
    }

    public static void getPreferencesNormal(ObservableList<Node> nodoRound, ObservableList<Node> nodoPlayers, ObservableList<Node> nodoGame, TextField player1, TextField player2, TextField roundCustomed) {
        Preferences prefs = Preferences.userNodeForPackage(FXMLController.class);
        String roundsOption, playerNumber;
        roundsOption = prefs.get("roundsNormal", "");
        changeStateRadioButton(nodoRound, roundsOption);
        playerNumber = prefs.get("playerNumber", "");
        changeStateRadioButton(nodoPlayers, playerNumber);
        changeStateRadioButton(nodoGame, prefs.get("GameOptionNormal", ""));
        player1.setText(prefs.get("playerJ1Name", ""));
        System.out.println("el numero de rondas son: "+roundsOption);
        player2.setText(prefs.get("playerJ2Name", ""));
        if (playerNumber.equals(ID_RADIOBUTTON_2_PLAYERS)) {
            player2.setManaged(true);
            player2.setVisible(true);
            player1.setPrefWidth(230.0);
        }
        roundCustomed.setText(prefs.get("roundCustomized", ""));
        if (roundsOption.equals(ID_RADIOBUTTON_ROUND_CUSTOMIZED)) {
            roundCustomed.setManaged(true);
            roundCustomed.setVisible(true);
        }
    }
    
    private static void changeStateRadioButton(ObservableList<Node> nodos, String valueToSet){
        for(int i=0;i<nodos.size();i++){
            RadioButton button=(RadioButton)nodos.get(i);
            System.out.println("radiobutton: "+button.getText());
            if(button.getId().equals(valueToSet)){
                i=nodos.size();
                button.setSelected(true);
            }
        }
    }

}
