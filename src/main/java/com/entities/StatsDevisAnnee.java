package com.entities;

import java.util.HashMap;

public class StatsDevisAnnee {
    private int annee;
    public int getAnnee() {
        return annee;
    }
    public void setAnnee(int annee) {
        this.annee = annee;
    }
    private double montantTotal;
    public double getMontantTotal() {
        return montantTotal;
    }
    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }
    public StatsDevisAnnee(HashMap<String, Object> map) {
        Double annee=Double.parseDouble(map.get("annee").toString());
        setAnnee(annee.intValue());
        setMontantTotal(Double.parseDouble(map.get("montant_total").toString()));
    }   
    public static StatsDevisAnnee[] getStatsMois(HashMap<String, Object>[] liste){
        StatsDevisAnnee[] stats=new StatsDevisAnnee[liste.length];
        for(int i=0;i<stats.length;i++){
            stats[i]=new StatsDevisAnnee(liste[i]);
        }
        return stats;
    }
}
