package com.thiordev.Gestion_des_Rendezvous.mapper;

import com.thiordev.Gestion_des_Rendezvous.dto.request.RendezVousRequestDto;
import com.thiordev.Gestion_des_Rendezvous.dto.response.RendezVousResponseDto;
import com.thiordev.Gestion_des_Rendezvous.models.Medecin;
import com.thiordev.Gestion_des_Rendezvous.models.Patient;
import com.thiordev.Gestion_des_Rendezvous.models.RendezVous;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-05T15:43:54+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class RendezVousMapperImpl implements RendezVousMapper {

    @Override
    public RendezVous toEntity(RendezVousRequestDto rendezVousRequestDto) {
        if ( rendezVousRequestDto == null ) {
            return null;
        }

        RendezVous.RendezVousBuilder rendezVous = RendezVous.builder();

        rendezVous.dateHeureDebut( rendezVousRequestDto.getDateHeureDebut() );
        rendezVous.dateHeureFin( rendezVousRequestDto.getDateHeureFin() );
        rendezVous.motifConsultation( rendezVousRequestDto.getMotifConsultation() );

        return rendezVous.build();
    }

    @Override
    public void updateEntityFromDto(RendezVousRequestDto rendezVousRequestDto, RendezVous rendezVous) {
        if ( rendezVousRequestDto == null ) {
            return;
        }

        if ( rendezVousRequestDto.getDateHeureDebut() != null ) {
            rendezVous.setDateHeureDebut( rendezVousRequestDto.getDateHeureDebut() );
        }
        if ( rendezVousRequestDto.getDateHeureFin() != null ) {
            rendezVous.setDateHeureFin( rendezVousRequestDto.getDateHeureFin() );
        }
        if ( rendezVousRequestDto.getMotifConsultation() != null ) {
            rendezVous.setMotifConsultation( rendezVousRequestDto.getMotifConsultation() );
        }
    }

    @Override
    public RendezVousResponseDto toResponseDto(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }

        RendezVousResponseDto.RendezVousResponseDtoBuilder rendezVousResponseDto = RendezVousResponseDto.builder();

        rendezVousResponseDto.patientId( rendezVousPatientId( rendezVous ) );
        rendezVousResponseDto.patientPrenom( rendezVousPatientPrenom( rendezVous ) );
        rendezVousResponseDto.patientNom( rendezVousPatientNom( rendezVous ) );
        rendezVousResponseDto.medecinId( rendezVousMedecinId( rendezVous ) );
        rendezVousResponseDto.medecinPrenom( rendezVousMedecinPrenom( rendezVous ) );
        rendezVousResponseDto.medecinNom( rendezVousMedecinNom( rendezVous ) );
        rendezVousResponseDto.id( rendezVous.getId() );
        rendezVousResponseDto.dateHeureDebut( rendezVous.getDateHeureDebut() );
        rendezVousResponseDto.dateHeureFin( rendezVous.getDateHeureFin() );
        rendezVousResponseDto.motifConsultation( rendezVous.getMotifConsultation() );
        rendezVousResponseDto.statut( rendezVous.getStatut() );
        rendezVousResponseDto.dateCreation( rendezVous.getDateCreation() );
        rendezVousResponseDto.dateModification( rendezVous.getDateModification() );

        rendezVousResponseDto.medecinSpecialite( rendezVous.getMedecin().getSpecialite().getLibelle() );

        return rendezVousResponseDto.build();
    }

    @Override
    public List<RendezVousResponseDto> toResponseDtoList(List<RendezVous> rendezVousList) {
        if ( rendezVousList == null ) {
            return null;
        }

        List<RendezVousResponseDto> list = new ArrayList<RendezVousResponseDto>( rendezVousList.size() );
        for ( RendezVous rendezVous : rendezVousList ) {
            list.add( toResponseDto( rendezVous ) );
        }

        return list;
    }

    private Long rendezVousPatientId(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }
        Patient patient = rendezVous.getPatient();
        if ( patient == null ) {
            return null;
        }
        Long id = patient.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String rendezVousPatientPrenom(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }
        Patient patient = rendezVous.getPatient();
        if ( patient == null ) {
            return null;
        }
        String prenom = patient.getPrenom();
        if ( prenom == null ) {
            return null;
        }
        return prenom;
    }

    private String rendezVousPatientNom(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }
        Patient patient = rendezVous.getPatient();
        if ( patient == null ) {
            return null;
        }
        String nom = patient.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }

    private Long rendezVousMedecinId(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }
        Medecin medecin = rendezVous.getMedecin();
        if ( medecin == null ) {
            return null;
        }
        Long id = medecin.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String rendezVousMedecinPrenom(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }
        Medecin medecin = rendezVous.getMedecin();
        if ( medecin == null ) {
            return null;
        }
        String prenom = medecin.getPrenom();
        if ( prenom == null ) {
            return null;
        }
        return prenom;
    }

    private String rendezVousMedecinNom(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }
        Medecin medecin = rendezVous.getMedecin();
        if ( medecin == null ) {
            return null;
        }
        String nom = medecin.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }
}
