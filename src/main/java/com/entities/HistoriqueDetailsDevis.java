package com.entities;

import java.sql.Connection;
import java.util.HashMap;

import handyman.HandyManUtils;
import veda.godao.DAO;
import veda.godao.annotations.Column;
import veda.godao.annotations.ForeignKey;
import veda.godao.annotations.PrimaryKey;
import veda.godao.annotations.Table;

@Table("historique_details_devis")
public class HistoriqueDetailsDevis {
    public static final String QUERY_IMPORT="""
            insert into historique_details_devis(iddevis, nom_typemaison, nom_typefinition, dureejours, numero_client)
            select devis.id, tm.nom, tf.nom, tm.dureejours, u.telephone
            from devis
            join type_maisons tm on devis.idtypemaison=tm.id
            join type_finitions tf on devis.idtypefinition=tf.id
            join utilisateurs u on devis.idclient=u.id;
            """;
    @PrimaryKey
    @Column("id")
    private Integer id;
    @ForeignKey(recursive = true)
    @Column("iddevis")
    private Devis devis;
    @Column("nom_typemaison")
    private String nomMaison;
    @Column("nom_typefinition")
    private String nomFinition;
    @Column("dureejours")
    private Double dureeJours;
    @Column("numero_client")
    private String numeroClient;
    public String getNumeroClient() {
        return numeroClient;
    }
    public void setNumeroClient(String numeroClient) {
        this.numeroClient = numeroClient;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Devis getDevis() {
        return devis;
    }
    public void setDevis(Devis devis) {
        this.devis = devis;
    }
    public String getNomMaison() {
        return nomMaison;
    }
    public void setNomMaison(String nomMaison) {
        this.nomMaison = nomMaison;
    }
    public String getNomFinition() {
        return nomFinition;
    }
    public void setNomFinition(String nomFinition) {
        this.nomFinition = nomFinition;
    }
    public Double getDureeJours() {
        return dureeJours;
    }
    public void setDureeJours(Double dureeJours) {
        this.dureeJours = dureeJours;
    }
    
    public HistoriqueDetailsDevis() {
    }
    public HistoriqueDetailsDevis(Devis devis, Maison maison, Finition finition) {
        this.devis = devis;
        setNumeroClient(devis.getClient().getPhone());
        setDureeJours(maison.getDureeJours());
        setNomFinition(finition.getNom());
        setNomMaison(maison.getNom());
    }
    public String getHTML(){
        String html="""
        <h2>%s</h2>
        <h3>%s</h3>
        <p>Debut des travaux: %s</p>
        <p>Fin des travaux: %s</p>
        <p>Duree: %s jours</p>
        <hr/>
        <p>Type de maison: %s</p>
        <p>Type de finition: %s</p>
        <p>Hausse du prix par rapport au type de finition: %s</p>
        """;
        String nombreJours=HandyManUtils.number_format(getDureeJours(), " ", ",");
        html=String.format(html, getDevis().getIdmatricule(), getNumeroClient(), getDevis().getDateDebutString(), getDevis().getDateFinString(), nombreJours, getNomMaison(), getNomFinition(), getDevis().getPourcentString());
        return html;
    }
    public static HistoriqueDetailsDevis[] getDetailsFromDevis(Connection connect, DAO dao, String listeIdDevis) throws Exception{
        String query="select * from historique_details_devis where iddevis in "+listeIdDevis+" order by iddevis";
        HashMap<String, Object>[] liste=dao.select(connect, query);
        HistoriqueDetailsDevis[] details=new HistoriqueDetailsDevis[liste.length];
        for(int i=0;i<details.length;i++){
            details[i]=new HistoriqueDetailsDevis();
            details[i].setId((Integer)liste[i].get("id"));
            details[i].setNomFinition((String)liste[i].get("nom_typefinition"));
            details[i].setNomMaison((String)liste[i].get("nom_typemaison"));
            details[i].setDureeJours(Double.parseDouble(liste[i].get("dureejours").toString()));
            details[i].setNumeroClient((String)liste[i].get("numero_client"));
        }
        return details;
    }
}
