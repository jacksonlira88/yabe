import models.Usuario;
import play.jobs.Job;
import play.jobs.Job.*;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;


@OnApplicationStart
public class Bootstrap extends Job{
	public void doJob(){
		Fixtures.delete();
		// CHECAR SE O BANCO DE DADOS ESTÁ VAZIO
		if(Usuario.count() == 0){
			
			Fixtures.loadModels("initial-data.yml");
		}
	}
	
}
