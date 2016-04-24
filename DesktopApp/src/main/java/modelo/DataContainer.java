package modelo;

import com.mycompany.datapptgame.Fichas3;
import com.mycompany.datapptgame.Fichas5;
import com.mycompany.datapptgame.Fichas9;
import java.util.HashMap;

/**
 * Created by Victor on 29/03/2016.
 */
public class DataContainer {
    private int roundsCounter;
    private int roundsLimit;
    private int victoriesP1;
    private int victoriesP2;
    private boolean turno;
    private int modalidadJuego;
    private Enum chosen1;
    private Enum chosen2;
    private int factorAlgoritmo;
    private String idImagenPulsada1;//En realidad no es el id, sino la ruta, solo se mantiene asi para ver la semejanza con la variable android
    private String idImagenPulsada2;
    private String nombreJ1;
    private String nombreJ2;
    private boolean jugando;
    private HashMap <String,Enum> mapFichas;
    private HashMap <String,String> mapFichasMaquina;

    public DataContainer() {
        inicializaMapFichas();
        inicializaMapFichasMaquina();
    }

    //<editor-fold defaultState="Collapsed" desc="GETTERS Y SETTERS">
    public int getRoundsCounter() {
        return roundsCounter;
    }

    public void setRoundsCounter(int roundsCounter) {
        this.roundsCounter = roundsCounter;
    }

    public int getRoundsLimit() {
        return roundsLimit;
    }

    public void setRoundsLimit(int roundsLimit) {
        this.roundsLimit = roundsLimit;
    }

    public int getVictoriesP1() {
        return victoriesP1;
    }

    public void setVictoriesP1(int victoriesP1) {
        this.victoriesP1 = victoriesP1;
    }

    public int getVictoriesP2() {
        return victoriesP2;
    }

    public void setVictoriesP2(int victoriesP2) {
        this.victoriesP2 = victoriesP2;
    }

    public boolean isTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    public int getModalidadJuego() {
        return modalidadJuego;
    }

    public void setModalidadJuego(int modalidadJuego) {
        this.modalidadJuego = modalidadJuego;
    }

    public Enum getChosen1() {
        return chosen1;
    }

    public void setChosen1(Enum chosen1) {
        this.chosen1 = chosen1;
    }

    public Enum getChosen2() {
        return chosen2;
    }

    public void setChosen2(Enum chosen2) {
        this.chosen2 = chosen2;
    }

    public int getFactorAlgoritmo() {
        return factorAlgoritmo;
    }

    public void setFactorAlgoritmo(int factorAlgoritmo) {
        this.factorAlgoritmo = factorAlgoritmo;
    }

    public String getIdImagenPulsada1() {
        return idImagenPulsada1;
    }

    public void setIdImagenPulsada1(String idImagenPulsada1) {
        this.idImagenPulsada1 = idImagenPulsada1;
    }

    public String getIdImagenPulsada2() {
        return idImagenPulsada2;
    }

    public void setIdImagenPulsada2(String idImagenPulsada2) {
        this.idImagenPulsada2 = idImagenPulsada2;
    }

    public String getNombreJ1() {
        return nombreJ1;
    }

    public void setNombreJ1(String nombreJ1) {
        this.nombreJ1 = nombreJ1;
    }

    public String getNombreJ2() {
        return nombreJ2;
    }

    public void setNombreJ2(String nombreJ2) {
        this.nombreJ2 = nombreJ2;
    }

    public boolean isJugando() {
        return jugando;
    }

    public void setJugando(boolean jugando) {
        this.jugando = jugando;
    }

    public HashMap<String, Enum> getMapFichas() {
        return mapFichas;
    }

    public HashMap<String, String> getMapFichasMaquina() {
        return mapFichasMaquina;
    }
    //</editor-fold>

    //<editor-fold defaultState="Collapsed" desc="GESTION ASIGNACION ENUMS">
    private Enum getCorrectEnum(Enum[] values,String valorBueno){
        Enum devuelve=null;
        boolean sal=false;
        for(int i=0;i<values.length&&!sal;i++){
            if(values[i].name().equalsIgnoreCase(valorBueno)){
                sal=true;
                devuelve=values[i];
            }
        }
        return devuelve;
    }
    //</editor-fold>

    //<editor-fold defaultState="Collapsed" desc="INICIALIZACION HASHMAP">
    private void inicializaMapFichas(){
        mapFichas=new HashMap();
        mapFichas.put("papel5",Fichas5.PAPEL);
        mapFichas.put("spock5",Fichas5.SPOCK);
        mapFichas.put("piedra5",Fichas5.PIEDRA);
        mapFichas.put("tijera5",Fichas5.TIJERA);
        mapFichas.put("lagarto5",Fichas5.LAGARTO);
        mapFichas.put("papel9",Fichas9.PAPEL);
        mapFichas.put("aire9",Fichas9.AIRE);
        mapFichas.put("agua9",Fichas9.AGUA);
        mapFichas.put("pistola9",Fichas9.PISTOLA);
        mapFichas.put("piedra9",Fichas9.PIEDRA);
        mapFichas.put("fuego9",Fichas9.FUEGO);
        mapFichas.put("tijera9",Fichas9.TIJERA);
        mapFichas.put("humano9",Fichas9.HUMANO);
        mapFichas.put("esponja9",Fichas9.ESPONJA);
        mapFichas.put("piedra3",Fichas3.PIEDRA);
        mapFichas.put("papel3",Fichas3.PAPEL);
        mapFichas.put("tijera3",Fichas3.TIJERA);
    }

