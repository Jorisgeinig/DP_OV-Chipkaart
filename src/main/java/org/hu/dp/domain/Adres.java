package org.hu.dp.domain;

import java.text.SimpleDateFormat;

public class Adres {
    private int adres_id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private Reiziger reiziger;

    public Adres(int adres_id, String postcode, String huisnummer, String straat, String woonplaats, Reiziger reiziger) {
        this.adres_id = adres_id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reiziger = reiziger;
    }

    public int getAdres_id() {
        return adres_id;
    }

    public void setAdres_id(int adres_id) {
        this.adres_id = adres_id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Choose the desired date format
        String formattedDate = dateFormat.format(reiziger.getGeboortedatum());


        String voorletters_metpunt = "";
        for (int i = 0; i < reiziger.getVoorletters().length(); i++) {
            voorletters_metpunt += reiziger.getVoorletters().charAt(i);

            if (i < reiziger.getVoorletters().length() - 1) {
                voorletters_metpunt += ".";
            }
        }
        if (reiziger.getVoorletters().length() > 1) {
            voorletters_metpunt += ".";
        }
        if (reiziger.getVoorletters().length() == 1) {
            voorletters_metpunt += ".";
        }

        String resultaat = String.format("Reiziger #%d %s%s %s (%s), Adres {#%d %s %s}",
                reiziger.getReiziger_id(),
                voorletters_metpunt,
                (reiziger.getTussenvoegsel() != null) ? " " + reiziger.getTussenvoegsel() : "",
                reiziger.getAchternaam(),
                formattedDate,
                adres_id,
                postcode,
                huisnummer);
        return resultaat;
    }
}
