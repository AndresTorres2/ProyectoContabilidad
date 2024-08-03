package modelo.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import modelo.entidades.Categoria;

public class CategoriaDAO {
	EntityManager em = null;
	
	public CategoriaDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	
	public void saveCategoria(Categoria categoria) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(categoria);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public Categoria findCategoriaById(int id) {
        return em.find(Categoria.class, id);
    }
    public List<Categoria> findAll() {
        return em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
    }
    
    
    public List<Categoria> getCategoriasByType(String tipo) {
        return em.createQuery("SELECT c FROM Categoria c WHERE c.categoriaType = :tipo", Categoria.class)
                 .setParameter("tipo", tipo)
                 .getResultList();
    }
    public List<Categoria> getCategorias() {
        TypedQuery<Categoria> query = em.createQuery("SELECT c FROM Categoria c", Categoria.class);
        return query.getResultList();
    }
    public Categoria getCategoriaById(int id) {
        return em.find(Categoria.class, id);
    }
	
    
    
}
