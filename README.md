# \[TP2]

## SabioDivisor

**Gestor de Gastos Compartidos - App de Escritorio**
Carrera: Analista de Sistemas
Materia: Programación Avanzada
Alumno: Maximiliano Martin Arias
Comisión: ACN3BV
Profesor: Lautaro Chiappero
[lautaro.chiappero@davinci.edu.ar](mailto:lautaro.chiappero@davinci.edu.ar)

---

## Descripción del Proyecto

SabioDivisor es una aplicación de escritorio desarrollada en Java para gestionar gastos compartidos entre múltiples usuarios. Está pensada para situaciones como compartir una casa, un viaje, o una cena entre amigos.

Cada usuario puede registrar lo que pagó, repartirlo entre quienes corresponda, ver quién le debe a quién y también registrar pagos directos entre usuarios. El sistema mantiene un estado de cuenta actualizado que muestra saldos y deudas entre personas.

---

## Funcionalidades Principales

* Registro, edición, login y logout de usuarios (no se permite eliminar usuarios).
* Registro de gastos con: fecha, monto, descripción, número de cuotas, pagadores y deudores.
* División de gastos: uno o más pagadores, uno o más deudores.
* Cálculo automático de deudas a partir de los gastos ingresados.
* Registro de pagos directos entre dos usuarios.
* Visualización de deudas personales y estado de cuenta.
* Consulta de estado de cuenta proyectado a una fecha futura (considerando cuotas pendientes).

---

## Diseño y Arquitectura

La aplicación sigue una arquitectura en capas, con separación de responsabilidades:

* **Vista (View):** contiene los formularios y pantallas (JFrame, JPanel).
* **Controlador (Controller):** recibe acciones del usuario y llama a los servicios. Su aplicación no está implementada aún; se hará para la versión web.
* **Servicios (Service):** contiene la lógica del sistema, como calcular balances, validar datos o generar deudas.
* **DAO:** maneja el acceso a la base de datos (lectura y escritura).
* **Modelo (Model):** clases que representan entidades del sistema (User, Expense, etc.).
* **DTO (Data Transfer Objects):** objetos para comunicar capas, útiles cuando no se quiere usar directamente el modelo.
* **Caché:** almacenamiento temporal en memoria que actúa como repositorio local para mejorar el rendimiento.

---

## Clases Principales

* **User:** representa al usuario (id, nombre, email, contraseña). Se relaciona con todas las demás clases.
* **Transaction (abstracta):** representa cualquier transacción monetaria.
* **Expense:** hereda de Transaction, representa un gasto compartido. Incluye una o más Debts, cada una indicando quién le debe a quién, cuánto y cuándo.
* **Payment:** hereda de Transaction, representa un pago directo entre dos usuarios.
* **Debt:** indica lo que un usuario le debe a otro por un gasto compartido. Está asociada a un Expense y contiene fecha de vencimiento.

---

## Validaciones y Manejo de Errores

Las validaciones y el manejo de excepciones se realizan en la capa de servicio (Service) y en la capa de acceso a la base de datos (DAO).

Se verifica que:

* Los campos obligatorios no estén vacíos o nulos.
* Los valores numéricos sean válidos (montos positivos, fechas correctas, etc.).
* No haya duplicados cuando corresponda.

Si una validación falla, se devuelve un objeto `Response` con `success = false` y un mensaje de error.

Todas las operaciones con la base de datos están contenidas en bloques `try-catch` que capturan errores y devuelven un mensaje comprensible al usuario.

---

## Requisitos del sistema:

* Java 17 o superior
* MySQL 8.0.16 o superior *(si usás otra versión, eliminar CHECK CONSTRAINT del script SQL)*
* Maven 3.9.x
* XAMPP (Apache y MySQL)
* IDE recomendado: IntelliJ IDEA

---

## Instalación y Ejecución

### Instalar Java (JDK 21)

* Descargar desde: [https://jdk.java.net/21/](https://jdk.java.net/21/)
* Instalar en: `C:\Program Files\Java\jdk-21`
* Crear variable de entorno:

    * Nombre: `JAVA_HOME`
    * Valor: `C:\Program Files\Java\jdk-21`
* Agregar al PATH del sistema:

    * Nuevo valor: `%JAVA_HOME%\bin`
* Verificar desde la terminal:

  ```sh
  java -version
  ```

### Instalar Maven (3.9.x)

* Descargar desde: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
* Descomprimir en: `C:\Program Files\Apache\Maven\apache-maven-3.9.10`
* Crear variable de entorno:

    * Nombre: `MAVEN_HOME`
    * Valor: `C:\Program Files\Apache\Maven\apache-maven-3.9.10`
* Agregar al PATH del sistema:

    * Nuevo valor: `%MAVEN_HOME%\bin`
* Verificar desde la terminal:

  ```sh
  mvn -version
  ```

### Instalar Git

* Descargar desde: [https://git-scm.com/downloads](https://git-scm.com/downloads)

### Clonar y compilar el proyecto

```sh
git clone https://github.com/maxiarias86/SabioDivisor.git
cd SabioDivisor
mvn clean compile
```

### Configurar la base de datos (XAMPP)

* Descargar e instalar XAMPP desde [https://www.apachefriends.org/es/index.html](https://www.apachefriends.org/es/index.html)
* Iniciar Apache y MySQL desde el panel de control de XAMPP
* Importar el archivo `DDL_sabio_divisor.sql` en phpMyAdmin
* En caso de querer hacer pruebas con una base de datos con filas ya cargadas (INSERTS), importar el archivo: `sabio_divisor_completo.sql`
  &#x20;

### Ejecutar el proyecto

* Abrir el proyecto en IntelliJ IDEA o Eclipse
* Ejecutar la clase principal `Main.java`

### Alternativa: Ejecutar el proyecto con Maven

Para usar Maven desde línea de comandos, se debe tener el siguiente bloque en el archivo `pom.xml` (ya configurado en el repositorio):

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>exec-maven-plugin</artifactId>
      <version>3.1.0</version>
      <configuration>
        <mainClass>org.example.Main</mainClass>
      </configuration>
    </plugin>
  </plugins>
</build>
```

Luego compilar y ejecutar el proyecto desde la raíz con:

```sh
mvn compile
mvn exec:java
```

---

## Uso general:

* Iniciar sesión o crear un nuevo usuario.
* Registrar gastos, dividirlos entre usuarios, registrar pagos y consultar deudas.
* Utilizar el botón de salida para cerrar la aplicación.
* Para pruebas, crear varios usuarios y registrar distintos gastos/pagos.
* Acceder al estado de cuenta desde la interfaz gráfica.
