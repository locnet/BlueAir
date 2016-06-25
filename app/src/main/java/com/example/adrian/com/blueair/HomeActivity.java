package com.example.adrian.com.blueair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends ActionBarActivity implements View.OnClickListener {


    // butoane
    Button searchFlightButton;
    Button userButton;
    Button checkinButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layer);
        View view = findViewById(R.id.home_activity);

        searchFlightButton = (Button) view.findViewById(R.id.home_flight_button);
        searchFlightButton.setOnClickListener(homeButtonsListener);
        userButton =(Button) view.findViewById(R.id.home_user_button);
        userButton.setOnClickListener(homeButtonsListener);
        checkinButton = (Button) view.findViewById(R.id.home_checkin_button);
        checkinButton.setOnClickListener(homeButtonsListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view) {
    }

    //listener
    View.OnClickListener homeButtonsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.home_flight_button:
                    Intent i = new Intent(view.getContext(), MainActivity.class);
                    startActivity(i);
                    break;
                case R.id.home_user_button:
                    Intent y = new Intent(view.getContext(),UserActivity.class);
                    startActivity(y);
                    break;
                case R.id.home_checkin_button:
                    Intent z = new Intent(view.getContext(), CheckinActivity.class);
                    startActivity(z);
                    break;
            }
        }
    };
}
