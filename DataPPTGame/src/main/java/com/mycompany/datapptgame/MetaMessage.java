/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.datapptgame;


/**
 *
 * @author oscar
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
