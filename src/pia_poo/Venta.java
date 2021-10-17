package pia_poo;

public class Venta {
	public String nombreArticulo, nombreEmpleado;
	public float valorUnitario, valorTotal;
	public int cantidad;
	
	// Constructor
	Venta(String nombreEmpleado, String nombreArticulo, float valorUnitario, int cantidad) {
		this.nombreArticulo = nombreArticulo;
		this.nombreEmpleado = nombreEmpleado;
		this.valorUnitario = valorUnitario;
		this.cantidad = cantidad;
		this.valorTotal = valorUnitario * cantidad;
	}

	// Getters/Setters
	public String getNombreArticulo() {
		return nombreArticulo;
	}

	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	public float getValorUnitario() {
		return valorUnitario;
	}

	public float getValorTotal() {
		return valorTotal;
	}

	public int getCantidad() {
		return cantidad;
	}
}
