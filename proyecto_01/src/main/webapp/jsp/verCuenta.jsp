<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Detalle de Cuenta</title>
</head>
<body>
    <h1>Detalles de la Cuenta</h1>
    
    <h2>Cuenta ${cuenta.nombreCuenta} </h2>
    <table border="1">
        <thead>
            <tr>
                <th>Saldo</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>${cuenta.total}</td>
            </tr>
        </tbody>
    </table>
    
    <h2>Movimientos de la Cuenta</h2>
    <table border="1">
        <thead>
            <tr>
                <th>Concepto</th>
                <th>Fecha</th>
                <th>Monto</th>
                <th>Cuenta Origen</th>
                <th>Cuenta Destino</th>
                <th>Categoría</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="movimiento" items="${movimientos}">
                <tr>
                    <td>${movimiento.concepto}</td>
                    <td>${movimiento.fecha}</td>
                    <td>${movimiento.monto}</td>
                    <td>${movimiento.origen}</td>
                    <td>${movimiento.destino}</td>
                    <td>${movimiento.categoria}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <a href="ContabilidadController?ruta=registrarIngresoForm&cuentaId=${cuenta.idCuenta}" >Registrar un Nuevo Ingreso</a>
    <br><br>
    <a href="ContabilidadController?ruta=mostrardashboard" >Regresar</a>
</body>
</html>