/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.datapptgame;

/**
 * Gestiona la informacion del logeo/registro de un usuario, transportandose entre
 * cliente y servidor
 * @author Victor e Ivan
 */
public class User {
    private String login;
    private String pass;

    public User(){
        
    }
    
    public User(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "User{" + "login=" + login + ", pass=" + pass + '}';
    }
    
    
}
