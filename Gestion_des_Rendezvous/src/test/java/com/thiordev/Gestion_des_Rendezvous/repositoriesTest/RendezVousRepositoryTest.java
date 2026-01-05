package com.thiordev.Gestion_des_Rendezvous.repositoriesTest;

import com.thiordev.Gestion_des_Rendezvous.models.Medecin;
import com.thiordev.Gestion_des_Rendezvous.models.Patient;
import com.thiordev.Gestion_des_Rendezvous.models.RendezVous;
import com.thiordev.Gestion_des_Rendezvous.models.Specialite;
import com.thiordev.Gestion_des_Rendezvous.repositories.MedecinRepository;
import com.thiordev.Gestion_des_Rendezvous.repositories.PatientRepository;
import com.thiordev.Gestion_des_Rendezvous.repositories.RendezVousRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test pour le RendezVousRepository")
public class RendezVousRepositoryTest {

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RendezVousRepository rendezVousRepository;

    private Medecin medecin1;
    private Medecin medecin2;
    private Patient patient1;
    private Patient patient2;

    private RendezVous rendezVous1;
    private RendezVous rendezVous2;
    private RendezVous rendezVous3;
    private RendezVous rdvAnnule;

    @BeforeEach
    public void setUp() {
        medecinRepository.deleteAll();
        patientRepository.deleteAll();
        rendezVousRepository.deleteAll();


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
                .specialite(Specialite.DERMATOLOGIE)
                .email("marieme.sy@hospital.sn")
                .telephone("+221772222222")
                .adresse("Thiès")
                .build();

        patient1 = Patient.builder()
                .prenom("Abdoulaye")
                .nom("Thior")
                .email("thior@gmail.com")
                .adresse("Medina fass, Dakar")
                .telephone("775565421")
                .numeroSecuriteSociale("123254588558855")
                .dateNaissance(LocalDate.of(2005, 5, 5))
                .build();

        patient2 = Patient.builder()
                .prenom("Aïda")
                .nom("Thiam")
                .email("thiam@gmail.com")
                .adresse("Sicap Mbao, Dakar")
                .telephone("785562123")
                .numeroSecuriteSociale("223854288558855")
                .dateNaissance(LocalDate.of(2005, 5, 5))
                .build();

        rendezVous1 = RendezVous.builder()
                .medecin(medecin1)
                .patient(patient1)
                .motifConsultation("maladie du coeur")
                .dateHeureDebut(LocalDateTime.now().plusDays(1).withHour(2).withMinute(0).withSecond(0).withNano(0))
                .dateHeureFin(LocalDateTime.now().plusDays(1).withHour(4).withMinute(0).withSecond(0).withNano(0))
                .statut(RendezVous.StatutRendezVous.EN_ATTENTE)
                .build();

        rendezVous2 = RendezVous.builder()
                .medecin(medecin2)
                .patient(patient2)
                .motifConsultation("maladie de la peau")
                .dateHeureDebut(LocalDateTime.now().plusDays(2).withHour(2).withMinute(0).withSecond(0).withNano(0))
                .dateHeureFin(LocalDateTime.now().plusDays(2).withHour(4).withMinute(0).withSecond(0).withNano(0))
                .statut(RendezVous.StatutRendezVous.TERMINE)
                .build();

        rendezVous3 = RendezVous.builder()
                .medecin(medecin2)
                .patient(patient1)
                .motifConsultation("maladie de la peau")
                .dateHeureDebut(LocalDateTime.now().plusDays(3).withHour(2).withMinute(0).withSecond(0).withNano(0))
                .dateHeureFin(LocalDateTime.now().plusDays(3).withHour(4).withMinute(0).withSecond(0).withNano(0))
                .statut(RendezVous.StatutRendezVous.EN_ATTENTE)
                .build();

        rdvAnnule = RendezVous.builder()
                .medecin(medecin1)
                .patient(patient1)
                .motifConsultation("Annulé")
                .dateHeureDebut(LocalDateTime.now().plusDays(4).withHour(2).withMinute(0).withSecond(0).withNano(0))
                .dateHeureFin(LocalDateTime.now().plusDays(4).withHour(4).withMinute(0).withSecond(0).withNano(0))
                .statut(RendezVous.StatutRendezVous.ANNULE)
                .build();


    }

