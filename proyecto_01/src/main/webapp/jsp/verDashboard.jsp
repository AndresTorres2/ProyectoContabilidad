<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Dashboard</title>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/verDash.css">
        </head>

        <body>

            <div class="encabezado">
                <h1>Estado de la Contabilidad</h1>

                <c:if test="${param.mensaje != null}">
                    <p style="color: green;">${param.mensaje}</p>
                </c:if>
                <%@include file="../template/fecha.html" %>
            </div>
            
            <br>
                <div class="container">
                    <div class="section">
                        <h2>Consolidado de las Cuentas</h2>
                        <div class="table-responsive">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Nombre</th>
                                        <th>Saldo</th>
                                        <th colspan="3">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="cuenta" items="${cuentas}">
                                        <tr>
                                            <td><a
                                                    href="ContabilidadController?ruta=mostrarCuenta&cuentaId=${cuenta.idCuenta}">${cuenta.nombreCuenta}</a>
                                            </td>
                                            <td>${cuenta.total}</td>
                                            <td><a
                                                    href="ContabilidadController?ruta=registrarIngresoForm&cuentaId=${cuenta.idCuenta}&origen=mostrardashboard">Registrar
                                                    Ingreso</a></td>
                                            <td><a
                                                    href="ContabilidadController?ruta=registrarEgresoForm&cuentaId=${cuenta.idCuenta}&origen=mostrardashboard">Registrar
                                                    Egreso</a></td>
                                            <td><a
                                                    href="ContabilidadController?ruta=registrarTransferenciaForm&cuentaId=${cuenta.idCuenta}&origen=mostrardashboard">Registrar
                                                    Transferencia</a></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="section">
                        <h2>Categoría de Ingreso</h2>
                        <div class="table-responsive">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Nombre de Categoría</th>
                                        <th>Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="categoria" items="${ingresos}">
                                        <tr>
                                            <td><a
                                                    href="ContabilidadController?ruta=mostrarCategoria&categoriaId=${categoria.idCategoria}">${categoria.nombreCategoria}</a>
                                            </td>
                                            <td>${totalIngresos[categoria.nombreCategoria]}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="section">
                        <h2>Categoría de Egreso</h2>
                        <div class="table-responsive">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Nombre de Categoría</th>
                                        <th>Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="categoria" items="${egresos}">
                                        <tr>
                                            <td><a
                                                    href="ContabilidadController?ruta=mostrarCategoria&categoriaId=${categoria.idCategoria}">${categoria.nombreCategoria}</a>
                                            </td>
                                            <td>${totalEgresos[categoria.nombreCategoria]}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="section">
                        <h2>Categoría de Transferencia</h2>
                        <div class="table-responsive">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Nombre de Categoría</th>
                                        <th>Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="categoria" items="${transferencias}">
                                        <tr>
                                            <td><a
                                                    href="ContabilidadController?ruta=mostrarCategoria&categoriaId=${categoria.idCategoria}">${categoria.nombreCategoria}</a>
                                            </td>
                                            <td>${totalTransferencias[categoria.nombreCategoria]}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="section section-full">
                        <h2>Movimientos</h2>
                        <div class="table-responsive">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Concepto</th>
                                        <th>Fecha</th>
                                        <th>Monto</th>
                                        <th>Cuenta Origen</th>
                                        <th>Cuenta Destino</th>
                                        <th>Categoría</th>
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
                                            <td>${movimiento.categoria}</td>
                                            <td>
                                                <div class="actions">
                                                    <a
                                                        href="ContabilidadController?ruta=formActualizarMovimiento&idMovimiento=${movimiento.idMovimiento}">Editar</a>
                                                    <a href="#" class="eliminarMovimiento"
                                                        data-id="${movimiento.idMovimiento}"
                                                        data-nombre="${movimiento.concepto}">Eliminar</a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="footer-buttons">
                    <button onclick="location.href='ContabilidadController?ruta=mostrarFormularioCuenta'">Crear
                        Cuenta</button>
                    <button onclick="location.href='ContabilidadController?ruta=mostrarFormularioCategoria'">Crear
                        Categoría</button>
                    <button onclick="location.href='ContabilidadController?ruta=cerrarSesion'">Cerrar Sesión</button>
                </div>

                <script type="text/javascript">
                    document.addEventListener("DOMContentLoaded", function () {
                        document.querySelectorAll(".eliminarCuenta").forEach(function (eliminarLink) {
                            eliminarLink.addEventListener("click", function (event) {
                                event.preventDefault();

                                var idCuenta = this.getAttribute("data-id");
                                var nombreCuenta = this.getAttribute("data-nombre");


                                var confirmacion = confirm("¿Desea eliminar esta cuenta: " + nombreCuenta + "?");

                                if (confirmacion) {
                                    // Redirige al controlador para eliminar la cuenta
                                    window.location.href = "ContabilidadController?ruta=eliminarCuenta&idCuenta=" + idCuenta;
                                }
                            });
                        });
                    });




                    document.addEventListener("DOMContentLoaded", function () {
                        document.querySelectorAll(".eliminarCategoria").forEach(function (eliminarLink) {
                            eliminarLink.addEventListener("click", function (event) {
                                event.preventDefault();

                                var idCategoria = this.getAttribute("data-id");
                                var nombreCategoria = this.getAttribute("data-nombre");

                                var confirmacion = confirm("¿Desea eliminar esta categoría: " + nombreCategoria + "?");

                                if (confirmacion) {
                                    // Redirige al controlador para eliminar la categoría
                                    window.location.href = "ContabilidadController?ruta=eliminarCategoria&idCategoria=" + idCategoria;
                                }
                            });
                        });
                    });


                    document.addEventListener("DOMContentLoaded", function () {
                        document.querySelectorAll(".eliminarMovimiento").forEach(function (eliminarLink) {
                            eliminarLink.addEventListener("click", function (event) {
                                event.preventDefault();

                                var idMovimiento = this.getAttribute("data-id");
                                var conceptoMovimiento = this.getAttribute("data-nombre");
                                var origen = "verDashboard";

                                var confirmacion = confirm("¿Desea eliminar este movimiento: " + conceptoMovimiento + "?");

                                if (confirmacion) {
                                    // Redirige al controlador para eliminar la categoría
                                    window.location.href = "ContabilidadController?ruta=eliminarMovimiento&idMovimiento=" + idMovimiento + "&vistaOrigen=" + encodeURIComponent(origen);
                                }
                            });
                        });
                    });


                </script>
        </body>

        </html>
