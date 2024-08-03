package modelo.entidades;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("INGRESO")
public class Ingreso extends Movimiento {   

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name= "categoria")
	private CategoriaIngreso categoria;
	
	
	

	public Ingreso() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Ingreso(int idMovimiento, String concepto, Date fecha, double valor, Cuenta origen, Cuenta destino, CategoriaIngreso categoriaIngreso) {
		super(idMovimiento, concepto, fecha, valor, origen, destino);
		this.categoria = categoriaIngreso;
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	
	public CategoriaIngreso getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaIngreso categoriaIngreso) {
		this.categoria = categoriaIngreso;
	}

	
}
