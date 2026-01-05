package com.thiordev.Gestion_des_Rendezvous.service;

import com.thiordev.Gestion_des_Rendezvous.dto.request.PatientRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.PatientResponseDto;
import com.thiordev.Gestion_des_Rendezvous.exception.ConflictException;
import com.thiordev.Gestion_des_Rendezvous.exception.RessourceNotFoundException;
import com.thiordev.Gestion_des_Rendezvous.mapper.PatientMapper;
import com.thiordev.Gestion_des_Rendezvous.models.Patient;
import com.thiordev.Gestion_des_Rendezvous.repositories.PatientRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientResponseDto createPatient(PatientRequestDto requestDto) {
        log.info("Création de patient : {}", requestDto);
        if(patientRepository.existsByEmail(requestDto.getEmail())) {
            log.error("L'email de ce patient d=existe dèja : {}", requestDto.getEmail());
            throw  new ConflictException("L'email de ce patient d=existe dèja : " + requestDto.getEmail());
        }
        Patient patient = patientMapper.toEntity(requestDto);
        patientRepository.save(patient);

        return patientMapper.toResponseDto(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDto getPatientById(Long id) {
        log.info("Recherche patient avec id : {}", id);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(()->new RessourceNotFoundException("Patient", "id", id));

        return patientMapper.toResponseDto(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponseDto> getAllPatients() {
        log.info("Recherche de tous les patients : ");
        List<Patient> patients = patientRepository.findAll();
        return patientMapper.toPatientResponseDtoList(patients);
    }

    @Override
    public PatientResponseDto updatePatient(Long id, PatientRequestDto requestDto) {
        log.info("Mis à jour du patient avec id : {}", id);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(()->new RessourceNotFoundException("Patient", "id", id));

        if(patientRepository.existsByEmail(requestDto.getEmail()) && !patient.getEmail().equals(requestDto.getEmail())) {
            log.error("L'email de ce patient avec id : {} existe dèja pour un autre patient", id);
            throw new  ConflictException("L'email de ce patient avec id : " + id);
        }

        patientMapper.updateEntityFromDto(requestDto, patient);

        return patientMapper.toResponseDto(patient);
    }

    @Override
    public void deletePatient(Long id) {
        log.info("Suppresion du patient avec l'Id : {}", id);
        if(!patientRepository.existsById(id)) {
            log.error("Le patient n'existe pas de id : {}", id);
            throw new RessourceNotFoundException("Patient", "id", id);
        }

        patientRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponseDto> searchPatients(String searchTerm) {
        log.info("Recherche de tous les patients avec les termes : {} ", searchTerm);
        List<Patient> patients = patientRepository.searchByNomOrPrenom(searchTerm);
        return patientMapper.toPatientResponseDtoList(patients);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDto getPatientByEmail(String email) {
        log.info("Recherche patient avec email : {}", email);

        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(()->new RessourceNotFoundException("Patient", "email", email));

        return patientMapper.toResponseDto(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPatients() {
        log.info("Comptage de tous les patients : {}", patientRepository.count());
        return patientRepository.count();
    }
}
