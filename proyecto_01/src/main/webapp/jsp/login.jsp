<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form method="POST" action="../ContabilidadController?ruta=ingresar">
		<fieldset>
			<legend>
				<label>Login</label>
				<label>Usuario:</label><br>
				<input type= "text" name = "usuario"/> 
				
				<br><br>
				
				<label>Password:</label><br>
				<input type="password" name= "clave"/>
				
				<br><br>
				
				<input type="submit" value="Ingresar"/>
			</legend>
		</fieldset>
	</form>


</body>
</html>