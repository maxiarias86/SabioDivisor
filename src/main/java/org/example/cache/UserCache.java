package org.example.cache;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.model.Response;

import java.util.HashMap;
import java.util.Map;

/*
Para no tener que rearmar de la BBDD cada vez que utilice un usuario,
 arme un cache o repository de usuarios
*/

public class UserCache {

    private final static UserCache instance = new UserCache();

    private final UserDAO userDAO;

    private final Map<Integer, User> userCache = new HashMap<>();

    public static UserCache getInstance() {
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
                userCache.put(user.getId(), user);
            }
        }
    }

    //Este metodo llena el cache de a uno. No le encuentro uso aún
    public Response<User> findById(int id) {
        try{
            if(!userCache.containsKey(id)){
                Response getUser = userDAO.read(id);
                if(getUser.isSuccess()){
                    User user = (User) getUser.getObj();
                    userCache.put(id, user);
                }else{
                    return getUser;
                }
            }
            return new Response<>(true, "200", "OK", userCache.get(id));
        }catch(Exception e){
            return new Response<>(false, "500", "Internal Server Error");
        }
    }

    //Devuelve todos los usuarios del cache
    public Map<Integer, User> getAllUsers() {
        return userCache;
    }

    //Devuelve un usuario
    public Response<User> getById(int id) {
        if (userCache.containsKey(id)) {
            return new Response<>(true, "200", "OK", userCache.get(id));
        } else {
            return new Response<>(false, "404", "El usuario no está en el cache");
        }
    }
}
