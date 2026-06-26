package Clases;

import java.math.BigDecimal;

/**
 * Entidad de dominio: Línea de detalle de una orden de compra.
 *
 * CORRECCIÓN APLICADA (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - `precioUnitario` migrado de `double` a `BigDecimal`.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §4.1)
 *
 * @author ERP LAREDO Team
 */
public class DetalleCompra {

    private int        idDetalle;
    private int        idCompra;
    private int        idProducto;
    private int        cantidad;
    private BigDecimal precioUnitario;  // CORREGIDO: era double

    public DetalleCompra() {}

    public int         getIdDetalle()     { return idDetalle; }
    public void        setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int         getIdCompra()      { return idCompra; }
    public void        setIdCompra(int idCompra) { this.idCompra = idCompra; }

    public int         getIdProducto()    { return idProducto; }
    public void        setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int         getCantidad()      { return cantidad; }
    public void        setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal  getPrecioUnitario() { return precioUnitario; }
    public void        setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
