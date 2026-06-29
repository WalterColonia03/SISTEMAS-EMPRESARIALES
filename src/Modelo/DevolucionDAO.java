package Modelo;

import Clases.Devolucion;
import Utils.LoggerGlobal;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DevolucionDAO {

    public DevolucionDAO() {
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS tb_devolucion (" +
                     "idDevolucion INT AUTO_INCREMENT PRIMARY KEY, " +
                     "idVenta INT NOT NULL, " +
                     "idProducto INT NOT NULL, " +
                     "cantidad INT NOT NULL, " +
                     "motivo VARCHAR(100), " +
                     "tipoReembolso VARCHAR(50), " +
                     "montoReembolso DECIMAL(10,2), " +
                     "fecha VARCHAR(50))";
        try (Connection conn = ConexionDB.getConexion();
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException ex) {
            LoggerGlobal.error("Error creando tabla tb_devolucion", ex);
        }
    }

    /**
     * Procesa una devolución con transaccionalidad ACID.
     */
    public boolean procesarDevolucion(Devolucion dev) throws SQLException {
        String checkVenta = "SELECT idVenta FROM tb_venta WHERE idVenta = ?";
        String checkDetalle = "SELECT precioUnitario, cantidad FROM tb_detalle_venta WHERE idVenta = ? AND idProducto = ?";
        String insertDev = "INSERT INTO tb_devolucion (idVenta, idProducto, cantidad, motivo, tipoReembolso, montoReembolso, fecha) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateStock = "UPDATE tb_producto SET cantidad = cantidad + ? WHERE idProducto = ?";
        String insertKardex = "INSERT INTO tb_kardex (idProducto, tipoMovimiento, cantidad, fecha, motivo) VALUES (?, 'ENTRADA', ?, ?, ?)";
        String insertCaja = "INSERT INTO tb_flujo_caja (fecha, tipo, concepto, monto) VALUES (?, 'EGRESO', ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false); // INICIO TRANSACCIÓN ACID

            // a) Validar que el ID de venta exista
            try (PreparedStatement ps = conn.prepareStatement(checkVenta)) {
                ps.setInt(1, dev.getIdVenta());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("La Venta ID " + dev.getIdVenta() + " no existe.");
                    }
                }
            }

            // Validar que el producto pertenezca a la venta para obtener el precio
            BigDecimal precioUnitario = BigDecimal.ZERO;
            int cantVendida = 0;
            try (PreparedStatement ps = conn.prepareStatement(checkDetalle)) {
                ps.setInt(1, dev.getIdVenta());
                ps.setInt(2, dev.getIdProducto());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("El Producto ID " + dev.getIdProducto() + " no pertenece a la Venta ID " + dev.getIdVenta());
                    }
                    precioUnitario = rs.getBigDecimal("precioUnitario");
                    cantVendida = rs.getInt("cantidad");
                }
            }
            
            if (dev.getCantidad() > cantVendida) {
                throw new SQLException("La cantidad a devolver (" + dev.getCantidad() + ") supera la cantidad vendida (" + cantVendida + ").");
            }

            BigDecimal montoReembolso = precioUnitario.multiply(new BigDecimal(dev.getCantidad()));
            dev.setMontoReembolso(montoReembolso);

            // b) Insertar el registro en la tabla de devoluciones
            try (PreparedStatement ps = conn.prepareStatement(insertDev)) {
                ps.setInt(1, dev.getIdVenta());
                ps.setInt(2, dev.getIdProducto());
                ps.setInt(3, dev.getCantidad());
                ps.setString(4, dev.getMotivo());
                ps.setString(5, dev.getTipoReembolso());
                ps.setBigDecimal(6, dev.getMontoReembolso());
                ps.setString(7, dev.getFecha());
                ps.executeUpdate();
            }

            // c) Actualizar el stock del producto sumando la cantidad devuelta
            try (PreparedStatement ps = conn.prepareStatement(updateStock)) {
                ps.setInt(1, dev.getCantidad());
                ps.setInt(2, dev.getIdProducto());
                ps.executeUpdate();
            }

            // d) Insertar un movimiento en el Kardex indicando "Devolución - Venta [ID]"
            try (PreparedStatement ps = conn.prepareStatement(insertKardex)) {
                ps.setInt(1, dev.getIdProducto());
                ps.setInt(2, dev.getCantidad());
                ps.setString(3, dev.getFecha());
                ps.setString(4, "Devolución - Venta " + dev.getIdVenta());
                ps.executeUpdate();
            }

            // e) Insertar un movimiento de EGRESO en el Flujo de Caja por el monto devuelto al cliente
            if ("Efectivo".equalsIgnoreCase(dev.getTipoReembolso())) {
                try (PreparedStatement ps = conn.prepareStatement(insertCaja)) {
                    ps.setString(1, dev.getFecha());
                    ps.setString(2, "Devolución a Cliente - Venta " + dev.getIdVenta());
                    ps.setBigDecimal(3, dev.getMontoReembolso());
                    ps.executeUpdate();
                }
            }

            conn.commit(); // CONFIRMAR TRANSACCIÓN
            return true;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); // REVERTIR CAMBIOS EN CASO DE ERROR
                } catch (SQLException rbEx) {
                    LoggerGlobal.error("Error al hacer ROLLBACK de devolución", rbEx);
                }
            }
            throw ex; // RELANZAR LA EXCEPCIÓN PARA LA VISTA
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    LoggerGlobal.error("Error restaurando autocommit", ex);
                }
            }
        }
    }

    public List<Object[]> listarDevolucionesDesc() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT d.idVenta, p.nombre AS producto, d.cantidad, d.motivo, d.tipoReembolso, d.fecha " +
                     "FROM tb_devolucion d " +
                     "INNER JOIN tb_producto p ON d.idProducto = p.idProducto " +
                     "ORDER BY d.idDevolucion DESC LIMIT 30";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("idVenta"),
                    rs.getString("producto"),
                    rs.getInt("cantidad"),
                    rs.getString("motivo"),
                    rs.getString("tipoReembolso"),
                    rs.getString("fecha")
                });
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("DevolucionDAO.listarDevolucionesDesc() falló", ex);
        }
        return lista;
    }
}
