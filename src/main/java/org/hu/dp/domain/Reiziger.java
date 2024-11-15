package org.hu.dp.domain;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Reiziger {
    private int reiziger_id;
    private String voorletters;
    private String tussenvoegsel = null;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres;
    private List<OVChipkaart> ovChipkaartList = new ArrayList<>();

    public Reiziger(){
    }

    public Reiziger(int reiziger_id, String voorletters,
                    String achternaam, Date geboortedatum) {
        this.reiziger_id = reiziger_id;
        this.achternaam = achternaam;
        this.voorletters = voorletters;
        this.geboortedatum = geboortedatum;
    }

    public Reiziger(int reiziger_id, String voorletters,
                    String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.reiziger_id = reiziger_id;
        this.achternaam = achternaam;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.geboortedatum = geboortedatum;
    }

    public int getReiziger_id() {
        return reiziger_id;
    }

    public void setReiziger_id(int reiziger_id) {
        this.reiziger_id = reiziger_id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Adres getAdres() {
        return this.adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setOvChipkaartList(List<OVChipkaart> ovChipkaartList) {
        this.ovChipkaartList = ovChipkaartList;
    }

    public List<OVChipkaart> getOvChipkaartList() {
        return ovChipkaartList;
    }

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Choose the desired date format
        String formattedDate = dateFormat.format(geboortedatum);

        // Logica om de punten (.) bij de voorletter goed neer te zetten.
        String voorl_punt = "";
        for (int i = 0; i < voorletters.length(); i++) {
            voorl_punt += voorletters.charAt(i);

            if (i < voorletters.length() - 1) {
                voorl_punt += ".";
            }
        }
        if (voorletters.length() > 1) {
            voorl_punt += ".";
        }
        if (voorletters.length() == 1) {
            voorl_punt += ".";
        }

        return String.format("#%d %s%s %s (%s)",
                reiziger_id,
                voorl_punt,
                (tussenvoegsel != null) ? " " + tussenvoegsel : "",
                achternaam,
                formattedDate);
    }
}
