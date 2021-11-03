

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import first.Empleado;

/**
 * Servlet implementation class RegistrarEmpleado
 */
public class RegistrarEmpleado extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static File registroEmpleados = new File("empleados.txt");

    /**
     * Default constructor. 
     */
    public RegistrarEmpleado() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
	    HttpSession sesion = request.getSession();
		ArrayList<Empleado> empleados = (ArrayList<Empleado>) sesion.getAttribute("empleados");

	    String nombre = request.getParameter("name");
	    String password = request.getParameter("password");
	    int edad = Integer.parseInt(request.getParameter("edad"));
	    int horaEntrada = Integer.parseInt(request.getParameter("entrada"));
	    int horaSalida = Integer.parseInt(request.getParameter("salida"));
	    int salario = Integer.parseInt(request.getParameter("salario"));
	    String sucursal = request.getParameter("sucursal");
	    String jefe = request.getParameter("jefe");
	    
        int[] horario = new int[]{horaEntrada, horaSalida};
        
        // Se crea el objeto del nuevo empleado
        Empleado empleado = new Empleado(nombre,
    			password,
    			edad,
    			horario,
    			salario,
    			sucursal,
    			false,
    			jefe
    	);
                
        // Se inicia sesion con el y se inserta en la lista
//        actual = empleado;
        empleados.add(empleado);
//        inicioSesion = true;
        // Se guarda el nuevo empleado en la lista
        guardarEmpleado(empleado);
        out.println("Se creo el empleado " + nombre);
        
        response.sendRedirect(request.getContextPath() + "/Menu");
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
}
