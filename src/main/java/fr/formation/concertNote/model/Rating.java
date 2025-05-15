package fr.formation.concertNote.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(5)
    private int score;

    private String comment;

    @ManyToOne
    private User user;

    @ManyToOne
    private Concert concert;

    public Rating() {}
}
