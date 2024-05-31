package com.example.bp2realisatie.classes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Transactie {
    private int id; // ID toegevoegd
    private DoubleProperty bedrag; // Bedrag transactie

    // Constructor met id en bedrag om te initialiseren
    public Transactie(int id, double bedrag) {
        this.id = id;
        this.bedrag = new SimpleDoubleProperty(bedrag);
    }

    // Constructor voor gewone nieuwe transacties zonder id
    public Transactie() {
        this.bedrag = new SimpleDoubleProperty(); // Standaardwaarde voor bedrag initialiseren
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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



