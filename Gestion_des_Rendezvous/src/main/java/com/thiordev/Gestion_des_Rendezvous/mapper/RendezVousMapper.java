package com.thiordev.Gestion_des_Rendezvous.mapper;

import com.thiordev.Gestion_des_Rendezvous.dto.request.RendezVousRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.RendezVousResponseDto;
import com.thiordev.Gestion_des_Rendezvous.models.Medecin;
import com.thiordev.Gestion_des_Rendezvous.models.Patient;
import com.thiordev.Gestion_des_Rendezvous.models.RendezVous;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RendezVousMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "medecin", ignore = true)
    @Mapping(target = "patient", ignore = true)
    RendezVous toEntity(RendezVousRequestDto rendezVousRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "medecin", ignore = true)
    @Mapping(target = "patient",  ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(RendezVousRequestDto rendezVousRequestDto,
                             @MappingTarget RendezVous rendezVous);

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.prenom", target = "patientPrenom")
    @Mapping(source = "patient.nom", target = "patientNom")
    @Mapping(source = "medecin.id", target = "medecinId")
    @Mapping(source = "medecin.prenom", target = "medecinPrenom")
    @Mapping(source = "medecin.nom", target = "medecinNom")
    @Mapping(target = "medecinSpecialite", expression = "java(rendezVous.getMedecin().getSpecialite().getLibelle())")
    RendezVousResponseDto toResponseDto(RendezVous rendezVous);

    List<RendezVousResponseDto> toResponseDtoList(List<RendezVous> rendezVousList);

    default RendezVous toEntityWithRelations(RendezVousRequestDto requestDto, Patient patient, Medecin medecin) {
        RendezVous rendezVous = toEntity(requestDto);
        rendezVous.setPatient(patient);
        rendezVous.setMedecin(medecin);
        return rendezVous;
    }

}
