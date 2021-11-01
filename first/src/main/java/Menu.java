

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import first.Calendario;
import first.Empleado;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import first.Tarea;
import first.Venta;
import first.Gerente;

/**
 * Servlet implementation class Menu
 */
public class Menu extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static File registroTareas = new File("tareas.txt"), registroVentas = new File("ventas.txt");
	private static DateTimeFormatter formatoDiaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es-ES"));

    /**
     * Default constructor. 
     */
    public Menu() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
	    HttpSession sesion = request.getSession();
	    Empleado actual = (Empleado) sesion.getAttribute("actual");
	    ArrayList<Empleado> empleados = (ArrayList<Empleado>) sesion.getAttribute("empleados");
		ArrayList<Venta> historialVentas = new ArrayList<Venta>();
		Calendario calendario = new Calendario();
	    
	    out.println("<p>¡Hola " + actual.getNombre() + "!</p>");
	    out.println("<h1>Menu</h1>");
	    out.print("<form action='Menu' method='post'>"); 
	    out.print("<select name='opcion'>");  
	    out.print("<option value='1'>Ver Mi Calendario</option>");
	    out.print("<option value='2'>Ver Calendario General</option>");
	    out.print("<option value='3'>Listar Empleados</option>");
	    out.print("<option value='4'>Registrar Venta</option>");
	    out.print("<option value='5'>Listar Ventas</option>");
	    out.print("<option value='6'>Listar Mis Ventas</option>");
	    
	    if (actual.isGerente()) {
		    out.print("<option value='7'>Registrar Empleado</option>");
		    out.print("<option value='8'>Programar Tarea a Empleado</option>");
		    out.print("<option value='9'>Eliminar Tarea a Empleado</option>");
		    out.print("<option value='10'>Ver Calendario Por Dia de Empleado</option>");
		    out.print("<option value='11'>Ver Calendario Completo de Empleado</option>");
		    out.print("<option value='12'>Actualizar Informacion de Empleado</option>");
		    out.print("<option value='13'>Listar Ventas por Empleado</option>");
		    
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
        	sesion.setAttribute("supervisor", new Gerente(actual.getNombre(),
            			actual.getPassword(),
            			actual.getEdad(),
            			actual.getHorario(),
            			actual.getSalario(),
            			actual.getSucursal(),
            			subordinados
        			));
	    }
	    
	    out.print("</select>");
	    out.print("<input type='submit' value='Seleccionar'/>");
	    out.print("</form>");
	    
        // Se cargan las tareas y las ventas desde sus archivos
        cargarTareas(calendario);
        cargarVentas(historialVentas);
	    sesion.setAttribute("calendario", calendario);
	    sesion.setAttribute("historialVentas", historialVentas);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
	    HttpSession sesion = request.getSession();
	    Empleado actual = (Empleado) sesion.getAttribute("actual");
	    ArrayList<Empleado> empleados = (ArrayList<Empleado>) sesion.getAttribute("empleados");
	    Calendario calendario = (Calendario) sesion.getAttribute("calendario");

		switch(Integer.parseInt(request.getParameter("opcion"))) {
	    	case 1:
	    	    out.println("<h1>Mi Calendario</h1>");

	    	    out.print("<table>");
	    	    out.print("<tr>");
	    	    out.print("<th>Nombre:</th>");
	    	    out.print("<th>Descripcion:</th>");
	    	    out.print("<th>Fecha Inico:</th>");
	    	    out.print("<th>Fecha Fin:</th>");
	    	    out.print("</tr>");
	    	    
	    		for (Tarea tarea : calendario.getTareas()) { // Se itera por las tareas
	    			if (tarea.getEmpleado().equalsIgnoreCase(actual.getNombre())) {
		    			out.print("<tr>");
		    			out.print("<td>" + tarea.getNombre() + "</td>");
		    			out.print("<td>" + tarea.getDescripcion() + "</td>");
		    			out.print("<td>" + tarea.getHorario()[0].format(formatoDiaHora) + "</td>");
		    			out.print("<td>" + tarea.getHorario()[1].format(formatoDiaHora) + "</td>");
	    			}
	    		}
	    	    
	    	    out.print("</table>");
	    		break;
	    	case 2:
	    	    out.println("<h1>Calendario General</h1>");

	    	    out.print("<table>");
	    	    out.print("<tr>");
	    	    out.print("<th>Empleado:</th>");
	    	    out.print("<th>Nombre:</th>");
	    	    out.print("<th>Descripcion:</th>");
	    	    out.print("<th>Fecha Inico:</th>");
	    	    out.print("<th>Fecha Fin:</th>");
	    	    out.print("</tr>");
	    	    
	    		for (Tarea tarea : calendario.getTareas()) { // Se itera por las tareas
	    			out.print("<tr>");
	    			out.print("<td>" + tarea.getEmpleado() + "</td>");
	    			out.print("<td>" + tarea.getNombre() + "</td>");
	    			out.print("<td>" + tarea.getDescripcion() + "</td>");
	    			out.print("<td>" + tarea.getHorario()[0].format(formatoDiaHora) + "</td>");
	    			out.print("<td>" + tarea.getHorario()[1].format(formatoDiaHora) + "</td>");
	    		}
	    	    
	    	    out.print("</table>");
	    		break;
	    	case 3:
	    	    out.println("<h1>Listar Empleados</h1>");

	    	    out.print("<table>");
	    	    out.print("<tr>");
	    	    out.print("<th>Nombre:</th>");
	    	    out.print("<th>Edad:</th>");
	    	    out.print("<th>Horario:</th>");
	    	    out.print("<th>Salario:</th>");
	    	    out.print("<th>Sucursal:</th>");
	    	    out.print("<th>Gerente:</th>");
	    	    out.print("</tr>");
	    	    
	    	    for (Empleado cur: empleados) {
		    	    out.print("<tr>");
		    	    out.print("<td>" + cur.getNombre() + "</td>");
		    	    out.print("<td>" + cur.getEdad() + "</td>");
		    	    out.print("<td>" + cur.getHorario()[0] + "-" + cur.getHorario()[1] + "</td>");
		    	    out.print("<td>" + cur.getSalario() + "</td>");
		    	    out.print("<td>" + cur.getSucursal() + "</td>");
		    	    out.print("<td>" + cur.getJefe() + "</td>");
		    	    out.print("</tr>");
	    	    }
	    	    
	    	    out.print("</table>");
	    		break;
	    	case 4:
	    		break;
	    	case 5:
	    		break;
	    	case 6:
	    		break;
	    	case 7:
	    	    out.println("<h1>Registrar Empleado</h1>");
	    		
	    	    out.print("<form action='RegistrarEmpleado' method='post'>");
	    	    out.print("<table>");
	    	    out.print("<tr><td>Nombre:</td><td><input type='text' name='name'/></td></tr>");  
	    	    out.print("<tr><td>Password:</td><td><input type='password' name='password'/></td></tr>'");
	    	    out.print("<tr><td>Edad:</td><td><input type='number' name='edad' placeholder='edad'/></td></tr>");
	    	    out.print("<tr><td>Horario de Entrada (Formato Militar v.g. 1600):</td><td><input type='number' name='entrada' placeholder='entrada'/></td></tr>");
	    	    out.print("<tr><td>Horario de Salida (Formato Militar v.g. 1600):</td><td><input type='number' name='salida' placeholder='salida'/></td></tr>");
	    	    out.print("<tr><td>Salario:</td><td><input type='number' name='salario' placeholder='salario'/></td></tr>");
	    	    out.print("<tr><td>Sucursal:</td><td><input type='text' name='sucursal' placeholder='sucursal'/></td></tr>");
	    	    out.print("<tr><td>Gerente:</td><td><input type='checkbox' name='gerente' placeholder='gerente' readonly/></td></tr>");
	    	    out.print("<tr><td>Jefe:</td><td><input type='text' name='jefe' placeholder='jefe' value='" + actual.getNombre() + "'/></td></tr>");
	    	    out.print("<tr><td colspan='2'><input type='submit' value='Registrar Empleado'/></td></tr>");  
	    	    out.print("</table>");  
	    	    out.print("</form>");          
	    		
	    		break;
	    	case 8:
	    	    out.println("<h1>Programar Tarea a Empleado</h1>");
	    		
	    	    out.print("<form action='RegistrarTarea' method='post'>");
	    	    out.print("<table>");
	    	    out.print("<tr><td>Nombre de Empleado:</td><td><input type='text' name='name'/></td></tr>");
	    	    out.print("<tr><td>Nombre de Tarea:</td><td><input type='text' name='nameTarea'/></td></tr>");
	    	    out.print("<tr><td>Fecha de Inicio (v.g. 16/10/2021 15:00):</td><td><input type='text' name='inicio'/></td></tr>");  
	    	    out.print("<tr><td>Fecha de Fin (v.g. 16/10/2021 15:00):</td><td><input type='text' name='fin'/></td></tr>");
	    	    out.print("<tr><td>Descripcion:</td><td><input type='text' name='descripcion'/></td></tr>");
	    	    out.print("<tr><td colspan='2'><input type='submit' value='Programar Tarea'/></td></tr>"); 
	    	    out.print("</table>");  
	    	    out.print("</form>");    
	    		break;
	    	case 9:
	    		break;
	    	case 10:
	    		break;
	    	case 11:
	    		break;
	    	case 12:
	    		break;
	    	case 13:
	    		break;
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

}
