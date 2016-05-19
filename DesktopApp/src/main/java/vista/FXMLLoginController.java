/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.datapptgame.ClaveComplemento;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import static utilities.UtilidadesJavaFX.*;
import static vista.DesktopApp.getHttpClient;
import static vista.DesktopApp.getStage;

/**
 * FXML Controller class
 *
 * @author Victor e Ivan
 */
public class FXMLLoginController implements Initializable {

    private String privateKey;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Entramos en intialize Login");
        //Enviar solicitud de clave de cifrado
        try {
            String ok = "";
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            CloseableHttpResponse response1;
            HttpPost httpPost = new HttpPost("http://localhost:8080/ServerPPTGame/seguridad");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("op", "security"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            response1 = getHttpClient().execute(httpPost);
            HttpEntity entity1 = response1.getEntity();
            ok = EntityUtils.toString(entity1, "UTF-8");
            System.out.println("veamos");
            ClaveComplemento keys = mapper.readValue(ok, new TypeReference<ClaveComplemento>() {
            });
            String clave = keys.getClaves().get((int) Math.random() * keys.getClaves().size());
            String complemento = keys.getComplementos().get((int) Math.random() * keys.getComplementos().size());
            privateKey = clave + complemento;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleButtonLogin(ActionEvent event) {
        System.out.println("EN LOGIN");
        boolean logueadoCorrectamente = true;
        boolean registro = ((PasswordField) DesktopApp.getStage().getScene().lookup("#Login_rePass")).isVisible();
        String log = ((TextField) DesktopApp.getStage().getScene().lookup("#Login_User")).getText();
        String pass = ((PasswordField) DesktopApp.getStage().getScene().lookup("#Login_Pass")).getText();
        String pass2 = ((PasswordField) DesktopApp.getStage().getScene().lookup("#Login_rePass")).getText();
        if (!log.isEmpty() && !pass.isEmpty() && (!registro || (registro && !pass2.isEmpty() && pass.equals(pass2)))) {
            if (registro) {
                //mensaje de registro
            } else //mensaje de login
            if (logueadoCorrectamente) {
                ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuJuegoOnline.fxml"), bundle);
                changeSceneRoot(loader, getStage());
            }
        } else {
            
        }
    }

    @FXML
    private void handleButtonRegister(MouseEvent event) {
        PasswordField segundaPass = (PasswordField) DesktopApp.getStage().getScene().lookup("#Login_rePass");
        Button boton = (Button) DesktopApp.getStage().getScene().lookup("#BotonLogin");
        if (!segundaPass.isVisible()) {
            System.out.println("A REGISTER");
            segundaPass.setManaged(true);
            segundaPass.setVisible(true);
            ((Label) event.getSource()).setText("Sign In");
            boton.setText("SIGN UP");
        } else {
            System.out.println("De vuelta a Login");
            segundaPass.setManaged(false);
            segundaPass.setVisible(false);
            ((Label) event.getSource()).setText("Sign Up");
            boton.setText("SIGN IN");
        }
    }

    @FXML
    private void handleButtonBack(ActionEvent event) {
        System.out.println("EN BACK");
        ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FXMLMenuPrincipal.fxml"), bundle);
        changeSceneRoot(loader, DesktopApp.getStage());
    }

    @FXML
    private void handleOnMouseOver(MouseEvent event) {
        gestionPunteroRatonOver();
    }

    @FXML
    private void handleOnMouseOut(MouseEvent event) {
        gestionPunteroRatonOut();
    }
}
