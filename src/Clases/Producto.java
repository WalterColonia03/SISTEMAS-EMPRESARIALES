package Clases;

import java.math.BigDecimal;

/**
 * Entidad de dominio: Producto del inventario.
 *
 * CORRECCIÓN APLICADA (2026-06-26T00:53:00-05:00 — Auditoría ERP):
 *   - El campo `precio` migrado de `double` a `BigDecimal` para evitar
 *     errores de coma flotante en cálculos financieros.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §4.1 — "Evita double; usa BigDecimal")
 *
 * @author ERP LAREDO Team
 */
public class Producto {

    private int       idProducto;
    private String    nombre;
    private int       cantidad;
    private BigDecimal precio;      // CORREGIDO: era double
    private String    descripcion;
    private int       idCategoria;
    private int       estado;

    public Producto() {}

    public Producto(int idProducto, String nombre, int cantidad,
                    BigDecimal precio, String descripcion,
                    int idCategoria, int estado) {
        this.idProducto  = idProducto;
        this.nombre      = nombre;
        this.cantidad    = cantidad;
        this.precio      = precio;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.estado      = estado;
    }

    public int       getIdProducto()  { return idProducto; }
    public void      setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String    getNombre()      { return nombre; }
    public void      setNombre(String nombre) { this.nombre = nombre; }

    public int       getCantidad()    { return cantidad; }
    public void      setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecio()     { return precio; }
    public void       setPrecio(BigDecimal precio) { this.precio = precio; }

    public String    getDescripcion() { return descripcion; }
    public void      setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int       getIdCategoria() { return idCategoria; }
    public void      setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public int       getEstado()      { return estado; }
    public void      setEstado(int estado) { this.estado = estado; }
}
