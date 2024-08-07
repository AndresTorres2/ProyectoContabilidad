package modelo.dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
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
	
	
	public void actualizarSaldo(int cuentaId, double monto) {
		EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            
            
            Cuenta cuenta = em.find(Cuenta.class, cuentaId);
            if (cuenta != null) {
                // Actualiza el saldo
            	double nuevoSaldo = cuenta.getTotal() + monto;
            	if (nuevoSaldo < 0) {
                    // Si el saldo es negativo, cancela la transacción y lanza una excepción
                    transaction.rollback();
                    throw new RuntimeException("El valor ingresado supera el saldo total de la cuenta.");
                }
            	
                cuenta.setTotal(nuevoSaldo);
                em.merge(cuenta); // Sincroniza el estado de la entidad con la base de datos
            }
            
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error al actualizar el saldo de la cuenta", e);
        }
    }
	
	
	
	
	
}
