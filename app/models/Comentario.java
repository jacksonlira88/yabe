package models;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;


@Entity
public class Comentario extends Model{
	
	@Required
	public String autor;
	
	@Required
	public Date dataPost;
	
	@Lob
	@Required
	public String conteudo;
	
	
	@ManyToOne
	@Required
	public Post post;
		
	
	public Comentario(Post post, String autor, String conteudo){
		this.post = post;
		this.autor = autor;
		this.conteudo = conteudo; 
		this.dataPost = new Date();
	}
	
	
	
}
