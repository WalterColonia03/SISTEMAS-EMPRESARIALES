package Modelo;

import Clases.Producto;
import Utils.LoggerGlobal;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Producto.
 *
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00 â€” Auditoría ERP):
 *   - `double` â†’ `BigDecimal` en precio (precisión financiera).
 *   - `e.printStackTrace()` â†’ `LoggerGlobal.error()` (observabilidad en .jar).
 *   - `SELECT *` â†’ columnas explícitas (Clean Code â€” evitar mapeos frágiles).
 *     (INSTRUCCIONES_IA_PROYECTO_ERP Â§2.A, Â§3.C, Â§4.1)
 */
public class ProductoDAO {

    /**
     * Lista todos los productos activos e inactivos de la BD.
     */
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado " +
                     "FROM tb_producto";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getBigDecimal("precio"),   // CORREGIDO: era getDouble
                        rs.getString("descripcion"),
                        rs.getInt("idCategoria"),
                        rs.getInt("estado")
                );
                lista.add(p);
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ProductoDAO.listarTodos() falló", ex); // CORREGIDO: era printStackTrace
        }
        return lista;
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE tb_producto SET nombre=?, cantidad=?, precio=?, " +
                     "descripcion=?, idCategoria=?, estado=? WHERE idProducto=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getCantidad());
            ps.setBigDecimal(3, p.getPrecio());   // CORREGIDO: era setDouble
            ps.setString(4, p.getDescripcion());
            ps.setInt(5, p.getIdCategoria());
            ps.setInt(6, p.getEstado());
            ps.setInt(7, p.getIdProducto());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("ProductoDAO.actualizar() falló para id=" + p.getIdProducto(), ex);
            return false;
        }
    }

    public boolean guardar(Producto p) {
        String sql = "INSERT INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getIdProducto());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getCantidad());
            ps.setBigDecimal(4, p.getPrecio());   // CORREGIDO: era setDouble
            ps.setString(5, p.getDescripcion());
            ps.setInt(6, p.getIdCategoria());
            ps.setInt(7, p.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("ProductoDAO.guardar() falló para producto=" + p.getNombre(), ex);
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM tb_producto WHERE idProducto=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("ProductoDAO.eliminar() falló para id=" + id, ex);
            return false;
        }
    }

    public int generarId() {
        String sql = "SELECT COALESCE(MAX(idProducto), 0) + 1 FROM tb_producto";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            LoggerGlobal.error("ProductoDAO.generarId() falló", ex);
        }
        return 1;
    }

    // FR-002/Dashboard: cuenta productos con stock < stockMinimo (default 5)
    public int contarProductosStockBajo() {
        String sql = "SELECT COUNT(*) FROM tb_producto WHERE cantidad < 5 AND estado = 1";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            LoggerGlobal.error("ProductoDAO.contarProductosStockBajo() fallo", ex);
        }
        return 0;
    }

    // FR-002/FR-018: lista productos con stock bajo para dashboard y reporte
    public List<Object[]> productosConStockBajo() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT p.idProducto, p.nombre, p.cantidad, 5 AS stockMin, " +
                     "       (5 - p.cantidad) AS faltante " +
                     "FROM tb_producto p WHERE p.cantidad < 5 AND p.estado = 1 " +
                     "ORDER BY faltante DESC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{ rs.getInt("idProducto"), rs.getString("nombre"), rs.getInt("cantidad"), rs.getInt("stockMin"), rs.getInt("faltante") });
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ProductoDAO.productosConStockBajo() fallo", ex);
        }
        return lista;
    }

    /**
     * Busca un producto específico por su identificador único (ID).
     * Capa: DAO — Implementa: FR-022 (Búsqueda en POS) y RNF-01 (Rendimiento).
     * Se utiliza para obtener un solo producto desde la BD vía índice primario (WHERE idProducto),
     * en lugar de cargar todos los productos en memoria (evita OOM y lentitud al usar lector de barras).
     *
     * @param id Identificador numérico del producto (código de barras)
     * @return Instancia del Producto o null si no existe.
     */
    public Producto buscarPorId(int id) {
        String sql = "SELECT idProducto,nombre,cantidad,precio,descripcion,idCategoria,estado FROM tb_producto WHERE idProducto = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(rs.getInt("idProducto"), rs.getString("nombre"), rs.getInt("cantidad"), rs.getBigDecimal("precio"), rs.getString("descripcion"), rs.getInt("idCategoria"), rs.getInt("estado"));
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ProductoDAO.buscarPorId() fallo id=" + id, ex);
        }
        return null;
    }

    // Busca producto por nombre exacto (para POS y validaciones)
    public Producto buscarPorNombre(String nombre) {
        String sql = "SELECT idProducto,nombre,cantidad,precio,descripcion,idCategoria,estado FROM tb_producto WHERE nombre = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(rs.getInt("idProducto"), rs.getString("nombre"), rs.getInt("cantidad"), rs.getBigDecimal("precio"), rs.getString("descripcion"), rs.getInt("idCategoria"), rs.getInt("estado"));
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ProductoDAO.buscarPorNombre() fallo nombre=" + nombre, ex);
        }
        return null;
    }

    // Busca productos cuyo nombre contiene el termino (para POS FR-022)
    public List<Producto> buscarPorTermino(String termino) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT idProducto,nombre,cantidad,precio,descripcion,idCategoria,estado FROM tb_producto WHERE LOWER(nombre) LIKE ? AND estado = 1";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + termino.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Producto(rs.getInt("idProducto"), rs.getString("nombre"), rs.getInt("cantidad"), rs.getBigDecimal("precio"), rs.getString("descripcion"), rs.getInt("idCategoria"), rs.getInt("estado")));
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ProductoDAO.buscarPorTermino() fallo termino=" + termino, ex);
        }
        return lista;
    }
}
