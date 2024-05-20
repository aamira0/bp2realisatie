package com.example.bp2realisatie;

import com.example.bp2realisatie.classes.Database;
import com.example.bp2realisatie.classes.Gebruiker;
import com.example.bp2realisatie.classes.screens.HomeScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private Stage primaryStage;
    private Gebruiker gebruiker;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Simulatie van ingelogde gebruiker
        gebruiker = new Gebruiker("John", "1234");

        Database database = new Database();

        String gebruikersnaam = "John";
        String wachtwoord = "1234";

        if (database.haalGebruikerIdOp(gebruikersnaam) == -1) {
            database.opslaanGebruiker(gebruikersnaam, wachtwoord);
        }

        //Voeg een nieuw gebruiker toe
        String nieuweGebruikersnaam = "nieuweGebruiker";
        String nieuwWachtwoord = "nieuwWachtwoord";
        database.opslaanGebruiker(nieuweGebruikersnaam, nieuwWachtwoord);

        // Haal gebruiker op
        String bestaandeGebruikersnaam = "bestaandeGebruiker";
        Gebruiker gevondenGebruiker = database.haalGebruikerOp(bestaandeGebruikersnaam);

        // Werk het wachtwoord van de bestaande gebruiker bij
        String teUpdatenGebruikersnaam = "bestaandeGebruiker";
        String nieuwWachtwoordVoorUpdate = "nieuwWachtwoord";
        database.updateWachtwoord(teUpdatenGebruikersnaam, nieuwWachtwoordVoorUpdate);

        // Verwijder gebruiker
        String teVerwijderenGebruiker = "teVerwijderenGebruiker";
        database.verwijderGebruiker(teVerwijderenGebruiker);

        if (gevondenGebruiker != null) {
            System.out.println("Gevonden gebruiker: " + gevondenGebruiker.getGebruikersnaam());
        } else {
            System.out.println("Gebruiker niet gevonden.");
        }

        Label lblGebruikersnaam = new Label("Gebruikersnaam:");
        Label lblWachtwoord = new Label("Wachtwoord:");

        TextField txtGebruikersnaam = new TextField();
        TextField txtWachtwoord = new TextField();
        Button inlogButton = new Button("Inloggen");

        VBox inlogVBox = new VBox(10, lblGebruikersnaam, txtGebruikersnaam, lblWachtwoord, txtWachtwoord, inlogButton);

        // Laat alleen het inlogscherm zien bij het starten
        Scene scene = new Scene(inlogVBox, 800, 600);

        primaryStage.setTitle("Inlogscherm");
        primaryStage.setScene(scene);
        primaryStage.show();

        inlogButton.setOnAction(e -> {
            String usernameInput = txtGebruikersnaam.getText();
            String passwordInput = txtWachtwoord.getText();

            // Hier kun je dynamisch gebruikersnamen en wachtwoorden accepteren
            if (!usernameInput.isEmpty() && !passwordInput.isEmpty()) {
                System.out.println("Input Username: " + usernameInput);
                System.out.println("Input Password: " + passwordInput);
                System.out.println("Stored Username: " + gebruiker.getGebruikersnaam());
                System.out.println("Stored Password: " + gebruiker.getWachtwoord());

                // In plaats van een harde codering, controleert tegen de ingevoerde gegevens
                if (gebruiker.getGebruikersnaam().equals(usernameInput) && gebruiker.getWachtwoord().equals(passwordInput)) {
                    // Maak een nieuwe instantie van HomeScreen na succesvol inloggen
                    HomeScreen homeScreen = new HomeScreen(primaryStage, gebruiker, database, usernameInput);

                    // Vervangt het inlogscherm door het startscherm
                    primaryStage.setScene(new Scene(homeScreen.getScreen(), 800, 600));
                } else {
                    System.out.println("Inloggen mislukt. Controleer je gegevens.");
                }
            } else {
                System.out.println("Vul beide velden in om in te loggen.");
            }
        });
    }

}
