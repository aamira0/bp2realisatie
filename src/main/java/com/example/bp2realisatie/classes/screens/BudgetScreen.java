package com.example.bp2realisatie.classes.screens;

import com.example.bp2realisatie.classes.Database;
import com.example.bp2realisatie.classes.Gebruiker;
import javafx.geometry.Insets;
import javafx.scene.Parent;
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
    Connection conn;

    public BudgetScreen(Database database, String gebruikersnaam) {
        this.primaryStage = primaryStage;
        this.database = database;
        this.gebruikersnaam = gebruikersnaam;
        this.conn = database.getConnection();

        // Initialize UI components
        root = new VBox(10);
        root.setPadding(new Insets(10));

        Label lblBudget = new Label("Budgetbedrag:");
        txtBudget = new TextField();

        Button verwerkBudgetButton = new Button("Verwerk Budget");
        verwerkBudgetButton.setOnAction(e -> verwerkBudget());

        root.getChildren().addAll(lblBudget, txtBudget, verwerkBudgetButton);
    }

    public Parent getScreen() {
        return root;
    }

    private void verwerkBudget() {
        try {
            // Probeer het bedrag in te lezen met behulp van de standaard locale
            double budgetBedrag = NumberFormat.getInstance().parse(txtBudget.getText()).doubleValue();

            // Opslaan in de database
            opslaanBudget(budgetBedrag);
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

    public void opslaanBudget(double bedrag) {
        try {
            int gebruikerId = database.haalGebruikerIdOp(gebruikersnaam);

            PreparedStatement statement = conn.prepareStatement("INSERT INTO budget (gebruiker_id, bedrag) VALUES (?, ?)");
            statement.setInt(1, gebruikerId);
            statement.setDouble(2, bedrag);
            statement.executeUpdate();
            conn.commit();
            System.out.println("Budget met succes opgeslagen!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Er is een fout opgetreden bij het opslaan van het budget.");
        }
    }
}
