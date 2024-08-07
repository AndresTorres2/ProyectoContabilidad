package modelo.dao;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import modelo.entidades.Categoria;
import modelo.entidades.Cuenta;
import modelo.entidades.Egreso;
import modelo.entidades.Ingreso;
import modelo.entidades.Movimiento;

public class EgresoDAO extends MovimientoDAO {

	EntityManager em = null;
	
	public EgresoDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	public List<Movimiento> findMovimientosByCategoriaEgreso(Categoria categoria) {
	    String jpql = "SELECT m FROM Egreso m WHERE m.categoria = :categoria";
	    return em.createQuery(jpql, Movimiento.class)
	             .setParameter("categoria", categoria)
	             .getResultList();
	}
	/*public List<Egreso> getAllMovements() {
        
		return em.createQuery("SELECT m FROM Egreso m", Egreso.class).getResultList();
    }*/
	
	public List<Movimiento> getMovimientosByCuenta(Cuenta cuenta) {
        try {
            // JPQL para obtener movimientos por el ID de la cuenta
            String jpql = "SELECT e FROM Egreso e WHERE e.origen = :cuenta";
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
	
	public void createEgreso(Egreso egreso) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(egreso); // Inserta el nuevo ingreso en la base de datos
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error al crear el egreso", e);
        }
    }
	
}
