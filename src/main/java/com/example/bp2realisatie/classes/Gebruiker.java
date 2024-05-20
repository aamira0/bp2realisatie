package com.example.bp2realisatie.classes;

import java.util.ArrayList;
import java.util.List;

public class Gebruiker {
    private int id;
    private String gebruikersnaam;
    private String wachtwoord;
    private List<Transactie> transacties;
    private List<Doel> doelen;
    private Gebruiker ingelogdeGebruiker;


    public Gebruiker(String gebruikersnaam, String wachtwoord) {
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord = wachtwoord;
        this.transacties = new ArrayList<>();
        this.doelen = new ArrayList<>();
    }

    public int getId() {  // Voeg deze getter toe
        return id;
    }

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public List<Transactie> getTransacties() {
        return transacties;
    }

    public void setTransacties(List<Transactie> transacties) {
        this.transacties = transacties;
    }

    public List<Doel> getDoelen() {
        return doelen;
    }

    public void setDoelen(List<Doel> doelen) {
        this.doelen = doelen;
    }

    public Gebruiker(int id, String gebruikersnaam, String wachtwoord) {
        this.id = id;
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord = wachtwoord;
        this.transacties = new ArrayList<>();
        this.doelen = new ArrayList<>();
    }

    // Methoden om transacties toe te voegen en op te halen
    public void voegTransactieToe(Transactie transactie) {
        transacties.add(transactie);
    }

    public List<Transactie> krijgAlleTransacties() {
        return transacties;
    }

    // Methoden om doelen toe te voegen en op te halen
    public void voegDoelToe(Doel doel) {
        doelen.add(doel);
    }

    public List<Doel> krijgAlleDoelen() {
        return doelen;
    }

    public void setIngelogdeGebruiker(String gebruikersnaam, String wachtwoord) {
        ingelogdeGebruiker = new Gebruiker(gebruikersnaam, wachtwoord);
    }
}
