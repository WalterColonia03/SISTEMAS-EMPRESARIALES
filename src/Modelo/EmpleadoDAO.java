package Modelo;

import Clases.Empleado;
import Utils.LoggerGlobal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    
    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT codigo, dni, nombres, apellidos, cargo, fechaIngreso, sueldoBase, turno, telefono, email, activo FROM empleados";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Empleado e = new Empleado(
                    rs.getString("codigo"), rs.getString("dni"), rs.getString("nombres"), rs.getString("apellidos"),
                    rs.getString("cargo"), rs.getDate("fechaIngreso"), rs.getBigDecimal("sueldoBase"),
                    rs.getString("turno"), rs.getString("telefono"), rs.getString("email"), rs.getBoolean("activo")
                );
                lista.add(e);
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("EmpleadoDAO.listarTodos() fallo", ex);
        }
        return lista;
    }
    
    public boolean guardar(Empleado e) {
        String sql = "INSERT INTO empleados (codigo, dni, nombres, apellidos, cargo, fechaIngreso, sueldoBase, turno, telefono, email, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, e.getCodigo());
            ps.setString(2, e.getDni());
            ps.setString(3, e.getNombres());
            ps.setString(4, e.getApellidos());
            ps.setString(5, e.getCargo());
            ps.setDate(6, new java.sql.Date(e.getFechaIngreso().getTime()));
            ps.setBigDecimal(7, e.getSueldoBase());
            ps.setString(8, e.getTurno());
            ps.setString(9, e.getTelefono());
            ps.setString(10, e.getEmail());
            ps.setBoolean(11, e.isActivo());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("EmpleadoDAO.guardar() fallo", ex);
            return false;
        }
    }
    
    public boolean actualizar(Empleado e) {
        String sql = "UPDATE empleados SET dni=?, nombres=?, apellidos=?, cargo=?, fechaIngreso=?, sueldoBase=?, turno=?, telefono=?, email=?, activo=? WHERE codigo=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, e.getDni());
            ps.setString(2, e.getNombres());
            ps.setString(3, e.getApellidos());
            ps.setString(4, e.getCargo());
            ps.setDate(5, new java.sql.Date(e.getFechaIngreso().getTime()));
            ps.setBigDecimal(6, e.getSueldoBase());
            ps.setString(7, e.getTurno());
            ps.setString(8, e.getTelefono());
            ps.setString(9, e.getEmail());
            ps.setBoolean(10, e.isActivo());
            ps.setString(11, e.getCodigo());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("EmpleadoDAO.actualizar() fallo", ex);
            return false;
        }
    }
    
    public String generarCodigo() {
        String sql = "SELECT MAX(CAST(SUBSTRING(codigo, 4) AS UNSIGNED)) FROM empleados";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int max = rs.getInt(1);
                return "EMP" + String.format("%03d", max + 1);
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("EmpleadoDAO.generarCodigo() fallo", ex);
        }
        return "EMP001";
    }
}
