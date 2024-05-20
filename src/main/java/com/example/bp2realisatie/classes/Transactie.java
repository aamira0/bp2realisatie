package com.example.bp2realisatie.classes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Transactie {
    private DoubleProperty bedrag;

    public Transactie(double bedrag) {
        this.bedrag = new SimpleDoubleProperty(bedrag);
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


