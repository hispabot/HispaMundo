/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ANONA
 */
public class MensajeEnvio implements Serializable {

    private static final long serialVersionUID = 5950169519310163575L;

    private String usuario;
    private String accion;
    
    private int pos_x;
    private int pos_y;
    private int size_x;
    private int size_y;
    private int[][] fraccion;
    private List<String> listaCambios;

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
     * @return the accion
     */
    public String getAccion() {
        return accion;
    }

    /**
     * @param accion the accion to set
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }

    /**
     * @return the pos_x
     */
    public int getPos_x() {
        return pos_x;
    }

    /**
     * @param pos_x the pos_x to set
     */
    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    /**
     * @return the pos_y
     */
    public int getPos_y() {
        return pos_y;
    }

    /**
     * @param pos_y the pos_y to set
     */
    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }

    /**
     * @return the size_x
     */
    public int getSize_x() {
        return size_x;
    }

    /**
     * @param size_x the size_x to set
     */
    public void setSize_x(int size_x) {
        this.size_x = size_x;
    }

    /**
     * @return the size_y
     */
    public int getSize_y() {
        return size_y;
    }

    /**
     * @param size_y the size_y to set
     */
    public void setSize_y(int size_y) {
        this.size_y = size_y;
    }

    /**
     * @return the fraccion
     */
    public int[][] getFraccion() {
        return fraccion;
    }

    /**
     * @param fraccion the fraccion to set
     */
    public void setFraccion(int[][] fraccion) {
        this.fraccion = fraccion;
    }

    /**
     * @return the listaCambios
     */
    public List<String> getListaCambios() {
        return listaCambios;
    }

    /**
     * @param listaCambios the listaCambios to set
     */
    public void setListaCambios(List<String> listaCambios) {
        this.listaCambios = listaCambios;
    }

}
