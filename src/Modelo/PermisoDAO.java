package Modelo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * DAO para leer/escribir permisos por rol (FR-042).
 * Usa permisos.properties.
 */
public class PermisoDAO {
    
    private final String RUTA_ARCHIVO = "permisos.properties";
    private Properties props;

    public PermisoDAO() {
        props = new Properties();
        cargar();
    }

    private void cargar() {
        try {
            File f = new File(RUTA_ARCHIVO);
            if (!f.exists()) {
                // Crear configuración por defecto
                props.setProperty("VENDEDOR", "VENTAS");
                props.setProperty("ALMACENERO", "INVENTARIO,COMPRAS");
                props.setProperty("FINANZAS", "FINANZAS,COMPRAS");
                props.setProperty("GERENTE", "VENTAS,INVENTARIO,COMPRAS,FINANZAS,PERSONAL,REPORTES");
                props.setProperty("ADMINISTRADOR", "VENTAS,INVENTARIO,COMPRAS,FINANZAS,PERSONAL,REPORTES,ADMINISTRACION");
                guardar();
            } else {
                try (FileInputStream fis = new FileInputStream(f)) {
                    props.load(fis);
                }
            }
        } catch (Exception ex) {
            Utils.LoggerGlobal.logError(this.getClass().getName(), "cargar", "Error leyendo permisos", ex);
        }
    }

    public void guardar() {
        try (FileOutputStream fos = new FileOutputStream(RUTA_ARCHIVO)) {
            props.store(fos, "Configuración de Permisos por Rol - ERP LAREDO (FR-042)");
        } catch (Exception ex) {
            Utils.LoggerGlobal.logError(this.getClass().getName(), "guardar", "Error guardando permisos", ex);
        }
    }

    public boolean tienePermiso(String rol, String modulo) {
        if (rol == null) return false;
        if (rol.equalsIgnoreCase("ADMINISTRADOR")) return true; // El admin siempre tiene acceso a todo
        
        String modulosStr = props.getProperty(rol.toUpperCase(), "");
        for (String m : modulosStr.split(",")) {
            if (m.trim().equalsIgnoreCase(modulo)) {
                return true;
            }
        }
        return false;
    }

    public void setPermisos(String rol, String modulosSeparadosPorComa) {
        props.setProperty(rol.toUpperCase(), modulosSeparadosPorComa);
        guardar();
    }
    
    public String getPermisos(String rol) {
        return props.getProperty(rol.toUpperCase(), "");
    }
}
