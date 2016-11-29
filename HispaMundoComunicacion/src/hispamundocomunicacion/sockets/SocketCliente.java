/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hispamundocomunicacion.sockets;

import entidades.MensajeEnvio;
import entidades.MensajePeticion;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author ANONA
 */
public class SocketCliente extends Thread {

    private final String host;
    private final int port;
    private final String usuario;
    private final String password;

    private final LinkedBlockingQueue<MensajeEnvio> queueMensajeEnvio;
    private final LinkedBlockingQueue<MensajePeticion> queueMensajePeticion;

    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;

    private Runnable hiloEnvia;
    private Runnable hiloRecibe;

    private boolean conectado = false;

    public SocketCliente(String host, int port, LinkedBlockingQueue<MensajeEnvio> queueMensajeEnvio, LinkedBlockingQueue<MensajePeticion> queueMensajePeticion, String usuario, String password) {
        this.host = host;
        this.port = port;
        this.queueMensajeEnvio = queueMensajeEnvio;
        this.queueMensajePeticion = queueMensajePeticion;
        this.usuario = usuario;
        this.password = password;
    }

    public boolean estaConectado() {
        return conectado;
    }

    private void inicia() throws IOException, InterruptedException, ClassNotFoundException {
        Thread.sleep(500);
        socket = new Socket(host, port);
        conectado = true;
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        MensajePeticion mlogin = new MensajePeticion();
        mlogin.setUsuario(usuario);
        mlogin.setAccion("login");
        mlogin.setParametro1(password);
        outputStream.writeObject(mlogin);
        MensajeEnvio m = (MensajeEnvio) inputStream.readObject();
        if (m.getAccion().equals("correcto") && m.getUsuario().equals(usuario)) {
            System.out.print("Loggin Correcto");
        }

        Thread.sleep(1000);

    }

    private void envia() throws IOException {
        while (true) {
            if (!queueMensajePeticion.isEmpty()) {
                outputStream.writeObject(queueMensajePeticion.remove());
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    conectado = false;
                }

            }
        }
    }

    private void recibe() throws IOException, ClassNotFoundException {
        while (true) {

            MensajeEnvio m = (MensajeEnvio) inputStream.readObject();
            queueMensajeEnvio.add(m);

        }
    }

    @Override
    public void run() {
        try {
            inicia();
        } catch (IOException | InterruptedException | ClassNotFoundException ex) {
            ex.printStackTrace();
            conectado = false;
            return;

        }

        hiloEnvia = new Runnable() {
            @Override
            public void run() {
                try {
                    envia();
                } catch (IOException ex) {
                    conectado = false;
                }
            }

        };
        new Thread(hiloEnvia).start();

        hiloRecibe = new Runnable() {
            @Override
            public void run() {
                try {
                    recibe();
                } catch (IOException | ClassNotFoundException ex) {
                    conectado = false;
                }
            }

        };
        new Thread(hiloRecibe).start();

    }

}
