import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class InitDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "root";
        
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
            
            // tb_proveedor
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tb_proveedor (" +
                "idProveedor INT PRIMARY KEY AUTO_INCREMENT," +
                "ruc VARCHAR(20) UNIQUE NOT NULL," +
                "razonSocial VARCHAR(150) NOT NULL," +
                "contacto VARCHAR(100)," +
                "telefono VARCHAR(20)," +
                "correo VARCHAR(100)," +
                "direccion VARCHAR(200)," +
                "estado INT DEFAULT 1" +
                ")");
                
            // tb_compra
            // CORRECCIÓN: total DECIMAL(10,2) en vez de DOUBLE (2026-06-26 — Auditoría ERP)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tb_compra (" +
                "idCompra INT PRIMARY KEY AUTO_INCREMENT," +
                "idProveedor INT NOT NULL," +
                "total DECIMAL(10,2) NOT NULL DEFAULT 0.00," +
                "fecha VARCHAR(50)," +
                "FOREIGN KEY (idProveedor) REFERENCES tb_proveedor(idProveedor)" +
                ")");;
                
            // tb_detalle_compra
            // CORRECCIÓN: precioUnitario DECIMAL(10,2) en vez de DOUBLE (2026-06-26 — Auditoría ERP)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tb_detalle_compra (" +
                "idDetalle INT PRIMARY KEY AUTO_INCREMENT," +
                "idCompra INT NOT NULL," +
                "idProducto INT NOT NULL," +
                "cantidad INT," +
                "precioUnitario DECIMAL(10,2) NOT NULL DEFAULT 0.00," +
                "FOREIGN KEY (idCompra) REFERENCES tb_compra(idCompra)" +
                ")");
                
            // tb_kardex (movimientos)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tb_kardex (" +
                "idMovimiento INT PRIMARY KEY AUTO_INCREMENT," +
                "idProducto INT NOT NULL," +
                "tipoMovimiento VARCHAR(50)," + // ENTRADA, SALIDA
                "cantidad INT," +
                "fecha VARCHAR(50)," +
                "motivo VARCHAR(100)" +
                ")");
                
            System.out.println("Tables created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
