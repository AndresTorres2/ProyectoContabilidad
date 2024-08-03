package modelo.entidades;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name= "Usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idUsuario;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name ="clave")
	private String clave;
	
	public Usuario() {

	}
	public Usuario(int idUsuario, String nombre, String clave) {
		super();
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.clave = clave;
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	
	
}
