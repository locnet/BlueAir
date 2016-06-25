package com.example.adrian.com.blueair;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * ARRAYADAPTER PERSONALIZAT
 */
public class ResultsArrayAdapter extends ArrayAdapter<BlueAirXmlParser.Entry>{

    public ResultsArrayAdapter(Context context,  List<BlueAirXmlParser.Entry> objects) {
        super(context, 0, objects);
    }

    // pentru formatar pretul
    private static final String NR_FORMAT = "###,###.00";
    DecimalFormat decimalFormat = new DecimalFormat(NR_FORMAT);

    /**
     * am nevoie de un array pentru culoarea de la fiecare item in functie de pret
     * o folosesc in ResultsFragment.java
     */
    public int[] circleColor = new int[this.getCount()];

    @Override
    public View getView(int position, View view, ViewGroup parent) {


        // creamos el View si il populam cu un element din array
        BlueAirXmlParser.Entry entry = getItem(position);

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_listview_item, parent, false);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.priceLayout);
        CircleWidget circleWidget = new CircleWidget(view.getContext(), linearLayout.getWidth(),
                entry.AdtAmount,  getRanking(this, entry.AdtAmount));
        linearLayout.setBackground(circleWidget.getCircle());

        // calculez milisecundele
        String d = entry.DepartureDate.substring(0,10);  // data actuala, format yyyy-mm-dd
        long n = MyDateParser.getMillisecondsFromString(d);

        // ziua plecarii
        TextView dayName = (TextView) view.findViewById(R.id.dayPriceText);
        dayName.setText(MyDateParser.getDayNameFromTime(n));

        // data plecarii
        TextView departureDateText = (TextView) view.findViewById(R.id.departureDate);
        departureDateText.setText(MyDateParser.getDayFromTime(n)+" " + MyDateParser.getMonthAndYearFromTime(n));

        // pret copil, nu este vizibil
        TextView hideChildAmount = (TextView) view.findViewById(R.id.childAmount);

        /**
         * data plecarii in format standard pentru a  putea diferentia pretul bagajului intre
         * sezon estival sau sezon normal
         * @see com.example.adrian.com.blueair.ResultsMainActivity
         */
        TextView depStandardDate = (TextView) view.findViewById(R.id.depStandardDate);
        depStandardDate.setText(d);

        TextView departureSTDText = (TextView) view.findViewById(R.id.departureSTD);
        departureSTDText.setText(entry.STD.substring(11,16));

        TextView departureSTAText = (TextView) view.findViewById(R.id.departureSTA);
        departureSTAText.setText(entry.STA.substring(11,16));

        TextView availableCountText = (TextView) view.findViewById(R.id.availableCount);
        availableCountText.setText("Disponibile: " + entry.AvailableCount);

        TextView flightNumberText = (TextView) view.findViewById(R.id.flightNumber);
        flightNumberText.setText("Zbor: 0B" + entry.FlightNumber);

        TextView amountText = (TextView) view.findViewById(R.id.amount);

        TextView discountText = (TextView) view.findViewById(R.id.discountProcent);
        if (entry.AdtAmount < 1) {
            // toate biletele sunt vandute, nu am pret
            amountText.setText("Vandute");
            // adaug la array culoarea gri
            circleColor[position] = view.getResources().getColor(R.color.lightGrey);
        } else {
            // am oferta ?
            if (entry.AdtFareDiscountAmount > 0 || entry.ChdFareDiscountAmount > 0) {
                double adt = entry.AdtAmount - entry.AdtFareDiscountAmount;
                amountText.setText(decimalFormat.format(adt)  + " €");

                double chd = entry.ChdAmount - entry.ChdFareDiscountAmount;
                hideChildAmount.setText(decimalFormat.format(chd));
                discountText.setText(entry.FareDiscountProcent);
            } else {
                amountText.setText(decimalFormat.format(entry.AdtAmount) + " €");
                hideChildAmount.setText(decimalFormat.format(entry.ChdAmount)+"");
            }
        }
        // se adauga la array culoarea item-ului respectiv
        circleColor[position] = circleWidget.getCircleColor(entry.AdtAmount);
        return view;
    }

    /**
     * pentru a diferentia vizualmente pretul biletelor  am nevoie de un "ranking", cele mai
     * ieftine le punem o culoare deschisa pentru a putea fi reperate usor
     * @param entries arrayul cu rezultatele cautarii
     * @param price pretul pentru care am nevoie de ranking
     * @return int  indica rankingul, ranking 0 fiind cel mai ieftin bilet
     * @see  com.example.adrian.com.blueair.CircleWidget
     */
    private int getRanking(ResultsArrayAdapter entries, Double price) {
        double control = 1000.00;
        int ranking = 0;
        ArrayList<Double> matches = new ArrayList<>();

        for (int i = 0; i < this.getCount(); i++){
            // verific sa am pret mai mare ca 1 (sa nu fie vandute)
            if (entries.getItem(i).AdtAmount > 1) {
                // daca pretul este mai mic ca control, actualizez control
                if (entries.getItem(i).AdtAmount < control){
                    control = entries.getItem(i).AdtAmount;
                }

                // daca intrarea din ResultsArrayAdapter este mai mica decat
                // pretul nostru (price) actualizez rankingul
                if ( Double.compare(entries.getItem(i).AdtAmount, price) < 0 ) {
                    if(!matches.contains(entries.getItem(i).AdtAmount)) {
                        matches.add(entries.getItem(i).AdtAmount);
                        ranking++;
                    }
                }
            }
        }
        return ranking;
    }

    /**
     * functie ajutatoare folosita in ResultsFragment.java, pentru a seta culoarea de la
     * layout detalii zbor la fel cu cea a zborului selectionat
     * @return array ce contine culorile corespunzatoare fiecarui pret
     */
    public int[] getAdapterColors() {
        return circleColor;
    }
}