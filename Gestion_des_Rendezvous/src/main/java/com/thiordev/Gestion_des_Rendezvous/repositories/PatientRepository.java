package com.thiordev.Gestion_des_Rendezvous.repositories;

import com.thiordev.Gestion_des_Rendezvous.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByEmail(String email);

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByNumeroSecuriteSociale(String nic);

    List<Patient> findAllByOrderByNomAsc();

    @Query("""
        SELECT p FROM Patient p
            WHERE LOWER(p.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                OR  LOWER(p.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%') )
    """)
    List<Patient> searchByNomOrPrenom(@Param("searchTerm") String searchTerm);


    @Query("SELECT COUNT(p) FROM Patient p")
    long countAllPatient();
}
