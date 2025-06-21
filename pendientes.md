# Pendientes
* Armar tabla de Debt y Expense
* Agregar Java Swing
* Reeditar los toStrings
* Revisar patrón Singleton de la BBDD

## Notas de mi corrección de la presentación
* Lautaro me pidió: "Definir si vas a separar las entidades del dominio de las vistas mediante DTOs o mappers."
-> Averiguar que es un mapper.
* Dejar constancia de cómo se manejarán validaciones y excepciones en las capas intermedias. 
### Sugerencias de mejora:
* Evaluar la creación de una clase Cuota si se va a modelar el gasto en pagos periódicos. 
* Considerar una clase abstracta Transaccion, si Pago y Gasto comparten atributos o métodos comunes. 
* La clase Resumen podría concebirse como un DTO si su uso es solo para visualización.