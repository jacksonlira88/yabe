package models;

import javax.persistence.Entity;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;



@Entity
public class Usuario  extends Model{
		// anotando para validação do administrador
		@Email
    	@Required
        public String email;
		
		@Required
        public String senha;
        public String nomeCompleto;
        public boolean eAdmin;


    public Usuario (String email, String senha, String nomeCompleto) {
        this.email = email;
        this.senha = senha;
        this.nomeCompleto = nomeCompleto;
    }
    
    
    
   public static Usuario conectar(String email, String senha){
	   return find("byEmailAndSenha", email, senha).first();
   }

   public String toString() {
	    return email;
	}
   
   

}