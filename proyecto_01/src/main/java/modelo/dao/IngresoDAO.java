package modelo.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.Categoria;
import modelo.entidades.Egreso;
import modelo.entidades.Ingreso;

public class IngresoDAO extends MovimientoDAO {

	
	EntityManager em = null;
	
	public IngresoDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	
	public List<Ingreso> findMovimientosByCategoriaIngreso(Categoria categoria) {
	    String jpql = "SELECT m FROM Ingreso m WHERE m.categoria = :categoria";
	    return em.createQuery(jpql, Ingreso.class)
	             .setParameter("categoria", categoria)
	             .getResultList();
	}
	
	
}
