package modelo.entidades;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TRANSFERENCIA")
public class Transferencia extends Movimiento {

	/**
	 * 
	 */
	@ManyToOne
	@JoinColumn(name= "origen")
	private Cuenta origen;
	@ManyToOne
	@JoinColumn(name= "destino")
	private Cuenta destino;
	
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name= "categoria")
	private CategoriaTransferencia categoria;

	public Transferencia() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Transferencia(int idMovimiento, String concepto, Date fecha, double valor, Cuenta origen, Cuenta destino, CategoriaTransferencia categoriaTransferencia,  Cuenta origenCuenta, Cuenta destinoCuenta) {
		super(idMovimiento, concepto, fecha, valor);
		this.categoria = categoriaTransferencia;
		this.origen = origenCuenta;
		this.destino =  destinoCuenta;
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	public Cuenta getOrigen() {
		return origen;
	}

	public void setOrigen(Cuenta origen) {
		this.origen = origen;
	}

	public Cuenta getDestino() {
		return destino;
	}

	public void setDestino(Cuenta destino) {
		this.destino = destino;
	}

	public CategoriaTransferencia getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaTransferencia categoriaTransferencia) {
		this.categoria = categoriaTransferencia;
	}

	
	

	
}
