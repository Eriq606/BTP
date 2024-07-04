package com.entities;

import java.util.HashMap;

import handyman.HandyManUtils;
import veda.godao.annotations.Column;
import veda.godao.annotations.PrimaryKey;
import veda.godao.annotations.Table;

@Table("import_maisontravaux")
public class ImportMaisonTravaux {
    @PrimaryKey
    @Column("id")
    private Integer id;
    @Column("ligne")
    private Integer ligne;
    public Integer getLigne() {
        return ligne;
    }
    public void setLigne(Integer ligne) {
        this.ligne = ligne;
    }
    @Column("type_maison")
    private String type_maison;
    @Column("description")
    private String description;
    @Column("surface")
    private Double surface;
    @Column("codetravaux")
    private String codetravaux;
    @Column("type_travaux")
    private String type_travaux;
    @Column("unite")
    private String unite;
    @Column("prix_unitaire")
    private Double prix_unitaire;
    @Column("quantite")
    private Double quantite;
    @Column("duree_travaux")
    private Double duree_travaux;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getType_maison() {
        return type_maison;
    }
    public void setType_maison(String type_maison) {
        this.type_maison = type_maison;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getSurface() {
        return surface;
    }
    public void setSurface(Double surface) {
        this.surface = surface;
    }
    public String getCodetravaux() {
        return codetravaux;
    }
    public void setCodetravaux(String codetravaux) {
        this.codetravaux = codetravaux;
    }
    public String getType_travaux() {
        return type_travaux;
    }
    public void setType_travaux(String type_travaux) {
        this.type_travaux = type_travaux;
    }
    public String getUnite() {
        return unite;
    }
    public void setUnite(String unite) {
        this.unite = unite;
    }
    public Double getPrix_unitaire() {
        return prix_unitaire;
    }
    public void setPrix_unitaire(Double prix_unitaire) {
        this.prix_unitaire = prix_unitaire;
    }
    public Double getQuantite() {
        return quantite;
    }
    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }
    public Double getDuree_travaux() {
        return duree_travaux;
    }
    public void setDuree_travaux(Double duree_travaux) {
        this.duree_travaux = duree_travaux;
    }
    public ImportMaisonTravaux(int ligne, HashMap<String, String> map){
        setCodetravaux(HandyManUtils.purgeUTF8(map.get("code_travaux")));
        setDescription(HandyManUtils.purgeUTF8(map.get("description")));
        setDuree_travaux(Double.parseDouble(HandyManUtils.purgeUTF8(map.get("duree_travaux")).replace(",", ".")));
        setLigne(ligne);
        setPrix_unitaire(Double.parseDouble(HandyManUtils.purgeUTF8(map.get("prix_unitaire")).replace(",", ".")));
        setQuantite(Double.parseDouble(HandyManUtils.purgeUTF8(map.get("quantite")).replace(",", ".")));
        setSurface(Double.parseDouble(HandyManUtils.purgeUTF8(map.get("surface")).replace(",", ".")));
        setType_maison(HandyManUtils.purgeUTF8(map.get("type_maison")));
        setType_travaux(HandyManUtils.purgeUTF8(map.get("type_travaux")));
        setUnite(HandyManUtils.purgeUTF8(map.get("unité")));
    }
}
