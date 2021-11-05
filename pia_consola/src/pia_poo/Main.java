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
	// Declaramos los archivos que usaremos en todos los metodos
	public static File registroEmpleados = new File("empleados.txt"), registroTareas = new File("tareas.txt"), registroVentas = new File("ventas.txt");
    // Declaramos los objetos para formatear y leer fechas
	public static DateTimeFormatter formatoDiaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es-ES")), formatoDia = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("es-ES"));
	public static Calendario calendario = new Calendario();
	public static Empleado actual = null;
	public static boolean inicioSesion = false;
    
	public static void main(String[] args) {
		// Scanner para leer informacion de la consola
		Scanner sc = new Scanner(System.in);
		// Listas de empleados y ventas
		ArrayList<Empleado> empleados = new ArrayList<Empleado>();
		ArrayList<Venta> historialVentas = new ArrayList<Venta>();
		// Variables auxiliares para leer y crear los elementos de las listas
		String nombre, password, sucursal, nombreTarea, fechaInicio, fechaFin, descTarea, jefe;
		int edad, horaEntrada, horaSalida, cantidad;
		int[] horario;
		float salario, valorUnitario;
		boolean gerente;
		Empleado empleado;
		Gerente supervisor = null;
		LocalDateTime dateInicio, dateFin;
        Tarea tarea;
		
        // Leemos la informacion de inicio de sesion
        System.out.print("Introduzca su nombre -> ");
        nombre = sc.nextLine();
        System.out.print("Introduzca su contraseña -> ");
        password = sc.nextLine();
		
        // Se cargan los empleados, y si es posible, se inicia sesion con uno de ellos
        cargarEmpleados(nombre, password, empleados);
        
        // Si no se ha podido iniciar sesion
        if (!inicioSesion) {
            System.out.println("No existe el empleado. Conteste las siguiente preguntas");
            
            // Se lee la informacion para crear el nuevo empleado
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
            
            // Se crea el objeto del nuevo empleado
            empleado = new Empleado(nombre,
        			password,
        			edad,
        			horario,
        			salario,
        			sucursal,
        			gerente,
        			jefe
        	);
            
            // Se inicia sesion con el y se inserta en la lista
            actual = empleado;
            empleados.add(empleado);
            inicioSesion = true;
            // Se guarda el nuevo empleado en la lista
            guardarEmpleado(empleado);
            
        }
        
        // Si el empleado que inicio sesion es gerente
        if (actual.isGerente()) {
        	// Se elimina el empleado actual de la lista de empleados
        	empleados.remove(actual);
        	// Se crea la lista de subordinados con todos menos el empleado actual
        	List<Empleado> subordinados = new ArrayList<Empleado>();
        	for (Empleado cur : empleados) {
        		if (cur.getJefe().equalsIgnoreCase(actual.getNombre())) {
        			subordinados.add(cur);
        		}
        	}
        	
        	// se crea el objeto del gerente
            supervisor = new Gerente(actual.getNombre(),
            			actual.getPassword(),
            			actual.getEdad(),
            			actual.getHorario(),
            			actual.getSalario(),
            			actual.getSucursal(),
            			subordinados
        			);
        }
        
        // Se cargan las tareas y las ventas desde sus archivos
        cargarTareas(calendario);
        cargarVentas(historialVentas);
        
        // Se indica el inicio de sesion
        System.out.println("Ha iniciado sesión como " + actual.getNombre());
        // Se imprime el menu
        imprimirMenu();
        
        while (true) {
        	System.out.print("Introduzca una opcion -> ");
	        switch(sc.nextInt()) {
	        	default: // En cualquier opcion no reconocida, se imprime el menu
	                imprimirMenu();
	        		break;
	        	case 0: // Se cierra el programa
	        		sc.close();
	        		
	        		// Se añade el empleado que ha iniciado sesion, si es gerente, de vuelta a la lista
	        		if (actual.isGerente()) {
	        			empleados.add(actual);
	        		}
	        		// Los cambios que se hayan realizado, se escriben al archivo
	        		sobreescribirEmpleados(empleados);
	        		
	        		System.exit(0);
	        		break;
	        	case 1:
	        		System.out.println("*** Mi Calendario ***");
	        		// Se muestra el calendario de el empleado actual
	        		calendario.mostrarTodasTareasDe(actual.getNombre(), formatoDiaHora);
	        		break;
	        	case 2:
	        		System.out.println("*** Calendario General ***");
	        		// Se muestran todas las tareas de todos los empleados
	        		calendario.mostrarTodasTareas(formatoDiaHora);
	        		break;
	        	case 3:
	        		System.out.println("*** Lista de Empleados ***");
	        		// Se listan los empleados
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
	        		sc.nextLine(); // Se come un salto de linea por si se empleo un sc.nextInt o nextFloat antes
	        		
	                System.out.print("Introduzca el nombre de articulo -> ");
	                nombre = sc.nextLine();
	                
	                System.out.print("Introduzca el precio (unitario) -> ");
	                valorUnitario = sc.nextFloat();
	                
	                System.out.print("Introduzca la cantidad vendida -> ");
	                cantidad = sc.nextInt();
	                
	                // Se crea el objeto con la informacion leida de la venta
	                Venta nuevaVenta = new Venta(actual.getNombre(), nombre, valorUnitario, cantidad);
	                // Se añade a la lista de ventas
	        		historialVentas.add(nuevaVenta);
	        		// Se guarda en el archivo de ventas
	        		guardarVenta(nuevaVenta);
	        		break;
	        	case 5:
	        		System.out.println("*** Listar Ventas ***");
	        		sc.nextLine(); // Se come un salto de linea por si se empleo un sc.nextInt o nextFloat antes
	        		
	        		// Se muestran las ventas de todos los empleados
	        		for (Venta venta : historialVentas) {
	        			System.out.println("\nVenta por " + venta.getNombreEmpleado() + " de " + venta.getCantidad() + " " + venta.getNombreArticulo() + " a " + venta.getValorUnitario() + "$ pza. Total: " + venta.getValorTotal() + "$");
	        		}
	        		
	        		break;
	        	case 6:
	        		System.out.println("*** Listar mis Ventas ***");

	        		// Se muestran las ventas que correspondan al empleado actual
	        		for (Venta venta : historialVentas) {
	        			if (venta.getNombreEmpleado().equalsIgnoreCase(actual.getNombre())) {
		        			System.out.println("\nVenta de " + venta.getCantidad() + " " + venta.getNombreArticulo() + " a " + venta.getValorUnitario() + "$ pza. Total: " + venta.getValorTotal() + "$");
	        			}
	        		}
	        		
	        		break;
	        	case 7:
	        		// Se verifica que sea gerente
	        		if (!actual.isGerente()) {
	        			System.out.println("*** No es gerente ***");
	        			break;
	        		}
	        		
	        		System.out.println("*** Registrar Empleado ***");
	        		sc.nextLine(); // Se come un salto de linea por si se empleo un sc.nextInt o nextFloat antes

	        		// Se lee la informacion del nuevo empleado
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
	                
	                // Se crea el objeto del nuevo empleado
	                empleado = new Empleado(nombre,
	            			password,
	            			edad,
	            			horario,
	            			salario,
	            			sucursal,
	            			false,
	            			supervisor.getNombre()
	            	);
	                
	                // Se añade a la lista de empleados
	                empleados.add(empleado);
	                // Se añade a los subordinados del gerente que lo esta registrando
	                supervisor.addSubordinado(empleado);
	                // Se guarda en el archivo
	                guardarEmpleado(empleado);
	                
	                System.out.println("\nSe registró a " + nombre);
	        		break;
	        	case 8:
	        		// Se verifica el estatus de gerente
	        		if (!actual.isGerente()) {
	        			System.out.println("*** No es gerente ***");
	        			break;
	        		}
	        		
	        		System.out.println("*** Programar Tarea ***");
	        		sc.nextLine(); // Se come un salto de linea por si se empleo un sc.nextInt o nextFloat antes

	        		// Se lee la informacion para registrar la tarea
	                System.out.print("Introduzca el nombre del empleado -> ");
	                nombre = sc.nextLine();
	                
	                System.out.print("Introduzca el nombre de la tarea -> ");
	                nombreTarea = sc.nextLine();
	                
	                System.out.print("Introduzca fecha de inicio de la tarea (v.g. 16/10/2021 15:00) -> ");
	                fechaInicio = sc.nextLine();
	                System.out.print("Introduzca fecha de fin de la tarea (v.g. 16/10/2021 15:00) -> ");
	                fechaFin = sc.nextLine();
	                
	                // Se convierten las cadenas a LocalDateTime siguiendo el formateador del inicio
	                dateInicio = LocalDateTime.parse(fechaInicio, formatoDiaHora);
	                dateFin = LocalDateTime.parse(fechaFin, formatoDiaHora);
	                
	                System.out.print("Introduzca descripción de la tarea -> ");
	                descTarea = sc.nextLine();
	                
	                // Se crea el objeto de tarea
	                tarea = new Tarea(nombre, nombreTarea, new LocalDateTime[] {dateInicio, dateFin}, descTarea);
	                // Se añade la tarea al calendario
	                supervisor.asignarTarea(calendario, tarea);
	                // Se guarda la tarea en el archivo
	        		guardarTarea(tarea);
	        		break;
	        	case 9:
	        		// Se verifica que sea gerente
	        		if (!actual.isGerente()) {
	        			System.out.println("*** No es gerente ***");
	        			break;
	        		}
	        		
	        		System.out.println("*** Eliminar Tarea ***");
	        		sc.nextLine(); // Se limpian los saltos de linea de la entrada
	        		
	        		// Se lee la informacion para eliminar una tarea
	                System.out.print("Introduzca el nombre del empleado -> ");
	                nombre = sc.nextLine();
	                
	                System.out.print("Introduzca el nombre de la tarea -> ");
	                nombreTarea = sc.nextLine();
	                
	                // Se elimina la tarea del archivo
	                eliminarTarea(nombre, nombreTarea);
	                // Se elimina la tarea de la lista que esta cargada
	                supervisor.eliminarTarea(calendario, nombre, nombreTarea);
	        		break;
	        	case 10:
	        		// Se verifica que sea gerente
	        		if (!actual.isGerente()) {
	        			System.out.println("*** No es gerente ***");
	        			break;
	        		}
	        		
	        		System.out.println("*** Ver Calendario Por Dia de Empleado ***");
	        		sc.nextLine(); // Se limpia la stdin
	        		
	        		// Se lee la informacion para filtrar tareas
	                System.out.print("Introduzca el nombre del empleado -> ");
	                nombre = sc.nextLine();
	                
	                System.out.print("Introduzca fecha de la tarea (v.g. 16/10/2021) -> ");
	                fechaInicio = sc.nextLine() + " 00:00"; // Se añade la primer hora del dia para no causar error al leerla
	                dateInicio = LocalDateTime.parse(fechaInicio, formatoDiaHora); // Se interpreta la fecha
	                
	        		calendario.mostrarTareasPorDiaDe(nombre, dateInicio, formatoDiaHora);
	        		break;
	        	case 11:
	        		// Se verifica que sea gerente
	        		if (!actual.isGerente()) {
	        			System.out.println("*** No es gerente ***");
	        			break;
	        		}
	        		
	        		System.out.println("*** Ver Calendario Completo de Empleado ***");
	        		sc.nextLine(); // Se limpia stdin
	        		
	                System.out.print("Introduzca el nombre del empleado -> ");
	                nombre = sc.nextLine();
	                
	                // Se muestran las tareas del empleado leido
	        		calendario.mostrarTodasTareasDe(nombre, formatoDiaHora);
	        		break;
	        	case 12:
	        		// Se verifica si es gerente
	        		if (!actual.isGerente()) {
	        			System.out.println("*** No es gerente ***");
	        			break;
	        		}
	        		
	        		// Se lee la informacion del empleado en cuestion
	        		System.out.println("*** Actualizar Informacion de Empleado ***");
	        		sc.nextLine(); // Se limpia STDIN
	        		
	                System.out.print("Introduzca el nombre del empleado -> ");
	                nombre = sc.nextLine();
	                
	                // Se busca el empleado que cumpla los requisitos
	                empleado = null;
	                for (Empleado cur: empleados) {
	                	if (cur.getNombre().equalsIgnoreCase(nombre)) {
	                		empleado = cur;
	                	}
	                }
	                
	                // Si no se encuentra, se notifica
	                if (empleado == null) {
		        		System.out.println("*** No se encontro el empleado ***");
	                	break;
	                }
	        		
	                // Se imprime un nuevo menu
	                System.out.println("*** Campos Disponibles ***");
	                System.out.println("\t1) Horario");
	                System.out.println("\t2) Salario");
	                System.out.println("\t3) Sucursal");
	                System.out.print("Seleccione el campo a cambiar -> ");
	                switch(sc.nextInt()) {
	                	case 1:
	                		// Permite cambiar el horario, se lee la informacion
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
	                		sc.nextLine(); // Se limpia STDIN
	    	                System.out.print("Introduzca la nueva sucursal del empleado -> ");
	    	                empleado.setSucursal(sc.nextLine());
	                		break;
	                };
	                
	        		break;
	        	case 13:
	        		System.out.println("*** Listar Ventas de Empleado ***");
	        		sc.nextLine(); // Se limpia STDIN

	        		System.out.print("Introduzca el nombre del empleado -> ");
	                nombre = sc.nextLine();
	        		
	                // Se muestran las ventas que cumplan con el nombre del empleado que se busca
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
		
		// Si existe el archivo
        if (registroEmpleados.exists()) {
            try{
            	// Se crea un escanner
                Scanner rd = new Scanner(registroEmpleados);
                // Se leen las lineas del archivo
                while (rd.hasNextLine()) {
                	// Se separan los campos
                    String[] data = rd.nextLine().split("\t", 8);
                    // Se crea el objeto
                    empleado = new Empleado(data[0],
                    			data[1],
                    			Integer.parseInt(data[2]),
                    			Stream.of(data[3].split(",", 3)).mapToInt(Integer::parseInt).toArray(),
                    			Float.parseFloat(data[4]),
                    			data[5],
                    			Boolean.parseBoolean(data[6]),
                    			data[7]
                    		);
                    // Se añade a la lista
                    empleados.add(empleado);
                    
                    // Si las credenciales de alguno coinciden, se inicia sesion como el
                    if (empleado.getNombre().equalsIgnoreCase(nombre) && empleado.checkPassword(password)) {
                    	actual = empleado;
                    	inicioSesion = true;
                    }
                }
                rd.close(); // se cierra el escaner
            } catch(IOException ex){
            	ex.printStackTrace();
            }
        } else { // Si no existe, se crea el archivo y se notifica
            try {
                System.out.println("No existen registros. Creando archivo...");
                registroEmpleados.createNewFile();                
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
	}
	
	public static void cargarVentas(ArrayList<Venta> historialVentas) {
		// Se verifica que existe un archivo de ventas
        if (registroVentas.exists()) {
            try{
            	// Se crea un escanner
                Scanner rd = new Scanner(registroVentas);
                while (rd.hasNextLine()) { // Se leen las lineas del archivo
                    String[] data = rd.nextLine().split("\t", 5); // Se identifican los campos
                    
                    // Los campos que son de tipos distintos a String, se convierten a su tipo correspondiente
                    historialVentas.add(new Venta(data[0],
                			data[1],
                			Float.parseFloat(data[2]),
                			Integer.parseInt(data[3])
                		));                    
                }
                rd.close(); // Se cierra el scanner
            } catch(IOException ex){
                ex.printStackTrace();
            }
        } else { // Si no existe, se crea el archivo
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
	
		// Se verifica que existe el archivo
        if (registroTareas.exists()) {
            try{
                Scanner rd = new Scanner(registroTareas);
                while (rd.hasNextLine()) {
                	// Se leen los campos
                    String[] data = rd.nextLine().split("\t", 5);
                    // Se separan los elementos del horario, y se convierten a un objeto de LocalDateTime
                    String[] horas = data[2].split(",", 3);
                    dateInicio = LocalDateTime.parse(horas[0], formatoDiaHora);
	                dateFin = LocalDateTime.parse(horas[1], formatoDiaHora);
	                // Se crea el objeto de tarea y se agrega a la lista
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
        } else { // Si no existe el archivo, se crea y se notifica
            try {
                System.out.println("No existen registros. Creando archivo...");
                registroTareas.createNewFile();                
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
	}
	
	public static void guardarEmpleado(Empleado empleado) {
    	BufferedWriter bw = null; // Se crea un lector como variable local, que pueda accederse fuera del try
        try {
            bw = new BufferedWriter(
                new FileWriter(registroEmpleados, true) // Se incia en modo de append o adjuntar
            ); // Se le asigna el archivo al BufferedWriter
            // Se escriben los datos del empleado
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
        } finally 	{ // Se cierra el archivo
        	try {
        	    if(bw != null) bw.close();
        	} catch(Exception ex){
        	       ex.printStackTrace();
        	}
        }
	}
	
	public static void sobreescribirEmpleados(ArrayList<Empleado> empleados) {
    	BufferedWriter bw = null; // Se crea el escritor como variable local para cerrarlo fuera del try
        try {
            bw = new BufferedWriter(
                new FileWriter(registroEmpleados, false) // Se inicia en modo de sobreescritura
            );
            for (Empleado empleado : empleados) { // Por cada empleado, se escriben sus datos
                bw.write(empleado.getNombre() + "\t" +
                		empleado.getPassword() + "\t" +
                		empleado.getEdad() + "\t" +
                		empleado.getHorario()[0] + "," + empleado.getHorario()[1] + "\t" +
                		empleado.getSalario() + "\t" +
               			empleado.getSucursal() + "\t" +
               			empleado.isGerente() + "\t" +
               			empleado.getJefe() + "\n"
                );
            }
        } catch(IOException ex){
            ex.printStackTrace();
        } finally 	{ // Se cierra el archivo
        	try {
        	    if(bw != null) bw.close();
        	} catch(Exception ex){
        	       ex.printStackTrace();
        	}
        }
	}
	
	public static void guardarTarea(Tarea tarea) {
    	BufferedWriter bw = null; // Se crea el escritor como variable local
        try {
            bw = new BufferedWriter(
                new FileWriter(registroTareas, true) // Se abre el archivo en modo de adjuntar
            );
            // Se escriben los datos de la tarea
            bw.write(tarea.getEmpleado() + "\t" +
                		tarea.getNombre() + "\t" +
                		tarea.getHorario()[0].format(formatoDiaHora) + "," + tarea.getHorario()[1].format(formatoDiaHora) + "\t" +
               			tarea.getDescripcion() + "\n"
            );   
        } catch(IOException ex){
            ex.printStackTrace();
        } finally 	{ // Se cierra el archivo
        	try {
        	    if(bw != null) bw.close();
        	} catch(Exception ex){
        	       ex.printStackTrace();
        	}
        }
	}
	
	public static void eliminarTarea(String nombre, String nombreTarea) {
    	BufferedWriter bw = null; // Se crea el escritor como variable local
        try {
            bw = new BufferedWriter(
                new FileWriter(registroTareas, false) // Se inicia en modo de sobreescritura
            );
    		for (Tarea tarea : calendario.getTareas()) { // Se itera sobre las tareas del calendario
    			if (tarea.getEmpleado().equals(nombre) && tarea.getNombre().equals(nombreTarea)) { // Si la tarea es la que se busca eliminar, no se escribe de nuevo
    				continue;
    			}
    			// Si la tarea no cumple con los datos de la que se busca, se vuelve a escribir
                bw.write(tarea.getEmpleado() + "\t" +
                		tarea.getNombre() + "\t" +
                		tarea.getHorario()[0].format(formatoDiaHora) + "," + tarea.getHorario()[1].format(formatoDiaHora) + "\t" +
               			tarea.getDescripcion() + "\n"
                );
    		}
        } catch(IOException ex){
            ex.printStackTrace();
        } finally 	{ 
        	try { // Se cierra el archivo
        	    if(bw != null) bw.close();
        	} catch(Exception ex){
        	       ex.printStackTrace();
        	}
        }
	}
	
	public static void guardarVenta(Venta venta) {
    	BufferedWriter bw = null; // Se crea el escritor como variable local
        try {
            bw = new BufferedWriter(
                new FileWriter(registroVentas, true) // Se inicia en modo de adjuntar
            );
            // Se escribe la venta
            bw.write(venta.getNombreEmpleado() + "\t" +
                		venta.getNombreArticulo() + "\t" +
                		venta.getValorUnitario() + "\t" +
               			venta.getCantidad() + "\n"
            );   
        } catch(IOException ex){
            ex.printStackTrace();
        } finally 	{ // Se cierra el archivo
        	try {
        	    if(bw != null) bw.close();
        	} catch(Exception ex){
        	       ex.printStackTrace();
        	}
        }
	}

}
