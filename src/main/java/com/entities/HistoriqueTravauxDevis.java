package com.entities;

import com.config.Constantes;

import handyman.HandyManUtils;
import veda.godao.annotations.Column;
import veda.godao.annotations.ForeignKey;
import veda.godao.annotations.PrimaryKey;
import veda.godao.annotations.Table;

@Table("historique_travaux_devis")
public class HistoriqueTravauxDevis {
    public static final String QUERY_IMPORT="""
            insert into historique_travaux_devis(iddevis, numerotravaux, nomtravaux, unite, quantitetravaux, pu, montant)
            select devis.id, t.numero, t.designation, u.symbole, ttm.quantitetravaux, t.prix_unitaire, t.prix_unitaire*ttm.quantitetravaux
            from devis
            join type_maisons tm on devis.idtypemaison=tm.id
            join travaux_typemaisons ttm on tm.id=ttm.idtypemaison
            join travaux t on ttm.idtravaux=t.id
            join unites u on t.idunite=u.id
            """;
    @PrimaryKey
    @Column("id")
    private Integer id;
    @ForeignKey(recursive = false)
    @Column("iddevis")
    private Devis devis;
    @Column("numerotravaux")
    private String numeroTravaux;
    @Column("nomtravaux")
    private String nomTravaux;
    @Column("unite")
    private String unite;
    @Column("quantitetravaux")
    private Double quantiteTravaux;
    @Column("pu")
    private Double prixUnitaire;
    @Column("montant")
    private Double montant;
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
    public String getNumeroTravaux() {
        return numeroTravaux;
    }
    public void setNumeroTravaux(String numeroTravaux) {
        this.numeroTravaux = numeroTravaux;
    }
    public String getNomTravaux() {
        return nomTravaux;
    }
    public void setNomTravaux(String nomTravaux) {
        this.nomTravaux = nomTravaux;
    }
    public String getUnite() {
        return unite;
    }
    public void setUnite(String unite) {
        this.unite = unite;
    }
    public Double getQuantiteTravaux() {
        return quantiteTravaux;
    }
    public void setQuantiteTravaux(Double quantiteTravaux) {
        this.quantiteTravaux = quantiteTravaux;
    }
    public Double getPrixUnitaire() {
        return prixUnitaire;
    }
    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
    public Double getMontant() {
        return montant;
    }
    public void setMontant(Double montant) {
        this.montant = montant;
    }
    
    public HistoriqueTravauxDevis() {
    }
    public HistoriqueTravauxDevis(Devis devis, Travaux travaux) {
        this.devis = devis;
        setMontant(travaux.getPrixUnitaire()*travaux.getQuantite());
        setNomTravaux(travaux.getDesignation());
        setNumeroTravaux(travaux.getNumero());
        setPrixUnitaire(travaux.getPrixUnitaire());
        setQuantiteTravaux(travaux.getQuantite());
        setUnite(travaux.getUnite());
    }
    public String getQuantiteString(){
        return HandyManUtils.number_format(getQuantiteTravaux(), " ", ",");
    }
    public String getPrixUnitaireString(){
        return HandyManUtils.number_format(getPrixUnitaire(), " ", ",")+" "+Constantes.ARIARY;
    }
    public String getMontantString(){
        return HandyManUtils.number_format(getMontant(), " ", ",")+" "+Constantes.ARIARY;
    }
    public String getHTML(){
        String html="""
        <tr class="details-travaux">
            <td style="padding: 10px; border: 1px solid;">%s</td>
            <td style="padding: 10px; border: 1px solid;">%s</td>
            <td style="padding: 10px; border: 1px solid;">%s</td>
            <td style="padding: 10px; border: 1px solid;text-align: center;">%s</td>
            <td style="padding: 10px; border: 1px solid;text-align: center;">%s</td>
            <td style="padding: 10px; border: 1px solid;text-align: center;">%s</td>
        </tr>
        """;
        html=String.format(html, getNumeroTravaux(), getNomTravaux(), getUnite(), getQuantiteString(), getPrixUnitaireString(), getMontantString());
        return html;
    }
}
