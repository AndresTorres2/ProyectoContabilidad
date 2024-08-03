package modelo.entidades;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name= "Categoria")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "categoria_type")
public class Categoria implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCategoria;
	@Column(name = "nombre")
	private String nombreCategoria;
	
	public Categoria() {
		
	}
	public Categoria(int idCategoria, String nombre) {
		super();
		this.idCategoria = idCategoria;
		this.nombreCategoria = nombre;
	}
	public int getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getNombreCategoria() {
		return nombreCategoria;
	}
	public void setNombreCategoria(String nombre) {
		this.nombreCategoria = nombre;
	}
	
	
}
