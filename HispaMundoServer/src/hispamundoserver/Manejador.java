/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hispamundoserver;

import hilos.ProcesaComandos;
import java.util.concurrent.LinkedBlockingQueue;
import mundo.Mundo;
import sockets.Servidor;

/**
 *
 * @author ANONA
 */
public class Manejador {

    private final Mundo m;
    private LinkedBlockingQueue<String> queueComandos;

    public Manejador() {
        m = new Mundo();
        m.generaMundo();
    }

    void init() {

        queueComandos = new LinkedBlockingQueue<String>();

        ProcesaComandos pc = new ProcesaComandos(queueComandos);
        (new Thread(pc)).start();

        Servidor s = new Servidor(4011, queueComandos, m);
        s.start();

    }

}
