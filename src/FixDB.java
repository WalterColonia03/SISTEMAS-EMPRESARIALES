import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class FixDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "root";
        
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
            
            // tb_venta
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tb_venta (" +
                "idVenta INT PRIMARY KEY AUTO_INCREMENT," +
                "cliente VARCHAR(255) NOT NULL," +
                "total DECIMAL(10,2) NOT NULL DEFAULT 0.00," +
                "fecha VARCHAR(50)" +
                ")");
                
            // tb_detalle_venta
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tb_detalle_venta (" +
                "idDetalle INT PRIMARY KEY AUTO_INCREMENT," +
                "idVenta INT NOT NULL," +
                "idProducto INT NOT NULL," +
                "cantidad INT," +
                "precioUnitario DECIMAL(10,2) NOT NULL DEFAULT 0.00," +
                "FOREIGN KEY (idVenta) REFERENCES tb_venta(idVenta)" +
                ")");
                
            System.out.println("Venta tables checked/created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}