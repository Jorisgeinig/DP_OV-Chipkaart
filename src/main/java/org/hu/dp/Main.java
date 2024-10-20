package org.hu.dp;

import org.hu.dp.domain.Reiziger;
import org.hu.dp.domain.ReizigerDAO;
import org.hu.dp.domain.ReizigerDAOPsql;

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

        System.out.println("[TEST] Zoek reiziger(s) o.b.v geboortedatum: " + (reizigerDAO.findByGbdatum("2002-12-03")));
    }


    public static void main(String[] args) throws SQLException {
        try {
            getConnection();
        } catch (SQLException e) {
            System.out.println("Er ging iets mis met het maken van de connectie..." + e);
        }

        //kan in try en catch
        //P1
        //printReizigers(connection);

        ReizigerDAO reizigerDAO = new ReizigerDAOPsql(connection);
        testReizigerDAO(reizigerDAO);

        try {
            closeConnection();
        } catch (SQLException e) {
            System.out.println("Er ging iets mis bij het sluiten van de connectie");
        }


    }

}