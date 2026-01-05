package com.thiordev.Gestion_des_Rendezvous.controller;

import com.thiordev.Gestion_des_Rendezvous.dto.request.PatientRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.PatientResponseDto;
import com.thiordev.Gestion_des_Rendezvous.service.PatientServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/patients")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientServiceImpl patientService;

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@RequestBody @Valid PatientRequestDto patientRequestDto) {
        log.info("API : Création de patient avec id ");
        PatientResponseDto response = patientService.createPatient(patientRequestDto);
        return new  ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> putPatient(
            @PathVariable("id") Long id,
            @RequestBody @Valid PatientRequestDto patientRequestDto
    ){
        PatientResponseDto  response = patientService.updatePatient(id, patientRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id){
        log.info("API : Suppression du patient avec id {}", id);
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatient(@PathVariable("id") Long id){
        log.info("API : Recherche du patient avec id {}", id);
        PatientResponseDto response = patientService.getPatientById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients(){
        log.info("API : Recherche de tous les patients");
        List<PatientResponseDto> response = patientService.getAllPatients();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PatientResponseDto> getPatientByEmail(@PathVariable("email") String email){
        log.info("API : Recherche du patient avec email {}", email);
        PatientResponseDto response = patientService.getPatientByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientResponseDto>> searchPatients(@RequestParam("email") String searchTerm){
        log.info("API : Recherche du patient avec {}", searchTerm);
        List<PatientResponseDto> response = patientService.searchPatients(searchTerm);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countPatients(){
        log.info("API : Comptage de tous les patients");
        Long count = patientService.countPatients();
        return ResponseEntity.ok(count);
    }


}
