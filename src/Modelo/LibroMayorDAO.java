package Modelo;

import Clases.LibroMayor;
import Utils.LoggerGlobal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad LibroMayor (asientos contables).
 *
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `double` → `BigDecimal` en montoDebe y montoHaber.
 *   - `e.printStackTrace()` → `LoggerGlobal.error()`.
 *   - `SELECT *` → columnas explícitas.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §2.A, §3.C, §4.1)
 */
public class LibroMayorDAO {

    /**
     * Lista todos los asientos contables ordenados cronológicamente.
     */
    public List<LibroMayor> listarTodos() {
        List<LibroMayor> lista = new ArrayList<>();
        String sql = "SELECT idAsiento, fecha, glosa, cuentaDebe, cuentaHaber, " +
                     "montoDebe, montoHaber, nroAsiento " +
                     "FROM tb_libro_mayor ORDER BY idAsiento ASC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LibroMayor lm = new LibroMayor();
                lm.setIdAsiento(rs.getInt("idAsiento"));
                lm.setFecha(rs.getString("fecha"));
                lm.setGlosa(rs.getString("glosa"));
                lm.setCuentaDebe(rs.getString("cuentaDebe"));
                lm.setCuentaHaber(rs.getString("cuentaHaber"));
                lm.setMontoDebe(rs.getBigDecimal("montoDebe"));   // CORREGIDO: era getDouble
                lm.setMontoHaber(rs.getBigDecimal("montoHaber")); // CORREGIDO: era getDouble
                lm.setNroAsiento(rs.getString("nroAsiento"));
                lista.add(lm);
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("LibroMayorDAO.listarTodos() falló", ex);
        }
        return lista;
    }

    /**
     * Guarda un nuevo asiento contable en el Libro Mayor.
     */
    public boolean guardar(LibroMayor lm) {
        String sql = "INSERT INTO tb_libro_mayor " +
                     "(fecha, glosa, cuentaDebe, cuentaHaber, montoDebe, montoHaber, nroAsiento) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, lm.getFecha());
            ps.setString(2, lm.getGlosa());
            ps.setString(3, lm.getCuentaDebe());
            ps.setString(4, lm.getCuentaHaber());
            ps.setBigDecimal(5, lm.getMontoDebe());   // CORREGIDO: era setDouble
            ps.setBigDecimal(6, lm.getMontoHaber());  // CORREGIDO: era setDouble
            ps.setString(7, lm.getNroAsiento());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("LibroMayorDAO.guardar() falló", ex);
            return false;
        }
    }
}
