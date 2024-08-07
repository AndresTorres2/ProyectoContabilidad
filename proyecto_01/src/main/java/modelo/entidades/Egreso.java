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
	
	@ManyToOne
	@JoinColumn(name= "origen")
	private Cuenta origen;
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name= "categoria")
	private CategoriaEgreso categoria;
	
	
	

	public Cuenta getOrigen() {
		return origen;
	}

	public void setOrigen(Cuenta origen) {
		this.origen = origen;
	}

	public Egreso() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Egreso(int idMovimiento, String concepto, Date fecha, double valor, CategoriaEgreso categoriaEgreso, Cuenta origenCuenta) {
		super(idMovimiento, concepto, fecha, valor);
		this.categoria = categoriaEgreso;
		this.origen = origenCuenta;
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
