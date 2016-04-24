/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.datapptgame;

/**
 *
 * @author ivanp
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
