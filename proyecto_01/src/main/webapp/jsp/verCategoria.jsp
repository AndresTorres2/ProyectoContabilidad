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
                    <td>${movimiento.origen}</td>
                    <td>${movimiento.destino}</td>
                    <td> <a href="#" class="eliminarMovimiento" data-idCategoria="${categoria.idCategoria} " data-id="${movimiento.idMovimiento}" data-nombre="${movimiento.concepto}" >Eliminar</a> </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
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
            var  idCategoria =  this.getAttribute("data-idCategoria");
            var origen = "verCategoria";
            
            var confirmacion = confirm("¿Desea eliminar este movimiento: " + conceptoMovimiento + "?");
            
            if (confirmacion) {
                // Redirige al controlador para eliminar la categoría
                window.location.href = "ContabilidadController?ruta=eliminarMovimiento&idMovimiento=" + idMovimiento + "&idCategoria="+ idCategoria + "&vistaOrigen=" + encodeURIComponent(origen);
            }
        });
    });
});

</script>
</html>
