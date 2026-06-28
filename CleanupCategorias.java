import java.sql.*;
import java.io.*;
import java.util.*;

public class CleanupCategorias {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/bd_erp?useSSL=false&serverTimezone=UTC";
        try (Connection c = DriverManager.getConnection(url, "root", "root");
             Statement s = c.createStatement()) {
            
            // 1. Update tb_producto
            int abarrotesUpdated = s.executeUpdate("UPDATE tb_producto SET idCategoria = 1 WHERE idCategoria = 5");
            System.out.println("Updated products from cat 5 to 1 (Abarrotes): " + abarrotesUpdated);
            
            int lacteosUpdated = s.executeUpdate("UPDATE tb_producto SET idCategoria = 3 WHERE idCategoria = 4");
            System.out.println("Updated products from cat 4 to 3 (Lacteos): " + lacteosUpdated);
            
            // 2. Delete duplicate categories
            int catsDeleted = s.executeUpdate("DELETE FROM tb_categoria WHERE idCategoria IN (4, 5)");
            System.out.println("Deleted duplicate categories 4 and 5: " + catsDeleted);
            
            // 3. Fix encoding of 3 (Lácteos) if it's messed up, we can ensure it's correct
            // Assuming it's already "Lácteos" or "Lacteos"
            s.executeUpdate("UPDATE tb_categoria SET descripcion = 'Lácteos' WHERE idCategoria = 3");
            
            // 4. Dump to categorias.txt (to maintain legacy file in sync)
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("categorias.txt"))) {
                try (ResultSet rs = s.executeQuery("SELECT * FROM tb_categoria ORDER BY idCategoria")) {
                    while (rs.next()) {
                        String desc = rs.getString(2);
                        // Fix any mojibake in description if necessary, but we fixed Lácteos above.
                        bw.write(rs.getInt(1) + "," + desc + "," + rs.getInt(3));
                        bw.newLine();
                    }
                }
            }
            System.out.println("categorias.txt updated successfully.");
            
        } catch (Exception e) { e.printStackTrace(); }
    }
}
