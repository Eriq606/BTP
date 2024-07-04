package com.entities;

import veda.godao.annotations.Column;
import veda.godao.annotations.PrimaryKey;
import veda.godao.annotations.Table;

@Table("type_finitions")
public class Finition {
    public static final String QUERY_IMPORT="insert into type_finitions(nom, pourcentage) select finition, taux_finition from import_devis group by finition, taux_finition";
    @PrimaryKey
    @Column("id")
    private Integer id;
    @Column("nom")
    private String nom;
    @Column("pourcentage")
    private Double pourcentage;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Double getPourcentage() {
        return pourcentage;
    }
    public void setPourcentage(Double pourcentage) {
        this.pourcentage = pourcentage;
    }
    public String getPourcentageString(){
        return getNom()+" ("+getPourcentage()+" %)";
    }
}
