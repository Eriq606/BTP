package com.config;

import veda.godao.DAO;

public class MyDAO extends DAO{
    public MyDAO(){
        setAllowKeyRetrieval(true);
        setDatabase("btp");
        setDriver("org.postgresql.Driver");
        setHost("localhost");
        setPort("5432");
        setPwd("root");
        setServer("postgresql");
        setSgbd(veda.godao.utils.Constantes.PSQL_ID);
        setUseSSL(false);
        setUser("eriq");
    }
}
