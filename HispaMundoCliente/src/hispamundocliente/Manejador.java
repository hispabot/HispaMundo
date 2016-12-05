/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hispamundocliente;

import hispamundocliente.lanterna.pantallas.Inicio;
import hispamundocliente.lanterna.pantallas.PantallaPrincipal;
import hispamundocomunicacion.HispaMundoComunicacion;

/**
 *
 * @author ANONA
 */
public class Manejador {
    private final Inicio  i = new Inicio();
    private PantallaPrincipal pp;

    
    public Manejador(){
        
    }
    
    public void init(){
        i.init();
        //Si ya esta conecado 
        pp = new PantallaPrincipal(i.getHmc(), i.getUsuario());
        pp.inicia();
        
        System.exit(0);
        
        
        
    }
    
}
