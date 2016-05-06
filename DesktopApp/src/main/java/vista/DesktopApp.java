/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import static utilities.UtilidadesJavaFX.showAlertFields;

/**
 *
 * @author Victor
 */
public class DesktopApp extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");//GESTIONA IDIOMAS
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuPrincipal.fxml"), bundle);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(bundle.getString("AppName"));
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }

//    public static void setStage(Stage newstage) {
//        stage=newstage;
//    }
    private void testMethod() {
        System.out.println("Entro en testMethod");
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
        showAlertFields(null, bundle.getString("FalloConexion"), bundle.getString("ErrorConexionTitle"), null);
        System.out.println("Salgo de testMethod");
    }
}
