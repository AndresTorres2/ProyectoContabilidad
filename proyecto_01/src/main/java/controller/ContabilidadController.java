package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
import modelo.dto.MovimientoDTO;
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
	private MovimientoDTO movimientoDTO;
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		usuarioDAO = new UsuarioDAO();
		cuentaDAO = new CuentaDAO();
		categoriaDAO = new CategoriaDAO();
		movimientoDAO = new MovimientoDAO();
		movimientoDTO = new MovimientoDTO();
		
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
		resp.sendRedirect("ContabilidadController?ruta=mostrardashboard");
		//resp.sendRedirect("ContabilidadController?ruta=mostrarFormulario");
		//resp.sendRedirect("jsp/createCategoria.jsp");
		
		
	}
	
	
	public void mostrarDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Cuenta> cuentas = cuentaDAO.getAllAccounts();
		List<Categoria> categorias = categoriaDAO.findAll();
		    
		List<CategoriaIngreso> ingresos = new ArrayList<>();
	    List<CategoriaEgreso> egresos = new ArrayList<>();
	    List<CategoriaTransferencia> transferencias = new ArrayList<>();
	    
	    List<Movimiento> movimientos = movimientoDAO.getAllMovements(); 
	    List<MovimientoDTO> movimientosDTO = movimientoDAO.getAllMovementsDTO(movimientos);
	    
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
        req.setAttribute("movimientos", movimientosDTO);
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
	    
	    List<Movimiento> movimientosEgreso =  egresoDAO.getMovimientosByCuenta(cuenta);
		List<Movimiento> movimientosIngreso = ingresoDAO.getMovimientosByCuenta(cuenta);
		List<Movimiento> movimientosTransferencia = transferenciaDAO.getMovimientosByCuenta(cuenta);
	    
	    
		List<Movimiento> movimientos = new ArrayList<>();
		movimientos.addAll(movimientosEgreso);
		movimientos.addAll(movimientosIngreso);
		movimientos.addAll(movimientosTransferencia);

	    List<MovimientoDTO> movimientosDTO = movimientoDAO.getAllMovementsDTO(movimientos);
	    
	    
	    
	    
	    // Establecer atributos en la solicitud
	    req.setAttribute("cuenta", cuenta);
	    req.setAttribute("movimientos", movimientosDTO);
	    
	    // Redirigir a la vista verCuenta.jsp
	    req.getRequestDispatcher("jsp/verCuenta.jsp").forward(req, resp);
	}

	
	public void mostrarCategoria(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		 int categoriaId = Integer.parseInt(req.getParameter("categoriaId"));
		 Categoria categoria = categoriaDAO.findCategoriaById(categoriaId);
		 //List<Movimiento> movimientos;
		 List<Movimiento> movimientosEgreso = null;
		 List<Movimiento> movimientosIngreso = null;
		 List<Movimiento> movimientosTransferencia = null;
		 double total = 0.0;
		 List<MovimientoDTO> movimientosDTO = null;
		    if (categoria instanceof CategoriaIngreso) {
		        
		    	movimientosIngreso = ingresoDAO.findMovimientosByCategoriaIngreso(categoria);
		    	movimientosDTO = movimientoDAO.getAllMovementsDTO(movimientosIngreso);
		    	total = categoriaIngresoDAO.getSumByCategory(categoriaId);
		    	req.setAttribute("movimientos", movimientosDTO);
		    } else if (categoria instanceof CategoriaEgreso) {
		    	movimientosEgreso = egresoDAO.findMovimientosByCategoriaEgreso(categoria);
		    	movimientosDTO = movimientoDAO.getAllMovementsDTO(movimientosEgreso);
		    	
		    	
		    	total = categoriaEgresoDAO.getSumByCategory(categoriaId);
		    	
		    	req.setAttribute("movimientos", movimientosDTO);
		    	
		    } else if (categoria instanceof CategoriaTransferencia) {
		    	movimientosTransferencia = transferenciaDAO.findMovimientosByCategoriaTransferencia(categoria);
		    	movimientosDTO = movimientoDAO.getAllMovementsDTO(movimientosTransferencia);
		        total = categoriaTransferenciaDAO.getSumByCategory(categoriaId);
		        req.setAttribute("movimientos", movimientosDTO);
		    }
		    
		   
		    req.setAttribute("categoria", categoria);
		    req.setAttribute("total", total);
		    
		    
		    req.getRequestDispatcher("jsp/verCategoria.jsp").forward(req, resp);
	}
	
	public void registrarIngresoForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
		
		
		Cuenta cuenta = cuentaDAO.getCuentaById(cuentaId);
		List<Categoria> categoriasIngreso = categoriaIngresoDAO.getCategoriasIngreso();
		
		req.setAttribute("cuenta", cuenta);
		req.setAttribute("categorias", categoriasIngreso);
		req.getRequestDispatcher("jsp/registrarIngreso.jsp").forward(req, resp);
		
	
	
	}
	public void registrarEgresoForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
		
		
		Cuenta cuenta = cuentaDAO.getCuentaById(cuentaId);
		List<Categoria> categoriasEgreso = categoriaEgresoDAO.getCategoriasEgreso();
		
		req.setAttribute("cuenta", cuenta);
		req.setAttribute("categorias", categoriasEgreso);
		req.getRequestDispatcher("jsp/registrarEgreso.jsp").forward(req, resp);
		
	
	
	}
	public void registrarEgreso(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
        String concepto = req.getParameter("concepto");
        double monto = Double.parseDouble(req.getParameter("monto"));
        String fechaStr = req.getParameter("fecha");
        int categoriaId = Integer.parseInt(req.getParameter("categoria"));
        Cuenta cuenta = cuentaDAO.getCuentaById(cuentaId);
        Categoria categoria  = categoriaEgresoDAO.getCategoriaById(categoriaId);
        
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
	    monto = -monto;
	    cuentaDAO.actualizarSaldo(cuentaId, monto);
	    //movimiento
	    Egreso nuevoEgreso=  new Egreso(0,concepto,fecha,monto,(CategoriaEgreso) categoria,cuenta);
	    egresoDAO.createEgreso(nuevoEgreso);
        //actualiza la cuenta
        
        
        resp.sendRedirect("ContabilidadController?ruta=mostrarCuenta&cuentaId=" + cuentaId + "&mensaje=Egreso registrado exitosamente");
		
	}
	
	public void registrarIngreso(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
        String concepto = req.getParameter("concepto");
        double monto = Double.parseDouble(req.getParameter("monto"));
        String fechaStr = req.getParameter("fecha");
        int categoriaId = Integer.parseInt(req.getParameter("categoria"));
        
        Cuenta cuenta = cuentaDAO.getCuentaById(cuentaId);
        
        Categoria categoria = categoriaIngresoDAO.getCategoriaById(categoriaId);
        
        
        
        
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
	    cuentaDAO.actualizarSaldo(cuentaId, monto);
	    Ingreso nuevoIngreso =  new Ingreso(0,concepto,fecha,monto,(CategoriaIngreso) categoria,cuenta);
	    ingresoDAO.createIngreso(nuevoIngreso);
        
        
        
        resp.sendRedirect("ContabilidadController?ruta=mostrarCuenta&cuentaId=" + cuentaId + "&mensaje=Ingreso registrado exitosamente");
	
	}
	
	public void registrarTransferenciaForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
	    
	    // Obtener la cuenta de origen
	    Cuenta cuentaOrigen = cuentaDAO.getCuentaById(cuentaId);
	    
	    // Obtener todas las cuentas excepto la cuenta de origen
	    List<Cuenta> todasCuentas = cuentaDAO.getAllAccounts();
	    List<Cuenta> cuentasDestino = todasCuentas.stream()
	        .filter(cuenta -> cuenta.getIdCuenta() != cuentaId)
	        .collect(Collectors.toList());
	    List<Categoria> categorias = categoriaTransferenciaDAO.getCategoriasTransferencia();
	    
	    req.setAttribute("cuenta", cuentaOrigen);
	    req.setAttribute("cuentasDestino", cuentasDestino);
	    req.setAttribute("categorias", categorias);
	    
	    req.getRequestDispatcher("jsp/registrarTransferenciaForm.jsp").forward(req, resp);
	    
	}
	
	public void transferir(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int cuentaOrigenId = Integer.parseInt(req.getParameter("cuentaOrigenId"));
		
		System.out.println(req.getParameter("cuentaOrigenId"));
		System.out.println(cuentaOrigenId);
		int cuentaDestinoId = Integer.parseInt(req.getParameter("destino"));
		System.out.println(req.getParameter("destino"));
		System.out.println(cuentaDestinoId);
        String concepto = req.getParameter("concepto");
        
        double monto = Double.parseDouble(req.getParameter("monto"));
        
        String fechaStr = req.getParameter("fecha");
        
        int categoriaId = Integer.parseInt(req.getParameter("categoria"));
        
        Cuenta cuentaOrigen = cuentaDAO.getCuentaById(cuentaOrigenId);
        Cuenta  cuentaDestino = cuentaDAO.getCuentaById(cuentaDestinoId);
        
        Categoria categoria  = categoriaTransferenciaDAO.getCategoriaById(categoriaId);
        
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
	    monto = -monto;
	    cuentaDAO.actualizarSaldo(cuentaOrigenId, monto);
	    monto = -monto;
	    cuentaDAO.actualizarSaldo(cuentaDestinoId, monto);
        
        
	    
	    Transferencia nuevaTransferencia=  new Transferencia(0,concepto,fecha,monto,(CategoriaTransferencia) categoria,cuentaOrigen,cuentaDestino);
	    														//int idMovimiento, String concepto, Date fecha, double valor, CategoriaTransferencia categoriaTransferencia,  Cuenta origenCuenta, Cuenta destinoCuenta
	    transferenciaDAO.createTransferencia(nuevaTransferencia);
	    
	    //Ingreso nuevoIngreso = new Ingreso(0,concepto,fecha,monto,(CategoriaIngreso) categoria,cuentaDestino);
        //ingresoDAO.createIngreso(nuevoIngreso);
        
        resp.sendRedirect("ContabilidadController?ruta=mostrarCuenta&cuentaId=" + cuentaOrigenId + "&mensaje=Transferencia registrada exitosamente");
		
	}
	
	//**//
	public void mostrarFormularioCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int usuarioId = Integer.parseInt(req.getParameter("idUsuario"));
	    Usuario usuario = usuarioDAO.findUsuarioById(usuarioId); // Asegúrate de tener un método para obtener un usuario por ID
	    
	    req.setAttribute("usuario", usuario);
		req.getRequestDispatcher("jsp/createCuenta.jsp").forward(req, resp);
	}
	
	public void createCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nombre = req.getParameter("nombreCuenta");
        double saldo = Double.parseDouble(req.getParameter("saldo"));
        int usuarioId = Integer.parseInt(req.getParameter("idUsuario"));
        Usuario usuario = usuarioDAO.findUsuarioById(usuarioId);
        
        Cuenta cuenta = new Cuenta();

        cuenta.setNombreCuenta(nombre);
        cuenta.setTotal(saldo);
        cuenta.setUsuario(usuario);

        cuentaDAO.createCuenta(cuenta);
        resp.sendRedirect("ContabilidadController?ruta=mostrardashboard");
        //resp.sendRedirect("jsp/createCategoria.jsp");
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
				case "mostrarFormularioCuenta":
	                this.mostrarFormularioCuenta(req, resp);
	                break;
				case "createCuenta":
	                this.createCuenta(req, resp);
	                break;
	                
				case "createCategoria":
	                this.crearCategoria(req, resp);
	                break;
				case "mostrarCuenta":
					this.mostrarCuenta(req, resp);
					break;
				case "mostrarCategoria":
					this.mostrarCategoria(req, resp);
					break;
				case "registrarIngresoForm":
					this.registrarIngresoForm(req,resp);
					break;
					
				case "registrarIngreso":
					this.registrarIngreso(req,resp);
					break;
				case "registrarEgresoForm":
					this.registrarEgresoForm(req,resp);
					break;
					
				case "registrarEgreso":
					this.registrarEgreso(req,resp);
					break;
				
				case "registrarTransferenciaForm":
					this.registrarTransferenciaForm(req,resp);
					break;
					
				case "transferir":
					this.transferir(req,resp);
					break;
					
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
