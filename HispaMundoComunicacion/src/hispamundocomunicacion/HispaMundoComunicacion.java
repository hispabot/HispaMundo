/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hispamundocomunicacion;

import entidades.MensajeEnvio;
import entidades.MensajePeticion;
import hispamundocomunicacion.sockets.SocketCliente;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANONA
 */
public class HispaMundoComunicacion {

    private final String host;
    private final int port;
    private final String usuario;
    private final String password;

    private LinkedBlockingQueue<MensajeEnvio> queueMensajeEnvio;
    private LinkedBlockingQueue<MensajePeticion> queueMensajePeticion;

    private final SocketCliente socketCliente;

    public HispaMundoComunicacion(String host, int port, String usuario, String password) {

        this.host = host;
        this.port = port;
        this.usuario = usuario;
        this.password = password;
        queueMensajeEnvio = new LinkedBlockingQueue<>();
        queueMensajePeticion = new LinkedBlockingQueue<>();
        socketCliente = new SocketCliente(host, port, queueMensajeEnvio, queueMensajePeticion, usuario, password);

    }

    public void inicia() {
        socketCliente.run();
    }

    public boolean estaConectado() {
        return socketCliente.estaConectado();
    }

    public MensajeEnvio getMensajeEnvio() {

        MensajeEnvio me = new MensajeEnvio();
        while (true) {
            if (!queueMensajeEnvio.isEmpty()) {
                me = queueMensajeEnvio.remove();
                break;
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }

        return me;
    }

    public void enviaPeticion(MensajePeticion mensajePeticion) {
        queueMensajePeticion.add(mensajePeticion);
    }

}
