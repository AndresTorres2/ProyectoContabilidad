package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.text.ParseException;


import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.entidades.Categoria;
import modelo.entidades.CategoriaEgreso;
import modelo.entidades.CategoriaIngreso;
import modelo.entidades.CategoriaTransferencia;
import modelo.entidades.Cuenta;
import modelo.entidades.Egreso;
import modelo.entidades.Ingreso;
import modelo.entidades.Movimiento;
import modelo.entidades.Transferencia;
import modelo.entidades.Usuario;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import modelo.dao.CategoriaDAO;
import modelo.dao.CategoriaEgresoDAO;
import modelo.dao.CategoriaIngresoDAO;
import modelo.dao.CategoriaTransferenciaDAO;
import modelo.dao.CuentaDAO;
import modelo.dao.EgresoDAO;
import modelo.dao.IngresoDAO;
import modelo.dao.MovimientoDAO;
import modelo.dao.TransferenciaDAO;
import modelo.dao.UsuarioDAO;
@WebServlet("/ContabilidadController")

public class ContabilidadController extends HttpServlet {

	/**
	 * 
	 */
	private CuentaDAO cuentaDAO;
	private UsuarioDAO usuarioDAO;
	private CategoriaDAO categoriaDAO;
	private MovimientoDAO movimientoDAO;
	
	private CategoriaIngresoDAO categoriaIngresoDAO;
	private CategoriaEgresoDAO categoriaEgresoDAO;
	private CategoriaTransferenciaDAO categoriaTransferenciaDAO;
	
	private EgresoDAO egresoDAO;
	private IngresoDAO ingresoDAO;
	private TransferenciaDAO transferenciaDAO;
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		usuarioDAO = new UsuarioDAO();
		cuentaDAO = new CuentaDAO();
		categoriaDAO = new CategoriaDAO();
		movimientoDAO = new MovimientoDAO();
		
		categoriaIngresoDAO = new CategoriaIngresoDAO();
		categoriaEgresoDAO =  new CategoriaEgresoDAO();
		categoriaTransferenciaDAO = new CategoriaTransferenciaDAO();
		
