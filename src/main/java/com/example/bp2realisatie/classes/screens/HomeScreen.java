package com.example.bp2realisatie.classes.screens;

import com.example.bp2realisatie.classes.Database;
import com.example.bp2realisatie.classes.Gebruiker;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeScreen {
    private VBox root;
    private Stage primaryStage; // De hoofd-Stage van de applicatie
    private TransactieScreen transactieScreen;
    private BudgetScreen budgetScreen;
    private DoelScreen doelScreen;
    private Gebruiker gebruiker; // De huidige gebruiker
    private Database database;
    private String gebruikersnaam; // Gebruikersnaam van de huidige gebruiker

    // Constructor voor het startscherm.
    public HomeScreen(Stage primaryStage, Gebruiker gebruiker, Database database, String gebruikersnaam) {  // Voeg gebruikersnaam als parameter toe
        this.primaryStage = primaryStage;
        this.gebruiker = gebruiker;
        this.database = database;
        this.gebruikersnaam = gebruikersnaam; // Initialiseer gebruikersnaam

        // Initialize andere screens
        this.transactieScreen = new TransactieScreen(primaryStage, database, gebruikersnaam);  // Geef gebruikersnaam en primaryStage ook door voor switchen van screens
        this.budgetScreen = new BudgetScreen(primaryStage, database, gebruikersnaam);
        this.doelScreen = new DoelScreen(primaryStage, database, gebruikersnaam);

        // Initialize ander UI components
        root = new VBox(10); // VBox met een tussenruimte van 10 pixels tussen kinderen
        root.setPadding(new Insets(10)); // Ruimte rondom de VBox

        // Knoppen om naar verschillende schermen te navigeren
        Button btnTransactieScherm = new Button("Naar Transactie Scherm");
        btnTransactieScherm.setOnAction(e -> primaryStage.setScene(new Scene(transactieScreen.getScreen())));

        Button btnBudgetScherm = new Button("Naar Budget Scherm");
        btnBudgetScherm.setOnAction(e -> primaryStage.setScene(new Scene(budgetScreen.getScreen())));

        Button btnDoelScherm = new Button("Naar Doel Scherm");
        btnDoelScherm.setOnAction(e -> primaryStage.setScene(new Scene(doelScreen.getScreen())));

        // Toevoegen van de knoppen aan de VBox
        root.getChildren().addAll(btnTransactieScherm, btnBudgetScherm, btnDoelScherm);
    }

    //Scherm ophalen
    public Parent getScreen() {
        return root;
    }
}


