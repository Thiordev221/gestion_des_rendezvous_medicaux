package com.thiordev.Gestion_des_Rendezvous.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin;

    @Column(nullable = false)
    private LocalDateTime dateHeureDebut;

    @Column(nullable = false)
    private LocalDateTime dateHeureFin;

    @Column(length = 500)
    private String motifConsultation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutRendezVous statut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    @PrePersist
    public void prePersist() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
        if (statut == null) {
            statut = StatutRendezVous.EN_ATTENTE;
        }

        if (dateHeureFin.isBefore(dateHeureDebut) || dateHeureDebut.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début");
        }
    }

    @PreUpdate
    public void preUpdate() {
        dateModification = LocalDateTime.now();

        if (dateHeureFin.isBefore(dateHeureDebut) || dateHeureDebut.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début");
        }
    }


    public enum StatutRendezVous {
        EN_ATTENTE,
        CONFIRME,
        ANNULE,
        TERMINE
    }
}
