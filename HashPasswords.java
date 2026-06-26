import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashPasswords {
    public static String hashPassword(String plainPassword) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) { sb.append(String.format("%02x", b)); }
        return sb.toString();
    }
    
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/bd_erp?useSSL=false&serverTimezone=UTC";
        try (Connection c = DriverManager.getConnection(url, "root", "root");
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT idUsuario, password FROM tb_usuario")) {
             
            while (rs.next()) {
                int id = rs.getInt(1);
                String pass = rs.getString(2);
                
                // Si la longitud es < 64 (ej: 1234), entonces es texto plano, hay que hashearlo
                if (pass.length() < 64) {
                    String hashed = hashPassword(pass);
                    try (PreparedStatement u = c.prepareStatement("UPDATE tb_usuario SET password = ? WHERE idUsuario = ?")) {
                        u.setString(1, hashed);
                        u.setInt(2, id);
                        u.executeUpdate();
                        System.out.println("Hasheada pass para ID " + id);
                    }
                }
            }
        }
    }
}
