package Modelo;

import Clases.CuentasCP;
import Utils.LoggerGlobal;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad CuentasCP (Cuentas por Cobrar y Pagar).
 *
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `double` → `BigDecimal` en montoOriginal y montoPendiente.
 *   - `e.printStackTrace()` → `LoggerGlobal.error()`.
 *   - `SELECT *` → columnas explícitas.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §2.A, §3.C, §4.1)
 */
public class CuentasCPDAO {

    public List<CuentasCP> listarPorTipo(String tipo) {
        List<CuentasCP> lista = new ArrayList<>();
        String sql = "SELECT idCuenta, tipo, clienteProveedor, documento, montoOriginal, " +
                     "montoPendiente, fechaEmision, fechaVencimiento, estado " +
                     "FROM tb_cuentas_cp WHERE tipo = ? ORDER BY fechaVencimiento ASC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CuentasCP c = new CuentasCP();
                    c.setIdCuenta(rs.getInt("idCuenta"));
                    c.setTipo(rs.getString("tipo"));
                    c.setClienteProveedor(rs.getString("clienteProveedor"));
                    c.setDocumento(rs.getString("documento"));
                    c.setMontoOriginal(rs.getBigDecimal("montoOriginal"));   // CORREGIDO
                    c.setMontoPendiente(rs.getBigDecimal("montoPendiente")); // CORREGIDO
                    c.setFechaEmision(rs.getString("fechaEmision"));
                    c.setFechaVencimiento(rs.getString("fechaVencimiento"));
                    c.setEstado(rs.getString("estado"));
                    lista.add(c);
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("CuentasCPDAO.listarPorTipo() falló para tipo=" + tipo, ex);
        }
        return lista;
    }

    public boolean guardar(CuentasCP c) {
        String sql = "INSERT INTO tb_cuentas_cp " +
                     "(tipo, clienteProveedor, documento, montoOriginal, montoPendiente, " +
                     "fechaEmision, fechaVencimiento, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getTipo());
            ps.setString(2, c.getClienteProveedor());
            ps.setString(3, c.getDocumento());
            ps.setBigDecimal(4, c.getMontoOriginal());   // CORREGIDO
            ps.setBigDecimal(5, c.getMontoPendiente());  // CORREGIDO
            ps.setString(6, c.getFechaEmision());
            ps.setString(7, c.getFechaVencimiento());
            ps.setString(8, c.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("CuentasCPDAO.guardar() falló", ex);
            return false;
        }
    }

    /**
     * Registra un abono parcial o total sobre una cuenta pendiente.
     * Usa una expresión SQL para actualizar el estado atómicamente.
     * @param idCuenta ID de la cuenta a abonar.
     * @param abono    Monto del abono (BigDecimal para precisión exacta).
     */
    public boolean abonar(int idCuenta, BigDecimal abono) {
        // Actualiza el saldo y cambia el estado a PAGADA si el saldo llega a 0
        String sql = "UPDATE tb_cuentas_cp " +
                     "SET montoPendiente = montoPendiente - ?, " +
                     "    estado = IF(montoPendiente - ? <= 0, 'PAGADA', estado) " +
                     "WHERE idCuenta = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, abono);  // CORREGIDO
            ps.setBigDecimal(2, abono);  // CORREGIDO
            ps.setInt(3, idCuenta);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("CuentasCPDAO.abonar() falló para idCuenta=" + idCuenta, ex);
            return false;
        }
    }
}
