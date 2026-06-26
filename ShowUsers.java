import java.sql.*;
public class ShowUsers {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/bd_erp?useSSL=false&serverTimezone=UTC";
        try (Connection c = DriverManager.getConnection(url, "root", "root");
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT usuario, password, rol FROM tb_usuario")) {
            while (rs.next()) {
                System.out.println("USER: " + rs.getString(1) + " | PASS_HASH: " + rs.getString(2) + " | ROL: " + rs.getString(3));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
