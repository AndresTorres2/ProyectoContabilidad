package modelo.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.dao.MovimientoDAO;
import modelo.entidades.Categoria;
import modelo.entidades.CategoriaEgreso;
import modelo.entidades.Cuenta;
import modelo.entidades.Egreso;
import modelo.entidades.Ingreso;
import modelo.entidades.Movimiento;

public class MovimientoDTO {
    private int idMovimiento;
    private String concepto;
    private Date fecha;
    private Double monto;
    private String categoria;
    private String origen;
    private String destino;
    public MovimientoDTO() {
    	
    }
    // Constructor
    public MovimientoDTO(int idMovimiento, String concepto, Date fecha, Double monto, 
                         String categoria, String origen, String destino) {
        this.idMovimiento = idMovimiento;
        this.concepto = concepto;
        this.fecha = fecha;
        this.monto = monto;
        this.categoria = categoria;
        this.origen = origen;
        this.destino = destino;
    }

    // Getters y Setters
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

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
    
   
    
	/*
    public List<MovimientoDTO> getMovimientosDTOByCuenta(Cuenta cuenta) {
        // Suponiendo que tienes un método para obtener los movimientos de una cuenta
        List<Movimiento> movimientos = getMovimientosByCuenta(cuenta);
        List<MovimientoDTO> movimientosDTO = new ArrayList<>();

        for (Movimiento movimiento : movimientos) {
            MovimientoDTO dto = toMovimientoDTO(movimiento);
            movimientosDTO.add(dto);
        }

        return movimientosDTO;
    }
    */

}

