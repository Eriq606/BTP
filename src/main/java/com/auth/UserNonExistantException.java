package com.auth;

public class UserNonExistantException extends Exception{
    private String identifiant;
    

    public UserNonExistantException(String identifiant) {
        this.identifiant = identifiant;
    }

    @Override
    public String getMessage() {
        String message="Erreur d'identifiant: "+getIdentifiant();
        return message;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }
    
}
