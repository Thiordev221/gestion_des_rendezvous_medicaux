package com.thiordev.Gestion_des_Rendezvous;

import com.github.javafaker.Faker;
import com.thiordev.Gestion_des_Rendezvous.models.Medecin;
import com.thiordev.Gestion_des_Rendezvous.models.Patient;
import com.thiordev.Gestion_des_Rendezvous.models.RendezVous;
import com.thiordev.Gestion_des_Rendezvous.models.Specialite;
import com.thiordev.Gestion_des_Rendezvous.repositories.MedecinRepository;
import com.thiordev.Gestion_des_Rendezvous.repositories.PatientRepository;
import com.thiordev.Gestion_des_Rendezvous.repositories.RendezVousRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class GestionDesRendezvousApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionDesRendezvousApplication.class, args);
	}

//    @Bean
    public CommandLineRunner commandLineRunner(
            PatientRepository patientRepository,
            MedecinRepository medecinRepository,
                RendezVousRepository rendezVousRepository
    ) {
        Faker faker = new Faker();
        return args -> {
            for(int i = 0; i < 10; i++){
                LocalDate localDate = faker.date()
                        .birthday()
                        .toInstant()                     // Instant
                        .atZone(ZoneId.systemDefault())  // Zone
                        .toLocalDate();
//                var patient = Patient.builder()
//                        .prenom(faker.name().firstName())
//                        .nom(faker.name().lastName())
//                        .email(faker.internet().emailAddress())
//                        .adresse(faker.address().fullAddress())
//                        .telephone(faker.phoneNumber().cellPhone())
//                        .numeroSecuriteSociale(faker.number().digits(15))
//                        .dateNaissance(localDate)
//                        .build();
//                Specialite[] values = Specialite.values();
//                Specialite randomSpec = values[new Random().nextInt(values.length)];
//
//                var medecin = Medecin.builder()
//                        .prenom(faker.name().firstName())
//                        .nom(faker.name().lastName())
//                        .adresse(faker.address().fullAddress())
//                        .telephone(faker.phoneNumber().cellPhone())
//                        .specialite(randomSpec)
//                        .email(faker.internet().emailAddress())
//                        .build();



//                patientRepository.save(patient);
//                medecinRepository.save(medecin);
                List<Patient> patients = patientRepository.findAll();
                List<Medecin> medecins = medecinRepository.findAll();
                RendezVous.StatutRendezVous[] status = RendezVous.StatutRendezVous.values();
                RendezVous.StatutRendezVous statutRendezVous = status[new Random().nextInt(status.length)];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                var rendezVous = RendezVous.builder()
                        .medecin(medecins.get(new Random().nextInt(medecins.size())))
                        .patient(patients.get(new Random().nextInt(patients.size())))
                        .dateHeureDebut(LocalDateTime.parse("2025-12-16 12:00:00", formatter))
                        .dateHeureFin(LocalDateTime.parse("2025-12-16 12:30:00", formatter))
                        .statut(statutRendezVous)
                        .motifConsultation(faker.lorem().sentence())
                        .build();

                rendezVousRepository.save(rendezVous);


            }
        };
    }
}
