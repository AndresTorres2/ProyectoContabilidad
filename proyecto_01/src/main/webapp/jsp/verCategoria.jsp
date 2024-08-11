<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Detalles de Categoría</title>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/verCat.css">
        </head>

        <body>

            <div class="container">
                <div class="header">
                    <h1>Detalles de la Categoría</h1>
                    <%@include file="../template/fecha.html" %>
                </div>

                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Nombre de la Categoría</th>
                                <th>Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>${categoria.nombreCategoria}</td>
                                <td>${total}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="table-responsive">
                    <h2>Movimientos Asociados</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>Concepto</th>
                                <th>Fecha</th>
                                <th>Monto</th>
                                <th>Cuenta Origen</th>
                                <th>Cuenta Destino</th>
                                <th>Acciones</th>
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
                                    <td>
                                        <a href="ContabilidadController?ruta=formActualizarMovimiento&idMovimiento=${movimiento.idMovimiento}"
                                            class="editar">Editar</a>
                                        <a href="#" class="eliminarMovimiento"
                                            data-idCategoria="${categoria.idCategoria}"
                                            data-id="${movimiento.idMovimiento}"
                                            data-nombre="${movimiento.concepto}">Eliminar</a>

                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="actions-container">
                    <a href="ContabilidadController?ruta=mostrardashboard">Regresar</a>
                </div>
            </div>



        </body>
        <script type="text/javascript">

            document.addEventListener("DOMContentLoaded", function () {
                document.querySelectorAll(".eliminarMovimiento").forEach(function (eliminarLink) {
                    eliminarLink.addEventListener("click", function (event) {
                        event.preventDefault();

                        var idMovimiento = this.getAttribute("data-id");
                        var conceptoMovimiento = this.getAttribute("data-nombre");
                        var idCategoria = this.getAttribute("data-idCategoria");
                        var origen = "mostrarCategoria";

                        var confirmacion = confirm("¿Desea eliminar este movimiento: " + conceptoMovimiento + "?");

                        if (confirmacion) {
                            // Redirige al controlador para eliminar la categoría
                            window.location.href = "ContabilidadController?ruta=eliminarMovimiento&idMovimiento=" + idMovimiento + "&idCategoria=" + idCategoria + "&vistaOrigen=" + origen;
                        }
                    });
                });
            });

        </script>

        </html>
