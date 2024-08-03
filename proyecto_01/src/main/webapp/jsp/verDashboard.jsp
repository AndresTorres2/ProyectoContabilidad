<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard</title>
</head>
<body>
	<h1>Estado de la Contabilidad</h1>
	
    <h2>Consolidado de las Cuentas</h2>
     <table border="1">
        <thead>
            <tr>
                <th>Nombre</th>
                <th>Saldo</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="cuenta" items="${cuentas}">
                <tr>
                    <td> <a href="ContabilidadController?ruta=mostrarCuenta&cuentaId=${cuenta.idCuenta}">${cuenta.nombreCuenta}</a> </td>
                    <td>${cuenta.total}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
     <h2>Categoría de Ingreso</h2>
    <table border="1">
        <thead>
            <tr>
                <th>Nombre de Categoría</th>
                <th>Total</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="categoria" items="${ingresos}">
                <tr>
                    <td> <a href="ContabilidadController?ruta=mostrarCategoria&categoriaId=${categoria.idCategoria}" >${categoria.nombreCategoria}</a> </td>
                    <td>${totalIngresos[categoria.nombreCategoria]}</td> 
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <h2>Categoría de Egreso</h2>
    <table border="1">
        <thead>
            <tr>
                <th>Nombre de Categoría</th>
                <th>Total</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="categoria" items="${egresos}">
                <tr>
                    <td> <a href="ContabilidadController?ruta=mostrarCategoria&categoriaId=${categoria.idCategoria}" >${categoria.nombreCategoria} </a> </td>
                    <td>${totalEgresos[categoria.nombreCategoria]}</td> 
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <h2>Categoría de Transferencia</h2>
    <table border="1">
        <thead>
            <tr>
                <th>Nombre de Categoría</th>
                <th>Total</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="categoria" items="${transferencias}">
                <tr>
                    <td> <a href="ContabilidadController?ruta=mostrarCategoria&categoriaId=${categoria.idCategoria}" >${categoria.nombreCategoria}</a> </td>
                    <td>${totalTransferencias[categoria.nombreCategoria]}</td> 
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <h2>Movimientos</h2>
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
                    <td>${movimiento.origen.nombreCuenta}</td>
                    <td>${movimiento.destino.nombreCuenta}</td>
                    <td>${movimiento.categoria.nombreCategoria}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>