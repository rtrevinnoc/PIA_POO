

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import first.Calendario;
import first.Empleado;
import first.Tarea;
import first.Venta;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class VerVentas
 */
public class VerVentas extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public VerVentas() {
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
	    ArrayList<Venta> historialVentas = (ArrayList<Venta>) sesion.getAttribute("historialVentas");
		
		out.println("<h1>Tareas de " + nombreEmpleado + "</h1>");
	    out.print("<table>");
	    out.print("<tr>");
	    out.print("<th>Nombre:</th>");
	    out.print("<th>Valor Unitario:</th>");
	    out.print("<th>Cantidad:</th>");
	    out.print("<th>Valor Total:</th>");
	    out.print("</tr>");
	    
    
		for (Venta venta : historialVentas) { // Se itera por las tareas
			if (venta.getNombreEmpleado().equalsIgnoreCase(nombreEmpleado)) {
    			out.print("<tr>");
    			out.print("<td>" + venta.getNombreArticulo() + "</td>");
    			out.print("<td>" + venta.getValorUnitario() + "</td>");
    			out.print("<td>" + venta.getCantidad() + "</td>");    			
    			out.print("<td>" + venta.getValorTotal() + "</td>");
    			out.print("</tr>");
			}
		}
	    
	    out.print("</table>");
	}

}
