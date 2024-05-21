package com.example.bp2realisatie.classes.screens;

import com.example.bp2realisatie.classes.Database;
import com.example.bp2realisatie.classes.Gebruiker;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;

public class DoelScreen {
    private VBox root;
    private Stage primaryStage;
    private Database database;
    private TextField txtDoel;
    private String gebruikersnaam;
    private Gebruiker gebruiker;
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

        Label lblDoel = new Label("Doelbedrag:");
        txtDoel = new TextField();

        Button btnStelDoelIn = new Button("Stel Doel In");
        btnStelDoelIn.setOnAction(e -> stelDoelIn());

        root.getChildren().addAll(backButton, lblDoel, txtDoel, btnStelDoelIn);
    }

    public Parent getScreen() {
        return root;
    }

    private void stelDoelIn() {
        try {
            // Bedrag ophalen uit het tekstveld
            String bedragString = txtDoel.getText();

            // Controleren of het tekstveld leeg is
            if (bedragString.isEmpty()) {
                System.out.println("Voer een bedrag in voor het doel.");
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
            boolean success = database.opslaanDoel(doelBedrag, gebruikerId);
            if (success) {
                System.out.println("Doel succesvol opgeslagen.");
            } else {
                System.out.println("Fout bij het opslaan van doel.");
            }


//            doel.setBedrag(doelBedrag);
//            lblDoel.setText("Doelbedrag: €" + doel.bedragProperty().get());
//
//            doelBereikt = false;

            txtDoel.clear();
        } catch (ParseException | NumberFormatException ex) {
            System.out.println("Voer een geldig bedrag in voor het doel.");
            ex.printStackTrace();
        }
    }
}
