package com.example.bp2realisatie.classes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Doel {
    private String naam;
    private DoubleProperty bedragProperty;
    private double voortgang;

    public Doel(String naam, double bedrag) {
        this.naam = naam;
        this.bedragProperty = new SimpleDoubleProperty(bedrag);
        this.voortgang = 0.0;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public DoubleProperty bedragProperty() {
        return bedragProperty;
    }

    public double getBedrag() {
        return bedragProperty.get();
    }

    public void setBedrag(double bedrag) {
        bedragProperty.set(bedrag);
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