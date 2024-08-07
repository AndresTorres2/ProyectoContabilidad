<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registrar Transferencia</title>
</head>
<body>
    <h1>Nueva Transferencia</h1>
    <form action="ContabilidadController?ruta=transferir&cuentaOrigenId=${cuenta.idCuenta}" method="post">
        <label for="concepto">Concepto:</label>
        <input type="text" id="concepto" name="concepto" required><br>
        
        <label for="monto">Monto:</label>
        <input type="number" id="monto" name="monto" step="0.01" required><br>
        
        <label for="fecha">Fecha y Hora:</label>
		<input type="datetime-local" id="fecha" name="fecha" required><br>

        
        <label for="origen">Cuenta de Origen:</label>
        <input type="text" id="origen" name="origen" value="${cuenta.nombreCuenta}" readonly><br>
        
        <label for="destino">Cuenta de Destino:</label>
        <select id="destino" name="destino" required>
            <c:forEach var="cuentaDestino" items="${cuentasDestino}">
                <option value="${cuentaDestino.idCuenta}">${cuentaDestino.nombreCuenta}</option>
                
            </c:forEach>  
        </select><br>
        
        <label for="categoria">Categoría:</label>
        <select id="categoria" name="categoria" required>
            <c:forEach var="categoria" items="${categorias}">
                <option value="${categoria.idCategoria}">${categoria.nombreCategoria}</option>
            </c:forEach>   
        </select>
        <br>
        
        <input type="submit" value="Registrar Transferencia">
        
        <br>
        <br>
        <a href="ContabilidadController?ruta=mostrarCuenta&cuentaId=${cuenta.idCuenta}" >Cancelar</a>
    </form>
</body>
</html>
