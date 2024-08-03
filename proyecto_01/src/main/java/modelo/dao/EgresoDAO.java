package modelo.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.Categoria;
import modelo.entidades.Egreso;

public class EgresoDAO extends MovimientoDAO {

	EntityManager em = null;
	
	public EgresoDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	public List<Egreso> findMovimientosByCategoriaEgreso(Categoria categoria) {
	    String jpql = "SELECT m FROM Egreso m WHERE m.categoria = :categoria";
	    return em.createQuery(jpql, Egreso.class)
	             .setParameter("categoria", categoria)
	             .getResultList();
	}
}
