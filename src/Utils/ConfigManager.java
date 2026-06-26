package Utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Cargador centralizado de propiedades del sistema ERP.
 * Lee desde config.properties en la raíz del proyecto.
 *
 * SEGURIDAD: Las credenciales de BD y tokens de API se leen
 * de este archivo, el cual está excluido del control de versiones
 * (.gitignore) para evitar su exposición.
 *
 * Creado: 2026-06-26T00:53:00-05:00
 * Principio aplicado: The Pragmatic Programmer — Separación de configuración del código.
 */
public class ConfigManager {

    private static final Properties props = new Properties();
    private static boolean loaded = false;

    static {
        cargarConfig();
    }

    /**
     * Carga el archivo config.properties desde el classpath o la raíz del proyecto.
     * Si no se encuentra, usa valores por defecto con un warning en el logger.
     */
    private static void cargarConfig() {
        // Intento 1: leer desde classpath (útil en .jar empaquetado)
        try (InputStream is = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (is != null) {
                props.load(is);
                loaded = true;
                return;
            }
        } catch (Exception ex) {
            // Ignorar, se intenta el siguiente método
        }

        // Intento 2: leer desde el directorio de trabajo (desarrollo)
        try (InputStream is = new java.io.FileInputStream("config.properties")) {
            props.load(is);
            loaded = true;
        } catch (Exception ex) {
            // Último recurso: valores por defecto para que el sistema arranque
            System.err.println("[ConfigManager] ADVERTENCIA: config.properties no encontrado. " +
                    "Usando valores por defecto. NO usar en producción.");
            props.setProperty("db.url",      "jdbc:mysql://localhost:3306/bd_erp?serverTimezone=UTC&useSSL=false");
            props.setProperty("db.user",     "root");
            props.setProperty("db.password", "root");
            props.setProperty("api.token",   "");
            props.setProperty("api.url.dni", "https://dniruc.apisperu.com/api/v1/dni/");
            props.setProperty("api.url.ruc", "https://dniruc.apisperu.com/api/v1/ruc/");
        }
    }

    /** Devuelve el valor de una propiedad por clave. */
    public static String get(String key) {
        return props.getProperty(key, "");
    }
}
