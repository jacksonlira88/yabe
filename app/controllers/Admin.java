package controllers;
 
import play.*;
import play.mvc.*;
import java.util.*;
import controllers.*; 
import models.*;
 
@With(Secure.class)
public class Admin extends Controller {
    
    @Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            Usuario usuario = Usuario.find("byEmail", Security.connected()).first();
            renderArgs.put("usuario", usuario.nomeCompleto);
        }
    }
 
    public static void index() {
    	String usuario = Security.connected();
    	List<Post> posts = Post.find("autor.email", usuario).fetch();
        render(posts);
    }
    
    //
    public static void form(Long id) {
        if(id != null){
        	Post post  = Post.findById(id);
        	render(post);
        }
    	
    	render();
    }
     
    public static void save(Long id, String titulo, String conteudo, String tags) {
        Post post;
    	if(id == null){
        	// criando novo post
        	Usuario autor = Usuario.find("byEmail", Security.connected()).first();
        	post = new Post(autor, titulo, conteudo);
        }else{
        	post = Post.findById(id);
        	//edita
        	post.titulo = titulo;
        	post.conteudo = conteudo;
        	post.tags.clear();
        }
       //definindo conjunto de tag
    	for(String tag : tags.split("\\s+")){
    		if(tag.trim().length() > 0){
    			 post.tags.add(Tag.procuraOuCriaPorNome(tag));
    		}
    	}
    	// Validate
        validation.valid(post);
        if(validation.hasErrors()) {
            render("@form", post);
        }
        // Save
        post.save();
        index();
    	
    }
}