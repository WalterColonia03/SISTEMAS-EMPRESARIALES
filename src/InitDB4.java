import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class InitDB4 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        try (Connection c = DriverManager.getConnection(url, "root", "root");
             Statement s = c.createStatement()) {
            
            // Ignorar error si la columna ya existe
            try {
                s.executeUpdate("ALTER TABLE tb_cliente ADD COLUMN puntos INT DEFAULT 0");
                System.out.println("Columna 'puntos' agregada a tb_cliente.");
            } catch (Exception e) {
                System.out.println("La columna 'puntos' ya existe o hubo un error: " + e.getMessage());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
