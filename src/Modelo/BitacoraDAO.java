package Modelo;

import Clases.Bitacora;
import Utils.LoggerGlobal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Bitacora — solo INSERT y SELECT (sin UPDATE/DELETE).
 *
 * Historia de Usuario: FR-020-v2 · CA-1 a CA-6
 * Criterio CA-5: el usuario de la aplicación solo tiene privilegios INSERT/SELECT
 * sobre tb_bitacora. Documentado en manual de BD.
 *
 * Creado: 2026-06-26T02:06:00-05:00
 */
public class BitacoraDAO {

    /**
     * Registra un evento en la bitácora de auditoría.
     * Operación INSERT-only — la tabla es append-only por diseño.
     */
    public boolean registrar(Bitacora b) {
        String sql = "INSERT INTO tb_bitacora " +
                     "(usuario, modulo, accion, resultado, detalle, timestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getUsuario()   != null ? b.getUsuario()   : "SISTEMA");
            ps.setString(2, b.getModulo()    != null ? b.getModulo()    : "DESCONOCIDO");
            ps.setString(3, b.getAccion()    != null ? b.getAccion()    : "ACCION");
            ps.setString(4, b.getResultado() != null ? b.getResultado() : "DESCONOCIDO");
            ps.setString(5, b.getDetalle());
            ps.setString(6, b.getTimestamp());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            LoggerGlobal.error("BitacoraDAO.registrar() falló para usuario=" + b.getUsuario(), ex);
            return false;
        }
    }

    /** Lista todos los registros de bitácora, más reciente primero. */
    public List<Bitacora> listarTodos() {
        List<Bitacora> lista = new ArrayList<>();
        String sql = "SELECT idBitacora, usuario, modulo, accion, resultado, detalle, timestamp " +
                     "FROM tb_bitacora ORDER BY idBitacora DESC LIMIT 1000";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bitacora b = mapRow(rs);
                lista.add(b);
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("BitacoraDAO.listarTodos() falló", ex);
        }
        return lista;
    }

    /** Filtra por usuario (CA-3). */
    public List<Bitacora> listarPorUsuario(String usuario) {
        List<Bitacora> lista = new ArrayList<>();
        String sql = "SELECT idBitacora, usuario, modulo, accion, resultado, detalle, timestamp " +
                     "FROM tb_bitacora WHERE usuario LIKE ? ORDER BY idBitacora DESC LIMIT 500";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + usuario + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("BitacoraDAO.listarPorUsuario() falló para=" + usuario, ex);
        }
        return lista;
    }

    /** Filtra por módulo y/o resultado. Null = sin filtro. */
    public List<Bitacora> listarPorFiltros(String modulo, String resultado) {
        List<Bitacora> lista = new ArrayList<>();
        // Construcción dinámica de WHERE para evitar NULLIF() incompatible con MySQL 5.x
        StringBuilder where = new StringBuilder("WHERE 1=1");
        if (modulo   != null && !modulo.isEmpty())    where.append(" AND modulo = ?");
        if (resultado != null && !resultado.isEmpty()) where.append(" AND resultado = ?");

        String sql = "SELECT idBitacora, usuario, modulo, accion, resultado, detalle, timestamp " +
                     "FROM tb_bitacora " + where + " ORDER BY idBitacora DESC LIMIT 500";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            if (modulo    != null && !modulo.isEmpty())    ps.setString(idx++, modulo);
            if (resultado != null && !resultado.isEmpty()) ps.setString(idx,   resultado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("BitacoraDAO.listarPorFiltros() falló", ex);
        }
        return lista;
    }

    /** Mapea un ResultSet a Bitacora (evita duplicación — DRY). */
    private Bitacora mapRow(ResultSet rs) throws SQLException {
        Bitacora b = new Bitacora(
                rs.getString("usuario"),
                rs.getString("modulo"),
                rs.getString("accion"),
                rs.getString("resultado"),
                rs.getString("detalle"),
                rs.getString("timestamp")
        );
        b.setIdBitacora(rs.getInt("idBitacora"));
        return b;
    }
}
