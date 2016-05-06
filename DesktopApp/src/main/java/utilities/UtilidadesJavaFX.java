/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import com.mycompany.datapptgame.*;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contextText);
        if (excepcion != null && !excepcion.isEmpty()) {
            Label label = new Label(info);
            TextArea textArea = new TextArea(excepcion);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);
            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);
            // Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);
        }
        System.out.println("CASI ESTAMOS");
        alert.showAndWait();
//        if (excepcion == null || (excepcion != null && excepcion.isEmpty())) {
//            alert.hide();
//        }
    }

    public static void changeSceneRoot(FXMLLoader loader, Stage stage) {
        Parent root = null;
        try {
            root = loader.load();
//            System.out.println("En El change scene root");
//            System.out.println("EL ISCONEXION DICE: " + FXMLController.getDatos().isConexionFallida());
//            if (FXMLController.getDatos().isConexionFallida()) {
//                System.out.println("ENTRAMOS EN CONEXION FALLIDA");
//                FXMLController.getDatos().changeConexionFallida();
//                ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
//                showAlertFields(null, bundle.getString("FalloConexion"), bundle.getString("ErrorConexionTitle"), null);
//            }
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (root != null) {
            stage.getScene().setRoot(root);
        }
    }
}
