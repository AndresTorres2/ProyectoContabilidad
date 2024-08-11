<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Registrar Transferencia</title>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registrar.css">
        </head>

        <body>

            <div class="container">
                <h1>Nueva Transferencia</h1>
                <form action="ContabilidadController?ruta=transferir&cuentaOrigenId=${cuenta.idCuenta}" method="post">
                    <input type="hidden" name="origen" value="${origen}">

                    <label for="concepto">Concepto:</label>
                    <input type="text" id="concepto" name="concepto" required>

                    <label for="monto">Monto:</label>
                    <input type="number" id="monto" name="monto" step="0.01" min="0" required
                        oninput="validateMonto(this)">

                    <label for="fecha">Fecha y Hora:</label>
                    <input type="datetime-local" id="fecha" name="fecha" required>

                    <label for="origen">Cuenta de Origen:</label>
                    <input type="text" id="origen" name="origen" value="${cuenta.nombreCuenta}" readonly>

                    <label for="destino">Cuenta de Destino:</label>
                    <select id="destino" name="destino" required>
                        <c:forEach var="cuentaDestino" items="${cuentasDestino}">
                            <option value="${cuentaDestino.idCuenta}">${cuentaDestino.nombreCuenta}</option>
                        </c:forEach>
                    </select>

                    <label for="categoria">Categoría:</label>
                    <select id="categoria" name="categoria" required>
                        <c:forEach var="categoria" items="${categorias}">
                            <option value="${categoria.idCategoria}">${categoria.nombreCategoria}</option>
                        </c:forEach>
                    </select>

                    <input type="submit" value="Registrar Transferencia">

                    <a href="ContabilidadController?ruta=${origen}&cuentaId=${cuenta.idCuenta}">Cancelar</a>
                </form>
            </div>

            <script>
                function validateMonto(input) {
                    // Eliminar cualquier carácter que no sea dígito o punto decimal
                    input.value = input.value.replace(/[^0-9.]/g, '');

                    // Eliminar el punto decimal si hay más de uno
                    let parts = input.value.split('.');
                    if (parts.length > 2) {
                        input.value = parts[0] + '.' + parts.slice(1).join('');
                    }

                    // Asegurarse de que el valor no sea negativo
                    if (parseFloat(input.value) < 0) {
                        input.value = '0';
                    }
                }
            </script>

        </body>

        </html>
