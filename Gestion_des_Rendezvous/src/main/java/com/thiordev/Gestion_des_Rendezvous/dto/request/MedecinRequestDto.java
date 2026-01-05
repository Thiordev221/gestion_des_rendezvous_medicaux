package com.thiordev.Gestion_des_Rendezvous.dto.request;

import com.thiordev.Gestion_des_Rendezvous.models.Specialite;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedecinRequestDto {
    @NotBlank(message = "Le prenom est obligatoire !")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères !")
    private String prenom;

    @NotBlank(message = "Le nom est obligatoire !")
    @Size(min = 2, max = 5, message = "Le nom doit être entre 2 et 50 caractères !")
    private String nom;

    @NotBlank
    @Email(message = "Votre email est invalide !")
    @Size(max = 100, message = "L'email ne peut pas dépasser 100 caractères")
    private String email;

    @Size(max = 200, message = "L'adresse doit avoir au maximum 200 caractères !")
    private String adresse;

    @NotBlank(message = "Le numéro de telephone est obligatoire !")
    @Pattern(regexp = "^(\\+221|00221)?[0-9]{9}$", message = "Le numéro de téléphone doit être valide (format: +221XXXXXXXXX ou 00221XXXXXXXXX)")
    private String telephone;

    @NotNull(message = "La spécialité est obligatoire")

    private Specialite specialite;
}
