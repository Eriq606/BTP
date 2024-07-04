package com.entities;

import java.sql.Connection;

import veda.godao.DAO;
import veda.godao.annotations.Column;
import veda.godao.annotations.ForeignKey;
import veda.godao.annotations.PrimaryKey;
import veda.godao.annotations.Table;

@Table("travaux")
public class TypeTravaux {
    @PrimaryKey
    @Column("id")
    private Integer id;
    @Column("designation")
    private String designation;
    @ForeignKey(recursive = true)
    @Column("idunite")
    private Unite unite;
    @Column("prix_unitaire")
    private Double prixunitaire;
    @Column("numero")
    private String numero;
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
    public Unite getUnite() {
        return unite;
    }
    public void setUnite(Unite unite) {
        this.unite = unite;
    }
    public Double getPrixunitaire() {
        return prixunitaire;
    }
    public void setPrixunitaire(Double prixunitaire) {
        this.prixunitaire = prixunitaire;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public TypeTravaux() {
    }
    public TypeTravaux(String id, String designation, String unite, String prixunitaire, String numero) {
        setId(Integer.parseInt(id));
        this.designation = designation;
        Unite uniteObj=new Unite();
        uniteObj.setId(Integer.parseInt(unite));
        setUnite(uniteObj);
        this.prixunitaire = Double.parseDouble(prixunitaire);
        this.numero = numero;
    }
    public void enregistrer(Connection connect, DAO dao) throws Exception{
        try{
            TypeTravaux where=new TypeTravaux();
            where.setId(getId());
            dao.update(connect, this, where);
        }catch(Exception e){
            connect.rollback();
            throw e;
        }
    }
}
