import com.example.bp2realisatie.classes.Database;
import com.example.bp2realisatie.classes.Gebruiker;
import com.example.bp2realisatie.classes.screens.HomeScreen;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {
    private Database database;
    private Gebruiker gebruiker;

    @BeforeEach
    void setUp() {
        // Initialisatie van database en gebruiker voor elke test
        database = new Database();
        gebruiker = new Gebruiker(1, "John", "1234");
    }

    @Test
    void testCorrectLogin() {
        // Testgeval 1: Testen van correct inloggen vaste gebruiker
        assertTrue(database.haalGebruikerIdOp("John") == 1);
    }

    @Test
    void testIncorrectLogin() {
        // Testgeval 2: Testen van incorrect inloggen
        assertTrue(database.haalGebruikerIdOp("Alice") == -1);
    }

    @Test
    void testTransactieToevoegen() {
        // Testgeval 3: Testen van het toevoegen van een transactie
        database.opslaanGebruiker("John", "1234"); // Gebruiker toevoegen voor de test
        int gebruikerId = database.haalGebruikerIdOp("John");
        assertTrue(gebruikerId != -1);

        // Het aanmaken van een transactie
        database.opslaanTransactie(100.0, "John");

        // Het ophalen van alle transacties van de gebruiker
        int aantalTransacties = database.laadTransacties().size();

        // Controleren of de transactie is toegevoegd
        assertTrue(aantalTransacties > 0);
    }
}
