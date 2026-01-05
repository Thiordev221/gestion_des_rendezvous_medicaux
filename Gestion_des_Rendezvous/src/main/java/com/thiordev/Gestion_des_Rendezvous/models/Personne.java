package com.thiordev.Gestion_des_Rendezvous.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;


@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, length = 50)
    protected String nom;

    @Column(nullable = false, length = 50)
    protected String prenom;

    @Column(nullable = false, unique = true, length = 100)
    protected String email;

    @Column(nullable = false, length = 20)
    protected String telephone;

    @Column(length = 200)
    protected String adresse;

    @Column(nullable = false, updatable = false)
    protected LocalDateTime dateCreation;

    @Column(nullable = false)
    protected LocalDateTime dateModification;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
}

