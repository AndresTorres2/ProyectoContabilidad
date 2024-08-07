package modelo.dao;

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
	
	public double getSumByCategory(int categoriaId) {
	    try {
	        // SQL query para obtener la suma para una categoría específica
	        String sql = "SELECT COALESCE(SUM(m.monto), 0) FROM Movimiento m JOIN Categoria c ON m.categoria = c.IDCATEGORIA WHERE c.IDCATEGORIA = ?1 AND c.categoria_type = 'C_TRANSFERENCIA'";
	        
	        // Crear la consulta nativa
	        Query query = em.createNativeQuery(sql);
	        query.setParameter(1, categoriaId);
	        
	        // Ejecutar la consulta y obtener el resultado
	        Double result = (Double) query.getSingleResult();
	        return result != null ? result : 0.0;
	        
	    } catch (Exception e) {
	        e.printStackTrace(); // Registrar o manejar otras excepciones
	        return 0.0; // Retornar 0 en caso de error
	    }
	}
	
	public List<Categoria> getCategoriasTransferencia() {
        String jpql = "SELECT c FROM CategoriaTransferencia c";
        TypedQuery<Categoria> query = em.createQuery(jpql, Categoria.class);
        return query.getResultList();
    }

	
}
