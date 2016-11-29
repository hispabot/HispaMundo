/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import mundo.Mundo;

/**
 *
 * @author ANONA
 */
public class Servidor  extends Thread{
    private final int puerto;
    private ServerSocket ss;
    private final LinkedBlockingQueue<String> queueComandos;
    private final Mundo mundo;
    
    public Servidor(int puerto, LinkedBlockingQueue<String> queueComandos, Mundo mundo){
        this.puerto = puerto;
        this.queueComandos = queueComandos;
        this.mundo = mundo;
    }
    
    @Override
    public void run(){
        System.out.print("Inicializando servidor... ");
         try {
            ss = new ServerSocket(puerto);
            System.out.println("\t[OK]");
            int idSession = 0;
            while (true) {
                Socket socket;
                socket = ss.accept();
                System.out.println("Nueva conexión entrante: "+socket);
                ((ServidorHilo) new ServidorHilo(socket, idSession, queueComandos,mundo)).start();
                idSession++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
