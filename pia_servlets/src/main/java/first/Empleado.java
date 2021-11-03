package first;

public class Empleado {
	private float salario;
	private int[] horario = new int[2];
	private String sucursal, password;
	public String nombre, jefe;
	public int edad;
	public boolean gerente;
	
	// Constructor
	public Empleado(String nombre, String password, int edad, int[] horario, float salario, String sucursal, boolean gerente, String jefe) {
		this.nombre = nombre;
		this.password = password;
		this.edad = edad;
		this.horario = horario;
		this.salario = salario;
		this.sucursal = sucursal;
		this.gerente = gerente;
		this.jefe = jefe;
	}
	
	// Getters/Setters
	public String getJefe() {
		return jefe;
	}

	public void setJefe(String jefe) {
		this.jefe = jefe;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public float getSalario() {
		return salario;
	}
	public int[] getHorario() {
		return horario;
	}
	public String getSucursal() {
		return sucursal;
	}
	public String getPassword() {
		return password;
	}
	public boolean isGerente() {
		return gerente;
	}
	public void setSalario(float salario) {
		this.salario = salario;
	}

	public void setHorario(int[] horario) {
		this.horario = horario;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setGerente(boolean gerente) {
		this.gerente = gerente;
	}

	// metodo para verificar la contraseña
	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}
}
