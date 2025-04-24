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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ViewRoomBookings extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout bookingsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room_bookings);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Get reference to the container where bookings will be displayed
        bookingsContainer = findViewById(R.id.bookingsContainer);

        // Load and display user bookings
        loadUserBookings();
    }

    private void loadUserBookings() {
        // Get the user email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", "");

        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Please log in to view your bookings", Toast.LENGTH_LONG).show();
            finish(); // Close activity if not logged in
            return;
        }

        // Get user ID from email
        int userId = dbHelper.getUserIdByEmail(userEmail);
        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
            finish(); // Close activity if user not found
            return;
        }

        // Check if user has any bookings
        if (!dbHelper.hasBookings(userId)) {
            TextView noBookingsText = new TextView(this);
            noBookingsText.setText("You don't have any bookings yet.");
            noBookingsText.setTextSize(16);
            noBookingsText.setPadding(0, 30, 0, 0);
            bookingsContainer.addView(noBookingsText);
            return;
        }

        // Get user bookings
        Cursor bookingsCursor = dbHelper.getUserBookings(userId);
        displayBookings(bookingsCursor);
        bookingsCursor.close();
    }

    @SuppressLint("Range")
    private void displayBookings(Cursor cursor) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        // Clear existing views
        bookingsContainer.removeAllViews();

        // Iterate through bookings
        if (cursor.moveToFirst()) {
            do {
                // Get booking details
                String roomType = cursor.getString(cursor.getColumnIndex("room_type"));
                String checkInDate = cursor.getString(cursor.getColumnIndex("check_in_date"));
                String checkOutDate = cursor.getString(cursor.getColumnIndex("check_out_date"));
                int adults = cursor.getInt(cursor.getColumnIndex("adults"));
                int children = cursor.getInt(cursor.getColumnIndex("children"));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex("total_price"));

                // Calculate number of nights
                long nights = 0;
                try {
                    Date checkIn = dateFormat.parse(checkInDate);
                    Date checkOut = dateFormat.parse(checkOutDate);
                    long diffInMillis = checkOut.getTime() - checkIn.getTime();
                    nights = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Inflate booking card view
                View bookingCard = LayoutInflater.from(this).inflate(
                        R.layout.booking_card_item, bookingsContainer, false);

                // Set booking details in the card
                TextView tvRoomType = bookingCard.findViewById(R.id.cardRoomType);
                TextView tvDates = bookingCard.findViewById(R.id.cardDates);
                TextView tvGuests = bookingCard.findViewById(R.id.cardGuests);
                TextView tvNights = bookingCard.findViewById(R.id.cardNights);
                TextView tvPrice = bookingCard.findViewById(R.id.cardPrice);

                tvRoomType.setText(roomType);
                tvDates.setText(checkInDate + " to " + checkOutDate);
                tvGuests.setText(adults + " Adults, " + children + " Children");
                tvNights.setText(nights + " night" + (nights != 1 ? "s" : ""));
                tvPrice.setText(String.format(Locale.US, "$%.2f", totalPrice));

                // Add card to container
                bookingsContainer.addView(bookingCard);

            } while (cursor.moveToNext());
        }
    }
}