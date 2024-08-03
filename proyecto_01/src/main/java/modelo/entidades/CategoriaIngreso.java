package modelo.entidades;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("C_INGRESO")

public class CategoriaIngreso extends Categoria {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CategoriaIngreso() {
		super();

	}

	public CategoriaIngreso(int idCategoria, String nombre) {
		super(idCategoria, nombre);
		// TODO Auto-generated constructor stub
	}
	
	
	
}
