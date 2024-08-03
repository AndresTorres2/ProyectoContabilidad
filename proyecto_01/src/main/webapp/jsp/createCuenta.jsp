<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Crear Cuenta</title>
</head>
<body>
    <h1>Crear Nueva Cuenta</h1>
    <form action="../ContabilidadController?ruta=createCuenta" method="post">
        <label for="nombre">Nombre:</label>
        <input type="text" id="nombre" name="nombre" required><br>
        <label for="saldo">Saldo:</label>
        <input type="number" id="saldo" name="saldo" step="0.01" required><br>
        <label for="idUsuario">ID Usuario:</label>
        <input type="number" id="idUsuario" name="idUsuario" required><br>
        <input type="submit" value="Crear Cuenta">
    </form>
</body>
</html>
