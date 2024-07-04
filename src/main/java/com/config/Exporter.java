package com.config;

import java.io.File;
import java.sql.Connection;

import com.entities.Devis;
import com.entities.HistoriqueDetailsDevis;
import com.entities.HistoriqueTravauxDevis;
import com.entities.Paiement;

import handyman.HandyManUtils;
import veda.godao.DAO;

public class Exporter {
    public static String formatterHTML(Connection connect, DAO dao, File html, Devis devis) throws Exception{
        HistoriqueTravauxDevis where=new HistoriqueTravauxDevis();
        where.setDevis(devis);
        HistoriqueTravauxDevis[] travaux=dao.select(connect, HistoriqueTravauxDevis.class, where);
        HistoriqueDetailsDevis where_details=new HistoriqueDetailsDevis();
        where_details.setDevis(devis);
        HistoriqueDetailsDevis details=dao.select(connect, HistoriqueDetailsDevis.class, where_details)[0];
        String lignesDetails=HandyManUtils.purgeUTF8(details.getHTML());
        String lignesTravaux="";
        double montantTotal=0;
        for(HistoriqueTravauxDevis h:travaux){
            montantTotal+=h.getMontant();
            lignesTravaux+=HandyManUtils.purgeUTF8(h.getHTML());
        }
        String montantAvecPourcentString=HandyManUtils.number_format(devis.getMontant(), " ", ",");
        String montantTotalString=HandyManUtils.number_format(montantTotal, " ", ",");
        String htmlContent=HandyManUtils.getFileContent(html);
        Paiement where_paie=new Paiement();
        where_paie.setDevis(devis);
        Paiement[] paies=dao.select(connect, Paiement.class, where_paie);
        String htmlPaies="<ul>";
        double sommePaie=0;
        for(Paiement p:paies){
            htmlPaies+=p.getHTML();
            sommePaie+=p.getMontant();
        }
        String sommePaieString=HandyManUtils.number_format(sommePaie, " ", ",")+" "+Constantes.ARIARY;
        htmlPaies+="</ul>";
        htmlContent=htmlContent.replace("<!-- PAIES -->", htmlPaies);
        htmlContent=htmlContent.replace("<!-- TOTAL PAIE -->", sommePaieString);
        htmlContent=htmlContent.replace("<!-- LIEU -->", devis.getLieu());
        htmlContent=htmlContent.replace("<!-- DETAILS DEVIS -->", lignesDetails);
        htmlContent=htmlContent.replace("<!-- LIGNES -->", lignesTravaux);
        htmlContent=htmlContent.replace("<!-- MONTANT TOTAL -->", montantTotalString);
        htmlContent=htmlContent.replace("<!-- MONTANT TOTAL POURCENT -->", montantAvecPourcentString);
        return htmlContent;
    }
}
