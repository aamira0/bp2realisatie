# Persoonlijk Financieel Beheersysteem

Dit Persoonlijk Financieel Beheersysteem is een eenvoudige Java-toepassing ontwikkeld om gebruikers te helpen bij het beheren van hun financiën. Met dit systeem kunnen gebruikers transacties bijhouden, budgetten instellen en doelen stellen om hun financiële doelstellingen te bereiken.

## Inhoudsopgave

1. [Inleiding](#inleiding)
2. [Implementatieplan](#implementatieplan)
3. [Staat](#staat)
4. [Communicatie](#communicatie)
5. [Installatie](#installatie)
6. [Gebruik](#gebruik)
7. [Licentie](#licentie)

## Inleiding

Dit Persoonlijk Financieel Beheersysteem is gebouwd met Java en JavaFX voor de grafische gebruikersinterface. Het maakt gebruik van een SQL-database om gebruikersgegevens op te slaan, zoals transacties, budgetten en doelen.

## Implementatieplan

Aan het begin had ik de basis functionaliteiten van elke scherm, ik kon bedragen toevoegen. Alleen was het erg rommelig. Het plan van aanpak was om eerst voor meerdere schermen te zorgen. Er bestaan nu knoppen op het homescherm om naar verschillende schermen te navigeren. Hier had ik naast het transactiescherm voornamelijk aan gewerkt eerst. Hierbij had ik er ook voor gezorgd dat alles van database niet meer op de front end zat. Op het transactiescherm kan de gebruiker nu bedragen (inkomen en afschrijvingen) invoeren die in het tabel komen te staan. Daarna focuste ik op het tonen van de ingevoerde bedragen op het doel- en budgetscherm. Ik ging me daarna focussen op het tonen van een totaalbedrag bij de transacties en om ervoor te zorgen dat de budget en doelbedragen van de laatst ingevoerde bedragen op het scherm bleven. Ik voegde ook de functie om een naam voor de bedragen bij het budget en doel toe te voegen. Uiteindelijk focuste ik me op het toevoegen van een update en verwijder optie bij de transacties. Nadat dit werkte zorgde ik ervoor dat deze samen met het totaalbedrag meteen update op het scherm na het invoeren van nieuwe gegevens. Ik heb uiteindelijk besloten om de functies voor het budget en doel simpel te houden.

## Staat

Het huidige staat van het systeem zorgt ervoor dat de gebruiker transacties kan toevoegen, updaten en verwijderen. Ook toont het de totaalbedrag bij de transacties. Verder kan de gebruiker een budget en doel toevoegen om zelf bij te houden.

## Communicatie

Er was geen sprake van communicatie tussen de gebruiker en opdrachtgever. Alles dat ik voor de eisen van het systeem moest weten kwam van de hoofd Ontwikkelteam af. Hierbij waren er frequente live gesprekken voor feedback. Op het einde heb ik testen uitgevoerd en mijn resultaten met het Ontwikkelteam gedeeld.

## Installatie

1. **Installatie**: Clone de repository: git clone https://github.com/aamira0/bp2realisatie.git
2. **Navigeer**: Navigeer naar het project directory
3. **Voer de applicatie uit**: Gebruik Java om het uit te voeren.

## Gebruik

1. **Inloggen**: Log in met het bestaande gebruikersnaam en wachtwoord.
2. **Navigeer**: Navigeer via de homescherm en de terugknoppen op elke scherm.
3. **Transacties**: Voeg nieuwe transacties toe, update bestaande transacties of verwijder transacties.
4. **Budgetten**: Stel een budget en naam in om uw uitgaven bij te houden.
5. **Doelen**: Stel een financieel doel en naam in om uw voortgang bij te houden.
6. **Afsluiten**: Sluit het programma door op het kruisje rechts bovenin het venster te klikken.

## Roadmap
1. **Budget**: Voeg een methode toe om het overgebleven totaalbedrag te tonen. 
2. **Doel**: Voeg een methode toe om aan te geven wanneer een doel is bereikt.
3. **Uitloggen**: Voeg een uitlogoptie toe.

## Licentie

Dit project is gemaakt voor Ad.