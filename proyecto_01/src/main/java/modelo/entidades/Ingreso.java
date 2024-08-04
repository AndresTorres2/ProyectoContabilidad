package modelo.entidades;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("INGRESO")
public class Ingreso extends Movimiento {  
	


	@ManyToOne
	@JoinColumn(name= "destino")
	private Cuenta destino;
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

	public Ingreso(int idMovimiento, String concepto, Date fecha, double valor, CategoriaIngreso categoriaIngreso, Cuenta cuentaDestino) {
		super(idMovimiento, concepto, fecha, valor);
		this.categoria = categoriaIngreso;
		this.destino = cuentaDestino;
		// TODO Auto-generated constructor stub
	}
	

	public Cuenta getDestino() {
		return destino;
	}

	public void setDestino(Cuenta destino) {
		this.destino = destino;
	}

	public CategoriaIngreso getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaIngreso categoriaIngreso) {
		this.categoria = categoriaIngreso;
	}


	
	
	
	
	
}
