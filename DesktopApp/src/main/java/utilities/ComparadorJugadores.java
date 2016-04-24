package utilities;

import modelo.Jugador;

import java.util.Comparator;

/**
 * Created by Victor on 14/12/2015.
 */
public class ComparadorJugadores {

}
/**
 * Define un criterio de comparación según las victorias que haya hecho cada Jugador.
 */
class porVictorias implements Comparator<Jugador> {

    @Override
    public int compare(Jugador o1, Jugador o2) {
        return o2.getGanados()-o1.getGanados();
    }
}

/**
 * Define un criterio de comparación según las jugadas de los Jugadores.
 */
class porJugadas implements Comparator<Jugador>{

    @Override
    public int compare(Jugador o1, Jugador o2) {
        return o2.getJugados()-o1.getJugados();
    }
}

/**
 * Define un criterio de comparación según los promedios de los Jugadores.
 */
class porPromedios implements Comparator<Jugador>{

    @Override
    public int compare(Jugador o1, Jugador o2) {
        int result=0;
        double factor1=o1.getGanados()/o1.getJugados();
        double factor2=o2.getGanados()/o2.getJugados();
        if((factor2-factor1)<=0){
            result=-1;
        }else{
            result=1;
        }
        return result;
    }
}