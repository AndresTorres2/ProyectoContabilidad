package modelo.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import modelo.dto.MovimientoDTO;

import modelo.entidades.Categoria;
import modelo.entidades.Cuenta;
import modelo.entidades.Egreso;
import modelo.entidades.Ingreso;
import modelo.entidades.Movimiento;
import modelo.entidades.Transferencia;

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
	
	public Movimiento findMovimientoById(int id) {
        return em.find(Movimiento.class, id);
    }
	
	public List<Movimiento> getAllMovements() {
        
		return em.createQuery("SELECT m FROM Movimiento m", Movimiento.class).getResultList();
    }
	public List<Movimiento> getAllMovementsByUserId(int usuarioId) {
		String sql = "SELECT m.* FROM Movimiento m " +
                "LEFT JOIN Cuenta c_origen ON m.origen = c_origen.idCuenta " +
                "LEFT JOIN Cuenta c_destino ON m.destino = c_destino.idCuenta " +
                "WHERE c_origen.propietario = ? OR c_destino.propietario = ?";
   
	   Query query = em.createNativeQuery(sql, Movimiento.class);
	   query.setParameter(1, usuarioId);
	   query.setParameter(2, usuarioId);
	   return query.getResultList();
	}
	

	
	//Ya no se usa
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
	
	
	 private  MovimientoDTO toMovimientoDTO(Movimiento movimiento) {
	        int idMovimiento = movimiento.getIdMovimiento();
	        String concepto = movimiento.getConcepto();
	        Date fecha = movimiento.getFecha();
	        Double monto = movimiento.getMonto();
	        
	        String categoria = "";
	        String origen = "";
	        String destino = "";

	        if (movimiento instanceof Egreso) {
	            Egreso egreso = (Egreso) movimiento;
	            categoria = egreso.getCategoria().getNombreCategoria();
	            origen = egreso.getOrigen().getNombreCuenta(); // Obtener el origen
	            destino = "EGRESO"; // Valor predeterminado para Egreso
	        } else if (movimiento instanceof Ingreso) {
	            Ingreso ingreso = (Ingreso) movimiento;
	            categoria  =ingreso.getCategoria().getNombreCategoria();
	            destino = ingreso.getDestino().getNombreCuenta(); // Obtener el destino
	            origen = "INGRESO"; // Valor predeterminado para Ingreso
	        }else if (movimiento instanceof Transferencia) {
	            Transferencia transferencia = (Transferencia) movimiento;
	            categoria = transferencia.getCategoria().getNombreCategoria();
	            origen = transferencia.getOrigen().getNombreCuenta(); // Obtener la cuenta de origen
	            destino = transferencia.getDestino().getNombreCuenta(); // Obtener la cuenta de destino
	             // Valor predeterminado para Transferencia
	        }

	        return new MovimientoDTO(idMovimiento, concepto, fecha, monto, categoria, origen, destino);
	    }
	    
	    
	    public List<MovimientoDTO> getAllMovementsDTO(List<Movimiento> movimientos) {
	        List<MovimientoDTO> movimientosDTO = new ArrayList<>();

	        for (Movimiento movimientoNew : movimientos) {
	            MovimientoDTO dto = toMovimientoDTO(movimientoNew);
	            movimientosDTO.add(dto);
	        }

	        return movimientosDTO;
	    }
	
	public void deleteMovimiento(int idMovimiento) {
		EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            System.out.println("Intentando eliminar la categoría con ID: " + idMovimiento);

            // Buscar la categoría por su ID
            Movimiento movimiento = em.find(Movimiento.class, idMovimiento);
            
            if (movimiento != null) {
                // Eliminar la categoría
                em.remove(movimiento);
            } else {
                throw new IllegalArgumentException("Categoría con ID " + idMovimiento + " no encontrada.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al eliminar la categoría", e);
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
