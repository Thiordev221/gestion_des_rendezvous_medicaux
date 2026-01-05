package com.thiordev.Gestion_des_Rendezvous.mapper;

import com.thiordev.Gestion_des_Rendezvous.dto.request.MedecinRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.MedecinResponseDto;
import com.thiordev.Gestion_des_Rendezvous.models.Medecin;
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
public class MedecinMapperImpl implements MedecinMapper {

    @Override
    public Medecin toEntity(MedecinRequestDto medecinRequestDto) {
        if ( medecinRequestDto == null ) {
            return null;
        }

        Medecin.MedecinBuilder<?, ?> medecin = Medecin.builder();

        medecin.nom( medecinRequestDto.getNom() );
        medecin.prenom( medecinRequestDto.getPrenom() );
        medecin.email( medecinRequestDto.getEmail() );
        medecin.telephone( medecinRequestDto.getTelephone() );
        medecin.adresse( medecinRequestDto.getAdresse() );
        medecin.specialite( medecinRequestDto.getSpecialite() );

        return medecin.build();
    }

    @Override
    public void updateEntityFromDto(MedecinRequestDto medecinRequestDto, Medecin medecin) {
        if ( medecinRequestDto == null ) {
            return;
        }

        medecin.setNom( medecinRequestDto.getNom() );
        medecin.setPrenom( medecinRequestDto.getPrenom() );
        medecin.setEmail( medecinRequestDto.getEmail() );
        medecin.setTelephone( medecinRequestDto.getTelephone() );
        medecin.setAdresse( medecinRequestDto.getAdresse() );
        medecin.setSpecialite( medecinRequestDto.getSpecialite() );
    }

    @Override
    public MedecinResponseDto toResponseDto(Medecin medecin) {
        if ( medecin == null ) {
            return null;
        }

        MedecinResponseDto.MedecinResponseDtoBuilder medecinResponseDto = MedecinResponseDto.builder();

        medecinResponseDto.id( medecin.getId() );
        medecinResponseDto.nom( medecin.getNom() );
        medecinResponseDto.prenom( medecin.getPrenom() );
        medecinResponseDto.specialite( medecin.getSpecialite() );
        medecinResponseDto.email( medecin.getEmail() );
        medecinResponseDto.telephone( medecin.getTelephone() );
        medecinResponseDto.adresse( medecin.getAdresse() );
        medecinResponseDto.dateCreation( medecin.getDateCreation() );
        medecinResponseDto.dateModification( medecin.getDateModification() );

        medecinResponseDto.nombreRendezVous( medecin.getRendezVouses() != null ? medecin.getRendezVouses().size() : 0 );

        return medecinResponseDto.build();
    }

    @Override
    public List<MedecinResponseDto> toResponseDtoList(List<Medecin> medecins) {
        if ( medecins == null ) {
            return null;
        }

        List<MedecinResponseDto> list = new ArrayList<MedecinResponseDto>( medecins.size() );
        for ( Medecin medecin : medecins ) {
            list.add( toResponseDto( medecin ) );
        }

        return list;
    }
}
