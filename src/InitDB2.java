import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Script de inicialización de BD — Tablas financieras (Flujo de Caja, Libro Mayor, Cuentas CP).
 *
 * CORRECCIÓN APLICADA (2026-06-26T01:09:00-05:00 — Auditoría ERP):
 *   - Todos los campos monetarios cambiados de `DOUBLE` → `DECIMAL(10,2)`.
 *     DOUBLE tiene imprecisión de coma flotante; DECIMAL garantiza exactitud
 *     para cualquier valor hasta 99,999,999.99 con centavos precisos.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §4.1 — BigDecimal en toda la pila)
 *
 * @see MigracionDB para seeding de datos iniciales.
 */
public class InitDB2 {
    public static void main(String[] args) {
        // NOTA: Credenciales leídas aquí directamente porque este es un script de utilidad
        //       (no es parte de la aplicación en runtime). En producción, usar ConfigManager.
        String url  = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "root";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            // -- Flujo de Caja --
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tb_flujo_caja (" +
                "idFlujo   INT PRIMARY KEY AUTO_INCREMENT, " +
                "fecha     VARCHAR(50), " +
                "tipo      VARCHAR(50), " +      // INGRESO | EGRESO
                "concepto  VARCHAR(200), " +
                "monto     DECIMAL(10,2) NOT NULL DEFAULT 0.00" + // CORREGIDO: era DOUBLE
                ")");

            // -- Libro Mayor (Asientos Contables) --
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tb_libro_mayor (" +
                "idAsiento   INT PRIMARY KEY AUTO_INCREMENT, " +
                "fecha        VARCHAR(50), " +
                "glosa        VARCHAR(200), " +
                "cuentaDebe   VARCHAR(100), " +
                "cuentaHaber  VARCHAR(100), " +
                "montoDebe    DECIMAL(10,2) NOT NULL DEFAULT 0.00, " + // CORREGIDO
                "montoHaber   DECIMAL(10,2) NOT NULL DEFAULT 0.00, " + // CORREGIDO
                "nroAsiento   VARCHAR(50)" +
                ")");

            // -- Cuentas por Cobrar y Pagar --
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tb_cuentas_cp (" +
                "idCuenta         INT PRIMARY KEY AUTO_INCREMENT, " +
                "tipo             VARCHAR(50), " +  // COBRAR | PAGAR
                "clienteProveedor VARCHAR(150), " +
                "documento        VARCHAR(100), " +
                "montoOriginal    DECIMAL(10,2) NOT NULL DEFAULT 0.00, " + // CORREGIDO
                "montoPendiente   DECIMAL(10,2) NOT NULL DEFAULT 0.00, " + // CORREGIDO
                "fechaEmision     VARCHAR(50), " +
                "fechaVencimiento VARCHAR(50), " +
                "estado           VARCHAR(50)" + // PENDIENTE | VENCIDA | PAGADA
                ")");

            System.out.println("[InitDB2] Tablas financieras creadas/verificadas correctamente.");
            System.out.println("  → tb_flujo_caja: monto DECIMAL(10,2)");
            System.out.println("  → tb_libro_mayor: montoDebe/montoHaber DECIMAL(10,2)");
            System.out.println("  → tb_cuentas_cp: montoOriginal/montoPendiente DECIMAL(10,2)");

        } catch (Exception ex) {
            System.err.println("[InitDB2] ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
