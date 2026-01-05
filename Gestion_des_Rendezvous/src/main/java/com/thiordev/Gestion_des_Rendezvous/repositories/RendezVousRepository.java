package com.thiordev.Gestion_des_Rendezvous.repositories;

import com.thiordev.Gestion_des_Rendezvous.models.RendezVous;
import com.thiordev.Gestion_des_Rendezvous.models.RendezVous.StatutRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    Optional<RendezVous> findById(Long id);

    List<RendezVous> findByPatientIdOrderByDateHeureDebutDesc(Long patientId);

    List<RendezVous> findByMedecinIdOrderByDateHeureDebutAsc(Long medecinId);

    List<RendezVous> findByStatutOrderByDateHeureDebutAsc(StatutRendezVous statut);


    List<RendezVous> findByPatientIdAndStatut(Long patientId, StatutRendezVous statut);

    List<RendezVous> findByMedecinIdAndStatut(Long medecinId, StatutRendezVous statut);


    @Query("SELECT r FROM RendezVous r WHERE r.dateHeureDebut BETWEEN :startDate AND :endDate " +
            "ORDER BY r.dateHeureDebut ASC")
    List<RendezVous> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT r FROM RendezVous r WHERE r.medecin.id = :medecinId " +
            "AND r.dateHeureDebut BETWEEN :startDate AND :endDate " +
            "ORDER BY r.dateHeureDebut ASC")
    List<RendezVous> findByMedecinAndDateRange(@Param("medecinId") Long medecinId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);


    @Query("SELECT COUNT(r) > 0 FROM RendezVous r WHERE r.medecin.id = :medecinId " +
            "AND r.statut NOT IN ('ANNULE') " +
            "AND ((r.dateHeureDebut < :endDate AND r.dateHeureFin > :startDate))")
    boolean existsConflictingRendezVous(@Param("medecinId") Long medecinId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);


    @Query("SELECT COUNT(r) > 0 FROM RendezVous r WHERE r.medecin.id = :medecinId " +
            "AND r.id != :rendezVousId " +
            "AND r.statut NOT IN ('ANNULE') " +
            "AND ((r.dateHeureDebut < :endDate AND r.dateHeureFin > :startDate))")
    boolean existsConflictingRendezVousExcludingCurrent(@Param("medecinId") Long medecinId,
                                                        @Param("rendezVousId") Long rendezVousId,
                                                        @Param("startDate") LocalDateTime startDate,
                                                        @Param("endDate") LocalDateTime endDate);


    long countByStatut(StatutRendezVous statut);


    @Query("SELECT r FROM RendezVous r WHERE r.patient.id = :patientId " +
            "AND r.dateHeureDebut > :now " +
            "AND r.statut IN ('EN_ATTENTE', 'CONFIRME') " +
            "ORDER BY r.dateHeureDebut ASC")
    List<RendezVous> findUpcomingRendezVousByPatient(@Param("patientId") Long patientId,
                                                     @Param("now") LocalDateTime now);


    @Query("SELECT r FROM RendezVous r WHERE r.medecin.id = :medecinId " +
            "AND r.dateHeureDebut > :now " +
            "AND r.statut IN ('EN_ATTENTE', 'CONFIRME') " +
            "ORDER BY r.dateHeureDebut ASC")
    List<RendezVous> findUpcomingRendezVousByMedecin(@Param("medecinId") Long medecinId,
                                                     @Param("now") LocalDateTime now);
}