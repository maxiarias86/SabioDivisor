package org.example.service;

import org.example.cache.DebtCache;
import org.example.cache.ExpenseCache;
import org.example.cache.UserCache;
import org.example.dao.DebtDAO;
import org.example.dao.ExpenseDAO;
import org.example.dao.UserDAO;
import org.example.dto.ExpenseDTO;
import org.example.dto.UserDTO;
import org.example.model.Debt;
import org.example.model.Expense;
import org.example.model.User;
import org.example.model.Response;

import java.time.LocalDate;
import java.util.*;

public class ExpenseService {

    public Response loadDebt(int expenseId, UserDTO userDTO) {
        try {
        /*
        Este metodo carga las deudas de un gasto específico al cache del usuario.
        Primero obtiene el Expense por su ID, luego obtiene las deudas asociadas a ese Expense,
        y finalmente las agrega al cache filtrandolas por usuario.
         */
            //Llamo al DebtCache.
            DebtCache debtCache = DebtCache.getInstance(userDTO);
            //Llamo al DebtDAO.
            ArrayList<Debt> debts;
            Response<Debt> response = DebtDAO.getInstance().readAllByExpenseAndUser(expenseId, userDTO);
            if (!response.isSuccess()) {
                return new Response<>(false, response.getCode(), response.getMessage());
            } else {
                debts = new ArrayList<>(response.getData());
                if (debts.isEmpty()) {
                    return new Response<>(false, "404", "No se encontraron deudas para el gasto especificado.");
                }
                //Agrego las deudas al cache del usuario.
                for (Debt debt : debts) {
                    debtCache.addDebt(debt);
                }
                return new Response<>(true, "200", "Deudas cargadas al cache correctamente.", debts);
            }
            //Busco deudas por expenseId y userDTO y las agrego a un arraylist.
            //Agrego eso al cache de deudas del usuario.

        } catch (Exception e) {
            return new Response<>(false, "500", "Error al cargar las deudas al cache.");
        }
    }

    public Response<Expense> deleteExpense(int id, UserDTO userDTO) {
        try {
            //Llamo al ExpenseDAO para eliminar el gasto por su ID.
            Response<Expense> response = ExpenseDAO.getInstance().delete(id);
            if (!response.isSuccess()) {
                return new Response<>(false, response.getCode(), response.getMessage());
            }
            //Si se eliminó correctamente, actualizo el cache de gastos.
            ExpenseCache expenseCache = ExpenseCache.getInstance(userDTO);
            expenseCache.deleteExpense(id);

            return new Response<>(true, "200", "Gasto eliminado correctamente.", response.getObj());
        } catch (Exception e) {
            return new Response<>(false, "500", "Error al eliminar el gasto.");
        }
    }

