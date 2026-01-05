package com.thiordev.Gestion_des_Rendezvous.repositoriesTest;

import com.thiordev.Gestion_des_Rendezvous.models.Patient;
import com.thiordev.Gestion_des_Rendezvous.repositories.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test du PatientRepository")
public class PatientRepositoryTest {
    @Autowired
    private PatientRepository patientRepository;

    private Patient patient1;
    private Patient patient2;

    @BeforeEach
    public void setup(){
        patientRepository.deleteAll();



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

    }

    @Test
    @DisplayName("Troufer un patient par son id")
    public void should_find_patient_by_id(){
        Patient saved = patientRepository.save(patient1);
        patientRepository.save(patient2);

        Optional<Patient> found =  patientRepository.findById(saved.getId());

        Assertions.assertTrue(found.isPresent());
        Assertions.assertNotNull(found.get());
        Assertions.assertEquals(patient1, found.get());

    }

    @Test
    @DisplayName("Teste si l'utilisateur exite par son email ")
    public void existsPatientByEmail(){
        Patient saved = patientRepository.save(patient1);
        boolean found = patientRepository.existsByEmail(saved.getEmail());
        boolean notFound = patientRepository.existsByEmail("moi@GMail.com");

        Assertions.assertTrue(found);
        Assertions.assertFalse(notFound);
    }

    @Test
    @DisplayName("Doit trouver un Patient par son Email")
    public void should_find_by_email(){
        Patient saved = patientRepository.save(patient1);

        Optional<Patient> found = patientRepository.findByEmail(saved.getEmail());

        Assertions.assertNotNull(found);
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(saved.getEmail(), found.get().getEmail());
        Assertions.assertEquals(saved.getPrenom(), found.get().getPrenom());
        Assertions.assertEquals(saved.getAdresse(), found.get().getAdresse());
        Assertions.assertEquals(saved.getTelephone(), found.get().getTelephone());
        Assertions.assertEquals(saved.getNom(), found.get().getNom());
        Assertions.assertEquals(saved.getNumeroSecuriteSociale(), found.get().getNumeroSecuriteSociale());

    }
    @Test
    @DisplayName("Doit trouver un Patient par son numéro de sécurité social")
    public void should_find_by_numero_de_securite_social(){
        Patient saved = patientRepository.save(patient1);

        Optional<Patient> found = patientRepository.findByNumeroSecuriteSociale(saved.getNumeroSecuriteSociale());

        Assertions.assertNotNull(found);
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(saved.getEmail(), found.get().getEmail());
        Assertions.assertEquals(saved.getPrenom(), found.get().getPrenom());
        Assertions.assertEquals(saved.getAdresse(), found.get().getAdresse());
        Assertions.assertEquals(saved.getTelephone(), found.get().getTelephone());
        Assertions.assertEquals(saved.getNom(), found.get().getNom());
        Assertions.assertEquals(saved.getNumeroSecuriteSociale(), found.get().getNumeroSecuriteSociale());

    }

    @Test
    @DisplayName("Doit trouver tous les patients triés de leurs noms dans l'ordre alphabétique")
    public void should_fin_all_patients_by_order_by_name_asc(){
       patientRepository.save(patient1);
       patientRepository.save(patient2);

       List<Patient> patients = patientRepository.findAllByOrderByNomAsc();

       Assertions.assertNotNull(patients);
       Assertions.assertEquals(2, patients.size());
       Assertions.assertTrue(patients.contains(patient1));
       Assertions.assertTrue(patients.contains(patient2));
       Assertions.assertEquals(patients.get(0), patient2);
       Assertions.assertNotEquals(patients.get(0), patient1);
    }

    @Test
    @DisplayName("Doit trouver tous les patients par leur nom ou leur prenom")
    public void should_find_by_search_by_nom_or_prenom(){
        patientRepository.save(patient1);
        patientRepository.save(patient2);

        List<Patient> patients = patientRepository.searchByNomOrPrenom("ab");
        Assertions.assertNotNull(patients);
        Assertions.assertEquals(1, patients.size());
        Assertions.assertTrue(patients.contains(patient1));
        Assertions.assertTrue(patients.get(0).getPrenom().toLowerCase().contains("ab"));
    }


    @Test
    @DisplayName("Doit trouver le nombre de patients")
    public void sould_count_all_patients(){
        patientRepository.save(patient1);
        patientRepository.save(patient2);

        long count = patientRepository.count();
        List<Patient> patients = patientRepository.findAll();

        Assertions.assertEquals(count, patients.size());


    }
}
