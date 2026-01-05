package com.thiordev.Gestion_des_Rendezvous.dto.response;

import com.thiordev.Gestion_des_Rendezvous.models.Specialite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedecinResponseDto {

    private Long id;
    private String nom;
    private String prenom;
    private Specialite specialite;
    private String specialiteLibelle; // Pour afficher le libellé lisible
    private String email;
    private String telephone;
    private String adresse;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    // Optionnel : ajouter le nombre de rendez-vous
    private Integer nombreRendezVous;
}