package org.example.cache;

import org.example.dao.DebtDAO;
import org.example.dto.UserDTO;
import org.example.model.Debt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Aquí podrías agregar métodos para manejar las deudas, como agregar, eliminar o buscar deudas

    public List<Debt> getOtherUserDebts(UserDTO friend) {
        List<Debt> otherUserDebts = new ArrayList<>();
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
