package com.example.adrian.com.blueair;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;


public class PriceFragment extends Fragment {

    // regula pentru a da format pretului
    private static final String NR_FORMAT = "###,###.00";
    DecimalFormat decimalFormat = new DecimalFormat(NR_FORMAT);

    // layout principal
    FrameLayout frameLayout;


    // campurile de text
    TextView priceTextView;                    // pret
    TextView taxTextView;                      // taxe
    TextView adultPaxText;                     // pasageri adulti
    TextView totalTaxPrice;                    // totalul taxelor

    // campuri text servicii
    TextView bagText;
    TextView checkinText;
    TextView umText;
    TextView cabinPetText;
    TextView bigPetText;

    // pret
    Double finalPrice = 0.00;
    Double departurePrice = 0.00;
    Double returnPrice = 0.00;
    Double departureChdPrice = 0.00;
    Double returnChdPrice = 0.00;

    // data zborului care apare in listView-ul cu rezultate
    long departureDate;
    long returnDate;

    // butoane servicii
    ImageButton bagButton;
    ImageButton checkButton;
    ImageButton umButton;
    ImageButton cabinPetButton;
    ImageButton bigPetButton;
    private int totalPax;
    private String trip;
    private String departureCity;
    private String arrivalCity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.price_fragment_layout, container, false);
        frameLayout = (FrameLayout) view.findViewById(R.id.price_fragment_layout);

        // pret si taxe
        priceTextView = (TextView) view.findViewById(R.id.price);
        taxTextView = (TextView) view.findViewById(R.id.taxText);
        adultPaxText = (TextView) view.findViewById(R.id.adultPaxText);
        totalTaxPrice = (TextView) view.findViewById(R.id.totalTaxPrice);

        // detalii pret servicii
        bagText = (TextView) view.findViewById(R.id.bagText);
        checkinText = (TextView) view.findViewById(R.id.checkinText);
        umText = (TextView) view.findViewById(R.id.umText);
        cabinPetText = (TextView) view.findViewById(R.id.cabinPetText);
        bigPetText = (TextView) view.findViewById(R.id.bigPetText);

        // butoane si listenere servicii
        bagButton = (ImageButton) view.findViewById(R.id.bagButton);
        bagButton.setOnClickListener(servicesListener);
        checkButton = (ImageButton) view.findViewById(R.id.checkButton);
        checkButton.setOnClickListener(servicesListener);
        umButton = (ImageButton) view.findViewById(R.id.umButton);
        umButton.setOnClickListener(servicesListener);
        cabinPetButton = (ImageButton) view.findViewById(R.id.cabinPetButton);
        cabinPetButton.setOnClickListener(servicesListener);
        bigPetButton = (ImageButton) view.findViewById(R.id.bigPetButton);
        bigPetButton.setOnClickListener(servicesListener);
        return view;
    }


    /**
     * calculeaza suma taxelor
     * @param paxNumber numarul de pasageri
     * @param tripType  tipul zborului (dus sau dus-intors)
     * @return totalu taxelor
     */
    private int getTaxTotal(int paxNumber, String tripType) {

        final int FEE_TAX = 8;            // taxa BlueAir 8 euro/segment si pasager
        final int ANULATION_TAX = 4;      // taxa anulare forta majora 4 euro/pasager
        int taxTotal = 0;

        if (tripType.equals("true")) {
         // zbor dus-intors, primul pasager plateste 17 €, al doilea 10, urmatorii 5
            if (paxNumber == 1) {
                taxTotal = 15;
            } else if (paxNumber == 2) {
                taxTotal = 25;
            } else {
                taxTotal = (paxNumber * 5) + 15;
            }
        } else if (tripType.equals("false")) {
            // zbor doar dus
            if (paxNumber == 1) {
                taxTotal = 10;
            } else if (paxNumber == 2) {
                taxTotal =  15;
            } else {
                taxTotal = ((paxNumber + 1) * 5);
            }
        }
        return taxTotal;
    }

    /**
     * @param bundle contine datele trimise din ResultMainActivity
     * @param segment verifica din ce listView a fost chemat, dus sau intors
     * @see com.example.adrian.com.blueair.ResultsMainActivity
     * pretul il iau din TextView, vine in format "39,98 €", trebuie sa elimin
     * spatiu si litera "€" si sa schimb virgula in punct pentru al folosi ca si Double
     */
    public void priceMaker(Bundle bundle, String segment){

        int infantPrice = 0;
        int taxTotal = 0;
        // pasageri
        int pax_adt = Integer.parseInt(bundle.get("pax_adt").toString());
        int pax_chd = Integer.parseInt(bundle.get("pax_chd").toString());
        int pax_inf = Integer.parseInt(bundle.get("pax_inf").toString());
        int paxTotal = pax_adt + pax_chd;

        // dus/dus-intors
        String tripType = bundle.get("triptype").toString();

        if (!bundle.get("departurePrice").equals("Vandute")) {
            if(segment.equals("departureSegment")){
                /**
                 * ziua plecarii in milisecunde, am nevoie de data in dialogFragment
                 * pentru a seta pretul bagajului
                 */
                departureDate = MyDateParser.
                        getMillisecondsFromString(bundle.get("departure_date").
                        toString());

                // pret adult, vine in format xxx,xx €, elimin € si spatiu
                String tempPrice = bundle.get("departurePrice").toString().replace(",",".");
                departurePrice = Double.parseDouble(tempPrice.substring(0,tempPrice.length() - 2));

                // pret copil
                String tempChildPrice = bundle.get("departureChdPrice").toString().replace(",",".");
                departureChdPrice = Double.parseDouble(tempChildPrice);
            } else if (segment.equals("returnSegment")) {
                /**
                 * ziua intoarcerii in milisecunde, am nevoie de data in dialogFragment
                 * pentru a pretul bagajului pretul bagajului
                 */
                returnDate = MyDateParser.getMillisecondsFromString(bundle.get("return_date").toString());

                // pret adult intoarcere
                String tempPrice = bundle.get("departurePrice").toString().replace(",", ".");
                returnPrice = Double.parseDouble(tempPrice.substring(0,tempPrice.length() - 2));

                //pret copil intoarcere
                String tempChildPrice = bundle.get("departureChdPrice").toString().replace(",",".");
                returnChdPrice = Double.parseDouble(tempChildPrice);
            }

            Double totalPrice = departurePrice + returnPrice;
            Double chdPrice = departureChdPrice + returnChdPrice;
            // calculez taxele + comision agentie
            if (tripType.equals("false")) {
                taxTotal = getTaxTotal(paxTotal, "false");
                infantPrice = pax_inf * 35;
            } else if (tripType.equals("true")) {
                taxTotal = getTaxTotal(paxTotal, "true");
                infantPrice = pax_inf * 70;
            }

            finalPrice = ((totalPrice * pax_adt) +(chdPrice * pax_chd)) + infantPrice + taxTotal;

            // parametri necesari la calculat pretul la servicii
            totalPax = paxTotal;
            trip = tripType;
            // oras plecare si destinatie
            departureCity = bundle.get("departure").toString();
            arrivalCity = bundle.get("arrival").toString();

            // arat fragmentul de la pret
            frameLayout.setVisibility(View.VISIBLE);

            priceTextView.setText("Total: \n" + decimalFormat.format(finalPrice +
                    ServPriceCalc.getBigTotal()) + " €");

            adultPaxText.setText("Pasageri: " + (pax_adt + pax_chd));
            if (pax_inf > 0) {
                adultPaxText.append("; infanti: " + pax_inf);
            }
            taxTextView.setText("Taxe: " + decimalFormat.format(taxTotal) + " € incluse");
        }
    }


    /**
     * listener butoane servicii
     */
    View.OnClickListener servicesListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDialog(view.getId());
        }
    };

    /**
     * creaza un dialogFragment pentru fiecare serviciu in parte
     * @param viewId id-ul butonului care a chemat dialogul
     */
    void showDialog(int viewId) {
        int serviceId = 0;
        if (viewId == bagButton.getId()){
            serviceId = 1;
        } else if (viewId == checkButton.getId()) {
            serviceId = 2;
        } else if (viewId == umButton.getId()) {
            serviceId = 3;
        } else if (viewId == cabinPetButton.getId()) {
            serviceId = 4;
        } else if (viewId == bigPetButton.getId()) {
            serviceId = 5;
        }
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentById(R.id.servicesDialogFragment);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Creeam si aratam dialogul
        ServicesDialogFragment newFragment = ServicesDialogFragment.newInstance(serviceId,
                                             departureCity, arrivalCity,totalPax, trip,
                                             departureDate, returnDate);
        newFragment.show(ft, "dialog");
    }

    /**
     * metoda pentru a actualiza pretul odata ce am ales un serviciu si dialogul a fost inchis
     * deasemenea schimba aparenta butoanelor odata ce a fost selectinat un serviciu
     * @param viewId id-ul serviciului
     * o chem in metoda onDetach() in  ServiceDialogFragment
     */
    public void updatePrice(int viewId) {
        Double t = finalPrice + ServPriceCalc.getBigTotal();
        priceTextView.setText("Total: " + decimalFormat.format(t) + " €");
        totalTaxPrice.setText("Servicii: " + decimalFormat.format(ServPriceCalc.getBigTotal()) + " €");

        switch (viewId) {
            case 1:
                bagText.setText(ServPriceCalc.getTotalServicePrice() + " €");
                if (ServPriceCalc.getTotalServicePrice() > 0) {
                    bagButton.setImageResource(R.drawable.green_bag);
                    bagButton.setBackgroundColor(getResources().getColor(R.color.green));
                } else {
                    bagButton.setImageResource(R.drawable.bag);
                    bagButton.setBackgroundColor(getResources().getColor(R.color.lightOrange));
                }
                break;
            case 2:
                checkinText.setText(ServPriceCalc.getTotalServicePrice() + " €");
                if (ServPriceCalc.getTotalServicePrice() > 0) {
                    checkButton.setImageResource(R.drawable.green_checkin);
                    checkButton.setBackgroundColor(getResources().getColor(R.color.green));
                } else {
                    checkButton.setImageResource(R.drawable.checkin);
                    checkButton.setBackgroundColor(getResources().getColor(R.color.lightOrange));
                }
                break;
            case 3:
                umText.setText(ServPriceCalc.getTotalServicePrice() + " €");
                if (ServPriceCalc.getTotalServicePrice() > 0) {
                    umButton.setImageResource(R.drawable.green_um);
                    umButton.setBackgroundColor(getResources().getColor(R.color.green));
                } else {
                    umButton.setImageResource(R.drawable.um);
                    umButton.setBackgroundColor(getResources().getColor(R.color.lightOrange));
                }
                break;
            case 4:
                cabinPetText.setText(ServPriceCalc.getTotalServicePrice() + " €");
                if (ServPriceCalc.getTotalServicePrice() > 0) {
                    cabinPetButton.setImageResource(R.drawable.green_cabinpet);
                    cabinPetButton.setBackgroundColor(getResources().getColor(R.color.green));
                } else {
                    cabinPetButton.setImageResource(R.drawable.cabinpet);
                    cabinPetButton.setBackgroundColor(getResources().getColor(R.color.lightOrange));
                }
                break;
            case 5:
                bigPetText.setText(ServPriceCalc.getTotalServicePrice() + " €");
                if (ServPriceCalc.getTotalServicePrice() > 0) {
                    bigPetButton.setImageResource(R.drawable.green_bigpet);
                    bigPetButton.setBackgroundColor(getResources().getColor(R.color.green));
                } else {
                    bigPetButton.setImageResource(R.drawable.bigpet);
                    bigPetButton.setBackgroundColor(getResources().getColor(R.color.lightOrange));
                }
                break;
        }
    }
}
