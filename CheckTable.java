import java.sql.*;

public class CheckTable {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/bd_erp?useSSL=false&serverTimezone=UTC";
        try (Connection c = DriverManager.getConnection(url, "root", "root");
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("DESCRIBE tb_producto")) {
            while (rs.next()) {
                System.out.println(rs.getString(1) + " | " + rs.getString(2));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
