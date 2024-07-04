package com.entities;

import java.util.HashMap;

public class StatsDevisMois {
    private int mois;
    private double montantTotal;
    private String moisString;
    public String getMoisString() {
        return moisString;
    }
    public void setMoisString(String moisString) {
        this.moisString = moisString;
    }
    public int getMois() {
        return mois;
    }
    public void setMois(int mois) {
        this.mois = mois;
    }
    public double getMontantTotal() {
        return montantTotal;
    }
    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }
    public StatsDevisMois(HashMap<String, Object> map) {
        Double mois=Double.parseDouble(map.get("moisnumero").toString());
        setMois(mois.intValue());
        setMontantTotal(Double.parseDouble(map.get("montant_total").toString()));
        setMoisString(map.get("moisstring").toString());
    }   
    public static StatsDevisMois[] getStatsMois(HashMap<String, Object>[] liste){
        StatsDevisMois[] stats=new StatsDevisMois[liste.length];
        for(int i=0;i<stats.length;i++){
            stats[i]=new StatsDevisMois(liste[i]);
        }
        return stats;
    }
}
