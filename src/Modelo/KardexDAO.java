package Modelo;

import Clases.Kardex;
import Utils.LoggerGlobal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Kardex (movimientos de inventario).
 *
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `e.printStackTrace()` → `LoggerGlobal.error()`.
 *   - `SELECT *` → columnas explícitas.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §2.A, §3.C)
 */
public class KardexDAO {

    public List<Kardex> listarPorProducto(int idProducto) {
        List<Kardex> lista = new ArrayList<>();
        String sql = "SELECT idMovimiento, idProducto, tipoMovimiento, cantidad, fecha, motivo " +
                     "FROM tb_kardex WHERE idProducto = ? ORDER BY idMovimiento ASC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Kardex k = new Kardex();
                    k.setIdMovimiento(rs.getInt("idMovimiento"));
                    k.setIdProducto(rs.getInt("idProducto"));
                    k.setTipoMovimiento(rs.getString("tipoMovimiento"));
                    k.setCantidad(rs.getInt("cantidad"));
                    k.setFecha(rs.getString("fecha"));
                    k.setMotivo(rs.getString("motivo"));
                    lista.add(k);
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("KardexDAO.listarPorProducto() falló para idProducto=" + idProducto, ex);
        }
        return lista;
    }

    public List<Object[]> listarTodosDesc() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT k.idMovimiento, p.nombre AS producto, k.tipoMovimiento, k.cantidad, k.fecha, k.motivo " +
                     "FROM tb_kardex k " +
                     "INNER JOIN tb_producto p ON k.idProducto = p.idProducto " +
                     "ORDER BY k.idMovimiento DESC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("fecha"),
                    rs.getString("producto"),
                    rs.getString("tipoMovimiento"),
                    rs.getInt("cantidad"),
                    "MOV-" + rs.getInt("idMovimiento"),
                    rs.getString("motivo")
                });
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("KardexDAO.listarTodosDesc() falló", ex);
        }
        return lista;
    }

    /**
     * Lista todos los movimientos de productos que pertenecen a una categoría específica.
     * Capa: DAO — Implementa: Filtro por categoría en Kardex.
     */
    public List<Object[]> listarPorCategoriaDesc(int idCategoria) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT k.idMovimiento, p.nombre AS producto, k.tipoMovimiento, k.cantidad, k.fecha, k.motivo " +
                     "FROM tb_kardex k " +
                     "INNER JOIN tb_producto p ON k.idProducto = p.idProducto " +
                     "WHERE p.idCategoria = ? " +
                     "ORDER BY k.idMovimiento DESC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                        rs.getString("fecha"),
                        rs.getString("producto"),
                        rs.getString("tipoMovimiento"),
                        rs.getInt("cantidad"),
                        "MOV-" + rs.getInt("idMovimiento"),
                        rs.getString("motivo")
                    });
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("KardexDAO.listarPorCategoriaDesc() falló para idCategoria=" + idCategoria, ex);
        }
        return lista;
    }

    /**
     * Registra un nuevo movimiento de Kardex (ENTRADA o SALIDA) en la BD.
     * Implementa: FR-004 — registro de movimientos de inventario.
     */
    public boolean guardar(Kardex k) {
        String sql = "INSERT INTO tb_kardex (idProducto, tipoMovimiento, cantidad, fecha, motivo) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, k.getIdProducto());
            ps.setString(2, k.getTipoMovimiento());
            ps.setInt(3, k.getCantidad());
            ps.setString(4, k.getFecha());
            ps.setString(5, k.getMotivo());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LoggerGlobal.error("KardexDAO.guardar() falló para idProducto=" + k.getIdProducto(), ex);
            return false;
        }
    }
}
