package com.example.adrian.com.blueair;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*|-----------------------------------------------------------------|
  |                   PARSER XML                                    |
  | Construit dupa exemplul luat din developer.android.com/training |
  | Proceseaza rezultatul cautarii care vine in forma de XML        |
  |-----------------------------------------------------------------|
 */
public class BlueAirXmlParser {
    // nu folosim namespaces
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    /**
     * cauta etichetele "JourneyDateMarket" si cheama readEntry pentru a procesa rezultatul
     * @param parser, raspunsul xml
     * @return List, lista cu datele de care avem nevoie odata scoase din XML
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<BlueAirXmlParser.Entry> entries = new ArrayList<Entry>();

        // cand chem pentru prima oara functia parserul este la prima eticheta: "Schedules"
        // trec la urmatoarea eticheta
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, ns, "ArrayOfJourneyDateMarket");

        // traversez XML pana la capat
        while (parser.next() != XmlPullParser.END_DOCUMENT ) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            // detaliile zborului sunt intre etichetele <Journey>
            if (name.equals("Journey")) {
                System.out.println("incep parsingul la XML");
                try {
                    entries.add(readEntry(parser));
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }
        return entries;
    }


    /**
    * creaza un obiect Entry care contine un set de date corespunzator
    * unui singur zbor
    * o chem in readEntry
    */
    public static class Entry {
        public final String DepartureDate;
        public final String DepartureStation;
        public final String ArrivalStation;
        public final String STA;
        public final String STD;
        public final String FlightNumber;
        public final String FareDiscountProcent;
        public final double AdtAmount;
        public final double ChdAmount;
        public final double AdtFareDiscountAmount;
        public final double ChdFareDiscountAmount;
        public final String AvailableCount;
        public final String SegmentSellKey;

        // constructor
        private Entry(String departureDate, String departureStation, String arrivalStation,
                      String sta, String std,  String flightNumber, String discountProcent,
                      double AdtDiscountAmount, double ChdDiscountAmount,
                      double adtAmount, double chdAmount, String AvailableCount,
                      String segmentSellkey) {
            this.DepartureDate = departureDate;
            this.DepartureStation = departureStation;
            this.ArrivalStation = arrivalStation;
            this.STA = sta;
            this.STD = std;
            this.FlightNumber = flightNumber;
            this.FareDiscountProcent = discountProcent;
            this.AdtAmount = adtAmount;
            this.ChdAmount = chdAmount;
            this.AdtFareDiscountAmount = AdtDiscountAmount;
            this.ChdFareDiscountAmount = ChdDiscountAmount;
            this.AvailableCount = AvailableCount;
            this.SegmentSellKey = segmentSellkey;
        }
    }

    /**
     * proceseaza datele continute intre etichetele <JourneyDateMarket>
     * din aceste date iau doar ce am nevoie, apoi creez un obiect Entry
     * pe care il populez cu datele scoase
     * Entry o sa fie atasat  listei "entries"
     * @return Entry, un obiect ce contine un set de date corespunzator unui zbor
     * @param  parser, xml care il citesc
     */
    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Journey");
        String DepartureDate = null;
        String DepartureStation = null;
        String ArrivalStation = null;
        String STA = null;
        String STD = null;
        String FlightNumber = null;
        double AdtFareDiscountAmount = 0.00;
        double ChdFareDiscountAmount = 0.00;
        String FareDiscountProcent = null;
        double AdtAmount = 0.00;
        double ChdAmount = 0.00;
        String AvailableCount = null;
        String SegmentSellKey = null;