    public Response generateDebtsFromDTO(ExpenseDTO dto) {
        UserDAO userDAO = UserDAO.getInstance();
        try {
            if (dto.getAmount() <= 0 || dto.getPayers().isEmpty() || dto.getDebtors().isEmpty()) {//Si el monto es menor o igual a 0 o si no hay pagadores o deudores, tira error.
                return new Response<>(false, "400", "Datos insuficientes para registrar el gasto.");
            }

            double totalPayed = 0;//Inicializa el total pagado en 0.
            for (Double amount : dto.getPayers().values()) {//Obtiene todos los valores pagados.
                totalPayed += amount;
            }

            if (Math.abs(totalPayed - dto.getAmount()) > 0.01) {//Si la diferencia entre ambos es mayor a 0.01 tira error. Se deja 0.01 por errores de redondeo.
                return new Response<>(false, "404", "La suma de los pagos no coincide con el monto total.");
            }

            //Calcula balances individuales
            Map<Integer, Double> balanceMap = new HashMap<>();// Crea un mapa para almacenar los balances de cada usuario, donde la clave es el ID del usuario y el valor es el saldo.

            for (Map.Entry<Integer, Double> entry : dto.getPayers().entrySet()) {//Recorre cada pagador del Map de los pagadores dentro del ExpenseDTO y sus montos.
                int userId = entry.getKey();// Obtiene el ID del usuario pagador
                double pagado = entry.getValue();// Obtiene el monto pagado por el usuario
                balanceMap.put(userId, pagado);// Agrega al balanceMap el ID del usuario y el monto pagado.
            }

            for (Map.Entry<Integer, Double> entry : dto.getDebtors().entrySet()) {//Recorre cada deudor del Map de los deudores dentro del ExpenseDTO y sus montos.
                int userId = entry.getKey();// Obtiene el ID del usuario deudor
                double deuda = entry.getValue();// Obtiene el monto que debe el usuario
                double balanceActual;
                if (balanceMap.containsKey(userId)) {// Verifica si el usuario ya está en el balanceMap
                    balanceActual = balanceMap.get(userId);// Si está, obtiene su balance actual
                } else {
                    balanceActual = 0.0;// Si no está, inicializa su balance en 0.0
                }
                double nuevoBalance = balanceActual - deuda;// Calcula el nuevo balance restando la deuda al balance actual
                balanceMap.put(userId, nuevoBalance);//Reemplaza el valor previo
            }

            //Listas separadas
            List<User> creditors = new ArrayList<>();// Lista de acreedores
            List<User> debtors = new ArrayList<>();// Lista de deudores

            for (Map.Entry<Integer, Double> entry : balanceMap.entrySet()) {// Recorre el balanceMap para separar los usuarios en acreedores y deudores.
                int userId = entry.getKey();// Obtiene el ID del usuario
                double balance = entry.getValue();// Obtiene el balance del usuario
                if (balance == 0) {
                    continue;//Si pago justo lo saltea
                }
                Response userResponse = userDAO.read(userId);// Obtiene el usuario por su ID desde el UserDAO.
                if (!userResponse.isSuccess()) {
                    return new Response<>(false, "404", "El usuario con ID " + userId + " no existe.");
                }
                User user = (User) userResponse.getObj();
                if (user == null) {//
                    return new Response<>(false, "404", "El usuario con ID " + userId + " no existe.");
                }
                if (balance > 0) {
                    creditors.add(user);// Lo pone en una lista u otra de acuerdo a si debe o no
                } else {
                    debtors.add(user);
                }
            }

            List<Debt> debts = new ArrayList<>();// Lista para almacenar las deudas que se van a generar

            int installments = dto.getInstallments();// Obtiene el número de cuotas del ExpenseDTO
            if (installments <= 0) {// Si el número de cuotas es menor o igual a 0, devuelve un error.
                return new Response<>(false, "400", "El número de cuotas debe ser mayor o igual a 1.");
            }
            if (creditors.isEmpty() || debtors.isEmpty()) {// Si no hay acreedores o deudores, devuelve un error.
                return new Response<>(false, "404", "No hay acreedores o deudores para generar deudas.");
            }
            //Generar deudas
            for (User debtor : debtors) {//Primero un deudor...
                int debtorId = debtor.getId();
                double pendiente = -balanceMap.get(debtorId);//Como el saldo es negativo lo paso a positivo

                for (User creditor : creditors) {//Tomo el primero de los acreedores
                    int creditorId = creditor.getId();
                    double disponible = balanceMap.get(creditorId);//Saldo que le deben

                    if (pendiente <= 0) break; // El deudor ya no debe nada, corto este for para que tome a otro deudor
                    if (disponible <= 0) continue; // este acreedor ya cobró, paso a la proxima iteracion de este for
                    //Dicen <=0 por un minimo error de redondeo que pueda llegar a haber.

                    double monto = Math.min(pendiente, disponible);//El mínimo entre lo que el deudor aún debe y lo que el acreedor aún debe recibir

                    double cuota = monto / installments;//Se divide por la cantidad de cuotas

                    for (int i = 1; i <= installments; i++) {
                        debts.add(new Debt(0, cuota, creditor, debtor, 0, dto.getDate().plusMonths(i - 1), i));
                        //El expenseId es 0 porque todavia no existe el Expense, se va a cargar en la capa DAO, en ExpenseDAO.save().
                        //El id de la Debt se genera automaticamente en la BBDD.
                    }

                    balanceMap.put(creditorId, disponible - monto);//Actualiza el balanceMap, pasa el ID y al disponible le resta el monto.
                    pendiente -= monto;
                }
            }
            return new Response<>(true, "200", "Deudas generadas correctamente.", debts);
        } catch (Exception e) {
            return new Response<>(false, "500", "Error al generar deudas.");
        }
    }

