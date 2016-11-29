/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package procesos;

/**
 *
 * @author ANONA
 */
public class ComandosJugador {
    
    private String usuario;
    
    
    public int[][] getMundoDescubierto(){
        int[][]mundo = new int[100][100];
        //Consulta a la base de lo ultimo visto
        return null;
    }
    
    public boolean esNuevoUsuario(){
        //validar si es un nuevo usuario
        return true;
    }
    
    public boolean guardarJuego(int[][] mundo){
        //guarada el progreso del jugador
        return true;
    }
    
     public boolean autenticaUsuario(String user, String pass){
        // aqui va todo la autenticacion de usuarios
        return user.equals(pass);
    }
}
