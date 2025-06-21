package org.example.model;

public class User {
    int id;
    String name;
    String email;
    String password;

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    //Métodos

    public void updateProfile(String name, String email) {
        try {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Email inválido");
            }
            setName(name);
            setEmail(email);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al actualizar el perfil: " + e.getMessage());
        }
    }

    public boolean changePassword(String newPassword) {
        try {
            if (newPassword == null || newPassword.length() < 6) {
                throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
            }
            setPassword(newPassword);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error al cambiar la contraseña: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        return "Usuario {" +
                "id=" + id +
                ", nombre_de_usuario='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    /*Por defecto, ¿qué hacen equals() y hashCode()?
    equals(): Compara por referencia. Solo devuelve true si ambos objetos son la misma instancia en memoria.
    Ejemplo:    User u1 = new User(1, "Maxi", "...", "...");
                User u2 = new User(1, "Maxi", "...", "...");
                System.out.println(u1.equals(u2)); es false (aunque tengan los mismos datos)
     hashCode(): Devuelve un número basado en la dirección de memoria del objeto,
     por lo que dos objetos con los mismos datos tienen hashCode distintos.
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    /*hashCode() Es un int que representa un objeto y que sirve para
    ubicarlo en una estructura hash, como:
    HashMap<User, Double>
    HashSet<User>
    Cuando ponés un objeto en un HashMap o HashSet, Java:
    Llama al método hashCode() del objeto para saber en qué "casillero" ponerlo
    Si varios objetos tienen el mismo hashCode, usa equals()
    para ver si realmente son iguales*/
    public int hashCode() {
        return Integer.hashCode(id);
    }









}
