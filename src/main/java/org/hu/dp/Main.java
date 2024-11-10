package org.hu.dp;

import org.hu.dp.domain.*;

import java.sql.*;
import java.util.List;

public class Main {
    private static Connection connection;

    public static void getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        String user = "postgres";
        String password = "Joris999!";
        connection = DriverManager.getConnection(url, user, password);
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }

    public static void printReizigers(Connection connection) throws SQLException {
        String statementString = "SELECT * FROM reiziger";
        PreparedStatement preparedStatement = connection.prepareStatement(statementString);

        ResultSet resultSet = preparedStatement.executeQuery();

        System.out.println("Alle reizigers:");
        while (resultSet.next()) {
            System.out.println("#" + resultSet.getString(1)
            + ": " + resultSet.getString(2) //Logica wat betreft punt(en) bij voorletters doe ik in P2.
                    + " " + resultSet.getString(3)
                    + " " + resultSet.getString(4)
                    + " " + resultSet.getString(5));
        }
    }

    private static void testReizigerDAO(ReizigerDAO reizigerDAO) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = reizigerDAO.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(6, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        reizigerDAO.save(sietske);
        reizigers = reizigerDAO.findAll();
        System.out.println(reizigers.size() + " reizigers\n");


        // Maak nieuw reiziger, persisteer die en wijzig die vervolgens met een andere reiziger
        System.out.print("[Test] Eerst word er een nieuwe reiziger aangemaakt, op dit moment zijn er " + reizigers.size() + " reizigers");
        Reiziger wopke = new Reiziger(7, "W", "", "Hoekstra", java.sql.Date.valueOf("1976-04-20"));
        reizigerDAO.save(wopke);
        System.out.print("\nNu zijn er: " + reizigers.size() + " reizigers, na ReizigerDAO.save() , deze reiziger wordt gewijzigd: " + reizigerDAO.findById(7));
        Reiziger wopkeVervanger = new Reiziger(7, "S", "", "Kaag", java.sql.Date.valueOf("1966-03-19"));
        reizigerDAO.update(wopkeVervanger);
        System.out.print("\nNa ReizigerDAO.update() is de reiziger met id 7 geupdate naar: " + reizigerDAO.findById(7));

        Reiziger mark = new Reiziger(99, "M", "", "Rutte", java.sql.Date.valueOf("1967-02-10"));
        reizigerDAO.save(mark);
        reizigers = reizigerDAO.findAll();
        System.out.println("\n\n[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        reizigerDAO.delete(mark);
        reizigers = reizigerDAO.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        reizigerDAO.delete(wopke);
        reizigerDAO.delete(sietske);

        System.out.println("[TEST] Zoek reiziger(s) o.b.v geboortedatum: " + (reizigerDAO.findByGbdatum("2002-12-03")));
    }

    private static void testAdres(AdresDAO adresDAO) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");


        Reiziger wopke = new Reiziger(13, "H", "", "De Jonge", java.sql.Date.valueOf("1980-04-20"));
        Adres adr1 = new Adres(6, "8834IK", "60", "Japstraat", "Gouda", wopke);
        int aantal_adres = adresDAO.findAll().size();
        wopke.setAdres(adr1);
        adresDAO.getRdao().save(wopke);
        adresDAO.save(adr1);
        System.out.println("[Test] Eerst " + aantal_adres + " adressen na adresDAO.save zijn er " +adresDAO.findAll().size() + " adressen\n" );

        Adres adr2 = new Adres(6, "8451DF", "78", "Hogeweg", "Maastricht", wopke);
        System.out.println("[TEST] update, adres: " + adresDAO.findByReiziger(wopke));
        wopke.setAdres(adr2);
        adresDAO.update(adr2);
        System.out.println("na update: " + adresDAO.findByReiziger(wopke));

        System.out.println("\n[TEST] delete, eerst " + adresDAO.findAll().size() + " adressen" );
        adresDAO.delete(adr1);
        System.out.println("Na adao.delete: " + adresDAO.findAll().size() + " adressen\n");

        System.out.println("overzicht van alle adressen na de tests:");
        for (Adres adres : adresDAO.findAll()) {
            System.out.println(adres);
        }
        adresDAO.getRdao().delete(wopke);
    }



    public static void main(String[] args) throws SQLException {
        try {
            getConnection();
        } catch (SQLException e) {
            System.out.println("Er ging iets mis met het maken van de connectie..." + e);
        }

        ReizigerDAOPsql reizigerDAO = new ReizigerDAOPsql(connection);
        AdresDAOPsql adresDAO = new AdresDAOPsql(connection);
        reizigerDAO.setAdresDAO(adresDAO);
        adresDAO.setReizigerDAO(reizigerDAO);
        try {
            testReizigerDAO(reizigerDAO);
            testAdres(adresDAO);
        } catch (SQLException e) {
            System.out.println("Er ging iets fout bij het testen" );
            e.printStackTrace();
        }

        try {
            closeConnection();
        } catch (SQLException e) {
            System.out.println("Er ging iets mis bij het sluiten van de connectie");
        }


    }

}