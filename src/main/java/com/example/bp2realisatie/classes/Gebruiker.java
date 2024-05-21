package com.example.bp2realisatie.classes;

import java.util.ArrayList;
import java.util.List;

public class Gebruiker {
    private int id;
    private String gebruikersnaam;
    private String wachtwoord;
    private List<Transactie> transacties;
    private List<Doel> doelen;
    private List<Budget> budgetten;

    public Gebruiker(int id, String gebruikersnaam, String wachtwoord) {
        this.id = id;
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord = wachtwoord;
        this.transacties = new ArrayList<>();
        this.doelen = new ArrayList<>();
        this.budgetten = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void voegTransactieToe(Transactie transactie) {
        this.transacties.add(transactie);
    }

    public List<Doel> getDoelen() {
        return doelen;
    }

    public void setDoelen(List<Doel> doelen) {
        this.doelen = doelen;
    }

    public void voegDoelToe(Doel doel) {
        this.doelen.add(doel);
    }

    public List<Budget> getBudgetten() {
        return budgetten;
    }

    public void setBudgetten(List<Budget> budgetten) {
        this.budgetten = budgetten;
    }

    public void voegBudgetToe(Budget budget) {
        this.budgetten.add(budget);
    }
}
