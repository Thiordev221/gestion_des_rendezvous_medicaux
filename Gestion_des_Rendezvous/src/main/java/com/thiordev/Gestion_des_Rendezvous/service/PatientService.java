package com.thiordev.Gestion_des_Rendezvous.service;

import com.thiordev.Gestion_des_Rendezvous.dto.request.PatientRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.PatientResponseDto;

import java.util.List;

/**
 * Interface de service pour la gestion des patients
 */
public interface PatientService {

    /**
     * Créer un nouveau patient
     * @param requestDto Les données du patient
     * @return Le patient créé
     * @throws org.springframework.boot.context.config.ConfigDataException si l'email existe déjà
     */
    PatientResponseDto createPatient(PatientRequestDto requestDto);

    /**
     * Récupérer un patient par son ID
     * @param id L'identifiant du patient
     * @return Le patient trouvé
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le patient n'existe pas
     */
    PatientResponseDto getPatientById(Long id);

    /**
     * Récupérer tous les patients
     * @return La liste de tous les patients
     */
    List<PatientResponseDto> getAllPatients();

    /**
     * Mettre à jour un patient
     * @param id L'identifiant du patient
     * @param requestDto Les nouvelles données
     * @return Le patient mis à jour
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le patient n'existe pas
     * @throws org.springframework.boot.context.config.ConfigDataException si le nouvel email existe déjà
     */
    PatientResponseDto updatePatient(Long id, PatientRequestDto requestDto);

    /**
     * Supprimer un patient
     * @param id L'identifiant du patient
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le patient n'existe pas
     */
    void deletePatient(Long id);

    /**
     * Rechercher des patients par nom ou prénom
     * @param searchTerm Le terme de recherche
     * @return La liste des patients trouvés
     */
    List<PatientResponseDto> searchPatients(String searchTerm);

    /**
     * Récupérer un patient par son email
     * @param email L'email du patient
     * @return Le patient trouvé
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le patient n'existe pas
     */
    PatientResponseDto getPatientByEmail(String email);

    /**
     * Compter le nombre total de patients
     * @return Le nombre de patients
     */
    long countPatients();
}