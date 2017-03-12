

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import models.*;
import play.test.Fixtures;
import play.test.UnitTest;

public class TesteBasico extends UnitTest {

	/**/
		
	// EXCLUID BANCO DE DADOS DEPOS DE CADA TESTE
	@Before
	public void setup(){
		Fixtures.deleteDatabase();
	}
	
	
	@Test
	public void criarERecuperarUsuario() {
		// save e find são da classe Model
		
		// CRIA UM NOVO USUÁRIO E SALVA ELE
		new Usuario("jackson@gmail.com", "secret", "jackson").save();
		
		// RECUPERA USUÁRIO PELO ENDEREÇO DE EMAIL
		Usuario jack = Usuario.find("byEmail", "jackson@gmail.com").first();
		
		
		//TESTE
		assertNotNull(jack);
		assertEquals("jackson", jack.nomeCompleto);
	}
	
	
	@Test
	public void tenteConectarUsuario(){
		// CRIANDO USUÁRIOE  SALVANDO
		new Usuario("jackson.rs@gmail.com", "secret", "jackson").save();
		
		//Test
		assertNotNull(Usuario.conectar("jackson.rs@gmail.com", "secret"));
		assertNull(Usuario.conectar("jackson.rs@gmail.com", "senhamal"));
		//assertNull(Usuario.conectar("jackson.rs@gmail.com", "secret"));
	}

	
	@Test
	public void criarPost(){
		Usuario jackson = new Usuario("jacksonlira.rs@gmail.com", "123456", "jackson lira").save();
		
		// CRIAR NOVO POST
		new Post(jackson, "Meu primeiro post", "Olá Mundo!").save();
		
		// TESTAR SE O POST TEM CIDO CRIADO
		assertEquals(1, Post.count());
		
		//RECUPERAR TODOS OS POSTES CRIADOS POR UM USUÁRIO
		List<Post> jacksonPosts = Post.find("byAutor", jackson).fetch();
	
		//TESTES
		assertEquals(1, jacksonPosts.size());
		Post primeiropost = jacksonPosts.get(0);
		assertNotNull(primeiropost);
		assertEquals(jackson, primeiropost.autor);
		assertEquals("Meu primeiro post", primeiropost.titulo);
		assertEquals("Olá Mundo!", primeiropost.conteudo);
		assertNotNull(primeiropost.dataPost);
		
	}
	
	@Test
	public void postarComentario(){
		// Create a new user and save it
	    Usuario bob = new Usuario("bob@gmail.com", "secret", "Bob").save();
	
	    //criar novo post
	    Post bobPost = new Post(bob, "Meu primeiro post", "Olá mundo!").save();
	    	
	    // postar o primeiro comentário
	    new Comentario(bobPost, "Jeff", "Ótimo post").save();
	    new Comentario(bobPost, "Tom", "Ótimo post").save();	
	
	    //RECUPERAR OS COMENTÁRIOS
	    List<Comentario> bobPostcomentarios = Comentario.find("byPost", bobPost).fetch();
	    
	    assertEquals(2, bobPostcomentarios.size());
	    
	    Comentario firstComment = bobPostcomentarios.get(0);
	    assertNotNull(firstComment);
	    assertEquals("Jeff", firstComment.autor);
	    assertEquals("Ótimo post", firstComment.conteudo);
	    assertNotNull(firstComment.dataPost);
	 
	    Comentario secondComment = bobPostcomentarios.get(1);
	    assertNotNull(secondComment);
	    assertEquals("Tom", secondComment.autor);
	    assertEquals("Ótimo post", secondComment.conteudo);
	    assertNotNull(secondComment.dataPost);
	}
	
	
		@Test
		public void useOsComentariosRelacao(){
			Usuario jackson = new Usuario("jacksonlira.rs@gmail.com", "secret", "jackson").save();
			
			// Create a new post
		    Post jacksonPost = new Post(jackson, "My first post", "Hello world").save();
		 
		    // Post a first comment
		    jacksonPost.addComentario("Jeff", "Nice post");
		    jacksonPost.addComentario("Tom", "I knew that !");
		 
		    // Count things
		    assertEquals(1, Usuario.count());
		    assertEquals(1, Post.count());
		    assertEquals(2, Comentario.count());
		 
		    // Retrieve Bob's post
		    jacksonPost = Post.find("byAutor	", jackson).first();
		    assertNotNull(jacksonPost);
		 
		    // Navigate to comments
		    assertEquals(2, jacksonPost.comentarios.size());
		    assertEquals("Jeff", jacksonPost.comentarios.get(0).autor);
		    
		    // Delete the post
		    jacksonPost.delete();
		    
		    // Check that all comments have been deleted
		    assertEquals(1, Usuario.count());
		    assertEquals(0, Post.count());
		    assertEquals(0, Comentario.count());
		}
	
		@Test
		public void testTags() {
		    // Create a new user and save it
		    Usuario bob = new Usuario("bob@gmail.com", "secret", "Bob").save();
		 
		    // Create a new post
		    Post bobPost = new Post(bob, "My first post", "Hello world").save();
		    Post anotherBobPost = new Post(bob, "Hop", "Hello world").save();
		 
		    // Well
		   assertEquals(0, Post.findTaggedWith("Red").size());
		 
		    // Tag it now
		    bobPost.tagItWith("Red").tagItWith("Blue").save();
		    anotherBobPost.tagItWith("Red").tagItWith("Green").save();
		 
		    // Check
		    assertEquals(2, Post.findTaggedWith("Red").size());
		    assertEquals(1, Post.findTaggedWith("Blue").size());
		    assertEquals(1, Post.findTaggedWith("Green").size());
		    assertEquals(1, Post.findTaggedWith("Red", "Blue").size());
		    assertEquals(1, Post.findTaggedWith("Red", "Green").size());
		    assertEquals(0, Post.findTaggedWith("Red", "Green", "Blue").size());
		    assertEquals(0, Post.findTaggedWith("Green", "Blue").size());
		    
		    List<Map> cloud = Tag.getCloud();
		    assertEquals(
		        "[{tag=Blue, pound=1}, {tag=Green, pound=1}, {tag=Red, pound=2}]",
		        cloud.toString());
		}
	
}
