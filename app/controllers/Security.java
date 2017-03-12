package controllers;

import models.*;

public class Security extends Secure.Security {

    static boolean authentify(String email, String senha) {
        return Usuario.conectar(email, senha) != null;
    }
    
    static boolean check(String perfil) {
        if("admin".equals(perfil)) {
            return Usuario.find("byEmail", connected()).<Usuario>first().eAdmin;
        }
        return false;
    }
    
    static void onDisconnected() {
        Application.index();
    }
    
    static void onAuthenticated() {
        Admin.index();
    }
    
    
}
