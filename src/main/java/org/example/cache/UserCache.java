package org.example.cache;

import org.example.dao.UserDAO;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.example.model.Response;
import org.example.service.UserService;

import java.util.HashMap;
import java.util.Map;

/*
Para no tener que rearmar de la BBDD cada vez que utilice un usuario,
 arme un cache o repository de usuarios
*/

public class UserCache {

    private static UserCache instance = new UserCache();

    private final UserDAO userDAO;
    private final UserService userService = new UserService();

    private final Map<Integer, UserDTO> userCache = new HashMap<>();

    public static UserCache getInstance() {
        if (instance == null) {
            instance = new UserCache();
        }
        return instance;
    }

    private UserCache() {
        this.userDAO = UserDAO.getInstance();
        loadUsers(); // Llamo al metodo directamente porque quiero que siempre se instancie ya cargado
    }

    private void loadUsers() {
        Response<User> response = userDAO.readAll();
        if (response.isSuccess()) {
            for (User user : response.getData()) {
                // Convertir User a UserDTO si es necesario
                UserDTO userDTO = userService.convertToDTO(user);

                userCache.put(user.getId(), userDTO);
            }
        }
    }

    public Map<Integer, UserDTO> getAllUsers() {//Devuelve todos los usuarios del cache
        return userCache;
    }

    public Response<UserDTO> getById(int id) {//Devuelve un usuario
        if (userCache.containsKey(id)) {// Verifica si el usuario está en el cache. Dado que es un Map, la búsqueda es más fácil.
            return new Response<>(true, "200", "OK", userCache.get(id));
        } else {
            return new Response<>(false, "404", "El usuario no está en el cache");
        }
    }

    public Response<User> getFalseUserById(int id) {//Devuelve un usuario con contraseña nula, para que no pasar esa información entre capas. Pero se utiliza User, y no UserDTO, para poder respetar otros modelos que tienen composición con User.
        if (userCache.containsKey(id)) {
            UserDTO userDTO = userCache.get(id);
            User user = new User(userDTO.getId(), userDTO.getName(), userDTO.getEmail(), null);
            return new Response<>(true, "200", "OK", user);
        } else {
            return new Response<>(false, "404", "El usuario no está en el cache");
        }
    }
    public static void reset() {
        if (instance != null) {
            instance.userCache.clear(); // Limpia la lista de pagos
        }
        instance = null;
    }
}