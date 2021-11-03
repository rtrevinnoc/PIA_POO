package first;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Calendario {
	public ArrayList<Tarea> tareas = new ArrayList<Tarea>();
	
	public void mostrarTodasTareas(DateTimeFormatter formato) {
		// Se itera por todas las tareas y se muestran
		for (Tarea tarea : tareas) {
			System.out.println("\nDe " + tarea.getEmpleado() + ": " + tarea.getNombre() + "(" + tarea.getDescripcion() + ") empieza en " + tarea.getHorario()[0].format(formato) + " y termina en " + tarea.getHorario()[1].format(formato) + "\n");
		}
	}
	
	public void mostrarTodasTareasDe(String nombreEmpleado, DateTimeFormatter formato) {
		System.out.println("Tareas de " + nombreEmpleado);
		for (Tarea tarea : tareas) { // Se itera por las tareas
			if (tarea.getEmpleado().equalsIgnoreCase(nombreEmpleado)) { // Si son del empleado que se busca, se imprimen
				System.out.println("\n" + tarea.getNombre() + "(" + tarea.getDescripcion() + ") empieza en " + tarea.getHorario()[0].format(formato) + " y termina en " + tarea.getHorario()[1].format(formato) + "\n");
			}
		}
	}
	
	public void mostrarTareasPorDiaDe(String nombreEmpleado, LocalDateTime dia, DateTimeFormatter formato) {
		System.out.println("Tareas de " + nombreEmpleado + " en " + dia.format(formato));
		for (Tarea tarea : tareas) { // Se itera por las tareas, y si son del dia y empleado que se busca, se imprimen
			if (tarea.getEmpleado().equalsIgnoreCase(nombreEmpleado) && tarea.getHorario()[0].getDayOfYear() == dia.getDayOfYear()) {
				System.out.println(tarea.getNombre() + "(" + tarea.getDescripcion() + ") empieza en " + tarea.getHorario()[0].format(formato) + " y termina en " + tarea.getHorario()[1].format(formato));
			}
		}
	}
	
	public void addTarea(Tarea tarea) {
		tareas.add(tarea);
	}
	
	public void delTarea(String nombreEmpleado, String nombreTarea) {
		for (Tarea tarea : tareas) { // Se itera por todas las tareas, y si tiene el nombre y es del empleado buscado, se elimina
			if (tarea.getEmpleado().equalsIgnoreCase(nombreEmpleado) && tarea.getNombre().equalsIgnoreCase(nombreTarea)) {
				tareas.remove(tarea);
			}
		}	
	}
	
	public void wipeTareas() {
		tareas.clear();
	}
	
	public void completarTarea(String nombreEmpleado, String nombreTarea, LocalDateTime dia) {
		for (Tarea tarea : tareas) {
			if (tarea.getEmpleado().equalsIgnoreCase(nombreEmpleado) && tarea.getNombre().equalsIgnoreCase(nombreTarea) && tarea.getHorario()[0].equals(dia)) {
				tarea.completar();
				tareas.remove(tarea);
			}
		}
	}

	public ArrayList<Tarea> getTareas() {
		return tareas;
	}
}
