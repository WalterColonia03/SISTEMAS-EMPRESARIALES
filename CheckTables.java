import java.sql.*;

public class CheckTables {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/bd_erp?useSSL=false&serverTimezone=UTC";
        try (Connection c = DriverManager.getConnection(url, "root", "root");
             Statement s = c.createStatement()) {
            System.out.println("--- tb_categoria ---");
            try (ResultSet rs = s.executeQuery("SELECT * FROM tb_categoria")) {
                while (rs.next()) System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getInt(3));
            }
            System.out.println("--- tb_producto categorias ---");
            try (ResultSet rs = s.executeQuery("SELECT idCategoria, count(*) FROM tb_producto GROUP BY idCategoria")) {
                while (rs.next()) System.out.println("Categoria: " + rs.getInt(1) + " - Count: " + rs.getInt(2));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
