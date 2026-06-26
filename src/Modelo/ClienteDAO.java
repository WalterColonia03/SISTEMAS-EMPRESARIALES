package Modelo;

import Clases.Cliente;
import Utils.LoggerGlobal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Cliente.
 *
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `e.printStackTrace()` → `LoggerGlobal.error()` (observabilidad en .jar).
 *   - `SELECT *` → columnas explícitas (incluye la columna `puntos` de forma explícita).
 *   - Eliminada la cláusula try/catch interna para `puntos` que enmascaraba errores.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §2.A, §3.C)
 */
public class ClienteDAO {

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT idCliente, nombre, apellido, dni, telefono, direccion, estado, puntos " +
                     "FROM tb_cliente";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getInt("estado")
                );
                c.setPuntos(rs.getInt("puntos")); // Columna explícita — ya no necesita try/catch propio
                lista.add(c);
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ClienteDAO.listarTodos() falló", ex);
        }
        return lista;
    }

    public boolean actualizar(Cliente c) {
        String sql = "UPDATE tb_cliente SET nombre=?, apellido=?, dni=?, telefono=?, " +
                     "direccion=?, estado=?, puntos=? WHERE idCliente=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getDni());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getDireccion());
            ps.setInt(6, c.getEstado());
            ps.setInt(7, c.getPuntos());
            ps.setInt(8, c.getIdCliente());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("ClienteDAO.actualizar() falló para id=" + c.getIdCliente(), ex);
            return false;
        }
    }

    public boolean guardar(Cliente c) {
        String sql = "INSERT INTO tb_cliente (idCliente, nombre, apellido, dni, telefono, direccion, estado, puntos) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getIdCliente());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getApellido());
            ps.setString(4, c.getDni());
            ps.setString(5, c.getTelefono());
            ps.setString(6, c.getDireccion());
            ps.setInt(7, c.getEstado());
            ps.setInt(8, c.getPuntos());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("ClienteDAO.guardar() falló para DNI=" + c.getDni(), ex);
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM tb_cliente WHERE idCliente=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("ClienteDAO.eliminar() falló para id=" + id, ex);
            return false;
        }
    }

    public int generarId() {
        String sql = "SELECT COALESCE(MAX(idCliente), 0) + 1 FROM tb_cliente";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            LoggerGlobal.error("ClienteDAO.generarId() falló", ex);
        }
        return 1;
    }

    /**
     * Actualiza los puntos de fidelidad de un cliente dado su DNI.
     * Usa un delta (positivo = suma, negativo = descuenta).
     * El "Consumidor Final" (DNI=00000000) no acumula puntos.
     *
     * @param dni         DNI del cliente.
     * @param deltaPuntos Puntos a sumar (positivo) o restar (negativo).
     * @return true si se actualizó correctamente.
     */
    public boolean actualizarPuntosPorDni(String dni, int deltaPuntos) {
        if (dni == null || dni.isEmpty() || "00000000".equals(dni)) {
            return false; // Consumidor Final no acumula puntos
        }
        String sql = "UPDATE tb_cliente SET puntos = puntos + ? WHERE dni = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deltaPuntos);
            ps.setString(2, dni);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("ClienteDAO.actualizarPuntosPorDni() falló para DNI=" + dni, ex);
            return false;
        }
    }
}
