package com.example.adrian.com.blueair;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * dialog pentru a arata detaliile zborului cand se face click pe un zbor.
 */
public class FlightDetailsDialogFragment extends DialogFragment implements View.OnClickListener{


    public FlightDetailsDialogFragment(){
        // constructor gol necesario
    }

    Button closeButton;

    /**
     * creaza un dialog
     * @param args
     * @return
     */
    public static FlightDetailsDialogFragment newInstance(Bundle args){

        FlightDetailsDialogFragment fragment = new FlightDetailsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Detalii zbor");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.flight_fragment_dialog, container, false);

        // chem setWidget care imi seteaza toate campurile de text cu datele care le-am trimis
        // din ResultsFragment.java
        setWidget(view);
        return view;
    }

    /**
     * initializeaza widgeturile
     * @param view
     */
    private void setWidget(View view) {
        // timp in milisecunde
        long time = MyDateParser.getMillisecondsFromString(getArguments().getString("depStandardDate"));

        // data plecarii in format ziua, luna an
        TextView depDate = (TextView) view.findViewById(R.id.departureDate);
        depDate.setText(MyDateParser.getDayNameFromTime(time) + ", " +
                       MyDateParser.getDayFromTime(time) + " " +
                       MyDateParser.getMonthAndYearFromTime(time));

        // ora plecarii si sosiri
        TextView depSTD = (TextView) view.findViewById(R.id.departureSTD);
        depSTD.setText(getArguments().getString("departureSTD"));
        TextView depSTA = (TextView) view.findViewById(R.id.departureSTA);
        depSTA.setText(getArguments().getString("departureSTA"));

        // locuri ramase in tarifa respectiva
        TextView seats = (TextView) view.findViewById(R.id.availableCount);
        seats.setText(getArguments().getString("availableCount"));

        // numar zbor
        TextView flight = (TextView) view.findViewById(R.id.flightNumber);
        flight.setText(getArguments().getString("flightNumber"));

        // pret zbor si eventualul discount
        TextView amount = (TextView) view.findViewById(R.id.amount);
        amount.setText(getArguments().getString("amount"));
        TextView discount = (TextView) view.findViewById(R.id.discountProcent);
        discount.setText(getArguments().getString("discount"));

        // butoane
        closeButton = (Button) view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
       dismiss();
    }
}
