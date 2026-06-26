package Clases;

import java.math.BigDecimal;

/**
 * Entidad de dominio: Asiento Contable del Libro Mayor.
 *
 * CORRECCIÓN APLICADA (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `montoDebe` y `montoHaber` migrados de `double` a `BigDecimal`.
 *     Crítico: el Libro Mayor es la base de la contabilidad; un error de
 *     redondeo aquí causa descuadres en los estados financieros.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §4.1)
 *
 * @author ERP LAREDO Team
 */
public class LibroMayor {

    private int        idAsiento;
    private String     fecha;
    private String     glosa;
    private String     cuentaDebe;
    private String     cuentaHaber;
    private BigDecimal montoDebe;    // CORREGIDO: era double
    private BigDecimal montoHaber;   // CORREGIDO: era double
    private String     nroAsiento;

    public LibroMayor() {}

    public int         getIdAsiento()    { return idAsiento; }
    public void        setIdAsiento(int idAsiento) { this.idAsiento = idAsiento; }

    public String      getFecha()        { return fecha; }
    public void        setFecha(String fecha) { this.fecha = fecha; }

    public String      getGlosa()        { return glosa; }
    public void        setGlosa(String glosa) { this.glosa = glosa; }

    public String      getCuentaDebe()   { return cuentaDebe; }
    public void        setCuentaDebe(String cuentaDebe) { this.cuentaDebe = cuentaDebe; }

    public String      getCuentaHaber()  { return cuentaHaber; }
    public void        setCuentaHaber(String cuentaHaber) { this.cuentaHaber = cuentaHaber; }

    public BigDecimal  getMontoDebe()    { return montoDebe; }
    public void        setMontoDebe(BigDecimal montoDebe) { this.montoDebe = montoDebe; }

    public BigDecimal  getMontoHaber()   { return montoHaber; }
    public void        setMontoHaber(BigDecimal montoHaber) { this.montoHaber = montoHaber; }

    public String      getNroAsiento()   { return nroAsiento; }
    public void        setNroAsiento(String nroAsiento) { this.nroAsiento = nroAsiento; }
}
