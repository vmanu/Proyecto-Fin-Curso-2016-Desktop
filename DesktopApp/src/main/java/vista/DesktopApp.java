/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static utilities.UtilidadesJavaFX.showAlertFields;

/**
 * Clase encargada de la creación del stage y la primera configuración para que
 * arranque la aplicación
 * @author Victor e Ivan
 */
public class DesktopApp extends Application {

    private static Stage stage;

    /**
     * Carga de la primera visualizacion en la aplicacion los titulos y otras
     * propiedades si se deseara
     */
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
}
