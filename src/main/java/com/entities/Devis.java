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

@Table("devis")
public class Devis {
    public static final String QUERY_IMPORT="""
            insert into devis(idtypemaison, idtypefinition, datecreation, pourcentagefinition, idmatricule, datedebuttravaux, datefintravaux, idclient, lieu)
            select tm.id, tf.id, i_d.date_devis, i_d.taux_finition, i_d.ref_devis, i_d.date_debut, i_d.date_debut + (tm.dureejours||' days')::interval, u.id, i_d.lieu
            from import_devis i_d
            join type_maisons tm on i_d.type_maison=tm.nom
            join type_finitions tf on i_d.finition=tf.nom
            join utilisateurs u on i_d.client=u.telephone
            """;
    public static final String QUERY_UPDATE_MONTANT1="""
            update devis
            set montant=(select prix from v_type_maison_prix where id=idtypemaison)
            """;
    public static final String QUERY_UPDATE_MONTANT2="""
            update devis
            set montant=montant+(montant*pourcentagefinition/100)
            """;
    public static final String QUERY_UPDATE_RESTEAPAYER="""
            update devis
            set reste_a_payer=montant
            """;
    public static final String QUERY_UPDATE_RESTEAPAYER_PAIEMENT="""
        update devis
        set reste_a_payer=montant-(select somme from v_devis_somme_paye where iddevis=id)
        where id in (select iddevis from v_devis_somme_paye)
    """;
    @Column("id")
    @PrimaryKey
    private Integer id;
    @Column("idtypemaison")
    @ForeignKey(recursive = true)
    private Maison maison;
    @Column("idtypefinition")
    @ForeignKey(recursive = true)
    private Finition finition;
    @Column("idmatricule")
    private String idmatricule;
    @Column("montant")
    private Double montant;
    @Column("datecreation")
    private LocalDateTime datecreation;
    @Column("datedebuttravaux")
    private LocalDateTime dateDebutTravaux;
    @Column("datefintravaux")
    private LocalDateTime dateFinTravaux;
    @Column("pourcentagefinition")
    private Double pourcentage;
    @Column("idclient")
    @ForeignKey(recursive = true)
    private Client client;
    @Column("reste_a_payer")
    private Double resteAPayer;
    @Column("lieu")
    private String lieu;
    public String getLieu() {
        return lieu;
    }
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
    public Double getResteAPayer() {
        return resteAPayer;
    }
    public void setResteAPayer(Double resteAPayer) {
        this.resteAPayer = resteAPayer;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Maison getMaison() {
        return maison;
    }
    public void setMaison(Maison maison) {
        this.maison = maison;
    }
    public Finition getFinition() {
        return finition;
    }
    public void setFinition(Finition finition) {
        this.finition = finition;
    }
    public String getIdmatricule() {
        return idmatricule;
    }
    public void setIdmatricule(String idmatricule) {
        this.idmatricule = idmatricule;
    }
    public Double getMontant() {
        return montant;
    }
    public void setMontant(Double montant) {
        this.montant = montant;
    }
    public LocalDateTime getDatecreation() {
        return datecreation;
    }
    public void setDatecreation(LocalDateTime datecreation) {
        this.datecreation = datecreation;
    }
    public LocalDateTime getDateDebutTravaux() {
        return dateDebutTravaux;
    }
    public void setDateDebutTravaux(LocalDateTime dateDebutTravaux) {
        this.dateDebutTravaux = dateDebutTravaux;
    }
    public LocalDateTime getDateFinTravaux() {
        return dateFinTravaux;
    }
    public void setDateFinTravaux(LocalDateTime dateFinTravaux) {
        this.dateFinTravaux = dateFinTravaux;
    }
    public Double getPourcentage() {
        return pourcentage;
    }
    public void setPourcentage(Double pourcentage) {
        this.pourcentage = pourcentage;
    }
    
    public Devis() {
    }
    public Devis(String typemaison, String typefinition, String datedebut, LocalDateTime datecreation, Client client) {
        setClient(client);
        Maison maison=new Maison();
        maison.setId(Integer.parseInt(typemaison));
        setMaison(maison);
        Finition finition=new Finition();
        finition.setId(Integer.parseInt(typefinition));
        setFinition(finition);
        setDatecreation(datecreation);
        setDateDebutTravaux(LocalDateTime.parse(datedebut));
        setIdmatricule("");
    }
    public void enregistrer(Connection connect, DAO dao) throws Exception{
        Maison maison=dao.select(connect, Maison.class, getMaison())[0];
        LocalDateTime datefin=HandyManUtils.plus(getDateDebutTravaux(), maison.getDureeJours());
        setDateFinTravaux(datefin);
        Finition finition=dao.select(connect, Finition.class, getFinition())[0];
        setPourcentage(finition.getPourcentage());
        double vraiMontant=maison.getPrix();
        vraiMontant=vraiMontant+((vraiMontant*finition.getPourcentage())/100);
        setMontant(vraiMontant);
        setResteAPayer(getMontant());
        try{
            int lastId=dao.insertWithoutPrimaryKey(connect, this);
            String idmatricule=HandyManUtils.padNumber(lastId, Constantes.DEVIS_PADDING);
            idmatricule=Constantes.DEVIS_PREFIXE+idmatricule;
            Devis where=new Devis();
            where.setId(lastId);
            setIdmatricule(idmatricule);
            dao.update(connect, this, where);
            setId(lastId);

            HistoriqueDetailsDevis details=new HistoriqueDetailsDevis(this, maison, finition);
            dao.insertWithoutPrimaryKey(connect, details);
            Travaux where_travaux=new Travaux();
            where_travaux.setMaison(maison);
            Travaux[] travaux=dao.select(connect, Travaux.class, where_travaux);
            HistoriqueTravauxDevis historique;
            for(Travaux t:travaux){
                historique=new HistoriqueTravauxDevis(this, t);
                dao.insertWithoutPrimaryKey(connect, historique);
            }
        }catch(Exception e){
            connect.rollback();
            throw e;
        }
    }
    public String getDateDebutString(){
        return getDateDebutTravaux().toLocalDate().toString();
    }
    public String getDateFinString(){
        return getDateFinTravaux().toLocalDate().toString();
    }
    public String getTermine(){
        return getDateFinTravaux().isBefore(LocalDateTime.now())?"oui":"non";
    }
    public String getPourcentString(){
        return getPourcentage()+" %";
    }
    public String getResteAPayerString(){
        return HandyManUtils.number_format(getResteAPayer(), " ", ",")+" "+Constantes.ARIARY;
    }
    public String getMontantString(){
        return HandyManUtils.number_format(getMontant(), " ", ",")+" "+Constantes.ARIARY;
    }
    public double getMontantPaye(){
        return getMontant()-getResteAPayer();
    }
    public String getMontantPayeString(){
        double paye=getMontantPaye();
        return HandyManUtils.number_format(paye, " ", ",")+" "+Constantes.ARIARY;
    }
    public double getPourcentPaye(){
        return getMontantPaye()*100/getMontant();
    }
    public String getPourcentPayeString(){
        double pourc=getPourcentPaye();
        return pourc+" "+"%";
    }
    public String getCouleur(){
        double pourcent=getPourcentPaye();
        if(pourcent==50){
            return "";
        }
        return getPourcentPaye()>50?"chartreuse":"red";
    }
    public String getCouleurPolicePourcent(){
        double pourcent=getPourcentPaye();
        if(pourcent==50){
            return "";
        }
        return "black";
    }
}
