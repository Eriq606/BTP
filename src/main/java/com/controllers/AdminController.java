package com.controllers;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.auth.Admin;
import com.auth.Client;
import com.auth.UserNonExistantException;
import com.config.Constantes;
import com.config.Iris;
import com.config.MyDAO;
import com.entities.Devis;
import com.entities.ErreurImport;
import com.entities.Finition;
import com.entities.HistoriqueDetailsDevis;
import com.entities.HistoriqueTravauxDevis;
import com.entities.ImportDevis;
import com.entities.ImportMaisonTravaux;
import com.entities.ImportPaiement;
import com.entities.Maison;
import com.entities.Paiement;
import com.entities.StatsDevisAnnee;
import com.entities.StatsDevisMois;
import com.entities.Travaux;
import com.entities.TypeTravaux;
import com.entities.Unite;

import handyman.HandyManUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import veda.godao.utils.DAOConnexion;

@Controller
public class AdminController {
    private MyDAO dao=new MyDAO();
    @GetMapping("/login-admin")
    public String login(Model model, String message, HttpServletRequest req){
        HttpSession session=req.getSession();
        session.removeAttribute("admin");
        session.setAttribute("previousURL", "/login-admin");
        String messageDecoded="";
        if(message!=null){
            messageDecoded=HandyManUtils.decodeURL_UTF8(message);
        }
        model.addAttribute("title", "Connexion");
        model.addAttribute("message", messageDecoded);   
        return "loginadmin";
    }
    @PostMapping("/login-admin")
    public RedirectView login(HttpServletRequest req, String identifiant, String motdepasse) throws Exception{
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            Admin admin=new Admin();
            admin.setIdentifiant(identifiant);
            admin.setMotDePasse(motdepasse);
            admin.seConnecter(connect, dao);
            connect.commit();
            HttpSession session=req.getSession();
            session.setAttribute("admin", admin);
            return new RedirectView("/tableau-bord");
        }catch(UserNonExistantException e){
            String message=e.getMessage();
            message=HandyManUtils.encodeURL_UTF8(message);
            return new RedirectView("/login-admin?message="+message);
        }
    }
    @GetMapping("/tableau-bord")
    public Object tableauBord(HttpServletRequest req, Model model, String annee) throws SQLException, Exception{
        HttpSession session=req.getSession();
        Object loggedIn=Iris.adminCheckWithoutTemplating(session, model, "tableaubord", "Tableau de bord");
        if(loggedIn instanceof RedirectView){
            return loggedIn;
        }
        int anneeFiltre=Constantes.ANNEE_DEFAUT;
        if(annee!=null){
            anneeFiltre=Integer.parseInt(annee);
        }
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            String queryDevisTotal="select coalesce(sum(montant),0) as somme from devis";
            String paiementTotal="select coalesce(sum(somme), 0) as somme from v_devis_somme_paye";
            String queryDevisAnnee="select * from v_devis_total_annee order by annee desc limit %s";
            queryDevisAnnee=String.format(queryDevisAnnee, Constantes.STATISTIQUES_NBANNEES_DEFAUT);
            String queryDevisMois=Constantes.QUERY_DEVIS_MOIS;
            queryDevisMois=String.format(queryDevisMois, anneeFiltre);
            double sommeDevis=Double.parseDouble(dao.select(connect, queryDevisTotal)[0].get("somme").toString());
            double sommePaiement=Double.parseDouble(dao.select(connect, paiementTotal)[0].get("somme").toString());
            String sommeDevisString=HandyManUtils.number_format(sommeDevis, " ", ",")+" "+Constantes.ARIARY;
            String sommePaiementString=HandyManUtils.number_format(sommePaiement, " ", ",")+" "+Constantes.ARIARY;
            HashMap<String, Object>[] devisAnnees=dao.select(connect, queryDevisAnnee);
            HashMap<String, Object>[] devisMois=dao.select(connect, queryDevisMois);
            StatsDevisAnnee[] devisAnneesTab=StatsDevisAnnee.getStatsMois(devisAnnees);
            StatsDevisMois[] devisMoisTab=StatsDevisMois.getStatsMois(devisMois);
            model.addAttribute("sommeDevis", sommeDevisString);
            model.addAttribute("sommePaiement", sommePaiementString);
            model.addAttribute("devisAnnee", devisAnneesTab);
            model.addAttribute("devisMois", devisMoisTab);
            model.addAttribute("anneeFiltre", anneeFiltre);
            return loggedIn;
        }
    }
    @GetMapping("/import-maison-travaux")
    public Object importerDonnees(HttpServletRequest req, Model model){
        HttpSession session=req.getSession();
        Object loggedIn=Iris.adminCheck(session, model, "importmaisontravaux", "Importation de donnees");
        return loggedIn;
    }
    @PostMapping("/import-maison-travaux")
    public Object importerDonnees(HttpServletRequest req, Model model, MultipartFile maisontravaux, MultipartFile devisMultipart) throws SQLException, Exception{
        HttpSession session=req.getSession();
        Object loggedIn=Iris.adminCheck(session, model, "revision-import-maisontravaux", "Confirmation d'import");
        if(loggedIn instanceof RedirectView){
            return loggedIn;
        }
        File maisonTravaux=HandyManUtils.createFileFromBytes(maisontravaux.getOriginalFilename(), maisontravaux.getBytes());
        File devis=HandyManUtils.createFileFromBytes(devisMultipart.getOriginalFilename(), devisMultipart.getBytes());
        HashMap<String, String>[] maisonsBrut=HandyManUtils.readCSV(maisonTravaux, ',');
        HashMap<String, String>[] devisBrut=HandyManUtils.readCSV(devis, ',');
        LinkedList<ImportMaisonTravaux> maisonsTravauxDonnees=new LinkedList<>();
        LinkedList<ImportDevis> devisDonnees=new LinkedList<>();
        LinkedList<ErreurImport> erreursMaisonTravaux=new LinkedList<>();
        LinkedList<ErreurImport> erreursDevis=new LinkedList<>();
        ImportMaisonTravaux import_maison;
        ImportDevis import_devis;
        ErreurImport erreur;
        for(int i=0;i<maisonsBrut.length;i++){
            try{
                import_maison=new ImportMaisonTravaux(i+1, maisonsBrut[i]);
                maisonsTravauxDonnees.add(import_maison);
            }catch(Exception e){
                erreur=new ErreurImport();
                erreur.setFichier(maisonTravaux);
                erreur.setLigne(i+1);
                erreur.setMessage(e.getMessage());
                erreursMaisonTravaux.add(erreur);
                e.printStackTrace();
            }
        }
        for(int i=0;i<devisBrut.length;i++){
            try{
                System.out.println(devisBrut[i].get("date_devis"));
                import_devis=new ImportDevis(i+1, devisBrut[i]);
                devisDonnees.add(import_devis);
            }catch(Exception e){
                erreur=new ErreurImport();
                erreur.setFichier(devis);
                erreur.setLigne(i+1);
                erreur.setMessage(e.getMessage());
                erreursDevis.add(erreur);
                e.printStackTrace();
            }
        }
        Connection connect=DAOConnexion.getConnexion(dao);
        try{
            for(ImportMaisonTravaux i:maisonsTravauxDonnees){
                dao.insertWithoutPrimaryKey(connect, i);
            }
            for(ImportDevis i:devisDonnees){
                dao.insertWithoutPrimaryKey(connect, i);
            }
            dao.execute(connect, Maison.QUERY_IMPORT);
            dao.execute(connect, Travaux.QUERY_IMPORT_UNITE);
            dao.execute(connect, Travaux.QUERY_IMPORT);
            dao.execute(connect, Travaux.QUERY_IMPORT_TYPEMAISON_TRAVAUX);
            dao.execute(connect, Client.QUERY_IMPORT);
            dao.execute(connect, Finition.QUERY_IMPORT);
            dao.execute(connect, Devis.QUERY_IMPORT);
            dao.execute(connect, Devis.QUERY_UPDATE_MONTANT1);
            dao.execute(connect, Devis.QUERY_UPDATE_MONTANT2);
            dao.execute(connect, Devis.QUERY_UPDATE_RESTEAPAYER);
            dao.execute(connect, HistoriqueDetailsDevis.QUERY_IMPORT);
            dao.execute(connect, HistoriqueTravauxDevis.QUERY_IMPORT);
            dao.execute(connect, Constantes.CLEAN_IMPORT_TABLES_MAISON);
            dao.execute(connect, Constantes.CLEAN_IMPORT_TABLES_DEVIS);
            connect.commit();
            return loggedIn;
        }catch(Exception e){
            connect.rollback();
            e.printStackTrace();
            return loggedIn;
        }finally{
            connect.close();
            model.addAttribute("fichierMaison", maisontravaux.getOriginalFilename());
            model.addAttribute("fichierDevis", devisMultipart.getOriginalFilename());
            model.addAttribute("erreursMaison", erreursMaisonTravaux);
            model.addAttribute("erreursDevis", erreursDevis);
        }
    }
    @GetMapping("/importpaiement")
    public Object importerPaiements(HttpServletRequest req, Model model){
        HttpSession session=req.getSession();
        Object loggedIn=Iris.adminCheck(session, model, "importpaiement", "Importation de paiement");
        return loggedIn;
    }
    @PostMapping("/importpaiement")
    public Object importerPaiement(HttpServletRequest req, Model model, MultipartFile paiementsMultipart) throws SQLException, Exception{
        HttpSession session=req.getSession();
        Object loggedIn=Iris.adminCheck(session, model, "revision-import-paiement", "Confirmation d'import de paiement");
        if(loggedIn instanceof RedirectView){
            return loggedIn;
        }
        File paiements=HandyManUtils.createFileFromBytes(paiementsMultipart.getOriginalFilename(), paiementsMultipart.getBytes());
        HashMap<String, String>[] paiementBrut=HandyManUtils.readCSV(paiements, ',');
        LinkedList<ImportPaiement> paiementDonnees=new LinkedList<>();
        LinkedList<ErreurImport> erreursPaiement=new LinkedList<>();
        ImportPaiement import_paiement;
        ErreurImport erreur;
        for(int i=0;i<paiementBrut.length;i++){
            try{
                import_paiement=new ImportPaiement(i+1, paiementBrut[i]);
                paiementDonnees.add(import_paiement);
            }catch(Exception e){
                erreur=new ErreurImport();
                erreur.setFichier(paiements);
                erreur.setLigne(i+1);
                erreur.setMessage(e.getMessage());
                erreursPaiement.add(erreur);
                e.printStackTrace();
            }
        }
        Connection connect=DAOConnexion.getConnexion(dao);
        try{
            Paiement where;
            boolean existsInCSV=false, existsInDatabase=false;
            for(int i=0;i<paiementDonnees.size();i++){
                where=new Paiement();
                where.setRefpaiement(paiementDonnees.get(i).getRef_paiement());
                for(int j=0;j<i;j++){
                    if(paiementDonnees.get(i).getRef_paiement().equals(paiementDonnees.get(j).getRef_paiement())){
                        existsInCSV=true;
                        break;
                    }
                }
                existsInDatabase=dao.exists(connect, Paiement.class, where);
                if(existsInCSV||existsInDatabase){
                    continue;
                }
                dao.insertWithoutPrimaryKey(connect, paiementDonnees.get(i));
            }
            dao.execute(connect, Paiement.QUERY_IMPORT);
            dao.execute(connect, Devis.QUERY_UPDATE_RESTEAPAYER_PAIEMENT);
            dao.execute(connect, Constantes.CLEAN_IMPORT_TABLES_PAIEMENTS);
            connect.commit();
            return loggedIn;
        }catch(Exception e){
            connect.rollback();
            e.printStackTrace();
            model.addAttribute("erreurs", e.getStackTrace());
            return loggedIn;
        }finally{
            connect.close();
            model.addAttribute("fichierPaiement", paiementsMultipart.getOriginalFilename());
            model.addAttribute("erreursPaiement", erreursPaiement);
        }
    }
    @GetMapping("/types-travaux")
    public Object travaux(HttpServletRequest req, Model model) throws SQLException, Exception{
        HttpSession session=req.getSession();
        Object loggedIn=Iris.adminCheck(session, model, "travaux", "Liste des types de travaux");
        if(loggedIn instanceof RedirectView){
            return loggedIn;
        }
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            TypeTravaux[] travaux=dao.select(connect, TypeTravaux.class);
            Unite[] unites=dao.select(connect, Unite.class);
            model.addAttribute("travaux", travaux);
            model.addAttribute("unites", unites);
            return loggedIn;
        }
    }
    @PostMapping("/types-travaux")
    public RedirectView travaux(HttpServletRequest req, String travaux, String designation, String unite, String prixunitaire, String code) throws Exception{
        HttpSession session=req.getSession();
        RedirectView loggedIn=Iris.adminCheckREST(session);
        if(loggedIn!=null){
            return loggedIn;
        }
        TypeTravaux travauxObj=new TypeTravaux(travaux, designation, unite, prixunitaire, code);
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            travauxObj.enregistrer(connect, dao);
            connect.commit();
            return new RedirectView("/types-travaux");
        }
    }
    @GetMapping("/finitions")
    public Object finitions(HttpServletRequest req, Model model) throws SQLException, Exception{
        HttpSession session=req.getSession();
        Object loggedIn=Iris.adminCheck(session, model, "finitions", "Finitions");
        if(loggedIn instanceof RedirectView){
            return loggedIn;
        }
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            Finition[] finitions=dao.select(connect, Finition.class);
            model.addAttribute("finitions", finitions);
            return loggedIn;
        }
    }
    @PostMapping("/finitions")
    public RedirectView finitions(HttpServletRequest req, String finition, String pourcentage) throws Exception{
        HttpSession session=req.getSession();
        RedirectView loggedIn=Iris.adminCheckREST(session);
        if(loggedIn!=null){
            return loggedIn;
        }
        Connection connect=DAOConnexion.getConnexion(dao);
        try{
            Finition where=new Finition();
            where.setId(Integer.parseInt(finition));
            Finition modif=new Finition();
            modif.setPourcentage(Double.parseDouble(pourcentage));
            dao.update(connect, modif, where);
            connect.commit();
            return new RedirectView("/finitions");
        }catch(Exception e){
            connect.rollback();
            throw e;
        }finally{
            connect.close();
        }
    }
    @GetMapping("/reset")
    public RedirectView resetData(HttpServletRequest req, Model model) throws SQLException, Exception{
        HttpSession session=req.getSession();
        RedirectView loggedIn=Iris.adminCheckREST(session);
        if(loggedIn!=null){
            return loggedIn;
        }
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            dao.execute(connect, Constantes.RESET_QUERY);
            connect.commit();
            return new RedirectView("/tableau-bord");
        }
    }
    @GetMapping("/exportdash")
    public Object exportDash(HttpServletRequest req, Model model, String annee) throws SQLException, Exception{
        HttpSession session=req.getSession();
        Object loggedIn=Iris.adminCheckWithoutTemplating(session, model, "dashboardpdf", "Tableau de bord");
        if(loggedIn instanceof RedirectView){
            return loggedIn;
        }
        int anneeFiltre=Constantes.ANNEE_DEFAUT;
        if(annee!=null){
            anneeFiltre=Integer.parseInt(annee);
        }
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            String queryDevisTotal="select coalesce(sum(montant),0) as somme from devis";
            String paiementTotal="select coalesce(sum(somme), 0) as somme from v_devis_somme_paye";
            String queryDevisAnnee="select * from v_devis_total_annee order by annee desc limit %s";
            queryDevisAnnee=String.format(queryDevisAnnee, Constantes.STATISTIQUES_NBANNEES_DEFAUT);
            String queryDevisMois=Constantes.QUERY_DEVIS_MOIS;
            queryDevisMois=String.format(queryDevisMois, anneeFiltre);
            double sommeDevis=Double.parseDouble(dao.select(connect, queryDevisTotal)[0].get("somme").toString());
            double sommePaiement=Double.parseDouble(dao.select(connect, paiementTotal)[0].get("somme").toString());
            String sommeDevisString=HandyManUtils.number_format(sommeDevis, " ", ",")+" "+Constantes.ARIARY;
            String sommePaiementString=HandyManUtils.number_format(sommePaiement, " ", ",")+" "+Constantes.ARIARY;
            HashMap<String, Object>[] devisAnnees=dao.select(connect, queryDevisAnnee);
            HashMap<String, Object>[] devisMois=dao.select(connect, queryDevisMois);
            StatsDevisAnnee[] devisAnneesTab=StatsDevisAnnee.getStatsMois(devisAnnees);
            StatsDevisMois[] devisMoisTab=StatsDevisMois.getStatsMois(devisMois);
            model.addAttribute("sommeDevis", sommeDevisString);
            model.addAttribute("sommePaiement", sommePaiementString);
            model.addAttribute("devisAnnee", devisAnneesTab);
            model.addAttribute("devisMois", devisMoisTab);
            model.addAttribute("anneeFiltre", anneeFiltre);
            return loggedIn;
        }
    }
}
