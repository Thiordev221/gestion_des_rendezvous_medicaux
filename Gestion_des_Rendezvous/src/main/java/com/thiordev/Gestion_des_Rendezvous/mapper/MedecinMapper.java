package com.thiordev.Gestion_des_Rendezvous.mapper;

import com.thiordev.Gestion_des_Rendezvous.dto.request.MedecinRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.MedecinResponseDto;
import com.thiordev.Gestion_des_Rendezvous.models.Medecin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedecinMapper {

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "dateCreation",  ignore = true)
    @Mapping(target = "dateModification",  ignore = true)
    @Mapping(target = "rendezVouses",  ignore = true)
    Medecin toEntity(MedecinRequestDto medecinRequestDto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification",  ignore = true)
    @Mapping(target = "rendezVouses", ignore = true)
    void updateEntityFromDto(MedecinRequestDto medecinRequestDto,
                             @MappingTarget Medecin medecin);

    @Mapping(target = "nombreRendezVous", expression = "java(medecin.getRendezVouses() != null ? medecin.getRendezVouses().size() : 0)")
    MedecinResponseDto  toResponseDto(Medecin medecin);

    List<MedecinResponseDto>  toResponseDtoList(List<Medecin> medecins);
}
