package com.thiordev.Gestion_des_Rendezvous.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDto {

    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String email;
    private String telephone;
    private String adresse;
    private String numeroSecuriteSociale;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    // Optionnel : ajouter le nombre de rendez-vous
    private Integer nombreRendezVous;
}