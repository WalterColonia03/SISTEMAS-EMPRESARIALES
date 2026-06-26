package Modelo;

import Clases.Usuario;
import Utils.LoggerGlobal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Usuario.
 *
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00 Ã¢â‚¬â€ AuditorÃƒÂ­a ERP):
 *   - `e.printStackTrace()` Ã¢â€ â€™ `LoggerGlobal.error()` (observabilidad).
 *   - `SELECT *` Ã¢â€ â€™ columnas explÃƒÂ­citas (Clean Code).
 *   - Las contraseÃƒÂ±as se almacenan como hash SHA-256 via PasswordUtils.
 *     La verificaciÃƒÂ³n de login se hace con PasswordUtils.verifyPassword().
 *     (INSTRUCCIONES_IA_PROYECTO_ERP Ã‚Â§3.D Ã¢â‚¬â€ OWASP A02)
 */
public class UsuarioDAO {

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT idUsuario, nombre, apellido, usuario, password, telefono, rol, estado, sesion_activa " +
                     "FROM tb_usuario";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("idUsuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("usuario"),
                        rs.getString("password"),  // El valor almacenado es hash SHA-256
                        rs.getString("telefono"),
                        rs.getString("rol"),
                        rs.getInt("estado")
                );
                u.setSesionActiva(rs.getInt("sesion_activa"));
                lista.add(u);
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("UsuarioDAO.listarTodos() fallÃƒÂ³", ex);
        }
        return lista;
    }

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE tb_usuario SET nombre=?, apellido=?, usuario=?, telefono=?, " +
                     "password=?, rol=?, estado=? WHERE idUsuario=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getUsuario());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getPassword());  // Debe venir ya hasheado desde la Vista
            ps.setString(6, u.getRol());
            ps.setInt(7, u.getEstado());
            ps.setInt(8, u.getIdUsuario());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("UsuarioDAO.actualizar() fallÃƒÂ³ para id=" + u.getIdUsuario(), ex);
            return false;
        }
    }

    public boolean guardar(Usuario u) {
        String sql = "INSERT INTO tb_usuario (idUsuario, nombre, apellido, usuario, telefono, password, rol, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, u.getIdUsuario());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getApellido());
            ps.setString(4, u.getUsuario());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getPassword());  // Debe venir ya hasheado desde la Vista
            ps.setString(7, u.getRol());
            ps.setInt(8, u.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("UsuarioDAO.guardar() fallÃƒÂ³ para usuario=" + u.getUsuario(), ex);
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM tb_usuario WHERE idUsuario=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("UsuarioDAO.eliminar() fallÃƒÂ³ para id=" + id, ex);
            return false;
        }
    }

    public int generarId() {
        String sql = "SELECT COALESCE(MAX(idUsuario), 0) + 1 FROM tb_usuario";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            LoggerGlobal.error("UsuarioDAO.generarId() fallÃƒÂ³", ex);
        }
        return 1;
    }

    public void cambiarEstadoSesion(String usuarioStr, int activa) {
        String sql = "UPDATE tb_usuario SET sesion_activa = ? WHERE usuario = ?";
        try (java.sql.Connection conn = ConexionDB.getConexion();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, activa);
            ps.setString(2, usuarioStr);
            ps.executeUpdate();
        } catch (Exception e) {
            Utils.LoggerGlobal.error("Error al actualizar sesion_activa", e);
        }
    }
}