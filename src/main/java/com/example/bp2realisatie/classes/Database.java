package com.example.bp2realisatie.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class Database {
    private Connection conn; // Connection-object om de databaseverbinding te vertegenwoordigen

    public Connection getConnection() {
        return conn; // Geeft de databaseverbinding terug.
    }

    public Database() {
        try { // Connectie maken met de externe database
            this.conn = DriverManager.getConnection("jdbc:mysql://adainforma.tk:3306/bp3_politieapp_ana_amira",
                    "politieapp_ana_amira", "9%92iBz9b");
            // Zet de automatische commit-modus uit
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Zorgt ervoor dat de tabellen bestaan (hieronder wordt ervan uitgegaan dat ze nog niet bestaan)
        createTables();
    }


    private void createTables() { //Creëert de vereiste tabellen als ze nog niet bestaan.
        try {
            Statement stm = conn.createStatement();

            // Tabel voor gebruikers
            stm.execute("CREATE TABLE IF NOT EXISTS gebruiker (id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "gebruikersnaam VARCHAR(255) NOT NULL, wachtwoord VARCHAR(255) NOT NULL)");

            // Tabel voor doelen
            stm.execute("CREATE TABLE IF NOT EXISTS doel (id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "gebruiker_id INT, bedrag DOUBLE, naam VARCHAR(255), " +
                    "FOREIGN KEY (gebruiker_id) REFERENCES gebruiker(id))");

            // Tabel voor transacties
            stm.execute("CREATE TABLE IF NOT EXISTS transactie (id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "gebruiker_id INT, bedrag DOUBLE, FOREIGN KEY (gebruiker_id) REFERENCES gebruiker(id))");

            // Tabel voor budgetten
            stm.execute("CREATE TABLE IF NOT EXISTS budget (id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "gebruiker_id INT, bedrag DOUBLE, naam VARCHAR(255), " +
                    "FOREIGN KEY (gebruiker_id) REFERENCES gebruiker(id))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Transacties opslaan
    public void opslaanTransactie(double bedrag, String gebruikersnaam) {
        try {
            // Haal het gebruiker-ID op basis van de gebruikersnaam
            int gebruikerId = haalGebruikerIdOp(gebruikersnaam);

            // Bereid een SQL-insert statement voor om de transactie op te slaan
            PreparedStatement statement = conn.prepareStatement("INSERT INTO transactie (gebruiker_id, bedrag) VALUES (?, ?)");
            statement.setInt(1, gebruikerId);
            statement.setDouble(2, bedrag);
            statement.executeUpdate(); // Voer het SQL-insert statement uit
            conn.commit(); // Bevestig de wijzigingen in de database
            System.out.println("Transactie met succes opgeslagen!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Er is een fout opgetreden bij het opslaan van de transactie.");
        }
    }

    //Transacties ophalen
    public ObservableList<Transactie> laadTransacties() {
        // Maak een nieuwe ObservableList om de transacties op te slaan
        ObservableList<Transactie> transacties = FXCollections.observableArrayList();
        try {
            // Creëer een statement-object om SQL-query's uit te voeren
            Statement stm = conn.createStatement();
            // Voer een SQL-select statement uit om alle transacties op te halen
            ResultSet rs = stm.executeQuery("SELECT * FROM transactie");
            while (rs.next()) { // Gaat door de rijen van resulsets
                // Haal de id en het bedrag van elke transactie op uit het resultSet
                int id = rs.getInt("id");
                double bedrag = rs.getDouble("bedrag");
                // Maak een nieuw Transactie-object met de opgehaalde id en bedrag
                Transactie transactie = new Transactie(id, bedrag);
                // Voeg het Transactie-object toe aan de ObservableList
                transacties.add(transactie);
            }
        } catch (SQLException e) {
            // Vang eventuele SQL-fouten op en geef false terug om aan te geven dat het opslaan niet is gelukt
            e.printStackTrace();
        }
        return transacties; // Geef de ObservableList met transacties terug
    }

    // Methode om een transactie bij te werken in de database
    public boolean updateTransactie(int transactieId, double nieuwBedrag) {
        try {
            // Bereid een SQL-update voor om het bedrag van de transactie bij te werken op basis van de transactie-ID
            PreparedStatement statement = conn.prepareStatement("UPDATE transactie SET bedrag = ? WHERE id = ?");
            // Voeg het nieuwe bedrag en de transactie-ID toe als parameters aan de SQL-query
            statement.setDouble(1, nieuwBedrag);
            statement.setInt(2, transactieId); //ID gekozen transactie
            // Voer de update uit en haal het aantal bijgewerkte rijen op
            int rowsUpdated = statement.executeUpdate();
            // Bevestig de wijzigingen in de database
            conn.commit();
            // Geef true terug als er ten minste één rij is bijgewerkt, anders false
            return rowsUpdated > 0;
        } catch (SQLException e) {
            // Vang eventuele SQL-fouten op en geef false terug om aan te geven dat de update niet is gelukt
            e.printStackTrace();
            return false;
        }
    }

    // Methode om een transactie uit de database te verwijderen
    public boolean verwijderTransactie(int transactieId) {
        try {
            // Bereid een SQL-delete voor om het bedrag van de transactie bij te verwijderen
            PreparedStatement statement = conn.prepareStatement("DELETE FROM transactie WHERE id = ?");
            statement.setInt(1, transactieId); // Gekozen transactie-ID
            // Voer de update uit en haal het aantal bijgewerkte rijen op
            int rowsDeleted = statement.executeUpdate();
            conn.commit(); // Bevestig de wijzigingen in de database
            return rowsDeleted > 0; // Geeft true terug als de transactie succesvol is verwijderd
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Geeft false terug als er een fout optreedt tijdens het verwijderen
        }
    }

    //Doel opslaan
    public boolean opslaanDoel(String naam, double bedrag, int gebruikerId) {
        try { // Maakt een SQL-insert statement voor om het doel op te slaan
            PreparedStatement statement = conn.prepareStatement("INSERT INTO doel (gebruiker_id, naam, bedrag) VALUES (?, ?, ?)");
            statement.setInt(1, gebruikerId);
            statement.setString(2, naam);
            statement.setDouble(3, bedrag);
            statement.executeUpdate(); // Voer het SQL-insert statement uit
            conn.commit(); // Bevestig de wijzigingen in de database
            return true; // Geeft true terug als de operatie succesvol is
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Geeft false terug als er een fout is opgetreden
        }
    }


    // Doelen ophalen
    public ObservableList<Doel> haalDoelenOp(String gebruikersnaam) {
        // Maak een nieuwe ObservableList om de doelen op te slaan
        ObservableList<Doel> doelen = FXCollections.observableArrayList();
        try { // Maakt een SQL-select statement voor om de doelen op te halen op basis van het gebruiker-ID
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM doel WHERE gebruiker_id = ?");
            statement.setInt(1, haalGebruikerIdOp(gebruikersnaam));
            ResultSet rs = statement.executeQuery(); // Voer het SQL-select statement uit

            while (rs.next()) { // Gaat door de rijen van resulsets
                // Haal het bedrag en de naam van elk doel op uit het resultSet
                double bedrag = rs.getDouble("bedrag");
                String naam = rs.getString("naam");
                Doel doel = new Doel(naam, bedrag); // Maak een nieuw Doel-object met de opgehaalde naam en bedrag
                doelen.add(doel); // Voeg het Doel-object toe aan de ObservableList
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doelen; // Geef de ObservableList met doelen terug
    }


    // Budget opslaan
    public boolean opslaanBudget(String naam, double bedrag, int gebruikerId) {
        try { // Bereid een SQL-insert statement voor om het budget op te slaan
            PreparedStatement statement = conn.prepareStatement("INSERT INTO budget (bedrag, naam, gebruiker_id) VALUES (?, ?, ?)");
            statement.setDouble(1, bedrag);
            statement.setString(2, naam);
            statement.setInt(3, gebruikerId);
            statement.executeUpdate(); // Voer het SQL-insert statement uit
            conn.commit(); // Bevestig de wijzigingen in de database
            return true; // Geeft true terug als de operatie succesvol is
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Geeft false terug als er een fout is opgetreden
        }
    }

    //Budget ophalen
    public ObservableList<Budget> haalBudgetOp(String gebruikersnaam) {
        // Maak een nieuwe ObservableList om de budgetten op te slaan
        ObservableList<Budget> budgetten = FXCollections.observableArrayList();
        try {  // Bereid een SQL-select statement voor om de budgetten op te halen op basis van het gebruiker-ID
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM budget WHERE gebruiker_id = ?");
            statement.setInt(1, haalGebruikerIdOp(gebruikersnaam));
            ResultSet rs = statement.executeQuery();

            while (rs.next()) { // Gaat door de rijen van resulsets
                // Haal het bedrag en de naam van elk budget op uit het resultSet
                double bedrag = rs.getDouble("bedrag");
                String naam = rs.getString("naam");
                Budget budget = new Budget(naam, bedrag); // Maak een nieuw Budget-object met de opgehaalde naam en bedrag
                budgetten.add(budget); // Voeg het Budget-object toe aan de ObservableList
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budgetten; // Geef de ObservableList met budgetten terug
    }


    // Gebruiker opslaan
    public boolean opslaanGebruiker(String gebruikersnaam, String wachtwoord) {
        try { // Bereid een SQL-insert statement voor om de gebruiker op te slaan
            PreparedStatement statement = conn.prepareStatement("INSERT INTO gebruiker (gebruikersnaam, wachtwoord) VALUES (?, ?)");
            statement.setString(1, gebruikersnaam);
            statement.setString(2, wachtwoord);
            statement.executeUpdate(); // Voer het SQL-insert statement uit
            conn.commit(); // Bevestig de wijzigingen in de database
            return true; // Geeft true terug als de operatie succesvol is
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Geeft false terug als er een fout is opgetreden
        }
    }

    // GebruikerID ophalen
    public int haalGebruikerIdOp(String gebruikersnaam) {
        int gebruikerId = -1; // Initialiseert gebruikerId met -1 (niet gevonden)
        try { // Bereid een SQL-select statement voor om het gebruiker-ID op te halen op basis van de gebruikersnaam
            PreparedStatement statement = conn.prepareStatement("SELECT id FROM gebruiker WHERE gebruikersnaam = ?");
            statement.setString(1, gebruikersnaam);
            ResultSet resultSet = statement.executeQuery(); // Voer het SQL-select statement uit

            if (resultSet.next()) {
                gebruikerId = resultSet.getInt("id"); // Haal het gebruiker-ID op uit het resultSet
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gebruikerId; // Geef het gevonden gebruiker-ID terug of -1 als het niet is gevonden
    }
}