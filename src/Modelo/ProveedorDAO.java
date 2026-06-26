package Modelo;

import Clases.Proveedor;
import Utils.LoggerGlobal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Proveedor.
 *
 * CORRECCIONES APLICADAS (2026-06-26T01:31:00-05:00 — Auditoría ERP):
 *   - `e.printStackTrace()` → `LoggerGlobal.error()` en todos los bloques catch.
 *   - `SELECT *` → columnas explícitas en listarTodos() y buscarPorRuc().
 *   - Mensajes de log ahora incluyen contexto (ruc, id) para diagnóstico rápido.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §2.A — SRE/Observabilidad)
 */
public class ProveedorDAO {

    public List<Proveedor> listarTodos() {
        List<Proveedor> lista = new ArrayList<>();
        // CORRECCIÓN: columnas explícitas en lugar de SELECT *
        String sql = "SELECT idProveedor, ruc, razonSocial, contacto, telefono, correo, direccion, estado " +
                     "FROM tb_proveedor";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Proveedor(
                        rs.getInt("idProveedor"),
                        rs.getString("ruc"),
                        rs.getString("razonSocial"),
                        rs.getString("contacto"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("direccion"),
                        rs.getInt("estado")
                ));
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ProveedorDAO.listarTodos() falló", ex); // CORRECCIÓN
        }
        return lista;
    }

    public Proveedor buscarPorRuc(String ruc) {
        // CORRECCIÓN: columnas explícitas en lugar de SELECT *
        String sql = "SELECT idProveedor, ruc, razonSocial, contacto, telefono, correo, direccion, estado " +
                     "FROM tb_proveedor WHERE ruc = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ruc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Proveedor(
                            rs.getInt("idProveedor"),
                            rs.getString("ruc"),
                            rs.getString("razonSocial"),
                            rs.getString("contacto"),
                            rs.getString("telefono"),
                            rs.getString("correo"),
                            rs.getString("direccion"),
                            rs.getInt("estado")
                    );
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ProveedorDAO.buscarPorRuc() falló para RUC=" + ruc, ex); // CORRECCIÓN
        }
        return null;
    }

    public boolean guardar(Proveedor p) {
        String sql = "INSERT INTO tb_proveedor " +
                     "(ruc, razonSocial, contacto, telefono, correo, direccion, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getRuc());
            ps.setString(2, p.getRazonSocial());
            ps.setString(3, p.getContacto());
            ps.setString(4, p.getTelefono());
            ps.setString(5, p.getCorreo());
            ps.setString(6, p.getDireccion());
            ps.setInt(7, p.getEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("ProveedorDAO.guardar() falló para RUC=" + p.getRuc(), ex); // CORRECCIÓN
            return false;
        }
    }

    public boolean actualizar(Proveedor p) {
        String sql = "UPDATE tb_proveedor " +
                     "SET ruc=?, razonSocial=?, contacto=?, telefono=?, correo=?, direccion=?, estado=? " +
                     "WHERE idProveedor=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getRuc());
            ps.setString(2, p.getRazonSocial());
            ps.setString(3, p.getContacto());
            ps.setString(4, p.getTelefono());
            ps.setString(5, p.getCorreo());
            ps.setString(6, p.getDireccion());
            ps.setInt(7, p.getEstado());
            ps.setInt(8, p.getIdProveedor());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("ProveedorDAO.actualizar() falló para id=" + p.getIdProveedor(), ex); // CORRECCIÓN
            return false;
        }
    }

    public boolean eliminar(int idProveedor) {
        String sql = "DELETE FROM tb_proveedor WHERE idProveedor=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProveedor);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("ProveedorDAO.eliminar() falló para id=" + idProveedor, ex); // CORRECCIÓN
            return false;
        }
    }

    public int generarId() {
        String sql = "SELECT COALESCE(MAX(idProveedor), 0) + 1 FROM tb_proveedor";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            LoggerGlobal.error("ProveedorDAO.generarId() falló", ex);
        }
        return 1;
    }
}
