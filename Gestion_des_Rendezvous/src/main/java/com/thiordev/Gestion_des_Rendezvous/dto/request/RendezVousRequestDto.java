package com.thiordev.Gestion_des_Rendezvous.dto.request;

import com.thiordev.Gestion_des_Rendezvous.models.Medecin;
import com.thiordev.Gestion_des_Rendezvous.models.Patient;
import com.thiordev.Gestion_des_Rendezvous.models.RendezVous;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RendezVousRequestDto {

    @NotNull(message = "L'ID du medecin est obligatoire !")
    @Positive(message = "L'ID du medecin doit être positive !")
    private Long medecinId;

    @NotNull(message = "L'ID du patient est obligatoire !")
    @Positive(message = "L'ID du patient doit être positive !")
    private Long patientId;

    @NotNull(message = "La date de debut est obligatoire !")
    @Future(message = "La date debut doit être au future !")
    private LocalDateTime dateHeureDebut;

    @NotNull(message = "La date de fin est obligatoire !")
    @Future(message = "La date fin doit être au future !")
    private LocalDateTime dateHeureFin;

    @Size(max = 500, message = "Le motif de consultation ne peut pas dépasser 500 caractères")
    private String motifConsultation;

    private RendezVous.StatutRendezVous statutRendezVous;

}
