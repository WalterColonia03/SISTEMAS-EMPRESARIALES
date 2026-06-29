import java.sql.*;
public class FixAdmin {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/bd_erp?useSSL=false&serverTimezone=UTC";
        try (Connection c = DriverManager.getConnection(url, "root", "root");
             Statement s = c.createStatement()) {
            int rows = s.executeUpdate("UPDATE tb_usuario SET password='240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9' WHERE usuario='admin'");
            System.out.println("Rows updated: " + rows);
        }
    }
}
