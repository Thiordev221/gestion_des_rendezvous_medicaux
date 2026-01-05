package com.thiordev.Gestion_des_Rendezvous.mapper;

import com.thiordev.Gestion_des_Rendezvous.dto.request.PatientRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.PatientResponseDto;
import com.thiordev.Gestion_des_Rendezvous.models.Patient;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-03T11:53:53+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    @Override
    public Patient toEntity(PatientRequestDto patientRequestDto) {
        if ( patientRequestDto == null ) {
            return null;
        }

        Patient.PatientBuilder<?, ?> patient = Patient.builder();

        patient.nom( patientRequestDto.getNom() );
        patient.prenom( patientRequestDto.getPrenom() );
        patient.email( patientRequestDto.getEmail() );
        patient.telephone( patientRequestDto.getTelephone() );
        patient.adresse( patientRequestDto.getAdresse() );
        patient.numeroSecuriteSociale( patientRequestDto.getNumeroSecuriteSociale() );
        patient.dateNaissance( patientRequestDto.getDateNaissance() );

        return patient.build();
    }

    @Override
    public void updateEntityFromDto(PatientRequestDto patientRequestDto, Patient patient) {
        if ( patientRequestDto == null ) {
            return;
        }

        if ( patientRequestDto.getNom() != null ) {
            patient.setNom( patientRequestDto.getNom() );
        }
        if ( patientRequestDto.getPrenom() != null ) {
            patient.setPrenom( patientRequestDto.getPrenom() );
        }
        if ( patientRequestDto.getEmail() != null ) {
            patient.setEmail( patientRequestDto.getEmail() );
        }
        if ( patientRequestDto.getTelephone() != null ) {
            patient.setTelephone( patientRequestDto.getTelephone() );
        }
        if ( patientRequestDto.getAdresse() != null ) {
            patient.setAdresse( patientRequestDto.getAdresse() );
        }
        if ( patientRequestDto.getNumeroSecuriteSociale() != null ) {
            patient.setNumeroSecuriteSociale( patientRequestDto.getNumeroSecuriteSociale() );
        }
        if ( patientRequestDto.getDateNaissance() != null ) {
            patient.setDateNaissance( patientRequestDto.getDateNaissance() );
        }
    }

    @Override
    public PatientResponseDto toResponseDto(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        PatientResponseDto.PatientResponseDtoBuilder patientResponseDto = PatientResponseDto.builder();

        patientResponseDto.id( patient.getId() );
        patientResponseDto.nom( patient.getNom() );
        patientResponseDto.prenom( patient.getPrenom() );
        patientResponseDto.dateNaissance( patient.getDateNaissance() );
        patientResponseDto.email( patient.getEmail() );
        patientResponseDto.telephone( patient.getTelephone() );
        patientResponseDto.adresse( patient.getAdresse() );
        patientResponseDto.numeroSecuriteSociale( patient.getNumeroSecuriteSociale() );
        patientResponseDto.dateCreation( patient.getDateCreation() );
        patientResponseDto.dateModification( patient.getDateModification() );

        patientResponseDto.nombreRendezVous( patient.getRendezVouses() != null ? patient.getRendezVouses().size() : 0 );

        return patientResponseDto.build();
    }

    @Override
    public List<PatientResponseDto> toPatientResponseDtoList(List<Patient> patients) {
        if ( patients == null ) {
            return null;
        }

        List<PatientResponseDto> list = new ArrayList<PatientResponseDto>( patients.size() );
        for ( Patient patient : patients ) {
            list.add( toResponseDto( patient ) );
        }

        return list;
    }
}
