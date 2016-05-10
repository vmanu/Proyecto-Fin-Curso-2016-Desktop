/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.datapptgame;

/**
 * Posibles opciones de estado de un jugador relativas al tipo de juego elegido:
 * @NONE: no se ha elegido aun
 * @ANY: se ha elegido la partida aleatoriamente, de modo que acepta cualquier
 * configuración
 * @JUEGO3,@JUEGO5,@JUEGO9: ha elegido jugar a un juego de 3, 5 y 9 respectivamente
 * @author Victor e Ivan
 */
public enum GameType {
    JUEGO3,JUEGO5,JUEGO9,ANY,NONE
}
