package Clases;

import java.math.BigDecimal;
import java.util.Date;

public class Empleado {
    private String codigo;
    private String dni;
    private String nombres;
    private String apellidos;
    private String cargo;
    private Date fechaIngreso;
    private BigDecimal sueldoBase;
    private String turno;
    private String telefono;
    private String email;
    private boolean activo;

    public Empleado() {}

    public Empleado(String codigo, String dni, String nombres, String apellidos, String cargo, Date fechaIngreso, BigDecimal sueldoBase, String turno, String telefono, String email, boolean activo) {
        this.codigo = codigo;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cargo = cargo;
        this.fechaIngreso = fechaIngreso;
        this.sueldoBase = sueldoBase;
        this.turno = turno;
        this.telefono = telefono;
        this.email = email;
        this.activo = activo;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public BigDecimal getSueldoBase() { return sueldoBase; }
    public void setSueldoBase(BigDecimal sueldoBase) { this.sueldoBase = sueldoBase; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
