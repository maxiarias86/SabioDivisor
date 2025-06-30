# SabioDivisor

### Segundo Parcial - Programación Avanzada 2025

## Descripción del Proyecto: Sistema de Gestión de Gastos Compartidos

Aplicación de escritorio en Java que permite registrar gastos entre múltiples usuarios, dividirlos en cuotas, calcular automáticamente las deudas resultantes y registrar pagos directos entre personas. El sistema mantiene un estado de cuenta actualizado para cada usuario.

### Funcionalidades Principales:

* Gestión de usuarios: registro, edición, login y logout. No se permite eliminar usuarios.
* Registro de gastos: monto, fecha, descripción y número de cuotas.
* División de gastos: entre uno o más pagadores y deudores.
* Registro de pagos entre usuarios.
* Visualización de deudas y estado de cuenta.

## Diseño Orientado a Objetos:

* **User**: id, name, email, password
* **Expense**: id, amount, date, description, installments
* **Debt**: representa lo que un usuario le debe a otro por un gasto
* **Payment**: transferencia directa entre usuarios
* **Response<T>**: clase genérica para retornar objetos y mensajes entre capas

## Validaciones y Excepciones:

En este proyecto, las validaciones y el manejo de excepciones se realizan principalmente en las clases de Service, que son la capa intermedia entre las Views (JFrame/JPanel) y los DAOs.
Antes de llamar al DAO, cada método valida que los datos obligatorios no estén vacíos o nulos, que los valores numéricos sean válidos (por ejemplo, montos positivos, fechas correctas, etc.), y que no haya duplicados cuando el caso lo requiere.
Si alguna validación falla, se devuelve un objeto `Response` con `success = false` y un mensaje que explica el motivo del error.
Además, todos los accesos a la base de datos están contenidos en bloques `try-catch`. Si ocurre una excepción, se captura y se devuelve un `Response` con `success = false` y el mensaje de la excepción.

---

# Instalación y Ejecución

1. Clonar el repositorio:
   `git clone https://github.com/usuario/sabiodivisor.git`

2. Instalar XAMPP y configurar la base de datos:

    * Descargar e instalar XAMPP desde [Apache Friends](https://www.apachefriends.org/index.html).
    * Iniciar el servidor Apache y MySQL desde el panel de control de XAMPP.
    * Importar el archivo `DDL_sabio_divisor.sql` en **phpMyAdmin** para crear las tablas necesarias.
    * Asegurarse de que la versión de MySQL sea **8.0.16 o superior**, ya que el script utiliza validaciones `CHECK`.
    * Configurar la conexión a la base de datos en el archivo `config.properties`, especificando:

      ```
      db.url=jdbc:mysql://localhost:3306/sabio_divisor
      db.user=tu_usuario
      db.password=tu_contraseña
      ```
    * Asegurarse de que `config.properties` esté en la ruta correcta dentro del proyecto.

3. Compilar el proyecto en un IDE como IntelliJ IDEA o Eclipse.

4. Ejecutar la aplicación:
   Abrir el archivo `Main.java` y correr la clase principal.

5. Iniciar sesión con un usuario existente o crear uno nuevo.

6. Utilizar las funcionalidades de la aplicación para registrar gastos, dividirlos, registrar pagos y consultar deudas.

7. Para cerrar la aplicación, utilizar el botón de salir en la interfaz gráfica.

8. Para realizar pruebas, se pueden crear varios usuarios y registrar diferentes gastos y pagos entre ellos.

9. Para ver el estado de cuenta de cada usuario, acceder a la sección correspondiente en la interfaz gráfica.

---

# Requisitos

* Java 17 o superior
* MySQL 8.0.16 o superior
* XAMPP (con Apache y MySQL)
* IDE recomendado: IntelliJ IDEA