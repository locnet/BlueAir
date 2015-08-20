package com.example.adrian.com.blueair;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * BlueAir app, created on 16/09/14.
 */
public class WeatherFragment extends Fragment {

    final String DEGREE = "\u00b0";
    private Bundle intentExtras;
    int count;
    // WheatherHttpClient object
    WeatherHttpClient weatherTask = new WeatherHttpClient();

    // layout clima, oras plecare
    TextView depLocation;
    ImageView depWeatherImage;
    TextView departureActualTempView;
    TextView departureMaxTempView;
    TextView departureMinTempView;

    // layout clima, oras sosire
    TextView arrLocation;
    ImageView  arrWeatherImage;
    TextView arrivalActualTempView;
    TextView arrivalMaxTempView;
    TextView arrivalMinTempView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate the spinner_row for this fragment
        View view = inflater.inflate(R.layout.weather_layout, container, false);

        // layout clima oras plecare
        depLocation = (TextView) view.findViewById(R.id.departureLocation);
        depWeatherImage = (ImageView) view.findViewById(R.id.departureWeatherIcon);
        departureActualTempView = (TextView) view.findViewById(R.id.actualTemperature);
        departureMaxTempView = (TextView) view.findViewById(R.id.departureMaxTempView);
        departureMinTempView = (TextView) view.findViewById(R.id.departureMinTempView);

        // layout clima oras sosire
        arrLocation = (TextView) view.findViewById(R.id.arrivalLocation);
        arrWeatherImage = (ImageView) view.findViewById(R.id.arrivalWeatherIcon);
        arrivalActualTempView = (TextView) view.findViewById(R.id.arrivalActualTemperature);
        arrivalMaxTempView = (TextView) view.findViewById(R.id.arrivalMaxTempView);
        arrivalMinTempView = (TextView) view.findViewById(R.id.arrivalMinTempView);

        // oras de plecare si oras sosire
        intentExtras =  getActivity().getIntent().getExtras();
        String departure = intentExtras.get("departure").toString();
        String arrival = intentExtras.get("arrival").toString();

        // execut parserul Json
        new GetWeatherJson().execute(fixDestinations(departure));
        new GetWeatherJson().execute(fixDestinations(arrival));

        count = 0;
        return view;
    }

    private class GetWeatherJson extends AsyncTask<String, ProgressDialog, String>{


        @Override
        protected String doInBackground(String... urls) {
            return weatherTask.getWeatherData(urls[0]);
        }

        @Override
        protected void onPostExecute(String string) {

            WeatherJsonParser.setJsonObject(string);
            depLocation.setText(intentExtras.getString("departure"));

            if (count == 0) {
                // es la primera vez que paso por el script
                setWeatherIcon.setDepartureIcon(depWeatherImage,WeatherJsonParser.getIconCode(),
                        WeatherJsonParser.getTimestamp());
                //actualizez textul temperatura minima si maxima oras plecare
                departureActualTempView.setText("" + WeatherJsonParser.getTemp() + DEGREE);
                departureMaxTempView.setText(WeatherJsonParser.getMaxTemp() + DEGREE);
                departureMinTempView.setText(WeatherJsonParser.getMinTemp() + DEGREE);
            } else if (count == 1){
                // es la segunda vez que paso por el script, actualizo la temperatura para
                // la ciudad de llegada
                arrLocation.setText(intentExtras.getString("arrival"));
                // setez imaginea de la imageView
                setWeatherIcon.setDepartureIcon(arrWeatherImage,WeatherJsonParser.getIconCode(),
                        WeatherJsonParser.getTimestamp());

                //actualizez textul temperatura minima si maxima oras plecare
                arrivalActualTempView.setText("" + WeatherJsonParser.getTemp() + DEGREE);
                arrivalMaxTempView.setText(WeatherJsonParser.getMaxTemp() + DEGREE);
                arrivalMinTempView.setText(WeatherJsonParser.getMinTemp() + DEGREE);
            }

            count += 1;
        }
    }

    /**
     * multe destinatii au numele compus, gen Madrid-Barajas, si unele nu sunt mapeate corect
     * @param string destinatia in forma de string care o verificam
     * @return doar destinatia pana la caracterul "-"
     */
    private String fixDestinations(String string) {
        String s = string;
        if(s.equals("Paris-Beveauis")){
            s = "Paris, France";
        }
        if(s.equals("Catania-Fontanarossa")){
            s = "Catania";
        }
        if(s.equals("Londra-Lutton")){
            s = "London";
        }
        if(s.equals("Milano-Bergamo") || s.equals("Milano-Linate")){
            s = "Milano";
        }
        if (s.equals("Dublin")){
            s = "Dublin,Ireland";
        }
        return s;
    }
}
