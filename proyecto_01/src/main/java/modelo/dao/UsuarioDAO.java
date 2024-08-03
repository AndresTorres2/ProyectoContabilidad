package modelo.dao;

import java.io.Serializable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.Usuario;

public class UsuarioDAO  {

	EntityManager em = null;
	
	public UsuarioDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	 public Usuario findUsuarioById(int id) {
	        return em.find(Usuario.class, id);
	    }
}
