package com.thiordev.Gestion_des_Rendezvous.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = "rendezVouses")
@ToString(exclude = "rendezVouses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Patient  extends Personne{

    @Column(length = 15)
    private String numeroSecuriteSociale;

    @Column(nullable = false)
    private LocalDate dateNaissance;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "patient"
    )
    @Builder.Default
    private List<RendezVous> rendezVouses = new ArrayList<>();


}
