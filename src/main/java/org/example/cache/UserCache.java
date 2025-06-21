package org.example.cache;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.model.Response;

import java.util.HashMap;
import java.util.Map;

/*Tuve que crear una lista de los usuarios para que la usen las vistas y el usuario pueda seleccionar
* el ID cuando ingresa un gasto nuevo */

public class UserCache {
    private static final UserCache instance = new UserCache();
    private final Map<Integer, User> userMap = new HashMap<>();

    private UserCache() {
        loadUsers();
    }

    public static UserCache getInstance() {
        return instance;
    }

    private void loadUsers() {
        Response<User> response = UserDAO.getInstance().readAll();
        if (response.isSuccess()) {
            for (User user : response.getData()) {
                userMap.put(user.getId(), user);
            }
        }
    }

    public Map<Integer, User> getAllUsers() {
        return userMap;
    }

    public User getById(int id) {
        return userMap.get(id);
    }
}
