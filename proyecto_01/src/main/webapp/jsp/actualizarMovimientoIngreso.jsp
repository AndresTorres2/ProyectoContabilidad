<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Actualizar Movimiento Ingreso</title>
</head>
<body>
    <h1>Actualizar Movimiento Ingreso</h1>
    <form action="ContabilidadController?ruta=actualizarMovimiento" method="post">
        <input type="hidden" name="idMovimiento" value="${movimiento.idMovimiento}">
        
        <label for="concepto">Concepto:</label>
        <input type="text" id="concepto" name="concepto" value="${movimiento.concepto}" required><br>
        
        <label for="monto">Monto:</label>
        <input type="number" id="monto" name="monto" value="${movimiento.monto}" step="0.01" required><br>
        
        <label for="fecha">Fecha y Hora:</label>
        <input type="datetime-local" id="fecha" name="fecha" value="${movimiento.fecha}" required><br>

        <!-- Para Ingreso, solo se muestra la cuenta de destino -->
        <label for="destino">Cuenta de Destino:</label>
        <input type="text" id="destino" name="destino" value="${movimiento.destino}" readonly><br>
        
        <label for="categoria">Categoría:</label>
        <select id="categoria" name="categoria" required>
            <c:forEach var="categoria" items="${categorias}">
                <option value="${categoria.idCategoria}" ${categoria.idCategoria == movimiento.categoriaId ? 'selected' : ''}>
                    ${categoria.nombreCategoria}
                </option>
            </c:forEach>
        </select>
        <br>
        
        <input type="submit" value="Actualizar Movimiento">
        
        <br><br>
        <!--<a href="ContabilidadController?ruta=mostrarCuenta&cuentaId=${cuenta.idCuenta}">Cancelar</a>-->
    </form>
</body>
</html>
