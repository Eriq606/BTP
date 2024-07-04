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

@Table("import_devis")
public class ImportDevis {
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
    @Column("client")
    private String client;
    @Column("ref_devis")
    private String ref_devis;
    @Column("type_maison")
    private String type_maison;
    @Column("finition")
    private String finition;
    @Column("taux_finition")
    private Double taux_finition;
    public Double getTaux_finition() {
        return taux_finition;
    }
    public void setTaux_finition(Double taux_finition) {
        this.taux_finition = taux_finition;
    }
    @Column("date_devis")
    private LocalDateTime date_devis;
    @Column("date_debut")
    private LocalDateTime date_debut;
    @Column("lieu")
    private String lieu;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getClient() {
        return client;
    }
    public void setClient(String client) {
        this.client = client;
    }
    public String getRef_devis() {
        return ref_devis;
    }
    public void setRef_devis(String ref_devis) {
        this.ref_devis = ref_devis;
    }
    public String getType_maison() {
        return type_maison;
    }
    public void setType_maison(String type_maison) {
        this.type_maison = type_maison;
    }
    public String getFinition() {
        return finition;
    }
    public void setFinition(String finition) {
        this.finition = finition;
    }
    public LocalDateTime getDate_devis() {
        return date_devis;
    }
    public void setDate_devis(LocalDateTime date_devis) {
        this.date_devis = date_devis;
    }
    public LocalDateTime getDate_debut() {
        return date_debut;
    }
    public void setDate_debut(LocalDateTime date_debut) {
        this.date_debut = date_debut;
    }
    public String getLieu() {
        return lieu;
    }
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
    public ImportDevis(int ligne, HashMap<String, String> map){
        setClient(HandyManUtils.purgeUTF8(map.get("client")));
        LocalDate date_debut_brut;
        for(String s:Constantes.DATEFORMATS){
            try{
                date_debut_brut=HandyManUtils.parseDate(HandyManUtils.purgeUTF8(map.get("date_debut")), s);
                setDate_debut(LocalDateTime.of(date_debut_brut, LocalTime.of(0, 0)));
                System.out.println(HandyManUtils.parseDate(HandyManUtils.purgeUTF8(map.get("date_debut")), s));
                break;
            }catch(ParseException e){
            }
        }
        LocalDate date_devis_brut;
        for(String s:Constantes.DATEFORMATS){
            try{
                date_devis_brut=HandyManUtils.parseDate(HandyManUtils.purgeUTF8(map.get("date_devis")), s);
                setDate_devis(LocalDateTime.of(date_devis_brut, LocalTime.of(0, 0)));
                System.out.println(HandyManUtils.parseDate(HandyManUtils.purgeUTF8(map.get("date_devis")), s));
                break;
            }catch(ParseException e){
            }
        }
        setFinition(HandyManUtils.purgeUTF8(map.get("finition")));
        setLieu(HandyManUtils.purgeUTF8(map.get("lieu")));
        setLigne(ligne);
        setRef_devis(HandyManUtils.purgeUTF8(map.get("ref_devis")));
        setTaux_finition(Double.parseDouble(HandyManUtils.purgeUTF8(map.get("taux_finition")).replace(",", ".").replace("%", "")));
        setType_maison(HandyManUtils.purgeUTF8(map.get("type_maison")));
    }
}
