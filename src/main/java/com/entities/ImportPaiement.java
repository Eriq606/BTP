package com.entities;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

import com.config.Constantes;

import handyman.HandyManUtils;
import veda.godao.annotations.Column;
import veda.godao.annotations.PrimaryKey;
import veda.godao.annotations.Table;

@Table("import_paiements")
public class ImportPaiement {
    @PrimaryKey
    @Column("id")
    private Integer id;
    @Column("ligne")
    private Integer ligne;
    @Column("ref_devis")
    private String ref_devis;
    @Column("ref_paiement")
    private String ref_paiement;
    @Column("date_paiement")
    private LocalDateTime date_paiement;
    @Column("montant")
    private Double montant;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getRef_devis() {
        return ref_devis;
    }
    public void setRef_devis(String ref_devis) {
        this.ref_devis = ref_devis;
    }
    public String getRef_paiement() {
        return ref_paiement;
    }
    public void setRef_paiement(String ref_paiement) {
        this.ref_paiement = ref_paiement;
    }
    public LocalDateTime getDate_paiement() {
        return date_paiement;
    }
    public void setDate_paiement(LocalDateTime date_paiement) {
        this.date_paiement = date_paiement;
    }
    public Double getMontant() {
        return montant;
    }
    public void setMontant(Double montant) {
        this.montant = montant;
    }
    public ImportPaiement(int ligne, HashMap<String,String> map){
        setLigne(ligne);
        for(String s:Constantes.DATEFORMATS){
            try{
                LocalDate datebrut=HandyManUtils.parseDate(HandyManUtils.purgeUTF8(map.get("date_paiement")), s);
                setDate_paiement(LocalDateTime.of(datebrut, LocalTime.of(0, 0)));
                break;
            }catch (ParseException e) {
            }
        }
        setMontant(Double.parseDouble(HandyManUtils.purgeUTF8(map.get("montant")).replace(",", ".")));
        setRef_devis(HandyManUtils.purgeUTF8(map.get("ref_devis")));
        setRef_paiement(HandyManUtils.purgeUTF8(map.get("ref_paiement")));
    }
    public Integer getLigne() {
        return ligne;
    }
    public void setLigne(Integer ligne) {
        this.ligne = ligne;
    }
}
