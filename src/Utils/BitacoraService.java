package Utils;

import Clases.Bitacora;
import Modelo.BitacoraDAO;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servicio transversal de Bitácora de Auditoría.
 *
 * Uso: BitacoraService.registrar(usuario, modulo, accion, resultado, detalle)
 *
 * Historia de Usuario: FR-020-v2 (IT-13)
 * Principio: SRP (Clean Code §3) — Este servicio hace UNA sola cosa:
 *            crear y persistir registros de auditoría.
 *
 * Creado: 2026-06-26T02:06:00-05:00
 */
public class BitacoraService {

    // Constantes de módulo — evita strings mágicos (Clean Code)
    public static final String MOD_LOGIN     = "LOGIN";
    public static final String MOD_POS       = "POS";
    public static final String MOD_INVENTARIO = "INVENTARIO";
    public static final String MOD_COMPRAS   = "COMPRAS";
    public static final String MOD_CLIENTES  = "CLIENTES";
    public static final String MOD_USUARIOS  = "USUARIOS";
    public static final String MOD_FINANZAS  = "FINANZAS";
    public static final String MOD_REPORTES  = "REPORTES";
    public static final String MOD_RRHH      = "RRHH";
    public static final String MOD_SEGURIDAD = "SEGURIDAD";
    public static final String MOD_VENTAS    = "VENTAS";

    // Constantes de resultado
    public static final String OK    = "EXITO";
    public static final String FALLO = "FALLO";

    // Formato de timestamp consistente en todo el sistema
    private static final String FMT_TIMESTAMP = "dd/MM/yyyy HH:mm:ss";

    private static final BitacoraDAO dao = new BitacoraDAO();

    /**
     * Registra un evento de auditoría con timestamp automático.
     *
     * @param usuario  Login del usuario activo (o "SISTEMA" si es automático)
     * @param modulo   Módulo donde ocurrió la acción (usar constantes MOD_*)
     * @param accion   Descripción corta de la acción (ej. REGISTRO_VENTA)
     * @param resultado OK o FALLO
     * @param detalle  Info adicional (id afectado, mensaje de error, etc.)
     */
    public static void registrar(String usuario, String modulo,
                                 String accion, String resultado, String detalle) {
        String ts = new SimpleDateFormat(FMT_TIMESTAMP).format(new Date());
        Bitacora b = new Bitacora(usuario, modulo, accion, resultado, detalle, ts);
        dao.registrar(b);
        // Log también al archivo para doble trazabilidad
        LoggerGlobal.info("[BITÁCORA] " + b.toString());
    }

    /**
     * Sobrecarga sin detalle (para eventos simples).
     */
    public static void registrar(String usuario, String modulo,
                                 String accion, String resultado) {
        registrar(usuario, modulo, accion, resultado, "");
    }
}
