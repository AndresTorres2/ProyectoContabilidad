package modelo.entidades;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("C_TRANSFERENCIA")
public class CategoriaTransferencia extends Categoria {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CategoriaTransferencia() {
		super();

	}

	public CategoriaTransferencia(int idCategoria, String nombre) {
		super(idCategoria, nombre);
		// TODO Auto-generated constructor stub
	}
}
