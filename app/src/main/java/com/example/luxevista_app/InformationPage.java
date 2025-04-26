package com.example.luxevista_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class InformationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_page);

        // go to account settings
        View viewAccSettings = findViewById(R.id.account_settings_button);
        viewAccSettings.setOnClickListener(view -> {

            Intent intent_viewAccSettings_button = new Intent(this, Reset_Password.class);
            startActivity(intent_viewAccSettings_button);

        });

        // go to booking history
        View viewBookingHistory = findViewById(R.id.booking_history_button);
        viewBookingHistory.setOnClickListener(view -> {

            Intent intent_viewBookingHistory_button = new Intent(this, ViewRoomBookings.class);
            startActivity(intent_viewBookingHistory_button);

        });


        // go to service booking history
        View viewServiceReservationHistory = findViewById(R.id.service_reservation_history_button);
        viewServiceReservationHistory.setOnClickListener(view -> {

            Intent intent_viewServiceReservationHistory_button = new Intent(this, ViewServiceReservations.class);
            startActivity(intent_viewServiceReservationHistory_button);

        });


    }
}