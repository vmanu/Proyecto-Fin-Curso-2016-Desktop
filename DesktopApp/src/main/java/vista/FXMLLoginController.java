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
import com.mycompany.datapptgame.User;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
import objetos_seguridad.PasswordHash;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
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

    private final String URL_SERVIDOR = "http://localhost:8080/ServerPPTGame/";
    private static String clave = "";
    private static String complemento = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Entramos en intialize Login");
        //Enviar solicitud de clave de cifrado
        if (clave.isEmpty()) {
            //SOLO ENTRA AQUI SI LA CLAVE HA SIDO VACIADA O ES LA PRIMERA VEZ EN ESTA EJECUCION, REDUCIMOS TIEMPOS DE CARGA Y AHORRAMOS TRABAJO AL SERVER
            try {
                String ok = "";
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                CloseableHttpResponse response1;
                HttpPost httpPost = new HttpPost(URL_SERVIDOR + "seguridad");
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("op", "security"));
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                response1 = getHttpClient().execute(httpPost);
                HttpEntity entity1 = response1.getEntity();
                ok = EntityUtils.toString(entity1, "UTF-8");
                System.out.println("veamos");
                ClaveComplemento keys = mapper.readValue(ok, new TypeReference<ClaveComplemento>() {
                });
                clave = keys.getClaves().get((int) Math.random() * keys.getClaves().size());
                complemento = keys.getComplementos().get((int) Math.random() * keys.getComplementos().size());
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void handleButtonLogin(ActionEvent event) {
        System.out.println("EN LOGIN "+(clave+complemento));
        boolean logueadoCorrectamente = false;
        boolean registro = ((PasswordField) DesktopApp.getStage().getScene().lookup("#Login_rePass")).isVisible();
        String log = ((TextField) DesktopApp.getStage().getScene().lookup("#Login_User")).getText();
        String pass = ((PasswordField) DesktopApp.getStage().getScene().lookup("#Login_Pass")).getText();
        String pass2 = ((PasswordField) DesktopApp.getStage().getScene().lookup("#Login_rePass")).getText();
        if (!log.isEmpty() && !pass.isEmpty() && (!registro || (registro && !pass2.isEmpty() && pass.equals(pass2)))) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            User usuario = new User(log, pass);
            ObjectMapper mapper = new ObjectMapper();
            try {
                nvps.add(new BasicNameValuePair("user", new String(Base64.encodeBase64(PasswordHash.cifra(mapper.writeValueAsString(usuario), (clave+complemento))))));
                nvps.add(new BasicNameValuePair("claveHasheada", PasswordHash.createHash(clave)));
                nvps.add(new BasicNameValuePair("complementoHasheado", PasswordHash.createHash(complemento)));
                if (registro) {
                    //mensaje de registro
                    HttpPost httpPost = new HttpPost(URL_SERVIDOR + "ServletDB?op=put");
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                    CloseableHttpResponse response1 = getHttpClient().execute(httpPost);
                    HttpEntity entity1 = response1.getEntity();
                    logueadoCorrectamente = EntityUtils.toString(entity1, "UTF-8").equals("SI");
                } else {
                    //mensaje de login
                    HttpPost httpPost = new HttpPost(URL_SERVIDOR + "login");
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                    CloseableHttpResponse response1 = getHttpClient().execute(httpPost);
                    HttpEntity entity1 = response1.getEntity();
                    logueadoCorrectamente = EntityUtils.toString(entity1, "UTF-8").equals("SI");
                }
            } catch (IOException ex) {
                Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (logueadoCorrectamente) {
                ResourceBundle bundle = ResourceBundle.getBundle("strings.UIResources");
                clave="";
                complemento="";
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
