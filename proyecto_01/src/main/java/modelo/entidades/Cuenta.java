package modelo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Cuenta")

public class Cuenta implements Serializable {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idCuenta")
	private int idCuenta;
	
	@Column(name="nombreCuenta")
	private String nombreCuenta;
	@Column(name= "total")
	private double total;
	
	@ManyToOne
	@JoinColumn(name = "propietario")
	private Usuario propietario;
	
	private static List<Cuenta> cuentas =  null;
	
	public Cuenta() {
		
	}


	public Cuenta(int id, String nombre, double total,Usuario usuario) {
		super();
		this.idCuenta = id;
		this.nombreCuenta = nombre;
		this.total = total;
		this.propietario = usuario;
	}


	public int getIdCuenta() {
		return idCuenta;
	}


	public void setIdCuenta(int id) {
		this.idCuenta = id;
	}


	public String getNombreCuenta() {
		return nombreCuenta;
	}


	public void setNombreCuenta(String nombre) {
		this.nombreCuenta = nombre;
	}


	public double getTotal() {
		return total;
	}


	public void setTotal(double total) {
		this.total = total;
	}
	
	public Usuario getUsuario() {
		return propietario;
	}


	public void setUsuario(Usuario usuario) {
		this.propietario = usuario;
	}


	/*******************METHODOS DE NEGOCIO****************/
	


	
}
