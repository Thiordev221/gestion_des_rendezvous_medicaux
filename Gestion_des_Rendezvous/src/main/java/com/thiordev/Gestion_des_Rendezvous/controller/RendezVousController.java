package com.thiordev.Gestion_des_Rendezvous.controller;

import com.thiordev.Gestion_des_Rendezvous.dto.request.RendezVousRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.RendezVousResponseDto;
import com.thiordev.Gestion_des_Rendezvous.models.RendezVous.StatutRendezVous;
import com.thiordev.Gestion_des_Rendezvous.service.RendezVousService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller REST pour la gestion des rendez-vous
 * Base URL: /api/rendezvous
 */
@RestController
@RequestMapping("/api/rendezvous")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class RendezVousController {

    private final RendezVousService rendezVousService;

    /**
     * Créer un nouveau rendez-vous
     * POST /api/rendezvous
     */
    @PostMapping
    public ResponseEntity<RendezVousResponseDto> createRendezVous(
            @Valid @RequestBody RendezVousRequestDto requestDto) {
        log.info("API: Création d'un nouveau rendez-vous");
        RendezVousResponseDto response = rendezVousService.createRendezVous(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupérer un rendez-vous par son ID
     * GET /api/rendezvous/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RendezVousResponseDto> getRendezVousById(@PathVariable Long id) {
        log.info("API: Récupération du rendez-vous {}", id);
        RendezVousResponseDto response = rendezVousService.getRendezVousById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer tous les rendez-vous
     * GET /api/rendezvous
     */
    @GetMapping
    public ResponseEntity<List<RendezVousResponseDto>> getAllRendezVous() {
        log.info("API: Récupération de tous les rendez-vous");
        List<RendezVousResponseDto> response = rendezVousService.getAllRendezVous();
        return ResponseEntity.ok(response);
    }

    /**
     * Mettre à jour un rendez-vous
     * PUT /api/rendezvous/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<RendezVousResponseDto> updateRendezVous(
            @PathVariable Long id,
            @Valid @RequestBody RendezVousRequestDto requestDto) {
        log.info("API: Mise à jour du rendez-vous {}", id);
        RendezVousResponseDto response = rendezVousService.updateRendezVous(id, requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Supprimer un rendez-vous
     * DELETE /api/rendezvous/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRendezVous(@PathVariable Long id) {
        log.info("API: Suppression du rendez-vous {}", id);
        rendezVousService.deleteRendezVous(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Annuler un rendez-vous
     * PATCH /api/rendezvous/{id}/cancel
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<RendezVousResponseDto> cancelRendezVous(@PathVariable Long id) {
        log.info("API: Annulation du rendez-vous {}", id);
        RendezVousResponseDto response = rendezVousService.cancelRendezVous(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Confirmer un rendez-vous
     * PATCH /api/rendezvous/{id}/confirm
     */
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<RendezVousResponseDto> confirmRendezVous(@PathVariable Long id) {
        log.info("API: Confirmation du rendez-vous {}", id);
        RendezVousResponseDto response = rendezVousService.confirmRendezVous(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Marquer un rendez-vous comme terminé
     * PATCH /api/rendezvous/{id}/complete
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<RendezVousResponseDto> completeRendezVous(@PathVariable Long id) {
        log.info("API: Marquage du rendez-vous {} comme terminé", id);
        RendezVousResponseDto response = rendezVousService.completeRendezVous(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer les rendez-vous d'un patient
     * GET /api/rendezvous/patient/{patientId}
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<RendezVousResponseDto>> getRendezVousByPatient(
            @PathVariable Long patientId) {
        log.info("API: Récupération des rendez-vous du patient {}", patientId);
        List<RendezVousResponseDto> response = rendezVousService.getRendezVousByPatient(patientId);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer les rendez-vous d'un médecin
     * GET /api/rendezvous/medecin/{medecinId}
     */
    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<RendezVousResponseDto>> getRendezVousByMedecin(
            @PathVariable Long medecinId) {
        log.info("API: Récupération des rendez-vous du médecin {}", medecinId);
        List<RendezVousResponseDto> response = rendezVousService.getRendezVousByMedecin(medecinId);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer les rendez-vous par statut
     * GET /api/rendezvous/statut/{statut}
     */
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<RendezVousResponseDto>> getRendezVousByStatut(
            @PathVariable StatutRendezVous statut) {
        log.info("API: Récupération des rendez-vous avec le statut {}", statut);
        List<RendezVousResponseDto> response = rendezVousService.getRendezVousByStatut(statut);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer les rendez-vous dans une plage de dates
     * GET /api/rendezvous/date-range?start=...&end=...
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<RendezVousResponseDto>> getRendezVousByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("API: Récupération des rendez-vous entre {} et {}", start, end);
        List<RendezVousResponseDto> response = rendezVousService.getRendezVousByDateRange(start, end);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer les rendez-vous à venir d'un patient
     * GET /api/rendezvous/patient/{patientId}/upcoming
     */
    @GetMapping("/patient/{patientId}/upcoming")
    public ResponseEntity<List<RendezVousResponseDto>> getUpcomingRendezVousByPatient(
            @PathVariable Long patientId) {
        log.info("API: Récupération des rendez-vous à venir du patient {}", patientId);
        List<RendezVousResponseDto> response = rendezVousService.getUpcomingRendezVousByPatient(patientId);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer les rendez-vous à venir d'un médecin
     * GET /api/rendezvous/medecin/{medecinId}/upcoming
     */
    @GetMapping("/medecin/{medecinId}/upcoming")
    public ResponseEntity<List<RendezVousResponseDto>> getUpcomingRendezVousByMedecin(
            @PathVariable Long medecinId) {
        log.info("API: Récupération des rendez-vous à venir du médecin {}", medecinId);
        List<RendezVousResponseDto> response = rendezVousService.getUpcomingRendezVousByMedecin(medecinId);
        return ResponseEntity.ok(response);
    }

    /**
     * Compter les rendez-vous par statut
     * GET /api/rendezvous/count/statut/{statut}
     */
    @GetMapping("/count/statut/{statut}")
    public ResponseEntity<Long> countRendezVousByStatut(@PathVariable StatutRendezVous statut) {
        log.info("API: Comptage des rendez-vous avec le statut {}", statut);
        long count = rendezVousService.countRendezVousByStatut(statut);
        return ResponseEntity.ok(count);
    }
}