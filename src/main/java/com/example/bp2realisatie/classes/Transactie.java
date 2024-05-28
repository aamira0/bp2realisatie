package com.example.bp2realisatie.classes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Transactie {
    private int id; // ID toegevoegd
    private DoubleProperty bedrag;


    public Transactie(int id, double bedrag) {
        this.id = id;
        this.bedrag = new SimpleDoubleProperty(bedrag);
    }

    //Gewone transactie
    public Transactie(double transactie) {
    }

    public int getId() {
        return id;
    }

    public double getBedrag() {
        return bedrag.get();
    }

    public DoubleProperty bedragProperty() {
        return bedrag;
    }

    public void setBedrag(double bedrag) {
        this.bedrag.set(bedrag);
    }
}



