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
                if (gebruiker.getGebruikersnaam().equals(usernameInput) && gebruiker.getWachtwoord().equals(passwordInput)) {
                    HomeScreen homeScreen = new HomeScreen(primaryStage, gebruiker, database, usernameInput);  // Geef gebruikersnaam door
                    Scene homeScene = new Scene(homeScreen.getScreen(), 800, 600);
                    primaryStage.setScene(homeScene);
                } else {
                    System.out.println("Onjuiste gebruikersnaam of wachtwoord.");
                }
            }
        });
    }
}
