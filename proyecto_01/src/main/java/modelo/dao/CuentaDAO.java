package modelo.dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelo.entidades.Cuenta;

public class CuentaDAO  {

	EntityManager em = null;
	
	public CuentaDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistencia");
		this.em  = emf.createEntityManager();
		
	}
	
	public void createCuenta(Cuenta cuenta) {
        em.getTransaction().begin();
        em.persist(cuenta);
        em.getTransaction().commit();
    }
	
	
	
	public  List<Cuenta> getAllAccounts() {
		return em.createQuery("SELECT c FROM Cuenta c", Cuenta.class).getResultList();
		
	}
	public Cuenta getCuentaById(int id) {
        return em.find(Cuenta.class, id);
    }
}
