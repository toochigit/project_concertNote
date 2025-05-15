package fr.formation.concertNote.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @Email
    private String email;

    private String passwordHash;

    // Getters & setters (génère-les automatiquement avec ton IDE)

    // Constructeur vide obligatoire
    public User() {}
}
