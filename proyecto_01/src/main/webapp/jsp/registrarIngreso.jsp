<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registrar Ingreso</title>
</head>
<body>
    <h1>Nuevo Ingreso</h1>
    
    <form action="ContabilidadController?ruta=registrarIngreso&cuentaId=${cuenta.idCuenta}" method="post">
    	<input type="hidden" name="origen" value="${origen}"> 
        <label for="concepto">Concepto:</label>
        <input type="text" id="concepto" name="concepto" required><br>
        
        
        <label for="monto">Monto:</label>
        <input type="number" id="monto" name="monto" step="0.01" required><br>
        
        <label for="fecha">Fecha y Hora:</label>
		<input type="datetime-local" id="fecha" name="fecha" required><br>

        
        <label for="destino">Cuenta de Destino:</label>
        <input type="text" id="destino" name="destino" value="${cuenta.nombreCuenta}" readonly><br>
        
        <br>
        
        <label for="categoria">Categoría:</label>
        <select id="categoria" name="categoria" required>
            <c:forEach var="categoria" items="${categorias}">
                <option value="${categoria.idCategoria}">${categoria.nombreCategoria}</option>
            </c:forEach>   
        </select><br>
        
        <input type="submit" value="Registrar Ingreso">
        
        <br>
        <br>
        <a href="ContabilidadController?ruta=${origen}&cuentaId=${cuenta.idCuenta}" >Cancelar</a>
    </form>
</body>
</html>
