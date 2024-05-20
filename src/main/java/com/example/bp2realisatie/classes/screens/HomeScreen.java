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
    private Stage primaryStage;
    private TransactieScreen transactieScreen;
    private BudgetScreen budgetScreen;
    private DoelScreen doelScreen;
    private Gebruiker gebruiker;
    private Database database;
    private String gebruikersnaam;

    public HomeScreen(Stage primaryStage, Gebruiker gebruiker, Database database, String gebruikersnaam) {  // Voeg gebruikersnaam als parameter toe
        this.primaryStage = primaryStage;
        this.gebruiker = gebruiker;
        this.database = database;
        this.gebruikersnaam = gebruikersnaam; // Sla de gebruikersnaam op

        // Initialize andere screens
        this.transactieScreen = new TransactieScreen(database, gebruikersnaam);  // Geef gebruikersnaam door
        this.budgetScreen = new BudgetScreen(database, gebruikersnaam);
        this.doelScreen = new DoelScreen(database, gebruikersnaam);

        // Initialize other UI components as before
        root = new VBox(10);
        root.setPadding(new Insets(10));

        Button btnTransactieScherm = new Button("Naar Transactie Scherm");
        btnTransactieScherm.setOnAction(e -> primaryStage.setScene(new Scene(transactieScreen.getScreen())));

        Button btnBudgetScherm = new Button("Naar Budget Scherm");
        btnBudgetScherm.setOnAction(e -> primaryStage.setScene(new Scene(budgetScreen.getScreen())));

        Button btnDoelScherm = new Button("Naar Doel Scherm");
        btnDoelScherm.setOnAction(e -> primaryStage.setScene(new Scene(doelScreen.getScreen())));

        root.getChildren().addAll(btnTransactieScherm, btnBudgetScherm, btnDoelScherm);
    }

    public Parent getScreen() {
        return root;
    }
}


