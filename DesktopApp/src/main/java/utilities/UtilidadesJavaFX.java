/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import com.mycompany.datapptgame.*;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import modelo.DataContainer;
import vista.FXMLController;

/**
 *
 * @author Victor
 */
public class UtilidadesJavaFX {

    public static Enum getEnumFromOrdinal(int ordinal, DataContainer datos) {
        Enum res = null;
        boolean sal = false;
        switch (datos.getFactorAlgoritmo()) {
            case 1:
                for (int i = 0; i < Fichas3.values().length && !sal; i++) {
                    if (i == ordinal) {
                        res = Fichas3.values()[i];
                        sal = true;
                    }
                }
            case 2:
                for (int i = 0; i < Fichas5.values().length && !sal; i++) {
                    if (i == ordinal) {
                        res = Fichas5.values()[i];
                        sal = true;
                    }
                }
                break;
            case 4:
                for (int i = 0; i < Fichas9.values().length && !sal; i++) {
                    if (i == ordinal) {
                        res = Fichas9.values()[i];
                        sal = true;
                    }
                }
                break;
        }
        return res;
    }

    public static String gestionaPulsadoMaquina(Enum chosen, DataContainer datos) {
        return datos.getMapFichasMaquina().get(chosen.name());
    }

    public static void showAlertFields(String excepcion, String contextText, String title, String info) {
        System.out.println("0");
        Alert alert = new Alert(AlertType.WARNING);
        System.out.println("1");
        alert.setTitle(title);
        System.out.println("2");
        alert.setHeaderText(null);
        System.out.println("3");
        alert.setContentText(contextText);
        System.out.println("4");
        if (excepcion != null && !excepcion.isEmpty()) {
            System.out.println("5");
            Label label = new Label(info);
            System.out.println("6");
            TextArea textArea = new TextArea(excepcion);
            System.out.println("7");
            textArea.setEditable(false);
            System.out.println("8");
            textArea.setWrapText(true);
            System.out.println("9");
            textArea.setMaxWidth(Double.MAX_VALUE);
            System.out.println("10");
            textArea.setMaxHeight(Double.MAX_VALUE);
            System.out.println("11");
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            System.out.println("12");
            GridPane.setHgrow(textArea, Priority.ALWAYS);
            System.out.println("13");
            GridPane expContent = new GridPane();
            System.out.println("14");
            expContent.setMaxWidth(Double.MAX_VALUE);
            System.out.println("15");
            expContent.add(label, 0, 0);
            System.out.println("16");
            expContent.add(textArea, 0, 1);
            System.out.println("17");
            // Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);
            System.out.println("18");
        }
        System.out.println("PREFIN");
        alert.showAndWait();
        System.out.println("FIN");
    }
    
    public static void changeSceneRoot(FXMLLoader loader, Stage stage) {
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (root != null) {
            stage.getScene().setRoot(root);
        }
    }
}
