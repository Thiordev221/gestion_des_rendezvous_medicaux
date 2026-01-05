package com.thiordev.Gestion_des_Rendezvous.mapper;

import com.thiordev.Gestion_des_Rendezvous.dto.request.PatientRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.PatientResponseDto;
import com.thiordev.Gestion_des_Rendezvous.models.Patient;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "rendezVouses", ignore = true)
    Patient toEntity(PatientRequestDto patientRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "rendezVouses", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void  updateEntityFromDto(PatientRequestDto patientRequestDto,
                              @MappingTarget Patient patient);

    @Mapping(target = "nombreRendezVous", expression = "java(patient.getRendezVouses() != null ? patient.getRendezVouses().size() : 0)")
    PatientResponseDto toResponseDto(Patient patient);


    List<PatientResponseDto>  toPatientResponseDtoList(List<Patient> patients);
}
