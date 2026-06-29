import java.sql.*;
public class AddVacaciones {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/bd_erp?useSSL=false&serverTimezone=UTC";
        try (Connection c = DriverManager.getConnection(url, "root", "root");
             Statement s = c.createStatement()) {
            try { s.execute("ALTER TABLE empleados ADD COLUMN vacacionesAcumuladas INT DEFAULT 15"); } catch(Exception e) {}
            System.out.println("vacacionesAcumuladas added");
        }
    }
}
