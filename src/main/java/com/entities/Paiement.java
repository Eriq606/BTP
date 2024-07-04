package com.entities;

import java.sql.Connection;
import java.time.LocalDateTime;

import com.auth.Client;
import com.config.Constantes;

import handyman.HandyManUtils;
import veda.godao.DAO;
import veda.godao.annotations.Column;
import veda.godao.annotations.ForeignKey;
import veda.godao.annotations.PrimaryKey;
import veda.godao.annotations.Table;

@Table("paiements")
public class Paiement {
    public static final String QUERY_IMPORT="""
            insert into paiements(idutilisateur, iddevis, dateheure, montant, ref_paiement)
            select u.id, devis.id, ip.date_paiement, ip.montant, ip.ref_paiement
            from import_paiements ip
            join devis on ip.ref_devis=devis.idmatricule
            join utilisateurs u on devis.idclient=u.id
            """;
    @PrimaryKey
    @Column("id")
    private Integer id;
    @Column("idutilisateur")
    @ForeignKey(recursive = false)
    private Client client;
    @Column("iddevis")
    @ForeignKey(recursive = false)
    private Devis devis;
    @Column("dateheure")
    private LocalDateTime dateheure;
    @Column("montant")
    private Double montant;
    @Column("ref_paiement")
    private String refpaiement;
    public String getRefpaiement() {
        return refpaiement;
    }
    public void setRefpaiement(String refpaiement) {
        this.refpaiement = refpaiement;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public Devis getDevis() {
        return devis;
    }
    public void setDevis(Devis devis) {
        this.devis = devis;
    }
    public LocalDateTime getDateheure() {
        return dateheure;
    }
    public void setDateheure(LocalDateTime dateheure) {
        this.dateheure = dateheure;
    }
    public Double getMontant() {
        return montant;
    }
    public void setMontant(Double montant) {
        this.montant = montant;
    }
    public String getMontantString(){
        return HandyManUtils.number_format(getMontant(), " ", ",")+" "+Constantes.ARIARY;
    }
    public Paiement(String iddevis, Client client, String datepaiement, String montant) {
        Devis devis=new Devis();
        devis.setId(Integer.parseInt(iddevis));
        setDevis(devis);
        setClient(client);
        LocalDateTime datepaie=LocalDateTime.parse(datepaiement);
        setDateheure(datepaie);
        setMontant(Double.parseDouble(montant));
    }
    
    public Paiement() {
    }
    public Paiement(Devis devis, LocalDateTime dateheure, Double montant) {
        this.devis = devis;
        setClient(devis.getClient());
        this.dateheure = dateheure;
        this.montant = montant;
    }
    public void enregistrer(Connection connect, DAO dao) throws Exception{
        try{
            Devis devisOriginal=dao.select(connect, Devis.class, getDevis())[0];
            devisOriginal.setResteAPayer(devisOriginal.getResteAPayer()-getMontant());
            dao.update(connect, devisOriginal, getDevis());
            int lastID=dao.insertWithoutPrimaryKey(connect, this);
            String padded=Constantes.PAIEMENT_PREFIX+HandyManUtils.padNumber(lastID, Constantes.PAIEMENT_PADDING);
            Paiement where=new Paiement();
            where.setId(lastID);
            setRefpaiement(padded);
            String query="update paiements set ref_paiement='%s' where id=%s";
            query=String.format(query, getRefpaiement(), lastID);
            dao.execute(connect, query);
        }catch(Exception e){
            connect.rollback();
            throw e;
        }
    }
    public String getHTML(){
        String html="<li>";
        html=html+getRefpaiement()+" : "+getMontantString()+" ,le "+getDateheure().toString().replace("T", " ")+"</li>";
        return html;
    }
}
