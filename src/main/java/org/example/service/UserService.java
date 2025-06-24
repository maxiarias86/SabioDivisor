package org.example.service;

import org.example.dto.UserDTO;
import org.example.dao.UserDAO;
import org.example.model.Response;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    public Response<Integer> registerUser(UserDTO dto) {// Devuelve una response con el int userId
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            return new Response<>(false, "400", "El nombre de usuario no puede estar vacío.");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            return new Response<>(false, "400", "El correo electrónico no puede estar vacío.");
        }
        if (!dto.getEmail().contains("@")) {
            return new Response<>(false, "400", "El correo electrónico debe contener '@'.");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            return new Response<>(false, "400", "La contraseña no puede estar vacía.");
        }
        try{
            if (UserDAO.getInstance().readByName(dto.getUsername()).isSuccess()) {//Busca el nombre en la base de datos
                return new Response<>(false, "409", "El nombre de usuario ya está registrado.");
            }
            if (UserDAO.getInstance().readByEmail(dto.getEmail()).isSuccess()) {//Busca el mail en la base de datos
                return new Response<>(false, "409", "El correo electrónico ya está registrado.");
            }

            // Crear una instancia usuario
            User newUser = new User();
            newUser.setName(dto.getUsername());
            newUser.setEmail(dto.getEmail());
            newUser.setPassword(dto.getPassword());

            Response<User> response = UserDAO.getInstance().create(newUser);//Guarda la instancia en BBDD
            if (response.isSuccess()) {
                return new Response<>(true, "201", "Usuario creado exitosamente.", response.getObj().getId());
            } else {
                return new Response<>(false, "500", "No se pudo insertar el registro.");
            }

        } catch (Exception e) {
            return new Response<>(false, "500", "Error inesperado al registrar el usuario: " + e.getMessage());
        }
    }

    public Response editUser(UserDTO dto){
        //Primero valida el DTO
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            return new Response<>(false, "400", "El nombre de usuario no puede estar vacío.");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            return new Response<>(false, "400", "El correo electrónico no puede estar vacío.");
        }
        if (!dto.getEmail().contains("@")) {
            return new Response<>(false, "400", "El correo electrónico debe contener '@'.");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            return new Response<>(false, "400", "La contraseña no puede estar vacía.");
        }
        if (dto.getId() <=0){
            return new Response<>(false, "400", "El ID del usuario no es válido.");
        }
        try{
            //Usa el DAO para traerse el objeto de la base
            Response<User> userResponse = UserDAO.getInstance().read(dto.getId());
            if (!userResponse.isSuccess()) {
                return new Response<>(false, "404", "Usuario no encontrado.");
            }
            User original = userResponse.getObj();
            if (!original.getName().equalsIgnoreCase(dto.getUsername())){
                if (UserDAO.getInstance().readByName(dto.getUsername()).isSuccess()) {//Busca el nombre en la base de datos
                    return new Response<>(false, "400", "El nombre de usuario ya está registrado.");
                }
                original.setName(dto.getUsername());
            }
            if (!original.getEmail().equalsIgnoreCase(dto.getEmail())) {
                if (UserDAO.getInstance().readByEmail(dto.getEmail()).isSuccess()) {
                    return new Response<>(false, "409", "El correo electrónico ya está registrado.");
                }
                original.setEmail(dto.getEmail());
            }
            original.setPassword(dto.getPassword()); // La clave sí o sí se actualiza

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
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword());
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

    public Response<List<UserDTO>> getAllUsers() {
        try {
            // Llama al DAO y obtiene los usuarios como entidades
            Response<User> daoResponse = UserDAO.getInstance().readAll();

            if (!daoResponse.isSuccess()) {
                return new Response<>(false, daoResponse.getCode(), daoResponse.getMessage());
            }
            List <User> dataDaoResponse = daoResponse.getData();

            // Convierte la lista de entidades a DTOs
            List<UserDTO> dtoList = new ArrayList<>();
            for (User user : dataDaoResponse) {
                dtoList.add(new UserDTO(user.getId(), user.getName(), user.getEmail()));
            }

            return new Response<List<UserDTO>>(true, "200", "Usuarios obtenidos exitosamente.", dtoList);

        } catch (Exception e) {
            return new Response<>(false, "500", "Error inesperado al obtener usuarios: " + e.getMessage());
        }
    }








}
