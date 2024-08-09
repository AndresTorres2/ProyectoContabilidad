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
import modelo.entidades.Ingreso;
import modelo.entidades.Movimiento;
import modelo.entidades.Usuario;

public class IngresoDAO extends MovimientoDAO {

	
	EntityManager em = null;
	
	public IngresoDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	
	public List<Movimiento> findMovimientosByCategoriaIngreso(int categoria, int propietario,Timestamp fechaInicio, Timestamp fechaFin) {
		 String sql = "SELECT m.* FROM movimiento m " +
                 "JOIN cuenta c ON m.destino = c.idCuenta " +
                 "WHERE m.categoria = ?1 " +
                 "AND c.propietario = ?2 " +
                 "AND m.fecha BETWEEN ?3 AND ?4";

	    // Crear la consulta nativa
	    Query query = em.createNativeQuery(sql, Movimiento.class)
	                     .setParameter(1, categoria)
	                     .setParameter(2, propietario)
	                     .setParameter(3, fechaInicio)
	                     .setParameter(4, fechaFin);
	    
	    // Ejecutar la consulta y obtener los resultados
	    List<Movimiento> movimientos = query.getResultList();
	    return movimientos;
	}
	
	public List<Movimiento> getMovimientosByCuenta(Cuenta cuenta, Timestamp fechaInicio, Timestamp fechaFin) {
	    try {
	        // JPQL para obtener movimientos por la cuenta y el rango de fechas
	        String jpql = "SELECT i FROM Ingreso i WHERE i.destino = :cuenta " +
	                      "AND i.fecha BETWEEN :fechaInicio AND :fechaFin";
	        
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
	
	public void createIngreso(Ingreso ingreso) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(ingreso); // Inserta el nuevo ingreso en la base de datos
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error al crear el ingreso", e);
        }
    }
	
	/*public List<Ingreso> getAllMovements() {
        
		return em.createQuery("SELECT m FROM Ingreso m", Ingreso.class).getResultList();
    }*/
	
	public Integer getCuentaIdByIngresoId(int idIngreso) {
		String query = "SELECT m.destino.idCuenta FROM Ingreso m WHERE m.idMovimiento = :idIngreso";
        TypedQuery<Integer> typedQuery = em.createQuery(query, Integer.class);
        typedQuery.setParameter("idIngreso", idIngreso);
         
        return typedQuery.getSingleResult();
    }

	public Integer getCategoriaIdByIngresoId(int idMovimiento) {
		String query = "SELECT m.categoria.idCategoria FROM Ingreso m WHERE m.idMovimiento = :idMovimiento";
        TypedQuery<Integer> typedQuery = em.createQuery(query, Integer.class);
        typedQuery.setParameter("idMovimiento", idMovimiento);
         
        return typedQuery.getSingleResult();
	}

	public void updateIngreso(Ingreso ingreso) {
		EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(ingreso); // Inserta el nuevo ingreso en la base de datos
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error al actualizar el ingreso", e);
        }
		
	}
}
