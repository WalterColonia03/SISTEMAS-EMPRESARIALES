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
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `e.printStackTrace()` → `LoggerGlobal.error()` (observabilidad).
 *   - `SELECT *` → columnas explícitas (Clean Code).
 *   - Las contraseñas se almacenan como hash SHA-256 via PasswordUtils.
 *     La verificación de login se hace con PasswordUtils.verifyPassword().
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §3.D — OWASP A02)
 */
public class UsuarioDAO {

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT idUsuario, nombre, apellido, usuario, password, telefono, rol, estado " +
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
                lista.add(u);
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("UsuarioDAO.listarTodos() falló", ex);
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
            LoggerGlobal.error("UsuarioDAO.actualizar() falló para id=" + u.getIdUsuario(), ex);
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
            LoggerGlobal.error("UsuarioDAO.guardar() falló para usuario=" + u.getUsuario(), ex);
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
            LoggerGlobal.error("UsuarioDAO.eliminar() falló para id=" + id, ex);
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
            LoggerGlobal.error("UsuarioDAO.generarId() falló", ex);
        }
        return 1;
    }
}
