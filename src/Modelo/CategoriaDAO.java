package Modelo;

import Clases.Categoria;
import Utils.LoggerGlobal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Categoria (clasificación de productos).
 *
 * CORRECCIONES APLICADAS (2026-06-26T01:31:00-05:00 — Auditoría ERP):
 *   - `e.printStackTrace()` → `LoggerGlobal.error()`.
 *   - `SELECT *` → columnas explícitas (idCategoria, descripcion, estado).
 *   - `MAX(id) + 1` → `COALESCE(MAX(id), 0) + 1` para evitar NPE con tabla vacía.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §2.A, §3.C)
 */
public class CategoriaDAO {

    public List<Categoria> listarTodos() {
        List<Categoria> lista = new ArrayList<>();
        // CORRECCIÓN: columnas explícitas en lugar de SELECT *
        String sql = "SELECT idCategoria, descripcion, estado FROM tb_categoria";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Categoria(
                        rs.getInt("idCategoria"),
                        rs.getString("descripcion"),
                        rs.getInt("estado")
                ));
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("CategoriaDAO.listarTodos() falló", ex); // CORRECCIÓN
        }
        return lista;
    }

    public boolean actualizar(Categoria c) {
        String sql = "UPDATE tb_categoria SET descripcion=?, estado=? WHERE idCategoria=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getDescripcion());
            ps.setInt(2, c.getEstado());
            ps.setInt(3, c.getIdCategoria());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("CategoriaDAO.actualizar() falló para id=" + c.getIdCategoria(), ex); // CORRECCIÓN
            return false;
        }
    }

    public boolean guardar(Categoria c) {
        String sql = "INSERT INTO tb_categoria (idCategoria, descripcion, estado) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getIdCategoria());
            ps.setString(2, c.getDescripcion());
            ps.setInt(3, c.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("CategoriaDAO.guardar() falló para desc=" + c.getDescripcion(), ex); // CORRECCIÓN
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM tb_categoria WHERE idCategoria=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("CategoriaDAO.eliminar() falló para id=" + id, ex); // CORRECCIÓN
            return false;
        }
    }

    public int generarId() {
        // CORRECCIÓN: COALESCE evita NullPointerException cuando la tabla está vacía
        String sql = "SELECT COALESCE(MAX(idCategoria), 0) + 1 FROM tb_categoria";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            LoggerGlobal.error("CategoriaDAO.generarId() falló", ex); // CORRECCIÓN
        }
        return 1;
    }
}
