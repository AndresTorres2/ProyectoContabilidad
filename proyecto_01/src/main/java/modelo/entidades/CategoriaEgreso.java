package modelo.entidades;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("C_EGRESO")
public class CategoriaEgreso extends Categoria {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CategoriaEgreso() {
		super();

	}

	public CategoriaEgreso(int idCategoria, String nombre) {
		super(idCategoria, nombre);
		// TODO Auto-generated constructor stub
	}
	
	
	

	
}
