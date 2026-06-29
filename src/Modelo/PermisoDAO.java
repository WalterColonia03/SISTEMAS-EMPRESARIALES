package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO para leer/escribir permisos por rol (FR-042).
 * Refactorizado para usar MySQL (tb_permisos).
 */
public class PermisoDAO {
    
    public PermisoDAO() {
        // Constructor vacío, la lógica ahora va directo a BD
    }

    public boolean tienePermiso(String rol, String modulo) {
        if (rol == null) return false;
        if (rol.equalsIgnoreCase("ADMINISTRADOR")) return true; // El admin siempre tiene acceso a todo
        
        String modulosStr = getPermisos(rol);
        for (String m : modulosStr.split(",")) {
            if (m.trim().equalsIgnoreCase(modulo)) {
                return true;
            }
        }
        return false;
    }

    public void setPermisos(String rol, String modulosSeparadosPorComa) {
        if (rol == null) return;
        String sql = "INSERT INTO tb_permisos (rol, modulos) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE modulos=VALUES(modulos)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol.toUpperCase());
            ps.setString(2, modulosSeparadosPorComa);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Utils.LoggerGlobal.error("Error guardando permisos en BD para rol " + rol, ex);
        }
    }
    
    public String getPermisos(String rol) {
        if (rol == null) return "";
        String sql = "SELECT modulos FROM tb_permisos WHERE rol = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("modulos");
                }
            }
        } catch (SQLException ex) {
            Utils.LoggerGlobal.error("Error obteniendo permisos de BD para rol " + rol, ex);
        }
        return "";
    }
}
