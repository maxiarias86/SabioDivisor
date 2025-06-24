package org.example.dto;

public class UserDTO {
    private int id; // 0 significa "no asignado"
    private String username;
    private String email;
    private String password;

    public UserDTO() {
    }

    public UserDTO(String username, String email,String password) {
        this.id = 0;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserDTO(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = null;
    }

    public UserDTO(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
