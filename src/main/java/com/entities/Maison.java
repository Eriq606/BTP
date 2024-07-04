package com.entities;

import handyman.HandyManUtils;
import veda.godao.annotations.Column;
import veda.godao.annotations.PrimaryKey;
import veda.godao.annotations.Table;

@Table("v_type_maison_prix")
public class Maison {
    public static final String QUERY_IMPORT="insert into type_maisons(nom, description, surface, dureejours) select type_maison, description, surface, duree_travaux from import_maisontravaux group by type_maison, description, surface, duree_travaux";
    @Column("id")
    @PrimaryKey
    private Integer id;
    @Column("nom")
    private String nom;
    @Column("dureejours")
    private Double dureeJours;
    @Column("prix")
    private Double prix;
    public Double getPrix() {
        return prix;
    }
    public void setPrix(Double prix) {
        this.prix = prix;
    }
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
    public Double getDureeJours() {
        return dureeJours;
    }
    public void setDureeJours(Double dureeJours) {
        this.dureeJours = dureeJours;
    }
    public String getDureeString(){
        return getDureeJours()+" jours";
    }
    public String getPrixString(){
        String retour=HandyManUtils.number_format(getPrix(), " ", ",");
        return retour;
    }
}
