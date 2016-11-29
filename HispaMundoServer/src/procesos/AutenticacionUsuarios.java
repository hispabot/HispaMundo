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
public class AutenticacionUsuarios {
    
    
    public boolean autenticaUsuario(String user, String pass){
        // aqui va todo la autenticacion de usuarios
        return user.equals(pass);
    }
}
