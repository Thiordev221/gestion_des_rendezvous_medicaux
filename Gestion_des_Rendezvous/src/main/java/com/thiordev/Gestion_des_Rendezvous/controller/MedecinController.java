package com.thiordev.Gestion_des_Rendezvous.controller;

import com.thiordev.Gestion_des_Rendezvous.dto.request.MedecinRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.MedecinResponseDto;
import com.thiordev.Gestion_des_Rendezvous.models.Specialite;
import com.thiordev.Gestion_des_Rendezvous.service.MedecinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour la gestion des médecins
 * Base URL: /api/medecins
 */
@RestController
@RequestMapping("/api/medecins")
@RequiredArgsConstructor
@Slf4j
public class MedecinController {

    private final MedecinService medecinService;

    /**
     * Créer un nouveau médecin
     * POST /api/medecins
     */
    @PostMapping
    public ResponseEntity<MedecinResponseDto> createMedecin(
            @Valid @RequestBody MedecinRequestDto requestDto) {
        log.info("API: Création d'un nouveau médecin");
        MedecinResponseDto response = medecinService.createMedecin(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupérer un médecin par son ID
     * GET /api/medecins/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedecinResponseDto> getMedecinById(@PathVariable Long id) {
        log.info("API: Récupération du médecin {}", id);
        MedecinResponseDto response = medecinService.getMedecinById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer tous les médecins
     * GET /api/medecins
     */
    @GetMapping
    public ResponseEntity<List<MedecinResponseDto>> getAllMedecins() {
        log.info("API: Récupération de tous les médecins");
        List<MedecinResponseDto> response = medecinService.getAllMedecins();
        return ResponseEntity.ok(response);
    }

    /**
     * Mettre à jour un médecin
     * PUT /api/medecins/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedecinResponseDto> updateMedecin(
            @PathVariable Long id,
            @Valid @RequestBody MedecinRequestDto requestDto) {
        log.info("API: Mise à jour du médecin {}", id);
        MedecinResponseDto response = medecinService.updateMedecin(id, requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Supprimer un médecin
     * DELETE /api/medecins/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedecin(@PathVariable Long id) {
        log.info("API: Suppression du médecin {}", id);
        medecinService.deleteMedecin(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupérer les médecins par spécialité
     * GET /api/medecins/specialite/{specialite}
     */
    @GetMapping("/specialite/{specialite}")
    public ResponseEntity<List<MedecinResponseDto>> getMedecinsBySpecialite(
            @PathVariable Specialite specialite) {
        log.info("API: Récupération des médecins de spécialité {}", specialite);
        List<MedecinResponseDto> response = medecinService.getMedecinsBySpecialite(specialite);
        return ResponseEntity.ok(response);
    }

    /**
     * Rechercher des médecins par nom ou prénom
     * GET /api/medecins/search?q=terme
     */
    @GetMapping("/search")
    public ResponseEntity<List<MedecinResponseDto>> searchMedecins(
            @RequestParam(name = "q") String searchTerm) {
        log.info("API: Recherche de médecins avec le terme '{}'", searchTerm);
        List<MedecinResponseDto> response = medecinService.searchMedecins(searchTerm);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer un médecin par email
     * GET /api/medecins/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<MedecinResponseDto> getMedecinByEmail(@PathVariable String email) {
        log.info("API: Récupération du médecin avec l'email {}", email);
        MedecinResponseDto response = medecinService.getMedecinByEmail(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Compter le nombre de médecins par spécialité
     * GET /api/medecins/count/specialite/{specialite}
     */
    @GetMapping("/count/specialite/{specialite}")
    public ResponseEntity<Long> countMedecinsBySpecialite(@PathVariable Specialite specialite) {
        log.info("API: Comptage des médecins de spécialité {}", specialite);
        long count = medecinService.countMedecinsBySpecialite(specialite);
        return ResponseEntity.ok(count);
    }
}