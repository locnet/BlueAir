package com.example.adrian.com.blueair;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * BlueAir - body fragment.
 */
public class MainActivityBodyFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {
    // parametri pentru url
    String tripType = "true";

    //capto los bottones
    Button onewayButton;
    Button roundtripButton;
    Spinner departureSpinner;
    Spinner arrivalSpinner;

    // calendar
    TextView departureDayValue;
    TextView departureMonthAndYearValue;
    TextView returnDayValue;
    TextView returnMonthAndYearValue;

    // textView ascuns
    TextView hideReturnDate;
    TextView hideDepartureDate;

    // array ciudades de llegada
    ArrivalCitySetter arrivalCityArray = new ArrivalCitySetter();

    // date parser
    String s = MyDateParser.getActualDateString();
    long depM = MyDateParser.getMillisecondsFromString(s);
    long retM = depM + MyDateParser.MilisecondsCalculator(7);

    // butoane pasageri
    TextView adults;
    TextView childs;
    TextView infants;
    Button adultIncrementBtn;
    Button adultDecreaseBtn;
    Button childIncrementBtn;
    Button childDecreaseBtn;
    Button infantIncrementBtn;
    Button infantDecreaseBtn;

    // buton cautare
    Button searchButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_searchform_layout, container, false);
        // initiez toate butoanele
        setWidgets(view);
        return view;
    }

    /**
     * override interface method
     * @param view pe care s-a facut click
     */
    public void onClick(View view) {
        // click botones ida / ida-vuelta
        switch (view.getId()) {
            case R.id.oneway_button:
                // escondo el botton para seleccionar dia de vuelta
                returnDayValue.setEnabled(false);
                returnDayValue.setText("X");
                // si se pulsa el boton de "solo ida" cambio el estilo de los dos botones
                view.setBackgroundResource(R.drawable.oneway_active_button);
                onewayButton.setTextColor(Color.WHITE);
                roundtripButton.setBackgroundResource(R.drawable.roundtrip_inactive_button);
                roundtripButton.setTextColor(Color.parseColor("#19ccbc"));
                tripType = "false";
                break;
            case R.id.roundtrip_button:
                // restauramos la imagen y el textView para fecha vuelta
                returnDayValue.setEnabled(true);

                returnDayValue.setText(MyDateParser.getDayFromTime(
                        MyDateParser.getMillisecondsFromString(
                                hideDepartureDate.getText().toString())+ 86400000)+"");
                returnMonthAndYearValue.setText(MyDateParser.getMonthAndYearFromTime(
                        MyDateParser.getMillisecondsFromString(
                                hideDepartureDate.getText().toString()) + 86400000));
                // se ha pulsado el boton "ida-y-vuelta" cambio el estilo de los botones
                view.setBackgroundResource(R.drawable.roundtrip_active_button);
                onewayButton.setBackgroundResource(R.drawable.oneway_inactive_button);
                roundtripButton.setTextColor(Color.WHITE);
                onewayButton.setTextColor(Color.parseColor("#19ccbc"));
                tripType = "true";
                break;
        }
        // click pe unul din butoanele del calendar
        if (view.getId() == R.id.departureDayValue || view.getId() == R.id.returnDayValue) {
            DatePickerFragment newFragment = new DatePickerFragment();
            // id button apasat
            newFragment.setClickedButtonId(view.getId());
            // trimit timpul in milisecunde catre DatePickerFragment
            // arat el datePicker
            newFragment.show(getFragmentManager(), "date");
        }
    }

    // metodo obligatorio de la interfaz OnItemSelectedListener

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // pozitie item selectat
        int positionSelected = parent.getSelectedItemPosition();

        // ciudades de llegada
        // usamos ArrivalCitySetter pentru a popula spinner-ul de intors
        ArrayAdapter<String> arrivalAdapter = new ArrayAdapter<>(this.getActivity(),
                R.layout.spinner_row, arrivalCityArray.getArrivalCity(positionSelected));
        arrivalSpinner.setAdapter(arrivalAdapter);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // listener butoane pasageri
    View.OnClickListener passengerBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // id buton ce a chemat listenerul
            int pressedBtn = view.getId();

            // din text in integer
            int adultsNumber = Integer.parseInt(adults.getText().toString()) ;
            int childsNumber = Integer.parseInt(childs.getText().toString());
            int infantsNumber = Integer.parseInt(infants.getText().toString());

            // filtrez eventul
            if (pressedBtn == adultIncrementBtn.getId()) {
                adults.setText((adultsNumber + 1) + "");

            }else if (pressedBtn == adultDecreaseBtn.getId()) {
                if (adultsNumber > 1) {
                    adults.setText((adultsNumber - 1) + "");

                    // nu putem avea mai multi infanti decat adulti
                    if (infantsNumber > Integer.parseInt(adults.getText().toString())) {
                        infants.setText(adults.getText().toString());
                    }
                } else {
                    Toast.makeText(view.getContext(), "Minim un adult", Toast.LENGTH_SHORT).show();
                }
            } else if (pressedBtn == childIncrementBtn.getId()) {

                childs.setText((childsNumber + 1) + "");

            } else if (pressedBtn == childDecreaseBtn.getId()) {

                if (childsNumber > 0) {
                    childs.setText((childsNumber - 1) + "");
                }

            } else if (pressedBtn == infantIncrementBtn.getId()) {
                if (infantsNumber + 1 <= adultsNumber) {
                    infants.setText((infantsNumber + 1) + "");
                } else {
                    Toast.makeText(view.getContext(), "Doar un infant x adult",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (pressedBtn == infantDecreaseBtn.getId()) {

                if (infantsNumber > 0) {
                    infants.setText((infantsNumber - 1) + "");
                }
            }
        }
    }; // fin listener

    // listener buton cautare
    View.OnClickListener searchButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            final String DEPARTURE = departureSpinner.getSelectedItem().toString();
            final String ARRIVAL = arrivalSpinner.getSelectedItem().toString();
            // pentru a stabili intervalul de cautare am nevoie de doua dati: startd si endd

            final String DEPARTURE_START_DATE = hideDepartureDate.getText().toString();
            final String RETURN_START_DATE = hideReturnDate.getText().toString();

            // 7 zile dupa data plecarii, daca avem zbor dus intors
            long d = MyDateParser.getMillisecondsFromString(DEPARTURE_START_DATE) +
                    MyDateParser.MilisecondsCalculator(7);
            String l;
            // zbor doar dus, caut pe o perioada de 14 zile
            if (MyDateParser.getMillisecondsFromString(RETURN_START_DATE) < d && returnDayValue.isEnabled()) {
                l = RETURN_START_DATE;
            } else if (!returnDayValue.isEnabled()) {
                l = MyDateParser.getDateStringFromMiliseconds((d + MyDateParser.MilisecondsCalculator(7)));
            } else {
                 l = MyDateParser.getDateStringFromMiliseconds(d);
            }
            final String DEPARTURE_END_DATE = l;

            // data intoarcerii
            long f = MyDateParser.getMillisecondsFromString(RETURN_START_DATE) +
                    MyDateParser.MilisecondsCalculator(7);
            final String RETURN_END_DATE = MyDateParser.getDateStringFromMiliseconds(f);

            final String PAX_ADT = adults.getText().toString();
            final String PAX_CHD = childs.getText().toString();
            final String PAX_INF = infants.getText().toString();
            final String TRIPTYPE = tripType;

            // intent
            Intent intent = new Intent(view.getContext(), ResultsMainActivity.class);
            intent.putExtra("departure",DEPARTURE);
            intent.putExtra("arrival",ARRIVAL);
            intent.putExtra("departureStartDate",DEPARTURE_START_DATE);
            intent.putExtra("departureEndDate", DEPARTURE_END_DATE);
            intent.putExtra("returnStartDate",RETURN_START_DATE);
            intent.putExtra("returnEndDate", RETURN_END_DATE);
            intent.putExtra("pax_adt",PAX_ADT);
            intent.putExtra("pax_chd",PAX_CHD);
            intent.putExtra("pax_inf",PAX_INF);
            intent.putExtra("triptype",TRIPTYPE);

            // verific daca am orasul de plecare setat
            if (departureSpinner.getSelectedItem().equals("Oras plecare")) {
                Toast.makeText(getActivity(),
                        "Trebuie sa alegeti un oras de plecare",
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                startActivity(intent);
            }
        }
    };

    /**
     * seteaza toate butoanele din view
     * @param view el view principal
     */
    private void setWidgets(View view) {
        // setamos listeners para los dos botones
        onewayButton = (Button) view.findViewById(R.id.oneway_button);
        onewayButton.setOnClickListener(this);
        roundtripButton = (Button) view.findViewById(R.id.roundtrip_button);
        roundtripButton.setOnClickListener(this);

        // spinner ciudades de salida & listener
        departureSpinner = (Spinner) view.findViewById(R.id.departure_spinner);
        departureSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> depAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.departure_city_array, R.layout.spinner_row);
        departureSpinner.setAdapter(depAdapter);

        // spinner ciudades llegada
        arrivalSpinner = (Spinner) view.findViewById(R.id.arrival_spinner);

        departureDayValue = (TextView) view.findViewById(R.id.departureDayValue);
        departureDayValue.setOnClickListener(this);
        departureDayValue.setText(MyDateParser.getDayFromTime(depM) + "");
        departureMonthAndYearValue = (TextView) view.findViewById(R.id.departureMonthAndYear);
        departureMonthAndYearValue.setText(MyDateParser.getMonthAndYearFromTime(depM));

        returnDayValue = (TextView) view.findViewById(R.id.returnDayValue);
        returnDayValue.setOnClickListener(this);
        returnDayValue.setText(MyDateParser.getDayFromTime(retM) + "");
        returnMonthAndYearValue = (TextView) view.findViewById(R.id.returnMonthAndYear);
        returnMonthAndYearValue.setText(MyDateParser.getMonthAndYearFromTime(retM));
        // text view ascuns pentru data plecare / intoarcere
        hideDepartureDate = (TextView) view.findViewById(R.id.hideDepartureTime);
        hideReturnDate = (TextView) view.findViewById(R.id.hideReturnTime);
        hideDepartureDate.setText(MyDateParser.getDateStringFromMiliseconds(depM));
        hideReturnDate.setText(MyDateParser.getDateStringFromMiliseconds(retM));

        // Textview numar pasageri
        adults =  (TextView) view.findViewById(R.id.adultsNumber);
        childs  = (TextView) view.findViewById(R.id.childsNumber);
        infants = (TextView) view.findViewById(R.id.infantsNumber);

        // butoanele "+" si "-"
        adultIncrementBtn = (Button) view.findViewById(R.id.adultIncrementBtn);
        adultDecreaseBtn = (Button) view.findViewById(R.id.adultDecreaseBtn);
        childDecreaseBtn = (Button) view.findViewById(R.id.childDecreaseBtn);
        childIncrementBtn = (Button) view.findViewById(R.id.childIncrementBtn);
        infantIncrementBtn = (Button) view.findViewById(R.id.infantIncrementBtn);
        infantDecreaseBtn = (Button) view.findViewById(R.id.infantDecreaseBtn);

        // atasam un listener pentru fiecare buton "+" si "-"
        adultIncrementBtn.setOnClickListener(passengerBtnListener);
        adultDecreaseBtn.setOnClickListener(passengerBtnListener);
        childIncrementBtn.setOnClickListener(passengerBtnListener);
        childDecreaseBtn.setOnClickListener(passengerBtnListener);
        infantIncrementBtn.setOnClickListener(passengerBtnListener);
        infantDecreaseBtn.setOnClickListener(passengerBtnListener);

        // buton cautare
        searchButton = (Button) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(searchButtonListener);
    }
}