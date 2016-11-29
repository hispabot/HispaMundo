/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hispamundocliente;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author ANONA
 */
public class Configuracion {
    // TODO code application logic here

    private final String fileConfig = "configuracion.properties";
    private boolean existeArchivo = true;
    private String host;
    private int port;
    private String usuario;
    private String password;

    public Configuracion() {
        Properties properties = new Properties();

        try {
            InputStream propsFile = null;
            propsFile = new FileInputStream(fileConfig);
            properties.load(propsFile);
            host = properties.getProperty("host");
            port = Integer.parseInt(properties.getProperty("port"));
            usuario = properties.getProperty("usuario");
            password = properties.getProperty("password");
        } catch (Exception e1) {

            existeArchivo = false;
        }

    }

    private void actualizaArchivo() {
        try {
            BufferedWriter writer00 = new BufferedWriter(new FileWriter(fileConfig, false));

            writer00.write("host=" + getHost() + "\n");
            writer00.write("port=" + getPort() + "\n");
            writer00.write("usuario=" + getUsuario() + "\n");
            writer00.write("password=" + getPassword() + "\n");
            writer00.close();
        } catch (IOException ex) {

        }
    }

    /**
     * @return the existeArchivo
     */
    public boolean isExisteArchivo() {
        return existeArchivo;
    }

    /**
     * @param existeArchivo the existeArchivo to set
     */
    public void setExisteArchivo(boolean existeArchivo) {
        this.existeArchivo = existeArchivo;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
