package com.example.adrian.com.blueair;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ServicesDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicesDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    public ServicesDialogFragment() {
        // Required empty public constructor
    }

    /**
     * facturatul online nu este premis de pe toate aeroporturile, lista de mai jos tine
     * o relatie de toate aeroporatele din care este permis in momentul actual
     */
    private List<String> checkinCity = Arrays.asList("BCM","BCN", "BRU", "CTA", "CGN", "OTP",
            "BRU","LCA", "AGP","MAD","BGY","NCE", "BVA","FCO","SBZ","CUF","VLC","LPL",
            "LIN", "TRN");

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SERVICE_TYPE = "service_title";
    private static final String SERVICE_DESCRIPTION = "service_description";
    private static final String SERVICE_ID = "service_id";
    private static final String TRIP_TYPE = "trip_type";
    private static final String PAX_TOTAL = "pax_total";
    private static final String DEP_CITY = "dep_city";
    private static final String ARR_CITY = "arr_city";
    private static final String DEPARTURE_DATE = "departure_date";
    private static final String RETURN_DATE = "return_date";
    private int viewId;

    // Views
    Spinner depSpinner;
    Spinner retSpinner;
    TextView priceText;

    /**
     * creaza un nou dialog
     * @param id id-ul serviciului (de la 1 la 5)
     * @param depCity oras plecare
     * @param arrCity oras sosire
     * @param totalPax numarul de pasageri (fara infanti)
     * @param tripType zbor dus sau dus-intors
     * @param depDate ziua de dus selectionata in milisecunde
     * @param retDate ziua de intors selectionata in milisecunde
     * @return o noua instanta ServiceDialogFragment cu parametri dati
     */
    public static ServicesDialogFragment newInstance(int id, String depCity, String arrCity,
                                                     int totalPax, String tripType,
                                                     long depDate, long retDate) {

        ServicesDialogFragment fragment = new ServicesDialogFragment();

        Bundle args = new Bundle();
        args.putString(SERVICE_TYPE, ServPriceCalc.getServiceTitle(id));
        args.putString(SERVICE_DESCRIPTION, ServPriceCalc.getServiceDescription(id));
        args.putInt(SERVICE_ID, id);
        args.putInt(PAX_TOTAL,totalPax);
        args.putString(DEP_CITY, ArrivalCitySetter.getIataCodeByName(depCity));
        args.putString(ARR_CITY,ArrivalCitySetter.getIataCodeByName(arrCity));
        args.putString(TRIP_TYPE,tripType);
        args.putLong(DEPARTURE_DATE, depDate);
        args.putLong(RETURN_DATE,retDate);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            viewId = getArguments().getInt(SERVICE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);

        // titlu si descriptia serviciului
        TextView dialogTitle = (TextView) view.findViewById(R.id.dialogTitle);
        dialogTitle.setText(getArguments().getString(SERVICE_TYPE));

        TextView serviceDescription = (TextView) view.findViewById(R.id.serviceDescription);
        serviceDescription.setText(getArguments().getString(SERVICE_DESCRIPTION));
        getDialog().setTitle("Servicii aditionale");

        // ruta zbor dus
        TextView departure = (TextView) view.findViewById(R.id.departureCityDetails);
        departure.setText(getArguments().getString(DEP_CITY) + "-" + getArguments().getString(ARR_CITY));

        // setez tipul serviciului in clasa
        ServPriceCalc.setServiceId(viewId);
        // spinner servicii zbor dus
        depSpinner = (Spinner) view.findViewById(R.id.departureSpinner);
        depSpinner.setAdapter(makeArrayAdapter(getArguments().getInt(PAX_TOTAL),viewId, "departure"));
        depSpinner.setOnItemSelectedListener(this);
        depSpinner.setSelection(ServPriceCalc.getItemNumber("departure"));

        // ruta zbor intors
        TextView returnDetails = (TextView) view.findViewById(R.id.returnCityDetails);
        returnDetails.setText(getArguments().getString(ARR_CITY) + "-" + getArguments().getString(DEP_CITY));
        retSpinner = (Spinner) view.findViewById(R.id.returnSpinner);
        retSpinner.setAdapter(makeArrayAdapter(getArguments().getInt(PAX_TOTAL), viewId,"return"));
        retSpinner.setOnItemSelectedListener(this);
        retSpinner.setSelection(ServPriceCalc.getItemNumber("return"));

        // daca nu am zbor dus intors desactivez spinnerul de intoarcere
        if (getArguments().getString(TRIP_TYPE).equals("false")) {
            retSpinner.setEnabled(false);
        }

        // pretul din dialog
        priceText = (TextView) view.findViewById(R.id.dialogPriceTextView);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        Spinner mySpinner = (Spinner) parent;        // spinner-ul din care a fost chemat

        // in clasa ServicePriceCalculator este tot ce am nevoie pentru
        // a calcula pretul serviciilor
        ServPriceCalc.setPax(getArguments().getInt(PAX_TOTAL));

        // setez data de dus si de intors cu care voi lucra
        ServPriceCalc.setDepAndRetDate(getArguments().getLong(DEPARTURE_DATE),
                                       getArguments().getLong(RETURN_DATE));

        // din ce spinner este chemat listenerul ?
        if (mySpinner.getId() == depSpinner.getId()) {
            ServPriceCalc.setSegment("departure");
        } else  {
            ServPriceCalc.setSegment("return");
        }

        // valoare item selectat
        String selectedItemText = parent.getSelectedItem().toString();

        /* serviciul de check-in este singurul care nu are valori numerice, dar pentru a
         * calcula pretul serviciului am nevoie de o valoare numerica
         */

        int valueOfSelectedItem = 0;
        if (viewId == 2) {
            if (selectedItemText.equals("online")) {
                valueOfSelectedItem = 0;
            } else if (selectedItemText.equals("aeroport")) {
                valueOfSelectedItem = 1;
            }
        } else {
            valueOfSelectedItem = Integer.parseInt(parent.getSelectedItem().toString());
        }

        ServPriceCalc.setServiceSegmentPrice(valueOfSelectedItem);
        priceText.setText(ServPriceCalc.getTotalServicePrice() + " €");
    }

    /**
     * cand selectionz un serviciu este nevoie sa actualizam pretul zborului adaugand
     * serviciile. In momentul in care dialogul se inchide pretul se actualizeaza
     */
    @Override
    public void onDetach() {
        super.onDetach();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        PriceFragment priceFragment = (PriceFragment) manager.findFragmentById(R.id.price_fragment);
        priceFragment.updatePrice(viewId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        adapterView.setSelection(adapterView.getFirstVisiblePosition());
    }

    /**
     * metoda pentru a crea un array pentru spinner in baza la numarul de pasageri si
     * tipul serviciului
     * @return array personalizat
     */
    private ArrayAdapter<String> makeArrayAdapter(int paxNumber, int serviceId, String segment) {

        List myList = new ArrayList();
        /**
         * depinde de tipul serviciului am mai multe sau mai putine optiuni in spinner
         * case 1: bagaj de cala
         * case 2: check-in online
         * case 3: minor neinsotit
         * case 4: animal de companie in cabina
         * case 5: animal de compania in cala
         */
        switch (serviceId){
            case 1:
                for (int i=0; i<= paxNumber * 4;i++) {
                    myList.add(i);
                }
                break;
            case 2:
                // check-ingul online nu este permis pe orice aeroport
                // verific daca este permis checkingul in aeroportul respectiv
                String dep = getArguments().getString(DEP_CITY);
                String ret = getArguments().getString(ARR_CITY);
                Boolean checkOk = false;
                if (segment.equals("departure")) {
                    for (String value : checkinCity) {
                        if (value.equals(dep)) {
                            checkOk = true;
                            break;
                        }
                    }
                } else {
                    for (String value : checkinCity) {
                        if (value.equals(ret)) {
                            checkOk = true;
                            break;
                        }
                    }
                }
                if (checkOk) {
                        myList.add("online");
                        myList.add("aeroport");
                } else {
                    myList.add("indisponibil");
                }
                break;
            case 3:
                for (int i=0; i<= paxNumber;i++) {
                    myList.add(i);
                }
                break;
            case 4:
                for (int i=0; i<= paxNumber;i++) {
                    myList.add(i);
                }
                break;
            case 5:
                for (int i=0; i<= paxNumber;i++) {
                    myList.add(i);
                }
                break;
        }

       return new ArrayAdapter<String>(this.getActivity() ,R.layout.services_spinner_row, myList);
    }
}
