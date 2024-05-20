package com.example.bp2realisatie.classes.screens;

import com.example.bp2realisatie.classes.Database;
import com.example.bp2realisatie.classes.Doel;
import com.example.bp2realisatie.classes.Gebruiker;
import com.example.bp2realisatie.classes.Transactie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;

public class HomeScreen {
    private VBox root;
    private Gebruiker gebruiker;
    private Stage primaryStage;
    private Database database;
    private TableView<Transactie> transactieTableView;
    private ObservableList<Transactie> transacties;
    private TextField txtTransactie;
    private Label lblTotaalBedrag;
    private double totaalBedrag;
    private TextField txtBudget;
    private Label lblOvergeblevenBudget;
    private double budget;
    private Doel doel;
    private TextField txtDoel;
    private Label lblDoel;
    private boolean doelBereikt = false;
    private String gebruikersnaam;
    private Connection conn; // Voegt de databaseverbinding toe

    public HomeScreen(Stage primaryStage, Gebruiker gebruiker, Database database, String gebruikersnaam) {
        this.primaryStage = primaryStage;
        this.gebruiker = gebruiker;
        this.gebruikersnaam = gebruikersnaam;
        this.transacties = FXCollections.observableArrayList();
        this.database = database;
        this.totaalBedrag = 0;
        this.budget = 0;
        this.doel = new Doel("Vakantie", 500);

        // Haal de gebruikerId op
        int gebruikerId = database.haalGebruikerIdOp(gebruikersnaam);

        // Initialisatie van de databaseverbinding
        this.conn = database.getConnection();

        transactieTableView = new TableView<>();

        // Laadt bestaande transacties vanuit de database
        this.transacties = laadTransacties();
        transactieTableView.setItems(transacties);

        // Initialisatie van GUI-componenten
        Label lblTransactie = new Label("Transactie:");
        txtTransactie = new TextField();


        try {
            String transactieTekst = txtTransactie.getText();

            if (!transactieTekst.isEmpty()) {
                double transactieBedrag = NumberFormat.getInstance().parse(transactieTekst).doubleValue();
                // Rest van de code voor transactie verwerking
            } else {
                System.out.println("Voer een geldig bedrag in voor de transactie.");
            }
        } catch (ParseException | NumberFormatException ex) {
            System.out.println("Voer een geldig bedrag in voor de transactie.");
            ex.printStackTrace();
        }


        Label lblBudget = new Label("Budget:");
        txtBudget = new TextField();

        lblDoel = new Label("Doelbedrag: €" + doel.bedragProperty().get());
        txtDoel = new TextField();

        // Initialisatie van de root-container
        root = new VBox(10);

        // Voeg GUI-componenten toe aan de root-container
        root.getChildren().addAll(lblTransactie, txtTransactie, lblBudget, txtBudget, lblDoel, txtDoel);

        Button verwerkTransactieButton = new Button("Verwerk Transactie");
        verwerkTransactieButton.setOnAction(e -> verwerkTransactie());
        root.getChildren().add(verwerkTransactieButton);

        Button verwerkBudgetButton = new Button("Verwerk Budget");
        verwerkBudgetButton.setOnAction(e -> verwerkBudget());
        root.getChildren().add(verwerkBudgetButton);

        Button stelDoelInButton = new Button("Stel Doel In");
        stelDoelInButton.setOnAction(e -> stelDoelIn());
        root.getChildren().add(stelDoelInButton);

        TableColumn<Transactie, Double> bedragColumn = new TableColumn<>("Bedrag");
        bedragColumn.setCellValueFactory(cellData -> cellData.getValue().bedragProperty().asObject());

        transactieTableView.getColumns().add(bedragColumn);

        lblTotaalBedrag = new Label("Totaalbedrag: €" + totaalBedrag);
        lblOvergeblevenBudget = new Label("Overgebleven budget: €" + budget);

        root.getChildren().addAll(lblTotaalBedrag, lblOvergeblevenBudget, transactieTableView);

        database.setGebruikersnaam(gebruikersnaam);
    }

    public Parent getScreen() {
        return root;
    }


