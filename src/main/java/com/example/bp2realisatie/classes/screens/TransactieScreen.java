package com.example.bp2realisatie.classes.screens;

import com.example.bp2realisatie.classes.Database;
import com.example.bp2realisatie.classes.Transactie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
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
        private String gebruikersnaam;  // Voeg deze regel toe
        private Connection conn;

        public TransactieScreen(Database database, String gebruikersnaam) {  // Voeg gebruikersnaam als parameter toe
            this.database = database;
            this.conn = database.getConnection();
            this.gebruikersnaam = gebruikersnaam;  // Sla de gebruikersnaam op

            this.transacties = FXCollections.observableArrayList();

            // Initialize UI components
            root = new VBox(10);
            root.setPadding(new Insets(10));

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

            root.getChildren().addAll(lblTransactie, txtTransactie, verwerkTransactieButton, transactieTableView);
        }

        public Parent getScreen() {
            return root;
        }

        // Voegt transactie toe aan de database
        public void opslaanTransactie(double bedrag) {
            try {
                int gebruikerId = database.haalGebruikerIdOp(gebruikersnaam);

                System.out.println("Transactie wordt opgeslagen voor gebruiker met ID " + gebruikerId + ", Bedrag: " + bedrag + " euro."); // Laat ons weten dat we bezig zijn met het opslaan van een transactie

                PreparedStatement statement = conn.prepareStatement("INSERT INTO transactie (gebruiker_id, bedrag) VALUES (?, ?)");
                statement.setInt(1, gebruikerId);
                statement.setDouble(2, bedrag);
                statement.executeUpdate();
                conn.commit();
                System.out.println("Transactie met succes opgeslagen!"); // Vier de triomf van het opslaan van de transactie
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Er is een fout opgetreden bij het opslaan van de transactie."); // Benadruk het ongeluk als er een fout optreedt
            }
        }

        // Event handler voor het verwerken van transactie
        private void verwerkTransactie() {
            try {
                // Bedrag ophalen uit de tekstveld
                double transactie = NumberFormat.getInstance().parse(txtTransactie.getText()).doubleValue();

                // Transactie opslaan in de database
                opslaanTransactie(transactie);

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
