package com.example.adrian.com.blueair;

/**
 * tot de ce avem nevoie in a calcula pretul serviciilor
 */
public class ServPriceCalc {

    private static int serviceId;
    private static String segment;
    private static int pax;
    private static long departureDate;
    private static long returnDate;

    // variabile pentru a tine socoteala la servicii
    private static int depBagPrice = 0;
    private static int retBagPrice = 0;
    private static int totalBagPrice = 0;

    private static int depCheckinPrice = 0;
    private static int retCheckinPrice = 0;
    private static int totalCheckinPrice = 0;

    private static int depUmPrice = 0;
    private static int retUmPrice = 0;
    private static int totalUmPrice = 0;

    private static int depCabinPetPrice = 0;
    private static int retCabinPetPrice = 0;
    private static int totalCabinPetPrice = 0;

    private static int depBigPetPrice = 0;
    private static int retBigPetPrice = 0;
    private static int totalBigPetPrice = 0;

    // totalul tuturor serviciilor
    private static int bigTotal = 0;

    /**
     * setter serviceId
     * @param id id-ul serviciului
     */
    public static void setServiceId(int id) {
        serviceId = id;
    }


    /**
     * setter segment
     * @param s descriptia segmentlui
     */
    public static void setSegment(String s) {
        segment = s;
    }

    /**
     *
     * @param i numarul de pasageri
     */
    public static void setPax(int i) {
        pax = i;
    }

    /**
     * seteaza data cu care vom lucra in milisecunde
     * @param d data plecarii in milisecunde
     * @param r data intoarcerii in milisecunde
     */
    public static void setDepAndRetDate(long d, long r) {
        departureDate = d;
        returnDate = r;
    }

    /**
     * inapoiaza pretul unitar al serviciuluiu
     * @param id ia ca parametru id-ul serviciului
     * @return servicePrice
     */
    public static int getUnitarServicePrice(int id, String seg) {
        int servicePrice = 0;
        switch (id) {
            case 1:
                /*
                 * pretul bagajului difera in functie de sezon, folosim checkBagPrice
                 */
                if (seg.equals("departure"))
                {
                    servicePrice = checkBagPrice(departureDate);   // bagaj
                } else {
                    servicePrice = checkBagPrice(returnDate);
                }

                break;
            case 2:
                servicePrice = 5;    // checkin
                break;
            case 3:
                servicePrice = 60;   // minor neisotit
                break;
            case 4:
                servicePrice = 35;   // animal in cabina
                break;
            case 5:
                servicePrice = 75;   // animal in cala
                break;
        }
        return  servicePrice;
    }

    /**
     * seteaza pretul serviciilor in functie de numarul pieselor alese sau in functie
     * de numarul de pasageri (in cazul checkin-gului)
     * @param id id-ul serviciului
     * @param price pretul unitar al serviciului
     * @param segment segmentul din care chem metoda
     */
    private static void setSelectedServicePrice (int id, int price, String segment) {
        switch (id) {
            case 1:
                if (segment.equals("departure")) {
                    depBagPrice = price;
                } else if (segment.equals("return")) {
                    retBagPrice = price;
                }
                break;
            case 2:
                if (segment.equals("departure")) {
                    depCheckinPrice = price;
                } else if (segment.equals("return")) {
                    retCheckinPrice = price;
                }
                break;
            case 3:
                if (segment.equals("departure")) {
                    depUmPrice = price;
                } else if (segment.equals("return")) {
                    retUmPrice = price;
                }
                break;
            case 4:
                if (segment.equals("departure")) {
                    depCabinPetPrice = price;
                } else if (segment.equals("return")) {
                    retCabinPetPrice = price;
                }
                break;
            case 5:
                if (segment.equals("departure")) {
                    depBigPetPrice = price;
                } else if (segment.equals("return")) {
                    retBigPetPrice = price;
                }
                break;
        }
    }

    /**
     * returneaza descriptia serviciului
     * @param id  id-ul serviciului
     * @return serviceName
     */
    public static String getServiceTitle(int id) {
        String serviceName = "";
        switch (id){
            case 1:
                serviceName = "Bagaj de cala";
                break;
            case 2:
                serviceName = "Checkin ";
                break;
            case 3:
                serviceName = "Minor neisotit";
                break;
            case 4:
                serviceName = "Animal de companie in cabina";
                break;
            case 5:
                serviceName = "Animal de companie in cala";
                break;
        }
        return serviceName;
    }

