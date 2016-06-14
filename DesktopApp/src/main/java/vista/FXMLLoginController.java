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
import static constantes.ConstantesCadenasLookup.*;
import static constantes.ConstantesStrings.*;
import static constantes.conexion.ConstantesConexion.*;
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
import utilities.PreferencesManager;
import static utilities.UtilidadesJavaFX.*;
import static vista.DesktopApp.getHttpClient;
import static vista.DesktopApp.getStage;
import static vista.FXMLController.changeLogueado;
import static vista.FXMLController.getDatos;

/**
 * FXML Controller class
 *
 * @author Victor e Ivan
 */
public class FXMLLoginController implements Initializable {

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
                String cc = "";
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                CloseableHttpResponse response1;
                HttpPost httpPost = new HttpPost(URL_SERVIDOR + URL_SEGURIDAD);
                response1 = getHttpClient().execute(httpPost);
                HttpEntity entity1 = response1.getEntity();
                cc = EntityUtils.toString(entity1, UTF_8);
                ClaveComplemento keys = mapper.readValue(cc, new TypeReference<ClaveComplemento>() {
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
        boolean logueadoCorrectamente = false;
        boolean registro = ((PasswordField) DesktopApp.getStage().getScene().lookup(CAMPO_PASS_CONFIRMACION)).isVisible();
        String log = ((TextField) DesktopApp.getStage().getScene().lookup(CAMPO_USER_LOGIN)).getText();
        String pass = ((PasswordField) DesktopApp.getStage().getScene().lookup(CAMPO_PASS)).getText();
        String pass2 = ((PasswordField) DesktopApp.getStage().getScene().lookup(CAMPO_PASS_CONFIRMACION)).getText();
        if (!log.isEmpty() && !pass.isEmpty() && (!registro || (registro && !pass2.isEmpty() && pass.equals(pass2)))) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            User usuario = new User(log, pass);
            ObjectMapper mapper = new ObjectMapper();
            try {
                nvps.add(new BasicNameValuePair(USER, new String(Base64.encodeBase64(PasswordHash.cifra(mapper.writeValueAsString(usuario), (clave + complemento))))));
                nvps.add(new BasicNameValuePair(CLAVE_HASHEADA, PasswordHash.createHash(clave)));
                nvps.add(new BasicNameValuePair(COMPLEMENTO_HASHEADO, PasswordHash.createHash(complemento)));
                if (registro) {
                    //mensaje de registro
                    HttpPost httpPost = new HttpPost(URL_SERVIDOR + URL_SERVLET_DB + URL_AGREGAR_USUARIO);
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                    CloseableHttpResponse response1 = getHttpClient().execute(httpPost);
                    HttpEntity entity1 = response1.getEntity();
                    logueadoCorrectamente = EntityUtils.toString(entity1, UTF_8).equals(SI);
                } else {
                    //mensaje de login
                    HttpPost httpPost = new HttpPost(URL_SERVIDOR + URL_LOGIN);
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                    CloseableHttpResponse response1 = getHttpClient().execute(httpPost);
                    HttpEntity entity1 = response1.getEntity();
                    logueadoCorrectamente = EntityUtils.toString(entity1, UTF_8).equals(SI);
                }
            } catch (IOException |ParseException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
                Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex){
                Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (logueadoCorrectamente) {
                ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
                clave = "";
                complemento = "";
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_ONLINE), bundle);
                getDatos().setNombreJ1(log);
                PreferencesManager.setPreferencesLogin(true, log);
                changeLogueado();
                changeSceneRoot(loader, getStage());
            } else {
                //alert con login o pass incorrecto
                shootAlert(registro?REGISTER_INCORRECT:LOGIN_INCORRECT, registro?REGISTER_INCORRECT_TITLE:LOGIN_INCORRECT_TITLE, false);
            }
        } else {
            StringBuilder cadena = new StringBuilder();
            ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
            boolean posibleMatchFailure = true;
            if (log.isEmpty()) {
                cadena.append(bundle.getString(MISSING_USER));
            }else{
                if(log.length()>30){
                    cadena.append(bundle.getString(USER_EXCEED_SIZE));
                }
            }
            if (pass.isEmpty()) {
                cadena.append(bundle.getString(MISSING_PASS_1));
                posibleMatchFailure = false;
            }
            if (registro && pass2.isEmpty()) {
                cadena.append(bundle.getString(MISSING_PASS_2));
                posibleMatchFailure = false;
            }
            if(posibleMatchFailure&&registro&&!pass.equals(pass2)){
                cadena.append(bundle.getString(FALLO_COINCIDENCIA_PASS));
            }
            showAlertFields(cadena.toString(), bundle.getString(WRONG_FIELDS), bundle.getString(WARNING), bundle.getString(WARNING_ARE));
        }
    }

    @FXML
    private void handleButtonRegister(MouseEvent event) {
        PasswordField segundaPass = (PasswordField) DesktopApp.getStage().getScene().lookup(CAMPO_PASS_CONFIRMACION);
        Button boton = (Button) DesktopApp.getStage().getScene().lookup(BOTON_LOGIN);
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
        if (!segundaPass.isVisible()) {
            segundaPass.setManaged(true);
            segundaPass.setVisible(true);
            ((Label) event.getSource()).setText(bundle.getString(SIGN_IN_LOW));
            boton.setText(bundle.getString(SIGN_UP));
        } else {
            segundaPass.setManaged(false);
            segundaPass.setVisible(false);
            ((Label) event.getSource()).setText(bundle.getString(SIGN_UP_LOW));
            boton.setText(bundle.getString(SIGN_IN));
        }
    }

    @FXML
    private void handleButtonBack(ActionEvent event) {
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_PPAL), bundle);
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
