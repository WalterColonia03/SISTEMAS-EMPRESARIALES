import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import Utils.PasswordUtils;

public class InitDB7_UsuariosSeed {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        String userDB = "root";
        String passDB = "root";

        String sql = "INSERT INTO tb_usuario (idUsuario, nombre, apellido, usuario, telefono, password, rol, estado, sesion_activa) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), apellido=VALUES(apellido), rol=VALUES(rol), password=VALUES(password)";

        try (Connection conn = DriverManager.getConnection(url, userDB, passDB);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 1. ADMIN (Administrador)
            ps.setInt(1, 1);
            ps.setString(2, "Caleb");
            ps.setString(3, "Venegas");
            ps.setString(4, "ADMIN");
            ps.setString(5, "987123654");
            ps.setString(6, PasswordUtils.hashPassword("1234"));
            ps.setString(7, "Administrador");
            ps.setInt(8, 1);
            ps.setInt(9, 0);
            ps.addBatch();

            // 2. user1 (Vendedor)
            ps.setInt(1, 2);
            ps.setString(2, "Carlos");
            ps.setString(3, "MARCOS");
            ps.setString(4, "user1");
            ps.setString(5, "912874653");
            ps.setString(6, PasswordUtils.hashPassword("2026"));
            ps.setString(7, "Vendedor");
            ps.setInt(8, 1);
            ps.setInt(9, 0);
            ps.addBatch();

            // 3. superadmin (Administrador)
            ps.setInt(1, 3);
            ps.setString(2, "Elias");
            ps.setString(3, "Santa Cruz");
            ps.setString(4, "superadmin");
            ps.setString(5, "959649719");
            ps.setString(6, PasswordUtils.hashPassword("superadmin"));
            ps.setString(7, "Administrador");
            ps.setInt(8, 1);
            ps.setInt(9, 0);
            ps.addBatch();

            ps.executeBatch();
            System.out.println("✅ Usuarios semilla registrados correctamente en MySQL.");

        } catch (Exception e) {
            System.err.println("❌ Error al insertar usuarios semilla: " + e.getMessage());
        }
    }
}
