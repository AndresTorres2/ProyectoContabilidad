package modelo.recursos;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import modelo.dao.EgresoDAO;
import modelo.dao.IngresoDAO;
import modelo.dao.TransferenciaDAO;
import modelo.entidades.Egreso;
import modelo.entidades.Ingreso;
import modelo.entidades.Movimiento;
import modelo.entidades.Transferencia;

@Path("/movimientos")
public class RecursoMovimiento {
	IngresoDAO ingreso = new IngresoDAO();
	EgresoDAO egreso = new EgresoDAO();
	TransferenciaDAO transferencia = new TransferenciaDAO();
			
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Movimiento> getMovimientos(){
		List<Movimiento> movimientos = new ArrayList<>();
		System.out.println("getMovimientos() method called");
		 List<Ingreso> ingresos = ingreso.getAllMovementsIngreso();
		    movimientos.addAll(ingresos);

		    // Obtener todos los egresos
		    List<Egreso> egresos = egreso.getAllMovementsEgreso();
		    movimientos.addAll(egresos);

		    // Obtener todas las transferencias
		    List<Transferencia> transferencias = transferencia.getAllMovementsTransferencia();
		    movimientos.addAll(transferencias);
		    
		return movimientos;
		
	} 
}
