/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import hispamundocomunicacion.HispaMundoComunicacion;
import entidades.MensajeEnvio;
import entidades.MensajePeticion;

/**
 *
 * @author ANONA
 */
public class Pruebas {

    /**
     * @param args the command line arguments
     */
    static HispaMundoComunicacion hmc;
    
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        String host = "127.0.0.1";
        int port = 4011;
        String usuario = "demo";
        String password = "demo";
        
        hmc = new HispaMundoComunicacion(host, port, usuario, password);
        hmc.inicia();
        if (!hmc.estaConectado()) {
            System.err.println("No conectado");
            System.exit(-1);
        }
        MensajePeticion mp = new MensajePeticion();
        mp.setUsuario(usuario);
        mp.setAccion("dameMapaInical");
        
        hmc.enviaPeticion(mp);
        
        MensajeEnvio me = new MensajeEnvio();
        me = hmc.getMensajeEnvio();
        System.out.print("Hola mundo");
        
        int[][] mundo = me.getFraccion();
        
        for (int i = 0; i < mundo.length; i++) {
            int[] is = mundo[i];
            for (int j = 0; j < is.length; j++) {
                int k = is[j];
                System.out.print(k);
            }
            System.out.print("\n");
        }
        
        while (true) {
            if (hmc.estaConectado()) {
                Thread.sleep(100);
            } else {
                System.exit(-1);
            }
        }
    }
    
}
