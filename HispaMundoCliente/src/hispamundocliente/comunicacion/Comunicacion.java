/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hispamundocliente.comunicacion;

import hispamundocomunicacion.HispaMundoComunicacion;


/**
 *
 * @author ANONA
 */
public class Comunicacion implements Runnable{

    private final HispaMundoComunicacion hmc;

    
    public Comunicacion(String host, int port, String usuario, String password) {
        hmc = new HispaMundoComunicacion(host, port, usuario, password);
    }
    public void conecta(){
        hmc.inicia();
    }
    
    
    
    

    @Override
    public void run() {
        while (true) {
            if (hmc.estaConectado()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            } else {
                break;
            }
        }
    }

    
}
