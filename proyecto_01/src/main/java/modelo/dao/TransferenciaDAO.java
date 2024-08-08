package modelo.dao;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import modelo.entidades.Categoria;
import modelo.entidades.Cuenta;
import modelo.entidades.Egreso;
import modelo.entidades.Movimiento;
import modelo.entidades.Transferencia;

public class TransferenciaDAO  extends MovimientoDAO {
	EntityManager em = null;
	
	public TransferenciaDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	
	public List<Movimiento> findMovimientosByCategoriaTransferencia(int categoria, int propietario ,Timestamp fechaInicio, Timestamp fechaFin) {
		String sql = "SELECT m.* FROM movimiento m JOIN cuenta c ON m.origen = c.idCuenta WHERE m.categoria = ?  AND c.propietario = ?"
					+ " AND m.fecha BETWEEN ?3 AND ?4 ;";
	    Query query = em.createNativeQuery(sql, Movimiento.class)
	             .setParameter(1, categoria)
	             .setParameter(2, propietario)
	             .setParameter(3, fechaInicio)
			     .setParameter(4, fechaFin);
	    List<Movimiento> movimiento = query.getResultList();
	    return movimiento;
	}
	/*public List<Transferencia> getAllMovements() {
        
		return em.createQuery("SELECT m FROM Transferencia m", Transferencia.class).getResultList();
    }*/
	
	public List<Movimiento> getMovimientosByCuenta(Cuenta cuenta, Timestamp fechaInicio, Timestamp fechaFin) {
	    try {
	        // JPQL para obtener movimientos por la cuenta y el rango de fechas
	        String jpql = "SELECT t FROM Transferencia t WHERE t.origen = :cuenta " +
	                      "AND t.fecha BETWEEN :fechaInicio AND :fechaFin";
	        
	        // Crear la consulta
	        TypedQuery<Movimiento> query = em.createQuery(jpql, Movimiento.class);
	        
	        // Establecer los parámetros de consulta
	        query.setParameter("cuenta", cuenta);
	        query.setParameter("fechaInicio", fechaInicio);
	        query.setParameter("fechaFin", fechaFin);
	        
	        // Ejecutar la consulta y devolver los resultados
	        return query.getResultList();
	    } catch (Exception e) {
	        e.printStackTrace(); // Registrar o manejar otras excepciones
	        return Collections.emptyList(); // Retornar una lista vacía en caso de error
	    }
	}

	public void createTransferencia(Transferencia transferencia) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(transferencia); // Inserta el nuevo ingreso en la base de datos
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error al crear la Transferencia", e);
        }
    }
}
