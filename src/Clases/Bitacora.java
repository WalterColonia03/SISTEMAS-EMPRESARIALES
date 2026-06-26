package Clases;

/**
 * Entidad de dominio: Registro de auditoría de seguridad.
 *
 * Historia de Usuario: FR-020-v2 — Bitácora de Auditoría (IT-13)
 * Criterios de Aceptación: CA-1 a CA-6 en 04b_ADDENDUM_R3_FASE3_IT11_IT13.md
 *
 * Diseño:
 *   - Inmutable desde la UI (no hay setters para campos de auditoría).
 *   - Solo INSERT en BD; no soporta UPDATE ni DELETE.
 *
 * Creado: 2026-06-26T02:06:00-05:00
 */
public class Bitacora {

    private int    idBitacora;
    private String usuario;    // login del usuario que realizó la acción
    private String modulo;     // POS, LOGIN, INVENTARIO, COMPRAS, etc.
    private String accion;     // REGISTRO_VENTA, LOGIN_INTENTO, MODIFICAR_PRODUCTO, etc.
    private String resultado;  // EXITO | FALLO
    private String detalle;    // información adicional (id de registro afectado, etc.)
    private String timestamp;  // formato: dd/MM/yyyy HH:mm:ss

    public Bitacora() {}

    public Bitacora(String usuario, String modulo, String accion,
                    String resultado, String detalle, String timestamp) {
        this.usuario   = usuario;
        this.modulo    = modulo;
        this.accion    = accion;
        this.resultado = resultado;
        this.detalle   = detalle;
        this.timestamp = timestamp;
    }

    public int    getIdBitacora() { return idBitacora; }
    public void   setIdBitacora(int idBitacora) { this.idBitacora = idBitacora; }

    public String getUsuario()   { return usuario; }
    public String getModulo()    { return modulo; }
    public String getAccion()    { return accion; }
    public String getResultado() { return resultado; }
    public String getDetalle()   { return detalle; }
    public String getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + usuario + " · " + modulo + " · " + accion + " → " + resultado;
    }
}
