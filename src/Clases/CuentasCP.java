package Clases;

import java.math.BigDecimal;

/**
 * Entidad de dominio: Cuenta por Cobrar o por Pagar.
 *
 * CORRECCIÓN APLICADA (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `montoOriginal` y `montoPendiente` migrados de `double` a `BigDecimal`.
 *     Crítico: los montos de deudas deben ser exactos para no sub/sobre-cobrar.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §4.1)
 *
 * @author ERP LAREDO Team
 */
public class CuentasCP {

    private int        idCuenta;
    private String     tipo;                // COBRAR | PAGAR
    private String     clienteProveedor;
    private String     documento;
    private BigDecimal montoOriginal;       // CORREGIDO: era double
    private BigDecimal montoPendiente;      // CORREGIDO: era double
    private String     fechaEmision;
    private String     fechaVencimiento;
    private String     estado;              // PENDIENTE | VENCIDA | PAGADA

    public CuentasCP() {}

    public int         getIdCuenta()          { return idCuenta; }
    public void        setIdCuenta(int id)    { this.idCuenta = id; }

    public String      getTipo()              { return tipo; }
    public void        setTipo(String tipo)   { this.tipo = tipo; }

    public String      getClienteProveedor()  { return clienteProveedor; }
    public void        setClienteProveedor(String cp) { this.clienteProveedor = cp; }

    public String      getDocumento()         { return documento; }
    public void        setDocumento(String d) { this.documento = d; }

    public BigDecimal  getMontoOriginal()     { return montoOriginal; }
    public void        setMontoOriginal(BigDecimal m) { this.montoOriginal = m; }

    public BigDecimal  getMontoPendiente()    { return montoPendiente; }
    public void        setMontoPendiente(BigDecimal m) { this.montoPendiente = m; }

    public String      getFechaEmision()      { return fechaEmision; }
    public void        setFechaEmision(String f) { this.fechaEmision = f; }

    public String      getFechaVencimiento()  { return fechaVencimiento; }
    public void        setFechaVencimiento(String f) { this.fechaVencimiento = f; }

    public String      getEstado()            { return estado; }
    public void        setEstado(String e)    { this.estado = e; }
}
