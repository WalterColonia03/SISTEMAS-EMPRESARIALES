package Modelo;

import Clases.Compra;
import Clases.DetalleCompra;
import Utils.LoggerGlobal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * DAO para la entidad Compra (registro de órdenes de compra a proveedores).
 *
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `double` → `BigDecimal` en total y precioUnitario.
 *   - `e.printStackTrace()` → `LoggerGlobal.error()` en bloques catch del rollback.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §2.A, §3.C, §4.1)
 *
 * NOTA: La transaccionalidad ACID ya estaba correctamente implementada.
 *       Esta clase mantiene setAutoCommit(false)/commit()/rollback().
 */
public class CompraDAO {

    /**
     * Registra una compra completa (cabecera + detalles + incremento de stock + kardex).
     * Operación transaccional: si cualquier paso falla, se revierte todo.
     */
    public boolean registrarCompra(Compra c) {
        String sqlCompra  = "INSERT INTO tb_compra (idProveedor, total, fecha) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO tb_detalle_compra (idCompra, idProducto, cantidad, precioUnitario) " +
                            "VALUES (?, ?, ?, ?)";
        String sqlStock   = "UPDATE tb_producto SET cantidad = cantidad + ? WHERE idProducto = ?";
        String sqlKardex  = "INSERT INTO tb_kardex (idProducto, tipoMovimiento, cantidad, fecha, motivo) " +
                            "VALUES (?, 'ENTRADA', ?, ?, 'COMPRA')";

        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            // 1. Insertar cabecera de compra
            int idCompraGenerado;
            try (PreparedStatement ps = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, c.getIdProveedor());
                ps.setBigDecimal(2, c.getTotal());   // CORREGIDO: era setDouble
                ps.setString(3, c.getFecha());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("No se generó ID para la compra");
                    idCompraGenerado = rs.getInt(1);
                }
            }

            // 2. Insertar detalles, incrementar stock y registrar en kardex (batch)
            try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle);
                 PreparedStatement psStock   = conn.prepareStatement(sqlStock);
                 PreparedStatement psKardex  = conn.prepareStatement(sqlKardex)) {

                for (DetalleCompra d : c.getDetalles()) {
                    psDetalle.setInt(1, idCompraGenerado);
                    psDetalle.setInt(2, d.getIdProducto());
                    psDetalle.setInt(3, d.getCantidad());
                    psDetalle.setBigDecimal(4, d.getPrecioUnitario());  // CORREGIDO: era setDouble
                    psDetalle.addBatch();

                    psStock.setInt(1, d.getCantidad());
                    psStock.setInt(2, d.getIdProducto());
                    psStock.addBatch();

                    psKardex.setInt(1, d.getIdProducto());
                    psKardex.setInt(2, d.getCantidad());
                    psKardex.setString(3, c.getFecha());
                    psKardex.addBatch();
                }
                psDetalle.executeBatch();
                psStock.executeBatch();
                psKardex.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException rbEx) {
                    LoggerGlobal.error("Error al hacer ROLLBACK de compra", rbEx); // CORREGIDO
                }
            }
            LoggerGlobal.error("CompraDAO.registrarCompra() falló — se hizo ROLLBACK", ex); // CORREGIDO
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ex) {
                    LoggerGlobal.error("Error al restaurar autoCommit en CompraDAO", ex); // CORREGIDO
                }
            }
        }
    }
}
