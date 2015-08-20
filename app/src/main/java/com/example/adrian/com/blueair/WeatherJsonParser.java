package com.example.adrian.com.blueair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * clasa pentru a parsea vremea
 */
public class WeatherJsonParser {

    private static JSONObject myJsonObject;
    private static JSONArray weatherArray;
    // Get the JSON object representing the day
    private static JSONObject dayForecast;
    private static JSONObject temperatureObject;
    private static JSONObject weatherObject;



    // These are the names of the JSON objects that need to be extracted.
    final static String OWM_LIST = "list";
    final static String OWM_WEATHER = "weather";
    final static String OWM_TIMESTAMP = "dt";
    final static String OWM_TEMPERATURE = "temp";
    final static String OWM_MAX = "max";
    final static String OWM_MIN = "min";
    final static String OWM_DAY_TEMP = "day";
    final static String OWM_CODE = "id";

    /**
     *
     * @param data raspunsul JSON in forma de string
     */
    public static void setJsonObject(String data){
        try{
            myJsonObject = new JSONObject(data);
            weatherArray = myJsonObject.getJSONArray(OWM_LIST);

            temperatureObject = weatherArray.getJSONObject(0).getJSONObject(OWM_TEMPERATURE);

            weatherObject = weatherArray.getJSONObject(0).getJSONArray(OWM_WEATHER).getJSONObject(0);
        } catch (JSONException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * inapoiaza un timestamp in format Linux
     */
    public static long getTimestamp(){
        try{
            return weatherArray.getJSONObject(0).getLong(OWM_TIMESTAMP);
        } catch (JSONException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * inapoiaza temperatura actuala
     */
    public static int getTemp(){
        try{
            double high = temperatureObject.getDouble(OWM_DAY_TEMP);
            return (int) Math.round(high);
        }catch (JSONException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * inapoiaza temperatura minima
     */
    public static int getMinTemp(){
        try{
            double min = temperatureObject.getDouble(OWM_MIN);
            return (int) Math.round(min);
        }catch (JSONException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * inapoiaza temperatura maxima
     */
    public static int getMaxTemp(){
        try{
            double max = temperatureObject.getDouble(OWM_MAX);
            return (int) Math.round(max);
        }catch (JSONException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * inapoiaza codul corespunzator
     */
    public static int getIconCode(){
        try{
            int code = weatherObject.getInt(OWM_CODE);
            System.out.println("Am luat codul: " + code);
            return code;
        }catch (JSONException e){
            throw new RuntimeException(e);
        }
    }
}
