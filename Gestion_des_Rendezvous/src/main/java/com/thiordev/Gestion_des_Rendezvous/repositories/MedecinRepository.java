package com.thiordev.Gestion_des_Rendezvous.repositories;

import com.thiordev.Gestion_des_Rendezvous.models.Medecin;
import com.thiordev.Gestion_des_Rendezvous.models.Specialite;
import jakarta.transaction.Transactional;
import jdk.jfr.TransitionFrom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Long> {

    boolean existsByEmail(String email);

    Optional<Medecin> findByEmail(String email);

    @Query(
            "SELECT m FROM Medecin m WHERE m.specialite = :specialite"
    )
    List<Medecin> findBySpecialite(@Param("specialite") Specialite specialite);

    @Query("SELECT m FROM Medecin m WHERE " +
            "LOWER(m.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(m.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Medecin> searchByNomOrPrenom(@Param("searchTerm") String searchTerm);


    List<Medecin> findAllByOrderBySpecialiteAscNomAsc();


    @Query("SELECT COUNT(m) FROM Medecin m WHERE m.specialite = :specialite")
    long countBySpecialite(@Param("specialite") Specialite specialite);


    @Query("SELECT DISTINCT m FROM Medecin m JOIN m.rendezVouses r")
    List<Medecin> findMedecinsWithRendezVous();
}
