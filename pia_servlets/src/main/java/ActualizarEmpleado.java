

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import first.Empleado;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ActualizarEmpleado
 */
public class ActualizarEmpleado extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static File registroEmpleados = new File("empleados.txt");

    /**
     * Default constructor. 
     */
    public ActualizarEmpleado() {
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
        // Se busca el empleado que cumpla los requisitos
		PrintWriter out = response.getWriter();
		HttpSession sesion = request.getSession();
		String nombreEmpleado = request.getParameter("name");
	    ArrayList<Empleado> empleados = (ArrayList<Empleado>) sesion.getAttribute("empleados");
	    Empleado actual = (Empleado) sesion.getAttribute("actual");
	    
        Empleado empleado = null;
        for (Empleado cur: empleados) {
        	if (cur.getNombre().equalsIgnoreCase(nombreEmpleado)) {
        		empleado = cur;
        	}
        }
        
        // Si no se encuentra, se notifica
        if (empleado == null) {
    		out.println("<h1>No se encontro el empleado<h1>");
            response.sendRedirect(request.getContextPath() + "/Menu");
        }
		
        String nuevoValor = request.getParameter("nuevo");
        switch(Integer.parseInt(request.getParameter("campo"))) {
        	case 1:
        		// Permite cambiar el horario, se lee la informacion
                empleado.setHorario(new int[] {Integer.parseInt(nuevoValor), empleado.getHorario()[1]});
        		break;
        	case 2:
                empleado.setHorario(new int[] {empleado.getHorario()[0], Integer.parseInt(nuevoValor)});
        		break;
        	case 3:
        		empleado.setSalario(Float.parseFloat(nuevoValor));
        		break;
        	case 4:
        		empleado.setSucursal(nuevoValor);
        		break;
        };
        
		if (actual.isGerente()) {
			empleados.add(actual);
		}
		// Los cambios que se hayan realizado, se escriben al archivo
		sobreescribirEmpleados(empleados);
        
        response.sendRedirect(request.getContextPath() + "/Menu");
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
	
}
