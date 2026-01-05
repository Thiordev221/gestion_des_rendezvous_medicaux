package com.thiordev.Gestion_des_Rendezvous.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString(exclude = "rendezVouses")
@EqualsAndHashCode(callSuper = true, exclude = "rendezVouses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Medecin extends Personne{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialite specialite;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "medecin"
    )
    @Builder.Default
    List<RendezVous> rendezVouses =  new ArrayList<>();

}