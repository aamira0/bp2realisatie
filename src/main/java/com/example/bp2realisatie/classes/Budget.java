package com.example.bp2realisatie.classes;

public class Budget {
    private String naam;
    private double bedrag;

    public Budget(String naam, double bedrag) {
        this.naam = naam;
        this.bedrag = bedrag;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public double getBedrag() {
        return bedrag;
    }

    public void setBedrag(double bedrag) {
        this.bedrag = bedrag;
    }
}
