package com.controllers;

import java.sql.Connection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.auth.Client;
import com.auth.UserNonExistantException;
import com.config.MyDAO;
import handyman.HandyManUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import veda.godao.utils.DAOConnexion;

@Controller
public class LoginClientController {
    private MyDAO dao=new MyDAO();
    @GetMapping("/loginclient")
    public String login(Model model, String message, HttpServletRequest req) throws Exception{
        HttpSession session=req.getSession();
        session.removeAttribute("client");
        session.setAttribute("previousURL", "/loginclient");
        String messageDecoded="";
        if(message!=null){
            messageDecoded=HandyManUtils.decodeURL_UTF8(message);
        }
        model.addAttribute("title", "Connexion");
        model.addAttribute("message", messageDecoded);   
        return "loginclient";
    }
    @PostMapping("/loginclient")
    public RedirectView connexion(String phone, HttpServletRequest req) throws Exception{
        try(Connection connect=DAOConnexion.getConnexion(dao)){
            Client client=new Client();
            client.setPhone(phone);
            client.seConnecter(connect, dao);
            connect.commit();
            HttpSession session=req.getSession();
            session.setAttribute("client", client);
            return new RedirectView("/liste-devis");
        }catch(UserNonExistantException e){
            String message=e.getMessage();
            message=HandyManUtils.encodeURL_UTF8(message);
            return new RedirectView("/loginclient?message="+message);
        }
    }
}
