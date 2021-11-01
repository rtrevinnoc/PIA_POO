package first;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Gerente extends Empleado {
	public List<Empleado> subordinados;
    public static DateTimeFormatter formatoDiaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es-ES"));

    // Hereda del empleado
	public Gerente(String nombre, String password, int edad, int[] horario, float salario, String sucursal, List<Empleado> subordinados) {
		super(nombre, password, edad, horario, salario, sucursal, true, null);
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
		
		// Se itera por sus subordinados
		for (Empleado empleado : subordinados) {
			// Si tiene el nombre de aquel al que se quiere asignar la tarea
			if (empleado.getNombre().equalsIgnoreCase(tarea.getEmpleado())) {
				horarioInicio = empleado.getHorario()[0];
				horarioFin = empleado.getHorario()[1];
				
				tareaInicio = tarea.getHorario()[0];
				tareaFin = tarea.getHorario()[1];
				
				// Se lee su horario, que esta como un int v.g 1600
				
				// Se cortan los dos digitos mayores
				if ((horarioInicio / 100) < 10) { // Si tiene un solo digito
					inicioHora = "0" + (horarioInicio / 100); // Se agrega un cero
				} else {
					inicioHora = String.valueOf(horarioInicio / 100); // Si no, se pasa igual
				}
				
				// Se repite el proceso anterior pero con la hora en que termina el horario del empleado
				if ((horarioFin / 100) < 10) {
					finHora = "0" + (horarioFin / 100);
				} else {
					finHora = String.valueOf(horarioFin / 100);
				}
				
				// Se cortan los ultimos dos digitos
				if (horarioInicio % 100 < 10) { // Si tiene un solo digito
					inicioMinutos = "0" + (horarioInicio % 100); // Se agrega un cero delante
				} else {
					inicioMinutos = String.valueOf(horarioInicio % 100);
				}
				
				// Se repite lo anterior, con el horario de salida
				if (horarioFin % 100 < 10) {
					finMinutos = "0" + (horarioFin % 100);
				} else {
					finMinutos = String.valueOf(horarioFin % 100);
				}
				
				// Se construye la fecha a partir de la fecha de la tarea, y se le agrega el horario del empleado, y se lee para convertirse a LocalDateTime
				dateInicio = LocalDateTime.parse(tareaInicio.getDayOfMonth() + "/" + tareaInicio.getMonthValue() + "/" + tareaInicio.getYear() + " " + inicioHora + ":" + inicioMinutos, formatoDiaHora);
				dateFin = LocalDateTime.parse(tareaFin.getDayOfMonth() + "/" + tareaFin.getMonthValue() + "/" + tareaFin.getYear() + " " + finHora + ":" + finMinutos, formatoDiaHora);
				
				// Se compara que el horario de la tarea este dentro del horario de trabajo del empleado
				if (tareaInicio.isAfter(dateInicio) && tareaFin.isBefore(dateFin)) {
					calendario.addTarea(tarea);	// Si esta, se agrega
				} else { // Si no esta, se notifica
					System.out.println("*** Debe ser dentro del horario del empleado de " + inicioHora + ":" + inicioMinutos + " a " + finHora + ":" + finMinutos + " ***");
				}
			}
		}
	}

	public void eliminarTarea(Calendario calendario, String nombreEmpleado, String nombreTarea) {
		for (Empleado empleado : subordinados) { // Se itera sobre los subordinados
			if (empleado.getNombre().equalsIgnoreCase(nombreEmpleado)) {
				calendario.delTarea(nombreEmpleado, nombreTarea); // Se verifica que el empleado exista, y sea subordinado del gerente
			}
		}
	}
	
	public void setSalario(String nombreEmpleado, float salario) {
		for (Empleado empleado : subordinados) { // Se itera sobre los subordinados
			if (empleado.getNombre().equalsIgnoreCase(nombreEmpleado)) { // Se verifica que el empleado exista y sea subordinado del gerente
				empleado.setSalario(salario); // Se cambia el salario
			}
		}
	}
	
	public void setHorario(String nombreEmpleado, int[] horario) {
		for (Empleado empleado : subordinados) {// Se itera sobre los subordinados
			if (empleado.getNombre().equalsIgnoreCase(nombreEmpleado)) {// Se verifica que el empleado exista y sea subordinado del gerente
				empleado.setHorario(horario); // Se cambia el horario
			}
		}
	}
	
	public void setSucursal(String nombreEmpleado, String sucursal) {
		for (Empleado empleado : subordinados) {// Se itera sobre los subordinados
			if (empleado.getNombre().equalsIgnoreCase(nombreEmpleado)) {// Se verifica que el empleado exista y sea subordinado del gerente
				empleado.setSucursal(sucursal); // Se cambia la sucursal
			}
		}		
	}
}
