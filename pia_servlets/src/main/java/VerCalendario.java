

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import first.Calendario;
import first.Empleado;
import first.Tarea;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class VerCalendario
 */
public class VerCalendario extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static DateTimeFormatter formatoDiaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es-ES"));

    /**
     * Default constructor. 
     */
    public VerCalendario() {
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
		String nombreEmpleado = request.getParameter("name");
		LocalDateTime dia = LocalDateTime.parse(request.getParameter("fecha") + " 00:00", formatoDiaHora); // Se interpreta la fecha
	    Calendario calendario = (Calendario) sesion.getAttribute("calendario");
		
		out.println("<h1>Tareas de " + nombreEmpleado + " en " + dia.format(formatoDiaHora) + "</h1>");
	    out.print("<table>");
	    out.print("<tr>");
	    out.print("<th>Descripcion:</th>");
	    out.print("<th>Fecha Inico:</th>");
	    out.print("<th>Fecha Fin:</th>");
	    out.print("</tr>");
	    
		for (Tarea tarea : calendario.getTareas()) { // Se itera por las tareas
			if (tarea.getEmpleado().equalsIgnoreCase(nombreEmpleado) && tarea.getHorario()[0].getDayOfYear() == dia.getDayOfYear()) {
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
