# PIA de Programación Orientada a Objetos

Equipo #3

|Nombre|Matricula|
|---|---|
|Ricardo Herrera Romero|#1953026|
|David Rene Hernandez Hernandez|#1753416|
|Roberto Treviño Cervantes|#1915003|

Version live en <https://piapoo.herokuapp.com/>

## Introducción

Nuestro proyecto consiste en un sistema de administración de una tienda de conveniencia, por lo tanto consideramos la delegación de tareas que realiza un gerente a sus subordinados en un calendario, asi como el manejo de inventario. Particularmente, nos apoyamos en la experiencia de nuestro compañero David para encontrar y resolver los problemas que sufren los puntos de venta de OXXO (como que es imposible eliminar articulos que ya no se desea pagar sin apagar la terminal) mediante una mejor integración del sistema.

No solo porque sea esa tienda en especifico significa que solo ese problema lo tenga ellos si no sen general donde pudimos ver que podría ayudar a las demás compañía así sea una una tiendas de conveniencia grande hasta una tienda chica donde pudieran usar ese programa donde pudimos  observar los diferentes tipos de problemas que pudieran tener hasta una organización de almacén(stock) hasta donde están los productos en ventas donde el publico pudiera agarrar el producto deseado.

## Propuesta técnica

Para manejar cada usuario que accede a su cuenta, y tener la capacidad de reconocer si son un empleado, usamos un archivo TSV que guarda las especificaciones con las cuales reconstruir cada empleado usando la clase `Empleado` y añadirlo a un `ArrayList<Empleado>`. Si el archivo no existe, se crea y se guarda el nuevo empleado que esta ejecutando el programa, de lo contrario, se busca uno que concuerde con las credenciales ingresadas y se cambia el valor de la variable booleana `inicioSesion`. A partir de esto, se realiza un proceso similar con las tareas, y el historial de ventas en sus archivos TSV correspondientes. Todo esto se ha separado en metodos dedicados como `cargarTareas()` y `guardarEmpleados()`.

Al momento de iniciar sesion como se ha descrito anteriormente, si la linea con las credenciales identificadas para reconstruir el usuario indica el campo `gerente` como verdadero, entonces se asigna a la variable supervisor un objeto `Gerente` con las especificaciones del mismo empleado leido. Esto es importante ya que en algunas de las opciones del menu que se presenta despues, verifican que el usuario actual sea gerente, mediante el metodo `isGerente()` de la clase empleado, sobre la variable actual, que contiene el empleado que ha iniciado sesion.

La clase `Gerente` tiene algunos metodos para cambiar atributos de los empleados que contenga en su `ArrayList<Empleados>`, los cuales se le han asignado al cargarse del archivo TSV `empleados.txt` si es que el campo `jefe` contenia su mismo nombre. Desde sus metodos `asignarTarea()` y `eliminarTarea()` (y otros) podemos manipular la informacion que concierne a los empleados, pero ellos no tienen capacidad de modificar.

Finalmente, la clase `Calendario` reune los objetos de clase `Tarea` que ha creado el `Gerente` para sus `Empleado`'s, esta nos permite filtrar por dias o por empleados, asi com elimainar y crear tareas. Esta es la que interactua directamente con la creación de las tareas, por lo tanto, el gerente lo hace a través de esta.

### Entidades:

#### “Empleado” Clase padre, tiene salario, horario, sucursal, nombre y contraseña.

- 8 atributos, float salario, horario Int[2], String sucursal, String nombre, int edad, boolean gerente y String jefe (public) y una variable String password (private).
- getSalario(), setSalario(float) establecer u obtener valores de Salario.
- getHorario(), setHorario(int[2]) establecer u obtener valor de Horario.
- getSucursal(), setSucursal(String) establecer u obtener sucursal.
- getNombre(), setNombre(String) establecer u obtener nombres.
- getEdad(), setEdad(int) establecer u obtener edades.
- setGerente(), isGerente() cambia u obtiene el estado de si es gerente.
- getJefe(), setJefe() cambia u obtiene el nombre del gerente encargado.
- Empleado(salario: float, horario: int[2], sucursal: String, nombre: String, edad: int, password: String, gerente: boolean, jefe: String) Método principal, crea los todos los objetos empleados con los parámetros ingresados.
- setPassword(String), checkPassword(String) establecer y verificar la contraseña.

#### “Gerente” Subclase de Empleado, maneja los subordinados, asigna tareas y crea gerentes.
- Atributos: subordinados: ArrayList<Empleado> (public)
- setSubordinados(List), getSubordinados() establecer y obtener los Subordinados
- asignarTarea(String, Tarea) Asigna tareas a los subordinados.
- setHorario(), setSalario() y setSucursal() configura los atributos de sus subordinados.
- Gerente(salario: float, horario: int[2], sucursal: String, nombre: String, edad: int, subordinados: List, password: String) Método principal, crea todos los objetos gerentes con los parámetros ingresados.
  
#### “Calendario” Guarda, muestra, completa y borra tareas en un ArrayList.
- Atributos tareas: HashMap (public).
- mostrarTodasTareasDe(nombre: String, formato: DateTimeFormatter) Regresa una lista con las tareas asignadas a cierto empleado.
- mostrarTodasTareasDePorDiaDe(nombre: String, día: LocalDateTime, formato: DateTimeFormatter) Regresa una lista con las tareas asignadas a cierto empleado en un dia especifico.
- mostrarTodasTareas(formato DateTimeFormatter) Regresa todas las tareas.
- addTarea(tarea: Tarea) Añade una tarea a tareas.
- delTarea(nombre: String, nombreTarea: String) Elimina una tarea de tareas.
- wipeTareas() Elimina todas las tareas existentes.
- completarTarea(nombre: String, nombreTarea: String, día: Date) Registra que una tarea fue completada y la fecha en que se acompletó.

#### “Tarea”
- Atributos String nombre, horario int[2], String descripcion, boolean completado.
- Tarea(nombre: String, nombreTarea: String, horario: LocalDateTime[2], descripcion: String) Crea tareas, con nombre, el horario y la descripción de lo que se pide.
- completar() Marca como completa una tarea.
- getDescripcion(), setDescripcion() Configura la descripcion de la tarea
- getEmpleado(), setEmpleado() Configura el encargado de la tarea
- getHorario(), setHorario() Configura la programacion de la tarea
- getNombre(), setNombre() Configura el identificador de la tarea

#### "Venta"
- Atributos String nombreArticulo, String nombreEmpleado, float valorUnitario, float valorTotal, int cantidad
- Venta(nombreArticulo, nombreEmpleado, valorUnitario, cantidad) Crea el objeto
- getCantidad()
- getNombreArticulo()
- getNombreEmpleado()
- getValorTotal()
- getValorUnitario()
