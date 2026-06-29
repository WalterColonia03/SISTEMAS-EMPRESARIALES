package Modelo;

import Clases.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class MigracionDB {

    public static void main(String[] args) {
        System.out.println("Iniciando Fase 1: Creación de tablas base...");
        try {
            crearTablas();
            System.out.println("Tablas base verificadas/creadas exitosamente.");
        } catch (Exception e) {
            System.err.println("Ocurrió un error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar();
        }
    }

    public static void crearTablas() throws SQLException {
        System.out.println("Verificando/Creando tablas en la base de datos...");
        Connection conn = ConexionDB.getConexion();
        Statement stmt = conn.createStatement();
        
        String sqlCategoria = "CREATE TABLE IF NOT EXISTS tb_categoria (" +
                "idCategoria INT PRIMARY KEY, " +
                "descripcion VARCHAR(100), " +
                "estado INT)";
        stmt.execute(sqlCategoria);

        String sqlUsuario = "CREATE TABLE IF NOT EXISTS tb_usuario (" +
                "idUsuario INT PRIMARY KEY, " +
                "nombre VARCHAR(100), " +
                "apellido VARCHAR(100), " +
                "usuario VARCHAR(50), " +
                "telefono VARCHAR(20), " +
                "password VARCHAR(100), " +
                "rol VARCHAR(50), " +
                "estado INT)";
        stmt.execute(sqlUsuario);

        String sqlCliente = "CREATE TABLE IF NOT EXISTS tb_cliente (" +
                "idCliente INT PRIMARY KEY, " +
                "nombre VARCHAR(100), " +
                "apellido VARCHAR(100), " +
                "dni VARCHAR(20), " +
                "telefono VARCHAR(20), " +
                "direccion VARCHAR(200), " +
                "estado INT)";
        stmt.execute(sqlCliente);

        String sqlProducto = "CREATE TABLE IF NOT EXISTS tb_producto (" +
                "idProducto INT PRIMARY KEY, " +
                "nombre VARCHAR(100), " +
                "cantidad INT, " +
                "precio DECIMAL(10,2), " +
                "descripcion VARCHAR(200), " +
                "idCategoria INT, " +
                "estado INT)";
        stmt.execute(sqlProducto);

        String sqlVenta = "CREATE TABLE IF NOT EXISTS tb_venta (" +
                "idVenta INT PRIMARY KEY, " +
                "cliente VARCHAR(200), " +
                "total DECIMAL(10,2), " +
                "fecha VARCHAR(50))";
        stmt.execute(sqlVenta);

        stmt.close();
    }
}
