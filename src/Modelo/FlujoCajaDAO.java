package Modelo;

import Clases.FlujoCaja;
import Utils.LoggerGlobal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad FlujoCaja.
 *
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `double` → `BigDecimal` en monto.
 *   - `e.printStackTrace()` → `LoggerGlobal.error()`.
 *   - `SELECT *` → columnas explícitas.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §2.A, §3.C, §4.1)
 */
public class FlujoCajaDAO {

    public List<FlujoCaja> listarTodos() {
        List<FlujoCaja> lista = new ArrayList<>();
        String sql = "SELECT idFlujo, fecha, tipo, concepto, monto " +
                     "FROM tb_flujo_caja ORDER BY idFlujo ASC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                FlujoCaja f = new FlujoCaja();
                f.setIdFlujo(rs.getInt("idFlujo"));
                f.setFecha(rs.getString("fecha"));
                f.setTipo(rs.getString("tipo"));
                f.setConcepto(rs.getString("concepto"));
                f.setMonto(rs.getBigDecimal("monto"));   // CORREGIDO: era getDouble
                lista.add(f);
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("FlujoCajaDAO.listarTodos() falló", ex);
        }
        return lista;
    }

    public boolean guardar(FlujoCaja f) {
        String sql = "INSERT INTO tb_flujo_caja (fecha, tipo, concepto, monto) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, f.getFecha());
            ps.setString(2, f.getTipo());
            ps.setString(3, f.getConcepto());
            ps.setBigDecimal(4, f.getMonto());  // CORREGIDO: era setDouble

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("FlujoCajaDAO.guardar() falló", ex);
            return false;
        }
    }
}
