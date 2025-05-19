package com.example.onlinevizoralejelentes;

public class Invoices {
    private String id;
    private String email;
    private int zipCode;
    private String varos;
    private String utca;
    private int hazNum;
    private int vizOraAllas;
    private String imageUrl;

    // Email = User Id

    public Invoices(){}
    public Invoices(String email,int zipCode, String varos, String utca, int hazNum, int vizOraAllas, String imageUrl){
        this.email = email;
        this.zipCode = zipCode;
        this.varos = varos;
        this.utca = utca;
        this.hazNum = hazNum;
        this.vizOraAllas = vizOraAllas;
        this.imageUrl = imageUrl;
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    ///////                         |
    /////// Getterek / Setterek     |
    ///////                         v
    ////////////////////////////////////////////////////////////////////////////////////////

    public int getZipCode() {return zipCode;}

    public String getVaros() {return varos;}

    public String getUtca() {return utca;}

    public int getHazNum() {return hazNum;}

    public int getVizOraAllas() {return vizOraAllas;}

    public String getEmail() {return email;}

    public String getImageUrl(){return imageUrl;}

    public String getId(){return id;}

    public void setId(String id){this.id = id;}


}
