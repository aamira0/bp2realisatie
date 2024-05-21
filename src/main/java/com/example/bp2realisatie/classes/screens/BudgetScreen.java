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

public class BudgetScreen {
    private VBox root;
    private Stage primaryStage;
    private Database database;
    private TextField txtBudget;
    private String gebruikersnaam;
    private Gebruiker gebruiker;
    Connection conn;

    public BudgetScreen(Stage primaryStage, Database database, String gebruikersnaam) {
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

        Label lblBudget = new Label("Budgetbedrag:");
        txtBudget = new TextField();

        Button verwerkBudgetButton = new Button("Verwerk Budget");
        verwerkBudgetButton.setOnAction(e -> verwerkBudget());

        root.getChildren().addAll(backButton, lblBudget, txtBudget, verwerkBudgetButton);
    }

    public Parent getScreen() {
        return root;
    }

    // Verwerken van het budget
    private void verwerkBudget() {
        try {
            // Bedrag ophalen uit het tekstveld
            String bedragString = txtBudget.getText();

            // Controleren of het tekstveld leeg is
            if (bedragString.isEmpty()) {
                System.out.println("Voer een bedrag in voor het budget.");
                return;
            }
            //Probeert het bedrag in te lezen
            double budget = NumberFormat.getInstance().parse(bedragString).doubleValue();

            // Haal gebruiker ID op
            int gebruikerId = database.haalGebruikerIdOp(gebruikersnaam);
            if (gebruikerId == -1) {
                System.out.println("Gebruiker niet gevonden.");
                return;
            }

            // Budget opslaan in de database
            boolean success = database.opslaanBudget(budget, gebruikerId);
            if (success) {
                System.out.println("Budget succesvol opgeslagen.");
            } else {
                System.out.println("Fout bij het opslaan van budget.");
            }

//
//            budget += budgetBedrag;
//            updateOvergeblevenBudget();

            // Tekstveld leegmaken
            txtBudget.clear();
        } catch (ParseException | NumberFormatException ex) {
            System.out.println("Voer een geldig bedrag in voor het budget.");
            ex.printStackTrace();
        }
    }
}
