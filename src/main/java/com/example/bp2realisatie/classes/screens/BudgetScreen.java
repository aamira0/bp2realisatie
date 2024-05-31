package com.example.bp2realisatie.classes.screens;

import com.example.bp2realisatie.classes.Budget;
import com.example.bp2realisatie.classes.Database;
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

public class BudgetScreen {
    private VBox root;
    private Stage primaryStage; // De hoofd-Stage van de applicatie
    private Database database;
    private TextField txtBudget;
    private String gebruikersnaam; // De gebruikersnaam van de huidige gebruiker
    private Label lblNaam;
    private Label lblBedrag;
    private TextField txtNaam;
    private double ingevoerdBedrag = 0.0; //Standaardwaarde voor het ingevoerde bedrag
    private String ingevoerdeNaam = ""; // Standaardwaarde voor de ingevoerde naam. Anders verschijnt naam niet
    private Gebruiker gebruiker; // De huidige gebruiker
    Connection conn;

    // Constructor voor het BudgetScreen. Initialiseer de UI-componenten en laad de budgetten.
    public BudgetScreen(Stage primaryStage, Database database, String gebruikersnaam) {
        this.primaryStage = primaryStage;
        this.database = database;
        this.gebruikersnaam = gebruikersnaam;
        this.conn = database.getConnection();

        // Initialize UI components
        root = new VBox(10); // VBox met een tussenruimte van 10 pixels tussen kinderen
        root.setPadding(new Insets(10)); // Ruimte rondom de VBox

        // Knop om terug te gaan naar het startscherm
        Button backButton = new Button("Terug naar Home");
        backButton.setOnAction(e -> {
            // Terug naar het startscherm
            primaryStage.setScene(new Scene(new HomeScreen(primaryStage, gebruiker, database, gebruikersnaam).getScreen()));
        });

        // Labels en tekstvelden voor naam en bedrag van het budget
        lblNaam = new Label("Naam:");
        lblBedrag = new Label("Bedrag:");

        Label lblNaamInput = new Label("Naam:");
        txtNaam = new TextField();

        Label lblBudget = new Label("Budgetbedrag:");
        txtBudget = new TextField();

        // Knop om het budget te verwerken
        Button verwerkBudgetButton = new Button("Verwerk Budget");
        verwerkBudgetButton.setOnAction(e -> verwerkBudget());

        // Voeg componenten toe aan de root container zodat het zichtbaar is.
        root.getChildren().addAll(backButton, lblNaamInput, txtNaam, lblBudget, txtBudget, verwerkBudgetButton, lblNaam, lblBedrag);

        // Laad budgetten bij het openen van het scherm
        laadBudgetten();
    }

    public Parent getScreen() {
        return root;
    } // Geeft het budgetscherm terug

    // Verwerken van het budget
    private void verwerkBudget() {
        try {
            // Bedrag en naam ophalen uit het tekstveld
            String bedragString = txtBudget.getText();
            String naam = txtNaam.getText();

            // Controleren of het tekstveld leeg is
            if (bedragString.isEmpty() || naam.isEmpty()) {
                System.out.println("Voer een naam en een bedrag in voor het budget.");
                return;
            }
            //Probeert het bedrag in te lezen
            double budget = NumberFormat.getInstance().parse(bedragString).doubleValue();

            // Haal gebruiker ID op
            int gebruikerId = database.haalGebruikerIdOp(gebruikersnaam);
            if (gebruikerId == -1) { //Als het ID -1 is
                System.out.println("Gebruiker niet gevonden.");
                return;
            }

            // Budget opslaan in de database
            boolean success = database.opslaanBudget(naam, budget, gebruikerId);
            if (success) {
                System.out.println("Budget succesvol opgeslagen.");
                // Update de labels met de ingevoerde naam en bedrag
                lblNaam.setText("Naam: " + naam);
                lblBedrag.setText("Bedrag: " + budget);
                // Laad opnieuw de budgetten om de laatste wijziging weer te geven
                laadBudgetten();
            } else {
                System.out.println("Fout bij het opslaan van budget.");
            }
            
            // Tekstveld leegmaken
            txtBudget.clear();
            txtNaam.clear();
        } catch (ParseException | NumberFormatException ex) {
            System.out.println("Voer een geldig bedrag in voor het budget.");
            ex.printStackTrace();
        }
    }

    private void laadBudgetten() {
        // Haal de lijst van budgetten op voor de huidige gebruiker
        ObservableList<Budget> budgets = database.haalBudgetOp(gebruikersnaam);

        // Controleer of er budgetten zijn opgehaald
        if (budgets.isEmpty()) {
            // Als er geen budgetten zijn, toon een bericht
            lblNaam.setText("Geen budgetten gevonden.");
            lblBedrag.setText("");
        } else {
            // Haal het laatste budget uit de lijst
            Budget laatsteBudget = budgets.get(budgets.size() - 1);

            // Toon de gegevens van het laatste budget
            lblNaam.setText("Naam: " + laatsteBudget.getNaam());
            lblBedrag.setText("Bedrag: " + laatsteBudget.getBedrag());
        }
    }
}