    private void inicializaMapFichasMaquina() {//Se usa para el setImagenPulsada en caso de jugar contra la maquina, con el .name del enum que sea elegido
        mapFichasMaquina=new HashMap();
        mapFichasMaquina.put("PAPEL","imagenes/papelazul.png");
        mapFichasMaquina.put("SPOCK","imagenes/spockazul.png");
        mapFichasMaquina.put("PIEDRA","imagenes/piedraazul.png");
        mapFichasMaquina.put("TIJERA","imagenes/tijerasazul.png");
        mapFichasMaquina.put("LAGARTO","imagenes/lizardazul.png");
        mapFichasMaquina.put("AIRE","imagenes/windazul.png");
        mapFichasMaquina.put("AGUA","imagenes/waterazul.png");
        mapFichasMaquina.put("PISTOLA","imagenes/gunazul.png");
        mapFichasMaquina.put("FUEGO","imagenes/fireazul.png");
        mapFichasMaquina.put("HUMANO","imagenes/humanazul.png");
        mapFichasMaquina.put("ESPONJA","imagenes/spongeazul.png");
    }

    private HashMap <String,String> inicializaMapImageTagsRojo() {
        HashMap <String,String> mapImageTagsRojo=new HashMap();
        mapImageTagsRojo.put("papel5","imagenes/papelrojo.png");
        mapImageTagsRojo.put("spock5","imagenes/spockrojo.png");
        mapImageTagsRojo.put("piedra5","imagenes/piedraroja.png");
        mapImageTagsRojo.put("tijera5","imagenes/tijerasrojo.png");
        mapImageTagsRojo.put("lagarto5","imagenes/lizardrojo.png");
        mapImageTagsRojo.put("papel9","imagenes/papelrojo.png");
        mapImageTagsRojo.put("aire9","imagenes/windrojo.png");
        mapImageTagsRojo.put("agua9","imagenes/waterrojo.png");
        mapImageTagsRojo.put("pistola9","imagenes/gunrojo.png");
        mapImageTagsRojo.put("piedra9","imagenes/piedraroja.png");
        mapImageTagsRojo.put("fuego9","imagenes/firerojo.png");
        mapImageTagsRojo.put("tijera9","imagenes/tijerasrojo.png");
        mapImageTagsRojo.put("humano9","imagenes/humanrojo.png");
        mapImageTagsRojo.put("esponja9","imagenes/spongerojo.png");
        mapImageTagsRojo.put("piedra3","imagenes/piedraroja.png");
        mapImageTagsRojo.put("papel3","imagenes/papelrojo.png");
        mapImageTagsRojo.put("tijera3","imagenes/tijerasrojo.png");
        return  mapImageTagsRojo;
    }

    private HashMap <String,String> inicializaMapImageTagsAzul() {
        HashMap <String,String> mapImageTagsAzul=new HashMap();
        mapImageTagsAzul.put("papel5","imagenes/papelazul.png");
        mapImageTagsAzul.put("spock5","imagenes/spockazul.png");
        mapImageTagsAzul.put("piedra5","imagenes/piedraazul.png");
        mapImageTagsAzul.put("tijera5","imagenes/tijerasazul.png");
        mapImageTagsAzul.put("lagarto5","imagenes/lizardazul.png");
        mapImageTagsAzul.put("papel9","imagenes/papelazul.png");
        mapImageTagsAzul.put("aire9","imagenes/windazul.png");
        mapImageTagsAzul.put("agua9","imagenes/waterazul.png");
        mapImageTagsAzul.put("pistola9","imagenes/gunazul.png");
        mapImageTagsAzul.put("piedra9","imagenes/piedraazul.png");
        mapImageTagsAzul.put("fuego9","imagenes/fireazul.png");
        mapImageTagsAzul.put("tijera9","imagenes/tijerasazul.png");
        mapImageTagsAzul.put("humano9","imagenes/humanazul.png");
        mapImageTagsAzul.put("esponja9","imagenes/spongeazul.png");
        mapImageTagsAzul.put("piedra3","imagenes/piedraazul.png");
        mapImageTagsAzul.put("papel3","imagenes/papelazul.png");
        mapImageTagsAzul.put("tijera3","imagenes/tijerasazul.png");
        return mapImageTagsAzul;
    }
    //</editor-fold>

    //<editor-fold defaultState="Collapsed" desc="OTROS METODOS">
    public void cambiaTurno(){
        turno=!turno;
    }

    public void sumaVictoriasP1(){
        victoriesP1++;
    }

    public void sumaVictoriasP2(){
        victoriesP2++;
    }

    public int getOrdinalChosen1(){
        return chosen1.ordinal();
    }

    public int getOrdinalChosen2(){
        return chosen2.ordinal();
    }

    public void avanzaRonda(){
        roundsCounter++;
    }

    public boolean rondasFinalizadas(){
        return roundsCounter==roundsLimit;
    }

    public void setValoresIniciales(){
        roundsCounter=0;
        victoriesP1=0;
        victoriesP2=0;
        chosen1=null;
        chosen2=null;
        idImagenPulsada1="";
        idImagenPulsada2="";
    }

    /*public void asignarTagsRojos(AppCompatActivity activity){
        HashMap<String, String> tags=inicializaMapImageTagsRojo();
        for(String key:tags.keySet()){
            activity.findViewById(key).setTag(tags.get(key));
        }
    }

    public void asignarTagsAzules(AppCompatActivity activity){
        HashMap<Integer, Integer> tags=inicializaMapImageTagsAzul();
        for(Integer key:tags.keySet()){
            activity.findViewById(key).setTag(tags.get(key));
        }
    }*/
    //</editor-fold>
}
