package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import jakarta.servlet.http.HttpSession;
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
	
	public void autenticar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*1.-Obtener los parametros
		 *2.- Hablar con el modelo
		 *3.- Llamar a la vista
		 */
		resp.sendRedirect("jsp/login.jsp");
	}
	public void ingresar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	    String username = req.getParameter("usuario");
	    String password = req.getParameter("clave");
	    Usuario usuario = usuarioDAO.authenticate(username, password);

	  
	    if (usuario != null) {
	    	HttpSession session = req.getSession(true);
	    	
	    	LocalDate hoy = LocalDate.now();
	    	LocalDate primerDiaDelMes = hoy.withDayOfMonth(1);

	    	// Generar LocalDateTime para el inicio y fin del periodo
	    	LocalDateTime fechaInicio = primerDiaDelMes.atStartOfDay();
	    	LocalDateTime fechaFin = hoy.atTime(23, 59, 59);

	    	// Formatear fechas para que sean compatibles con el input datetime-local
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	    	String fechaInicioStr = fechaInicio.format(formatter);
	    	String fechaFinStr = fechaFin.format(formatter);
	        
	        // Guardar fechas en la sesión
	        session.setAttribute("fechaInicio", fechaInicioStr);
	        session.setAttribute("fechaFin", fechaFinStr);
	    	req.getSession().setAttribute("usuarioId", usuario.getIdUsuario()); 
	    	session.setMaxInactiveInterval(30 * 60);
	        resp.sendRedirect("ContabilidadController?ruta=mostrardashboard");
	        
	    } else {
	        // Si la autenticación falla, redirigir a la página de inicio de sesión con un mensaje de error
	        req.getSession().setAttribute("errorMessage", "Nombre de usuario o contraseña incorrectos");
	        resp.sendRedirect("jsp/login.jsp"); 
	    }
		
		
	}
	public void mostrarFormularioUsuario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("jsp/createUsuario.jsp");
	}
	public void createUsuario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nombre =  req.getParameter("nombre");
		String clave =  req.getParameter("clave");
		Usuario usuario  =  new Usuario(0,nombre,clave);
		usuarioDAO.create(usuario);
		resp.sendRedirect("ContabilidadController?ruta=iniciar");
	}
	
	
	public void mostrarDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int usuarioId =(int) req.getSession().getAttribute("usuarioId");
		HttpSession session = req.getSession();
		
		

	    // Leer fechas desde la solicitud
	    String fechaInicioStr = req.getParameter("fechaInicio");
	    String fechaFinStr = req.getParameter("fechaFin");

	    // Usar formato para fechas
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

	    LocalDateTime fechaInicio;
	    LocalDateTime fechaFin;

	    if (fechaInicioStr != null && !fechaInicioStr.isEmpty() &&
	        fechaFinStr != null && !fechaFinStr.isEmpty()) {
	        // Si se proporcionan nuevas fechas, usarlas
	        fechaInicio = LocalDateTime.parse(fechaInicioStr, formatter);
	        fechaFin = LocalDateTime.parse(fechaFinStr, formatter);
	    } else {
	        // Si no se proporcionan fechas, usar las fechas almacenadas en la sesión
	        String fechaInicioSessionStr = (String) session.getAttribute("fechaInicio");
	        String fechaFinSessionStr = (String) session.getAttribute("fechaFin");

	        if (fechaInicioSessionStr != null && !fechaInicioSessionStr.isEmpty() &&
	            fechaFinSessionStr != null && !fechaFinSessionStr.isEmpty()) {
	            // Convertir las fechas de la sesión a LocalDateTime
	            fechaInicio = LocalDateTime.parse(fechaInicioSessionStr, formatter);
	            fechaFin = LocalDateTime.parse(fechaFinSessionStr, formatter);
	        } else {
	            // Establecer fechas predeterminadas si no hay fechas en la sesión
	            fechaInicio = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
	            fechaFin = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
	        }
	    }

	    // Guardar las fechas en la sesión
	    session.setAttribute("fechaInicio", fechaInicio.format(formatter));
	    session.setAttribute("fechaFin", fechaFin.format(formatter));

	    Timestamp fechaInicioTimestamp = Timestamp.valueOf(fechaInicio);
	    Timestamp fechaFinTimestamp = Timestamp.valueOf(fechaFin);
	    
	    
	    
	    
		List<Cuenta> cuentas = cuentaDAO.getAllAccountsByUserId(usuarioId);
		List<Categoria> categorias = categoriaDAO.findAll();
		
		
		
		    
		List<CategoriaIngreso> ingresos = new ArrayList<>();
	    List<CategoriaEgreso> egresos = new ArrayList<>();
	    List<CategoriaTransferencia> transferencias = new ArrayList<>();
	    
	    List<Movimiento> movimientos = movimientoDAO.getAllMovementsByUserId(usuarioId,fechaInicioTimestamp,fechaFinTimestamp);
	    List<MovimientoDTO> movimientosDTO = movimientoDAO.getAllMovementsDTO(movimientos);
	    
	    Map<String, Double> ingresosTotales = categoriaIngresoDAO.getAllSumarizedByUserId(usuarioId,fechaInicioTimestamp,fechaFinTimestamp);
	    Map<String, Double> totalEgresos = categoriaEgresoDAO.getAllSumarizedByUserId(usuarioId,fechaInicioTimestamp,fechaFinTimestamp);
	    Map<String, Double> transferenciasTotales = categoriaTransferenciaDAO.getAllSumarizedByUserId(usuarioId,fechaInicioTimestamp,fechaFinTimestamp);


	    for (Categoria categoria : categorias) {
	        if (categoria instanceof CategoriaIngreso) {
	            ingresos.add((CategoriaIngreso) categoria);
	        } else if (categoria instanceof CategoriaEgreso) {
	            egresos.add((CategoriaEgreso) categoria);
	        } else if (categoria instanceof CategoriaTransferencia) {
	            transferencias.add((CategoriaTransferencia) categoria);
	        }
	    }

	    req.setAttribute("origen", "mostrardashboard");
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
	public void filtrar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		
		
		String origen = req.getParameter("origen");
	
		
	    // Leer fechas desde la solicitud
	    String fechaInicioStr = req.getParameter("fechaInicio");
	    String fechaFinStr = req.getParameter("fechaFin");

	    // Usar formato para fechas
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

	    LocalDateTime fechaInicio;
	    LocalDateTime fechaFin;

	    if (fechaInicioStr != null && !fechaInicioStr.isEmpty() &&
	        fechaFinStr != null && !fechaFinStr.isEmpty()) {
	        // Si se proporcionan nuevas fechas, usarlas
	        fechaInicio = LocalDateTime.parse(fechaInicioStr, formatter);
	        fechaFin = LocalDateTime.parse(fechaFinStr, formatter);
	    } else {
	        // Si no se proporcionan fechas, usar las fechas almacenadas en la sesión
	        String fechaInicioSessionStr = (String) session.getAttribute("fechaInicio");
	        String fechaFinSessionStr = (String) session.getAttribute("fechaFin");

	        if (fechaInicioSessionStr != null && !fechaInicioSessionStr.isEmpty() &&
	            fechaFinSessionStr != null && !fechaFinSessionStr.isEmpty()) {
	            // Convertir las fechas de la sesión a LocalDateTime
	            fechaInicio = LocalDateTime.parse(fechaInicioSessionStr, formatter);
	            fechaFin = LocalDateTime.parse(fechaFinSessionStr, formatter);
	        } else {
	            // Establecer fechas predeterminadas si no hay fechas en la sesión
	            fechaInicio = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
	            fechaFin = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
	        }
	    }

	    // Guardar las fechas en la sesión
	    session.setAttribute("fechaInicio", fechaInicio.format(formatter));
	    session.setAttribute("fechaFin", fechaFin.format(formatter));

	    
	    if ("mostrardashboard".equals(origen)) {
	    	resp.sendRedirect("ContabilidadController?ruta=" + origen);
	    }else if ("mostrarCuenta".equals(origen)) {
	    	int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
	    	resp.sendRedirect("ContabilidadController?ruta=" + origen + "&cuentaId="+ cuentaId);
	    	
	    }else if ("mostrarCategoria".equals(origen)) {
	    	int categoriaId = Integer.parseInt(req.getParameter("categoriaId"));
	    	resp.sendRedirect("ContabilidadController?ruta=" + origen + "&categoriaId="+ categoriaId);
	    }
	    
	    
	    
	    
	}
	public void mostrarCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // Obtener el identificador de la cuenta desde la solicitud
		int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
		HttpSession session = req.getSession();

	    // Obtener fechas desde la sesión
	    String fechaInicioStr = (String) session.getAttribute("fechaInicio");
	    String fechaFinStr = (String) session.getAttribute("fechaFin");

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	    LocalDateTime fechaInicio = fechaInicioStr != null && !fechaInicioStr.isEmpty() ?
	        LocalDateTime.parse(fechaInicioStr, formatter) :
	        LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
	    LocalDateTime fechaFin = fechaFinStr != null && !fechaFinStr.isEmpty() ?
	        LocalDateTime.parse(fechaFinStr, formatter) :
	        LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

	    // Convertir a Timestamp para su uso en la base de datos
	    Timestamp fechaInicioTimestamp = Timestamp.valueOf(fechaInicio);
	    Timestamp fechaFinTimestamp = Timestamp.valueOf(fechaFin);


	    
	    // Obtener la cuenta y sus movimientos desde el DAO
	    Cuenta cuenta = cuentaDAO.getCuentaById(cuentaId);
	    
	    List<Movimiento> movimientosEgreso =  egresoDAO.getMovimientosByCuenta(cuenta, fechaInicioTimestamp, fechaFinTimestamp);
		List<Movimiento> movimientosIngreso = ingresoDAO.getMovimientosByCuenta(cuenta, fechaInicioTimestamp, fechaFinTimestamp);
		List<Movimiento> movimientosTransferencia = transferenciaDAO.getMovimientosByCuenta(cuenta, fechaInicioTimestamp, fechaFinTimestamp);
	    
	    
		List<Movimiento> movimientos = new ArrayList<>();
		movimientos.addAll(movimientosEgreso);
		movimientos.addAll(movimientosIngreso);
		movimientos.addAll(movimientosTransferencia);

	    List<MovimientoDTO> movimientosDTO = movimientoDAO.getAllMovementsDTO(movimientos);
	    
	    
	    
	    
	    // Establecer atributos en la solicitud
	    req.setAttribute("origen", "mostrarCuenta");
	    req.setAttribute("cuenta", cuenta);
	    req.setAttribute("movimientos", movimientosDTO);
	    
	    // Redirigir a la vista verCuenta.jsp
	    req.getRequestDispatcher("jsp/verCuenta.jsp").forward(req, resp);
	}

	
	public void mostrarCategoria(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		int usuarioId =(int) req.getSession().getAttribute("usuarioId"); 
	    int categoriaId = Integer.parseInt(req.getParameter("categoriaId"));
	    
	    HttpSession session = req.getSession();

	    // Obtener fechas desde la sesión
	    String fechaInicioStr = (String) session.getAttribute("fechaInicio");
	    String fechaFinStr = (String) session.getAttribute("fechaFin");

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	    LocalDateTime fechaInicio = fechaInicioStr != null && !fechaInicioStr.isEmpty() ?
	        LocalDateTime.parse(fechaInicioStr, formatter) :
	        LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
	    LocalDateTime fechaFin = fechaFinStr != null && !fechaFinStr.isEmpty() ?
	        LocalDateTime.parse(fechaFinStr, formatter) :
	        LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

	    // Convertir a Timestamp para su uso en la base de datos
	    Timestamp fechaInicioTimestamp = Timestamp.valueOf(fechaInicio);
	    Timestamp fechaFinTimestamp = Timestamp.valueOf(fechaFin);
	    

		 Categoria categoria = categoriaDAO.findCategoriaById(categoriaId);
		 //List<Movimiento> movimientos;
		 List<Movimiento> movimientosEgreso = null;
		 List<Movimiento> movimientosIngreso = null;
		 List<Movimiento> movimientosTransferencia = null;
		 double total = 0.0;
		 List<MovimientoDTO> movimientosDTO = null;
		    if (categoria instanceof CategoriaIngreso) {
		        
		    	movimientosIngreso = ingresoDAO.findMovimientosByCategoriaIngreso(categoriaId,usuarioId, fechaInicioTimestamp,fechaFinTimestamp);
		    	System.out.println("Hola" + categoriaId + "" + usuarioId);
		    	movimientosDTO = movimientoDAO.getAllMovementsDTO(movimientosIngreso);
		    	total = categoriaIngresoDAO.getSumByUserIdAndCategory(usuarioId, categoriaId , fechaInicioTimestamp,fechaFinTimestamp);
		    	req.setAttribute("movimientos", movimientosDTO);
		    } else if (categoria instanceof CategoriaEgreso) {
		    	movimientosEgreso = egresoDAO.findMovimientosByCategoriaEgreso(categoriaId, usuarioId , fechaInicioTimestamp,fechaFinTimestamp);
		    	movimientosDTO = movimientoDAO.getAllMovementsDTO(movimientosEgreso);
		    	
		    	
		    	total = categoriaEgresoDAO.getSumByUserIdAndCategory(usuarioId,categoriaId, fechaInicioTimestamp,fechaFinTimestamp);
		    	
		    	req.setAttribute("movimientos", movimientosDTO);
		    	
		    } else if (categoria instanceof CategoriaTransferencia) {
		    	movimientosTransferencia = transferenciaDAO.findMovimientosByCategoriaTransferencia(categoriaId,usuarioId , fechaInicioTimestamp,fechaFinTimestamp);
		    	movimientosDTO = movimientoDAO.getAllMovementsDTO(movimientosTransferencia);
		        total = categoriaTransferenciaDAO.getSumByUserIdAndCategory(usuarioId,categoriaId, fechaInicioTimestamp,fechaFinTimestamp);
		        req.setAttribute("movimientos", movimientosDTO);
		    }
		    
		    req.setAttribute("origen", "mostrarCategoria");
		    req.setAttribute("categoria", categoria);
		    req.setAttribute("total", total);
		    
		    
		    req.getRequestDispatcher("jsp/verCategoria.jsp").forward(req, resp);
	}
	
	public void registrarIngresoForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
		String origen =req.getParameter("origen");
		if (origen == null || origen.isEmpty()) {
	        origen = "mostrarCuenta"; // Valor predeterminado
	    }
		Cuenta cuenta = cuentaDAO.getCuentaById(cuentaId);
		List<Categoria> categoriasIngreso = categoriaIngresoDAO.getCategoriasIngreso();
		
		System.out.println("SOY EL ORIGEN FORM " + origen);
		req.setAttribute("origen", origen);
		req.setAttribute("cuenta", cuenta);
		req.setAttribute("categorias", categoriasIngreso);
		req.getRequestDispatcher("jsp/registrarIngreso.jsp").forward(req, resp);
		
	
	
	}
	public void registrarEgresoForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
		 String origen = req.getParameter("origen");
		 if (origen == null || origen.isEmpty()) {
		        origen = "mostrarCuenta"; // Valor predeterminado
		    }
		Cuenta cuenta = cuentaDAO.getCuentaById(cuentaId);
		List<Categoria> categoriasEgreso = categoriaEgresoDAO.getCategoriasEgreso();
		req.setAttribute("origen", origen);
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
        String origen = req.getParameter("origen");
        
        Timestamp fecha = convertirFecha(fechaStr,resp);

	    monto = -monto;
	    cuentaDAO.actualizarSaldo(cuentaId, monto);
	    //movimiento
	    Egreso nuevoEgreso=  new Egreso(0,concepto,fecha,monto,(CategoriaEgreso) categoria,cuenta);
	    egresoDAO.createEgreso(nuevoEgreso);
        //actualiza la cuenta
	    if("mostrardashboard".equals(origen)) {
	    	resp.sendRedirect("ContabilidadController?ruta=" + origen + "&mensaje=Egreso Registrado exitosamente");
	    }else
	    {
	    	resp.sendRedirect("ContabilidadController?ruta=mostrarCuenta&cuentaId=" + cuentaId + "&mensaje= Egreso registrado exitosamente");
	    }
	   
        
        
		
	}
	
	public void registrarIngreso(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
        String concepto = req.getParameter("concepto");
        double monto = Double.parseDouble(req.getParameter("monto"));
        String fechaStr = req.getParameter("fecha");
        int categoriaId = Integer.parseInt(req.getParameter("categoria"));
        String origen = req.getParameter("origen");
        Cuenta cuenta = cuentaDAO.getCuentaById(cuentaId);
        
        Categoria categoria = categoriaIngresoDAO.getCategoriaById(categoriaId);

        Timestamp fecha = convertirFecha(fechaStr,resp);
	    cuentaDAO.actualizarSaldo(cuentaId, monto);
	    Ingreso nuevoIngreso =  new Ingreso(0,concepto,fecha,monto,(CategoriaIngreso) categoria,cuenta);
	    ingresoDAO.createIngreso(nuevoIngreso);
        
	 
	    if( "mostrardashboard".equals(origen)) {
	    	resp.sendRedirect("ContabilidadController?ruta=" + origen + "&mensaje=Ingreso Registrado exitosamente");
	    }else
	    {
	    	resp.sendRedirect("ContabilidadController?ruta=mostrarCuenta&cuentaId=" + cuentaId + "&mensaje=Ingreso registrado exitosamente");
	    }
        
        
	
	}
	
	public void registrarTransferenciaForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int cuentaId = Integer.parseInt(req.getParameter("cuentaId"));
		int usuarioId =(int) req.getSession().getAttribute("usuarioId"); 
		String origen = req.getParameter("origen");
		if (origen == null || origen.isEmpty()) {
	        origen = "mostrarCuenta"; // Valor predeterminado
	    }
	    // Obtener la cuenta de origen
	    Cuenta cuentaOrigen = cuentaDAO.getCuentaById(cuentaId);
	    
	    // Obtener todas las cuentas excepto la cuenta de origen
	    List<Cuenta> todasCuentas = cuentaDAO.getAllAccountsByUserId(usuarioId);
	    List<Cuenta> cuentasDestino = todasCuentas.stream()
	        .filter(cuenta -> cuenta.getIdCuenta() != cuentaId)
	        .collect(Collectors.toList());
	    List<Categoria> categorias = categoriaTransferenciaDAO.getCategoriasTransferencia();
	    
	    req.setAttribute("origen", origen);
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
        String origen = req.getParameter("origen");
        Categoria categoria  = categoriaTransferenciaDAO.getCategoriaById(categoriaId);
        
        Timestamp fecha = convertirFecha(fechaStr,resp);

	 

	    cuentaDAO.actualizarSaldo(cuentaOrigenId, -monto);
	    cuentaDAO.actualizarSaldo(cuentaDestinoId, monto);
        
        
	    
	    Transferencia nuevaTransferencia=  new Transferencia(0,concepto,fecha,monto,(CategoriaTransferencia) categoria,cuentaOrigen,cuentaDestino);
	    														//int idMovimiento, String concepto, Date fecha, double valor, CategoriaTransferencia categoriaTransferencia,  Cuenta origenCuenta, Cuenta destinoCuenta
	    transferenciaDAO.createTransferencia(nuevaTransferencia);
	    
	    if("mostrardashboard".equals(origen)) {
	    	resp.sendRedirect("ContabilidadController?ruta=" + origen + "&mensaje=Transferencia Registrada exitosamente");
	    }else
	    {
	    	resp.sendRedirect("ContabilidadController?ruta=mostrarCuenta&cuentaId=" + cuentaOrigenId + "&mensaje=Transferencia registrada exitosamente");
	    }
	    
        
        
		
	}
	
	//**//
	public void mostrarFormularioCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int usuarioId = (int) req.getSession().getAttribute("usuarioId");
	    Usuario usuario = usuarioDAO.findUsuarioById(usuarioId); // Asegúrate de tener un método para obtener un usuario por ID
	    //int usuarioId =(int) req.getSession().getAttribute("usuarioId");
	    
	    req.setAttribute("usuario", usuario);
		req.getRequestDispatcher("jsp/createCuenta.jsp").forward(req, resp);
	}
	
	public void createCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nombre = req.getParameter("nombreCuenta");
        double saldo = Double.parseDouble(req.getParameter("saldo"));
        int usuarioId = (int) req.getSession().getAttribute("usuarioId");
        Usuario usuario = usuarioDAO.findUsuarioById(usuarioId);
        
        Cuenta cuenta = new Cuenta(0,nombre,saldo,usuario);

        cuentaDAO.createCuenta(cuenta);
        resp.sendRedirect("ContabilidadController?ruta=mostrardashboard");
        //resp.sendRedirect("jsp/createCategoria.jsp");
    }
	
	public void eliminarCuenta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 String idCuentaParam = req.getParameter("idCuenta");
		    
		    if (idCuentaParam != null) {
		        int idCuenta = Integer.parseInt(idCuentaParam);
		        CuentaDAO cuentaDAO = new CuentaDAO();
		        cuentaDAO.delete(idCuenta);
		        resp.sendRedirect("ContabilidadController?ruta=mostrardashboard");
		    } else {
		        // Manejo de error si no se proporciona el idCuenta
		        req.getSession().setAttribute("errorMessage", "ID de cuenta no proporcionado.");
		        resp.sendRedirect("jsp/error.jsp");
		    }
	}
	
	public void mostrarFormularioCategoria(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("jsp/createCategoria.jsp").forward(req, resp);
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
	public void eliminarCategoria(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		int idCategoria = Integer.parseInt(req.getParameter("idCategoria"));
		categoriaDAO.eliminarCategoria(idCategoria);
		resp.sendRedirect("ContabilidadController?ruta=mostrardashboard");
		
	}
	
	public void eliminarMovimiento(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int idMovimiento =  Integer.parseInt(req.getParameter("idMovimiento"));
		String vistaOrigen = req.getParameter("vistaOrigen");
		
		Movimiento movimiento =  movimientoDAO.findMovimientoById(idMovimiento);
		
		
		
		movimientoDAO.deleteMovimiento(idMovimiento);
		if (movimiento instanceof Egreso) {
			 Egreso egreso = (Egreso) movimiento;
	        // Para un Egreso, el monto debe ser restado
	        cuentaDAO.actualizarSaldo(egreso.getOrigen().getIdCuenta(),-egreso.getMonto());
	    } else if (movimiento instanceof Ingreso) {
	    	Ingreso ingreso = (Ingreso) movimiento;
	        // Para un Ingreso, el monto debe ser sumado
	    	cuentaDAO.actualizarSaldo(ingreso.getDestino().getIdCuenta(),-ingreso.getMonto());
	    } else if (movimiento instanceof Transferencia) {
	        Transferencia transferencia = (Transferencia) movimiento;
	        // Para una Transferencia, ajustar dos cuentas
	        cuentaDAO.actualizarSaldo(transferencia.getOrigen().getIdCuenta(), transferencia.getMonto());
	        cuentaDAO.actualizarSaldo(transferencia.getDestino().getIdCuenta(),-transferencia.getMonto());
	    }
		
		
		
		
		 String redireccionURL = "ContabilidadController?ruta=mostrardashboard"; // Valor por defecto

		    if ("mostrarCuenta".equals(vistaOrigen)) {
		        redireccionURL = "ContabilidadController?ruta=mostrarCuenta&cuentaId=" + Integer.parseInt(req.getParameter("idCuenta"));
		    } else if ("mostrarCategoria".equals(vistaOrigen)) {
		    	System.out.println(req.getParameter("idCategoria"));
		        redireccionURL = "ContabilidadController?ruta=mostrarCategoria&categoriaId=" + Integer.parseInt(req.getParameter("idCategoria"));
		    }
		    
		    resp.sendRedirect(redireccionURL);
	}
	public void formActualizarMovimiento(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int  idMovimiento = Integer.parseInt(req.getParameter("idMovimiento"));
		Movimiento movimiento = movimientoDAO.findMovimientoById(idMovimiento);
		Date fecha = movimiento.getFecha();
		LocalDateTime localDateTime = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		String fechaFormateada = localDateTime.format(formatter);
		 int usuarioId = (int) req.getSession().getAttribute("usuarioId");
		
		 List<Categoria> categorias = null;
		 List<Cuenta> cuentasDestino = cuentaDAO.getAllAccountsByUserId(usuarioId);
		 req.setAttribute("cuentasDestino", cuentasDestino);
		 req.setAttribute("fechaFormateada", fechaFormateada);
	    // Agregar los datos a la solicitud
	   
	    if (movimiento instanceof Transferencia) {
	    	categorias = categoriaTransferenciaDAO.getCategoriasTransferencia();
	    	Integer destinoId = transferenciaDAO.getDestinoIdByTransferenciaId(idMovimiento);
	    	Integer origenId = transferenciaDAO.getOrigenIdByTransferenciaId(idMovimiento);
	    	Integer categoriaId = transferenciaDAO.getCategoriaIdTransferenciaId(idMovimiento);

	    	
	    	
	    	req.setAttribute("origenId", origenId);
	    	req.setAttribute("destinoId", destinoId);
	    	req.setAttribute("categoriaId", categoriaId);
	    	req.setAttribute("movimiento", movimiento);
		    req.setAttribute("categoria", categorias);
		  
	    	req.getRequestDispatcher("jsp/actualizarMovimientoTransferencia.jsp").forward(req, resp);
	    } else if (movimiento instanceof Ingreso) {
	    	Integer cuentaId = ingresoDAO.getCuentaIdByIngresoId(idMovimiento);
	    	Integer categoriaId = ingresoDAO.getCategoriaIdByIngresoId(idMovimiento);
	    	categorias = categoriaIngresoDAO.getCategoriasIngreso();
	    	
	    	
	    	req.setAttribute("destinoId", cuentaId);
	    	req.setAttribute("categoriaId", categoriaId);
	    	req.setAttribute("movimiento", movimiento);
		    req.setAttribute("categoria", categorias);
		   
	    	req.getRequestDispatcher("jsp/actualizarMovimientoIngreso.jsp").forward(req, resp);
	    } else if (movimiento instanceof Egreso) {
	    	Integer cuentaId = egresoDAO.getCuentaIdByEgresoId(idMovimiento);
	    	Integer categoriaId = egresoDAO.getCategoriaIdByEgresoId(idMovimiento);
	    	
	    	categorias = categoriaEgresoDAO.getCategoriasEgreso();
	    	req.setAttribute("movimiento", movimiento);
		    req.setAttribute("categoria", categorias);
		    req.setAttribute("origenId", cuentaId);
	    	req.setAttribute("categoriaId", categoriaId);
	    	req.getRequestDispatcher("jsp/actualizarMovimientoEgreso.jsp").forward(req, resp);
	    }
	    ;
	    
	}
	public void actualizarMovimiento(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
	        // Recoger datos del formulario
			 int idMovimiento = Integer.parseInt(req.getParameter("idMovimiento"));
		        String concepto = req.getParameter("concepto");
		        double monto = Double.parseDouble(req.getParameter("monto"));
		        String fechaStr = req.getParameter("fecha");
		        int categoriaId = Integer.parseInt(req.getParameter("categoria"));
		        
		        Timestamp fecha = convertirFecha(fechaStr, resp);
		        // Obtener los objetos necesarios
		        
		        //Proceso para eliminar el valor anterior de la cuenta 
		        Movimiento movimientoAnterior = movimientoDAO.findMovimientoById(idMovimiento);
		        double montoAnterior = movimientoAnterior.getMonto();
		       
	        

	        // Actualizar los detalles del movimiento
		        movimientoAnterior.setConcepto(concepto);
		        movimientoAnterior.setMonto(monto);
		        movimientoAnterior.setFecha(fecha);

	        // Actualizar en función del tipo de movimiento
	        if (movimientoAnterior instanceof Transferencia) {
	        	
	        	
		        Transferencia transferenciaAnterior = (Transferencia) movimientoAnterior;
		        int cuentaIdOrigenAnterior = transferenciaAnterior.getOrigen().getIdCuenta();
		        int cuentaIdDestinoAnterior = transferenciaAnterior.getDestino().getIdCuenta();
		        cuentaDAO.actualizarSaldo(cuentaIdOrigenAnterior, montoAnterior);
		        cuentaDAO.actualizarSaldo(cuentaIdDestinoAnterior, -montoAnterior);
	        	
	        	
	            Transferencia transferencia = (Transferencia) movimientoAnterior;
	            int origenId = Integer.parseInt(req.getParameter("cuentaOrigen")); 
	            int destinoId = Integer.parseInt(req.getParameter("cuentaDestino")); 
	            Cuenta origen = cuentaDAO.getCuentaById(origenId);
	            Cuenta destino = cuentaDAO.getCuentaById(destinoId);
	            Categoria categoriaTransferencia= categoriaTransferenciaDAO.getCategoriaById(categoriaId);
	            transferencia.setOrigen(origen);
	            transferencia.setDestino(destino);
	            transferencia.setCategoria((CategoriaTransferencia)categoriaTransferencia);
	            cuentaDAO.actualizarSaldo(origenId, -transferencia.getMonto());
	            cuentaDAO.actualizarSaldo(destinoId, transferencia.getMonto());
	            transferenciaDAO.updateTransferencia(transferencia);
	            
	            
	            
	            
	           // transferencia.setCuentaOrigen(cuentaDAO.findCuentaById(cuentaOrigenId));
	            //transferencia.setCuentaDestino(cuentaDAO.findCuentaById(cuentaDestinoId));
	            //transferencia.setCategoria(categoriaDAO.findCategoriaById(categoriaId));
	            //transferenciaDAO.updateTransferencia(transferencia);
	        } else if (movimientoAnterior instanceof Ingreso) {
	        	
	        	//Ingreso Anterior
		        Ingreso ingresoAnterior = (Ingreso) movimientoAnterior;
		        int cuentaIdAnterior = ingresoAnterior.getDestino().getIdCuenta();
		        cuentaDAO.actualizarSaldo(cuentaIdAnterior, -montoAnterior);
	        	
	        	//Ingreso Actualizado
	        	
	        	
	            Ingreso ingreso = (Ingreso) movimientoAnterior;
	            int cuentaId = Integer.parseInt(req.getParameter("cuentaDestino")); 
	            Cuenta destino = cuentaDAO.getCuentaById(cuentaId);
	            Categoria categoriaIngreso = categoriaIngresoDAO.getCategoriaById(categoriaId);
	            ingreso.setDestino(destino);
	            ingreso.setCategoria((CategoriaIngreso)categoriaIngreso);
	            cuentaDAO.actualizarSaldo(cuentaId, ingreso.getMonto());
	            ingresoDAO.updateIngreso(ingreso);
	            
	        } else if (movimientoAnterior instanceof Egreso) {

		        Egreso egresoAnterior = (Egreso) movimientoAnterior;
		        int cuentaIdAnterior = egresoAnterior.getOrigen().getIdCuenta();
		        cuentaDAO.actualizarSaldo(cuentaIdAnterior, -montoAnterior);
	        	
	        	
	        	
	        	//Egreso Modificado
	            Egreso egreso = (Egreso) movimientoAnterior;
	            int cuentaId = Integer.parseInt(req.getParameter("cuentaOrigen")); 
	            Cuenta origen = cuentaDAO.getCuentaById(cuentaId);
	            Categoria categoriaEgreso = categoriaEgresoDAO.getCategoriaById(categoriaId);
	            egreso.setMonto(-monto);
	            egreso.setOrigen(origen);
	            egreso.setCategoria((CategoriaEgreso)categoriaEgreso);
	            cuentaDAO.actualizarSaldo(cuentaId, egreso.getMonto());
	            egresoDAO.updateEgreso(egreso);
	            //egreso.setCuentaOrigen(cuentaDAO.findCuentaById(cuentaOrigenId));
	            //egreso.setCategoria(categoriaDAO.findCategoriaById(categoriaId));
	            //egresoDAO.updateEgreso(egreso);
	        }

	        // Redirigir a la página de éxito o mostrar un mensaje de éxito
	        resp.sendRedirect("ContabilidadController?ruta=mostrardashboard&mensaje=Movimiento Modificado exitosamente");
	    } catch (Exception e) {
	        e.printStackTrace();
	        req.setAttribute("error", "Error al actualizar el movimiento: " + e.getMessage());
	        req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);
	    }
	}

	private void ruteador(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String ruta = (req.getParameter("ruta") == null)? "iniciar": req.getParameter("ruta");
		switch (ruta) {
				case "ingresar": 
					this.ingresar(req, resp);
					break;
				case "autenticar":
					this.autenticar(req, resp);
					break;
				case "mostrarFormUsuario":
					this.mostrarFormularioUsuario(req,resp);
					break;
					
				case "createUsuario":
					this.createUsuario(req,resp);
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
				case "eliminarCuenta":
	                this.eliminarCuenta(req, resp);
	                break;
				case "mostrarFormularioCategoria":
	                this.mostrarFormularioCategoria(req, resp);
	                break;    
				case "createCategoria":
	                this.crearCategoria(req, resp);
	                break;
				case "eliminarCategoria":
					this.eliminarCategoria(req,resp);
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
				case "actualizarMovimiento":
					this.actualizarMovimiento(req,resp);
					break;
				case "formActualizarMovimiento":
					this.formActualizarMovimiento(req, resp);
					break;
				case "eliminarMovimiento":
					this.eliminarMovimiento(req,resp);
					break;
				case "filtrar":
					this.filtrar(req, resp);
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
	
	public Timestamp convertirFecha(String fechaStr, HttpServletResponse resp) throws IOException {
	    if (fechaStr == null || fechaStr.trim().isEmpty()) {
	        // Manejo de error si la fecha es nula o vacía
	        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Fecha no proporcionada");
	        return null;
	    }

	    Timestamp fecha = null;
	    try {
	        fechaStr = fechaStr.replace("T", " "); // Reemplazar 'T' con un espacio para el formato correcto
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        dateFormat.setLenient(false); // Asegurarse de que el análisis sea estricto
	        Date parsedDate = dateFormat.parse(fechaStr);
	        fecha = new Timestamp(parsedDate.getTime()); // Convertir la fecha del formulario a un objeto Timestamp
	    } catch (ParseException e) {
	        // Manejo de error si la fecha no es válida
	        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Fecha inválida");
	        return null;
	    }

	    return fecha;
	}
	
}
