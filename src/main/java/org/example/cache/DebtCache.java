package org.example.cache;

import org.example.dao.DebtDAO;
import org.example.dto.UserDTO;
import org.example.model.Debt;

import java.util.ArrayList;
import java.util.List;

public class DebtCache {

    private static DebtCache instance;
    private ArrayList<Debt> debts;

    private DebtCache(UserDTO userDTO) {
        // Constructor privado para evitar instanciación externa
        this.debts = new ArrayList<>(DebtDAO.getInstance().readAllByUser(userDTO).getData());
    }

    public static DebtCache getInstance(UserDTO userDTO) {
        if (instance == null) {
            instance = new DebtCache(userDTO);
        }
        return instance;
    }

    public List<Debt> getOtherUserDebts(UserDTO friend) {// Obtiene las deudas entre el usuario actual y un amigo
        List<Debt> otherUserDebts = new ArrayList<>();// Inicializa una lista para almacenar las deudas entre el usuario actual y el amigo
        for (Debt debt : debts) {
            if (debt.getCreditor().getId() == friend.getId() || debt.getDebtor().getId() == friend.getId()) { // Verifica si la deuda es entre el usuario actual y el amigo
                otherUserDebts.add(debt);
            }
        }
        return otherUserDebts;
    }

    public ArrayList<Debt> getDebts() {
        return debts;
    }
    public void addDebt(Debt debt) {
        this.debts.add(debt);
    }

    // Metodo para borrar el cache al hacer logout
    public static void reset() {
        instance = null;
    }
}