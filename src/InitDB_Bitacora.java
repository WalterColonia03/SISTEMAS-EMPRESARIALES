import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Script de creación de la tabla de Bitácora de Auditoría.
 *
 * Historia de Usuario: FR-020-v2 · CA-5
 * Criterio CA-5: el usuario de aplicación debe tener solo INSERT/SELECT
 * sobre esta tabla. Ejecutar manualmente tras correr este script:
 *
 *   REVOKE UPDATE, DELETE ON bd_erp.tb_bitacora FROM 'root'@'localhost';
 *   (En producción, usar un usuario de aplicación con privilegios mínimos)
 *
 * Creado: 2026-06-26T02:06:00-05:00
 */
public class InitDB_Bitacora {
    public static void main(String[] args) {
        String url  = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "root";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            // Tabla de bitácora — diseñada como append-only
            // ENGINE=InnoDB para consistencia ACID en INSERT
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS tb_bitacora (" +
                "  idBitacora INT PRIMARY KEY AUTO_INCREMENT, " +
                "  usuario    VARCHAR(100)  NOT NULL, " +
                "  modulo     VARCHAR(50)   NOT NULL, " +
                "  accion     VARCHAR(100)  NOT NULL, " +
                "  resultado  VARCHAR(20)   NOT NULL, " +    // EXITO | FALLO
                "  detalle    VARCHAR(500)  DEFAULT '', " +  // info adicional
                "  timestamp  VARCHAR(25)   NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );

            System.out.println("[InitDB_Bitacora] ✅ tb_bitacora creada/verificada.");
            System.out.println("  NOTA: Ejecutar manualmente para restringir permisos:");
            System.out.println("  REVOKE UPDATE, DELETE ON bd_erp.tb_bitacora FROM 'root'@'localhost';");

        } catch (Exception ex) {
            System.err.println("[InitDB_Bitacora] ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
