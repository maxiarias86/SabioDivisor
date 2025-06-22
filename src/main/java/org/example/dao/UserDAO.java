package org.example.dao;

import org.example.model.Response;
import org.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends BaseDAO<User> {

    private final String tableName = "users";
    private static final UserDAO instance = new UserDAO();


    public static UserDAO getInstance(){
        return instance;
    }

    private UserDAO() {
        super();
    }

    @Override
    public Response<User> create(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                user.setId(idGenerado);
                return new Response<>(true, "201", "Usuario creado exitosamente.", user);
            } else {
                return new Response<>(false, "500", "No se pudo obtener el ID del nuevo usuario.");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }


    @Override
    public Response<User> read(int id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
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
                return new Response<>(false, "404", "No se encontró el usuario");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<User> update(User entity) {
        String sql = "UPDATE " + tableName + " SET name = ?, email = ?, password = ? WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getPassword());
            ps.setInt(4, entity.getId());

            int rows = ps.executeUpdate();

            if (rows == 1) {
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
    public Response<User> delete(int id) {
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

    @Override
    public Response<User> readAll() {
        String sql = "SELECT * FROM " + tableName;
        List<User> lista = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                lista.add(user);
            }

            return new Response<>(true, "200", "Listado de usuarios obtenido", lista);

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    //Hice un metodo para buscarlos por email porque no siempre voy a conocer el ID.
    //Mapea todos los campos del modelo User.

    public Response<User> readByEmail(String email) {
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
