package com.example.luxevista_app;

import android.annotation.SuppressLint;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReserveServiceActivity extends AppCompatActivity {

    private Spinner spinnerService;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnReserve;
    private DatabaseHelper dbHelper;
    private int userId; // You need to get this from session or intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_service);

        spinnerService = findViewById(R.id.spinnerService);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        btnReserve = findViewById(R.id.btnReserveService);
        dbHelper = new DatabaseHelper(this);

        // Set up service options
        String[] services = {"Spa Treatment", "Poolside Cabanas"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services);
        spinnerService.setAdapter(adapter);

        // Example user ID
        userId = getIntent().getIntExtra("userId", -1);  // You should pass it from login or session

        btnReserve.setOnClickListener(v -> {
            String selectedService = spinnerService.getSelectedItem().toString();

            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, day);

            @SuppressLint("DefaultLocale")
            String dateStr = String.format("%02d/%02d/%04d", month + 1, day, year);

            if (checkUserBookingOnDate(userId, dateStr)) {
                Toast.makeText(this, selectedService + " reserved for " + dateStr, Toast.LENGTH_LONG).show();
                // You can add reservation saving logic here later
            } else {
                Toast.makeText(this, "No room booking on selected date!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkUserBookingOnDate(int userId, String selectedDate) {
        Cursor bookings = dbHelper.getUserBookings(userId);
        if (bookings != null && bookings.moveToFirst()) {
            do {
                String checkIn = bookings.getString(bookings.getColumnIndex("check_in_date"));
                String checkOut = bookings.getString(bookings.getColumnIndex("check_out_date"));

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    Date in = sdf.parse(checkIn);
                    Date out = sdf.parse(checkOut);
                    Date selected = sdf.parse(selectedDate);

                    if (selected != null && in != null && out != null && !selected.before(in) && !selected.after(out)) {
                        return true;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (bookings.moveToNext());
        }
        return false;
    }
}
