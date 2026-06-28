package Clases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio: Venta registrada en el POS.
 *
 * CORRECCIÓN APLICADA (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - El campo `total` migrado de `double` a `BigDecimal` para garantizar
 *     precisión contable y evitar descuadres de caja por errores de coma flotante.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §4.1)
 *
 * @author ERP LAREDO Team
 */
public class Venta {

    private int            idVenta;
    private String         cliente;
    private BigDecimal     total;
    private String         fecha;
    private String         metodoPago;
    private List<DetalleVenta> detalles;

    public Venta() {
        this.detalles = new ArrayList<>();
        this.metodoPago = "Efectivo";
    }

    public Venta(int idVenta, String cliente, BigDecimal total, String fecha, String metodoPago) {
        this.idVenta  = idVenta;
        this.cliente  = cliente;
        this.total    = total;
        this.fecha    = fecha;
        this.metodoPago = metodoPago;
        this.detalles = new ArrayList<>();
    }

    public int          getIdVenta()  { return idVenta; }
    public void         setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public String       getCliente()  { return cliente; }
    public void         setCliente(String cliente) { this.cliente = cliente; }

    public BigDecimal   getTotal()    { return total; }
    public void         setTotal(BigDecimal total) { this.total = total; }

    public String       getFecha()    { return fecha; }
    public void         setFecha(String fecha) { this.fecha = fecha; }

    public String       getMetodoPago() { return metodoPago; }
    public void         setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
}
