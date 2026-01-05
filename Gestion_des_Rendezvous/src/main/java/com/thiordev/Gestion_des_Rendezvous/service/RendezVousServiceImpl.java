package com.thiordev.Gestion_des_Rendezvous.service;

import com.thiordev.Gestion_des_Rendezvous.dto.request.RendezVousRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.RendezVousResponseDto;
import com.thiordev.Gestion_des_Rendezvous.exception.BadRequestException;
import com.thiordev.Gestion_des_Rendezvous.exception.ConflictException;
import com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException;
import com.thiordev.Gestion_des_Rendezvous.mapper.RendezVousMapper;
import com.thiordev.Gestion_des_Rendezvous.models.Medecin;
import com.thiordev.Gestion_des_Rendezvous.models.Patient;
import com.thiordev.Gestion_des_Rendezvous.models.RendezVous;
import com.thiordev.Gestion_des_Rendezvous.models.RendezVous.StatutRendezVous;
import com.thiordev.Gestion_des_Rendezvous.repositories.MedecinRepository;
import com.thiordev.Gestion_des_Rendezvous.repositories.PatientRepository;
import com.thiordev.Gestion_des_Rendezvous.repositories.RendezVousRepository;
import com.thiordev.Gestion_des_Rendezvous.service.RendezVousService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implémentation du service de gestion des rendez-vous
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RendezVousServiceImpl implements RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final RendezVousMapper rendezVousMapper;

    @Override
    public RendezVousResponseDto createRendezVous(RendezVousRequestDto requestDto) {
        log.info("Création d'un nouveau rendez-vous pour le patient {} avec le médecin {}",
                requestDto.getPatientId(), requestDto.getMedecinId());

        // Valider les dates
        validateRendezVousDates(requestDto.getDateHeureDebut(), requestDto.getDateHeureFin());

        // Récupérer le patient
        Patient patient = patientRepository.findById(requestDto.getPatientId())
                .orElseThrow(() -> new RessourceNotFoundException("Patient", "id", requestDto.getPatientId()));

        // Récupérer le médecin
        Medecin medecin = medecinRepository.findById(requestDto.getMedecinId())
                .orElseThrow(() -> new RessourceNotFoundException("Medecin", "id", requestDto.getMedecinId()));

        // Vérifier les conflits de créneaux
        if (rendezVousRepository.existsConflictingRendezVous(
                medecin.getId(),
                requestDto.getDateHeureDebut(),
                requestDto.getDateHeureFin())) {
            log.error("Conflit de créneau pour le médecin {} à la date {}",
                    medecin.getId(), requestDto.getDateHeureDebut());
            throw new ConflictException("Ce créneau horaire est déjà réservé pour ce médecin");
        }

        // Créer le rendez-vous
        RendezVous rendezVous = rendezVousMapper.toEntityWithRelations(requestDto, patient, medecin);

        // Sauvegarder
        RendezVous savedRendezVous = rendezVousRepository.save(rendezVous);

        log.info("Rendez-vous créé avec succès - ID: {}", savedRendezVous.getId());

        return rendezVousMapper.toResponseDto(savedRendezVous);
    }

    @Override
    @Transactional(readOnly = true)
    public RendezVousResponseDto getRendezVousById(Long id) {
        log.info("Récupération du rendez-vous avec l'ID: {}", id);

        RendezVous rendezVous = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("RendezVous", "id", id));

        return rendezVousMapper.toResponseDto(rendezVous);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RendezVousResponseDto> getAllRendezVous() {
        log.info("Récupération de tous les rendez-vous");

        List<RendezVous> rendezVousList = rendezVousRepository.findAll();

        return rendezVousMapper.toResponseDtoList(rendezVousList);
    }

    @Override
    public RendezVousResponseDto updateRendezVous(Long id, RendezVousRequestDto requestDto) {
        log.info("Mise à jour du rendez-vous avec l'ID: {}", id);

        // Vérifier que le rendez-vous existe
        RendezVous existingRendezVous = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("RendezVous", "id", id));

        // Valider les nouvelles dates
        validateRendezVousDates(requestDto.getDateHeureDebut(), requestDto.getDateHeureFin());

        // Récupérer le patient si changé
        if (!existingRendezVous.getPatient().getId().equals(requestDto.getPatientId())) {
            Patient newPatient = patientRepository.findById(requestDto.getPatientId())
                    .orElseThrow(() -> new RessourceNotFoundException("Patient", "id", requestDto.getPatientId()));
            existingRendezVous.setPatient(newPatient);
        }

        // Récupérer le médecin si changé
        if (!existingRendezVous.getMedecin().getId().equals(requestDto.getMedecinId())) {
            Medecin newMedecin = medecinRepository.findById(requestDto.getMedecinId())
                    .orElseThrow(() -> new RessourceNotFoundException("Medecin", "id", requestDto.getMedecinId()));
            existingRendezVous.setMedecin(newMedecin);
        }

        // Vérifier les conflits (en excluant le rendez-vous actuel)
        if (rendezVousRepository.existsConflictingRendezVousExcludingCurrent(
                requestDto.getMedecinId(),
                id,
                requestDto.getDateHeureDebut(),
                requestDto.getDateHeureFin())) {
            log.error("Conflit de créneau lors de la mise à jour du rendez-vous {}", id);
            throw new ConflictException("Ce créneau horaire est déjà réservé pour ce médecin");
        }

        // Mettre à jour les champs
        rendezVousMapper.updateEntityFromDto(requestDto, existingRendezVous);

        // Sauvegarder
        RendezVous updatedRendezVous = rendezVousRepository.save(existingRendezVous);

        log.info("Rendez-vous mis à jour avec succès - ID: {}", updatedRendezVous.getId());

        return rendezVousMapper.toResponseDto(updatedRendezVous);
    }

    @Override
    public void deleteRendezVous(Long id) {
        log.info("Suppression du rendez-vous avec l'ID: {}", id);

        if (!rendezVousRepository.existsById(id)) {
            throw new RessourceNotFoundException("RendezVous", "id", id);
        }

        rendezVousRepository.deleteById(id);

        log.info("Rendez-vous supprimé avec succès - ID: {}", id);
    }

    @Override
    public RendezVousResponseDto cancelRendezVous(Long id) {
        log.info("Annulation du rendez-vous avec l'ID: {}", id);

        RendezVous rendezVous = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("RendezVous", "id", id));

        rendezVous.setStatut(StatutRendezVous.ANNULE);
        RendezVous updatedRendezVous = rendezVousRepository.save(rendezVous);

        log.info("Rendez-vous annulé avec succès - ID: {}", id);

        return rendezVousMapper.toResponseDto(updatedRendezVous);
    }

    @Override
    public RendezVousResponseDto confirmRendezVous(Long id) {
        log.info("Confirmation du rendez-vous avec l'ID: {}", id);

        RendezVous rendezVous = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("RendezVous", "id", id));

        rendezVous.setStatut(StatutRendezVous.CONFIRME);
        RendezVous updatedRendezVous = rendezVousRepository.save(rendezVous);

        log.info("Rendez-vous confirmé avec succès - ID: {}", id);

        return rendezVousMapper.toResponseDto(updatedRendezVous);
    }

    @Override
    public RendezVousResponseDto completeRendezVous(Long id) {
        log.info("Marquage du rendez-vous comme terminé - ID: {}", id);

        RendezVous rendezVous = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("RendezVous", "id", id));

        rendezVous.setStatut(StatutRendezVous.TERMINE);
        RendezVous updatedRendezVous = rendezVousRepository.save(rendezVous);

        log.info("Rendez-vous marqué comme terminé - ID: {}", id);

        return rendezVousMapper.toResponseDto(updatedRendezVous);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RendezVousResponseDto> getRendezVousByPatient(Long patientId) {
        log.info("Récupération des rendez-vous du patient: {}", patientId);

        List<RendezVous> rendezVousList = rendezVousRepository.findByPatientIdOrderByDateHeureDebutDesc(patientId);

        return rendezVousMapper.toResponseDtoList(rendezVousList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RendezVousResponseDto> getRendezVousByMedecin(Long medecinId) {
        log.info("Récupération des rendez-vous du médecin: {}", medecinId);

        List<RendezVous> rendezVousList = rendezVousRepository.findByMedecinIdOrderByDateHeureDebutAsc(medecinId);

        return rendezVousMapper.toResponseDtoList(rendezVousList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RendezVousResponseDto> getRendezVousByStatut(StatutRendezVous statut) {
        log.info("Récupération des rendez-vous avec le statut: {}", statut);

        List<RendezVous> rendezVousList = rendezVousRepository.findByStatutOrderByDateHeureDebutAsc(statut);

        return rendezVousMapper.toResponseDtoList(rendezVousList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RendezVousResponseDto> getRendezVousByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Récupération des rendez-vous entre {} et {}", startDate, endDate);

        validateDateRange(startDate, endDate);

        List<RendezVous> rendezVousList = rendezVousRepository.findByDateRange(startDate, endDate);

        return rendezVousMapper.toResponseDtoList(rendezVousList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RendezVousResponseDto> getUpcomingRendezVousByPatient(Long patientId) {
        log.info("Récupération des rendez-vous à venir du patient: {}", patientId);

        List<RendezVous> rendezVousList = rendezVousRepository.findUpcomingRendezVousByPatient(
                patientId,
                LocalDateTime.now()
        );

        return rendezVousMapper.toResponseDtoList(rendezVousList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RendezVousResponseDto> getUpcomingRendezVousByMedecin(Long medecinId) {
        log.info("Récupération des rendez-vous à venir du médecin: {}", medecinId);

        List<RendezVous> rendezVousList = rendezVousRepository.findUpcomingRendezVousByMedecin(
                medecinId,
                LocalDateTime.now()
        );

        return rendezVousMapper.toResponseDtoList(rendezVousList);
    }

    @Override
    @Transactional(readOnly = true)
    public long countRendezVousByStatut(StatutRendezVous statut) {
        log.info("Comptage des rendez-vous avec le statut: {}", statut);

        return rendezVousRepository.countByStatut(statut);
    }

    // Méthodes privées de validation

    private void validateRendezVousDates(LocalDateTime debut, LocalDateTime fin) {
        if (debut == null || fin == null) {
            throw new BadRequestException("Les dates de début et de fin sont obligatoires");
        }

        if (fin.isBefore(debut) || fin.isEqual(debut)) {
            throw new BadRequestException("La date de fin doit être après la date de début");
        }

        if (debut.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("La date de début ne peut pas être dans le passé");
        }
    }

    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new BadRequestException("Les dates de début et de fin sont obligatoires");
        }

        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("La date de fin doit être après la date de début");
        }
    }
}