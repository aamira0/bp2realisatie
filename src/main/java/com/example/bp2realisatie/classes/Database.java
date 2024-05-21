package com.example.bp2realisatie.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;

public class Database {
    private Connection conn;
    private String gebruikersnaam;

    public Connection getConnection() {
        return conn;
    }

    public Database() {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/realisatie2",
                    "root", "");
            // Zet de automatische commit-modus uit
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Zorgt ervoor dat de tabellen bestaan (hieronder wordt ervan uitgegaan dat ze nog niet bestaan)
        createTables();
    }


    private void createTables() {
        try {
            Statement stm = conn.createStatement();

            // Tabel voor gebruikers
            stm.execute("CREATE TABLE IF NOT EXISTS gebruiker (id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "gebruikersnaam VARCHAR(255) NOT NULL, wachtwoord VARCHAR(255) NOT NULL)");

            // Tabel voor doelen
            stm.execute("CREATE TABLE IF NOT EXISTS doel (id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "gebruiker_id INT, bedrag DOUBLE, FOREIGN KEY (gebruiker_id) REFERENCES gebruiker(id))");

            // Tabel voor transacties
            stm.execute("CREATE TABLE IF NOT EXISTS transactie (id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "gebruiker_id INT, bedrag DOUBLE, FOREIGN KEY (gebruiker_id) REFERENCES gebruiker(id))");

            // Tabel voor budgetten
            stm.execute("CREATE TABLE IF NOT EXISTS budget (id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "gebruiker_id INT, bedrag DOUBLE, FOREIGN KEY (gebruiker_id) REFERENCES gebruiker(id))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Voegt gebruiker toe aan de database
    public boolean opslaanGebruiker(String gebruikersnaam, String wachtwoord) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO gebruiker (gebruikersnaam, wachtwoord) VALUES (?, ?)");
            statement.setString(1, gebruikersnaam);
            statement.setString(2, wachtwoord);
            statement.executeUpdate();
            conn.commit();
            return true; // Geeft true terug als de operatie succesvol is
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Geeft false terug als er een fout is opgetreden
        }
    }

    public ObservableList<Budget> haalBudgetOp(String gebruikersnaam) {
        ObservableList<Budget> budgetten = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM budget WHERE gebruiker_id = ?");
            statement.setInt(1, haalGebruikerIdOp(gebruikersnaam));
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                double bedrag = rs.getDouble("bedrag");
                Budget budget = new Budget("Naam van budget", bedrag); // Pas de naam van het budget aan zoals in jouw implementatie
                budgetten.add(budget);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budgetten;
    }


    // Methode om gebruikers op te halen vanuit de database
    public ObservableList<Gebruiker> haalAlleGebruikersOp() {
        ObservableList<Gebruiker> gebruikers = FXCollections.observableArrayList();
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM gebruiker");
            while (rs.next()) {
                int id = rs.getInt("id");
                String gebruikersnaam = rs.getString("gebruikersnaam");
                String wachtwoord = rs.getString("wachtwoord");
                Gebruiker gebruiker = new Gebruiker(id, gebruikersnaam, wachtwoord);
                gebruikers.add(gebruiker);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gebruikers;
    }

    public ObservableList<Transactie> laadTransacties() {
        ObservableList<Transactie> transacties = FXCollections.observableArrayList();
        try {
            Statement stm = conn.createStatement(); // Hier krijg je de Connection van de Database
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

    public int haalGebruikerIdOp(String gebruikersnaam) {
        int gebruikerId = -1;
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT id FROM gebruiker WHERE gebruikersnaam = ?");
            statement.setString(1, gebruikersnaam);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                gebruikerId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gebruikerId;
    }

    // Read
    public Gebruiker haalGebruikerOp(String gebruikersnaam) {
        Gebruiker gebruiker = null;
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM gebruiker WHERE gebruikersnaam = ?");
            statement.setString(1, gebruikersnaam);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String wachtwoord = resultSet.getString("wachtwoord");
                gebruiker = new Gebruiker(id, gebruikersnaam, wachtwoord);
                System.out.println("Inloggen: Gebruiker " + gebruikersnaam + " is ingelogd."); // Voeg dit logboekbericht toe
            } else {
                System.out.println("Inloggen: Gebruiker " + gebruikersnaam + " kon niet worden gevonden."); // Voeg dit logboekbericht toe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gebruiker;
    }

    // Delete
    public void verwijderGebruiker(String gebruikersnaam) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM gebruiker WHERE gebruikersnaam = ?");
            statement.setString(1, gebruikersnaam);
            statement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update
    public void updateWachtwoord(String gebruikersnaam, String nieuwWachtwoord) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE gebruiker SET wachtwoord = ? WHERE gebruikersnaam = ?");
            statement.setString(1, nieuwWachtwoord);
            statement.setString(2, gebruikersnaam);
            statement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }
}