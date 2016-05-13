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
import javafx.application.Platform;
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
import vista.DesktopApp;
import vista.FXMLController;

/**
 * Clase con metodos y utilidades necesarias para el funcionamiento de JavaFX
 *
 * @author Victor e Ivan
 */
public class UtilidadesJavaFX {

    /**
     * Devuelve el Enum a partir de su ordinal, analizando el parametro
     * factorAlgoritmo de el objeto datos (Datacontainer) para determinar que
     * enum debe ser revisado y a traves de su ordinal obtener el enum concreto
     *
     * @param ordinal integer que indica la posicion del Enum
     * @param datos
     * @return
     */
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

    /**
     * Método encargado de devolver el String de la ruta de la imagen a mostrar,
     * acorde a lo obtenido aleatoriamente por la maquina
     *
     * @param chosen enum elegido
     * @param datos necesario para la obtención del hashmap que retorna la ruta
     * @return String con el valor de la ruta a la imagen adecuada
     */
    public static String gestionaPulsadoMaquina(Enum chosen, DataContainer datos) {
        return datos.getMapFichasMaquina().get(chosen.name());
    }

    /**
     * Método encargado de los alert usados para notificar cualquier tipo de
     * fallo o alerta que deba ser mostrado al usuario
     *
     * @param excepcion
     * @param contextText
     * @param title
     * @param info
     */
    public static void showAlertFields(String excepcion, String contextText, String title, String info) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contextText);
        boolean isNormalMessage = excepcion != null && !excepcion.isEmpty();
        if (isNormalMessage) {
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
        alert.showAndWait();
    }

    /**
     * Método encargado de la carga de la nueva escena, y por tanto, de la nueva
     * visualización
     *
     * @param loader FXMLLoader con la ruta al FXML asignado al que se quiere
     * cambiar
     * @param stage Escenario en la que se carga la escena
     */
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

    /**
     * Gestiona la emisión de un Alert concreto para cuando se ha perdido la conexion
     * websocket con otro jugador
     */
    public static void shootAlert() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
                showAlertFields(null, bundle.getString("FalloConexion"), bundle.getString("ErrorConexionTitle"), null);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuPrincipal.fxml"), bundle);
                Stage stage = DesktopApp.getStage();
                changeSceneRoot(loader, stage);
                FXMLController.getDatos().setValoresIniciales();
            }
        });
    }
}
