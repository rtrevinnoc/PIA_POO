

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import first.Calendario;
import first.Empleado;
import first.Tarea;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class VerCalendarioCompleto
 */
public class VerCalendarioCompleto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static DateTimeFormatter formatoDiaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es-ES"));

    /**
     * Default constructor. 
     */
    public VerCalendarioCompleto() {
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
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		HttpSession sesion = request.getSession();
		String nombreEmpleado = request.getParameter("name");
	    Calendario calendario = (Calendario) sesion.getAttribute("calendario");
		
		out.println("<h1>Tareas de " + nombreEmpleado + "</h1>");
	    out.print("<table>");
	    out.print("<tr>");
	    out.print("<th>Descripcion:</th>");
	    out.print("<th>Fecha Inico:</th>");
	    out.print("<th>Fecha Fin:</th>");
	    out.print("</tr>");
	    
		for (Tarea tarea : calendario.getTareas()) { // Se itera por las tareas
			if (tarea.getEmpleado().equalsIgnoreCase(nombreEmpleado)) {
    			out.print("<tr>");
    			out.print("<td>" + tarea.getDescripcion() + "</td>");
    			out.print("<td>" + tarea.getHorario()[0].format(formatoDiaHora) + "</td>");
    			out.print("<td>" + tarea.getHorario()[1].format(formatoDiaHora) + "</td>");
    			out.print("</tr>");
			}
		}
	    
	    out.print("</table>");
	}

}
