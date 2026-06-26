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
}
