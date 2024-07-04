package com.config;

import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import com.auth.Admin;
import com.auth.Client;
import jakarta.servlet.http.HttpSession;

public class Iris {
    public static Object loggedInCheck(HttpSession session, Model model, String viewpage, String title){
        Object userObj=session.getAttribute("client");
        if(userObj==null){
            return new RedirectView("/loginclient?message="+Constantes.UNAUTHENTICATED_MESSAGE);
        }
        Client client=(Client)session.getAttribute("client");
        model.addAttribute("currentuser", client.getPhone());
        model.addAttribute("loginurl", "/loginclient");
        model.addAttribute("title", title);
        model.addAttribute("viewpage", viewpage);
        return "layout/layout-client";
    }


    public static Object adminCheck(HttpSession session, Model model, String viewpage, String title){
        Object userObj=session.getAttribute("admin");
        if(userObj==null){
            return new RedirectView("/loginclient?message="+Constantes.UNAUTHENTICATED_MESSAGE);
        }
        Admin admin=(Admin)session.getAttribute("admin");
        model.addAttribute("currentuser", admin.getIdentifiant());
        model.addAttribute("loginurl", "/login-admin");
        model.addAttribute("title", title);
        model.addAttribute("viewpage", viewpage);
        return "layout/layout";
    }
    public static Object adminCheckWithoutTemplating(HttpSession session, Model model, String viewpage, String title){
        Object userObj=session.getAttribute("admin");
        if(userObj==null){
            return new RedirectView("/loginclient?message="+Constantes.UNAUTHENTICATED_MESSAGE);
        }
        Admin admin=(Admin)session.getAttribute("admin");
        model.addAttribute("currentuser", admin.getIdentifiant());
        model.addAttribute("loginurl", "/login-admin");
        model.addAttribute("title", title);
        return viewpage;
    }


    public static RedirectView loggedInCheckREST(HttpSession session){
        Object userObj=session.getAttribute("client");
        if(userObj==null){
            return new RedirectView("/loginclient?message="+Constantes.UNAUTHENTICATED_MESSAGE);
        }
        return null;
    }

    
    public static RedirectView adminCheckREST(HttpSession session){
        Object userObj=session.getAttribute("admin");
        if(userObj==null){
            return new RedirectView("/loginclient?message="+Constantes.UNAUTHENTICATED_MESSAGE);
        }
        return null;
    }
}
