package com.example.bp2realisatie;

import com.example.bp2realisatie.classes.Database;
import com.example.bp2realisatie.classes.Gebruiker;
import com.example.bp2realisatie.classes.screens.HomeScreen;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private Stage primaryStage; // De hoofd-Stage van de applicatie
    private Gebruiker gebruiker; // De huidige gebruiker

    // Het startpunt van de applicatie
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Simulatie van ingelogde gebruiker
        gebruiker = new Gebruiker(1, "John", "1234");

        Database database = new Database();

        String gebruikersnaam = "John";
        String wachtwoord = "1234";

        // Controleren of de gebruiker al bestaat in de database
        if (database.haalGebruikerIdOp(gebruikersnaam) == -1) {
            database.opslaanGebruiker(gebruikersnaam, wachtwoord);
        }

        // Het maken van een VBox voor het inlogscherm
        VBox inlogVBox = new VBox(10); // VBox met een tussenruimte van 10 pixels tussen kinderen
        inlogVBox.setPadding(new Insets(10)); // Ruimte rondom de VBox

        // Labels en tekstvelden voor gebruikersnaam en wachtwoord
        Label lblGebruikersnaam = new Label("Gebruikersnaam:");
        TextField txtGebruikersnaam = new TextField();
        txtGebruikersnaam.setMaxWidth(200); // Stel de maximale breedte in voor het invoerveld

        Label lblWachtwoord = new Label("Wachtwoord:");
        TextField txtWachtwoord = new TextField();
        txtWachtwoord.setMaxWidth(200);

        // Inlogbutton
        Button inlogButton = new Button("Inloggen");

        // Voeg alle elementen toe aan de VBox
        inlogVBox.getChildren().addAll(lblGebruikersnaam, txtGebruikersnaam, lblWachtwoord, txtWachtwoord, inlogButton);

        // Laat alleen het inlogscherm zien bij het starten
        Scene scene = new Scene(inlogVBox);

        // Instellingen voor de primaire stage
        primaryStage.setTitle("Persoonlijk Financieel Beheersysteem");
        primaryStage.setScene(scene);
        primaryStage.setWidth(400);
        primaryStage.setHeight(500);
        primaryStage.show();

        // Acties inlogbutton
        inlogButton.setOnAction(e -> {
            String usernameInput = txtGebruikersnaam.getText();
            String passwordInput = txtWachtwoord.getText();

            // Controleer of beide velden zijn ingevuld
            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                System.out.println("Vul zowel gebruikersnaam als wachtwoord in.");
                return; // Stopt de verdere verwerking als een van de velden leeg is
            }

            // Controleert de geldigheid van de gebruikersnaam en wachtwoord
            if (gebruiker.getGebruikersnaam().equals(usernameInput) && gebruiker.getWachtwoord().equals(passwordInput)) {
                // Maak een homescherm aan en toon het
                HomeScreen homeScreen = new HomeScreen(primaryStage, gebruiker, database, usernameInput);  // Geeft gebruikersnaam door
                Scene homeScene = new Scene(homeScreen.getScreen(), 300, 200);
                primaryStage.setScene(homeScene);
                System.out.println("Inloggen gelukt!"); // Voegt een melding toe dat het inloggen gelukt is
            } else {
                System.out.println("Onjuiste gebruikersnaam of wachtwoord.");
            }
        });

    }
}