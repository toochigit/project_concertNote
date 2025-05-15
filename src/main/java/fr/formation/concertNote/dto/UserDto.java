package fr.formation.concertNote.dto;

import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class UserDto {

    @NotBlank(message = "Nom d'utilisateur obligatoire")
    private String username;

    @NotBlank(message = "Email obligatoire")
    @Email(message = "Format email invalide")
    private String email;

    @NotBlank(message = "Mot de passe obligatoire")
    @Size(min = 6, message = "Mot de passe trop court")
    private String password;

    public UserDto() {}

    // Getters & setters


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

