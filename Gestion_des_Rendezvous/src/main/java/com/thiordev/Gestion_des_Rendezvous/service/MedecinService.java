package com.thiordev.Gestion_des_Rendezvous.service;

import com.thiordev.Gestion_des_Rendezvous.dto.request.MedecinRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.MedecinResponseDto;
import com.thiordev.Gestion_des_Rendezvous.models.Specialite;

import java.util.List;

/**
 * Interface de service pour la gestion des médecins
 */
public interface MedecinService {

    /**
     * Créer un nouveau médecin
     * @param requestDto Les données du médecin
     * @return Le médecin créé
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.ConflictException si l'email existe déjà
     */
    MedecinResponseDto createMedecin(MedecinRequestDto requestDto);

    /**
     * Récupérer un médecin par son ID
     * @param id L'identifiant du médecin
     * @return Le médecin trouvé
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le médecin n'existe pas
     */
    MedecinResponseDto getMedecinById(Long id);

    /**
     * Récupérer tous les médecins
     * @return La liste de tous les médecins
     */
    List<MedecinResponseDto> getAllMedecins();

    /**
     * Mettre à jour un médecin
     * @param id L'identifiant du médecin
     * @param requestDto Les nouvelles données
     * @return Le médecin mis à jour
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le médecin n'existe pas
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.ConflictException si le nouvel email existe déjà
     */
    MedecinResponseDto updateMedecin(Long id, MedecinRequestDto requestDto);

    /**
     * Supprimer un médecin
     * @param id L'identifiant du médecin
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le médecin n'existe pas
     */
    void deleteMedecin(Long id);

    /**
     * Récupérer les médecins par spécialité
     * @param specialite La spécialité recherchée
     * @return La liste des médecins de cette spécialité
     */
    List<MedecinResponseDto> getMedecinsBySpecialite(Specialite specialite);

    /**
     * Rechercher des médecins par nom ou prénom
     * @param searchTerm Le terme de recherche
     * @return La liste des médecins trouvés
     */
    List<MedecinResponseDto> searchMedecins(String searchTerm);

    /**
     * Récupérer un médecin par son email
     * @param email L'email du médecin
     * @return Le médecin trouvé
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le médecin n'existe pas
     */
    MedecinResponseDto getMedecinByEmail(String email);

    /**
     * Compter le nombre de médecins par spécialité
     * @param specialite La spécialité
     * @return Le nombre de médecins
     */
    long countMedecinsBySpecialite(Specialite specialite);
}