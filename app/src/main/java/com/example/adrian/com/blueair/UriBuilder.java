package com.example.adrian.com.blueair;

import android.net.Uri;
import android.os.Bundle;

/**
 * uri builder.
 */
public class UriBuilder {
    private Bundle bundle;

    UriBuilder(Bundle b){
        bundle = b;
    }
    final String BASE_URL = "http://93.113.0.179/checkAvailability.aspx?";
    final String USER = "locnetapi";
    final String PASS = "Locnetapi-1";
    final String DOMAIN = "EXT";
    final String CARRIER = "0b";
    final String CURRENCY = "EUR";


    public String getDepartureUriString(){
        Uri buildDepartureUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("departure",ArrivalCitySetter.getIataCodeByName(bundle.getString("departure")))
                .appendQueryParameter("arrival",ArrivalCitySetter.getIataCodeByName(bundle.getString("arrival")))
                .appendQueryParameter("carrier",CARRIER)
                .appendQueryParameter("startd",bundle.getString("departureStartDate"))
                .appendQueryParameter("endd",bundle.getString("departureEndDate"))
                .appendQueryParameter("currency",CURRENCY)
                .appendQueryParameter("pax_adt",bundle.getString("pax_adt"))
                .appendQueryParameter("pax_chd",bundle.getString("pax_chd"))
                .appendQueryParameter("pax_inf",bundle.getString("pax_inf"))
                .appendQueryParameter("user",USER)
                .appendQueryParameter("pass",PASS)
                .appendQueryParameter("domain",DOMAIN)
                .build();
        return buildDepartureUri.toString();
    }

    public String getReturnUriString(){
        Uri buildReturnUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("departure",ArrivalCitySetter.getIataCodeByName(bundle.getString("arrival")))
                .appendQueryParameter("arrival",ArrivalCitySetter.getIataCodeByName(bundle.getString("departure")))
                .appendQueryParameter("carrier",CARRIER)
                .appendQueryParameter("startd",bundle.getString("returnStartDate"))
                .appendQueryParameter("endd",bundle.getString("returnEndDate"))
                .appendQueryParameter("currency",CURRENCY)
                .appendQueryParameter("pax_adt",bundle.getString("pax_adt"))
                .appendQueryParameter("pax_chd",bundle.getString("pax_chd"))
                .appendQueryParameter("pax_inf",bundle.getString("pax_inf"))
                .appendQueryParameter("user",USER)
                .appendQueryParameter("pass",PASS)
                .appendQueryParameter("domain",DOMAIN)
                .build();
        return buildReturnUri.toString();
    }
}
