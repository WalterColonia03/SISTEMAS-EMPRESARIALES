import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class InicializarProyecto {
    public static void main(String[] args) {
        String urlRoot = "jdbc:mysql://localhost:3306/?serverTimezone=UTC&useSSL=false";
        String urlDB = "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "root";

        System.out.println("--- INICIALIZANDO PROYECTO ERP LAREDO ---");

        // 1. Crear Base de Datos
        try (Connection conn = DriverManager.getConnection(urlRoot, user, pass);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS bd_erp");
            System.out.println("✅ Base de datos 'bd_erp' verificada/creada.");
        } catch (Exception e) {
            System.out.println("❌ Error conectando a MySQL: " + e.getMessage());
            System.out.println("Asegúrate de que XAMPP/MySQL esté encendido en el puerto 3306 con usuario 'root' y contraseña 'root'.");
            return;
        }

        // 2. Ejecutar InitDBs
        try {
            System.out.println("Ejecutando scripts de creación de tablas...");
            InitDB.main(args);
            InitDB2.main(args);
            InitDB3.main(args);
            InitDB4.main(args);
            InitDB5_MigrarDecimal.main(args);
            InitDB6_MetodoPago.main(args);
            InitDB_Bitacora.main(args);
            
            System.out.println("✅ Estructura de tablas creada.");
            
            System.out.println("Iniciando migración de datos iniciales...");
            Modelo.MigracionDB.main(args);
            System.out.println("✅ Inicialización completada con éxito. Ya puedes iniciar sesión.");
        } catch (Exception e) {
            System.out.println("❌ Error durante la creación de tablas: " + e.getMessage());
        }
    }
}
