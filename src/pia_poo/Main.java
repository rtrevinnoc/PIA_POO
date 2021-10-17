package pia_poo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
	public static File registroEmpleados = new File("empleados.txt"), registroTareas = new File("tareas.txt"), registroVentas = new File("ventas.txt");
    public static DateTimeFormatter formatoDiaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es-ES")), formatoDia = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("es-ES"));
	public static Calendario calendario = new Calendario();
	public static Empleado actual = null;
	public static boolean inicioSesion = false;
    
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		ArrayList<Empleado> empleados = new ArrayList<Empleado>();
		ArrayList<Venta> historialVentas = new ArrayList<Venta>();
		String nombre, password, sucursal, nombreTarea, fechaInicio, fechaFin, descTarea, jefe;
		int edad, horaEntrada, horaSalida, cantidad;
		int[] horario;
		float salario, valorUnitario;
		boolean gerente;//, inicioSesion = false;
		Empleado empleado;//, actual = null;
		Gerente supervisor = null;
		LocalDateTime dateInicio, dateFin;
        Tarea tarea;
		
        System.out.print("Introduzca su nombre -> ");
        nombre = sc.nextLine();
        System.out.print("Introduzca su contraseña -> ");
        password = sc.nextLine();
		
        cargarEmpleados(nombre, password, empleados);
        
        if (!inicioSesion) {
            System.out.println("No existe el empleado. Conteste las siguiente preguntas");
            
            System.out.print("Introduzca su edad -> ");
            edad = sc.nextInt();
            System.out.print("Introduzca su horario de entrada en formato militar (v.g. 1600) -> ");
            horaEntrada = sc.nextInt();
            System.out.print("Introduzca su horario de salida en formato militar (v.g. 1600) -> ");
            horaSalida = sc.nextInt();
            horario = new int[]{horaEntrada, horaSalida};
            System.out.print("Introduzca su salario -> ");
            salario = sc.nextFloat();
            sc.nextLine();
            System.out.print("Introduzca su sucursal -> ");
            sucursal = sc.nextLine();
            System.out.print("¿Es usted gerente? [Si/No] -> ");
            if (sc.nextLine().equalsIgnoreCase("si")) {
            	gerente = true;
            	jefe = null;
            } else {
            	gerente = false;
                System.out.print("Introduzca el nombre de su jefe -> ");
                jefe = sc.nextLine();
            }
            
            empleado = new Empleado(nombre,
        			password,
        			edad,
        			horario,
        			salario,
        			sucursal,
        			gerente,
        			jefe
        	);
            actual = empleado;
            empleados.add(empleado);
            inicioSesion = true;
            guardarEmpleado(empleado);
            
        }
        
        if (actual.isGerente()) {
        	empleados.remove(actual);
        	List<Empleado> subordinados = new ArrayList<Empleado>();
        	
        	for (Empleado cur : empleados) {
        		if (cur.getJefe().equalsIgnoreCase(actual.getNombre())) {
        			subordinados.add(cur);
        		}
        	}
            supervisor = new Gerente(actual.getNombre(),
            			actual.getPassword(),
            			actual.getEdad(),
            			actual.getHorario(),
            			actual.getSalario(),
            			actual.getSucursal(),
            			subordinados
        			);
        }
        
        cargarTareas(calendario);
        
        cargarVentas(historialVentas);
        
        System.out.println("Ha iniciado sesión como " + actual.getNombre());
        imprimirMenu();
        
        while (true) {
        	System.out.print("Introduzca una opcion -> ");
	        switch(sc.nextInt()) {
	        	default:
	                imprimirMenu();
	        		break;
	        	case 0:
	        		sc.close();
	        		System.exit(0);
	        		break;
	        	case 1:
	        		System.out.println("*** Mi Calendario ***");
	        		calendario.mostrarTodasTareasDe(actual.getNombre(), formatoDiaHora);
	        		break;
	        	case 2:
	        		System.out.println("*** Calendario General ***");
	        		calendario.mostrarTodasTareas(formatoDiaHora);
	        		break;
	        	case 3:
	        		System.out.println("*** Lista de Empleados ***");
	        		for (Empleado cur: empleados) {
	        			System.out.println("\nNombre: " + cur.getNombre());
	        			System.out.println("Edad: " + cur.getEdad());
	        			System.out.println("Horario: " + cur.getHorario()[0] + "-" + cur.getHorario()[1]);
	        			System.out.println("Salario: " + cur.getSalario());
	        			System.out.println("Sucursal: " + cur.getSucursal());
	        			System.out.println("Gerente: " + cur.getJefe() + "\n");
	        		}
	        		break;
	        	case 4:
	        		System.out.println("*** Registrar Venta ***");
	        		sc.nextLine();
	        		
	                System.out.print("Introduzca el nombre de articulo -> ");
	                nombre = sc.nextLine();
	                
	                System.out.print("Introduzca el precio (unitario) -> ");
	                valorUnitario = sc.nextFloat();
	                
	                System.out.print("Introduzca la cantidad vendida -> ");
	                cantidad = sc.nextInt();
	                
	                Venta nuevaVenta = new Venta(actual.getNombre(), nombre, valorUnitario, cantidad);
	        		historialVentas.add(nuevaVenta);
	        		guardarVenta(nuevaVenta);
	        		break;
	        	case 5:
	        		System.out.println("*** Listar Ventas ***");
	        		sc.nextLine();
	        		
	        		for (Venta venta : historialVentas) {
	        			System.out.println("\nVenta por " + venta.getNombreEmpleado() + " de " + venta.getCantidad() + " " + venta.getNombreArticulo() + " a " + venta.getValorUnitario() + "$ pza. Total: " + venta.getValorTotal() + "$");
	        		}
	        		
	        		break;
	        	case 6:
	        		System.out.println("*** Listar mis Ventas ***");

	        		for (Venta venta : historialVentas) {
	        			if (venta.getNombreEmpleado().equalsIgnoreCase(actual.getNombre())) {
		        			System.out.println("\nVenta de " + venta.getCantidad() + " " + venta.getNombreArticulo() + " a " + venta.getValorUnitario() + "$ pza. Total: " + venta.getValorTotal() + "$");
	        			}
	        		}
	        		
	        		break;
	        	case 7:
	        		if (!actual.isGerente()) {
	        			System.out.println("*** No es gerente ***");
	        			break;
	        		}
	        		
	        		System.out.println("*** Registrar Empleado ***");
	        		sc.nextLine();
	        		
	                System.out.print("Introduzca el nombre -> ");
	                nombre = sc.nextLine();
	                System.out.print("Introduzca la contraseña -> ");
	                password = sc.nextLine();
	                System.out.print("Introduzca la edad -> ");
	                edad = sc.nextInt();
	                System.out.print("Introduzca el horario de entrada en formato militar (v.g. 1600) -> ");
	                horaEntrada = sc.nextInt();
	                System.out.print("Introduzca el horario de salida en formato militar (v.g. 1600) -> ");
	                horaSalida = sc.nextInt();
	                horario = new int[]{horaEntrada, horaSalida};
	                System.out.print("Introduzca el salario -> ");
	                salario = sc.nextFloat();
	                sc.nextLine();
	                System.out.print("Introduzca la sucursal -> ");
	                sucursal = sc.nextLine();
	                
	                empleado = new Empleado(nombre,
	            			password,
	            			edad,
	            			horario,
	            			salario,
	            			sucursal,
	            			false,
	            			supervisor.getNombre()
	            	);
	                empleados.add(empleado);
	                supervisor.addSubordinado(empleado);
	                guardarEmpleado(empleado);
	                
	                System.out.println("\nSe registró a " + nombre);
	        		break;
	        	case 8:
	        		if (!actual.isGerente()) {
	        			System.out.println("*** No es gerente ***");
	        			break;
	        		}
	        		
	        		System.out.println("*** Programar Tarea ***");
	        		sc.nextLine();
	        		
	                System.out.print("Introduzca el nombre del empleado -> ");
	                nombre = sc.nextLine();
	                
	                System.out.print("Introduzca el nombre de la tarea -> ");
	                nombreTarea = sc.nextLine();
	                
	                System.out.print("Introduzca fecha de inicio de la tarea (v.g. 16/10/2021 15:00) -> ");
	                fechaInicio = sc.nextLine();
	                System.out.print("Introduzca fecha de fin de la tarea (v.g. 16/10/2021 15:00) -> ");
	                fechaFin = sc.nextLine();
	                
	                dateInicio = LocalDateTime.parse(fechaInicio, formatoDiaHora);
	                dateFin = LocalDateTime.parse(fechaFin, formatoDiaHora);
	                
	                System.out.print("Introduzca descripción de la tarea -> ");
	                descTarea = sc.nextLine();
	                
	                tarea = new Tarea(nombre, nombreTarea, new LocalDateTime[] {dateInicio, dateFin}, descTarea);
	                supervisor.asignarTarea(calendario, tarea);
	        		guardarTarea(tarea);
	        		break;
	        	case 9:
	        		if (!actual.isGerente()) {
	        			System.out.println("*** No es gerente ***");
	        			break;
	        		}
	        		
	        		System.out.println("*** Eliminar Tarea ***");
	        		sc.nextLine();
	        		
	                System.out.print("Introduzca el nombre del empleado -> ");
	                nombre = sc.nextLine();
	                
	                System.out.print("Introduzca el nombre de la tarea -> ");
	                nombreTarea = sc.nextLine();
	                
	                eliminarTarea(nombre, nombreTarea);
	                supervisor.eliminarTarea(calendario, nombre, nombreTarea);
	        		break;
	        	case 10:
	        		if (!actual.isGerente()) {
	        			System.out.println("*** No es gerente ***");
	        			break;
	        		}
	        		
	        		System.out.println("*** Ver Calendario Por Dia de Empleado ***");
	        		sc.nextLine();
	        		
	                System.out.print("Introduzca el nombre del empleado -> ");
	                nombre = sc.nextLine();
	                
	                System.out.print("Introduzca fecha de la tarea (v.g. 16/10/2021) -> ");
	                fechaInicio = sc.nextLine() + " 00:00";
	                dateInicio = LocalDateTime.parse(fechaInicio, formatoDiaHora);
	                
	        		calendario.mostrarTareasPorDiaDe(nombre, dateInicio, formatoDiaHora);
	        		break;
	        	case 11:
	        		if (!actual.isGerente()) {
	        			System.out.println("*** No es gerente ***");
	        			break;
	        		}
	        		
	        		System.out.println("*** Ver Calendario Completo de Empleado ***");
	        		sc.nextLine();
	        		
	                System.out.print("Introduzca el nombre del empleado -> ");
	                nombre = sc.nextLine();
	                
	        		calendario.mostrarTodasTareasDe(nombre, formatoDiaHora);
	        		break;
	        	case 12:
	        		if (!actual.isGerente()) {
	        			System.out.println("*** No es gerente ***");
	        			break;
	        		}
	        		
	        		System.out.println("*** Ver Calendario Completo de Empleado ***");
	        		sc.nextLine();
	        		
	                System.out.print("Introduzca el nombre del empleado -> ");
	                nombre = sc.nextLine();
	                
	                empleado = null;
	                for (Empleado cur: empleados) {
	                	if (cur.getNombre().equalsIgnoreCase(nombre)) {
	                		empleado = cur;
	                	}
	                }
	                
	                if (empleado == null) {
		        		System.out.println("*** No se encontro el empleado ***");
	                	break;
	                }
	        		
	                System.out.println("*** Campos Disponibles ***");
	                System.out.println("\t1) Horario");
	                System.out.println("\t1) Salario");
	                System.out.println("\t1) Sucursal");
	                System.out.print("Seleccione el campo a cambiar -> ");
	                switch(sc.nextInt()) {
	                	case 1:
	    	                System.out.print("Introduzca el horario de entrada en formato militar (v.g. 1600) -> ");
	    	                horaEntrada = sc.nextInt();
	    	                System.out.print("Introduzca el horario de salida en formato militar (v.g. 1600) -> ");
	    	                horaSalida = sc.nextInt();
	    	                empleado.setHorario(new int[]{horaEntrada, horaSalida});
	                		break;
	                	case 2:
	    	                System.out.print("Introduzca el nuevo salario del empleado -> ");
	    	                empleado.setSalario(sc.nextFloat());
	                		break;
	                	case 3:
	    	                System.out.print("Introduzca la nueva sucursal del empleado -> ");
	    	                empleado.setSucursal(sc.nextLine());
	                		break;
	                };
	                
	        		calendario.mostrarTodasTareasDe(nombre, formatoDiaHora);
	        		break;
	        	case 13:
	        		System.out.println("*** Listar Ventas de Empleado ***");

	        		System.out.print("Introduzca el nombre del empleado -> ");
	                nombre = sc.nextLine();
	        		
	        		for (Venta venta : historialVentas) {
	        			if (venta.getNombreEmpleado().equalsIgnoreCase(nombre)) {
		        			System.out.println("\nVenta de " + venta.getCantidad() + " " + venta.getNombreArticulo() + " a " + venta.getValorUnitario() + "$ pza. Total: " + venta.getValorTotal() + "$");
	        			}
	        		}
	        		
	        		break;
	        }
        }
        
	}
	
	public static void imprimirMenu() {
        System.out.println("*** MENU ***");
        System.out.println("\t0) Salir");
        System.out.println("\t1) Ver Mi Calendario");
        System.out.println("\t2) Ver Calendario General");
        System.out.println("\t3) Listar Empleados");
        System.out.println("\t4) Registrar Venta");
        System.out.println("\t5) Listar Ventas");
        System.out.println("\t6) Listar Mis Venta");
        System.out.println("\tPara Gerentes...");
        System.out.println("\t7) Registrar Empleado");
        System.out.println("\t8) Programar Tarea a Empleado");
        System.out.println("\t9) Eliminar Tarea a Empleado");
        System.out.println("\t10) Ver Calendario Por Dia de Empleado");
        System.out.println("\t11) Ver Calendario Completo de Empleado");
        System.out.println("\t12) Actualizar Informacion de Empleado");
        System.out.println("\t13) Listar Ventas por Empleado");
	}
	
	public static void cargarEmpleados(String nombre, String password, ArrayList<Empleado> empleados) {
		Empleado empleado;
		
        if (registroEmpleados.exists()) {
            try{
                Scanner rd = new Scanner(registroEmpleados);
                while (rd.hasNextLine()) {
                    String[] data = rd.nextLine().split("\t", 8);
                    empleado = new Empleado(data[0],
                    			data[1],
                    			Integer.parseInt(data[2]),
                    			Stream.of(data[3].split(",", 3)).mapToInt(Integer::parseInt).toArray(),
                    			Float.parseFloat(data[4]),
                    			data[5],
                    			Boolean.parseBoolean(data[6]),
                    			data[7]
                    		);
                    empleados.add(empleado);
                    
                    if (empleado.getNombre().equalsIgnoreCase(nombre) && empleado.checkPassword(password)) {
                    	actual = empleado;
                    	inicioSesion = true;
                    }
                }
                rd.close();
            } catch(IOException ex){
            	ex.printStackTrace();
            }
        } else {
            try {
                System.out.println("No existen registros. Creando archivo...");
                registroEmpleados.createNewFile();                
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
	}
	
	public static void cargarVentas(ArrayList<Venta> historialVentas) {
        if (registroVentas.exists()) {
            try{
                Scanner rd = new Scanner(registroVentas);
                while (rd.hasNextLine()) {
                    String[] data = rd.nextLine().split("\t", 5);
                    
                    historialVentas.add(new Venta(data[0],
                			data[1],
                			Float.parseFloat(data[2]),
                			Integer.parseInt(data[3])
                		));                    
                }
                rd.close();
            } catch(IOException ex){
                ex.printStackTrace();
            }
        } else {
            try {
                System.out.println("No existen registros. Creando archivo...");
                registroVentas.createNewFile();        
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
	}
	
	public static void cargarTareas(Calendario calendario) {
		LocalDateTime dateInicio, dateFin;
		Tarea tarea;
		
        if (registroTareas.exists()) {
            try{
                Scanner rd = new Scanner(registroTareas);
                while (rd.hasNextLine()) {
                    String[] data = rd.nextLine().split("\t", 5);
                    String[] horas = data[2].split(",", 3);
                    dateInicio = LocalDateTime.parse(horas[0], formatoDiaHora);
	                dateFin = LocalDateTime.parse(horas[1], formatoDiaHora);
                    tarea = new Tarea(data[0],
                    			data[1],
                    			new LocalDateTime[] {dateInicio, dateFin},
                    			data[3]
                    		);
                    calendario.addTarea(tarea);
                }
                rd.close();
            } catch(IOException ex){
            	ex.printStackTrace();
            }
        } else {
            try {
                System.out.println("No existen registros. Creando archivo...");
                registroTareas.createNewFile();                
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
	}
	
	public static void guardarEmpleado(Empleado empleado) {
    	BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(
                new FileWriter(registroEmpleados, true)
            );
            bw.write(empleado.getNombre() + "\t" +
                		empleado.getPassword() + "\t" +
                		empleado.getEdad() + "\t" +
                		empleado.getHorario()[0] + "," + empleado.getHorario()[1] + "\t" +
                		empleado.getSalario() + "\t" +
               			empleado.getSucursal() + "\t" +
               			false + "\t" +
               			empleado.getJefe() + "\n"
            );   
        } catch(IOException ex){
            ex.printStackTrace();
        } finally 	{ 
        	try {
        	    if(bw != null) bw.close();
        	} catch(Exception ex){
        	       ex.printStackTrace();
        	}
        }
	}
	
	public static void guardarTarea(Tarea tarea) {
    	BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(
                new FileWriter(registroTareas, true)
            );
            bw.write(tarea.getEmpleado() + "\t" +
                		tarea.getNombre() + "\t" +
                		tarea.getHorario()[0].format(formatoDiaHora) + "," + tarea.getHorario()[1].format(formatoDiaHora) + "\t" +
               			tarea.getDescripcion() + "\n"
            );   
        } catch(IOException ex){
            ex.printStackTrace();
        } finally 	{ 
        	try {
        	    if(bw != null) bw.close();
        	} catch(Exception ex){
        	       ex.printStackTrace();
        	}
        }
	}
	
	public static void eliminarTarea(String nombre, String nombreTarea) {
    	BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(
                new FileWriter(registroTareas, false)
            );
    		for (Tarea tarea : calendario.getTareas()) {
    			if (tarea.getEmpleado().equals(nombre) && tarea.getNombre().equals(nombreTarea)) {
    				continue;
    			}
                bw.write(tarea.getEmpleado() + "\t" +
                		tarea.getNombre() + "\t" +
                		tarea.getHorario()[0].format(formatoDiaHora) + "," + tarea.getHorario()[1].format(formatoDiaHora) + "\t" +
               			tarea.getDescripcion() + "\n"
                );
    		}
        } catch(IOException ex){
            ex.printStackTrace();
        } finally 	{ 
        	try {
        	    if(bw != null) bw.close();
        	} catch(Exception ex){
        	       ex.printStackTrace();
        	}
        }
	}
	
	public static void guardarVenta(Venta venta) {
    	BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(
                new FileWriter(registroVentas, true)
            );
            bw.write(venta.getNombreEmpleado() + "\t" +
                		venta.getNombreArticulo() + "\t" +
                		venta.getValorUnitario() + "\t" +
               			venta.getCantidad() + "\n"
            );   
        } catch(IOException ex){
            ex.printStackTrace();
        } finally 	{ 
        	try {
        	    if(bw != null) bw.close();
        	} catch(Exception ex){
        	       ex.printStackTrace();
        	}
        }
	}

}
