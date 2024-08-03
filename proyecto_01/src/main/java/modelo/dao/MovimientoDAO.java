package modelo.dao;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import modelo.entidades.Categoria;
import modelo.entidades.Cuenta;
import modelo.entidades.Movimiento;

public class MovimientoDAO {

	EntityManager em = null;
	
	public MovimientoDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	
	public void createMovimiento(Movimiento movimiento) {
        em.getTransaction().begin();
        em.persist(movimiento);
        em.getTransaction().commit();
    }
	public  List<Movimiento> getAllMovements() {
        
		return em.createQuery("SELECT m FROM Movimiento m", Movimiento.class).getResultList();
    }
	public Movimiento findMovimientoById(int id) {
        return em.find(Movimiento.class, id);
    }
	
	
	public List<Movimiento> getMovimientosByCuenta(Cuenta cuenta) {
        try {
            // JPQL para obtener movimientos por el ID de la cuenta
            String jpql = "SELECT m FROM Movimiento m WHERE m.origen = :cuenta";
            // Crear la consulta
            TypedQuery<Movimiento> query = em.createQuery(jpql, Movimiento.class);
            // Establecer el parámetro de consulta
            query.setParameter("cuenta", cuenta);
            // Ejecutar la consulta y devolver los resultados
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Registrar o manejar otras excepciones
            return Collections.emptyList(); // Retornar una lista vacía en caso de error
        }
    }

	/*public List<Movimiento> findMovimientosByCategoria(Categoria categoria) {
	    try {
	        // Crear la consulta JPQL para obtener movimientos asociados a la categoría
	        String jpql = "SELECT m FROM Movimiento m WHERE m.categoria = :categoria";
	        TypedQuery<Movimiento> query = em.createQuery(jpql, Movimiento.class);
	        query.setParameter("categoria", categoria);
	        
	        // Ejecutar la consulta y devolver los resultados
	        return query.getResultList();
	    } catch (Exception e) {
	        e.printStackTrace(); // Registrar o manejar otras excepciones
	        return Collections.emptyList(); // Retornar una lista vacía en caso de error
	    }
	}*/

	
}
