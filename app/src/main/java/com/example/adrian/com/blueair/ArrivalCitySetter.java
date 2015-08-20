package com.example.adrian.com.blueair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * aqui se cuecen los destinos on 22/09/14.
 */
public class ArrivalCitySetter {

    public static Map<String, String> iataCodes = new HashMap<String, String>();
    // devolvemos codul iata dupa numele aeroportului

    // declar o variabila care o sa tina toate orasele, apoi o populez.
    ArrayList<String[]> cityArray = new ArrayList<String[]>();

    public  String[] getArrivalCity(int depCity) {

        // vacio
        final String[] FIRST = {"Oras sosire"};
        cityArray.add(FIRST);

        // Antalya
        final String[] AYT = {"Bucuresti-Otopeni"};
        cityArray.add(AYT);

        // Bacau
        final String[] BCM  = {"Bologna-Marconi", "Bruxelles", "Dublin",
                "Londra-Luton", "Liverpool-John-Lennon",
                "Milano-Bergamo", "Paris-Beveauis",
                "Roma-Fiumicino","Torino-Cuneo"};
        cityArray.add(BCM);

        // Barcelona
        final String[] BCN = {"Bucuresti-Otopeni"};
        cityArray.add(BCN);

        // Bolognia
        final String[] BLQ = {"Bacau", "Bucuresti-Otopeni"};
        cityArray.add(BLQ);

        // Brusellas
        final String[] BRU = {"Bacau", "Bucuresti-Otopeni"};
        cityArray.add(BRU);

        // Bucuresti-Otopeni
        final String[] OTP = {"Barcelona-Prat","Bologna-Marconi","Bruxelles","Catania-Fontanarossa",
                               "Dublin","Iasi",
                               "Koln","Larnaca","Lisabona","Londra-Luton","Madrid-Barajas",
                               "Malaga","Milano-Bergamo", "Milano-Linate", "Napoli",
                               "Nice","Liverpool-John-Lennon", "Paris-Beveauis","Roma-Fiumicino",
                               "Stuttgart", "Torino-Cuneo","Valencia-Manises"};
        cityArray.add(OTP);

        // Catania
        final String[] CTA = {"Bucuresti-Otopeni", "Torino-Caselle"};
        cityArray.add(CTA);

        // Dublin
        final String[] DUB = {"Bacau", "Bucuresti-Otopeni"};
        cityArray.add(DUB);

        // Ibiza
        final String[] IBZ = {"Torino-Caselle"};
        cityArray.add(IBZ);

        //Iasi
        final String[] IAS = {"Bucuresti-Otopeni","Londra-Luton","Paris-Beveauis", "Roma-Fiumicino"};
        cityArray.add(IAS);

        // Koln
        final String[] CGN = {"Bucuresti-Otopeni"};
        cityArray.add(CGN);

        // Larnaca
        final String[] LCA = {"Bucuresti-Otopeni"};
        cityArray.add(LCA);

        //Liverpool
        final String[] LPL = {"Bacau", "Bucuresti-Otopeni"};
        cityArray.add(LPL);

        // Londres
        final String[] LTN = {"Bacau", "Bucuresti-Otopeni", "Iasi"};
        cityArray.add(LTN);

        // Madrid-Barajas
        final String[] MAD = {"Bucuresti-Otopeni"};
        cityArray.add(MAD);

        // Malaga
        final String[] AGP = {"Bucuresti-Otopeni"};
        cityArray.add(AGP);

        // Milano-Bergamo
        final String[] BGY = {"Bacau", "Bucuresti-Otopeni"};
        cityArray.add(BGY);

        // Milano-Linate
        final String[] LIN = {"Bucuresti-Otopeni"};
        cityArray.add(LIN);

        // Napoli
        final String[] NAP = {"Bucuresti-Otopeni"};
        cityArray.add(NAP);

        // Nice
        final String[] NCE = {"Bucuresti-Otopeni"};
        cityArray.add(NCE);

        // Paris-Beuveaus
        final String[] BVA = {"Bacau", "Bucuresti-Otopeni", "Iasi"};
        cityArray.add(BVA);

        // Roma-Fiumicino
        final String[] FCO = {"Bacau", "Bucuresti-Otopeni", "Iasi"};
        cityArray.add(FCO);

        // Sibiu
        final String[] SBZ = {"Stuttgart"};
        cityArray.add(SBZ);

        // Stuttgart
        final String[] STR = {"Bucuresti-Otopeni", "Sibiu"};
        cityArray.add(STR);

        // Torino-Cuneo
        final String[] CUF = {"Bacau", "Bucuresti-Otopeni"};
        cityArray.add(CUF);

        // Torino-Caselle
        final String[] TRN = {"Catania-Fontanarossa", "Ibiza"};
        cityArray.add(TRN);

        // Valencia-Manises
        final String[] VLC = {"Bucuresti-Otopeni"};
        cityArray.add(VLC);

        // return ArrayList
        return cityArray.get(depCity);
    }


    // codurile iata
    public static String getIataCodeByName(String s) {

        iataCodes.put("Antalya", "AYT");
        iataCodes.put("Bacau", "BCM");
        iataCodes.put("Barcelona-Prat", "BCN");
        iataCodes.put("Bologna-Marconi", "BLQ");
        iataCodes.put("Bruxelles","BRU");
        iataCodes.put("Bucuresti-Otopeni", "OTP");
        iataCodes.put("Catania-Fontanarossa", "CTA");
        iataCodes.put("Dublin","DUB");
        iataCodes.put("Ibiza","IBZ");
        iataCodes.put("Iasi","IAS");
        iataCodes.put("Koln", "CGN");
        iataCodes.put("Larnaca", "LCA");
        iataCodes.put("Liverpool-John-Lennon","LPL");
        iataCodes.put("Londra-Luton","LTN");
        iataCodes.put("Madrid-Barajas","MAD");
        iataCodes.put("Malaga","AGP");
        iataCodes.put("Milano-Bergamo","BGY");
        iataCodes.put("Milano-Linate","LIN");
        iataCodes.put("Napoli","NAP");
        iataCodes.put("Nice","NCE");
        iataCodes.put("Paris-Beveauis","BVA");
        iataCodes.put("Roma-Fiumicino","FCO");
        iataCodes.put("Sibiu","SBZ");
        iataCodes.put("Stuttgart","STR");
        iataCodes.put("Torino-Cuneo","CUF");
        iataCodes.put("Torino-Caselle", "TRN");
        iataCodes.put("Valencia-Manises","VLC");

        return iataCodes.get(s);
    }
}
