package com.example.adrian.com.blueair;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * date picker class.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    final Calendar c = Calendar.getInstance();

    int clickedButtonId;  // id textView pe care s-a facut click

    TextView departureDayValue;
    TextView departureMonthAndYearValue;

    TextView returnDayValue;
    TextView returnMonthAndYearValue;

    // fechas escondidas en textView

    TextView hideReturnDate;
    TextView hideDepartureDate;

    // capturez butonul apasat, dus sau intors
    public void setClickedButtonId(int id){
        clickedButtonId = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year;
        int month;
        int day;
        // cand se creaza dialogul captam campurile de text si butoanele

        departureDayValue = (TextView) getActivity().findViewById(R.id.departureDayValue);
        departureMonthAndYearValue = (TextView) getActivity().findViewById(R.id.departureMonthAndYear);

        returnDayValue = (TextView) getActivity().findViewById(R.id.returnDayValue);
        returnMonthAndYearValue = (TextView) getActivity().findViewById(R.id.returnMonthAndYear);

        hideDepartureDate = (TextView) getActivity().findViewById(R.id.hideDepartureTime);
        hideReturnDate = (TextView) getActivity().findViewById(R.id.hideReturnTime);

        long referenceTime;

        // daca am ziua plecarii setata, o captez
        if (departureDayValue.getId() == clickedButtonId) {
            referenceTime = MyDateParser.getMillisecondsFromString(hideDepartureDate.getText().toString());
        } else {
            referenceTime = MyDateParser.getMillisecondsFromString(hideReturnDate.getText().toString());
        }

        if (referenceTime > 0) {
            // daca am text in departureTextView il folosesc pentru a seta o data de pornire
            // in date picker
            year = MyDateParser.getYearFromTime(referenceTime);
            month = MyDateParser.getMonthFromTime(referenceTime);
            day = MyDateParser.getDayFromTime(referenceTime);
        } else {
            // nu am nici o data selectata, datepicker ia ca zi de plecare ziua de astazi
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        // creamos una nueva instace del DatePickerDialog y lo devolvemos
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // valoarea in milisecunde timp dus si intors
        long departureTime = MyDateParser.getMillisecondsFromString(hideDepartureDate.getText().toString());
        long returnTime = MyDateParser.getMillisecondsFromString(hideReturnDate.getText().toString());

        // timpul ales din datePicker
        long timeInMilliseconds = MyDateParser.getMillisecondsFromString(year + "-" + (month + 1) + "-" + day);

        // verific ca data aleasa sa nu fie anterioara zilei de azi
        boolean result = MyDateParser.checkSelectedDate(timeInMilliseconds);
        if (!result) {
            Toast.makeText(view.getContext(),"Data plecarii incorecta, incearca din nou",
                    Toast.LENGTH_SHORT).show();
        } else {

           // datePicker chemat de butonul de "dus"
            if (departureDayValue.getId() == clickedButtonId) {

                departureDayValue.setText(MyDateParser.getDayFromTime(timeInMilliseconds) + "");
                departureMonthAndYearValue.setText(MyDateParser.getMonthAndYearFromTime(timeInMilliseconds));
                // daca e cazul facem ziua de dus o zi mai tarziu ca cea de intors, in milisecunde
                if( returnTime <= timeInMilliseconds)  {

                    returnTime = timeInMilliseconds + MyDateParser.MilisecondsCalculator(1);
                    if (returnDayValue.isEnabled()) {
                        // schimb ziua de intoarcere daca am zbor dus-intors
                        returnDayValue.setText(MyDateParser.getDayFromTime(returnTime) + "");
                    }


                    returnMonthAndYearValue.setText(MyDateParser.getMonthAndYearFromTime(returnTime));
                    hideReturnDate.setText(MyDateParser.getDateStringFromMiliseconds(returnTime));
                }
            hideDepartureDate.setText(MyDateParser.getDateStringFromMiliseconds(timeInMilliseconds));
            } else {
                // datePicker chemat de butonul de "intoarcere"

                if (timeInMilliseconds <= departureTime) {
                    // ziua de intoarcere anterioara sau egala zilei de dus
                    Toast.makeText(view.getContext(), "Ziua intoarceri nu poate fi anterioara " +
                                    "sau egala zilei plecarii!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    returnDayValue.setText(MyDateParser.getDayFromTime(timeInMilliseconds) + "");
                    returnMonthAndYearValue.setText(MyDateParser.getMonthAndYearFromTime(timeInMilliseconds));
                    hideReturnDate.setText(MyDateParser.getDateStringFromMiliseconds(timeInMilliseconds));
                }
            }
        }
    }
}
