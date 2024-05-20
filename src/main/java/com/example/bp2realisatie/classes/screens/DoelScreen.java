package com.example.bp2realisatie.classes.screens;

import com.example.bp2realisatie.classes.Database;
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

public class DoelScreen {
    private VBox root;
    private Stage primaryStage;
    private Database database;
    private TextField txtDoel;
    private String gebruikersnaam;
    Connection conn;

    public DoelScreen(Database database, String gebruikersnaam) {
        this.primaryStage = primaryStage;
        this.database = database;
        this.gebruikersnaam = gebruikersnaam;
        this.conn = database.getConnection();

        // Initialize UI components
        root = new VBox(10);
        root.setPadding(new Insets(10));

        Label lblDoel = new Label("Doelbedrag:");
        txtDoel = new TextField();

        Button btnStelDoelIn = new Button("Stel Doel In");
        btnStelDoelIn.setOnAction(e -> stelDoelIn());

        root.getChildren().addAll(lblDoel, txtDoel, btnStelDoelIn);
    }

    public Parent getScreen() {
        return root;
    }

    private void stelDoelIn() {
        try {
            // Probeer het bedrag in te lezen met behulp van de standaard locale
            double doelBedrag = NumberFormat.getInstance().parse(txtDoel.getText()).doubleValue();

            // Opslaan in de database
            opslaanDoel(doelBedrag);

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

    public void opslaanDoel( double bedrag) {
        try {
            int gebruikerId = database.haalGebruikerIdOp(gebruikersnaam);

            PreparedStatement statement = conn.prepareStatement("INSERT INTO doel (gebruiker_id, bedrag) VALUES (?, ?)");
            statement.setInt(1, gebruikerId);
            statement.setDouble(2, bedrag);
            statement.executeUpdate();
            conn.commit();
            System.out.println("Doel met succes opgeslagen!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Er is een fout opgetreden bij het opslaan van het doel.");
        }
    }
    }
