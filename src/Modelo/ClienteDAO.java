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
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00 â€” AuditorÃ­a ERP):
 *   - `e.printStackTrace()` â†’ `LoggerGlobal.error()` (observabilidad en .jar).
 *   - `SELECT *` â†’ columnas explÃ­citas (incluye la columna `puntos` de forma explÃ­cita).
 *   - Eliminada la clÃ¡usula try/catch interna para `puntos` que enmascaraba errores.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP Â§2.A, Â§3.C)
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
                c.setPuntos(rs.getInt("puntos")); // Columna explÃ­cita â€” ya no necesita try/catch propio
                lista.add(c);
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ClienteDAO.listarTodos() fallÃ³", ex);
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
            LoggerGlobal.error("ClienteDAO.actualizar() fallÃ³ para id=" + c.getIdCliente(), ex);
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
            LoggerGlobal.error("ClienteDAO.guardar() fallÃ³ para DNI=" + c.getDni(), ex);
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
            LoggerGlobal.error("ClienteDAO.eliminar() fallÃ³ para id=" + id, ex);
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
            LoggerGlobal.error("ClienteDAO.generarId() fallÃ³", ex);
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
     * @return true si se actualizÃ³ correctamente.
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
            LoggerGlobal.error("ClienteDAO.actualizarPuntosPorDni() fallÃ³ para DNI=" + dni, ex);
            return false;
        }
    }

    // â”€â”€ FR-016: bÃºsqueda exacta por DNI â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    public Cliente buscarPorDni(String dni) {
        String sql = "SELECT idCliente,nombre,apellido,dni,telefono,direccion,estado,puntos FROM tb_cliente WHERE dni=?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente(rs.getInt("idCliente"), rs.getString("nombre"),
                        rs.getString("apellido"), rs.getString("dni"),
                        rs.getString("telefono"), rs.getString("direccion"), rs.getInt("estado"));
                    c.setPuntos(rs.getInt("puntos"));
                    return c;
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ClienteDAO.buscarPorDni() fallo DNI=" + dni, ex);
        }
        return null;
    }

    /** Valida DNI duplicado (excluirId=-1 para nuevo registro) */
    public boolean existeDni(String dni, int excluirId) {
        String sql = "SELECT COUNT(*) FROM tb_cliente WHERE dni=? AND idCliente<>?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.setInt(2, excluirId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ClienteDAO.existeDni() fallo", ex);
        }
        return false;
    }

    /** BÃºsqueda dinÃ¡mica por nombre, apellido o DNI */
    public List<Cliente> buscarPorNombre(String termino) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT idCliente,nombre,apellido,dni,telefono,direccion,estado,puntos " +
                     "FROM tb_cliente WHERE LOWER(nombre) LIKE ? OR LOWER(apellido) LIKE ? OR dni LIKE ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pat = "%" + termino.toLowerCase() + "%";
            ps.setString(1, pat); ps.setString(2, pat); ps.setString(3, pat);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente(rs.getInt("idCliente"), rs.getString("nombre"),
                        rs.getString("apellido"), rs.getString("dni"),
                        rs.getString("telefono"), rs.getString("direccion"), rs.getInt("estado"));
                    c.setPuntos(rs.getInt("puntos"));
                    lista.add(c);
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ClienteDAO.buscarPorNombre() fallo termino=" + termino, ex);
        }
        return lista;
    }

    /**
     * FR-032 â€” Ranking VIP: top N clientes por volumen total de compras.
     * Retorna: {nombre, apellido, dni, totalCompras(BigDecimal), puntos}
     */
    public List<Object[]> rankingVip(int topN) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT c.nombre, c.apellido, c.dni, SUM(v.total) AS totalCompras, c.puntos " +
                     "FROM tb_cliente c JOIN tb_venta v ON v.cliente = c.dni " +
                     "WHERE c.dni <> '00000000' " +
                     "GROUP BY c.idCliente, c.nombre, c.apellido, c.dni, c.puntos " +
                     "ORDER BY totalCompras DESC LIMIT ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, topN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                        rs.getString("nombre"), rs.getString("apellido"),
                        rs.getString("dni"), rs.getBigDecimal("totalCompras"),
                        rs.getInt("puntos")
                    });
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ClienteDAO.rankingVip() fallo", ex);
        }
        return lista;
    }

    /**
     * FR-021 â€” Top N productos mÃ¡s comprados por un cliente.
     * Retorna: {nombreProducto, totalUds(int), totalGastado(BigDecimal)}
     */
    public List<Object[]> topProductosPorCliente(String dniCliente, int topN) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT p.nombre, SUM(dv.cantidad) AS totalUds, " +
                     "       SUM(dv.cantidad * dv.precioUnitario) AS totalGastado " +
                     "FROM tb_detalle_venta dv " +
                     "JOIN tb_venta v    ON v.idVenta    = dv.idVenta " +
                     "JOIN tb_producto p ON p.idProducto = dv.idProducto " +
                     "WHERE v.cliente = ? " +
                     "GROUP BY p.idProducto, p.nombre " +
                     "ORDER BY totalUds DESC LIMIT ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dniCliente);
            ps.setInt(2, topN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                        rs.getString("nombre"),
                        rs.getInt("totalUds"),
                        rs.getBigDecimal("totalGastado")
                    });
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ClienteDAO.topProductosPorCliente() fallo DNI=" + dniCliente, ex);
        }
        return lista;
    }
}
