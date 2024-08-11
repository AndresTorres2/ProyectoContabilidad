<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login de Cartera Virtual</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/log.css">
</head>
<body>
    <div class="form-container">
        <h1>Login</h1>
        <form method="POST" action="../ContabilidadController?ruta=ingresar">
            <label for="usuario">Usuario:</label>
            <input type="text" id="usuario" name="usuario" required>

            <label for="clave">Password:</label>
            <input type="password" id="clave" name="clave" required>

            <input type="submit" value="Ingresar">
        </form>

        <div class="message-container">
            <c:if test="${sessionScope.errorMessage != null}">
                <p>${sessionScope.errorMessage}</p>
                <!-- Limpiar el mensaje después de mostrarlo -->
                <c:remove var="errorMessage"/>
            </c:if>
        </div>

        <div class="actions-container">
            <a href="../ContabilidadController?ruta=mostrarFormUsuario">Crear Nuevo Usuario</a>
        </div>
    </div>
</body>
</html>
