package modelo.dao;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import modelo.entidades.Categoria;
import modelo.entidades.CategoriaTransferencia;

public class CategoriaTransferenciaDAO extends CategoriaDAO {

	EntityManager em = null;
	
	public CategoriaTransferenciaDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}

	public void saveCategoriaTransferencia(CategoriaTransferencia categoriaTransferencia) {
        super.saveCategoria(categoriaTransferencia);
    }
	
	public Map<String, Double> getAllSumarized() {
	    try {
	        // SQL query para obtener la suma agrupada por nombre de categoría
	        String sql = "SELECT c.nombre, COALESCE(SUM(m.monto), 0) AS total FROM Categoria c LEFT JOIN Movimiento m ON c.IDCATEGORIA = m.categoria WHERE c.categoria_type = 'C_TRANSFERENCIA'"
	        	 +"GROUP BY c.nombre;";
	        
	        // Crear la consulta nativa
	        Query query = em.createNativeQuery(sql);
	        
	        // Ejecutar la consulta y obtener los resultados
	        List<Object[]> results = query.getResultList();
	        
	        // Crear un mapa para almacenar los resultados
	        Map<String, Double> resultMap = new HashMap<>();
	        
	        // Iterar sobre los resultados y llenar el mapa
	        for (Object[] row : results) {
	            String categoriaNombre = (String) row[0];
	            Double montoTotal = ((Number) row[1]).doubleValue();
	            resultMap.put(categoriaNombre, montoTotal);
	        }
	        
	        return resultMap;
	        
	    } catch (Exception e) {
	        e.printStackTrace(); // Registrar o manejar otras excepciones
	        return Collections.emptyMap(); // Retornar un mapa vacío en caso de error
	    }
	}
	
	public Double getSumByUserIdAndCategory(int usuarioId, int categoriaId, Timestamp fechaInicio, Timestamp fechaFin) {
	    try {
	        // SQL query para obtener la suma total de movimientos filtrados por idUsuario y idCategoria
	        String sql = "SELECT COALESCE(SUM(m.monto), 0.0) AS total_transferencia "
	                   + "FROM movimiento m "
	                   + "LEFT JOIN cuenta cu ON m.origen = cu.idCuenta "
	                   + "WHERE cu.propietario = ?1 AND m.categoria = ?2 "
	                   + "AND m.fecha BETWEEN ?3 AND ?4 ;";

	        // Crear la consulta nativa
	        Query query = em.createNativeQuery(sql);
	        query.setParameter(1, usuarioId); // Establecer el parámetro del id del usuario
	        query.setParameter(2, categoriaId);
	        query.setParameter(3, fechaInicio); // Establecer el parámetro de la fecha de inicio
	        query.setParameter(4, fechaFin);// Establecer el parámetro del id de la categoría
	        
	        // Ejecutar la consulta y obtener el resultado
	        Double totalIngreso = ((Number) query.getSingleResult()).doubleValue();
	        System.out.println("das"  + usuarioId + "cate" + categoriaId );

	        return totalIngreso;

	    } catch (Exception e) {
	        e.printStackTrace(); // Registrar o manejar otras excepciones
	        return 0.0; // Retornar 0.0 en caso de error
	    }
	}
	
	public List<Categoria> getCategoriasTransferencia() {
        String jpql = "SELECT c FROM CategoriaTransferencia c";
        TypedQuery<Categoria> query = em.createQuery(jpql, Categoria.class);
        return query.getResultList();
    }
	
	
	public Map<String, Double> getAllSumarizedByUserId(int usuarioId, Timestamp fechaInicio, Timestamp fechaFin) {
	    try {
	        // SQL query para obtener la suma agrupada por nombre de categoría,
	        // asegurando que todas las categorías sean incluidas incluso si no tienen movimientos
	    	String sql = "SELECT c.nombre, "
	        		+ "       COALESCE(SUM(m.monto), 0.0) AS total_transferencia "
	        		+ "FROM categoria c "
	        		+ "LEFT JOIN ( "
	        		+ "    SELECT m.*, cu.propietario "
	        		+ "    FROM movimiento m "
	        		+ "    LEFT JOIN cuenta cu ON m.origen = cu.idCuenta "
	        		+ "    WHERE m.fecha BETWEEN ? AND ? "
	        		+ ") m ON c.IDCATEGORIA = m.categoria AND m.propietario = ? "
	        		+ "WHERE c.categoria_type = 'C_TRANSFERENCIA' "
	        		+ "GROUP BY c.nombre;";
	        
	        
	        // Crear la consulta nativa
	        Query query = em.createNativeQuery(sql);
	        query.setParameter(1, fechaInicio); // Establecer el parámetro de la fecha de inicio
	        query.setParameter(2, fechaFin); // Establecer el parámetro de la fecha de fin
	        query.setParameter(3, usuarioId);  // Establecer el parámetro del id del usuario
	        
	        // Ejecutar la consulta y obtener los resultados
	        List<Object[]> results = query.getResultList();
	        
	        // Crear un mapa para almacenar los resultados
	        Map<String, Double> resultMap = new HashMap<>();
	        
	        // Iterar sobre los resultados y llenar el mapa
	        for (Object[] row : results) {
	            String categoriaNombre = (String) row[0];
	            Double montoTotal = ((Number) row[1]).doubleValue();
	            resultMap.put(categoriaNombre, montoTotal);
	        }
	        
	        // Opcional: Puedes asegurarte de que todas las categorías están en el mapa, incluso con 0.0 si es necesario
	        // Aquí deberías tener una lista de todas las categorías de transferencia para verificar
	        
	        return resultMap;
	        
	    } catch (Exception e) {
	        e.printStackTrace(); // Registrar o manejar otras excepciones
	        return Collections.emptyMap(); // Retornar un mapa vacío en caso de error
	    }
	}

//ESTO SOLO ES PARA CUMPLIR REGLA DE NEGOCIO DE TRANSFERIR
	public CategoriaTransferencia getCategoriaTransferenciaByName(String nombre) {
        try {
            return em.createQuery("SELECT c FROM CategoriaTransferencia c WHERE c.nombreCategoria = :nombre", CategoriaTransferencia.class)
                     .setParameter("nombre", nombre)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
	


	
}
