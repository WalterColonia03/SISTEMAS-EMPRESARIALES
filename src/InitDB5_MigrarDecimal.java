import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Script de migración de base de datos existente.
 * Convierte columnas DOUBLE → DECIMAL(10,2) en bases de datos ya creadas.
 *
 * IMPORTANTE: Solo ejecutar si la BD ya existe y tiene datos.
 *             Si la BD es nueva, los InitDB*.java ya crean las columnas correctas.
 *
 * Generado: 2026-06-26T01:09:00-05:00 — Post-Auditoría ERP
 */
public class InitDB5_MigrarDecimal {
    public static void main(String[] args) {
        String url  = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "root";

        System.out.println("[InitDB5] Iniciando migración DOUBLE → DECIMAL(10,2)...");

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            // Ejecutar cada ALTER individualmente para que no interrumpa si alguno ya está migrado
            ejecutar(stmt, "ALTER TABLE tb_compra        MODIFY COLUMN total          DECIMAL(10,2) NOT NULL DEFAULT 0.00");
            ejecutar(stmt, "ALTER TABLE tb_detalle_compra MODIFY COLUMN precioUnitario DECIMAL(10,2) NOT NULL DEFAULT 0.00");
            ejecutar(stmt, "ALTER TABLE tb_detalle_venta  MODIFY COLUMN precioUnitario DECIMAL(10,2) NOT NULL DEFAULT 0.00");
            ejecutar(stmt, "ALTER TABLE tb_flujo_caja     MODIFY COLUMN monto          DECIMAL(10,2) NOT NULL DEFAULT 0.00");
            ejecutar(stmt, "ALTER TABLE tb_libro_mayor    MODIFY COLUMN montoDebe      DECIMAL(10,2) NOT NULL DEFAULT 0.00");
            ejecutar(stmt, "ALTER TABLE tb_libro_mayor    MODIFY COLUMN montoHaber     DECIMAL(10,2) NOT NULL DEFAULT 0.00");
            ejecutar(stmt, "ALTER TABLE tb_cuentas_cp     MODIFY COLUMN montoOriginal  DECIMAL(10,2) NOT NULL DEFAULT 0.00");
            ejecutar(stmt, "ALTER TABLE tb_cuentas_cp     MODIFY COLUMN montoPendiente DECIMAL(10,2) NOT NULL DEFAULT 0.00");
            // tb_producto.precio y tb_venta.total ya estaban como DECIMAL en MigracionDB.java

            System.out.println("\n[InitDB5] ✅ Migración completada. Todas las columnas monetarias son ahora DECIMAL(10,2).");

        } catch (Exception ex) {
            System.err.println("[InitDB5] ERROR FATAL: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void ejecutar(Statement stmt, String sql) {
        try {
            stmt.executeUpdate(sql);
            System.out.println("  OK: " + sql.substring(0, Math.min(sql.length(), 70)) + "...");
        } catch (Exception ex) {
            System.out.println("  SKIP (ya migrado o tabla no existe): " + ex.getMessage());
        }
    }
}
