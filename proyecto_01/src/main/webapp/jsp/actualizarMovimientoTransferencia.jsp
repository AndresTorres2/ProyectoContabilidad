<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Actualizar Movimiento Transferencia</title>
</head>
<body>
    <h1>Actualizar Movimiento Transferencia</h1>
    <form action="ContabilidadController?ruta=actualizarMovimiento" method="post">
        <input type="hidden" name="idMovimiento" value="${movimiento.idMovimiento}">
        
        <label for="concepto">Concepto:</label>
        <input type="text" id="concepto" name="concepto" value="${movimiento.concepto}" required><br>
        
        <label for="monto">Monto:</label>
        <input type="number" id="monto" name="monto" value="${movimiento.monto}" step="0.01" required><br>
        
        <label for="fecha">Fecha y Hora:</label>
        <input type="datetime-local" id="fecha" name="fecha" value="${fechaFormateada}" required><br>

		<label for="cuentaOrigen">Cuenta de Origen:</label>
		<select id="cuentaOrigen" name="cuentaOrigen" required>
		    <c:forEach var="cuenta" items="${cuentasDestino}">
		        <option value="${cuenta.idCuenta}" ${cuenta.idCuenta == origenId ? 'selected' : ''}>
		            ${cuenta.nombreCuenta}
		        </option>
		    </c:forEach>
		</select><br>


        <!-- Para Ingreso, solo se muestra la cuenta de destino -->
        <label for="cuentaDestino">Cuenta de Destino:</label>
		<select id="cuentaDestino" name="cuentaDestino" required>
		    <c:forEach var="cuenta" items="${cuentasDestino}">
		        <option value="${cuenta.idCuenta}" ${cuenta.idCuenta == destinoId ? 'selected' : ''}>
		            ${cuenta.nombreCuenta}
		        </option>
		    </c:forEach>
		</select><br>

        <label for="categoria">Categoría:</label>
		<select id="categoria" name="categoria" required>
		    <c:forEach var="categoria" items="${categoria}"> <!-- Usa el mismo nombre del atributo aquí -->
		        <option value="${categoria.idCategoria}" ${categoria.idCategoria == categoriaId ? 'selected' : ''}>
		            ${categoria.nombreCategoria}
		        </option>
		    </c:forEach>
		</select><br>
        
        <input type="submit" value="Actualizar Movimiento">
        
        <br><br>
		
        
    </form>
    <a href="javascript:history.back()">Cancelar</a>
</body>
</html>
