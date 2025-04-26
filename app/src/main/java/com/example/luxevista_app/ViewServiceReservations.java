package com.example.luxevista_app;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewServiceReservations extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private LinearLayout reservationsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service_reservations);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Find the container where we'll add reservation cards
        reservationsContainer = findViewById(R.id.reservationsContainer);

        // Load user's service reservations
        loadServiceReservations();
    }

    private void loadServiceReservations() {
        // Get email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", "");

        if (email.isEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get user ID from email
        int userId = databaseHelper.getUserIdByEmail(email);
        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get user's service reservations
        Cursor cursor = databaseHelper.getUserServiceReservations(userId);

        if (cursor.getCount() == 0) {
            // No reservations found
            TextView noReservationsText = new TextView(this);
            noReservationsText.setText("You have no service reservations yet.");
            noReservationsText.setTextSize(16);
            noReservationsText.setPadding(16, 16, 16, 16);
            reservationsContainer.addView(noReservationsText);
        } else {
            // Add reservation cards for each reservation
            addReservationCards(cursor);
        }

        cursor.close();
    }

    @SuppressLint("Range")
    private void addReservationCards(Cursor cursor) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);

        while (cursor.moveToNext()) {
            // Get data from cursor
            String serviceName = cursor.getString(cursor.getColumnIndex("service_name"));
            String reservationDate = cursor.getString(cursor.getColumnIndex("reservation_date"));
            String reservationTime = cursor.getString(cursor.getColumnIndex("reservation_time"));
            int reservationId = cursor.getInt(cursor.getColumnIndex("id"));

            // Format date for display
            String formattedDate = reservationDate;
            try {
                Date date = inputDateFormat.parse(reservationDate);
                formattedDate = outputDateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Inflate the card layout
            View cardView = LayoutInflater.from(this).inflate(R.layout.service_reservation_card, reservationsContainer, false);

            // Set reservation details
            TextView serviceNameTextView = cardView.findViewById(R.id.serviceNameText);
            TextView dateTextView = cardView.findViewById(R.id.dateText);
            TextView timeTextView = cardView.findViewById(R.id.timeText);

            serviceNameTextView.setText(serviceName);
            dateTextView.setText(formattedDate);
            timeTextView.setText(reservationTime);

            // Add card to container
            reservationsContainer.addView(cardView);
        }
    }
}