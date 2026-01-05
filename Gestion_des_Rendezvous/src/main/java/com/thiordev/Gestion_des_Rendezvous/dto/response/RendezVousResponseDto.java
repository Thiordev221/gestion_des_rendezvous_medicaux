package com.thiordev.Gestion_des_Rendezvous.dto.response;

import com.thiordev.Gestion_des_Rendezvous.models.RendezVous.StatutRendezVous;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVousResponseDto {

    private Long id;

    // Informations du patient (sans tout exposer)
    private Long patientId;
    private String patientNom;
    private String patientPrenom;

    // Informations du médecin
    private Long medecinId;
    private String medecinNom;
    private String medecinPrenom;
    private String medecinSpecialite;

    // Détails du rendez-vous
    private LocalDateTime dateHeureDebut;
    private LocalDateTime dateHeureFin;
    private String motifConsultation;
    private StatutRendezVous statut;

    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
}