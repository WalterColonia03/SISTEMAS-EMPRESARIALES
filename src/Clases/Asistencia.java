package Clases;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Time;

public class Asistencia {
    private String id;
    private String idEmpleado;
    private Date fecha;
    private Time horaEntrada;
    private Time horaSalida;
    private BigDecimal horasTrabajadas;
    private String observacion;

    public Asistencia() {}

    public Asistencia(String id, String idEmpleado, Date fecha, Time horaEntrada, Time horaSalida, BigDecimal horasTrabajadas, String observacion) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.horasTrabajadas = horasTrabajadas;
        this.observacion = observacion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { this.idEmpleado = idEmpleado; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Time getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(Time horaEntrada) { this.horaEntrada = horaEntrada; }

    public Time getHoraSalida() { return horaSalida; }
    public void setHoraSalida(Time horaSalida) { this.horaSalida = horaSalida; }

    public BigDecimal getHorasTrabajadas() { return horasTrabajadas; }
    public void setHorasTrabajadas(BigDecimal horasTrabajadas) { this.horasTrabajadas = horasTrabajadas; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
