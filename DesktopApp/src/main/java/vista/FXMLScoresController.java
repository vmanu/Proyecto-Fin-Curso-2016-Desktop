/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.datapptgame.Player;
import static constantes.ConstantesStrings.SERVICIO_STRINGS_BUNDLE;
import static constantes.ConstantesStrings.*;
import static constantes.conexion.ConstantesConexion.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import static utilities.UtilidadesJavaFX.changeSceneRoot;
import static utilities.UtilidadesJavaFX.gestionPunteroRatonOut;
import static utilities.UtilidadesJavaFX.gestionPunteroRatonOver;

/**
 * FXML Controller class
 *
 * @author Victor e Ivan
 */
public class FXMLScoresController implements Initializable {

    @FXML
    private ComboBox cbox;
    @FXML
    private ListView listViewScores;
    private HashMap<String, String> getUrls;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> list = new ArrayList<String>();
        list.add(rb.getString(VICTORIES_OPTION));
        list.add(rb.getString(ROUNDS_OPTION));
        list.add(rb.getString(AVERAGE_OPTION));
        ObservableList obList = FXCollections.observableList(list);
        cbox.getItems().clear();
        cbox.setItems(obList);
        cbox.setValue(list.get(0));
        getUrls = new HashMap();
        getUrls.put(rb.getString(VICTORIES_OPTION), URL_SERVLET_DB + URL_GET_BY_VICTORIES);
        getUrls.put(rb.getString(ROUNDS_OPTION), URL_SERVLET_DB + URL_GET_BY_ROUNDS);
        getUrls.put(rb.getString(AVERAGE_OPTION), URL_SERVLET_DB + URL_GET_BY_AVERAGE);
        cbox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if (!t.equals(t1)) {
                    ObservableList<String> data = FXCollections.observableArrayList(formateaDatos(recogerDatos(getUrls.get(t1))));
                    listViewScores.setItems(data);
                }
            }
        });
        ObservableList<String> data = FXCollections.observableArrayList(formateaDatos(recogerDatos(URL_SERVLET_DB + URL_GET_BY_VICTORIES)));
        listViewScores.setItems(data);
    }

    public List<Player> recogerDatos(final String url) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Player> players = null;
        try {
            HttpPost httpPost = new HttpPost(URL_SERVIDOR + url);
            CloseableHttpResponse response1 = DesktopApp.getHttpClient().execute(httpPost);
            HttpEntity entity1 = response1.getEntity();
            String jugds = EntityUtils.toString(entity1, UTF_8);
            players = mapper.readValue(jugds, new TypeReference<ArrayList<Player>>() {
            });
        } catch (IOException ex) {
            Logger.getLogger(FXMLScoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return players;
    }

    public List<String> formateaDatos(List<Player> players) {
        List<String> format = new ArrayList();
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
        for (Player p : players) {
            StringBuilder cadena = new StringBuilder();
            cadena.append(bundle.getString(SCORES_STRING_NOMBRE)).append(p.getNamePlayer()).append(bundle.getString(SCORES_STRING_VICTORIAS)).append(p.getNumVictorias()).append(bundle.getString(SCORES_STRING_RONDAS)).append(p.getNumPartidas());
            format.add(cadena.toString());
        }
        return format;
    }

    /**
     * Gestiona los eventos del boton back del los menus: Reglas, Reglas
     * graficas, Desarrolladores y Puntuaciones. Se gestionan en un solo metodo
     * ya que la funcionalidad es la misma
     *
     * @param event utilizado para gestionar la obtencion del id del boton
     * pulsado y manejar los cambios de escena requerido
     */
    @FXML
    private void handleButtonBackAction(ActionEvent event) {
        Stage stage = DesktopApp.getStage();
        ResourceBundle bundle = ResourceBundle.getBundle(SERVICIO_STRINGS_BUNDLE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ESCENA_MENU_ONLINE), bundle);
        changeSceneRoot(loader, stage);
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
