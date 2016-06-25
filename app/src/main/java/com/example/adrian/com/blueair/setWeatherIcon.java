package com.example.adrian.com.blueair;

import android.widget.ImageView;

import java.util.Calendar;

/**
 * Created by adrian on 14/04/15.
 */
public class setWeatherIcon {


    public static void setDepartureIcon(ImageView view,int code,long timestamp) {


        String time = getDayHour();
        /**
         * furtuna cu fulgere, de la cod 200 la 232 e acelasi icono
         */
        if (code < 233){
            if (time.equals("day")){
                view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon11d));
            }else{
                view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon11n));
            }
            /**
             * ploaie usoara, acelasi cod de la 300 la 322
             */
        } else if (code >= 300 && code < 322) {
            if (time.equals("day")){
                view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon09d));
            }else{
                view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon09n));
            }

            /**
             * ploaie
             */
        } else if (code >= 500 && code < 532) {
            if (code >= 500 && code <= 504){
                if (time.equals("day")){
                    view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon10d));
                }else{
                    view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon10n));
                }
            } else if (code == 511) {
                if (time.equals("day")){
                    view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon13d));
                }else{
                    view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon13n));
                }
            } else {
                if (time.equals("day")){
                    view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon09d));
                }else{
                    view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon09n));
                }
            }

            /**
             * ninsoare
             */
        } else if (code >= 600 && code < 623) {
            if (time.equals("day")){
                view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon13d));
            }else{
                view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon13n));
            }

            /**
             * ceata?
             */
        } else if (code >= 701 && code < 781) {
            if (time.equals("day")){
                view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon50d));
            }else{
                view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon50n));
            }

            /**
             * nori sau soare
             */
        } else if (code >= 800 && code < 805) {
            switch (code) {
                case 800:
                    if (time.equals("day")){
                        view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon01d));
                    }else{
                        view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon01n));
                    }
                    break;
                case 801:
                    if (time.equals("day")){
                        view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon02d));
                    }else{
                        view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon02n));
                    }
                    break;
                case 802:
                    if (time.equals("day")){
                        view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon03d));
                    }else{
                        view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon03n));
                    }
                    break;
                case 803:
                    if (time.equals("day")){
                        view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon03d));
                    }else{
                        view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon03n));
                    }
                    break;
                case 804:
                    if (time.equals("day")){
                        view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon03d));
                    } else {
                        view.setImageDrawable(view.getResources().getDrawable(R.drawable.icon03n));
                    }
                    break;
            }
        }
    }

    /**
     * daca ora actuala este intre 6 si 22 inapoiez "day", daca nu inapoiez "night"
     * @return zi sau noapte
     */
    private static String getDayHour() {
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.HOUR_OF_DAY) > 6 && cal.get(Calendar.HOUR_OF_DAY) < 22) {
            return "day";
        } else {
            return "night";
        }
    }
}
