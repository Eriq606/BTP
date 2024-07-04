package com.controllers;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.config.Iris;
import com.config.MyDAO;
import com.entities.Devis;
import com.entities.HistoriqueDetailsDevis;
import com.entities.HistoriqueTravauxDevis;

import handyman.HandyManUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import veda.godao.utils.DAOConnexion;

@Controller
public class DevisAdminController {
    private MyDAO dao=new MyDAO();
    @GetMapping("/liste-devis-admin")
    public Object listeDevis(HttpServletRequest req, Model model) throws SQLException, Exception{
        HttpSession session=req.getSession();
        Object loggedInCheck=Iris.adminCheck(session, model, "listedevis-admin", "Liste des devis");
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            Devis[] devis=dao.select(connect, Devis.class);
            if(devis.length>0){
                String iddevis="(";
                for(Devis d:devis){
                    iddevis+=d.getId()+",";
                }
                iddevis=iddevis.substring(0, iddevis.length()-1);
                iddevis+=")";
                HistoriqueDetailsDevis[] details=HistoriqueDetailsDevis.getDetailsFromDevis(connect, dao, iddevis);
                model.addAttribute("details", details);
            }
            model.addAttribute("devis", devis);
            return loggedInCheck;
        }
    }
    @GetMapping("/detailsdevis-admin")
    public Object detailsDevis(HttpServletRequest req, Model model, String iddevis) throws SQLException, Exception{
        HttpSession session=req.getSession();
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            Devis devis=new Devis();
            devis.setId(Integer.parseInt(iddevis));
            devis=dao.select(connect, Devis.class, devis)[0];
            Object loggedInCheck=Iris.adminCheck(session, model, "detailsdevis", "Details du devis "+devis.getIdmatricule());
            if(loggedInCheck instanceof RedirectView){
                return loggedInCheck;
            }
            HistoriqueDetailsDevis details=new HistoriqueDetailsDevis();
            details.setDevis(devis);
            details=dao.select(connect, HistoriqueDetailsDevis.class, details)[0];
            HistoriqueTravauxDevis where_travaux=new HistoriqueTravauxDevis();
            where_travaux.setDevis(devis);
            HistoriqueTravauxDevis[] travaux=dao.select(connect, HistoriqueTravauxDevis.class, where_travaux);
            double montantTotal=0;
            for(HistoriqueTravauxDevis h:travaux){
                montantTotal+=h.getMontant();
            }
            String montantTotalString=HandyManUtils.number_format(montantTotal, " ", ",");
            model.addAttribute("montantTotal", montantTotalString);
            model.addAttribute("devis", devis);
            model.addAttribute("details", details);
            model.addAttribute("travaux", travaux);
            return loggedInCheck;
        }
    }
}
