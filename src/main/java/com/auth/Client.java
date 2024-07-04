package com.auth;

import java.sql.Connection;

import veda.godao.DAO;
import veda.godao.annotations.Column;
import veda.godao.annotations.PrimaryKey;
import veda.godao.annotations.Table;

@Table("utilisateurs")
public class Client {
    public static final String QUERY_IMPORT="insert into utilisateurs(telephone) select client from import_devis group by client";
    @Column("id")
    @PrimaryKey
    private Integer id;
    @Column("telephone")
    private String phone;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void seConnecter(Connection connect, DAO dao) throws Exception{
        Client[] client=dao.select(connect, Client.class, this);
        if(client.length==1){
            setId(client[0].getId());
            return;
        }
        try{
            int lastId=dao.insertWithoutPrimaryKey(connect, this);
            setId(lastId);
        }catch(Exception e){
            connect.rollback();
            throw e;
        }
    }
}
