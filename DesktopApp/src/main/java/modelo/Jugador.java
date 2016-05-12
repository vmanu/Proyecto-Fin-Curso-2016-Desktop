package modelo;

import java.util.Date;

/**
 * Clase usada para la gestión de mostrar el score, ayudando a organizarlos segun
 * se pida
 * @author Victor e Ivan
 */
public class Jugador{
    private int ganados;
    private int jugados;
    private Date date;
    private String nombre;

    public Jugador(){

    }

    public Jugador(int ganados, int jugados, Date date, String nombre) {
        this.ganados = ganados;
        this.jugados = jugados;
        this.date = date;
        this.nombre = nombre;
    }

    public int getGanados() {
        return ganados;
    }

    public void setGanados(int ganados) {
        this.ganados = ganados;
    }

    public int getJugados() {
        return jugados;
    }

    public void setJugados(int jugados) {
        this.jugados = jugados;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}