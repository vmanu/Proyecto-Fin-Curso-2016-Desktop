/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.datapptgame;

/**
 * Clase contenedora de informacion relativa a los jugadores implicados en una
 * partida del juego
 * @namePlayer Nombre del jugador en cuestión
 * @tipoJuego Indica la configuracion de la partida (elegida por el usuario) respecto
 * al tipo de juego (Juego a 3, a 5 o a 9)
 * @numberOfRounds Indica la configuracion de la partida (elegida por el usuario)
 * respecto al numero de rondas elegidas (1,3,5)
 * @author Victor e Ivan
 */
public class Player {
    private String namePlayer;
    private GameType tipoJuego;
    private RoundsNumber numberOfRounds;
    private boolean playing;
    private int numPartidas;
    private int numVictorias;
    private double promedio;

    public Player() {
    }

    public Player(String namePlayer, GameType tipoJuego, RoundsNumber numberOfRounds, boolean playing, int numPartidas) {
        this.namePlayer = namePlayer;
        this.tipoJuego = tipoJuego;
        this.numberOfRounds = numberOfRounds;
        this.playing = playing;
        this.numPartidas=numPartidas;
    }

    public Player(String name, int numPartidas) {
        this.namePlayer = name;
        this.numPartidas=numPartidas;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public GameType getTipoJuego() {
        return tipoJuego;
    }

    public void setTipoJuego(GameType tipoJuego) {
        this.tipoJuego = tipoJuego;
    }

    public RoundsNumber getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(RoundsNumber numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getNumPartidas() {
        return numPartidas;
    }

    public void setNumPartidas(int numPartidas) {
        this.numPartidas = numPartidas;
    }
    
    public int getNumVictorias() {
        return numVictorias;
    }

    public void setNumVictorias(int numVictorias) {
        this.numVictorias = numVictorias;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    @Override
    public String toString() {
        return "Player{" + "namePlayer=" + namePlayer + ", tipoJuego=" + tipoJuego + ", numberOfRounds=" + numberOfRounds + ", playing=" + playing + ", numPartidas="+numPartidas+'}';
    }
}
