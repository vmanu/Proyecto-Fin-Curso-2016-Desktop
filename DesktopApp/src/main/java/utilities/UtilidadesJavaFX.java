/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import com.mycompany.datapptgame.*;
import modelo.DataContainer;

/**
 *
 * @author Victor
 */
public class UtilidadesJavaFX {
    public static Enum getEnumFromOrdinal(int ordinal,DataContainer datos){
        Enum res=null;
        boolean sal=false;
        switch (datos.getFactorAlgoritmo()){
            case 1:
                for(int i=0;i< Fichas3.values().length&&!sal;i++){
                    if(i==ordinal){
                        res= Fichas3.values()[i];
                        sal=true;
                    }
                }
            case 2:
                for(int i=0;i< Fichas5.values().length&&!sal;i++){
                    if(i==ordinal){
                        res= Fichas5.values()[i];
                        sal=true;
                    }
                }
                break;
            case 4:
                for(int i=0;i< Fichas9.values().length&&!sal;i++){
                    if(i==ordinal){
                        res=Fichas9.values()[i];
                        sal=true;
                    }
                }
                break;
        }
        return res;
    }
    
    public static String gestionaPulsadoMaquina(Enum chosen,DataContainer datos){
        return datos.getMapFichasMaquina().get(chosen.name());
    }
}
