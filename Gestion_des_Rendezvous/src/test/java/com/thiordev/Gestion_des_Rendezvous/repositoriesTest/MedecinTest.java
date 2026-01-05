package com.thiordev.Gestion_des_Rendezvous.repositoriesTest;

import com.thiordev.Gestion_des_Rendezvous.models.Medecin;
import com.thiordev.Gestion_des_Rendezvous.models.Specialite;
import com.thiordev.Gestion_des_Rendezvous.repositories.MedecinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Tests du MedecinRepository")
class MedecinRepositoryTest {

    @Autowired
    private MedecinRepository medecinRepository;

    private Medecin medecin1;
    private Medecin medecin2;
    private Medecin medecin3;

    @BeforeEach
    void setUp() {
        medecinRepository.deleteAll();

        medecin1 = Medecin.builder()
                .nom("Fall")
                .prenom("Ibrahima")
                .specialite(Specialite.CARDIOLOGIE)
                .email("ibrahima.fall@hospital.sn")
                .telephone("+221771111111")
                .adresse("Dakar")
                .build();

        medecin2 = Medecin.builder()
                .nom("Sy")
                .prenom("Marieme")
                .specialite(Specialite.PEDIATRIE)
                .email("marieme.sy@hospital.sn")
                .telephone("+221772222222")
                .adresse("Thiès")
                .build();

        medecin3 = Medecin.builder()
                .nom("Sall")
                .prenom("Moussa")
                .specialite(Specialite.CARDIOLOGIE)
                .email("moussa.sall@hospital.sn")
                .telephone("+221773333333")
                .adresse("Saint-Louis")
                .build();
    }

    @Test
    @DisplayName("Devrait sauvegarder un médecin avec succès")
    void testSaveMedecin() {
        // When
        Medecin savedMedecin = medecinRepository.save(medecin1);

        // Then
        assertThat(savedMedecin).isNotNull();
        assertThat(savedMedecin.getId()).isNotNull();
        assertThat(savedMedecin.getNom()).isEqualTo("Fall");
        assertThat(savedMedecin.getSpecialite()).isEqualTo(Specialite.CARDIOLOGIE);
        assertThat(savedMedecin.getDateCreation()).isNotNull();
    }

    @Test
    @DisplayName("Devrait trouver un médecin par email")
    void testFindByEmail() {
        // Given
        medecinRepository.save(medecin1);

        // When
        Optional<Medecin> foundMedecin = medecinRepository.findByEmail("ibrahima.fall@hospital.sn");

        // Then
        assertThat(foundMedecin).isPresent();
        assertThat(foundMedecin.get().getNom()).isEqualTo("Fall");
    }

    @Test
    @DisplayName("Devrait vérifier si un email existe")
    void testExistsByEmail() {
        // Given
        medecinRepository.save(medecin1);

        // When
        boolean exists = medecinRepository.existsByEmail("ibrahima.fall@hospital.sn");
        boolean notExists = medecinRepository.existsByEmail("inexistant@hospital.sn");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Devrait trouver tous les médecins d'une spécialité")
    void testFindBySpecialite() {
        // Given
        medecinRepository.save(medecin1); // CARDIOLOGIE
        medecinRepository.save(medecin2); // PEDIATRIE
        medecinRepository.save(medecin3); // CARDIOLOGIE

        // When
        List<Medecin> cardiologues = medecinRepository.findBySpecialite(Specialite.CARDIOLOGIE);
        List<Medecin> pediatres = medecinRepository.findBySpecialite(Specialite.PEDIATRIE);

        // Then
        assertThat(cardiologues).hasSize(2);
        assertThat(pediatres).hasSize(1);
        assertThat(pediatres.get(0).getPrenom()).isEqualTo("Marieme");
    }

    @Test
    @DisplayName("Devrait rechercher des médecins par nom ou prénom")
    void testSearchByNomOrPrenom() {
        // Given
        medecinRepository.save(medecin1); // Fall Ibrahima
        medecinRepository.save(medecin2); // Sy Marieme
        medecinRepository.save(medecin3); // Sall Moussa

        // When
        List<Medecin> resultsByNom = medecinRepository.searchByNomOrPrenom("Fall");
        List<Medecin> resultsByPrenom = medecinRepository.searchByNomOrPrenom("Moussa");
        List<Medecin> resultsPartial = medecinRepository.searchByNomOrPrenom("sa"); // Sall et Moussa

        // Then
        assertThat(resultsByNom).hasSize(1);
        assertThat(resultsByPrenom).hasSize(1);
        assertThat(resultsPartial).hasSize(1); // Sall
    }

    @Test
    @DisplayName("Devrait trouver tous les médecins triés par spécialité et nom")
    void testFindAllByOrderBySpecialiteAscNomAsc() {
        // Given
        medecinRepository.save(medecin2); // PEDIATRIE - Sy
        medecinRepository.save(medecin1); // CARDIOLOGIE - Fall
        medecinRepository.save(medecin3); // CARDIOLOGIE - Sall

        // When
        List<Medecin> medecins = medecinRepository.findAllByOrderBySpecialiteAscNomAsc();

        // Then
        assertThat(medecins).hasSize(3);
        // CARDIOLOGIE vient avant PEDIATRIE alphabétiquement
        assertThat(medecins.get(0).getSpecialite()).isEqualTo(Specialite.CARDIOLOGIE);
        assertThat(medecins.get(0).getNom()).isEqualTo("Fall");
        assertThat(medecins.get(1).getNom()).isEqualTo("Sall");
        assertThat(medecins.get(2).getSpecialite()).isEqualTo(Specialite.PEDIATRIE);
    }

    @Test
    @DisplayName("Devrait compter le nombre de médecins par spécialité")
    void testCountBySpecialite() {
        // Given
        medecinRepository.save(medecin1); // CARDIOLOGIE
        medecinRepository.save(medecin2); // PEDIATRIE
        medecinRepository.save(medecin3); // CARDIOLOGIE

        // When
        long cardioCount = medecinRepository.countBySpecialite(Specialite.CARDIOLOGIE);
        long pediatrieCount = medecinRepository.countBySpecialite(Specialite.PEDIATRIE);
        long dermatoCount = medecinRepository.countBySpecialite(Specialite.DERMATOLOGIE);

        // Then
        assertThat(cardioCount).isEqualTo(2);
        assertThat(pediatrieCount).isEqualTo(1);
        assertThat(dermatoCount).isZero();
    }

    @Test
    @DisplayName("Devrait supprimer un médecin")
    void testDeleteMedecin() {
        // Given
        Medecin savedMedecin = medecinRepository.save(medecin1);

        // When
        medecinRepository.deleteById(savedMedecin.getId());

        // Then
        Optional<Medecin> deletedMedecin = medecinRepository.findById(savedMedecin.getId());
        assertThat(deletedMedecin).isEmpty();
    }

    @Test
    @DisplayName("Devrait mettre à jour un médecin")
    void testUpdateMedecin() {
        // Given
        Medecin savedMedecin = medecinRepository.save(medecin1);
        Long medecinId = savedMedecin.getId();

        // When
        savedMedecin.setTelephone("+221770000000");
        savedMedecin.setSpecialite(Specialite.NEUROLOGIE);
        medecinRepository.save(savedMedecin);

        // Then
        Optional<Medecin> updatedMedecin = medecinRepository.findById(medecinId);
        assertThat(updatedMedecin).isPresent();
        assertThat(updatedMedecin.get().getTelephone()).isEqualTo("+221770000000");
        assertThat(updatedMedecin.get().getSpecialite()).isEqualTo(Specialite.NEUROLOGIE);
    }
}