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
        private Stage primaryStage; // De hoofd-Stage van de applicatie
        private Database database;
        private ObservableList<Transactie> transacties;// ObservableList voor het bijhouden van transacties
    private TableView<Transactie> transactieTableView; // TableView voor het weergeven van transacties
        private TextField txtTransactie;
        private String gebruikersnaam; // Gebruikersnaam van de huidige gebruiker
        double bedrag;
        private Label lblTotaalTransactieBedrag;
        private Gebruiker gebruiker; // De huidige gebruiker
        private Connection conn;

        public TransactieScreen(Stage primaryStage, Database database, String gebruikersnaam) {  // Voeg gebruikersnaam als parameter toe
            this.primaryStage = primaryStage;
            this.database = database;
            this.conn = database.getConnection();
            this.gebruikersnaam = gebruikersnaam; // Gebruikersnaam initialiseren
            this.transacties = FXCollections.observableArrayList();

            // Initialize UI components
            root = new VBox(10);// VBox met een tussenruimte van 10 pixels tussen kinderen
            root.setPadding(new Insets(10)); // Ruimte rondom de VBox

            // Terugknop naar het startscherm
            Button backButton = new Button("Terug naar Home");
            backButton.setOnAction(e -> {
                // Terug naar het startscherm
                primaryStage.setScene(new Scene(new HomeScreen(primaryStage, gebruiker, database, gebruikersnaam).getScreen()));
            });

            // Label en tekstveld voor het invoeren van een transactiebedrag
            Label lblTransactie = new Label("Transactiebedrag:");
            txtTransactie = new TextField();

            // Button voor het verwerken van een nieuwe transactie
            Button verwerkTransactieButton = new Button("Verwerk Transactie");
            verwerkTransactieButton.setOnAction(e -> verwerkTransactie());

            // Button voor het updaten van gekozen transactie
            Button updateButton = new Button("Update");
            updateButton.setOnAction(e -> updateTransactie());

            // Button voor het verwijderen van gekozen transactie
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteTransactie());

            // TableView voor het weergeven van transacties
            transactieTableView = new TableView<>();
            // Kolom voor het weergeven van het bedrag van de transactie
            TableColumn<Transactie, Double> bedragColumn = new TableColumn<>("Bedrag");
            // Koppel de waarde van de cellen aan de bedragProperty van Transactie
            bedragColumn.setCellValueFactory(cellData -> cellData.getValue().bedragProperty().asObject());
            transactieTableView.getColumns().add(bedragColumn); // Voeg de bedragkolom toe aan de TableView
            transactieTableView.setItems(transacties); // Zet de items in de TableView op basis van de transacties-lijst
            transactieTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);//1 item in tableview klikbaar maken

            // Laadt transacties bij het starten van het scherm
            transacties.addAll(database.laadTransacties());
            transactieTableView.setItems(transacties); // Werkt de TableView bij met de geladen transacties

            // Label voor het totaalbedrag van transacties
            lblTotaalTransactieBedrag = new Label("Totaalbedrag transacties: " + berekenTotaalTransactieBedrag());

            // Voeg de TableView toe aan een ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(transactieTableView);
            scrollPane.setFitToWidth(true); // Pas de breedte van de ScrollPane aan op de breedte van het scherm

            // Toevoegen van UI-componenten aan de VBox
            root.getChildren().addAll(backButton, lblTransactie, txtTransactie, verwerkTransactieButton, scrollPane, lblTotaalTransactieBedrag, updateButton, deleteButton);
        }

        public Parent getScreen() {
            return root;
        } // Geeft het transactiescherm terug

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

            // Probeert het bedrag in te lezen
            double transactieBedrag = NumberFormat.getInstance().parse(bedragString).doubleValue();

            // Functie aanroepen om transactie op te slaan in de database
            database.opslaanTransactie(transactieBedrag, gebruikersnaam);

            // Maak een nieuwe transactie aan met het ingevoerde bedrag
            Transactie nieuweTransactie = new Transactie();
            nieuweTransactie.setBedrag(transactieBedrag); // Instellen van het bedrag van de transactie

            // Voeg de nieuwe transactie toe aan de lijst
            transacties.add(nieuweTransactie);

            // Werk de TableView bij
            transactieTableView.setItems(transacties);

            // Werk het totaalbedrag bij
            lblTotaalTransactieBedrag.setText("Totaalbedrag transacties: " + berekenTotaalTransactieBedrag());

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

    // Methode om een geselecteerde transactie bij te werken
    private void updateTransactie() {
        // Haal de geselecteerde transactie op uit de TableView
        Transactie selectedTransactie = transactieTableView.getSelectionModel().getSelectedItem();
        // Controleer of er een transactie is geselecteerd
        if (selectedTransactie != null) {
            // Controleer of er een nieuw bedrag is ingevuld
            if (!txtTransactie.getText().isEmpty()) {
                try {
                    // Probeer het ingevoerde bedrag te parsen naar een double
                    double nieuwBedrag = Double.parseDouble(txtTransactie.getText());
                    // Werk het bedrag van de geselecteerde transactie bij
                    selectedTransactie.setBedrag(nieuwBedrag);
                    // Update de transactie in de database
                    database.updateTransactie(selectedTransactie.getId(), nieuwBedrag);
                    // Vernieuw de TableView om de wijzigingen weer te geven
                    transactieTableView.refresh();
                    // Werk het totaalbedrag bij
                    lblTotaalTransactieBedrag.setText("Totaalbedrag transacties: " + berekenTotaalTransactieBedrag());
                } catch (NumberFormatException e) {
                    System.out.println("Voer een geldig bedrag in.");
                }
            } else {
                System.out.println("Voer een nieuw bedrag in om de transactie bij te werken.");
            }
        } else {
            System.out.println("Selecteer een transactie om te bijwerken.");
        }
    }

    // Methode om een transactie te verwijderen
    private void deleteTransactie() {
        // Haal de geselecteerde transactie op uit de TableView
        Transactie selectedTransactie = transactieTableView.getSelectionModel().getSelectedItem();

        // Controleer of er een transactie is geselecteerd
        if (selectedTransactie != null) {
            transacties.remove(selectedTransactie); // Verwijder de geselecteerde transactie uit de lijst

            // Verwijder de transactie uit de database
            database.verwijderTransactie(selectedTransactie.getId());

            // Werk de TableView bij
            transactieTableView.setItems(transacties);

            // Werk het totaalbedrag bij
            lblTotaalTransactieBedrag.setText("Totaalbedrag transacties: " + berekenTotaalTransactieBedrag());
        } else {
            System.out.println("Selecteer een transactie om te verwijderen.");
        }
    }
}
