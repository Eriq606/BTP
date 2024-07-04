package com.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.config.MyDAO;
import com.entities.Devis;
import com.entities.Paiement;

import handyman.HandyManUtils;
import veda.godao.utils.DAOConnexion;

@RestController
public class PaiementAjaxController {
    private MyDAO dao=new MyDAO();
    @PostMapping("/verifier")
    public String verifierPayer(String devis, String datepaiement, String montant) throws SQLException, Exception{
        double montantDouble=Double.parseDouble(montant);
        Connection connect=DAOConnexion.getConnexion(dao);
        try{
            Devis devisObj=new Devis();
            devisObj.setId(Integer.parseInt(devis));
            devisObj=dao.select(connect, Devis.class, devisObj)[0];
            Paiement paiement=new Paiement(devisObj, LocalDateTime.parse(datepaiement), montantDouble);
            boolean valide=devisObj.getResteAPayer()>=montantDouble;
            int reponse=0;
            if(valide){
                reponse=1;
                paiement.enregistrer(connect, dao);
                connect.commit();
            }
            return HandyManUtils.toJson(reponse);
        }catch(Exception e){
            connect.rollback();
            e.printStackTrace();
            throw e;
        }
    }
}
