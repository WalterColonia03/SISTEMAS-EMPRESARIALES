package Clases;

public class Sesion {
    private static String rolActual = "";
    private static String usuarioActual = "";

    public static String getRol() { return rolActual; }
    public static void setRol(String rol) { rolActual = rol; }

    public static String getUsuario() { return usuarioActual; }
    public static void setUsuario(String usuario) { usuarioActual = usuario; }
}
