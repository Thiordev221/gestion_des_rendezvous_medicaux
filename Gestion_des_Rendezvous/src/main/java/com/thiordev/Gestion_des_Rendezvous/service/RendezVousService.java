package com.thiordev.Gestion_des_Rendezvous.service;

import com.thiordev.Gestion_des_Rendezvous.dto.request.RendezVousRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.RendezVousResponseDto;
import com.thiordev.Gestion_des_Rendezvous.models.RendezVous.StatutRendezVous;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface de service pour la gestion des rendez-vous
 */
public interface RendezVousService {

    /**
     * Créer un nouveau rendez-vous
     * @param requestDto Les données du rendez-vous
     * @return Le rendez-vous créé
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le patient ou médecin n'existe pas
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.ConflictException si le créneau est déjà occupé
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.BadRequestException si les données sont invalides
     */
    RendezVousResponseDto createRendezVous(RendezVousRequestDto requestDto);

    /**
     * Récupérer un rendez-vous par son ID
     * @param id L'identifiant du rendez-vous
     * @return Le rendez-vous trouvé
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le rendez-vous n'existe pas
     */
    RendezVousResponseDto getRendezVousById(Long id);

    /**
     * Récupérer tous les rendez-vous
     * @return La liste de tous les rendez-vous
     */
    List<RendezVousResponseDto> getAllRendezVous();

    /**
     * Mettre à jour un rendez-vous
     * @param id L'identifiant du rendez-vous
     * @param requestDto Les nouvelles données
     * @return Le rendez-vous mis à jour
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le rendez-vous n'existe pas
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.ConflictException si le nouveau créneau est occupé
     */
    RendezVousResponseDto updateRendezVous(Long id, RendezVousRequestDto requestDto);

    /**
     * Supprimer un rendez-vous
     * @param id L'identifiant du rendez-vous
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le rendez-vous n'existe pas
     */
    void deleteRendezVous(Long id);

    /**
     * Annuler un rendez-vous
     * @param id L'identifiant du rendez-vous
     * @return Le rendez-vous annulé
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le rendez-vous n'existe pas
     */
    RendezVousResponseDto cancelRendezVous(Long id);

    /**
     * Confirmer un rendez-vous
     * @param id L'identifiant du rendez-vous
     * @return Le rendez-vous confirmé
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le rendez-vous n'existe pas
     */
    RendezVousResponseDto confirmRendezVous(Long id);

    /**
     * Marquer un rendez-vous comme terminé
     * @param id L'identifiant du rendez-vous
     * @return Le rendez-vous terminé
     * @throws com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException si le rendez-vous n'existe pas
     */
    RendezVousResponseDto completeRendezVous(Long id);

    /**
     * Récupérer les rendez-vous d'un patient
     * @param patientId L'identifiant du patient
     * @return La liste des rendez-vous du patient
     */
    List<RendezVousResponseDto> getRendezVousByPatient(Long patientId);

    /**
     * Récupérer les rendez-vous d'un médecin
     * @param medecinId L'identifiant du médecin
     * @return La liste des rendez-vous du médecin
     */
    List<RendezVousResponseDto> getRendezVousByMedecin(Long medecinId);

    /**
     * Récupérer les rendez-vous par statut
     * @param statut Le statut recherché
     * @return La liste des rendez-vous avec ce statut
     */
    List<RendezVousResponseDto> getRendezVousByStatut(StatutRendezVous statut);

    /**
     * Récupérer les rendez-vous dans une plage de dates
     * @param startDate Date de début
     * @param endDate Date de fin
     * @return La liste des rendez-vous dans cette plage
     */
    List<RendezVousResponseDto> getRendezVousByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Récupérer les rendez-vous à venir d'un patient
     * @param patientId L'identifiant du patient
     * @return La liste des rendez-vous à venir
     */
    List<RendezVousResponseDto> getUpcomingRendezVousByPatient(Long patientId);

    /**
     * Récupérer les rendez-vous à venir d'un médecin
     * @param medecinId L'identifiant du médecin
     * @return La liste des rendez-vous à venir
     */
    List<RendezVousResponseDto> getUpcomingRendezVousByMedecin(Long medecinId);

    /**
     * Compter les rendez-vous par statut
     * @param statut Le statut
     * @return Le nombre de rendez-vous
     */
    long countRendezVousByStatut(StatutRendezVous statut);
}