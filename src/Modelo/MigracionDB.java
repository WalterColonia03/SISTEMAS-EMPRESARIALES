package Modelo;

import ArchivosTXT.*;
import Clases.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

public class MigracionDB {

    public static void main(String[] args) {
        System.out.println("Iniciando Fase 1: Migración de datos históricos (TXT -> MySQL)...");
        try {
            crearTablas();
            migrarCategorÃ­as();
            migrarUsuarios();
            migrarClientes();
            migrarProductos();
            migrarVentas();
            System.out.println("Migración completada exitosamente.");
        } catch (Exception e) {
            System.err.println("Ocurrió un error durante la migración: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionDB.cerrar();
        }
    }

    private static void crearTablas() throws SQLException {
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

    private static void migrarCategorÃ­as() throws SQLException {
        System.out.println("Migrando Categorías...");
        ArchivoCategoriaTXT txt = new ArchivoCategoriaTXT();
        List<Categoria> lista = txt.leer();
        
        Connection conn = ConexionDB.getConexion();
        String sql = "INSERT INTO tb_categoria (idCategoria, descripcion, estado) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE descripcion=VALUES(descripcion), estado=VALUES(estado)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        for (Categoria c : lista) {
            ps.setInt(1, c.getIdCategoria());
            ps.setString(2, c.getDescripcion());
            ps.setInt(3, c.getEstado());
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }

    private static void migrarUsuarios() throws SQLException {
        System.out.println("Migrando Usuarios...");
        ArchivoUsuarioTXT txt = new ArchivoUsuarioTXT();
        List<Usuario> lista = txt.leer();

        Connection conn = ConexionDB.getConexion();
        String sql = "INSERT INTO tb_usuario (idUsuario, nombre, apellido, usuario, telefono, password, rol, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), apellido=VALUES(apellido), " +
                     "usuario=VALUES(usuario), telefono=VALUES(telefono), password=VALUES(password), " +
                     "rol=VALUES(rol), estado=VALUES(estado)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for (Usuario u : lista) {
            ps.setInt(1, u.getIdUsuario());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getUsuario());
            ps.setString(5, u.getTelefono());
            // CORRECCIÓN OWASP A02: contraseña hasheada con SHA-256 antes de persistir.
            // Si el archivo .txt ya tiene el hash, se usa directamente.
            // Si tiene texto plano (< 64 chars), se hashea ahora.
            String pwd = u.getPassword();
            if (pwd != null && pwd.length() < 64) {
                pwd = Utils.PasswordUtils.hashPassword(pwd);
            }
            ps.setString(6, pwd);
            ps.setString(7, u.getRol());
            ps.setInt(8, u.getEstado());
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
        System.out.println("  Contraseñas de usuarios migradas con hash SHA-256.");
    }

    private static void migrarClientes() throws SQLException {
        System.out.println("Migrando Clientes...");
        ArchivoClienteTXT txt = new ArchivoClienteTXT();
        List<Cliente> lista = txt.leer();
        
        Connection conn = ConexionDB.getConexion();
        String sql = "INSERT INTO tb_cliente (idCliente, nombre, apellido, dni, telefono, direccion, estado) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), apellido=VALUES(apellido), dni=VALUES(dni), telefono=VALUES(telefono), direccion=VALUES(direccion), estado=VALUES(estado)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        for (Cliente c : lista) {
            ps.setInt(1, c.getIdCliente());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getApellido());
            ps.setString(4, c.getDni());
            ps.setString(5, c.getTelefono());
            ps.setString(6, c.getDireccion());
            ps.setInt(7, c.getEstado());
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }

    private static void migrarProductos() throws SQLException {
        System.out.println("Migrando Productos...");
        ArchivoProductoTXT txt = new ArchivoProductoTXT();
        List<Producto> lista = txt.leer();
        
        Connection conn = ConexionDB.getConexion();
        String sql = "INSERT INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), cantidad=VALUES(cantidad), precio=VALUES(precio), descripcion=VALUES(descripcion), idCategoria=VALUES(idCategoria), estado=VALUES(estado)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        for (Producto p : lista) {
            ps.setInt(1, p.getIdProducto());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getCantidad());
            ps.setBigDecimal(4, p.getPrecio()); // CORRECCIÓN: era setDouble
            ps.setString(5, p.getDescripcion());
            ps.setInt(6, p.getIdCategoria());
            ps.setInt(7, p.getEstado());
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }

    private static void migrarVentas() throws SQLException {
        System.out.println("Migrando Ventas...");
        ArchivoVentaTXT txt = new ArchivoVentaTXT();
        List<Venta> lista = txt.leer();
        
        Connection conn = ConexionDB.getConexion();
        String sql = "INSERT INTO tb_venta (idVenta, cliente, total, fecha) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE cliente=VALUES(cliente), total=VALUES(total), fecha=VALUES(fecha)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        for (Venta v : lista) {
            ps.setInt(1, v.getIdVenta());
            ps.setString(2, v.getCliente());
            ps.setBigDecimal(3, v.getTotal()); // CORRECCIÓN: era setDouble
            ps.setString(4, v.getFecha());
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }
}
