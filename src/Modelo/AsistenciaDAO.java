package Modelo;

import Clases.Asistencia;
import Utils.LoggerGlobal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDAO {

    public List<Asistencia> listarPorEmpleadoYMes(String idEmpleado, int mes, int anio) {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT id, idEmpleado, fecha, horaEntrada, horaSalida, horasTrabajadas, observacion " +
                     "FROM asistencia WHERE idEmpleado = ? AND MONTH(fecha) = ? AND YEAR(fecha) = ? ORDER BY fecha ASC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idEmpleado);
            ps.setInt(2, mes);
            ps.setInt(3, anio);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Asistencia a = new Asistencia(
                        rs.getString("id"), rs.getString("idEmpleado"), rs.getDate("fecha"),
                        rs.getTime("horaEntrada"), rs.getTime("horaSalida"),
                        rs.getBigDecimal("horasTrabajadas"), rs.getString("observacion")
                    );
                    lista.add(a);
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("AsistenciaDAO.listarPorEmpleadoYMes() fallo", ex);
        }
        return lista;
    }
    
    // For demo purposes, we can add a method to register attendance
    public boolean registrar(Asistencia a) {
        String sql = "INSERT INTO asistencia (id, idEmpleado, fecha, horaEntrada, horaSalida, horasTrabajadas, observacion) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, a.getId());
            ps.setString(2, a.getIdEmpleado());
            ps.setDate(3, new java.sql.Date(a.getFecha().getTime()));
            ps.setTime(4, a.getHoraEntrada());
            ps.setTime(5, a.getHoraSalida());
            ps.setBigDecimal(6, a.getHorasTrabajadas());
            ps.setString(7, a.getObservacion());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("AsistenciaDAO.registrar() fallo", ex);
            return false;
        }
    }
}
