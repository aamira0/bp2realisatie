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

            root.getChildren().addAll(backButton, lblTransactie, txtTransactie, verwerkTransactieButton, transactieTableView);
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

                // Functie aantoepen om transactie op te slaan in de database
                database.opslaanTransactie(bedrag, gebruikersnaam);

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
    }
