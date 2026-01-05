package com.thiordev.Gestion_des_Rendezvous.models;

import lombok.Getter;

@Getter
public enum Specialite {
    CARDIOLOGIE("Cardiologie"),
    DERMATOLOGIE("Dermatologie"),
    PEDIATRIE("Pédiatrie"),
    NEUROLOGIE("Neurologie"),
    OPHTALMOLOGIE("Ophtalmologie"),
    MEDECINE_GENERALE("Médecine Générale"),
    GYNECOLOGIE("Gynécologie");

    private final String libelle;

    Specialite(String libelle) {
        this.libelle = libelle;
    }

}