        while (parser.next() != XmlPullParser.END_TAG ) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("State")) {
                skip(parser, "ActionStatusCode");
            } else if (name.equals("ArrivalStation")) {
                ArrivalStation = readStringTag(parser,"ArrivalStation",true);
                skip(parser,"ChangeReasonCode");
            } else if (name.equals("DepartureStation")) {
                DepartureStation = readStringTag(parser, "DepartureStation",true);
                skip(parser, "SegmentType");
            }  else if (name.equals("STA")){
                STA = readStringTag(parser, "STA", false);
            } else if (name.equals("STD")) {
                STD = readStringTag(parser, "STD", false);
                DepartureDate = STD;
            }  else if (name.equals("International")) { // skip pana la CarrierCode
                skip(parser,"CarrierCode");
            } else if (name.equals("FlightNumber")) { // skip pana la XrefFlightDesignator
                FlightNumber = readStringTag(parser, "FlightNumber", false);
                skip(parser, "XrefFlightDesignator");
            } else if (name.equals("Fares")) {
                if (checkFares(parser)) {              // verific daca am pret
                    boolean discount = false;
                    skip(parser,"PaxType");
                    parser.nextTag();                  // sunt la "PaxDiscountcode"
                    if (parser.isEmptyElementTag()) {
                        skip(parser,"PaxDiscountCode"); //  nu am discount
                        // System.out.println("NU am PaxDiscountCode!");
                    } else {
                        discount = true;
                        System.out.println("am PaxDiscountCode la adult!");
                    }
                    // avem fareDiscountCode (reducere in procente)?
                    parser.nextTag();
                    if (parser.isEmptyElementTag()) {
                        // System.out.println("Nu am fareDiscountProcent la: " + parser.getPositionDescription());
                        skip(parser,"CurrencyCode");
                    } else {
                        discount = true;
                        FareDiscountProcent = readStringTag(parser,"FareDiscountCode", false); // procent discount
                        System.out.println("control fareDiscountProcent " + parser.getPositionDescription());
                        skip(parser,"CurrencyCode");
                    }

                    // sunt la END_TAG de CurrencyCode, merg la urmatorul tag
                    parser.nextTag();
                    AdtAmount = readDoubleTag(parser, "Amount", false);
                    if (discount) {                  // am descuento
                        parser.nextTag();
                        skip(parser,"CurrencyCode");
                        parser.nextTag();
                        AdtFareDiscountAmount = readDoubleTag(parser, "Amount", false);
                    }
                    /*
                    * am si copii intre pasageri, desi pretul este acelasi il iau separat
                    * fiindca este posibil ca BlueAir sa faca discount diferit pentru copii
                    */
                    skip(parser,"PaxType");
                    parser.nextTag();
                    if (parser.isEmptyElementTag()) {
                        skip(parser,"PaxDiscountCode"); //  nu am discount
                        // System.out.println("NU am PaxDiscountCode!");
                    } else {
                        discount = true;
                        System.out.println("am PaxDiscountCode la copil!" + parser.getPositionDescription());
                    }
                    // avem fareDiscountCode (reducere in procente)?

                    parser.nextTag();
                    if (parser.isEmptyElementTag()) {
                         System.out.println("Nu am fareDiscountProcent la: " + parser.getPositionDescription());
                         parser.nextTag();
                         skip(parser,"CurrencyCode");
                    } else {
                        discount = true;
                        FareDiscountProcent = readStringTag(parser,"FareDiscountCode", false); // procent discount
                        //System.out.println("control fareDiscountProcent " +
                                //parser.getPositionDescription() + " disc: " + FareDiscountProcent);
                        skip(parser, "CurrencyCode");
                    }
                    System.out.println("Am ajuns la: " + parser.getPositionDescription());
                    // sunt la END_TAG de CurrencyCode, merg la urmatorul tag
                    parser.nextTag();
                    ChdAmount = readDoubleTag(parser, "Amount", true);
                    if (discount) {                  // am descuento
                        parser.nextTag();
                        skip(parser, "CurrencyCode");
                        parser.nextTag();
                        ChdFareDiscountAmount = readDoubleTag(parser, "Amount", true);
                        System.out.println("discount de: " + ChdFareDiscountAmount);
                    }
                    skip(parser,"FareSellKey");

                } else {                               // nu am pret, zbor vandut
                    AdtAmount = 0;
                    AvailableCount = "0";
                    skip(parser,"SalesDate");
                }
            } else if(name.equals("AvailableCount")) {
                AvailableCount = readStringTag(parser, "AvailableCount", false);
                skip(parser, "SalesDate");
            } else if (name.equals("SegmentSellKey")) {
                SegmentSellKey = readStringTag(parser, "SegmentSellKey", false);
                skip(parser,"JourneySellKey");
            }

        }
        parser.require(XmlPullParser.END_TAG,ns, "Journey"); // termin la tag-ul corect?
        System.out.println("E final..." + parser.getPositionDescription());
        System.out.println("|--- incep din nou ------|");

        // obiectul inapoiat contine un set de date corespunzator unui singur zbor
        return new Entry(DepartureDate, DepartureStation, ArrivalStation, STA, STD, FlightNumber,
                         FareDiscountProcent, AdtFareDiscountAmount, ChdFareDiscountAmount,
                         AdtAmount, ChdAmount, AvailableCount, SegmentSellKey);
    }


    /**
     * proceseza textul ce il gasim intre tag-uri
     * @param parser, xml
     * @param tagName, tagul pe care il caut
     * @param message, mesaj cu numele tag-ului la care am eroare
     * @return String, textul continut de tag-ul citit
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readStringTag(XmlPullParser parser, String tagName, boolean message)
            throws IOException,XmlPullParserException {
        String d;
        parser.require(XmlPullParser.START_TAG, ns, tagName);
        d = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        if (message) {
            System.out.println("Metoda readStringTag a trecut pe la == " + tagName + ": " + d);
        }
        return d;
    }

    /**
     * proceseza tag-urile care contin double
     * @param parser, xml
     * @param tagName, numele tag-ului care il citesc
     * @param message, mesaj cu numele etichetei la care ramane oprit in caz de eroare
     * @return Double, cifra continuta de ethichete
     * @throws IOException
     * @throws XmlPullParserException
     */
    private Double readDoubleTag(XmlPullParser parser, String tagName, boolean message)
            throws IOException,XmlPullParserException {
        String d;
        parser.require(XmlPullParser.START_TAG, ns, tagName);
        d = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        if (message) {
            System.out.println("Metoda readDoubleTag a trecut pe la: " + tagName + ": " + d);
        }
        return Double.parseDouble(d);
    }

    /**
     * For the tags title and summary, extracts their text values.
     * @param parser, xml care il citesc
     * @return String, textul continut de eticheta
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /**
     * verifica daca primul zbor din lista de rezulate este un zbor care poate fi vandut
     * pretul zborului este in eticheta <Fare>
     * daca nu exista eticheta <Fare> zborul nu poate fi vandut
     * @param parser, xml
     * @return boolean, inapoiaza false daca nu am "Fare"
     * @throws XmlPullParserException
     * @throws IOException
     */
    private boolean checkFares(XmlPullParser parser) throws  XmlPullParserException, IOException{
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        boolean result;
        // parserul este la eticheta <Fares> care contine la randul ei eticheta <Fare>
        parser.nextTag();     // caut eticheta <Fare>
        String name = parser.getName();

        if (name.equals("Fare") && parser.getEventType() == XmlPullParser.START_TAG) {
           //  System.out.println("Am gasit Fares!");
            result = true;
        } else {
            result = false;  // nu am <Fare>
            // System.out.println("Nu am fares");
            parser.nextTag();
        }
        // @return
        return result;
    }

    /**
     * metoda ajutatoare - traverseaza documentul xml de la eticheta actual pana la urmatoarea
     * echicheta de care am nevoie
     *
     * @param parser xml
     * @param endTag  eticheta la care trebuie sa ma opresc
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skip(XmlPullParser parser, String endTag) throws XmlPullParserException, IOException {
        //System.out.println("skip II! sunt la : " + parser.getName());

        boolean stop = false;
        while (!stop) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    // am gasit tag-ul cautat, ies din while
                    if (endTag.equals(parser.getName())) {
                        stop = true;
                    }
                    break;
                case XmlPullParser.START_TAG:
                    // eticheta cautata este de tip "empty" (ex. <SegmentType />
                    if (parser.isEmptyElementTag() && endTag.equals(parser.getName())){
                        parser.nextTag();
                        stop = true;
                    }
                    // am observat ca parserul se opreste cand gaseste o eticheta care are
                    // atribut, ex: <State xmls="valoare atribut"  />
                    // sar la urmatoarea eticheta
                    if (parser.getAttributeCount() != 0) {
                        continue;
                    }
                    break;
            }
        }
        //System.out.println("******** am iesit din skip II la tag " + parser.getPositionDescription());
    }
}
