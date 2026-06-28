import Clases.Usuario;
import Modelo.UsuarioDAO;
import Utils.PasswordUtils;

public class InitUsers {
    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();
        
        System.out.println("Creando usuarios con roles...");
        
        crearUsuario(dao, "cajero1", "Cajero", "Cajero");
        crearUsuario(dao, "almacen1", "Almacen", "Almacenero");
        crearUsuario(dao, "contador1", "Finanzas", "Contador");
        crearUsuario(dao, "gerente1", "Gerencia", "Gerente");
        
        System.out.println("¡Usuarios creados con éxito!");
    }
    
    private static void crearUsuario(UsuarioDAO dao, String usuario, String area, String rol) {
        int id = dao.generarId();
        Usuario u = new Usuario(id, "Empleado", area, usuario, "123456789", "123", rol, 1);
        u.setPassword(PasswordUtils.hashPassword("123")); // Contraseña por defecto: 123
        if (dao.guardar(u)) {
            System.out.println("Usuario " + usuario + " [" + rol + "] creado exitosamente.");
        } else {
            System.out.println("El usuario " + usuario + " ya existe o hubo un error.");
        }
    }
}
