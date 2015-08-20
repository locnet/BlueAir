package com.example.adrian.com.blueair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Un parseador de fechas on 27/09/14.
 */
public class MyDateParser {

    final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // data actuala in format text, funciona corectamente
    public static String getActualDateString(){
        return dateFormat.format(new Date());
    }

    // calculam milisecundele din string in format "yyyy-MM-dd", funciona corectamente
    public static long getMillisecondsFromString(String s) {
        long m = 0;
        try {
            Date d = dateFormat.parse(s);
            m = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return m;
    }

    // para devolver al año a partir de milisecundos
    public static int getYearFromTime(long n) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(n);
        return cal.get(Calendar.YEAR);
    }

    // para devolver el mes
    public static int getMonthFromTime(long n) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(n);
        return (cal.get(Calendar.MONTH));
    }

    // para devolver el dia
    public static int getDayFromTime(long n) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(n);
        return cal.get(Calendar.DAY_OF_MONTH);
    }


    // devuelve numele zilei
    public static String getDayNameFromTime(long n) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(n);
        ArrayList<String> d = new ArrayList<String>();
        d.add("duminica"); d.add("luni");d.add("marti");
        d.add("miercuri");d.add("joi");d.add("vineri");d.add("sambata");
        return d.get(cal.get(Calendar.DAY_OF_WEEK) - 1);
    }
    // para devolver un string con el mes y el año en formato "DEC 2014"
    public static String getMonthAndYearFromTime(long n){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(n);
        ArrayList<String> m = new ArrayList<String>();
        m.add("ian."); m.add("feb.");m.add("mar.");m.add("abr.");m.add("mai");m.add("iun.");
        m.add("iul.");m.add("aug.");m.add("sep.");m.add("oct.");m.add("nov.");m.add("dec.");

        return m.get(cal.get(Calendar.MONTH)) + " "  + cal.get(Calendar.YEAR);
    }

    // devolver data in format "yyyy-MM-dd" a partir de milisecundos, funciona corectamente
    public static String getDateStringFromMiliseconds(long n) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(n);
        String month;
        if ((cal.get(Calendar.MONTH) +1) < 10) {
            month = "0" + (cal.get(Calendar.MONTH) + 1);
        } else {
            month = "" + (cal.get(Calendar.MONTH) + 1);
        }
        String day;
        if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + cal.get(Calendar.DAY_OF_MONTH);
        } else {
            day = "" + cal.get(Calendar.DAY_OF_MONTH);
        }

        return cal.get(Calendar.YEAR) + "-" + month + "-" + day;
    }

    // nu putem permite ca ziua aleasa din datepicker sa fie anterioara zilei de azi
    // devuelve false daca ziua care o comparam este mai mica decat ziua de azi
    public static boolean  checkSelectedDate(long dayToCheck) {
        long today = getMillisecondsFromString(dateFormat.format(new Date()));

        return (dayToCheck >= today);
    }

    // calculeaza milisecundele dintr-un numar de zile date
    public static long MilisecondsCalculator(int days) {
        return days * 24 * 3600 * 1000;
    }

}
