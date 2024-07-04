package com.auth;

import java.sql.Connection;

import veda.godao.DAO;
import veda.godao.annotations.Column;
import veda.godao.annotations.PrimaryKey;
import veda.godao.annotations.Table;

@Table("admins")
public class Admin {
    @PrimaryKey
    @Column("id")
    private Integer id;
    @Column("identifiant")
    private String identifiant;
    @Column("motdepasse")
    private String motDePasse;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getIdentifiant() {
        return identifiant;
    }
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }
    public String getMotDePasse() {
        return motDePasse;
    }
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    public void seConnecter(Connection connect, DAO dao) throws Exception{
        Admin[] admin=dao.select(connect, Admin.class, this);
        if(admin.length==1){
            setId(admin[0].getId());
            setMotDePasse(null);
            return;
        }
        try{
            int lastId=dao.insertWithoutPrimaryKey(connect, this);
            setId(lastId);
            setMotDePasse(null);
        }catch(Exception e){
            connect.rollback();
            throw e;
        }
    }
}
