package com.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.auth.Client;
import com.config.Exporter;
import com.config.Iris;
import com.config.MyDAO;
import com.entities.Devis;
import com.entities.Finition;
import com.entities.Maison;
import com.entities.Paiement;

import handyman.HandyManUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import veda.godao.utils.DAOConnexion;

@Controller
public class DevisController {
    private MyDAO dao=new MyDAO();
    @Autowired
    private ResourceLoader loader;
    @GetMapping("/creation-devis")
    public Object creationDevis(HttpServletRequest req, Model model) throws Exception{
        HttpSession session=req.getSession();
        Object loggedInCheck=Iris.loggedInCheck(session, model, "creationdevis", "Creation de devis");
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            Maison[] maisons=dao.select(connect, Maison.class);
            Finition[] finitions=dao.select(connect, Finition.class);
            String aujourdhui=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).replace(" ", "T");
            model.addAttribute("maisons", maisons);
            model.addAttribute("finitions", finitions);
            model.addAttribute("aujourdhui", aujourdhui);
            return loggedInCheck;
        }
    }
    @PostMapping("/creation-devis")
    public RedirectView creationDevis(HttpServletRequest req, String lieu, String typemaison, String finition, String datedebut) throws Exception{
        HttpSession session=req.getSession();
        RedirectView loggedInCheck=Iris.loggedInCheckREST(session);
        if(loggedInCheck!=null){
            return loggedInCheck;
        }
        LocalDateTime datecreationDevis=LocalDateTime.now();
        Client client=(Client)session.getAttribute("client");
        Devis devis=new Devis(typemaison, finition, datedebut, datecreationDevis, client);
        devis.setLieu(lieu);
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            devis.enregistrer(connect, dao);
            connect.commit();
            return new RedirectView("/liste-devis");
        }
    }
    @GetMapping("/liste-devis")
    public Object listeDevis(HttpServletRequest req, Model model) throws SQLException, Exception{
        HttpSession session=req.getSession();
        Object loggedInCheck=Iris.loggedInCheck(session, model, "listedevis", "Liste des devis");
        if(loggedInCheck instanceof RedirectView){
            return loggedInCheck;
        }
        Client client=(Client)session.getAttribute("client");
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            Devis where=new Devis();
            where.setClient(client);
            Devis[] devis=dao.select(connect, Devis.class, where);
            model.addAttribute("devis", devis);
            return loggedInCheck;
        }
    }
    @PostMapping("/payer")
    public RedirectView payer(HttpServletRequest req, String devis, String datepaiement, String montant) throws SQLException, Exception{
        HttpSession session=req.getSession();
        RedirectView loggedIn=Iris.loggedInCheckREST(session);
        if(loggedIn!=null){
            return loggedIn;
        }
        Client client=(Client)session.getAttribute("client");
        Paiement paiement=new Paiement(devis, client, datepaiement, montant);
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            paiement.enregistrer(connect, dao);
            connect.commit();
            return new RedirectView("/liste-devis");
        }
    }
    @PostMapping("/payerAjax")
    public String payerAJAX(HttpServletRequest req, String devis, String datepaiement, String montant) throws SQLException, Exception{
        double montantDouble=0;
        if(montant!=null){
            montantDouble=Double.parseDouble(montant);
        }
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            Devis devisObj=new Devis();
            devisObj.setId(Integer.parseInt(devis));
            devisObj=dao.select(connect, Devis.class, devisObj)[0];
            Paiement paiement=new Paiement(devisObj, LocalDateTime.parse(datepaiement), montantDouble);
            boolean valide=devisObj.getResteAPayer()>=montantDouble;
            if(valide){
                paiement.enregistrer(connect, dao);
            }
            return HandyManUtils.toJson(valide);
        }
    }
    @GetMapping("/exporter")
    public void exporter(String iddevis, HttpServletResponse response) throws Exception{
        Resource htmlResource=loader.getResource("classpath:static/htmlpdf/devis.html");
        Resource fauxResource=loader.getResource("classpath:static/htmlpdf/faux.html");
        File html=htmlResource.getFile();
        File faux=fauxResource.getFile();
        Devis where_devis=new Devis();
        where_devis.setId(Integer.parseInt(iddevis));
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            Devis devis=dao.select(connect, Devis.class, where_devis)[0];
            String newHTML=Exporter.formatterHTML(connect, dao, html, devis);
            HandyManUtils.overwriteFileContent(faux, newHTML);
            File pdf=HandyManUtils.generatePDF(faux, "devis-"+devis.getIdmatricule()+".pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + pdf.getName());
            try(InputStream is=new FileInputStream(pdf);OutputStream os=response.getOutputStream()){
                os.write(is.readAllBytes());
            }
        }
    }
}
