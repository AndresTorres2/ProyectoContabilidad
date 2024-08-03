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
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name= "categoria")
	private CategoriaTransferencia categoria;

	public Transferencia() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Transferencia(int idMovimiento, String concepto, Date fecha, double valor, Cuenta origen, Cuenta destino, CategoriaTransferencia categoriaTransferencia) {
		super(idMovimiento, concepto, fecha, valor, origen, destino);
		this.categoria = categoriaTransferencia;
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	public CategoriaTransferencia getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaTransferencia categoriaTransferencia) {
		this.categoria = categoriaTransferencia;
	}

	
	

	
}
