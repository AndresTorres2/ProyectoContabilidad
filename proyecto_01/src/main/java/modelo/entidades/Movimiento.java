package modelo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;




@Entity
@Table(name = "Movimiento")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")

public class Movimiento implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idMovimiento")
	private int idMovimiento;
	
	@Column(name="concepto")
	private String concepto;
	@Column(name="fecha")
	private Date fecha;
	@Column(name="monto")
	private double monto;
	
	
	
	
	
	public Movimiento() {

		
	}
	
	public Movimiento(int idMovimiento, String concepto, Date fecha, double valor) {
		super();
		this.idMovimiento = idMovimiento;
		this.concepto = concepto;
		this.fecha = fecha;
		this.monto = valor;

	}


	public int getIdMovimiento() {
		return idMovimiento;
	}
	public void setIdMovimiento(int idMovimiento) {
		this.idMovimiento = idMovimiento;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	
	

	
	
}
