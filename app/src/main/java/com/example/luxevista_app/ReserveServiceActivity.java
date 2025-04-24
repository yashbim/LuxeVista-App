package com.example.luxevista_app;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ReserveServiceActivity extends AppCompatActivity {

    private Spinner spinnerService;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnReserve;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_service);

        spinnerService = findViewById(R.id.spinnerService);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        btnReserve = findViewById(R.id.btnReserveService);
        dbHelper = new DatabaseHelper(this);

        // Get user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", "");

        if (!userEmail.isEmpty()) {
            userId = dbHelper.getUserIdByEmail(userEmail);
        } else {
            userId = -1;
        }

        if (userId == -1) {
            Toast.makeText(this, "Please log in to reserve services", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Set up service options
        String[] services = {"Spa Treatment", "Poolside Cabanas"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services);
        spinnerService.setAdapter(adapter);

        btnReserve.setOnClickListener(v -> {
            String selectedService = spinnerService.getSelectedItem().toString();

            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            @SuppressLint("DefaultLocale")
            String dateStr = String.format("%02d/%02d/%04d", month + 1, day, year);

            // Format time (handle 24-hour format)
            int hour = timePicker.getCurrentHour();
            int minute = timePicker.getCurrentMinute();
            @SuppressLint("DefaultLocale")
            String timeStr = String.format("%02d:%02d", hour, minute);

            // Check if user has an active booking on the selected date
            if (dbHelper.hasActiveBookingOnDate(userId, dateStr)) {
                // Check if service is available at the selected time
                if (dbHelper.isServiceAvailable(selectedService, dateStr, timeStr)) {
                    // Create service reservation
                    boolean success = dbHelper.createServiceReservation(userId, selectedService, dateStr, timeStr);

                    if (success) {
                        Toast.makeText(this, selectedService + " reserved for " + dateStr + " at " + timeStr, Toast.LENGTH_LONG).show();
                        finish(); // Return to previous screen
                    } else {
                        Toast.makeText(this, "Failed to reserve service. Please try again.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Service not available at selected time. Please choose another time.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "You must have an active room booking on " + dateStr + " to reserve this service.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