    @Test
    @DisplayName("should find rendezèvous by id")
    public void find_by_id(){
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);
        rendezVousRepository.save(rendezVous1);

        Optional<RendezVous> found = rendezVousRepository.findById(rendezVous1.getId());

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(rendezVous1.getId(), found.get().getId());
    }

    @Test
    @DisplayName("Doit trouver le rendez-vous à partir de l'Id d'un patient")
    public void find_By_Patient_Id_Order_By_Date_Heure_Debut_Desc(){
        medecinRepository.save(medecin1);
        medecinRepository.save(medecin2);
        patientRepository.save(patient1);
        patientRepository.save(patient2);
        rendezVousRepository.save(rendezVous1);
        rendezVousRepository.save(rendezVous3);

        List<RendezVous> rdvFound = rendezVousRepository.findByPatientIdOrderByDateHeureDebutDesc(patient1.getId());

        Assertions.assertNotNull(rdvFound);
        Assertions.assertEquals(2, rdvFound.size());
        Assertions.assertTrue(rdvFound.contains(rendezVous1));
        Assertions.assertTrue(rdvFound.contains(rendezVous3));
        Assertions.assertEquals(rdvFound.get(0), rendezVous3);
        Assertions.assertEquals(rdvFound.get(1), rendezVous1);


    }

    @Test
    @DisplayName("Trouver rendez-vous par id du medecin ")
    public void find_By_Medecin_Id_Order_By_Date_Heure_Debut_Asc(){

        medecinRepository.save(medecin2);
        patientRepository.save(patient1);
        patientRepository.save(patient2);
        rendezVousRepository.save(rendezVous2);
        rendezVousRepository.save(rendezVous3);

        List<RendezVous> rendezVous = rendezVousRepository.findByMedecinIdOrderByDateHeureDebutAsc(medecin2.getId());

        Assertions.assertNotNull(rendezVous);
        Assertions.assertEquals(2, rendezVous.size());
        Assertions.assertTrue(rendezVous.contains(rendezVous2));
        Assertions.assertTrue(rendezVous.contains(rendezVous3));
        Assertions.assertEquals(rendezVous.get(0), rendezVous2);
        Assertions.assertEquals(rendezVous.get(1), rendezVous3);

    }

    @Test
    @DisplayName("Trouver les rendez-vous grâce au statut")
    public void find_By_Statut_Order_By_Date_Heure_Debut_Asc(){
        medecinRepository.save(medecin1);
        medecinRepository.save(medecin2);
        patientRepository.save(patient1);
        patientRepository.save(patient2);
        rendezVousRepository.save(rendezVous1);
        rendezVousRepository.save(rendezVous3);

        List<RendezVous> rdvfound = rendezVousRepository.findByStatutOrderByDateHeureDebutAsc(RendezVous.StatutRendezVous.EN_ATTENTE);

        Assertions.assertNotNull(rdvfound);
        Assertions.assertEquals(2, rdvfound.size());
        Assertions.assertTrue(rdvfound.contains(rendezVous1));
        Assertions.assertTrue(rdvfound.contains(rendezVous3));
        Assertions.assertEquals(rdvfound.get(0), rendezVous1);
        Assertions.assertEquals(rdvfound.get(1), rendezVous3);
    }

    @Test
    @DisplayName(("Trouver les rendez-vous avec l'id du medecin et le statut"))
    public void should_find_rdv_by_medecinId_and_statut(){
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);
        rendezVousRepository.save(rendezVous1);

        List<RendezVous> rdvFound = rendezVousRepository.findByMedecinIdAndStatut(medecin1.getId(), rendezVous1.getStatut());

        Assertions.assertNotNull(rdvFound);
        Assertions.assertEquals(1, rdvFound.size());
        Assertions.assertTrue(rdvFound.contains(rendezVous1));
    }

    @Test
    @DisplayName(("Trouver les rendez-vous avec l'id du patient et le statut"))
    public void should_find_rdv_by_patientId_and_statut(){
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);
        rendezVousRepository.save(rendezVous1);

        List<RendezVous> rdvFound = rendezVousRepository.findByPatientIdAndStatut(patient1.getId(), rendezVous1.getStatut());

        Assertions.assertNotNull(rdvFound);
        Assertions.assertEquals(1, rdvFound.size());
        Assertions.assertTrue(rdvFound.contains(rendezVous1));
    }

    @Test
    @DisplayName("Devrait sauvegarder un rendez-vous avec succès")
    void testSaveRendezVous() {
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);

        // Given
        LocalDateTime debut = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime fin = debut.plusHours(1);

        RendezVous rendezVous = RendezVous.builder()
                .patient(patient1)
                .medecin(medecin1)
                .dateHeureDebut(debut)
                .dateHeureFin(fin)
                .motifConsultation("Consultation cardiologique")
                .statut(RendezVous.StatutRendezVous.EN_ATTENTE)
                .build();

        // When
        RendezVous saved = rendezVousRepository.save(rendezVous);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStatut()).isEqualTo(RendezVous.StatutRendezVous.EN_ATTENTE);
        assertThat(saved.getDateCreation()).isNotNull();
    }


    @Test
    @DisplayName("Devrait trouver les rendez-vous dans une plage de dates")
    public void testFindByDateRange() {
        // Given - Sauvegarde des entités
        medecinRepository.save(medecin1);
        medecinRepository.save(medecin2);
        patientRepository.save(patient1);
        patientRepository.save(patient2);

        // Sauvegarde des 3 rendez-vous avec des dates différentes
        rendezVousRepository.save(rendezVous1); // 25 décembre 2025
        rendezVousRepository.save(rendezVous2); // 10 janvier 2026
        rendezVousRepository.save(rendezVous3); // 8 janvier 2026

        // When - Recherche entre le 1er janvier et le 9 janvier 2026
        List<RendezVous> rendezVousList = rendezVousRepository.findByDateRange(
                LocalDateTime.now().plusDays(3).withHour(1).withMinute(0),
                LocalDateTime.now().plusDays(6).withHour(2).withMinute(0)
        );

        // Then - Devrait trouver seulement le rendez-vous du 8 janvier (rendezVous3)
        Assertions.assertNotNull(rendezVousList);
        Assertions.assertEquals(1, rendezVousList.size());
        Assertions.assertTrue(rendezVousList.contains(rendezVous3));
        Assertions.assertEquals(rendezVousList.get(0).getDateHeureDebut(),
                LocalDateTime.now().plusDays(3).withHour(2).withMinute(0).withSecond(0).withNano(0));
        Assertions.assertEquals("maladie de la peau",
                rendezVousList.get(0).getMotifConsultation());
        Assertions.assertEquals("Thior", rendezVousList.get(0).getPatient().getNom());
        Assertions.assertEquals("Sy", rendezVousList.get(0).getMedecin().getNom());
    }

    @Test
    @DisplayName("Devrait trouver les rendez-vous d'un médecin dans une plage de dates")
    public void testFindByMedecinAndDateRange() {
        // Given
        medecinRepository.save(medecin1);
        medecinRepository.save(medecin2);
        patientRepository.save(patient1);
        patientRepository.save(patient2);

        rendezVousRepository.save(rendezVous1); // medecin1 - 25 déc 2025
        rendezVousRepository.save(rendezVous2); // medecin2 - 10 jan 2026
        rendezVousRepository.save(rendezVous3); // medecin2 - 8 jan 2026

        // When - Recherche les rendez-vous du medecin2 en janvier 2026
        List<RendezVous> rendezVousList = rendezVousRepository.findByMedecinAndDateRange(
                medecin2.getId(),
                LocalDateTime.now().plusDays(2).withHour(1).withMinute(0),
                LocalDateTime.now().plusDays(3).withHour(5).withMinute(0)
        );

        // Then - Devrait trouver les 2 rendez-vous du medecin2
        Assertions.assertNotNull(rendezVousList);
        Assertions.assertEquals(2, rendezVousList.size());
        Assertions.assertTrue(rendezVousList.contains(rendezVous2));
        Assertions.assertTrue(rendezVousList.contains(rendezVous3));

        // Vérifier le tri par date croissante
        Assertions.assertEquals(rendezVousList.get(0), rendezVous2); // 8 jan
        Assertions.assertEquals(rendezVousList.get(1), rendezVous3); // 10 jan
    }

    @Test
    @DisplayName("Devrait retourner une liste vide si le médecin n'a pas de rendez-vous dans la plage")
    public void testFindByMedecinAndDateRangeEmpty() {
        // Given
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);
        rendezVousRepository.save(rendezVous1); // medecin1 - 25 déc 2025

        // When - Recherche les rendez-vous du medecin1 en janvier 2026
        List<RendezVous> rendezVousList = rendezVousRepository.findByMedecinAndDateRange(
                medecin1.getId(),
                LocalDateTime.now().plusDays(1).withHour(1).withMinute(0),
                LocalDateTime.now().plusDays(4).withHour(3).withMinute(0)
        );

        // Then
        Assertions.assertNotNull(rendezVousList);
        Assertions.assertEquals(1, rendezVousList.size());
        Assertions.assertFalse(rendezVousList.isEmpty());
    }

    @Test
    @DisplayName("Devrait détecter un conflit de rendez-vous - chevauchement total")
    public void testExistsConflictingRendezVous_TotalOverlap() {
        // Given - Rendez-vous existant de 12h30 à 14h30
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);
        rendezVousRepository.save(rendezVous1);

        // When - Essayer de créer un rendez-vous exactement au même moment
        boolean conflict = rendezVousRepository.existsConflictingRendezVous(
                medecin1.getId(),
                LocalDateTime.now().plusDays(1).withHour(2).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(4).withMinute(0)
        );

        // Then
        Assertions.assertTrue(conflict);
    }

    @Test
    @DisplayName("Devrait détecter un conflit de rendez-vous - chevauchement partiel début")
    public void testExistsConflictingRendezVous_PartialOverlapStart() {
        // Given - Rendez-vous existant de 12h30 à 14h30
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);
        rendezVousRepository.save(rendezVous1);

        // When - Nouveau rendez-vous qui commence pendant le rendez-vous existant
        boolean conflict = rendezVousRepository.existsConflictingRendezVous(
                medecin1.getId(),
                LocalDateTime.now().plusDays(1).withHour(2).withMinute(30),  // Commence à 13h
                LocalDateTime.now().plusDays(1).withHour(4).withMinute(0)  // Finit à 15h
        );

        // Then
        Assertions.assertTrue(conflict);
    }

    @Test
    @DisplayName("Devrait détecter un conflit de rendez-vous - chevauchement partiel fin")
    public void testExistsConflictingRendezVous_PartialOverlapEnd() {
        // Given - Rendez-vous existant de 12h30 à 14h30
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);
        rendezVousRepository.save(rendezVous1);

        // When - Nouveau rendez-vous qui finit pendant le rendez-vous existant
        boolean conflict = rendezVousRepository.existsConflictingRendezVous(
                medecin1.getId(),
                LocalDateTime.now().plusDays(1).withHour(2).withMinute(0),  // Commence à 11h
                LocalDateTime.now().plusDays(1).withHour(4).withMinute(0)   // Finit à 13h
        );

        // Then
        Assertions.assertTrue(conflict);
    }

    @Test
    @DisplayName("Devrait détecter un conflit de rendez-vous - englobement")
    public void testExistsConflictingRendezVous_Engulfing() {
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);
        rendezVousRepository.save(rendezVous1);

        boolean conflict = rendezVousRepository.existsConflictingRendezVous(
                medecin1.getId(),
                LocalDateTime.now().plusDays(1).withHour(1).withMinute(0),  // Commence avant
                LocalDateTime.now().plusDays(1).withHour(5).withMinute(0)   // Finit après
        );

        // Then
        Assertions.assertTrue(conflict);
    }

    @Test
    @DisplayName("Ne devrait pas détecter de conflit - pas de chevauchement")
    public void testExistsConflictingRendezVous_NoConflict() {
        // Given - Rendez-vous existant de 12h30 à 14h30
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);
        rendezVousRepository.save(rendezVous1);

        // When - Nouveau rendez-vous après le rendez-vous existant
        boolean conflict = rendezVousRepository.existsConflictingRendezVous(
                medecin1.getId(),
                LocalDateTime.now().plusDays(1).withHour(6).withMinute(0).withSecond(0).withNano(0),  // Commence après
                LocalDateTime.now().plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0)
        );

        // Then
        Assertions.assertFalse(conflict);
    }

    @Test
    @DisplayName("Ne devrait pas détecter de conflit - médecin différent")
    public void testExistsConflictingRendezVous_DifferentMedecin() {
        // Given - Rendez-vous pour medecin1
        medecinRepository.save(medecin1);
        medecinRepository.save(medecin2);
        patientRepository.save(patient1);
        rendezVousRepository.save(rendezVous1);

        // When - Vérifier conflit pour medecin2 au même moment
        boolean conflict = rendezVousRepository.existsConflictingRendezVous(
                medecin2.getId(),
                LocalDateTime.of(2025, 12, 25, 12, 30),
                LocalDateTime.of(2025, 12, 25, 14, 30)
        );

        // Then
        Assertions.assertFalse(conflict);
    }

    @Test
    @DisplayName("Ne devrait pas détecter de conflit si le rendez-vous est annulé")
    public void testExistsConflictingRendezVous_CancelledRendezVous() {
        // Given - Rendez-vous annulé
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);

        rendezVous1.setStatut(RendezVous.StatutRendezVous.ANNULE);
        rendezVousRepository.save(rendezVous1);

        // When - Vérifier conflit au même moment
        boolean conflict = rendezVousRepository.existsConflictingRendezVous(
                medecin1.getId(),
                LocalDateTime.now().plusDays(1).withHour(1).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().plusDays(3).withHour(2).withMinute(0).withSecond(0).withNano(0)
        );

        // Then
        Assertions.assertFalse(conflict);
    }

    @Test
    @DisplayName("Devrait détecter un conflit en excluant le rendez-vous actuel")
    public void testExistsConflictingRendezVousExcludingCurrent_WithConflict() {
        // Given - Deux rendez-vous pour le même médecin
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);
        patientRepository.save(patient2);

        RendezVous rdv1 = rendezVousRepository.save(rendezVous1); // 12h30-14h30

        RendezVous rdv2 = RendezVous.builder()
                .medecin(medecin1)
                .patient(patient2)
                .motifConsultation("Autre consultation")
                .dateHeureDebut(LocalDateTime.now().plusDays(1).withHour(2).withMinute(0))
                .dateHeureFin(LocalDateTime.now().plusDays(1).withHour(4).withMinute(0))
                .statut(RendezVous.StatutRendezVous.CONFIRME)
                .build();
        RendezVous savedRdv2 = rendezVousRepository.save(rdv2);

        // When - Modifier rdv1 pour qu'il chevauche rdv2
        boolean conflict = rendezVousRepository.existsConflictingRendezVousExcludingCurrent(
                medecin1.getId(),
                rdv1.getId(),
                LocalDateTime.now().plusDays(1).withHour(2).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(4).withMinute(0)
        );

        // Then
        Assertions.assertTrue(conflict);
    }

    @Test
    @DisplayName("Ne devrait pas détecter de conflit pour le rendez-vous lui-même")
    public void testExistsConflictingRendezVousExcludingCurrent_NoSelfConflict() {
        // Given
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);
        RendezVous savedRdv = rendezVousRepository.save(rendezVous1);

        // When - Vérifier le rendez-vous contre lui-même (lors d'une modification)
        boolean conflict = rendezVousRepository.existsConflictingRendezVousExcludingCurrent(
                medecin1.getId(),
                savedRdv.getId(),
                LocalDateTime.now().plusDays(1).withHour(2).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().plusDays(1).withHour(4).withMinute(0).withSecond(0).withNano(0)
        );

        // Then
        Assertions.assertFalse(conflict);
    }

    @Test
    @DisplayName("Devrait compter les rendez-vous par statut")
    public void testCountByStatut() {
        // Given
        medecinRepository.save(medecin1);
        medecinRepository.save(medecin2);
        patientRepository.save(patient1);
        patientRepository.save(patient2);

        rendezVousRepository.save(rendezVous1); // EN_ATTENTE
        rendezVousRepository.save(rendezVous2); // TERMINE
        rendezVousRepository.save(rendezVous3); // EN_ATTENTE

        // When
        long enAttente = rendezVousRepository.countByStatut(RendezVous.StatutRendezVous.EN_ATTENTE);
        long termine = rendezVousRepository.countByStatut(RendezVous.StatutRendezVous.TERMINE);
        long confirme = rendezVousRepository.countByStatut(RendezVous.StatutRendezVous.CONFIRME);
        long annule = rendezVousRepository.countByStatut(RendezVous.StatutRendezVous.ANNULE);

        // Then
        Assertions.assertEquals(2, enAttente);
        Assertions.assertEquals(1, termine);
        Assertions.assertEquals(0, confirme);
        Assertions.assertEquals(0, annule);
    }

    @Test
    @DisplayName("Devrait trouver les rendez-vous à venir pour un patient")
    public void testFindUpcomingRendezVousByPatient() {
        // Given
        medecinRepository.save(medecin1);
        medecinRepository.save(medecin2);
        patientRepository.save(patient1);
        patientRepository.save(patient2);

        // Rendez-vous passés et futurs
        rendezVousRepository.save(rendezVous1); // 25 déc 2025 - EN_ATTENTE
        rendezVousRepository.save(rendezVous3); // 8 jan 2026 - EN_ATTENTE

        // Rendez-vous annulé (ne doit pas apparaître)

        rendezVousRepository.save(rdvAnnule);

        LocalDateTime now = LocalDateTime.now().plusDays(1).withHour(1).withMinute(0).withSecond(0).withSecond(0).withNano(0);
        List<RendezVous> upcomingRdv = rendezVousRepository.findUpcomingRendezVousByPatient(
                patient1.getId(),
                now
        );

        // Then - Devrait trouver seulement rendezVous3 (après le 1er jan et non annulé)
        Assertions.assertNotNull(upcomingRdv);
        Assertions.assertEquals(2, upcomingRdv.size());
        Assertions.assertTrue(upcomingRdv.contains(rendezVous1));
        Assertions.assertTrue(upcomingRdv.contains(rendezVous3));
    }

    @Test
    @DisplayName("Devrait trouver les rendez-vous à venir avec statut CONFIRME")
    public void testFindUpcomingRendezVousByPatient_WithConfirmedStatus() {
        // Given
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);

        RendezVous rdvConfirme = RendezVous.builder()
                .medecin(medecin1)
                .patient(patient1)
                .motifConsultation("Confirmé")
                .dateHeureDebut(LocalDateTime.now().plusDays(5).withHour(2).withMinute(0).withSecond(0).withNano(0))
                .dateHeureFin(LocalDateTime.now().plusDays(5).withHour(4).withMinute(0).withSecond(0).withNano(0))
                .statut(RendezVous.StatutRendezVous.CONFIRME)
                .build();
        rendezVousRepository.save(rdvConfirme);

        // When
        LocalDateTime now = LocalDateTime.now().plusDays(5).withHour(1).withMinute(0).withSecond(0).withNano(0);
        List<RendezVous> upcomingRdv = rendezVousRepository.findUpcomingRendezVousByPatient(
                patient1.getId(),
                now
        );

        // Then
        Assertions.assertNotNull(upcomingRdv);
        Assertions.assertEquals(1, upcomingRdv.size());
        Assertions.assertEquals(RendezVous.StatutRendezVous.CONFIRME, upcomingRdv.get(0).getStatut());
    }

    @Test
    @DisplayName("Ne devrait pas trouver les rendez-vous terminés dans les rendez-vous à venir")
    public void testFindUpcomingRendezVousByPatient_NoTerminated() {
        // Given
        medecinRepository.save(medecin2);
        patientRepository.save(patient2);

        rendezVousRepository.save(rendezVous2); // TERMINE

        // When
        LocalDateTime now = LocalDateTime.now().plusDays(2).withHour(1).withMinute(0).withSecond(0).withNano(0);
        List<RendezVous> upcomingRdv = rendezVousRepository.findUpcomingRendezVousByPatient(
                patient2.getId(),
                now
        );

        // Then
        Assertions.assertNotNull(upcomingRdv);
        Assertions.assertEquals(0, upcomingRdv.size());
    }

    @Test
    @DisplayName("Devrait trouver les rendez-vous à venir pour un médecin")
    public void testFindUpcomingRendezVousByMedecin() {
        // Given
        medecinRepository.save(medecin2);
        patientRepository.save(patient1);
        patientRepository.save(patient2);

        rendezVousRepository.save(rendezVous2);
        rendezVousRepository.save(rendezVous3);

        // When
        LocalDateTime now = LocalDateTime.now().plusDays(1).withHour(1).withMinute(0).withSecond(0).withNano(0);
        List<RendezVous> upcomingRdv = rendezVousRepository.findUpcomingRendezVousByMedecin(
                medecin2.getId(),
                now
        );

        // Then - Devrait trouver seulement rendezVous3 (EN_ATTENTE, pas TERMINE)
        Assertions.assertNotNull(upcomingRdv);
        Assertions.assertEquals(1, upcomingRdv.size());
        Assertions.assertTrue(upcomingRdv.contains(rendezVous3));
        Assertions.assertEquals(upcomingRdv.get(0).getDateHeureDebut(),
                LocalDateTime.now().plusDays(3).withHour(2).withMinute(0).withSecond(0).withNano(0));
    }

    @Test
    @DisplayName("Devrait retourner une liste vide si le médecin n'a pas de rendez-vous à venir")
    public void testFindUpcomingRendezVousByMedecin_Empty() {
        // Given
        medecinRepository.save(medecin1);
        patientRepository.save(patient1);

        rendezVousRepository.save(rendezVous1); // 25 déc 2025

        // When
        LocalDateTime now = LocalDateTime.now().plusDays(2).withHour(1).withMinute(0).withSecond(0).withNano(0);
        List<RendezVous> upcomingRdv = rendezVousRepository.findUpcomingRendezVousByMedecin(
                medecin1.getId(),
                now
        );

        // Then
        Assertions.assertNotNull(upcomingRdv);
        Assertions.assertEquals(0, upcomingRdv.size());
        Assertions.assertTrue(upcomingRdv.isEmpty());
    }
}
