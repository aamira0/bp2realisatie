package com.example.bp2realisatie.classes.screens;

import com.example.bp2realisatie.classes.Database;
import com.example.bp2realisatie.classes.Gebruiker;
import com.example.bp2realisatie.classes.Transactie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;

public class TransactieScreen {
        private VBox root;
        private Stage primaryStage;
        private Database database;
        private ObservableList<Transactie> transacties;
        private TableView<Transactie> transactieTableView;
        private TextField txtTransactie;
        private String gebruikersnaam;
        double bedrag;
        private Label lblTotaalTransactieBedrag;
        private Gebruiker gebruiker;
        private Connection conn;

        public TransactieScreen(Stage primaryStage, Database database, String gebruikersnaam) {  // Voeg gebruikersnaam als parameter toe
            this.primaryStage = primaryStage;
            this.database = database;
            this.conn = database.getConnection();
            this.gebruikersnaam = gebruikersnaam;  // Sla de gebruikersnaam op

            this.transacties = FXCollections.observableArrayList();

            // Initialize UI components
            root = new VBox(10);
            root.setPadding(new Insets(10));

            Button backButton = new Button("Terug naar Home");
            backButton.setOnAction(e -> {
                // Terug naar het startscherm
                primaryStage.setScene(new Scene(new HomeScreen(primaryStage, gebruiker, database, gebruikersnaam).getScreen()));
            });

            Label lblTransactie = new Label("Transactiebedrag:");
            txtTransactie = new TextField();

            Button verwerkTransactieButton = new Button("Verwerk Transactie");
            verwerkTransactieButton.setOnAction(e -> verwerkTransactie());

            transactieTableView = new TableView<>();
            TableColumn<Transactie, Double> bedragColumn = new TableColumn<>("Bedrag");
            bedragColumn.setCellValueFactory(cellData -> cellData.getValue().bedragProperty().asObject());
            transactieTableView.getColumns().add(bedragColumn);
            transactieTableView.setItems(transacties);

            // Laadt transacties bij het starten van het scherm
            transacties.addAll(database.laadTransacties());
            transactieTableView.setItems(transacties); // Werkt de TableView bij met de geladen transacties

            // Label voor het totaalbedrag van transacties
            lblTotaalTransactieBedrag = new Label("Totaalbedrag transacties: " + berekenTotaalTransactieBedrag());

            // Voeg de TableView toe aan een ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(transactieTableView);
            scrollPane.setFitToWidth(true); // Pas de breedte van de ScrollPane aan op de breedte van het scherm

            root.getChildren().addAll(backButton, lblTransactie, txtTransactie, verwerkTransactieButton, scrollPane, lblTotaalTransactieBedrag);
        }

        public Parent getScreen() {
            return root;
        }

        // Verwerken van transactie
        private void verwerkTransactie() {
            try {
                // Bedrag ophalen uit het tekstveld
                String bedragString = txtTransactie.getText();

                // Controleren of het tekstveld leeg is
                if (bedragString.isEmpty()) {
                    System.out.println("Voer een bedrag in voor de transactie.");
                    return;
                }

                //Probeert het bedrag in te lezen
                double transactie = NumberFormat.getInstance().parse(bedragString).doubleValue();

                // Functie aanroepen om transactie op te slaan in de database
                database.opslaanTransactie(transactie, gebruikersnaam);

                // Transactie aan de lijst toevoegen en TableView bijwerken
                Transactie nieuweTransactie = new Transactie(transactie);
                transacties.add(nieuweTransactie);
                transactieTableView.setItems(transacties);

                // Tekstveld leegmaken
                txtTransactie.clear();
            } catch (ParseException | NumberFormatException ex) {
                System.out.println("Voer een geldig bedrag in voor de transactie.");
                ex.printStackTrace();
            }
        }
    // Methode om het totaalbedrag van alle transacties te berekenen
    private double berekenTotaalTransactieBedrag() {
        // Variabele om het totaalbedrag bij te houden, start met nul
        double totaalBedrag = 0.0;

        // Loopt door alle transacties en tel het bedrag van elke transactie op bij het totaalbedrag
        for (Transactie transactie : transacties) {
            totaalBedrag += transactie.getBedrag(); // Bedrag van de huidige transactie toevoegen aan het totaalbedrag
        }

        // Het berekende totaalbedrag retourneren
        return totaalBedrag;
    }
}