		egresoDAO = new EgresoDAO();
		ingresoDAO = new IngresoDAO();
		transferenciaDAO = new TransferenciaDAO();
		super.init(config);
	}
	
	public void iniciar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*1.-Obtener los parametros
		 *2.- Hablar con el modelo
		 *3.- Llamar a la vista
		 */
		
		resp.sendRedirect("jsp/login.jsp");
	}
	public void ingresar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//resp.sendRedirect("ContabilidadController?ruta=mostrardashboard");
		resp.sendRedirect("ContabilidadController?ruta=mostrarFormulario");
		//resp.sendRedirect("jsp/createCategoria.jsp");
		
		
	}
	
	
	public void mostrarDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Cuenta> cuentas = cuentaDAO.getAllAccounts();
		List<Categoria> categorias = categoriaDAO.findAll();
		    
		List<CategoriaIngreso> ingresos = new ArrayList<>();
	    List<CategoriaEgreso> egresos = new ArrayList<>();
	    List<CategoriaTransferencia> transferencias = new ArrayList<>();
	    
	    List<Movimiento> movimientos = movimientoDAO.getAllMovements();
	    Map<String, Double> ingresosTotales = categoriaIngresoDAO.getAllSumarized();
	    Map<String, Double> totalEgresos = categoriaEgresoDAO.getAllSumarized();
	    Map<String, Double> transferenciasTotales = categoriaTransferenciaDAO.getAllSumarized();


	    for (Categoria categoria : categorias) {
	        if (categoria instanceof CategoriaIngreso) {
	            ingresos.add((CategoriaIngreso) categoria);
	        } else if (categoria instanceof CategoriaEgreso) {
	            egresos.add((CategoriaEgreso) categoria);
	        } else if (categoria instanceof CategoriaTransferencia) {
	            transferencias.add((CategoriaTransferencia) categoria);
	        }
	    }

        req.setAttribute("cuentas", cuentas);
        req.setAttribute("ingresos", ingresos);
        req.setAttribute("egresos", egresos);
        req.setAttribute("transferencias", transferencias);
        req.setAttribute("movimientos", movimientos);
        req.setAttribute("totalIngresos", ingresosTotales);
        req.setAttribute("totalEgresos", totalEgresos);
        req.setAttribute("totalTransferencias", transferenciasTotales);
        
        req.getRequestDispatcher("jsp/verDashboard.jsp").forward(req, resp);
		
	}
	
	public void mostrarCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // Obtener el identificador de la cuenta desde la solicitud
		int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));

	    
	    // Obtener la cuenta y sus movimientos desde el DAO
	    Cuenta cuenta = cuentaDAO.getCuentaById(cuentaId);
	    List<Movimiento> movimientos = movimientoDAO.getMovimientosByCuenta(cuenta);
	    
	    // Establecer atributos en la solicitud
	    req.setAttribute("cuenta", cuenta);
	    req.setAttribute("movimientos", movimientos);
	    
	    // Redirigir a la vista verCuenta.jsp
	    req.getRequestDispatcher("jsp/verCuenta.jsp").forward(req, resp);
	}

	
	public void mostrarCategoria(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		 int categoriaId = Integer.parseInt(req.getParameter("categoriaId"));
		 Categoria categoria = categoriaDAO.findCategoriaById(categoriaId);
		 //List<Movimiento> movimientos;
		 List<Egreso> movimientosEgreso = null;
		 List<Ingreso> movimientosIngreso = null;
		 List<Transferencia> movimientosTransferencia = null;
		 double total = 0.0;
		    if (categoria instanceof CategoriaIngreso) {
		        
		    	movimientosIngreso = ingresoDAO.findMovimientosByCategoriaIngreso(categoria);
		    	total = categoriaIngresoDAO.getSumByCategory(categoriaId);
		    	req.setAttribute("movimientos", movimientosIngreso);
		    } else if (categoria instanceof CategoriaEgreso) {
		    	movimientosEgreso = egresoDAO.findMovimientosByCategoriaEgreso(categoria);
		    	total = categoriaEgresoDAO.getSumByCategory(categoriaId);
		    	
		    	req.setAttribute("movimientos", movimientosEgreso);
		    	
		    } else if (categoria instanceof CategoriaTransferencia) {
		    	movimientosTransferencia = transferenciaDAO.findMovimientosByCategoriaTransferencia(categoria);
		        total = categoriaTransferenciaDAO.getSumByCategory(categoriaId);
		    }
		    
		   
		    req.setAttribute("categoria", categoria);
		    req.setAttribute("total", total);
		    
		    
		    req.getRequestDispatcher("jsp/verCategoria.jsp").forward(req, resp);
	}
	
	
	
	
	
	
	//**//
	
	
	
	public void createCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nombre = req.getParameter("nombre");
        double saldo = Double.parseDouble(req.getParameter("saldo"));
        Usuario usuario = usuarioDAO.findUsuarioById(1);
        
        Cuenta cuenta = new Cuenta();
        cuenta.setNombreCuenta(nombre);
        cuenta.setTotal(saldo);
        cuenta.setUsuario(usuario);

        cuentaDAO.createCuenta(cuenta);
        //resp.sendRedirect("ContabilidadController?ruta=mostrardashboard");
        resp.sendRedirect("jsp/createCategoria.jsp");
    }
	
	public void crearCategoria(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String tipoCategoria = req.getParameter("tipoCategoria");
	    String nombreCategoria = req.getParameter("nombreCategoria");

	    CategoriaDAO categoriaDAO = null;
	    Categoria categoria = null;

	    if ("egreso".equalsIgnoreCase(tipoCategoria)) {
	        categoria = new CategoriaEgreso(0,nombreCategoria);
	        categoriaDAO = new CategoriaEgresoDAO();
	    } else if ("ingreso".equalsIgnoreCase(tipoCategoria)) {
	        categoria = new CategoriaIngreso(0,nombreCategoria);
	        categoriaDAO = new CategoriaIngresoDAO();
	    } else if ("transferencia".equalsIgnoreCase(tipoCategoria)) {
	        categoria = new CategoriaTransferencia(0,nombreCategoria);
	        categoriaDAO = new CategoriaTransferenciaDAO();
	    }

	    if (categoria != null && categoriaDAO != null) {
	        categoriaDAO.saveCategoria(categoria);
	        resp.sendRedirect("ContabilidadController?ruta=mostrardashboard");
	    } else {
	        // Manejo de error
	    }
	}
	public void crearMovimiento(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String concepto = req.getParameter("concepto");
	    double monto = Double.parseDouble(req.getParameter("monto"));
	    String fechaStr = req.getParameter("fecha");
	    Timestamp fecha = null;

	    try {
	    	fechaStr = fechaStr.replace("T", " "); // Reemplazar 'T' con un espacio para el formato correcto
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        fecha = new Timestamp(dateFormat.parse(fechaStr).getTime()); // Convertir la fecha del formulario a un objeto Date
	    } catch (ParseException e) {
	        // Manejo de error si la fecha no es válida
	        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Fecha inválida");
	        return;
	    }

	    int idOrigen = Integer.parseInt(req.getParameter("origen"));
	    int idDestino = Integer.parseInt(req.getParameter("destino"));
	    int idCategoria = Integer.parseInt(req.getParameter("categoria"));

	    // Obtener las cuentas y categoría desde la base de datos
	    Cuenta origen = cuentaDAO.getCuentaById(idOrigen);
	    Cuenta destino = cuentaDAO.getCuentaById(idDestino);
	    Categoria categoria = categoriaDAO.getCategoriaById(idCategoria);

	    Movimiento movimiento = null;

	    if (categoria instanceof CategoriaIngreso) {
	        movimiento = new Ingreso();
	        ((Ingreso) movimiento).setCategoria((CategoriaIngreso) categoria);
	    } else if (categoria instanceof CategoriaEgreso) {
	        movimiento = new Egreso();
	        ((Egreso) movimiento).setCategoria((CategoriaEgreso) categoria);
	        monto = -monto;
	    } else if (categoria instanceof CategoriaTransferencia) {
	        movimiento = new Transferencia();
	        ((Transferencia) movimiento).setCategoria((CategoriaTransferencia) categoria);
	    } else {
	        // Manejo de error si la categoría no es válida
	        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Categoría inválida");
	        return;
	    }

	    if (movimiento != null) {
	        movimiento.setConcepto(concepto);
	        movimiento.setMonto(monto);
	        movimiento.setFecha(fecha);
	        movimiento.setOrigen(origen);
	        movimiento.setDestino(destino);

	        
	        movimientoDAO.createMovimiento(movimiento);
	        resp.sendRedirect("ContabilidadController?ruta=mostrardashboard");
	    } else {
	        // Manejo de error si el movimiento no se pudo crear
	        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo crear el movimiento");
	    }
	}


	public void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 
	    List<Cuenta> cuentas = cuentaDAO.getAllAccounts();
	    List<Categoria> categorias = categoriaDAO.findAll();
	    
	   
	    req.setAttribute("cuentas", cuentas);
	    req.setAttribute("categorias", categorias);
	    
	    
	    req.getRequestDispatcher("jsp/formularioMovimiento.jsp").forward(req, resp);
	}
	

	private void ruteador(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ruta = (req.getParameter("ruta") == null)? "iniciar": req.getParameter("ruta");
		switch (ruta) {
				case "ingresar": 
					this.ingresar(req, resp);
					break;
				case "iniciar":
					this.iniciar(req, resp);
					break;
				case "mostrardashboard":
					this.mostrarDashboard(req, resp);
					break;
				case "createCuenta":
	                this.createCuenta(req, resp);
	                break;
				case "createCategoria":
	                this.crearCategoria(req, resp);
	                break;
				case "createMovimiento":
	                this.crearMovimiento(req, resp);
	                break;
				case "mostrarFormulario":
					this.mostrarFormulario(req, resp);
				case "mostrarCuenta":
					this.mostrarCuenta(req, resp);
				case "mostrarCategoria":
					this.mostrarCategoria(req, resp);
				default:
					
				}
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.ruteador(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.ruteador(req, resp);
	}

	
	
	
	/*Posterior*/
	 public static Date getPrimerDiaDelMes() {
	        return new GregorianCalendar(2024, GregorianCalendar.JULY, 1).getTime();
	    }

	    public static Date getUltimoDiaDelMes() {
	        return new GregorianCalendar(2024, GregorianCalendar.JULY, 31).getTime();
	    }
}
