package com.example.adrian.com.blueair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Rezultate cautare 16/10/14.
 */
public class ResultsFragment extends Fragment  {

    public ResultsFragment(){
        // Required empty public constructor
    }
    Activity mActivity;
    LinearLayout topList_header;            // header rezultate dus
    LinearLayout bottomList_header;         // header rezultate intoarcere
    LinearLayout noResultError;             // layer ascuns, il arat doar daca nu am rezultate
    TextView topListDepCity;
    TextView topListArrCity;
    TextView bottomListDepCity;
    TextView bottomListArrCity;
    HorizontalListView departureFlightList; // lista rezultate cautare dus
    HorizontalListView returnFlightList;    // lista rezultate cautare intors

    // detalii zbor dus departure_flight_detail_layout
    LinearLayout departureFlightDetailsLayout;
    TextView hideDepSTD;
    TextView hideDepSTA;
    TextView departureSeats;
    TextView depFlightNr;
    ImageButton departureCloseButton;
    TextView departureDiscount;

    // detalii zbor intors return_flight_detail_layout
    LinearLayout returnFlightDetailsLayout;
    TextView hideRetSTD;
    TextView hideRetSTA;
    TextView returnSeats;
    TextView retFlightNr;
    ImageButton returnCloseButton;
    TextView returnDiscount;

    Bundle b;                               // bundle cu datele  din intent
    private int tripType = 1;

    // animation
    private Animation anim = null;

