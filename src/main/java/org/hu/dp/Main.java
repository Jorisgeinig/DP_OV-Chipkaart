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

    private static void testOVChipkaarten(OVChipkaartDAO odao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        // Voorbeeld reiziger om de ovchipkaarten aan te koppelen
        Reiziger reiziger1 = new Reiziger(10, "K", "", "Kempers", java.sql.Date.valueOf("1995-03-14"));
        odao.getRdao().save(reiziger1);

        OVChipkaart ov1 = new OVChipkaart(76545, java.sql.Date.valueOf("2021-01-01"), 1, 40.00, reiziger1);
        OVChipkaart ov2 = new OVChipkaart(84958, java.sql.Date.valueOf("2022-01-01"), 1, 80, reiziger1);

        // Test save van ovchipkaart
        System.out.println("[TEST] eerst " + odao.findAll().size() + " ovchipkaarten");
        odao.save(ov1);
        System.out.println("na odao.save: " + odao.findAll().size() + " ovchipkaarten\n");

        // Koppel de ovchipkaarten aan de reiziger in java
        List<OVChipkaart> listOV = List.of(ov1, ov2);
        reiziger1.setOvChipkaartList(listOV);

        // Test update van ovchipkaart
        System.out.println("[TEST] update, ovchipkaart:\n " + odao.findbyKaartNummer(76545));
        OVChipkaart ov3 = new OVChipkaart(76545, java.sql.Date.valueOf("2022-01-01"), 2, 50, reiziger1);
        odao.update(ov3);
        System.out.println("na update: " + odao.findbyKaartNummer(76545));

        // Test findAll van ovchipkaart
        System.out.println("\n[TEST] findAll() geeft de volgende OVChipkaarten:\n");
        for (OVChipkaart ov : odao.findAll()) {
            System.out.println(ov.toString());
        }
        System.out.println();

        // Test findByReiziger van ovchipkaart
        System.out.println("[TEST] findByReiziger() geeft de volgende OVChipkaarten:");
        for (OVChipkaart ov : odao.findByReiziger(reiziger1)) {
            System.out.println(ov.toString());
        }

        // Test delete van ovchipkaart
        System.out.println("\n[TEST] delete, eerst " + odao.findAll().size() + " ovchipkaarten" );
      //  odao.getRdao().findById(ov1.getReiziger().getOvChipkaartList().remove(ov1));
        odao.delete(ov1);
        System.out.println("Na odao.delete: " + odao.findAll().size() + " ovchipkaarten\n");

        // delete de aangemaakte reiziger
        odao.getRdao().delete(reiziger1);
    }

    private static void testProductDAO(ProductDAO pdao) throws SQLException {
        System.out.println("\n---------- Test ProductDAO -------------");


        // Initialisatie van objecten om mee te testen.
        OVChipkaart ov7 = new OVChipkaart(77777, java.sql.Date.valueOf("2021-01-01"), 1, 50.00, pdao.getOdao().getRdao().findAll().get(0));
        
        // Slaat een nieuw product op in de database en koppelt deze aan de ovchipkaart
        Product product1 = new Product(7, "Weekend Vrij", "Gratis reizen in het weekend", 10.00);
        Product product2 = new Product(8, "Alleen staan", "Alleen staan in het ov", 5.00);
        ov7.addProduct(product1);
        ov7.addProduct(product2);
        product1.addOvChipkaart(ov7);
        product2.addOvChipkaart(ov7);
        pdao.getOdao().save(ov7);

        System.out.println("[TEST] eerst " + pdao.findAll().size() + " producten");
        pdao.save(product1);
        pdao.save(product2);
        System.out.println("na twee keer pdao.save: " + pdao.findAll().size() + " producten\n");

        // Test findByOVChipkaart van product
        System.out.println("[TEST] findByOVChipkaart() geeft de volgende producten:");
        System.out.println(pdao.findByOVChipkaart(ov7));


        // Test update van product
        System.out.println("\n[TEST] update, product:\n " + product2);
        Product product3 = new Product(product2.getProduct_nummer(), "Doordeweeks vrij", "Gratis reizen doordeweeks", 199.00);
        pdao.update(product3);
        System.out.println("na update: " + pdao.findByOVChipkaart(ov7).get(1));
        ;

        // Test findAll van product
        System.out.println("\n[TEST] findAll() geeft de volgende producten:\n");
        for (Product product : pdao.findAll()) {
            System.out.println(product.toString());
        }


        // Test delete van product
        System.out.println("\n[TEST] delete, eerst " + pdao.findAll().size() + " producten" );
        pdao.getOdao().delete(ov7);
        pdao.delete(product1);
        pdao.delete(product2);
        System.out.println("Na 2 keer pdao.delete: " + pdao.findAll().size() + " producten\n");

        // delete de aangemaakte ovchipkaart
        pdao.getOdao().delete(ov7);
    }



    public static void main(String[] args) throws SQLException {
        try {
            getConnection();
        } catch (SQLException e) {
            System.out.println("Er ging iets mis met het maken van de connectie..." + e);
        }

        ReizigerDAOPsql reizigerDAO = new ReizigerDAOPsql(connection);
        AdresDAOPsql adresDAO = new AdresDAOPsql(connection);
        OVChipkaartDAOPsql OvchipkaartDAO = new OVChipkaartDAOPsql(connection);
        ProductDAOsql productDAO = new ProductDAOsql(connection);

        reizigerDAO.setAdresDAO(adresDAO);
        adresDAO.setReizigerDAO(reizigerDAO);
        OvchipkaartDAO.setRdao(reizigerDAO);

        productDAO.setOdao(OvchipkaartDAO);
        OvchipkaartDAO.setPdao(productDAO);
        try {
//            testReizigerDAO(reizigerDAO);
            testAdres(adresDAO);
         //   testOVChipkaarten(OvchipkaartDAO);
           // testProductDAO(productDAO);
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