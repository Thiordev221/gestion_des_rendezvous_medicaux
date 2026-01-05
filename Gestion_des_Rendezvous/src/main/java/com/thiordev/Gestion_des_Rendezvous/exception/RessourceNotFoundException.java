package com.thiordev.Gestion_des_Rendezvous.exception;

import lombok.Getter;

@Getter
public class RessourceNotFoundException extends RuntimeException {

    private String ressourceName;
    private String fieldName;
    private Object fieldValue;

    public RessourceNotFoundException(String ressourceName, String fieldName, Object fieldValue) {
        super(String.format("%s non trouvé(e) avec %s : '%s'", ressourceName, fieldName, fieldValue));

        this.ressourceName = ressourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public RessourceNotFoundException(String message){
        super(message);
    }

}
