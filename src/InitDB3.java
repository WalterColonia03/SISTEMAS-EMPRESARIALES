import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Script de inicialización de BD — Tablas de ventas (DetVenta, Cliente).
 *
 * CORRECCIÓN APLICADA (2026-06-26T01:09:00-05:00 — Auditoría ERP):
 *   - `precioUnitario DOUBLE` → `precioUnitario DECIMAL(10,2)`.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §4.1)
 */
public class InitDB3 {
    public static void main(String[] args) {
        String url  = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "root";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            // -- Detalle de Venta --
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tb_detalle_venta (" +
                "idDetalle      INT PRIMARY KEY AUTO_INCREMENT, " +
                "idVenta        INT, " +
                "idProducto     INT, " +
                "cantidad       INT, " +
                "precioUnitario DECIMAL(10,2) NOT NULL DEFAULT 0.00" + // CORREGIDO: era DOUBLE
                ")");

            // -- Clientes --
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tb_cliente (" +
                "idCliente  INT PRIMARY KEY AUTO_INCREMENT, " +
                "nombre     VARCHAR(100), " +
                "apellido   VARCHAR(100), " +
                "dni        VARCHAR(20), " +
                "telefono   VARCHAR(20), " +
                "direccion  VARCHAR(200), " +
                "estado     INT DEFAULT 1, " +
                "puntos     INT DEFAULT 0" +
                ")");

            // -- Consumidor Final (registro base inmutable) --
            stmt.executeUpdate(
                "INSERT IGNORE INTO tb_cliente (idCliente, dni, nombre) " +
                "VALUES (1, '00000000', 'Consumidor Final')");

            System.out.println("[InitDB3] Tablas de ventas creadas/verificadas correctamente.");
            System.out.println("  → tb_detalle_venta: precioUnitario DECIMAL(10,2)");

        } catch (Exception ex) {
            System.err.println("[InitDB3] ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
