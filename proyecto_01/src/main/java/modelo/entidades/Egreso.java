package modelo.entidades;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;


@Entity
@DiscriminatorValue("EGRESO")
public class Egreso extends Movimiento {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name= "categoria")
	private CategoriaEgreso categoria;
	
	
	

	public Egreso() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Egreso(int idMovimiento, String concepto, Date fecha, double valor, Cuenta origen, Cuenta destino, CategoriaEgreso categoriaEgreso) {
		super(idMovimiento, concepto, fecha, valor, origen, destino);
		this.categoria = categoriaEgreso;
		// TODO Auto-generated constructor stub
	}








	public CategoriaEgreso getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaEgreso categoriaEgreso) {
		this.categoria = categoriaEgreso;
	}

	
	
	
	////////////////////////////
	
	
}
