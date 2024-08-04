package modelo.dao;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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
	
	public List<Movimiento> findMovimientosByCategoriaTransferencia(Categoria categoria) {
	    String jpql = "SELECT m FROM Transferencia m WHERE m.categoria = :categoria";
	    return em.createQuery(jpql, Movimiento.class)
	             .setParameter("categoria", categoria)
	             .getResultList();
	}
	/*public List<Transferencia> getAllMovements() {
        
		return em.createQuery("SELECT m FROM Transferencia m", Transferencia.class).getResultList();
    }*/
	
	public List<Movimiento> getMovimientosByCuenta(Cuenta cuenta) {
        try {
            // JPQL para obtener movimientos por el ID de la cuenta
            String jpql = "SELECT t FROM Transferencia t WHERE t.origen = :cuenta";
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

}
