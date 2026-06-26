import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AlterDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "root";
        
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
            
            try {
                stmt.executeUpdate("ALTER TABLE tb_venta MODIFY COLUMN idVenta INT AUTO_INCREMENT");
                System.out.println("tb_venta altered to AUTO_INCREMENT.");
            } catch (Exception e) { e.printStackTrace(); }
            
            try {
                stmt.executeUpdate("ALTER TABLE tb_detalle_venta MODIFY COLUMN idDetalle INT AUTO_INCREMENT");
                System.out.println("tb_detalle_venta altered to AUTO_INCREMENT.");
            } catch (Exception e) { e.printStackTrace(); }
            
            try {
                stmt.executeUpdate("ALTER TABLE tb_kardex MODIFY COLUMN idMovimiento INT AUTO_INCREMENT");
                System.out.println("tb_kardex altered to AUTO_INCREMENT.");
            } catch (Exception e) { e.printStackTrace(); }

            System.out.println("ALTERATIONS COMPLETE.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}