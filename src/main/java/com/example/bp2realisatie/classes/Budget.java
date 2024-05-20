package com.example.bp2realisatie.classes;

import java.util.ArrayList;
import java.util.List;

public class Budget {
    private String categorie;
    private double bedrag;
    private List<Transactie> transacties;

    public Budget(String categorie, double bedrag) {
        this.categorie = categorie;
        this.bedrag = bedrag;
        this.transacties = new ArrayList<>();
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public double getBedrag() {
        return bedrag;
    }

    public void setBedrag(double bedrag) {
        this.bedrag = bedrag;
    }

    public List<Transactie> getTransacties() {
        return transacties;
    }

    public void voegTransactieToe(Transactie transactie) {
        transacties.add(transactie);
    }
}