import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.math.BigDecimal;

public class TestVenta {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "root";
        
        String sqlVenta   = "INSERT INTO tb_venta (cliente, total, fecha) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO tb_detalle_venta (idVenta, idProducto, cantidad, precioUnitario) " +
                            "VALUES (?, ?, ?, ?)";
        String sqlStock   = "UPDATE tb_producto SET cantidad = cantidad - ? WHERE idProducto = ?";
        String sqlKardex  = "INSERT INTO tb_kardex (idProducto, tipoMovimiento, cantidad, fecha, motivo) " +
                            "VALUES (?, 'SALIDA', ?, ?, 'VENTA')";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            conn.setAutoCommit(false);
            
            int idVentaGenerado = -1;
            try (PreparedStatement psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setString(1, "Consumidor Final");
                psVenta.setBigDecimal(2, new BigDecimal("175.40"));
                psVenta.setString(3, "2026-06-26 10:30:00");
                psVenta.executeUpdate();

                try (ResultSet rs = psVenta.getGeneratedKeys()) {
                    if (rs.next()) idVentaGenerado = rs.getInt(1);
                }
            }
            
            System.out.println("Venta inserted, ID: " + idVentaGenerado);

            try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle);
                 PreparedStatement psStock   = conn.prepareStatement(sqlStock);
                 PreparedStatement psKardex  = conn.prepareStatement(sqlKardex)) {

                // Mock Detalle
                psDetalle.setInt(1, idVentaGenerado);
                psDetalle.setInt(2, 1); // idProducto 1 (Leche condensada)
                psDetalle.setInt(3, 10);
                psDetalle.setBigDecimal(4, new BigDecimal("4.50"));
                psDetalle.executeUpdate();
                System.out.println("Detalle inserted.");

                psStock.setInt(1, 10);
                psStock.setInt(2, 1);
                psStock.executeUpdate();
                System.out.println("Stock updated.");

                psKardex.setInt(1, 1);
                psKardex.setInt(2, 10);
                psKardex.setString(3, "2026-06-26 10:30:00");
                psKardex.executeUpdate();
                System.out.println("Kardex inserted.");
            }

            conn.commit();
            System.out.println("ALL SUCCESSFUL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}