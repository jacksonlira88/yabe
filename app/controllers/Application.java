package controllers;

import play.*;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.*;
import play.cache.*;
import java.util.*;

import models.*;

public class Application extends Controller {

	@Before
	static void addDefaults() {
	    renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
	    renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
	}
	
	public static void index() {
    	Post postFrente = Post.find("order by dataPost desc").first();
    	List<Post> postVelhos = Post.find("order by dataPost desc").from(1).fetch();
    	render(postFrente, postVelhos);
    }
	
	public static void show(Long id) {
	    Post post = Post.findById(id);
	    String randomID = Codec.UUID();
	    render(post, randomID);
	}
	
	public static void postComentario(
			Long postId, 
	        @Required(message="Autor é obrigatorio") String autor, 
	        @Required(message="Mensagem é obrigatoria") String conteudo, 
	        @Required(message="Entre com o codigo") String code, 
	        String randomID) {
		Post post = Post.findById(postId);
	    validation.equals(code, Cache.get(randomID)).message("Código invalido. Por favor, tente novamente");
	    if(validation.hasErrors()) {
	        render("Application/show.html", post, randomID);
	    }
	    post.addComentario(autor, conteudo);
	    flash.success("Obrigador por postar, %s", autor);
	    Cache.delete(randomID);
	    show(postId);
	}
	
	//	
	public static void captcha(String id) {
		Images.Captcha captcha = Images.captcha();
	    String code = captcha.getText("#E4EAFD");
	    Cache.set(id, code, "10mn");
	    renderBinary(captcha);
	}

	public static void listTagged(String tag) {
	    List<Post> posts = Post.findTaggedWith(tag);
	    render(tag, posts);
	}
}