    public static String getServiceDescription(int id) {
        String serviceName = "";
        switch (id){
            case 1:
                serviceName = "Fiecare valiza are o greutate maxima de 32 kg. Pretul bagajului este " +
                        "18 € sau 25 €, in functie de perioada calatoriei. Fiecare pasager are " +
                        "dreptul de a transporta un bagaj de mana de 10 kg si dimensiunile " +
                        "55x40x20 cm. Infantul nu are dreputul la bagaj de mana.";
                break;
            case 2:
                serviceName = "Check-in online este gratuit iar pentru serviciul de check-in in " +
                        "aeroport se percepe o taxa de 5 €/segment de calatorie. Daca alegeti " +
                        "optiunea de check-in online aveti obligatia sa va prezentati in aeroport " +
                        "cu cartile de imbarcare (boarding-pass) imprimate. ";
                break;
            case 3:
                serviceName = "Pentru transportul minorilor cu varsta cuprinsa intre 6 si 18 ani " +
                        "se percepe o taxa de 60 €/segment. Pentru zborurile cu plecare din " +
                        "Romania minori de nationalitate romana au nevoie de procura din partea " +
                        "parintilor sau tutorilor legali.";
                break;
            case 4:
                serviceName = "Pentru a putea fi transportat in cabina animalul trebuie sa dispuna " +
                        "de o cusca corespunzatoare si de documente, chip si" +
                        " vaccinele necesare. Greutatea custii cu animalul inauntru nu " +
                        "poate depasi 6 kg. Pretul serviciului este de 35 €/segment. " +
                        "Se pot transporta doar caini sau pisici.";
                break;
            case 5:
                serviceName = "Serviciu pentru animale de companie de talie medie, cu greutate " +
                        "cuprinsa intre 6 - 32 kg (animal + cusca). Animalul trebuie transportat " +
                        "intr-o cusca standardizata si trebuie sa dispuna de toate documentele " +
                        "necesare. Pretul este de 75 €/segment. Pentru animale care depasesc 32 kg " +
                        "(animal + cusca) se va plati direct in aeroport la ghiseul de checkin " +
                        " o taxa suplimentare de 50 €/segment.";
                break;
        }
        return serviceName;
    }

    /**
     * metoda pentru a returna pretul total al unui serviciu pe un anumit segment (dus sau intors)
     * @param items  numarul de servicii (cate bagaje, sau check-in-uri)
     * @return total totalul pret serviciu x numarul de items
     */
    public static int setServiceSegmentPrice(int items) {

        // calculez pretul total multiplicand pretul serviciului cu numarul de unitati
        // la checkin este diferit, inmultesc si cu nr. de pasageri
        int mPrice = (serviceId == 2) ? (getUnitarServicePrice(serviceId,segment) * items * pax) :
                                        (getUnitarServicePrice(serviceId, segment) * items);

        // nu uit sa setez pretul total al serviciului respectiv
        setSelectedServicePrice(serviceId, mPrice,segment);

        return mPrice;
    }

    /**
     * returneaza pretul total al unui tip de serviciu in particular
     * @return bigTotal  pretul total al serviciului
     */
    public static int getTotalServicePrice() {
        int total = 0;

        switch (serviceId) {
            case 1:
                totalBagPrice = depBagPrice + retBagPrice;
                total =  totalBagPrice;
                break;
            case 2:
                totalCheckinPrice = depCheckinPrice + retCheckinPrice;
                total = totalCheckinPrice;
                break;
            case 3:
                totalUmPrice = depUmPrice + retUmPrice;
                total = totalUmPrice;
                break;
            case 4:
                totalCabinPetPrice = depCabinPetPrice + retCabinPetPrice;
                total = totalCabinPetPrice;
                break;
            case 5:
                totalBigPetPrice = depBigPetPrice + retBigPetPrice;
                total = totalBigPetPrice;
                break;
        }
        System.out.println("getTotalServicePrice:  " + total);
        return total;
    }

