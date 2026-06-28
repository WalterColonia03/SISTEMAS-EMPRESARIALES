import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class InitDB6_MetodoPago {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "root";
        
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
            
            // Añadir columna de metodoPago si no existe
            try {
                stmt.executeUpdate("ALTER TABLE tb_venta ADD COLUMN metodoPago VARCHAR(50) DEFAULT 'Efectivo'");
                System.out.println("[InitDB6] OK: Columna metodoPago agregada a tb_venta.");
            } catch (Exception e) {
                if (e.getMessage().contains("Duplicate column name")) {
                    System.out.println("[InitDB6] INFO: La columna metodoPago ya existe.");
                } else {
                    System.err.println("[InitDB6] ERROR: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
