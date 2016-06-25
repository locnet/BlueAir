package com.example.adrian.com.blueair;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * conexiunea cu api de openweathermap.org
 */
public class WeatherHttpClient {

    // Will contain the raw JSON response as a string.
    String forecastJsonStr = null;
    String format = "json";
    String units = "metric";
    final String appid = "35347d093e8d761a399380e8f24f6730";
    int numDays = 1;

    /**
     *
     * @param location orasul pentru care cer datele
     * @return
     */

    public String getWeatherData(String location) {
        HttpURLConnection con = null ;
        InputStream is = null;

        try {

            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String FORECAST_BASE_URL =
                    "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_PARAM = "q";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String APPID ="APPID";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM,location)
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(UNITS_PARAM, units)
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                    .appendQueryParameter(APPID,appid)
                    .build();

            // Create the request to OpenWeatherMap, and open the connection
            URL url = new URL(builtUri.toString());
            System.out.println(url);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }
}
