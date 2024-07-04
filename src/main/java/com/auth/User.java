package com.auth;

import java.sql.Connection;
import java.util.HashMap;

import com.config.Constantes;
import com.config.MyDAO;

import handyman.HandyManUtils;
import veda.godao.DAO;
import veda.godao.utils.DAOConnexion;

public class User {
    private String nom;
    private int autorite;
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public int getAutorite() {
        return autorite;
    }
    public void setAutorite(int autorite) {
        this.autorite = autorite;
    }
    public static User seConnecter(Connection connex, String nom, String motdepasse) throws Exception{
        boolean opened=false;
        Connection connect=connex;
        MyDAO dao=new MyDAO();
        if(connect==null){
            connect=DAOConnexion.getConnexion(dao);
            opened=true;
        }
        String query="select nom, permission from v_utilisateur_profil where nom='%s' and motdepasse='%s'";
        motdepasse=HandyManUtils.saltPBKDF(motdepasse, Constantes.PASSWORD_SALT);
        query=String.format(query, nom, motdepasse);
        try{
            HashMap<String, Object>[] result=dao.select(connect, query);
            if(result.length==0){
                throw new UserNonExistantException(nom);
            }
            User user=new User();
            user.setNom(result[0].get("nom").toString());
            user.setAutorite(Integer.parseInt(result[0].get("permission").toString()));
            return user;
        }finally{
            if(opened){
                connect.close();
            }
        }
    }
    public static void inscription(Connection connex, DAO dao, String nom, String motdepasse) throws Exception{
        String query="insert into utilisateurs values(default, '%s', %s, '%s')";
        motdepasse=HandyManUtils.saltPBKDF(motdepasse, Constantes.PASSWORD_SALT);
        query=String.format(query, nom, Constantes.USER_ID, motdepasse);
        try{
            dao.execute(connex, query);
        }catch(Exception e){
            connex.rollback();
            throw e;
        }
    }
}
