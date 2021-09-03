# PIA de Programación Orientada a Objetos

Equipo #3

|Nombre|Matricula|
|---|---|
|Ricardo Herrera Romero|#1953026|
|David Rene Hernandez Hernandez|#1753416|
|Roberto Treviño Cervantes|#1915003|

## Introducción

Nuestro proyecto consiste en un sistema de administración de una tienda de conveniencia, por lo tanto consideramos la delegación de tareas que realiza un gerente a sus subordinados en un calendario, asi como el manejo de inventario. Particularmente, nos apoyamos en la experiencia de nuestro compañero David para encontrar y resolver los problemas que sufren los puntos de venta de OXXO (como que es imposible eliminar articulos que ya no se desea pagar sin apagar la terminal) mediante una mejor integración del sistema.

## Propuesta técnica

Para manejar cada usuario que acceda a su cuenta en el servidor y tener la capacidad de reconocer si es que es un empleado o un gerente, tenemos la clase **Session** cuyos atributos _nombre_ y _string_ nos permite buscarlo (luego de ser creada) en una base de datos simulada con un archivo JSON que reside en el servidor, además esta clase cuenta con los metodos getEmpleado() y getGerente() que nos permiten obtener un objeto con las propiedades del empleado, y en el caso de emplear el segundo metodo y que las credenciales no pertenezcan a un gerente, retornar nulo.

El nucleo de la aplicación reside en la clase **Empleado** que contiene la información (en forma de atributos) de _salario_, _horario_ (como un par de enteros en un array), la _sucursal_, _nombre_, _edad_, _password_ (privado). La mayoria de los atributos anteriores permiten interacción mediante getters y setters regulares, excepto _password_ que solo permite la verificación con `checkPassword(): void`. De esta clase, hereda la clase **Gerente**, y añade el atributo _subordinados_ que es una lista de objetos de la clase **Empleado**, y puede modificarse mediante los setters y getters regulares, y tambien incluye un metodo `asignarTarea(empleado:String, tarea:Tarea): void` que permite asignar una tarea a un empleado.

Los **Gerente**'s tienen la capacidad de modificar directamente el objeto de Inventario, pero los **Empleado**'s requieren la mediación de un objeto de la clase **CajaDeCobro** que les permite vender (eliminar) mercancias del inventario y aumentar el dinero en la caja de cobro.

Además, los **Empleado** tiene acceso a un objeto global de la clase **Calendario** donde el **Gerente** programa sus tareas, y desde el cual podemos leer cada tarea de cada empleado para desplegarla en la interfaz grafica, esto a través de que cada tarea esta especificada mediante la clase **Tarea** que contiene un horario (en el mismo formato del que tiene el empleado) asi como un booleano que nos indica si esta completado.

### Entidades:

#### “Session” Esta clase tiene como función principal el inicio de sesión en el sistema.
  
- 2 atributos String, nombre (public) y password (private).
- Session(nombre: String, password: String) Verifica las listas de empleados y los gerentes, para corroborar si nombre y password coinciden con algún registro.
- getEmpleado() Regresa las coincidencias de empleados.
- getGerentes() Regresa las coincidencias de gerentes.

#### “Empleado” Clase padre, asigna salarios, horarios, sucursales, nombres y contraseñas, crea empleados y compara las contraseñas.

- 6 atributos, float salario, horario Int[2], String sucursal, String nombre, int edad  (public) y una variable String password (private).
- getSalario(), setSalario(float) establecer u obtener valores de Salario.
- getHorario(), setHorario(int[2]) establecer u obtener valor de Horario.
- getSucursal(), setSucursal(String) establecer u obtener sucursal.
- getNombre(), setNombre(String) establecer u obtener nombres.
- getEdad(), setEdad(int) establecer u obtener edades.
- Empleado(salario: float, horario: int[2], sucursal: String, nombre: String, edad: int, password: String) Método principal, crea los todos los objetos empleados con los parámetros ingresados.
- setPassword(String), checkPassword(String) establecer y verificar la contraseña.

#### “Gerente” Subclase de Empleado, maneja los subordinados, asigna tareas y crea gerentes.
- Atributos: subordinados: List<Empleado> (public)
- setSubordinados(List), getSubordinados() establecer y obtener los Subordinados
- asignarTarea(String, Tarea) Asigna tareas a los subordinados.
- Gerente(salario: float, horario: int[2], sucursal: String, nombre: String, edad: int, subordinados: List, password: String) Método principal, crea todos los objetos gerentes con los parámetros ingresados.
  
#### “Calendario” Guarda, muestra, completa y borra tareas en un HashMap.
- Atributos tareas: HashMap (public).
- mostrarTareasDe(nombre: String, día: Date) Regresa una lista con las tareas asignadas a cierto empleado.
- addTarea(nombre: String, tarea: Tarea) Añade una tarea a tareas.
- delTarea(nombre: String, nombreTarea: String) Elimina una tarea de tareas.
- wipeTareas() Elimina todas las tareas existentes.
- completarTarea(nombre: String, nombreTarea: String, día: Date) Registra que una tarea fue completada y la fecha en que se acompletó.

#### “Tarea”
- Atributos String nombre, horario int[2], String descripcion, boolean completado.
- Tarea(nombre: String, horario: int[2], descripcion: String) Crea tareas, con nombre, el horario y la descripción de lo que se pide.

#### “Caja de cobro”
- Atributos inventario: Inventario (private).
- vender(nombre: String, cantidad: String) Método para vender, ingresa el producto y la cantidad a vender.
- CajaDeCobro(inventario: Inventario) Comprueba existencias en el inventario.

#### “Inventario”
- Variable mercancías: List<Mercancia>
- Inventario()
- addMercancia(Mercancia) Añade productos a la lista de mercancía.
- updateMercancia(Mercancia) Actualiza los productos existentes en la lista.
- getMercancia(nombre: String) Ubica un producto dentro de la lista por su nombre.
- delMercancia(nombre: String) Ubica y elimina un producto de la lista por su nombre.

#### “Mercancia”
- Variables: String nombre, float precio, int existencia, int mínimo.
- getNombre(), setNombre(String) Obtiene o establece el nombre de la mercancía.
- getPrecio(), setPrecio(float) Obtiene o establece el precio de la mercancía.
- getExistencia(), setEsistencia(int) Obtiene o establece las existencias de cierta mercancía.
- getMinimo(), setMinimo(int) Obtiene o establece el mínimo de mercancía requerido en inventario.
- Mercancia(nombre: String, precio: float, existencia: int, mínimo: int) Método principal, crea los todos los objetos mercancias con los parámetros ingresados.