    // Event handler voor het verwerken van transactie
    private void verwerkTransactie() {
        try {
            // Bedrag ophalen uit de tekstveld
            double transactieBedrag = NumberFormat.getInstance().parse(txtTransactie.getText()).doubleValue();

            // Transactie opslaan in de database
            opslaanTransactie(transactieBedrag);

            // Transactie aan de lijst toevoegen en TableView bijwerken
            Transactie nieuweTransactie = new Transactie(transactieBedrag);
            transacties.add(nieuweTransactie);
            transactieTableView.setItems(transacties);
            // Totaalbedrag en doel bijwerken
            updateTotaalBedrag(transactieBedrag);
            doel.updateVoortgang(transactieBedrag);
            updateOvergeblevenBudget();

            // Tekstveld leegmaken
            txtTransactie.clear();
        } catch (ParseException | NumberFormatException ex) {
            System.out.println("Voer een geldig bedrag in voor de transactie.");
            ex.printStackTrace();
        }
    }

    private void verwerkBudget() {
        try {
            // Probeer het bedrag in te lezen met behulp van de standaard locale
            double budgetBedrag = NumberFormat.getInstance().parse(txtBudget.getText()).doubleValue();

            // Opslaan in de database
            opslaanBudget(budgetBedrag);

            budget += budgetBedrag;
            updateOvergeblevenBudget();

            txtBudget.clear();
        } catch (ParseException | NumberFormatException ex) {
            System.out.println("Voer een geldig bedrag in voor het budget.");
            ex.printStackTrace();
        }
    }

    private void stelDoelIn() {
        try {
            // Probeer het bedrag in te lezen met behulp van de standaard locale
            double doelBedrag = NumberFormat.getInstance().parse(txtDoel.getText()).doubleValue();

            // Opslaan in de database
            opslaanDoel(doelBedrag);

            doel.setBedrag(doelBedrag);
            lblDoel.setText("Doelbedrag: €" + doel.bedragProperty().get());

            doelBereikt = false;

            txtDoel.clear();
        } catch (ParseException | NumberFormatException ex) {
            System.out.println("Voer een geldig bedrag in voor het doel.");
            ex.printStackTrace();
        }
    }

    private void updateTotaalBedrag(double bedrag) {
        totaalBedrag += bedrag;
        String totaalBedragString = String.format("Totaalbedrag: €%.2f", totaalBedrag);
        lblTotaalBedrag.setText(totaalBedragString);
    }

    // Methode om overgebleven budget te berekenen
    private void updateOvergeblevenBudget() {
        double overgeblevenBudget = totaalBedrag - budget;
        String overgeblevenBudgetString = String.format("Overgebleven budget: €%.2f", overgeblevenBudget);
        lblOvergeblevenBudget.setText(overgeblevenBudgetString);

        // Checkt of het doel is bereikt
        if (overgeblevenBudget >= doel.getBedrag() && !doelBereikt) {
            System.out.println("Gefeliciteerd! Het doel is bereikt!");
            doelBereikt = true; // Markeert het doel als bereikt om herhaling te voorkomen
        }
    }

    // Methode om transactie op te slaan in de database
    private void opslaanTransactie(double bedrag) {
        try {
            // Haalt de gebruikerId op
            int gebruikerId = database.haalGebruikerIdOp(gebruikersnaam);

            PreparedStatement statement = conn.prepareStatement("INSERT INTO transactie (gebruiker_id, bedrag) VALUES (?, ?)");
            statement.setInt(1, gebruikerId);
            statement.setDouble(2, bedrag);
            statement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Methode om transacties te laden vanuit de database
    private ObservableList<Transactie> laadTransacties() {
        ObservableList<Transactie> transacties = FXCollections.observableArrayList();
        try {
            Statement stm = database.getConnection().createStatement(); // Hier krijg je de Connection van de Database
            ResultSet rs = stm.executeQuery("SELECT * FROM transactie");
            while (rs.next()) {
                double bedrag = rs.getDouble("bedrag");
                Transactie transactie = new Transactie(bedrag);
                transacties.add(transactie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transacties;
    }

}