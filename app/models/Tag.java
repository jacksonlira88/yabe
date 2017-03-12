package models;
 
import java.util.*;
import javax.persistence.*;

import play.data.validation.Required;
import play.db.jpa.*;
 
@Entity
public class Tag extends Model implements Comparable<Tag> {
 
	@Required
    public String nome;
 
    private Tag(String nome) {
        this.nome = nome;
    }
 
    public String toString() {
        return nome;
    }
 
    public int compareTo(Tag outraTag) {
        return nome.compareTo(outraTag.nome);
    }
    
    public static Tag procuraOuCriaPorNome(String nome) {
	    Tag tag = Tag.find("byNome", nome).first();
	    if(tag == null) {
	        tag = new Tag(nome);
	    }
	    return tag;
	}

    //uma núvem de tags
    public static List<Map> getCloud() {
        List<Map> result = Tag.find(
            "select new map(t.nome as tag, count(p.id) as pound) from Post p join p.tags as t group by t.nome order by t.nome"
        ).fetch();
        return result;
    }
	
}