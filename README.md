# SabioDivisor
### Segundo Parcial Programación Avanzada 2025
## Descripción del Proyecto: Sistema de Gestión de Gastos Compartidos
Este sistema permite a los usuarios registrar gastos compartidos con otros, dividirlos y gestionar deudas. Los gastos pueden tener una o más cuotas. Al finalizar cada mes, se genera un estado de cuenta mensual para cada usuario con el detalle de sus deudas. Los usuarios pueden realizar pagos entre sí para cancelar deudas o registrar préstamos.

### Funcionalidades Principales:
* Gestión de Usuarios: Registro, inicio de sesión y edición de perfil.
* Carga de Gastos: Los usuarios ingresan gastos detallando monto, descripción, fecha, categoría, forma de pago, moneda y cuotas.
* División de Gastos: Los gastos se reparten entre participantes en partes iguales o proporciones personalizadas.
* Pagos: Registro de pagos entre usuarios, ya sea para saldar deudas o prestar dinero.
* Gestión Multimoneda: Soporte para pesos argentinos y dólares. Los gastos en dólares se convierten a pesos al tipo de cambio oficial del primer día del mes siguiente, al calcular deudas y estados de cuenta.
## Modelo de Datos (Tablas):
* users: id, name, mail, password, token
* expenses: id, description, amount, date, category, installments
* payers / costBearers: usuario, monto, gasto
* payments: deudor, acreedor, monto, fecha
## Casos de Uso:
* Registro y edición de usuario
* Inicio de sesión
* Carga y división de gastos
* Cálculo de deudas
* Generación de estado de cuenta mensual
* Registro de pagos
* Conversión de monedas
## Diseño Orientado a Objetos:
* User: id, name, mail, password, token
* Expense: Gasto que incluye fecha, monto, descripción, cuotas, moneda, método de pago, y composición con Payer y CostBearer
* Payer: Mapa de usuarios y montos pagados
* CostBearer: Mapa de usuarios y montos adeudados
* Payment: Representa una transferencia entre deudor y acreedor, o en su defecto, un préstamo de dinero
* Enum Currency: PESO, DOLAR
* Enum PaymentMethod: EFECTIVO, DEBITO, CREDITO

*La clase Expense utiliza composición, incorporando objetos Payer y CostBearer para representar quién pagó y quién debe pagar, respectivamente. Esto facilita el cálculo preciso de deudas y refuerza la cohesión del modelo.*
