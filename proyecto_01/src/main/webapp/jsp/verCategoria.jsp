<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Detalles de Categoría</title>
</head>
<body>
    <h1>Detalles de la Categoría</h1>
    
    <h2>Nombre de la Categoría</h2>
    <p>${categoria.nombreCategoria}</p>
    
    <h2>Total</h2>
    <p>${total}</p>

    <h2>Movimientos Asociados</h2>
    <table border="1">
        <thead>
            <tr>
                <th>Concepto</th>
                <th>Fecha</th>
                <th>Monto</th>
                <th>Cuenta Origen</th>
                <th>Cuenta Destino</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="movimiento" items="${movimientos}">
                <tr>
                    <td>${movimiento.concepto}</td>
                    <td>${movimiento.fecha}</td>
                    <td>${movimiento.monto}</td>
                    <td>${movimiento.origen.nombreCuenta}</td>
                    <td>${movimiento.destino.nombreCuenta}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
