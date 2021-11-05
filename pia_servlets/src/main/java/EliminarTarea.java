

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import first.Calendario;
import first.Gerente;
import first.Tarea;

/**
 * Servlet implementation class EliminarTarea
 */
public class EliminarTarea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static File registroTareas = new File("tareas.txt");
	private static DateTimeFormatter formatoDiaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.forLanguageTag("es-ES"));

    /**
     * Default constructor. 
     */
    public EliminarTarea() {
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
		HttpSession sesion = request.getSession();
		Gerente supervisor = (Gerente) sesion.getAttribute("supervisor");
	    Calendario calendario = (Calendario) sesion.getAttribute("calendario");
	    
	    String nombre = request.getParameter("name"), nombreTarea = request.getParameter("nameTarea");
	    
        // Se elimina la tarea del archivo
        eliminarTarea(calendario, nombre, nombreTarea);
        // Se elimina la tarea de la lista que esta cargada
        supervisor.eliminarTarea(calendario, nombre, nombreTarea);
        
        response.sendRedirect(request.getContextPath() + "/Menu");
	}
	
	public static void eliminarTarea(Calendario calendario, String nombre, String nombreTarea) {
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

}
