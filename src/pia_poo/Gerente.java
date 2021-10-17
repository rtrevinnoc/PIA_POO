package pia_poo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Gerente extends Empleado {
	public List<Empleado> subordinados;
    public static DateTimeFormatter formatoDiaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es-ES"));

	Gerente(String nombre, String password, int edad, int[] horario, float salario, String sucursal, List<Empleado> subordinados) {
		super(nombre, password, edad, horario, salario, sucursal, true, null);
		// TODO Auto-generated constructor stub
		this.subordinados = subordinados;
	}
	
	public List<Empleado> getSubordinados() {
		return subordinados;
	}
	public void addSubordinado(Empleado subordinado) {
		this.subordinados.add(subordinado);
	}
	
	public void asignarTarea(Calendario calendario, Tarea tarea) {
		int horarioInicio, horarioFin;
		LocalDateTime tareaInicio, tareaFin, dateInicio, dateFin;
		String inicioHora, finHora, inicioMinutos, finMinutos;
		
		for (Empleado empleado : subordinados) {
			if (empleado.getNombre().equalsIgnoreCase(tarea.getEmpleado())) {
				horarioInicio = empleado.getHorario()[0];
				horarioFin = empleado.getHorario()[1];
				
				tareaInicio = tarea.getHorario()[0];
				tareaFin = tarea.getHorario()[1];
				
				if ((horarioInicio / 100) < 10) {
					inicioHora = "0" + (horarioInicio / 100);
				} else {
					inicioHora = String.valueOf(horarioInicio / 100);
				}
				
				if ((horarioFin / 100) < 10) {
					finHora = "0" + (horarioFin / 100);
				} else {
					finHora = String.valueOf(horarioFin / 100);
				}
				
				if (horarioInicio % 100 == 0) {
					inicioMinutos = "00";
				} else {
					inicioMinutos = String.valueOf(horarioInicio % 100);
				}
				
				if (horarioFin % 100 == 0) {
					finMinutos = "00";
				} else {
					finMinutos = String.valueOf(horarioFin % 100);
				}
				
				dateInicio = LocalDateTime.parse(tareaInicio.getDayOfMonth() + "/" + tareaInicio.getMonthValue() + "/" + tareaInicio.getYear() + " " + inicioHora + ":" + inicioMinutos, formatoDiaHora);
				dateFin = LocalDateTime.parse(tareaFin.getDayOfMonth() + "/" + tareaFin.getMonthValue() + "/" + tareaFin.getYear() + " " + finHora + ":" + finMinutos, formatoDiaHora);
				
				if (tareaInicio.isAfter(dateInicio) && tareaFin.isBefore(dateFin)) {
					calendario.addTarea(tarea);	
				} else {
					System.out.println("*** Debe ser dentro del horario del empleado de " + inicioHora + ":" + inicioMinutos + " a " + finHora + ":" + finMinutos + " ***");
				}
			}
		}
	}

	public void eliminarTarea(Calendario calendario, String nombreEmpleado, String nombreTarea) {
		for (Empleado empleado : subordinados) {
			if (empleado.getNombre().equalsIgnoreCase(nombreEmpleado)) {
				calendario.delTarea(nombreEmpleado, nombreTarea);
			}
		}
	}
	
	public void setSalario(String nombreEmpleado, float salario) {
		for (Empleado empleado : subordinados) {
			if (empleado.getNombre().equalsIgnoreCase(nombreEmpleado)) {
				empleado.setSalario(salario);
			}
		}
	}
	
	public void setHorario(String nombreEmpleado, int[] horario) {
		for (Empleado empleado : subordinados) {
			if (empleado.getNombre().equalsIgnoreCase(nombreEmpleado)) {
				empleado.setHorario(horario);
			}
		}
	}
	
	public void setSucursal(String nombreEmpleado, String sucursal) {
		for (Empleado empleado : subordinados) {
			if (empleado.getNombre().equalsIgnoreCase(nombreEmpleado)) {
				empleado.setSucursal(sucursal);
			}
		}		
	}
}