    public Response<Integer> registerExpense(ExpenseDTO dto) {//Usa expenseDAO.save() para guardar el Expense y las deudas generadas.
        Response debtsResponse = this.generateDebtsFromDTO(dto);//Llama al metodo para generar las deudas a partir del DTO del gasto.
        if (!debtsResponse.isSuccess()) {
            return new Response<>(false, debtsResponse.getCode(), debtsResponse.getMessage());
        }
        List<Debt> debts = (List<Debt>) debtsResponse.getData();//Obtiene las deudas generadas por el metodo anterior.
        try {
            Response expenseDAOResponse = ExpenseDAO.getInstance().save(dto, debts);//Llama al DAO para guardar el Expense y las deudas generadas.
            if (!expenseDAOResponse.isSuccess()) {
                return new Response<>(false, expenseDAOResponse.getCode(), expenseDAOResponse.getMessage());
            }
            return new Response<>(true, "200", "Gasto creado correctamente.", (Integer) expenseDAOResponse.getObj());// Devuelve el ID del Expense creado para poder buscar las Debt asociadas y agregarlas al Cache
        } catch (Exception e) {
            return new Response<>(false, "500", "Error al crear el expense.");
        }
    }

    public Response<Expense> updateExpense(ExpenseDTO expenseDTO, UserDTO userDTO) {// Este metodo se puede usar para actualizar un gasto. No usa el expenseDAO.save() porque no se actualizan las deudas. Esto se hace por separado. Utiliza el .update().

        try {
            // Verifica que el ID del gasto sea válido
            if (expenseDTO.getId() == null || expenseDTO.getId() <= 0) {
                return new Response<>(false, "400", "El ID del gasto no es válido.");
            }
            Response debtsGenerationResponse = this.generateDebtsFromDTO(expenseDTO);// Llama al metodo para generar las deudas a partir del DTO del gasto.
            if(!debtsGenerationResponse.isSuccess()) {
                return new Response<>(false, debtsGenerationResponse.getCode(), debtsGenerationResponse.getMessage());
            }

            List<Debt> debts = debtsGenerationResponse.getData();// Obtiene las deudas generadas por el metodo anterior.

            Expense expense = new Expense();// Crea un nuevo objeto Expense para actualizarlo.
            expense.setAmount(expenseDTO.getAmount());// Asigna el monto, día, número de cuotas, id, deudas y descripción del DTO al Expense.
            expense.setDate(expenseDTO.getDate());
            expense.setInstallments(expenseDTO.getInstallments());
            expense.setDescription(expenseDTO.getDescription());
            expense.setId(expenseDTO.getId());
            expense.setDebts(debts);

            Response<Expense> response = ExpenseDAO.getInstance().update(expense);// Llama al DAO para actualizar el gasto con el nuevo objeto Expense.
            if (!response.isSuccess()) {
                return new Response<>(false, response.getCode(), response.getMessage());
            }
            // Ahora hay que actualizar las debts asociadas al gasto.
            // Primero borro las deudas asociadas al gasto
            Response deleteResponse = DebtDAO.getInstance().deleteByExpenseId(expense.getId());// Llama al DAO para eliminar las deudas asociadas al gasto por su ID.
            if(!deleteResponse.isSuccess()){// Si no se pudo eliminar las deudas, devuelve un error.
                return new Response<>(false, deleteResponse.getCode(), deleteResponse.getMessage());
            }
            // Luego guardo las nuevas deudas generadas
            for (Debt debt : debts) {// Recorre las deudas generadas y las guarda en la base de datos.
                debt.setExpenseId(expense.getId()); // Asigna el ID del gasto a cada deuda
                Response<Debt> debtResponse = DebtDAO.getInstance().create(debt);
                if (!debtResponse.isSuccess()) {
                    return new Response<>(false, debtResponse.getCode(), debtResponse.getMessage());
                }
            }
            // Actualiza el cache de gastos
            try{// Actualiza el cache de gastos y deudas del usuario.
                ExpenseCache expenseCache = ExpenseCache.getInstance(userDTO);
                expenseCache.updateExpenseCache(userDTO);
                DebtCache debtCache = DebtCache.getInstance(userDTO);
                debtCache.updateDebtCache(userDTO);
            } catch (Exception e) {// Si no se pudo actualizar el cache, devuelve un error.
                return new Response<>(false, "500", "Error al actualizar el cache.");
            }

            return new Response<>(true, "200", "Gasto actualizado correctamente.", response.getObj());
        } catch (Exception e) {
            return new Response<>(false, "500", "Error al actualizar el gasto: " + e.getMessage());
        }
    }

}