

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import first.Empleado;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import first.Venta;

/**
 * Servlet implementation class RegistrarVenta
 */
public class RegistrarVenta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static File registroVentas = new File("ventas.txt");

    /**
     * Default constructor. 
     */
    public RegistrarVenta() {
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
	    Empleado actual = (Empleado) sesion.getAttribute("actual");
	    ArrayList<Venta> historialVentas = (ArrayList<Venta>) sesion.getAttribute("historialVentas");
        
        // Se crea el objeto con la informacion leida de la venta
        Venta nuevaVenta = new Venta(actual.getNombre(), request.getParameter("name"), Integer.parseInt(request.getParameter("precio")), Integer.parseInt(request.getParameter("cantidad")));
        // Se añade a la lista de ventas
		historialVentas.add(nuevaVenta);
		// Se guarda en el archivo de ventas
		guardarVenta(nuevaVenta);
		
        response.sendRedirect(request.getContextPath() + "/Menu");
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
