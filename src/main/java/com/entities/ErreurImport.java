package com.entities;

import java.io.File;

public class ErreurImport {
    private File fichier;
    private int ligne;
    private String message;
    public File getFichier() {
        return fichier;
    }
    public void setFichier(File fichier) {
        this.fichier = fichier;
    }
    public int getLigne() {
        return ligne;
    }
    public void setLigne(int ligne) {
        this.ligne = ligne;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
}
