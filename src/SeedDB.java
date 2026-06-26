import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SeedDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "root";
        
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
            
            // Insertar Categorías
            stmt.executeUpdate("INSERT IGNORE INTO tb_categoria (idCategoria, descripcion, estado) VALUES (1, 'Abarrotes', 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_categoria (idCategoria, descripcion, estado) VALUES (2, 'Bebidas', 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_categoria (idCategoria, descripcion, estado) VALUES (3, 'Lácteos', 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_categoria (idCategoria, descripcion, estado) VALUES (4, 'Limpieza', 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_categoria (idCategoria, descripcion, estado) VALUES (5, 'Snacks', 1)");

            // Insertar Productos
            stmt.executeUpdate("INSERT IGNORE INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (101, 'Arroz Costeño 5kg', 50, 18.00, 'Arroz extra', 1, 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (102, 'Aceite Primor 1L', 40, 9.50, 'Aceite vegetal', 1, 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (103, 'Fideos Don Vittorio 500g', 100, 3.20, 'Fideos largos', 1, 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (104, 'Coca Cola 500ml', 80, 2.50, 'Gaseosa', 2, 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (105, 'Inca Kola 500ml', 80, 2.50, 'Gaseosa', 2, 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (106, 'Agua San Luis 625ml', 60, 1.50, 'Agua sin gas', 2, 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (107, 'Leche Gloria Entera 1L', 100, 3.50, 'Leche evaporada', 3, 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (108, 'Yogurt Gloria Fresa 1L', 40, 6.50, 'Yogurt bebible', 3, 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (109, 'Queso Mantecoso 200g', 30, 8.00, 'Queso fresco', 3, 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (110, 'Detergente Ariel 500g', 50, 8.50, 'Detergente en polvo', 4, 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (111, 'Galleta Oreo 54g', 120, 1.20, 'Galletas rellenas', 5, 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_producto (idProducto, nombre, cantidad, precio, descripcion, idCategoria, estado) VALUES (112, 'Papas Lays 35g', 80, 2.00, 'Snack de papas', 5, 1)");

            // Insertar Proveedores
            stmt.executeUpdate("INSERT IGNORE INTO tb_proveedor (idProveedor, ruc, razonSocial, contacto, telefono, correo, direccion, estado) VALUES (1, '20100152356', 'Alicorp S.A.A.', 'Juan Perez', '999888777', 'ventas@alicorp.pe', 'Av. Argentina 4793', 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_proveedor (idProveedor, ruc, razonSocial, contacto, telefono, correo, direccion, estado) VALUES (2, '20100113610', 'Gloria S.A.', 'Maria Gomez', '988777666', 'distribucion@gloria.com.pe', 'Av. Republica de Panama 2461', 1)");
            stmt.executeUpdate("INSERT IGNORE INTO tb_proveedor (idProveedor, ruc, razonSocial, contacto, telefono, correo, direccion, estado) VALUES (3, '20100113611', 'Arca Continental', 'Luis Ramirez', '977666555', 'pedidos@arcacontal.com', 'Av. Nicolas Ayllon', 1)");

            System.out.println("Base de datos poblada con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}