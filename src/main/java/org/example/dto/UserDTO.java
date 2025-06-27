package org.example.dto;

public class UserDTO {
    private int id; // 0 significa "no asignado"
    private String name;
    private String email;

    public UserDTO() {
    }

    public UserDTO(String name, String email) {
        this.id = 0;
        this.name = name;
        this.email = email;
    }

    public UserDTO(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "id=" + id + "->" + name + "->" + email;
    }
}
