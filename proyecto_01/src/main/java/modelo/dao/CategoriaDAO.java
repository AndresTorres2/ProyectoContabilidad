package modelo.dao;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.TypedQuery;
import modelo.entidades.Categoria;
import modelo.entidades.Cuenta;

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
    public List<Categoria> findAllByUserId(int usuarioId) {
        try {
            // SQL query para obtener todas las categorías asociadas a un usuario específico
            String sql = "SELECT DISTINCT c.* " +
                         "FROM Categoria c " +
                         "JOIN Movimiento m ON c.IDCATEGORIA = m.categoria " +
                         "JOIN Cuenta cu ON m.origen = cu.idCuenta OR m.destino = cu.idCuenta " +
                         "WHERE cu.propietario = ?";
            
            // Crear la consulta nativa
            Query query = em.createNativeQuery(sql, Categoria.class);
            query.setParameter(1, usuarioId); // Establecer el parámetro del id del usuario
            
            // Ejecutar la consulta y obtener los resultados
            List<Categoria> categorias = query.getResultList();
            
            return categorias;
            
        } catch (Exception e) {
            e.printStackTrace(); // Registrar o manejar otras excepciones
            return Collections.emptyList(); // Retornar una lista vacía en caso de error
        }
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
    
    public void eliminarCategoria(int idCategoria) {
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            System.out.println("Intentando eliminar la categoría con ID: " + idCategoria);

            // Buscar la categoría por su ID
            Categoria categoria = em.find(Categoria.class, idCategoria);
            
            if (categoria != null) {
                // Eliminar la categoría
                em.remove(categoria);
            } else {
                throw new IllegalArgumentException("Categoría con ID " + idCategoria + " no encontrada.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al eliminar la categoría", e);
        }
    }
	
    
    
}