    // adapterul pentru rezultate, il populez in onPostExecute
    ResultsArrayAdapter mResultAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.results_listview_fragment, container, false);

        // e o cautare noua, resetez la zero serviciile
        ServPriceCalc.resetValues();

        // seteaza valorile pentru toate layout-urile
        setWidgets(view);

        // captez datele extra din intent si formez un string cu url-ul
        String departureUrlString;
        String returnUrlString;

        // extra din intent
        b =  getActivity().getIntent().getExtras();
        UriBuilder bundle = new UriBuilder(b);

        // animation
        anim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.list_item_animation);

        // titlu cu ruta -- zbor dus
        topListDepCity.setText(ArrivalCitySetter.getIataCodeByName(b.getString("departure")));
        topListArrCity.setText(ArrivalCitySetter.getIataCodeByName(b.getString("arrival")));

        // titlu cu ruta -- zbor intors
        bottomListDepCity.setText(ArrivalCitySetter.getIataCodeByName(b.getString("arrival")));
        bottomListArrCity.setText(ArrivalCitySetter.getIataCodeByName(b.getString("departure")));

        // url dus
        departureUrlString = bundle.getDepartureUriString();
        new DownloadXmlTask().execute(departureUrlString);

        // zbor dus-intors?
        if (b.getString("triptype").equals("true")) {
            returnUrlString = bundle.getReturnUriString();
            new DownloadXmlTask().execute(returnUrlString);
            bottomList_header.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private OnFragmentInteractionListener mListener;

    /**
     * listener listView pentru rezultate dus
     * mListener cheama metdoda onFragmentIteraction din interfata onFragmentInteractionListener
     * si transmite activitati el View din care este chemat listenerul
     */
    private AdapterView.OnItemClickListener depItemClickedHandler = new AdapterView.OnItemClickListener() {

        /**
         *
         * @param adapterView
         * @param view
         * @param position
         * @param l
         */
        @Override
        public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {
        // legatura cu ResultMainActivity
        mListener.onFragmentInteraction(view, "departureSegment");

        // cand userul face click pe un zbor se schimba culoarea la background
        // schimbul se face in clasa HorizontalListView
        TextView amount =(TextView) view.findViewById(R.id.amount);
        if (!amount.getText().toString().equals("Vandute")) {
            departureFlightList.setSelection(position, view);

            // setez detaliile zborului si chem animatia
            setFlightDetails(view, "departure");
            Animation slide = AnimationUtils.loadAnimation(getActivity(),R.anim.flights_result_animation);
            if (departureFlightDetailsLayout.getVisibility() == View.GONE) {
                departureFlightDetailsLayout.startAnimation(slide);
            } else {
                slide = AnimationUtils.loadAnimation(getActivity(),R.anim.list_item_animation);
                departureFlightDetailsLayout.startAnimation(slide);
            }
            departureFlightDetailsLayout.setVisibility(View.VISIBLE);
        }

        departureCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation aClose = AnimationUtils.loadAnimation(getActivity(),R.anim.close_fligths_details);
                departureFlightDetailsLayout.startAnimation(aClose);
                departureFlightDetailsLayout.setVisibility(View.GONE);
            }
        });

        // animation
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {
                BlueAirXmlParser.Entry item = mResultAdapter.getItem(position);
                mResultAdapter.remove(item);
            }
        });
        view.startAnimation(anim);
        }
    };

    /**
     * listener listView rezultate intoarcere
     */
    private AdapterView.OnItemClickListener retItemClickedHandler = new AdapterView.OnItemClickListener() {

        /**
         *
         * @param adapterView
         * @param view
         * @param position
         * @param l
         */
        @Override
        public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {
            mListener.onFragmentInteraction(view,"returnSegment");

            TextView amount =(TextView) view.findViewById(R.id.amount);
            // cand userul face click pe un zbor se schimba culoarea la background
            // schimbul se face in clasa HorizontalListView
            if (!amount.getText().toString().equals("Vandute")) {
                returnFlightList.setSelection(position, view);

                setFlightDetails(view, "return");
                Animation slide = AnimationUtils.loadAnimation(getActivity(),R.anim.flights_result_animation);
                // vreau ca animatia sa ruleze doar daca layout-ul nu este vizibil
                if (returnFlightDetailsLayout.getVisibility() == View.GONE) {
                    returnFlightDetailsLayout.startAnimation(slide);
                } else {
                    slide = AnimationUtils.loadAnimation(getActivity(),R.anim.list_item_animation);
                    returnFlightDetailsLayout.startAnimation(slide);
                }
                returnFlightDetailsLayout.setVisibility(View.VISIBLE);
            }

            // listener buton de inchis layout detalii
            returnCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation aClose = AnimationUtils.loadAnimation(getActivity(),R.anim.close_fligths_details);
                    returnFlightDetailsLayout.startAnimation(aClose);
                    returnFlightDetailsLayout.setVisibility(View.GONE);
                }
            });

            // animation
            anim.setAnimationListener(new Animation.AnimationListener(){
                @Override
                public void onAnimationStart(Animation animation) {

                }
                @Override
                public void onAnimationEnd(Animation animation) {

                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                    BlueAirXmlParser.Entry item = mResultAdapter.getItem(position);
                    mResultAdapter.remove(item);
                }
            });
            view.startAnimation(anim);
        }
    };


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " trebuie implementata interfata OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Aceasta interfata trebuie implementata de catre activitatile ce contin
     * acest fragment pentru ca orice interactiune cu acest fragment sa fie
     * comunicata activitati si altor fragmente din activitatea respectiva
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(View view, String segment);
    }

    /**
     * clasa care creaza conexiunea si proceseaza XML-ul cu rezultatele cautarii
     */
    private class DownloadXmlTask extends AsyncTask<String, ProgressDialog, List<BlueAirXmlParser.Entry>> {
        private ProgressDialog progressDialog;
        private String mException = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected List<BlueAirXmlParser.Entry> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                progressDialog.dismiss();
                // erroare de conexiune
                mException = getResources().getString(R.string.connection_error);
                return null;
            } catch (XmlPullParserException e) {
                progressDialog.dismiss();
                // erroare de xml
                mException = getResources().getString(R.string.xml_error);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<BlueAirXmlParser.Entry> result) {
            progressDialog.dismiss();
            if (mException != null) {
                topList_header.setVisibility(View.GONE);
                bottomList_header.setVisibility(View.GONE);
                noResultError.setVisibility(View.VISIBLE);
                return;
            }
            ArrayList<BlueAirXmlParser.Entry> departureArrayOfResults;
            ArrayList<BlueAirXmlParser.Entry> returnArrayOfResults;
            // metoda a fost chemata pt. zborul de dus
            if (tripType == 1) {
                departureArrayOfResults = (ArrayList) result;
                mResultAdapter = new ResultsArrayAdapter(getActivity(), departureArrayOfResults);
                departureFlightList.setAdapter(mResultAdapter);
                departureFlightList.setOnItemClickListener(depItemClickedHandler); // set listener
                tripType++;
            // metoda chemata pentru zborul de intoarcere
            } else if (tripType == 2) {
                returnArrayOfResults = (ArrayList) result;
                mResultAdapter = new ResultsArrayAdapter(getActivity(), returnArrayOfResults);
                // listView-ul de intoarcere este ascuns in mod normal
                returnFlightList.setVisibility(View.VISIBLE);
                returnFlightList.setAdapter(mResultAdapter);
                returnFlightList.setOnItemClickListener(retItemClickedHandler); // set listener
            }
        }
    }

    /**
     * @param urlString url-ul de unde obtin xml
     * @return rezultatele gasite
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<BlueAirXmlParser.Entry> loadXmlFromNetwork(String urlString)
            throws XmlPullParserException, IOException {

        InputStream stream = null, in;
        // Instantiate the parser
        BlueAirXmlParser blueAirXmlParser = new BlueAirXmlParser();
        List<BlueAirXmlParser.Entry> entries;
        String cleanStream;

        try {
            /* normal ar fi sa returnam direct stream-ul, dar am nevoie sa elimin
            *  primele 3 caractere din raspunsul xml fiindca incepe cu caractere "OK:"  */
            stream = downloadUrl(urlString);
            // transform stream-ul in string si tai primele 3 caractere
            cleanStream = getStringFromStream(stream);

            // transform din string in stream din nou
            in = new ByteArrayInputStream(cleanStream.getBytes());
            entries = blueAirXmlParser.parse(in);
            return entries;

        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
     *
     * @param urlString adresa url
     * @return stream cu rezultatele de la cautare
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

    /**
     * functie ajutatoare
     * primele 3 caractere din raspuns impiedica un parseat corect, le elmin
     * @param in streamul cu care lucrez
     * @return streamul fara primele 3 caractere
     * @throws IOException
     */
    public static String getStringFromStream(InputStream in) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        return out.toString().substring(3);
    }

    /**
     * initializeaza valorile pentru toate widget-urile
     * @param view view pe care s-a facut click
     */
    private void setWidgets(View view) {
        // layout-uri care sunt deasupra listelor cu rezultate

        topList_header = (LinearLayout) view.findViewById(R.id.topList_header);
        // layout erroare de cautare, nu am rezultate
        noResultError = (LinearLayout) view.findViewById(R.id.noResultsError);

        bottomList_header = (LinearLayout) view.findViewById(R.id.bottomList_header);
        // header lista rezutlate zbor dus
        topListDepCity = (TextView) view.findViewById(R.id.topList_depCity);
        topListArrCity = (TextView) view.findViewById(R.id.topList_arrCity);

        // ListView 's rezultate
        departureFlightList = (HorizontalListView) view.findViewById(R.id.departureListView);

        bottomListDepCity = (TextView) view.findViewById(R.id.bottomList_depCity);
        bottomListArrCity = (TextView) view.findViewById(R.id.bottomList_arrCity);
        returnFlightList = (HorizontalListView) view.findViewById(R.id.returnListView);

        // departure_flights_detail_layout
        departureFlightDetailsLayout = (LinearLayout) view.findViewById(R.id.departureFlightDetailsLayout);
        hideDepSTD = (TextView) view.findViewById(R.id.hideDepartureSTD);
        hideDepSTA = (TextView) view.findViewById(R.id.hideDepartureSTA);
        departureSeats = (TextView) view.findViewById(R.id.hideAvailableCount);
        depFlightNr = (TextView) view.findViewById(R.id.hideFlightNumber);
        departureDiscount = (TextView) view.findViewById(R.id.departureDiscountProcent);
        departureCloseButton = (ImageButton) view.findViewById(R.id.closeLayout);

        // return_flights_detail_layout
        returnFlightDetailsLayout = (LinearLayout) view.findViewById(R.id.returnFlightDetailsLayout);
        hideRetSTD = (TextView) view.findViewById(R.id.hideReturnSTD);
        hideRetSTA = (TextView) view.findViewById(R.id.hideReturnSTA);
        returnSeats = (TextView) view.findViewById(R.id.hideReturnSeats);
        retFlightNr = (TextView) view.findViewById(R.id.hideReturnFlightNumber);
        returnDiscount = (TextView) view.findViewById(R.id.returnDiscountProcent);
        returnCloseButton = (ImageButton) view.findViewById(R.id.returnCloseButton);
    }

    /**
     * @param view
     * @param segment
     */
    private void setFlightDetails(View view, String segment) {
        //ora sosire
        TextView departureSTD = (TextView) view.findViewById(R.id.departureSTD);
        TextView departureSTA = (TextView) view.findViewById(R.id.departureSTA);
        TextView seats = (TextView) view.findViewById(R.id.availableCount);
        TextView flight = (TextView) view.findViewById(R.id.flightNumber);
        // pret
        TextView amount = (TextView) view.findViewById(R.id.amount);

        // pret copil
        TextView childAmount = (TextView) view.findViewById(R.id.childAmount);

        // discount
        TextView discont = (TextView) view.findViewById(R.id.discountProcent);

        if (segment.equals("departure")) {
            //ora sosire
            hideDepSTD.setText(departureSTD.getText().toString());
            // ora plecare
            hideDepSTA.setText(departureSTA.getText().toString());
            // locuri disponibile
            departureSeats.setText(seats.getText().toString());
            // numar zbor
            depFlightNr.setText(flight.getText().toString());
            //discount
            if (discont.getText().toString().length() > 0){
                departureDiscount.setText(discont.getText().toString());
            }

        } else {
            //ora sosire
            hideRetSTD.setText(departureSTD.getText().toString());
            // ora plecare
            hideRetSTA.setText(departureSTA.getText().toString());
            // locuri disponibile
            returnSeats.setText(seats.getText().toString());
            // numar zbor
            retFlightNr.setText(flight.getText().toString());
            // discount
            if (discont.getText().toString().length() > 0){
                returnDiscount.setText(discont.getText().toString());
            }
        }
    }
}
