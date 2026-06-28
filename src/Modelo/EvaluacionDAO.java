package Modelo;

import Clases.Evaluacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Evaluacion de Desempeño (FR-038)
 */
public class EvaluacionDAO {

    public EvaluacionDAO() {
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS tb_evaluaciones (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "idEmpleado VARCHAR(20), " +
                     "puntaje INT, " +
                     "comentarios VARCHAR(255), " +
                     "fecha VARCHAR(50))";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (Exception e) {
            Utils.LoggerGlobal.error("Error creando tb_evaluaciones", e);
        }
    }

    public boolean registrar(Evaluacion e) {
        String sql = "INSERT INTO tb_evaluaciones (idEmpleado, puntaje, comentarios, fecha) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getIdEmpleado());
            ps.setInt(2, e.getPuntaje());
            ps.setString(3, e.getComentarios());
            ps.setString(4, e.getFecha());
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            Utils.LoggerGlobal.error("Error registrando evaluación", ex);
            return false;
        }
    }

    public List<Evaluacion> listarPorEmpleado(String idEmpleado) {
        List<Evaluacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_evaluaciones WHERE idEmpleado = ? ORDER BY id DESC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Evaluacion(
                        rs.getInt("id"),
                        rs.getString("idEmpleado"),
                        rs.getInt("puntaje"),
                        rs.getString("comentarios"),
                        rs.getString("fecha")
                    ));
                }
            }
        } catch (Exception ex) {
            Utils.LoggerGlobal.error("Error consultando evaluaciones", ex);
        }
        return lista;
    }
}
