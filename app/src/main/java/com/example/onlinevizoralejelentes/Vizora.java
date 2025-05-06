package com.example.onlinevizoralejelentes;

public class Vizora {
    private int id;
    private String email;
    private int zipCode;
    private String varos;
    private String utca;
    private int hazNum;
    private int vizOraAllas;

    // Email = User Id
    public Vizora(String email,int zipCode, String varos, String utca, int hazNum, int vizOraAllas){
        this.email = email;
        this.zipCode = zipCode;
        this.varos = varos;
        this.utca = utca;
        this.hazNum = hazNum;
        this.vizOraAllas = vizOraAllas;
    }



    ////////////////////////////////////////////////////////////////////////////////////////
    ///////                         |
    /////// Getterek / Setterek     |
    ///////                         v
    ////////////////////////////////////////////////////////////////////////////////////////

    public int getZipCode() {
        return zipCode;
    }

    public String getVaros() {
        return varos;
    }

    public String getUtca() {
        return utca;
    }

    public int getHazNum() {
        return hazNum;
    }

    public int getVizOraAllas() {
        return vizOraAllas;
    }

    public String getEmail() {
        return email;
    }
}
