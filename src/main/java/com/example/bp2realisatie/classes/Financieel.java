package com.example.bp2realisatie.classes;

public class Financieel {
    private String naam;
    private double bedrag;

    // Constructor om een nieuw financieel object te initialiseren met een naam en bedrag.
    public Financieel(String naam, double bedrag) {
        this.naam = naam;// Initialiseer de naam met de gegeven waarde
        this.bedrag = bedrag; // Initialiseer het bedrag met de gegeven waarde
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
