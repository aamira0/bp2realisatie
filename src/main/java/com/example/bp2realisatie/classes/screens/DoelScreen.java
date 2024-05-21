package com.example.bp2realisatie.classes.screens;

import com.example.bp2realisatie.classes.Database;
import com.example.bp2realisatie.classes.Doel;
import com.example.bp2realisatie.classes.Gebruiker;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.text.NumberFormat;
import java.text.ParseException;

public class DoelScreen {
    private VBox root;
    private Stage primaryStage;
    private Database database;
    private TextField txtDoel;
    private TextField txtNaam;
    private String gebruikersnaam;
    private Gebruiker gebruiker;
    private double ingevoerdBedrag = 0.0;
    private String ingevoerdeNaam = "";
    private Label lblNaam;
    private Label lblBedrag;

    Connection conn;

    public DoelScreen(Stage primaryStage, Database database, String gebruikersnaam) {
        this.primaryStage = primaryStage;
        this.database = database;
        this.gebruikersnaam = gebruikersnaam;
        this.conn = database.getConnection();

        // Initialize UI components
        root = new VBox(10);
        root.setPadding(new Insets(10));

        Button backButton = new Button("Terug naar Home");
        backButton.setOnAction(e -> {
            // Terug naar het startscherm
            primaryStage.setScene(new Scene(new HomeScreen(primaryStage, gebruiker, database, gebruikersnaam).getScreen()));
        });

        // Creëer labels voor naam en bedrag
        lblNaam = new Label("Naam:");
        lblBedrag = new Label("Bedrag:");

        Label lblNaamInput = new Label("Naam:");
        txtNaam = new TextField();

        Label lblDoel = new Label("Doelbedrag:");
        txtDoel = new TextField();

        Button btnStelDoelIn = new Button("Stel Doel In");
        btnStelDoelIn.setOnAction(e -> stelDoelIn());

        root.getChildren().addAll(backButton, lblNaamInput, txtNaam, lblDoel, txtDoel, btnStelDoelIn, lblNaam, lblBedrag);

        // Laad doelen bij het openen van het scherm
        laadDoelen();
    }

    public Parent getScreen() {
        return root;
    }

    private void stelDoelIn() {
        try {
            // Bedrag en naam ophalen uit de tekstvelden
            String bedragString = txtDoel.getText();
            String naam = txtNaam.getText();

            // Controleren of het tekstveld leeg is
            if (bedragString.isEmpty() || naam.isEmpty()) {
                System.out.println("Voer een naam en een bedrag in voor het doel.");
                return;
            }

            // Probeert het bedrag in te lezen met behulp van de standaard locale
            double doelBedrag = NumberFormat.getInstance().parse(bedragString).doubleValue();

            // Haal gebruiker ID op
            int gebruikerId = database.haalGebruikerIdOp(gebruikersnaam);
            if (gebruikerId == -1) {
                System.out.println("Gebruiker niet gevonden.");
                return;
            }

            // Doel opslaan in de database
            boolean success = database.opslaanDoel(naam, doelBedrag, gebruikerId);
            if (success) {
                System.out.println("Doel succesvol opgeslagen.");
                // Update de labels met de ingevoerde naam en bedrag
                lblNaam.setText("Naam: " + naam);
                lblBedrag.setText("Bedrag: " + doelBedrag);
                // Laad opnieuw de doelen om de laatste wijziging weer te geven
                laadDoelen();
            } else {
                System.out.println("Fout bij het opslaan van doel.");
            }

            // Tekstvelden leegmaken
            txtDoel.clear();
            txtNaam.clear();
        } catch (ParseException | NumberFormatException ex) {
            System.out.println("Voer een geldig bedrag in voor het doel.");
            ex.printStackTrace();
        }
    }

    private void laadDoelen() {
        // Haal de doelen op uit de database
        ObservableList<Doel> doelen = database.haalDoelenOp(gebruikersnaam);

        // Controleer of er doelen zijn opgehaald
        if (doelen.isEmpty()) {
            lblNaam.setText("Geen doelen gevonden.");
            lblBedrag.setText("");
        } else {
            // Haal het laatste doel uit de lijst
            Doel laatsteDoel = doelen.get(doelen.size() - 1);

            // Toon de gegevens van het laatste doel
            lblNaam.setText("Naam: " + laatsteDoel.getNaam());
            lblBedrag.setText("Bedrag: " + laatsteDoel.getBedrag());
        }
    }
}
