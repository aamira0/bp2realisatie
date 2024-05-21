package com.example.bp2realisatie.classes;

public class Doel {
    private String naam;
    private double bedrag;
    private double voortgang;

    public Doel(String naam, double bedrag) {
        this.naam = naam;
        this.bedrag = bedrag;
        this.voortgang = 0.0;
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

    public double getVoortgang() {
        return voortgang;
    }

    public void updateVoortgang(double bedrag) {
        voortgang += bedrag;
    }

    public boolean isDoelBereikt() {
        return voortgang >= bedrag;
    }
}
