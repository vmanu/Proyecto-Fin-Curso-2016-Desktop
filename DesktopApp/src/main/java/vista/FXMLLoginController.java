/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import static utilities.UtilidadesJavaFX.*;

/**
 * FXML Controller class
 *
 * @author Victor
 */
public class FXMLLoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Entramos en intialize Login");
    }    
    
    @FXML
    private void handleButtonLogin(ActionEvent event) {
        System.out.println("EN LOGIN");
        
    }
    
    @FXML
    private void handleButtonRegister(MouseEvent event) {
        PasswordField segundaPass=(PasswordField)DesktopApp.getStage().getScene().lookup("#Login_rePass");
        Button boton=(Button)DesktopApp.getStage().getScene().lookup("#BotonLogin");
        if(!segundaPass.isVisible()){
            System.out.println("A REGISTER");
            segundaPass.setManaged(true);
            segundaPass.setVisible(true);
            ((Label)event.getSource()).setText("Sign In");
            boton.setText("SIGN UP");
        }else{
            System.out.println("De vuelta a Login");
            segundaPass.setManaged(false);
            segundaPass.setVisible(false);
            ((Label)event.getSource()).setText("Sign Up");
            boton.setText("SIGN IN");
        }
    }
    
    @FXML
    private void handleButtonBack(ActionEvent event) {
        System.out.println("EN BACK");
        FXMLLoader loader = null;
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
        loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuPrincipal.fxml"), bundle);
        changeSceneRoot(loader, DesktopApp.getStage());
    }
    
    @FXML
    private void handleOnMouseOver(MouseEvent event){
        gestionPunteroRatonOver();
    }
    
    @FXML
    private void handleOnMouseOut(MouseEvent event){
        gestionPunteroRatonOut();
    }
}
