/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import static constantes.ConstantesStrings.*;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Clase encargada de la creación del stage y la primera configuración para que
 * arranque la aplicación
 * @author Victor e Ivan
 */
public class DesktopApp extends Application {

    private static Stage stage;
    private static CloseableHttpClient httpclient;

    /**
     * Carga de la primera visualizacion en la aplicacion los titulos y otras
     * propiedades si se deseara
     */
    @Override
    public void start(final Stage stage) throws Exception {
        this.stage = stage;
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);//GESTIONA IDIOMAS
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_PPAL), bundle);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add("styles/Styles.css");
        stage.setFullScreenExitHint(bundle.getString(ESC_TO_EXIT_HINT));
        stage.setFullScreenExitKeyCombination(KeyCombination.valueOf(ESC_BUTTON));
        stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.isAltDown()&&event.getCode()==KeyCode.ENTER){
                    stage.setFullScreen(true);
                }
            }
        });
        httpclient=HttpClients.createDefault();
        stage.setTitle(bundle.getString(NOMBRE_APLICACION));
        stage.getIcons().add(new Image(IMAGEN_ICONO_APP));
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
    
    public static CloseableHttpClient getHttpClient(){
        return httpclient;
    }
}
