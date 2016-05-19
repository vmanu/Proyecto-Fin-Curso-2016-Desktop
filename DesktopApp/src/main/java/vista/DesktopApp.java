/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

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
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");//GESTIONA IDIOMAS
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuPrincipal.fxml"), bundle);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreenExitHint("Para salir pulse ESC");
        stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("ESC"));
        stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.isAltDown()&&event.getCode()==KeyCode.ENTER){
                    stage.setFullScreen(true);
                }
            }
            
        });
        httpclient=HttpClients.createDefault();
        stage.setTitle(bundle.getString("AppName"));
        stage.getIcons().add(new Image("/imagenes/fivegame.jpg"));
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
