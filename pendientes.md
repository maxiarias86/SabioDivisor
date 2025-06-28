# Pendientes
* Reeditar los toStrings
* Armar README para que tenga todo lo necesario para instalarlo en una computadora desde cero.
* Instalar xampp en otra computadora (ponerlo tambien en readme).

## Notas de mi corrección de la presentación
* Lautaro me pidió: "Definir si vas a separar las entidades del dominio de las vistas mediante DTOs o mappers."
-> Averiguar que es un mapper.
* Dejar constancia de cómo se manejarán validaciones y excepciones en las capas intermedias. 
### Sugerencias de mejora:
* Evaluar la creación de una clase Cuota si se va a modelar el gasto en pagos periódicos. 
* Considerar una clase abstracta Transaccion, si Pago y Gasto comparten atributos o métodos comunes. 
* La clase Resumen podría concebirse como un DTO si su uso es solo para visualización.

### Cambios para a hacer en mi proyecto:
* Cambiar en JPanelAllUsers el método getAllUsersButOne() por el getAllUsers().
* Que no se puedan generar gastos sin deudas. Por ejemplo: Maxi pago 1000 y tenía que pagar 1000, entonces como nadie debe a nadie se genera el gasto pero no la deuda.
* Ver si vale la pena tener un PaymentDTO o lo cambio directamente por Payment en las views dado que no pasa información sensible.
* Armar un metodo que busque la descripcion del expense que da origen a la deuda. Convendia tenerlas en un cache para no ir a la base de datos.