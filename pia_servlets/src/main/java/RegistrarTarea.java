

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import first.Empleado;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import first.Calendario;
import first.Gerente;
import first.Tarea;

/**
 * Servlet implementation class RegistrarTarea
 */
public class RegistrarTarea extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static File registroTareas = new File("tareas.txt");
	private static DateTimeFormatter formatoDiaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.forLanguageTag("es-ES"));

    /**
     * Default constructor. 
     */
    public RegistrarTarea() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		PrintWriter out = response.getWriter();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		PrintWriter out = response.getWriter();
	    HttpSession sesion = request.getSession();
		Gerente supervisor = (Gerente) sesion.getAttribute("supervisor");
	    Calendario calendario = (Calendario) sesion.getAttribute("calendario");
		
        // Se convierten las cadenas a LocalDateTime siguiendo el formateador del inicio
        LocalDateTime dateInicio = LocalDateTime.parse(request.getParameter("inicio"), formatoDiaHora);
        LocalDateTime dateFin = LocalDateTime.parse(request.getParameter("fin"), formatoDiaHora);
        
        // Se crea el objeto de tarea
        Tarea tarea = new Tarea(request.getParameter("name"), request.getParameter("nameTarea"), new LocalDateTime[] {dateInicio, dateFin}, request.getParameter("descripcion"));
        // Se a??ade la tarea al calendario
        if(supervisor.asignarTarea(calendario, tarea)) guardarTarea(tarea);
		
        response.sendRedirect(request.getContextPath() + "/Menu");
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

}
