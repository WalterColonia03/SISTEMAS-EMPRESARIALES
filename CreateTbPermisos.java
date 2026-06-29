import java.sql.*;
public class CreateTbPermisos {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/bd_erp?useSSL=false&serverTimezone=UTC";
        try (Connection c = DriverManager.getConnection(url, "root", "root");
             Statement s = c.createStatement()) {
            
            s.execute("CREATE TABLE IF NOT EXISTS tb_permisos (" +
                      "rol VARCHAR(50) PRIMARY KEY, " +
                      "modulos VARCHAR(500))");
                      
            s.execute("INSERT INTO tb_permisos (rol, modulos) VALUES " +
                      "('ADMINISTRADOR', 'VENTAS,INVENTARIO,COMPRAS,FINANZAS,PERSONAL,REPORTES,ADMINISTRACION')," +
                      "('VENDEDOR', 'VENTAS')," +
                      "('ALMACENERO', 'INVENTARIO,COMPRAS')," +
                      "('FINANZAS', 'FINANZAS,COMPRAS')," +
                      "('GERENTE', 'VENTAS,INVENTARIO,COMPRAS,FINANZAS,PERSONAL,REPORTES') " +
                      "ON DUPLICATE KEY UPDATE modulos=VALUES(modulos)");
                      
            System.out.println("tb_permisos created and seeded successfully.");
        }
    }
}
