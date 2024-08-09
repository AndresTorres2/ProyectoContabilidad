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
    
   
	  <%@include file="../template/fecha.html" %>
    
     <!-- Mostrar mensaje de éxito si está presente -->
    <c:if test="${param.mensaje != null}">
        <p style="color: green;">${param.mensaje}</p>
    </c:if>
    
    
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
                    <td> <a href="ContabilidadController?ruta=formActualizarMovimiento&idMovimiento=${movimiento.idMovimiento}" >Editar(En Construcción)</a> </td>
                    <td> <a href="#" class="eliminarMovimiento" data-idCuenta="${cuenta.idCuenta}" data-id="${movimiento.idMovimiento}" data-nombre="${movimiento.concepto}" >Eliminar</a> </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <a href="ContabilidadController?ruta=registrarIngresoForm&cuentaId=${cuenta.idCuenta}" >Registrar un Nuevo Ingreso</a>
    <br><br>
    <a href="ContabilidadController?ruta=registrarEgresoForm&cuentaId=${cuenta.idCuenta}" >Registrar un Nuevo Egreso</a>
    <br><br>
    <a href="ContabilidadController?ruta=registrarTransferenciaForm&cuentaId=${cuenta.idCuenta}" >Registrar una Nueva Transferencia</a>
    <br><br>
    <a href="ContabilidadController?ruta=mostrardashboard" >Regresar</a>
</body>

<script type="text/javascript">
document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll(".eliminarMovimiento").forEach(function(eliminarLink) {
        eliminarLink.addEventListener("click", function(event) {
            event.preventDefault();
            
            var idMovimiento = this.getAttribute("data-id");
            var conceptoMovimiento = this.getAttribute("data-nombre");
            var idCuenta = this.getAttribute("data-idCuenta");
            var origen = "mostrarCuenta";
            
            var confirmacion = confirm("¿Desea eliminar este movimiento: " + conceptoMovimiento + "?");
            
            if (confirmacion) {
                // Redirige al controlador para eliminar la categoría
                window.location.href = "ContabilidadController?ruta=eliminarMovimiento&idMovimiento=" + idMovimiento +"&idCuenta="+ idCuenta + "&vistaOrigen=" + origen;
            }
        });
    });
});


</script>

</html>