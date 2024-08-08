<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login de Cartera Virtual</title>
</head>
<body>
	<form method="POST" action="../ContabilidadController?ruta=ingresar">
		<fieldset>
			<legend>
				<label>Login</label>
				<label>Usuario:</label><br>
				<input type= "text"  id="usuario" name = "usuario"/> 
				
				<br><br>
				
				<label>Password:</label><br>
				<input type="password" id="clave" name= "clave"/>
				
				<br><br>
				
				<input type="submit" value="Ingresar"/>
			</legend>
		</fieldset>
	</form>

	<c:if test="${sessionScope.errorMessage != null}">
        <p style="color: RED;">${sessionScope.errorMessage}</p>
        <!-- Limpiar el mensaje después de mostrarlo -->
        <c:remove var="errorMessage"/>
    </c:if>
	<a href= "../ContabilidadController?ruta=mostrarFormUsuario" >Crear Nuevo Usuario</a>

</body>
</html>