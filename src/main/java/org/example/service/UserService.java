package org.example.service;

import org.example.dto.UserDTO;
import org.example.dao.UserDAO;
import org.example.model.Response;
import org.example.model.User;

public class UserService {

    public Response<Integer> registerUser(UserDTO dto) {
        // Validaciones básicas
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            return new Response<>(false, "400", "El nombre de usuario no puede estar vacío.");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            return new Response<>(false, "400", "El correo electrónico no puede estar vacío.");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            return new Response<>(false, "400", "La contraseña no puede estar vacía.");
        }

        // Verificar que no exista el username
        if (UserDAO.getInstance().readByName(dto.getUsername()).isSuccess()) {
            return new Response<>(false, "409", "El nombre de usuario ya está registrado.");
        }


        // Verificar que no exista el email
        if (UserDAO.getInstance().readByEmail(dto.getEmail()).isSuccess()) {
            return new Response<>(false, "409", "El correo electrónico ya está registrado.");
        }


        // Crear y guardar usuario
        User nuevoUsuario = new User();
        nuevoUsuario.setName(dto.getUsername());
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setPassword(dto.getPassword());

        Response<User> response = UserDAO.getInstance().create(nuevoUsuario);
        if (response.isSuccess()) {
            return new Response<>(true, "201", "Usuario creado exitosamente.", response.getObj().getId());
        } else {
            return new Response<>(false, response.getCode(), response.getMessage());
        }
    }
}
