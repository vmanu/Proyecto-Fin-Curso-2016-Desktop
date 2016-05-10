/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.datapptgame;

/**
 * Indica los diferentes tipos de comunicación posibles entre cliente y servidor,
 * de ellos dependerá la funcionalidad en ambos entornos
 * @CONEXION: cuando se establece conexion, para mandar la informacion de la partida
 * elegida
 * @PARTIDA: opcion de juego elegida y en su caso, resultado
 * @RESPUESTA: mensaje desde servidor cuando envia a uno de los participantes la
 * opcion elegida por el otro participante de la partida
 * @DESCONEXION: cuando se desconecta una sesion o se termina abruptamente una partida,
 * para notificarlo a aquellos usuarios unidos al usuario desconectado
 * @NOMBRE: mensaje desde Servidor que indica al cliente que es un mensaje cuyo
 * unico proposito es otorgarle el nombre del adversario
 * @author Victor e Ivan
 */
public enum TypeMessage {
    CONEXION,PARTIDA,RESPUESTA,DESCONEXION,NOMBRE
}
