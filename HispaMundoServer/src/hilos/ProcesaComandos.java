/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANONA
 */
public class ProcesaComandos implements Runnable{
    
    private LinkedBlockingQueue <String> queueComandos;
    private boolean running;
    private LinkedBlockingQueue <String> queueSalidas;
    
    public ProcesaComandos(LinkedBlockingQueue<String> queueComandos){
        this.queueComandos = queueComandos;
        this.running = true;
        
        
                
    }

    @Override
    public void run() {
        System.out.println("Hilos procesando comandos, iniciado" );
        while(true){
            if(!queueComandos.isEmpty()){
                System.out.println("Comando procesado->" + queueComandos.remove());
            }else{
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
    
}
