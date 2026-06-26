package Modelo;

import Utils.ConfigManager;
import Utils.LoggerGlobal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestor de conexión a la base de datos MySQL.
 *
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00):
 *   - SEGURIDAD: Credenciales ya NO están hardcodeadas. Se leen desde
 *     config.properties via ConfigManager (OWASP - Datos sensibles en código).
 *   - CONCURRENCIA: Método getConexion() es ahora `synchronized` para
 *     evitar race conditions cuando múltiples hilos (timer de inactividad +
 *     eventos de UI) intentan obtener la conexión simultáneamente.
 *     (Designing Data-Intensive Applications — Concurrencia segura)
 *   - OBSERVABILIDAD: Errores ahora se registran en LoggerGlobal en lugar
 *     de e.printStackTrace() que desaparece en producción (.jar).
 */
public class ConexionDB {

    private static final String URL  = ConfigManager.get("db.url");
    private static final String USER = ConfigManager.get("db.user");
    private static final String PASS = ConfigManager.get("db.password");

    private static Connection conn = null;

    /**
     * Retorna una conexión activa a la base de datos.
     * Es synchronized para evitar condiciones de carrera en entornos multi-hilo.
     * @throws SQLException si no se puede establecer la conexión.
     */
    public static synchronized Connection getConexion() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
            } catch (ClassNotFoundException ex) {
                String msg = "Driver de MySQL no encontrado. Verifique que mysql-connector-java esté en /librerias";
                LoggerGlobal.error(msg, ex);
                throw new SQLException(msg, ex);
            } catch (SQLException ex) {
                LoggerGlobal.error("Error al conectar a la BD en: " + URL, ex);
                throw ex;
            }
        }
        return conn;
    }

    /**
     * Cierra la conexión activa con la base de datos.
     */
    public static synchronized void cerrar() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("Error al cerrar la conexión con la BD", ex);
        }
    }
}
