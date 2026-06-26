package Clases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio: Orden de Compra a proveedor.
 *
 * CORRECCIÓN APLICADA (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `total` migrado de `double` a `BigDecimal`.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §4.1)
 *
 * @author ERP LAREDO Team
 */
public class Compra {

    private int              idCompra;
    private int              idProveedor;
    private BigDecimal       total;       // CORREGIDO: era double
    private String           fecha;
    private List<DetalleCompra> detalles;

    public Compra() {
        this.detalles = new ArrayList<>();
    }

    public int          getIdCompra()     { return idCompra; }
    public void         setIdCompra(int idCompra) { this.idCompra = idCompra; }

    public int          getIdProveedor()  { return idProveedor; }
    public void         setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public BigDecimal   getTotal()        { return total; }
    public void         setTotal(BigDecimal total) { this.total = total; }

    public String       getFecha()        { return fecha; }
    public void         setFecha(String fecha) { this.fecha = fecha; }

    public List<DetalleCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompra> detalles) { this.detalles = detalles; }
}
