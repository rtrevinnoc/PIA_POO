package pia_poo;

import java.time.LocalDateTime;

public class Tarea {
	public String nombre, descripcion;
	public LocalDateTime[] horario;
	public String empleado;
	private boolean compleatado = false;
	
	// Constructor
	Tarea(String empleado, String nombre, LocalDateTime[] horario, String descripcion) {
		this.empleado = empleado;
		this.nombre = nombre;
		this.horario = horario;
		this.descripcion = descripcion;
	}

	// Getters/Setters
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDateTime[] getHorario() {
		return horario;
	}

	public void setHorario(LocalDateTime[] horario) {
		this.horario = horario;
	}

	public boolean isCompleatado() {
		return compleatado;
	}
	
	public void completar() {
		this.compleatado = true;
	}

	public String getEmpleado() {
		return empleado;
	}

	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}
}
