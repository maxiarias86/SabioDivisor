package org.example.dao;

import org.example.model.Response;
import org.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends BaseDAO<User> {
    private final String tableName = "users";// La tabla de usuarios
    private static final UserDAO instance = new UserDAO();//Todos los DAO son Singleton.
    public static UserDAO getInstance(){
        return instance;
    }
    private UserDAO() {
        super();
    }

    @Override
    public Response<User> create(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";//No paso el ID porque es autoincremental

        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);// Esto permite obtener el ID generado automáticamente. No lo voy a usar en este caso, solo en Expense me es indispensable.
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();// Ejecuta la inserción

            ResultSet rs = ps.getGeneratedKeys();// Obtiene el id generado.
            if (rs.next()) {// Si se generó un ID, lo asigno al objeto User que le pase como parametro.
                int idGenerado = rs.getInt(1);// Obtiene el primer (y debería ser el único) ID generado
                user.setId(idGenerado);// Asigna el ID al objeto User
                return new Response<>(true, "201", "Usuario creado exitosamente.", user);// Retorna el objeto User con el ID asignado
            } else {
                return new Response<>(false, "500", "No se pudo obtener el ID del nuevo usuario.");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<User> read(int id) {// Busca un usuario por ID
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);// Pasa el ID del usuario a buscar
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                return new Response<>(true, "200", "Usuario encontrado", user);// Retorna el usuario encontrado
            } else {
                return new Response<>(false, "404", "No se encontró el usuario");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }
    @Override
    public Response<User> readAll() {
        String sql = "SELECT * FROM " + tableName + " ORDER BY id ASC";//Ordena por ID ascendente
        List<User> lista = new ArrayList<>();// Lista para almacenar los usuarios obtenidos

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {// Bucle sobre los resultados
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                lista.add(user);// Agrega el usuario a la lista
            }
            return new Response<>(true, "200", "Listado de usuarios obtenido", lista);// Retorna la lista de usuarios
        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<User> update(User entity) {// Edita un usuario
        String sql = "UPDATE " + tableName + " SET name = ?, email = ?, password = ? WHERE id = ?";// Actualiza el usuario por ID
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getPassword());
            ps.setInt(4, entity.getId());
            int rows = ps.executeUpdate();// Ejecuta
            if (rows == 1) {// Si se actualizó un registro, y solo uno, retorna éxito.
                return new Response<>(true, "200", "Usuario actualizado correctamente");
            } else if (rows == 0) {
                return new Response<>(false, "404", "No se encontró el usuario");
            } else {
                return new Response<>(false, "500", "Error: se afectaron múltiples registros");
            }
        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<User> delete(int id) {// Elimina un usuario por ID pero no se va a aplicar en la aplicación, ya que no se va a eliminar un usuario una vez creado.
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 1) {
                return new Response<>(true, "200", "Usuario eliminado correctamente");
            } else {
                return new Response<>(false, "404", "No se encontró el usuario");
            }
        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    public Response<User> readByEmail(String email) {// Busca un usuario por email
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                return new Response<>(true, "200", "Usuario encontrado", user);
            } else {
                return new Response<>(false, "404", "No se encontró el usuario con ese email");
            }
        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    public Response<User> readByName(String name) {
        String sql = "SELECT * FROM users WHERE name = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                return new Response<>(true, "200", "Usuario encontrado", user);
            } else {
                return new Response<>(false, "404", "No se encontró el usuario con ese nombre");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }
}