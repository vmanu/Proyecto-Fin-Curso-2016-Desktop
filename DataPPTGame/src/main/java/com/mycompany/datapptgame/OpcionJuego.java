/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.datapptgame;

/**
 * Clase encargada de guardar el ordinal del enum de FichaX (siendo X: 3, 5 o 9)
 * y el resultado si ya estuviera terminada la partida
 * @author Victor e Ivan
 */
public class OpcionJuego {
    private int opcion;
    private Result result;

    public OpcionJuego() {
    }

    public OpcionJuego(int opcion, Result result) {
        this.opcion = opcion;
        this.result = result;
    }

    public int getOpcion() {
        return opcion;
    }

    public void setOpcion(int opcion) {
        this.opcion = opcion;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "OpcionJuego{" + "opcion=" + opcion + ", gana=" + result + '}';
    }
    
    
}
