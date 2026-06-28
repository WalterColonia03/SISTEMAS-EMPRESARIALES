package Clases;

public class Evaluacion {
    private int id;
    private String idEmpleado;
    private int puntaje;
    private String comentarios;
    private String fecha;

    public Evaluacion() {}

    public Evaluacion(int id, String idEmpleado, int puntaje, String comentarios, String fecha) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.puntaje = puntaje;
        this.comentarios = comentarios;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { this.idEmpleado = idEmpleado; }

    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int puntaje) { this.puntaje = puntaje; }

    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
