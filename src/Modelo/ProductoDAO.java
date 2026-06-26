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
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `double` → `BigDecimal` en precio (precisión financiera).
 *   - `e.printStackTrace()` → `LoggerGlobal.error()` (observabilidad en .jar).
 *   - `SELECT *` → columnas explícitas (Clean Code — evitar mapeos frágiles).
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §2.A, §3.C, §4.1)
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
}
