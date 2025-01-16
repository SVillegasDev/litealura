# Proyecto LiterAlura

**Descripción:**
El proyecto **LiterAlura** es una aplicación de consola que permite gestionar una base de datos de libros y autores, con funcionalidades de búsqueda y listado. Esta aplicación permite interactuar con una base de datos, filtrar libros por título, listar los autores registrados, buscar autores vivos en un año específico y consultar libros por idioma. 

**Tecnologías utilizadas:**
- **Java**: Lenguaje de programación principal.
- **Spring Boot**: Framework para el desarrollo de aplicaciones Java basadas en Spring.
- **JPA (Java Persistence API)**: Para la persistencia de datos en la base de datos.
- **H2 Database**: Base de datos en memoria (se puede cambiar a otra base de datos si es necesario).
- **Maven**: Para la gestión de dependencias y construcción del proyecto.
- **Spring Data JPA**: Para la interacción con la base de datos y la creación de repositorios.

**Estructura del Proyecto:**
- **Modelos**: Clases que representan las entidades del sistema, como `Libro`, `Autor`.
- **Repositorios**: Interfaces para acceder a los datos de la base de datos.
- **Servicios**: Lógica de negocio que manipula los datos.
- **Controladores**: Interfaz de usuario (consola) para interactuar con el sistema.
- **Base de Datos**: Almacén de datos de libros y autores.
  
**Funcionalidades principales:**
1. **Buscar libro por título**: Permite al usuario ingresar el título de un libro y buscarlo en la base de datos.
2. **Listar libros registrados**: Muestra todos los libros registrados en la base de datos.
3. **Listar autores registrados**: Muestra todos los autores registrados en la base de datos junto con sus libros.
4. **Listar autores vivos en un año específico**: Permite al usuario ingresar un año y mostrar los autores que estaban vivos en ese año.
5. **Buscar libros por idioma**: Permite al usuario ingresar un idioma y ver todos los libros disponibles en ese idioma.
6. **Top 5 libros mas descargados**: Muestra los 5 libros mas descargados de la plataforma.

**Manejo de errores:**
El sistema deberá manejar varios casos de error para mejorar la experiencia del usuario y evitar caídas del sistema:
- **Errores de entrada inválida**: Si el usuario ingresa un valor no válido (por ejemplo, un texto cuando se espera un número), debe mostrarse un mensaje adecuado e invitar al usuario a ingresar un valor válido.
- **Acciones con datos inexistentes**: Si se realiza una búsqueda o filtrado de libros/autores que no existen, el sistema debe informar al usuario que no se encontraron resultados.
- **Libros ya registrados en la base de datos**: Si se realiza una búsqueda de un libro que ya esta en la base datos, el sistema debe informar al usuario de su existencia.
- **Errores en la base de datos**: Si ocurre un error al intentar acceder a la base de datos (por ejemplo, conexión fallida), el sistema debe manejar este error y mostrar un mensaje apropiado.
 
