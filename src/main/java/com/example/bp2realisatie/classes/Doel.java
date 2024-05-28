package com.example.bp2realisatie.classes;

public class Doel extends Financieel{
    private double voortgang;

    public Doel(String naam, double bedrag) {
        super(naam, bedrag);
        this.voortgang = 0.0;
    }

    public double getVoortgang() {
        return voortgang;
    }

    public void updateVoortgang(double bedrag) {
        voortgang += bedrag;
    }

    public boolean isDoelBereikt() {
        return voortgang >= getBedrag();
    }
}
