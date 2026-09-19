package edu.umg.programacion2.proyecto.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {
    private int id;
    private String nombrePaciente;
    private LocalDate fechaCita;
    private LocalTime horaInicio;
    private LocalTime horaFin; // Rango de cita
    private String motivo;
    private EstadoCita estado;
    private int sala;

    public Cita(int id, String nombrePaciente, LocalDate fechaCita, LocalTime horaInicio, LocalTime horaFin, String motivo, EstadoCita estado, int sala) {
        this.id = id;
        this.nombrePaciente = nombrePaciente;
        this.fechaCita = fechaCita;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin != null ? horaFin : horaInicio.plusHours(1);
        this.motivo = motivo;
        this.estado = estado;
        this.sala = sala;
    }

    public Cita(String nombrePaciente, LocalDate fechaCita, LocalTime horaInicio, LocalTime horaFin, String motivo, EstadoCita estado, int sala) {
        this(0, nombrePaciente, fechaCita, horaInicio, horaFin, motivo, estado, sala);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombrePaciente() { return nombrePaciente; }
    public LocalDate getFechaCita() { return fechaCita; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public String getMotivo() { return motivo; }
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
    public int getSala() { return sala; }
}