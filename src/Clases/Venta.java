package Clases;

public class Venta {

    private int idVenta;
    private String cliente;
    private double total;
    private String fecha;

    public Venta() {
    }

    public Venta(int idVenta, String cliente,
            double total, String fecha) {

        this.idVenta = idVenta;
        this.cliente = cliente;
        this.total = total;
        this.fecha = fecha;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
