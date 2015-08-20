package com.example.adrian.com.blueair;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ResultsMainActivity extends ActionBarActivity
        implements ResultsFragment.OnFragmentInteractionListener {

    // captez fragmentele pentru a ne comunica cu ele
    WeatherFragment weatherFragment;
    ResultsFragment resultsFragment;
    PriceFragment priceFragment;
    // in intent am toti parametri de cautare
    Bundle intentExtras = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_main_activity);
        // get the intent extra
        intentExtras = getIntent().getExtras();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * captez valorile de la campurile de text din layout-ul result_listview_item.xml in
     * momentul in care este selectat, apoi le trec intr-un bundle pe care il trimit catre
     * clasa PriceFragment
     * @param view el view
     * @param segment segmentul din care este chemata interfata
     */
    @Override
    public void onFragmentInteraction(View view, String segment) {
        FragmentManager manager = getSupportFragmentManager();
        priceFragment = (PriceFragment) manager.findFragmentById(R.id.price_fragment);
        resultsFragment = (ResultsFragment) manager.findFragmentById(R.id.results_fragment);
        Bundle args = new Bundle();

        // extra din intent, aceste date vin din MainActivityBodyFragment
        if (intentExtras != null) {
            // oras plecare si intoarcere
            args.putString("departure", intentExtras.get("departure").toString());
            args.putString("arrival", intentExtras.get("arrival").toString());
            // nr. pasageri
            args.putString("pax_adt", intentExtras.get("pax_adt").toString());
            args.putString("pax_chd", intentExtras.get("pax_chd").toString());
            args.putString("pax_inf", intentExtras.get("pax_inf").toString());
            // tipul zborului
            args.putString("triptype", intentExtras.get("triptype").toString());

        } else {
            args.putString("departure", "nu am intent");
        }
        // pretul adult
        TextView amount = (TextView)  view.findViewById(R.id.amount);
        args.putString("departurePrice", amount.getText().toString());
        // pret copil
        TextView chdAmount = (TextView) view.findViewById(R.id.childAmount);
        args.putString("departureChdPrice", chdAmount.getText().toString());

        // data plecarii in format YYYY-MM-DD
        TextView departure_date = (TextView) view.findViewById(R.id.depStandardDate);
        args.putString("departure_date", departure_date.getText().toString());

        TextView depSTD = (TextView) view.findViewById(R.id.departureSTD);
        TextView depSTA = (TextView) view.findViewById(R.id.departureSTA);
        args.putString("departureSTA", depSTA.getText().toString());
        args.putString("departureSTD", depSTD.getText().toString());

        // detalii zbor intors
        if (intentExtras.get("triptype").toString().equals("true")) {
            // data intoarcerii in format YYYY-MM-DD
            TextView return_date = (TextView) view.findViewById(R.id.depStandardDate);
            args.putString("return_date", departure_date.getText().toString());

            TextView retSTD = (TextView) view.findViewById(R.id.departureSTD);
            TextView retSTA = (TextView) view.findViewById(R.id.departureSTA);
            args.putString("departureSTA", depSTA.getText().toString());
            args.putString("departureSTD", depSTD.getText().toString());
        }

        if (priceFragment != null) {
            priceFragment.priceMaker(args, segment);
        } else {
            // TODO : daca o sa fac un container diferit in care priceFragment sa nu fie vizibil
            // din start trebuie adaugat codul aici
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fetch_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            return  inflater.inflate(R.layout.result_activity_fragments_container, container, false);
        }
    }
}
