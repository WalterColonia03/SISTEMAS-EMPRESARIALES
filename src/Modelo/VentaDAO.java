package Modelo;

import Clases.DetalleVenta;
import Clases.Venta;
import Utils.LoggerGlobal;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Venta.
 *
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00 Ã¢â‚¬â€ AuditorÃƒÂ­a ERP):
 *   - `double` Ã¢â€ â€™ `BigDecimal` en total y precioUnitario.
 *   - `e.printStackTrace()` Ã¢â€ â€™ `LoggerGlobal.error()`.
 *   - `SELECT *` Ã¢â€ â€™ columnas explÃƒÂ­citas.
 *   - Import duplicado de SQLException eliminado.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP Ã‚Â§2.A, Ã‚Â§3.C, Ã‚Â§4.1)
 */
public class VentaDAO {

    public List<Venta> listarTodos() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT idVenta, cliente, total, fecha FROM tb_venta";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venta v = new Venta(
                        rs.getInt("idVenta"),
                        rs.getString("cliente"),
                        rs.getBigDecimal("total"),   // CORREGIDO: era getDouble
                        rs.getString("fecha")
                );
                lista.add(v);
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("VentaDAO.listarTodos() fallÃƒÂ³", ex);
        }
        return lista;
    }

    public boolean guardar(Venta v) {
        String sql = "INSERT INTO tb_venta (idVenta, cliente, total, fecha) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, v.getIdVenta());
            ps.setString(2, v.getCliente());
            ps.setBigDecimal(3, v.getTotal());  // CORREGIDO: era setDouble
            ps.setString(4, v.getFecha());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("VentaDAO.guardar() fallÃƒÂ³", ex);
            return false;
        }
    }

    public boolean actualizar(Venta v) {
        String sql = "UPDATE tb_venta SET cliente=?, total=?, fecha=? WHERE idVenta=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, v.getCliente());
            ps.setBigDecimal(2, v.getTotal());  // CORREGIDO: era setDouble
            ps.setString(3, v.getFecha());
            ps.setInt(4, v.getIdVenta());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("VentaDAO.actualizar() fallÃƒÂ³ para id=" + v.getIdVenta(), ex);
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM tb_venta WHERE idVenta=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("VentaDAO.eliminar() fallÃƒÂ³ para id=" + id, ex);
            return false;
        }
    }

    public int generarId() {
        String sql = "SELECT COALESCE(MAX(idVenta), 0) + 1 FROM tb_venta";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            LoggerGlobal.error("VentaDAO.generarId() fallÃƒÂ³", ex);
        }
        return 1;
    }

    /**
     * Registra una venta completa (cabecera + detalles + descuento de stock + kardex)
     * dentro de una ÃƒÂºnica transacciÃƒÂ³n ACID.
     *
     * GarantÃƒÂ­as:
     *   - Si cualquier INSERT o UPDATE falla, se hace ROLLBACK completo.
     *   - Los IDs de venta se generan con RETURN_GENERATED_KEYS (evita MAX+1 en concurrencia).
     *   - Los precios se transmiten como BigDecimal para garantizar precisiÃƒÂ³n.
     *
     * Creado: 2026-06-25 | Revisado: 2026-06-26
     */
    public boolean registrarVentaCompleta(Venta v) {
        String sqlVenta   = "INSERT INTO tb_venta (cliente, total, fecha) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO tb_detalle_venta (idVenta, idProducto, cantidad, precioUnitario) " +
                            "VALUES (?, ?, ?, ?)";
        String sqlStock   = "UPDATE tb_producto SET cantidad = cantidad - ? WHERE idProducto = ?";
        String sqlKardex  = "INSERT INTO tb_kardex (idProducto, tipoMovimiento, cantidad, fecha, motivo) " +
                            "VALUES (?, 'SALIDA', ?, ?, 'VENTA')";

        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            // 1. Insertar cabecera y obtener ID generado por BD (thread-safe)
            int idVentaGenerado;
            try (PreparedStatement psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setString(1, v.getCliente());
                psVenta.setBigDecimal(2, v.getTotal());  // CORREGIDO: era setDouble
                psVenta.setString(3, v.getFecha());
                psVenta.executeUpdate();

                try (ResultSet rs = psVenta.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("No se generÃƒÂ³ ID para la venta");
                    idVentaGenerado = rs.getInt(1);
                }
            }

            // 2. Insertar detalles, actualizar stock y registrar en kardex (batch)
            try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle);
                 PreparedStatement psStock   = conn.prepareStatement(sqlStock);
                 PreparedStatement psKardex  = conn.prepareStatement(sqlKardex)) {

                for (DetalleVenta d : v.getDetalles()) {
                    psDetalle.setInt(1, idVentaGenerado);
                    psDetalle.setInt(2, d.getIdProducto());
                    psDetalle.setInt(3, d.getCantidad());
                    psDetalle.setBigDecimal(4, d.getPrecioUnitario());  // CORREGIDO: era setDouble
                    psDetalle.addBatch();

                    psStock.setInt(1, d.getCantidad());
                    psStock.setInt(2, d.getIdProducto());
                    psStock.addBatch();

                    psKardex.setInt(1, d.getIdProducto());
                    psKardex.setInt(2, d.getCantidad());
                    psKardex.setString(3, v.getFecha());
                    psKardex.addBatch();
                }
                psDetalle.executeBatch();
                psStock.executeBatch();
                psKardex.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException ex) {
            // Rollback ante cualquier fallo para garantizar consistencia ACID
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException rbEx) {
                    LoggerGlobal.error("Error al hacer ROLLBACK de venta", rbEx);
                }
            }
            LoggerGlobal.error("VentaDAO.registrarVentaCompleta() fallÃƒÂ³ Ã¢â‚¬â€ se hizo ROLLBACK", ex);
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ex) {
                    LoggerGlobal.error("Error al restaurar autoCommit", ex);
                }
            }
        }
    }

    // FR-001 CA-1: total de ventas del dia actual (YYYY-MM-DD)
    public java.math.BigDecimal totalVentasHoy() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM tb_venta WHERE DATE(fecha) = CURDATE()";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getBigDecimal(1);
        } catch (SQLException ex) {
            LoggerGlobal.error("VentaDAO.totalVentasHoy() fallo", ex);
        }
        return java.math.BigDecimal.ZERO;
    }

    // FR-001 CA-3: total de ventas de la semana en curso (lunes a domingo)
    public java.math.BigDecimal totalVentasSemana() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM tb_venta WHERE YEARWEEK(fecha, 1) = YEARWEEK(CURDATE(), 1)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getBigDecimal(1);
        } catch (SQLException ex) {
            LoggerGlobal.error("VentaDAO.totalVentasSemana() fallo", ex);
        }
        return java.math.BigDecimal.ZERO;
    }

    // FR-001 CA-3: total de ventas del mes en curso
    public java.math.BigDecimal totalVentasMes() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM tb_venta WHERE YEAR(fecha) = YEAR(CURDATE()) AND MONTH(fecha) = MONTH(CURDATE())";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getBigDecimal(1);
        } catch (SQLException ex) {
            LoggerGlobal.error("VentaDAO.totalVentasMes() fallo", ex);
        }
        return java.math.BigDecimal.ZERO;
    }

    // FR-001: conteo de transacciones del dia
    public int conteoVentasHoy() {
        String sql = "SELECT COUNT(*) FROM tb_venta WHERE DATE(fecha) = CURDATE()";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            LoggerGlobal.error("VentaDAO.conteoVentasHoy() fallo", ex);
        }
        return 0;
    }

    // FR-002/Dashboard: ultimas N ventas para tabla "Ventas Recientes"
    public List<Object[]> ultimasVentas(int n) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT idVenta, cliente, total, fecha FROM tb_venta ORDER BY idVenta DESC LIMIT ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, n);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{ rs.getInt("idVenta"), rs.getString("cliente"), rs.getBigDecimal("total"), rs.getString("fecha") });
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("VentaDAO.ultimasVentas() fallo", ex);
        }
        return lista;
    }

    // FR-016: historial de ventas de un cliente por su DNI
    public List<Object[]> ventasPorCliente(String dniCliente) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT idVenta, cliente, total, fecha FROM tb_venta WHERE cliente = ? ORDER BY idVenta DESC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dniCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{ rs.getInt("idVenta"), rs.getString("cliente"), rs.getBigDecimal("total"), rs.getString("fecha") });
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("VentaDAO.ventasPorCliente() fallo DNI=" + dniCliente, ex);
        }
        return lista;
    }

    public java.util.List<Object[]> obtenerDetallesVenta(int idVenta) {
        java.util.List<Object[]> detalles = new java.util.ArrayList<>();
        String sql = "SELECT d.cantidad, p.nombre, p.descripcion, d.precioUnitario, (d.cantidad * d.precioUnitario) as subtotal " +
                     "FROM tb_detalle_venta d " +
                     "JOIN tb_producto p ON d.idProducto = p.idProducto " +
                     "WHERE d.idVenta = ?";
        try (java.sql.Connection conn = ConexionDB.getConexion();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(new Object[]{
                        rs.getInt("cantidad"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBigDecimal("precioUnitario"),
                        rs.getBigDecimal("subtotal")
                    });
                }
            }
        } catch (Exception e) {
            Utils.LoggerGlobal.error("Error al obtener detalles", e);
        }
        return detalles;
    }
}