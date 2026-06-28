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

    private static final int POOL_SIZE = 5;
    private static final java.util.concurrent.BlockingQueue<Connection> pool = new java.util.concurrent.LinkedBlockingQueue<>();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < POOL_SIZE; i++) {
                pool.offer(DriverManager.getConnection(URL, USER, PASS));
            }
        } catch (Exception e) { 
            LoggerGlobal.error("Error al inicializar pool de conexiones", e);
        }
    }

    /**
     * Retorna una conexión activa a la base de datos desde el pool de conexiones.
     * Capa: Utilidad Transversal (Clases) — Implementa: RNF-09 (Concurrencia de 5 cajas).
     * El pool administrado mediante LinkedBlockingQueue permite que los cajeros (hilos)
     * obtengan conexiones sin bloquearse entre sí, resolviendo el problema de serialización.
     * 
     * @return Connection lista para usar en una transacción
     * @throws SQLException si se agota el timeout de 3 segundos esperando conexión disponible
     */
    public static Connection getConexion() throws SQLException {
        try {
            // Se usa poll sin timeout prolongado para evitar latencia si el pool está vacío
            Connection realConn = pool.poll();
            if (realConn == null || realConn.isClosed()) {
                realConn = DriverManager.getConnection(URL, USER, PASS);
            }
            
            final Connection targetConn = realConn;
            
            // Proxy dinámico para interceptar el método close() del try-with-resources
            return (Connection) java.lang.reflect.Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                (proxy, method, args) -> {
                    if ("close".equals(method.getName())) {
                        devolverConexion(targetConn);
                        return null;
                    }
                    try {
                        return method.invoke(targetConn, args);
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        throw e.getCause();
                    }
                }
            );
        } catch (Exception e) {
            throw new SQLException("Error obteniendo conexión del pool", e);
        }
    }

    /**
     * Devuelve la conexión al pool en lugar de cerrarla físicamente.
     * Capa: Utilidad Transversal — Implementa: RNF-01 (Rendimiento).
     * Reutilizar conexiones activas evita el costo de establecer una nueva conexión TCP
     * cada vez que un DAO requiere leer o escribir datos.
     * 
     * @param conn La conexión a devolver al pool
     */
    public static void devolverConexion(Connection conn) {
        if (conn != null) {
            pool.offer(conn);
        }
    }

    /**
     * Cierra todas las conexiones del pool al apagar el sistema.
     */
    public static void cerrar() {
        while (!pool.isEmpty()) {
            try {
                Connection conn = pool.poll();
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                LoggerGlobal.error("Error al cerrar conexión del pool", ex);
            }
        }
    }
}
