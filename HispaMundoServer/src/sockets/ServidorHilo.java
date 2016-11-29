/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.*;
import mundo.Mundo;
import entidades.MensajeEnvio;
import entidades.MensajePeticion;
import procesos.AutenticacionUsuarios;
import procesos.ComandosJugador;

/**
 *
 * @author ANONA
 */
public class ServidorHilo extends Thread {

    private Socket socket;
    private ObjectOutputStream dos; 
    
    private ObjectInputStream dis;
    private int idSessio;
    private LinkedBlockingQueue<String> queueComandos;
    private Mundo mundo;
    private ComandosJugador cj;

    public ServidorHilo(Socket socket, int id, LinkedBlockingQueue<String> queueComandos, Mundo mundo) {
        this.queueComandos = queueComandos;
        this.socket = socket;
        this.idSessio = id;
        this.mundo = mundo;
        this.cj = new ComandosJugador();
        try {
            dos = new ObjectOutputStream(socket.getOutputStream());
            dis = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desconnectar() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        //mundo.imprime("Cliente_"+this.idSessio);
        //String accion = "";
        MensajePeticion  mensajePeticion;
        while (true) {
            try {
                Object o = (MensajePeticion)dis.readObject();
                mensajePeticion = (MensajePeticion)o;
                MensajeEnvio me = procesaPeticion(mensajePeticion);
                if(me.getAccion().equals("incorrecto")){
                    return;
                }
               dos.writeObject(me);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("El cliente con idSesion " + this.idSessio + " Termino");
                break;
            }
        }

        desconnectar();
    }
    
    
    private MensajeEnvio procesaPeticion(MensajePeticion mp){
        MensajeEnvio envio = new MensajeEnvio();
        envio.setUsuario(mp.getUsuario());
        switch (mp.getAccion()) {
            case "login":
                //logica de autenticacion
                AutenticacionUsuarios au = new AutenticacionUsuarios();
                if( au.autenticaUsuario(mp.getUsuario(), mp.getParametro1())){
                    envio.setAccion("correcto");
                }else{
                    envio.setAccion("incorrecto");
                }   break;
            case "dameMapaInical":
                envio.setAccion("envioMundoInicial");
                if(cj.esNuevoUsuario()){
                    // agrega la posicion inicial
                   int[] posJugador = mundo.agregaUsuario(mp.getUsuario());
                   
                    envio.setFraccion(mundo.getMundoInicial(posJugador));
                }
                else{
                    envio.setFraccion(cj.getMundoDescubierto());
                }
                break;
            case "getMundo":
                envio.setAccion("envioMundo");
                int[][] mundo_fraccion = this.mundo.getMundo(envio.getPos_x(), envio.getPos_y(), envio.getSize_x(), envio.getSize_y());
                    for (int[] mundo_fraccion1 : mundo_fraccion) {
                        for (int j = 0; j < mundo_fraccion1.length; j++) {
                            System.out.print(mundo_fraccion1[j]);
                        }
                        System.out.print("\n");
                    }
                envio.setFraccion(mundo_fraccion);
                    
                break;
            
                
            default:
                envio.setAccion("Comando desconocido");
                break;
        }
        
        return envio;
    }
    
}
