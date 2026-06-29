import java.sql.*;
public class AddCol {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/bd_erp?useSSL=false&serverTimezone=UTC";
        try (Connection c = DriverManager.getConnection(url, "root", "root");
             Statement s = c.createStatement()) {
            try { s.execute("ALTER TABLE tb_producto ADD COLUMN fechaVencimiento VARCHAR(20) DEFAULT NULL"); } catch(Exception e) {}
            try { s.execute("ALTER TABLE tb_producto ADD COLUMN lote VARCHAR(50) DEFAULT NULL"); } catch(Exception e) {}
            System.out.println("Columns added");
        }
    }
}
