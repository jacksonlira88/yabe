package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.*;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;


@Entity
public class Post extends Model {
	
	@Required
	public String titulo;
	@Required
	public Date dataPost;
	
	@Lob
	@Required
	@MaxSize(1000)
	public String conteudo;
	
	@Required
	@ManyToOne
	public Usuario autor;
	
	@OneToMany(mappedBy="post", cascade=CascadeType.ALL)
	public List<Comentario> comentarios;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	public Set<Tag> tags;
	
	public Post(Usuario autor, String titulo, String conteudo){
		this.comentarios = new ArrayList<Comentario>();
		this.tags = new TreeSet<Tag>();
		this.autor = autor;
		this.titulo = titulo;
		this.conteudo = conteudo;
		this.dataPost = new Date();
	}
	
	
	public Post addComentario(String autor, String conteudo){
		Comentario novoComentario = new Comentario(this, autor, conteudo).save();
		this.comentarios.add(novoComentario);
		this.save();
		return this;
	}
	
	public Post previous() {
	    return Post.find("dataPost < ? order by dataPost desc", dataPost).first();
	}
	 
	public Post next() {
	    return Post.find("dataPost > ? order by dataPost asc", dataPost).first();
	}
	
	// MARCA UM POST COM UMA TEG
	public Post tagItWith(String name) {
	    tags.add(Tag.procuraOuCriaPorNome(name));
	    return this;
	}
	
	/*
	 *
	}RECUPERA TODOS OS POSTS POR UMA TAG ESPEFÍFICA
		public static List<Post> findTaggedWith(String tag) {
		    return Post.find(
		        "select distinct p from Post p join p.tags as t where t.nome = ?", tag
		    ).fetch();

	 * */
	
	public static List<Post> findTaggedWith(String... tags) {
	    return Post.find(
	            "select distinct p from Post p join p.tags as t where t.nome in (:tags) group by p.id, p.autor, p.titulo, p.conteudo,p.dataPost having count(t.id) = :size"
	    ).bind("tags", tags).bind("size", tags.length).fetch();
	
	 // 
		}
	
	// RECUPERA TODOS OS POSTS POR UMA TAG ESPEFÍFICA
	
	public String toString() {
	    return titulo;
	}
	
}
