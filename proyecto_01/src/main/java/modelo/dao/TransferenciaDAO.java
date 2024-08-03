package modelo.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.Categoria;
import modelo.entidades.Transferencia;

public class TransferenciaDAO  extends MovimientoDAO {
	EntityManager em = null;
	
	public TransferenciaDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	
	public List<Transferencia> findMovimientosByCategoriaTransferencia(Categoria categoria) {
	    String jpql = "SELECT m FROM Transferencia m WHERE m.categoria = :categoria";
	    return em.createQuery(jpql, Transferencia.class)
	             .setParameter("categoria", categoria)
	             .getResultList();
	}

}
