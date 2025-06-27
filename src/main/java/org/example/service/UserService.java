package org.example.service;

import org.example.dto.UserDTO;
import org.example.dao.UserDAO;
import org.example.model.Response;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {


    public Response<Integer> registerUser(User user) {// Devuelve una response con el int userId
        if (user.getName() == null || user.getName().isBlank()) {
            return new Response<>(false, "400", "El nombre de usuario no puede estar vacío.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return new Response<>(false, "400", "El correo electrónico no puede estar vacío.");
        }
        if (!user.getEmail().contains("@")) {
            return new Response<>(false, "400", "El correo electrónico debe contener '@'.");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return new Response<>(false, "400", "La contraseña no puede estar vacía.");
        }
        try{
            if (UserDAO.getInstance().readByName(user.getName()).isSuccess()) {//Busca el nombre en la base de datos
                return new Response<>(false, "409", "El nombre de usuario ya está registrado.");
            }
            if (UserDAO.getInstance().readByEmail(user.getEmail()).isSuccess()) {//Busca el mail en la base de datos
                return new Response<>(false, "409", "El correo electrónico ya está registrado.");
            }


            Response<User> response = UserDAO.getInstance().create(user);//Guarda la instancia en BBDD
            if (response.isSuccess()) {
                return new Response<>(true, "201", "Usuario creado exitosamente.", response.getObj().getId());
            } else {
                return new Response<>(false, "500", "No se pudo insertar el registro.");
            }

        } catch (Exception e) {
            return new Response<>(false, "500", "Error inesperado al registrar el usuario: " + e.getMessage());
        }
    }

    public Response editUser(User user){
        //Primero valida el DTO
        if (user.getName() == null || user.getName().isBlank()) {
            return new Response<>(false, "400", "El nombre de usuario no puede estar vacío.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return new Response<>(false, "400", "El correo electrónico no puede estar vacío.");
        }
        if (!user.getEmail().contains("@")) {
            return new Response<>(false, "400", "El correo electrónico debe contener '@'.");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return new Response<>(false, "400", "La contraseña no puede estar vacía.");
        }
        if (user.getId() <=0){
            return new Response<>(false, "400", "El ID del usuario no es válido.");
        }
        try{
            //Usa el DAO para traerse el objeto de la base
            Response<User> userResponse = UserDAO.getInstance().read(user.getId());
            if (!userResponse.isSuccess()) {
                return new Response<>(false, "404", "Usuario no encontrado.");
            }
            User original = userResponse.getObj();
            if (!original.getName().equalsIgnoreCase(user.getName())){
                if (UserDAO.getInstance().readByName(user.getName()).isSuccess()) {//Busca el nombre en la base de datos
                    return new Response<>(false, "400", "El nombre de usuario ya está registrado.");
                }
                original.setName(user.getName());
            }
            if (!original.getEmail().equalsIgnoreCase(user.getEmail())) {
                if (UserDAO.getInstance().readByEmail(user.getEmail()).isSuccess()) {
                    return new Response<>(false, "409", "El correo electrónico ya está registrado.");
                }
                original.setEmail(user.getEmail());
            }
            original.setPassword(user.getPassword()); // La clave sí o sí se actualiza

            Response<User> updateResponse = UserDAO.getInstance().update(original);
            if (updateResponse.isSuccess()) {
                return new Response<>(true, "200", "Usuario actualizado correctamente.", original.getId());
            } else {
                return new Response<>(false, "500", updateResponse.getMessage());
            }
        } catch (Exception e) {
            return new Response<>(false, "500", "Error inesperado al editar el usuario: " + e.getMessage());
        }
    }

    public UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    //Creo que no voy a permitir borrar usuarios salvo que no tengan ningun debt o payment a su nombre.
    public Response deleteUser(int id) {
        try{
            Response delection = UserDAO.getInstance().delete(id);
            if (delection.isSuccess()) {
                return new Response<>(true, "200", "Usuario eliminado correctamente.", id);
            } else {
                return new Response<>(false, "500", "No se pudo eliminar el usuario");
            }
        } catch (Exception e) {
            return new Response<>(false,"500", "Error inesperado al eliminar el usuario: " + e.getMessage());
        }
    }

    public Response<List<User>> getAllUsersButOne(int id) {
        try {
            Response<User> daoResponse = UserDAO.getInstance().readAll();

            if (!daoResponse.isSuccess()) {
                return new Response<>(false, daoResponse.getCode(), daoResponse.getMessage());
            }

            ArrayList<User> dataDaoResponse = (ArrayList<User>) daoResponse.getData();
            if (dataDaoResponse == null) dataDaoResponse = new ArrayList<>();

            // Filtrá el usuario por ID
            List<User> filteredList = new ArrayList<>();
            for (User user : dataDaoResponse) {
                if (user.getId() != id) {
                    filteredList.add(user);
                }
            }

            return new Response(true, "200", "Usuarios obtenidos exitosamente.", filteredList);

        } catch (Exception e) {
            return new Response<>(false, "500", "Error inesperado al obtener usuarios: " + e.getMessage());
        }
    }
}
