/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.datapptgame;


/**
 * Esta clase será usada para comunicar cliente-servidor, está compuesta por dos
 * atributos encargadas de dicha funcion
 * @type Es el tipo de mensaje cuyas opciones dependen de las opciones de
 * enum TypeMessage
 * @content Es una variable tipo Object, ya que puede contener tanto objetos de
 * clase OpcionJuego como de Player o User
 * @author Victor e Ivan
 */
public class MetaMessage {
    private TypeMessage type;
    private Object content;

    public MetaMessage() {
    }

    public TypeMessage getType() {
        return type;
    }

    public void setType(TypeMessage type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MetaMessage{" + "type=" + type + ", content=" + content + '}';
    }
}
