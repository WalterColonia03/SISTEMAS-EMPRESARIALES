package Clases;

import java.math.BigDecimal;

/**
 * Entidad de dominio: Movimiento de Flujo de Caja (Ingreso/Egreso).
 *
 * CORRECCIÓN APLICADA (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `monto` migrado de `double` a `BigDecimal`.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §4.1)
 *
 * @author ERP LAREDO Team
 */
public class FlujoCaja {

    private int        idFlujo;
    private String     fecha;
    private String     tipo;      // INGRESO | EGRESO
    private String     concepto;
    private BigDecimal monto;     // CORREGIDO: era double

    public FlujoCaja() {}

    public int         getIdFlujo()  { return idFlujo; }
    public void        setIdFlujo(int idFlujo) { this.idFlujo = idFlujo; }

    public String      getFecha()    { return fecha; }
    public void        setFecha(String fecha) { this.fecha = fecha; }

    public String      getTipo()     { return tipo; }
    public void        setTipo(String tipo) { this.tipo = tipo; }

    public String      getConcepto() { return concepto; }
    public void        setConcepto(String concepto) { this.concepto = concepto; }

    public BigDecimal  getMonto()    { return monto; }
    public void        setMonto(BigDecimal monto) { this.monto = monto; }
}
