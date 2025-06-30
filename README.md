# SabioDivisor

### Segundo Parcial - Programación Avanzada 2025

## Descripción del Proyecto

**SabioDivisor** es una aplicación de escritorio desarrollada en Java para gestionar gastos compartidos entre múltiples usuarios. Está pensada para situaciones como compartir una casa, un viaje, o una cena entre amigos.

Cada usuario puede registrar lo que pagó, repartirlo entre quienes corresponda, ver quién le debe a quién y también registrar pagos directos entre usuarios. El sistema mantiene un estado de cuenta actualizado que muestra saldos y deudas entre personas.

### Funcionalidades Principales

* Registro, edición, login y logout de usuarios (no se permite eliminar usuarios).
* Registro de gastos con: fecha, monto, descripción, número de cuotas, pagadores y deudores.
* División de gastos: uno o más pagadores, uno o más deudores.
* Cálculo automático de deudas a partir de los gastos ingresados.
* Registro de pagos directos entre dos usuarios.
* Visualización de deudas personales y estado de cuenta.
* Consulta de estado de cuenta proyectado a una fecha futura (considerando cuotas pendientes).

---

## Diseño y Arquitectura

La aplicación sigue una **arquitectura en capas**, con separación clara de responsabilidades:

* **Vista (View)**: contiene los formularios y pantallas (`JFrame`, `JPanel`).
* **Controlador (Controller)**: recibe acciones del usuario y llama a los servicios. Su aplicación no está implementada aún; se hará para la versión web.
* **Servicios (Service)**: contiene la lógica del sistema, como calcular balances, validar datos o generar deudas.
* **DAO**: maneja el acceso a la base de datos (lectura y escritura).
* **Modelo (Model)**: clases que representan entidades del sistema (`User`, `Expense`, etc.).
* **DTO (Data Transfer Objects)**: objetos para comunicar capas, útiles cuando no se quiere usar directamente el modelo.
* **Caché**: almacenamiento temporal en memoria que actúa como repositorio local para mejorar el rendimiento.

### Clases Principales

* **User**: representa al usuario (id, nombre, email, contraseña). Se relaciona con todas las demás clases.
* **Transaction** *(abstracta)*: representa cualquier transacción monetaria.

   * **Expense**: hereda de `Transaction`, representa un gasto compartido. Incluye una o más **Debts**, cada una indicando quién le debe a quién, cuánto y cuándo.
   * **Payment**: hereda de `Transaction`, representa un pago directo entre dos usuarios.
* **Debt**: indica lo que un usuario le debe a otro por un gasto compartido. Está asociada a un `Expense` y contiene fecha de vencimiento.
* **Response**: clase genérica para retornar objetos, estados y mensajes entre capas.

---

## Validaciones y Manejo de Errores

* Las **validaciones** y el manejo de **excepciones** se realizan en la capa de servicios (`Service`).
* Se verifica que:

   * Los campos obligatorios no estén vacíos o nulos.
   * Los valores numéricos sean válidos (montos positivos, fechas correctas, etc.).
   * No haya duplicados cuando corresponda.
* Si una validación falla, se devuelve un objeto `Response` con `success = false` y un mensaje de error.
* Todas las operaciones con la base de datos están contenidas en bloques `try-catch` que capturan errores y devuelven un mensaje comprensible al usuario.

---

## Instalación y Ejecución

1. **Clonar el repositorio:**

   ```bash
   git clone https://github.com/usuario/sabiodivisor.git
   ```

2. **Instalar XAMPP y configurar la base de datos:**

   * Descargar e instalar XAMPP desde [Apache Friends](https://www.apachefriends.org/index.html).
   * Iniciar Apache y MySQL desde el panel de control de XAMPP.
   * Importar el archivo `DDL_sabio_divisor.sql` en **phpMyAdmin** para crear las tablas.
   * Requisitos:

      * MySQL versión **8.0.16 o superior** (usa `CHECK` constraints).
   * Configurar `config.properties` con los datos de conexión:

     ```properties
     db.url=jdbc:mysql://localhost:3306/sabio_divisor
     db.user=tu_usuario
     db.password=tu_contraseña
     ```
   * Verificar que `config.properties` esté en la ruta correcta del proyecto.

3. **Compilar el proyecto** en IntelliJ IDEA o Eclipse.

4. **Ejecutar la aplicación:**

   * Correr `Main.java` como clase principal.

5. **Uso general:**

   * Iniciar sesión o crear un nuevo usuario.
   * Registrar gastos, dividirlos entre usuarios, registrar pagos y consultar deudas.
   * Utilizar el botón de salida para cerrar la aplicación.
   * Para pruebas, crear varios usuarios y registrar distintos gastos/pagos.
   * Acceder al estado de cuenta desde la interfaz gráfica.

---

## Requisitos del Sistema

* Java 17 o superior
* MySQL 8.0.16 o superior
* XAMPP (con Apache y MySQL)
* IDE recomendado: IntelliJ IDEA