    /**
     * @return inapoiaza suma tuturor serviciilor
     */
    public static int getBigTotal() {
        bigTotal =   totalBagPrice + totalCheckinPrice + totalUmPrice +
                totalCabinPetPrice + totalBigPetPrice;
        return bigTotal;
    }

    /**
     * calculeaza cate unitati de un anumit serviciu au fost selectate
     * pe un segment (de ex. cate bagaje sau cate animale de companie au fost selectate)
     * @param segmentCall  segmentul de dus sau de intors
     * @return items numarul de unitati
     */
    public static int getItemNumber(String segmentCall) {
        int items = 0;
        switch (serviceId) {
            case 1:
                if (segmentCall.equals("departure")) {
                    items = (depBagPrice > 0) ?
                            (depBagPrice / getUnitarServicePrice(serviceId,"departure")) : 0;
                } else {
                    items = (retBagPrice > 0) ? (retBagPrice / getUnitarServicePrice(serviceId,"return")) : 0;
                }
                break;
            case 2:
                /*
                 * checking este un caz special, are doar optiunea "online" (pozitie 0) si
                 * "aeroport" (pozitie 1)
                 */
                if (segmentCall.equals("departure")) {
                    items = (depCheckinPrice > 0) ? 1 : 0;
                } else {
                    items = (retCheckinPrice > 0) ? 1 : 0;
                }
                break;
            case 3:
                if (segmentCall.equals("departure")) {
                    items = (depUmPrice > 0) ? (depUmPrice / getUnitarServicePrice(serviceId,"departure")) : 0;
                } else {
                    items = (retUmPrice > 0) ? (retUmPrice / getUnitarServicePrice(serviceId,"return")) : 0;
                }
                break;
            case 4:
                if (segmentCall.equals("departure")) {
                    items = (depCabinPetPrice > 0) ?
                            (depCabinPetPrice / getUnitarServicePrice(serviceId,"departure")) : 0;
                } else {
                    items = (retCabinPetPrice > 0) ?
                            (retCabinPetPrice / getUnitarServicePrice(serviceId,"return")) : 0;
                }
                break;
            case 5:
                if (segmentCall.equals("departure")) {
                    items = (depBigPetPrice > 0) ?
                            (depBigPetPrice / getUnitarServicePrice(serviceId, "departure")) : 0;
                } else {
                    items = (retBigPetPrice > 0) ?
                            (retBigPetPrice / getUnitarServicePrice(serviceId,"return")) : 0;
                }
                break;
        }
        return items;
    }

    /**
     * metoda care verifica daca pretul bagajului este 18 sau 25 €, in functie de
     * perioada calatoriei
     * @param d data plecarii in milisecunde
     * @return pretul bagajului
     * o folosesc mai sus, in getUnitarServicePrice
     * in fiecare an aceste dati variaza
     */
    private static int checkBagPrice(long d) {
        int bagPrice;
        if ((d >= MyDateParser.getMillisecondsFromString("2015-01-13") &&
             d <= MyDateParser.getMillisecondsFromString("2015-03-27"))
                ||
            ( d >= MyDateParser.getMillisecondsFromString("2015-04-21") &&
              d <= MyDateParser.getMillisecondsFromString("2015-06-04"))
                ||
            (d >= MyDateParser.getMillisecondsFromString("2015-09-21") &&
            d <= MyDateParser.getMillisecondsFromString("2015-10-24"))
            )
        {
            bagPrice = 18;
        } else {
            bagPrice = 25;
        }

        return bagPrice;
    }

    /**
     * reseteaza toate preturile la 0
     * @see com.example.adrian.com.blueair.ResultsFragment
     */
    public static void resetValues() {
            depBagPrice = 0;
            retBagPrice = 0;
            totalBagPrice = 0;

            depUmPrice = 0;
            retUmPrice = 0;
            totalUmPrice = 0;

            depCheckinPrice = 0;
            retCheckinPrice = 0;
            totalCheckinPrice = 0;

            depCabinPetPrice = 0;
            retCabinPetPrice = 0;
            totalCabinPetPrice = 0;

            depBigPetPrice = 0;
            totalBigPetPrice = 0;
            retBigPetPrice = 0;
    }
}
