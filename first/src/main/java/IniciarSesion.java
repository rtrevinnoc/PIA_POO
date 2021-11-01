

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import first.Empleado;

/**
 * Servlet implementation class IniciarSesion
 */
public class IniciarSesion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static File registroEmpleados = new File("empleados.txt");
	public static Empleado actual = null;
	public static boolean inicioSesion = false;
	ArrayList<Empleado> empleados = new ArrayList<Empleado>();

    /**
     * Default constructor. 
     */
    public IniciarSesion() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
				
	    out.println("<h1>Iniciar Sesion</h1>");
	    out.print("<form action='IniciarSesion' method='post'>");  
	    out.print("<table>");
	    out.print("<tr><td>Nombre:</td><td><input type='text' name='name' placeholder='nombre' /></td></tr>");  
	    out.print("<tr><td>Password:</td><td><input type='password' name='password' placeholder='password' /></td></tr>'");
	    out.print("<tr><td colspan='2'><input type='submit' value='Iniciar Sesion'/></td></tr>");  
	    out.print("</table>");  
	    out.print("</form>"); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
	    String nombre = request.getParameter("name");
	    String password = request.getParameter("password");
	    
	    HttpSession sesion = request.getSession();
	    
	    cargarEmpleados(nombre, password, empleados);
	    
        // Si no se ha podido iniciar sesion
        if (!inicioSesion) {
            out.println("<p>No existe el empleado. Conteste las siguiente preguntas</p>");
            
            // Se lee la informacion para crear el nuevo empleado
    	    out.print("<form action='CrearEmpleado' method='post'>");
    	    out.print("<table>");
    	    out.print("<tr><td>Nombre:</td><td><input type='text' name='name' readonly value='" + nombre + "'/></td></tr>");  
    	    out.print("<tr><td>Password:</td><td><input type='password' name='password' readonly value='" + password + "'/></td></tr>'");
    	    out.print("<tr><td>Edad:</td><td><input type='number' name='edad' placeholder='edad'/></td></tr>");
    	    out.print("<tr><td>Horario de Entrada (Formato Militar v.g. 1600):</td><td><input type='number' name='entrada' placeholder='entrada'/></td></tr>");
    	    out.print("<tr><td>Horario de Salida (Formato Militar v.g. 1600):</td><td><input type='number' name='salida' placeholder='salida'/></td></tr>");
    	    out.print("<tr><td>Salario:</td><td><input type='number' name='salario' placeholder='salario'/></td></tr>");
    	    out.print("<tr><td>Sucursal:</td><td><input type='text' name='sucursal' placeholder='sucursal'/></td></tr>");
    	    out.print("<tr><td>Gerente:</td><td><input type='checkbox' name='gerente' placeholder='gerente'/></td></tr>");
    	    out.print("<tr><td>Jefe:</td><td><input type='text' name='jefe' placeholder='jefe'/></td></tr>");
    	    out.print("<tr><td colspan='2'><input type='submit' value='Crear Empleado'/></td></tr>");  
    	    out.print("</table>");  
    	    out.print("</form>");          
        } else {
    	    out.println("<h1>Ha iniciado sesion como: </h1>");
    	    out.print("<form action='IniciarSesion' method='post'>");  
    	    out.print("<table>");
    	    out.print("<tr><td>" + nombre + "</td></tr>");  
    	    out.print("<tr><td>" + password + "</td></tr>'");
    	    out.print("</table>");  
    	    out.print("</form>");
    	    
    	    sesion.setAttribute("actual", actual);
    	    sesion.setAttribute("empleados", empleados);
    	    
    	    response.sendRedirect(request.getContextPath() + "/Menu");
        }
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
                    	inicioSesion = true;
                    	actual = empleado;
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

}
