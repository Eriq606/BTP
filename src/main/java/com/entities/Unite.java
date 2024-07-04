package com.entities;

import veda.godao.annotations.Column;
import veda.godao.annotations.PrimaryKey;
import veda.godao.annotations.Table;

@Table("unites")
public class Unite {
    @PrimaryKey
    @Column("id")
    private Integer id;
    @Column("symbole")
    private String symbole;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getSymbole() {
        return symbole;
    }
    public void setSymbole(String symbole) {
        this.symbole = symbole;
    }
    
}
