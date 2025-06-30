package org.example.cache;

import org.example.dao.ExpenseDAO;
import org.example.dto.UserDTO;
import org.example.model.Debt;
import org.example.model.Expense;
import org.example.model.Response;

import java.util.*;

public class ExpenseCache {// Cache para almacenar los gastos de un usuario
    private static ExpenseCache instance;// Singleton para asegurar que solo haya una instancia de ExpenseCache

    private List<Expense> expenses = new ArrayList<>();// Lista que almacena los gastos del usuario

    private ExpenseCache() {
    }

    public static ExpenseCache getInstance(UserDTO userDTO) {
        if (instance == null) {
            instance = new ExpenseCache();
            instance.updateExpenseCache(userDTO);
        }
        return instance;
    }

    public void updateExpenseCache(UserDTO user) {//Metodo para llenar el cache de gastos desde las deudas del usuario (UserCache)
        DebtCache debtCache = DebtCache.getInstance(user);
        ExpenseDAO expenseDAO = ExpenseDAO.getInstance();

        Set<Integer> idsUnicos = new HashSet<>();// Uso un Set para evitar gastos duplicados
        List<Expense> nuevasExpenses = new ArrayList<>();// Lista para almacenar los nuevos gastos

        for (Debt d : debtCache.getDebts()) {// Itero sobre las deudas del DebtCache
            int expenseId = d.getExpenseId();// Obtengo el ID del gasto asociado a la deuda
            if (idsUnicos.contains(expenseId)){
                continue;// Si el ID ya está en el Set, lo salto para evitar duplicados
            }

            Response<Expense> response = expenseDAO.read(expenseId);// Llamo al DAO para obtener el gasto asociado a la deuda
            if (response.isSuccess() && response.getObj() != null) {// Si la respuesta es exitosa y el gasto no es nulo
                nuevasExpenses.add(response.getObj());// Agrego el gasto a la lista de nuevos gastos
                idsUnicos.add(expenseId);// Agrego el ID del gasto al Set para evitar duplicados
            }
        }

        this.expenses = nuevasExpenses;// Actualizo la lista de gastos del cache con los nuevos gastos obtenidos
    }

    public Response<Expense> getExpenseById(int id) {// Metodo para obtener un gasto por su ID
        try{
            for (Expense expense : expenses) {// Itero sobre la lista de gastos
                if (expense.getId() == id) {// Si el ID del gasto coincide con el ID buscado
                    return new Response<>(true,"200","Gasto encontrado",expense);// Retorno el gasto encontrado
                }
            }
            Expense notFound = new Expense();
            notFound.setDescription("Gasto no encontrado");// Si no se encuentra el gasto, retorna null salvo la descripción, para que se vea el error en BalancesJPanel.
            return new Response<>(false,"404","Gasto no encontrado",notFound);// Si no se encuentra el gasto, retorno un Response con error 404
        }catch(Exception e){
            return new Response (false,"500","Error al buscar el gasto asociado a esa deuda",e.getMessage());
        }
    }

    public Response deleteExpense(int id) {// Metodo para eliminar un gasto por su ID
        try {
            for (Expense expense : expenses) {// Itero sobre la lista de gastos
                if (expense.getId() == id) {// Si el ID del gasto coincide con el ID a eliminar
                    expenses.remove(expense);// Elimino el gasto de la lista
                    return new Response<>(true,"200","Gasto eliminado correctamente",null);// Retorno un Response exitoso
                }
            }
            return new Response<>(false,"404","Gasto no encontrado",null);// Si no se encuentra el gasto, retorno un Response con error 404
        } catch (Exception e) {
            return new Response<>(false,"500","Error al eliminar el gasto",e.getMessage());// Si ocurre un error, retorno un Response con error 500
        }
    }

    public void reset() {// Metodo para resetear la instancia del cache al hacer logout
        if(instance != null) {
            this.expenses.clear();// Limpia la lista de gastos
            instance = null;// Resetea la instancia para que se pueda volver a crear
        }
    }

    public List<Expense> getExpenses() {return expenses;}
}