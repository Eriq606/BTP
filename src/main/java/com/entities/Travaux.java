package com.entities;

import veda.godao.annotations.Column;
import veda.godao.annotations.ForeignKey;
import veda.godao.annotations.PrimaryKey;
import veda.godao.annotations.Table;

@Table("v_travaux_typemaisons")
public class Travaux {
    public static final String QUERY_IMPORT_UNITE="insert into unites(symbole) select unite from import_maisontravaux group by unite";
    public static final String QUERY_IMPORT="insert into travaux(designation, idunite, prix_unitaire, numero) select im.type_travaux, u.id as idunite, im.prix_unitaire, im.codetravaux from import_maisontravaux im join unites u on im.unite=u.symbole group by type_travaux, idunite, prix_unitaire, codetravaux";
    public static final String QUERY_IMPORT_TYPEMAISON_TRAVAUX="insert into travaux_typemaisons(idtypemaison, idtravaux, quantitetravaux) select tm.id as idtypemaison, t.id as idtravaux, im.quantite from import_maisontravaux im join travaux t on im.type_travaux=t.designation join type_maisons tm on im.type_maison=tm.nom";
    @PrimaryKey
    @Column("idtravaux")
    private Integer id;
    @Column("designation")
    private String designation;
    @ForeignKey(recursive = true)
    @Column("idtypemaison")
    private Maison maison;
    @Column("prix_unitaire")
    private Double prixUnitaire;
    @Column("unite")
    private String unite;
    @Column("numero")
    private String numero;
    @Column("quantitetravaux")
    private Double quantite;
    public Double getQuantite() {
        return quantite;
    }
    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public Maison getMaison() {
        return maison;
    }
    public void setMaison(Maison maison) {
        this.maison = maison;
    }
    public Double getPrixUnitaire() {
        return prixUnitaire;
    }
    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
    public String getUnite() {
        return unite;
    }
    public void setUnite(String unite) {
        this.unite = unite;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
}
