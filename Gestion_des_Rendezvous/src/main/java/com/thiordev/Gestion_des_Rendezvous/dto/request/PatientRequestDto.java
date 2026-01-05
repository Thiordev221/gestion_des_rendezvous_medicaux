package com.thiordev.Gestion_des_Rendezvous.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientRequestDto {
    @NotBlank
    @Size(min = 2, max = 50, message = "Le nom doit être entre 2 et 50 caractères !")
    private String nom;

    @NotBlank
    @Size(min = 2, max = 50, message = "Le prenom doit être entre 2 et 50 caractères !")
    private String prenom;

    @NotBlank(message = "Le champ email est obligatoire !")
    @Email(message = "Votre email n'est pas valide !")
    @Size(max = 100, message = "L'email ne peut pas dépasser 100 caractères")
    private String email;

    @Size(max = 200, message = "L'adresse peut contenir au maximum 200 caractères !")
    private String adresse;

    @NotBlank(message = "Le numéro de téléphone est obligatoire !")
    @Pattern(regexp = "^(\\+221|00221)?[0-9]{9}$",
            message = "Le numéro de téléphone doit être valide (format: +221XXXXXXXXX ou 00221XXXXXXXXX)")
    private String telephone;

    @Size(max = 15, message = "Le numero social doit contenir au maximum 15 caractères")
    private String numeroSecuriteSociale;

    @NotNull(message = "La date de naissace est obligatoire !")
    @Past(message = "La date de naissance doit être au passé !")
    private LocalDate dateNaissance;


}
