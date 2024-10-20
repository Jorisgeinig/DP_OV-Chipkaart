package org.hu.dp;

import java.sql.*;

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

    public static void main(String[] args) throws SQLException {
        try {
            getConnection();
        } catch (SQLException e) {
            System.out.println("Er ging iets mis met het maken van de connectie..." + e);
        }

        //kan in try en catch
        printReizigers(connection);
        try {
            closeConnection();
        } catch (SQLException e) {
            System.out.println("Er ging iets mis bij het sluiten van de connectie");
        }


    }

}