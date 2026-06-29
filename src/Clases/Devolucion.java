package Clases;

import java.math.BigDecimal;

public class Devolucion {
    private int idDevolucion;
    private int idVenta;
    private int idProducto;
    private int cantidad;
    private String motivo;
    private String tipoReembolso;
    private BigDecimal montoReembolso;
    private String fecha;

    public Devolucion() {}

    public Devolucion(int idVenta, int idProducto, int cantidad, String motivo, String tipoReembolso, BigDecimal montoReembolso, String fecha) {
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.tipoReembolso = tipoReembolso;
        this.montoReembolso = montoReembolso;
        this.fecha = fecha;
    }

    public int getIdDevolucion() { return idDevolucion; }
    public void setIdDevolucion(int idDevolucion) { this.idDevolucion = idDevolucion; }
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getTipoReembolso() { return tipoReembolso; }
    public void setTipoReembolso(String tipoReembolso) { this.tipoReembolso = tipoReembolso; }
    public BigDecimal getMontoReembolso() { return montoReembolso; }
    public void setMontoReembolso(BigDecimal montoReembolso) { this.montoReembolso = montoReembolso; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
