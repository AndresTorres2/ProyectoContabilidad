package modelo.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
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
	
	 
	 public  Usuario authenticate(String username,String password) {
			
		 try {
			 String jpql = "SELECT u FROM Usuario u WHERE u.nombre = :username AND u.clave = :password";
				Query consulta =  em.createQuery(jpql);
				consulta.setParameter("username", username);
				consulta.setParameter("password", password);
				Usuario usuarioAutenticado =  (Usuario) consulta.getSingleResult();
				return usuarioAutenticado;
		 }catch(NoResultException e) {
			 return null;
		 	}catch(Exception e) {
		 		e.printStackTrace();
		 		return null;
			 	}
		
			
			
			
		}
	 
	 public  void create(Usuario usuario) {
			em.getTransaction().begin();
			try {
				em.persist(usuario);
				em.getTransaction().commit();
				
			}catch(Exception e){
				if(em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			}
		} 
}
