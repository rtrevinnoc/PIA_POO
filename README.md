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
