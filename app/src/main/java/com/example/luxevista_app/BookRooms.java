package com.example.luxevista_app;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BookRooms extends AppCompatActivity {

    // UI Elements
    private Spinner roomTypeSpinner;
    private EditText checkInDateEditText, checkOutDateEditText;
    private ImageButton checkInDateButton, checkOutDateButton;
    private Spinner adultsSpinner, childrenSpinner;
    private TextView summaryRoomType, summaryCheckIn, summaryCheckOut, summaryDuration, summaryGuests, summaryTotalPrice;
    private Button bookNowButton;

    // Calendar for date selection
    private Calendar calendarCheckIn = Calendar.getInstance();
    private Calendar calendarCheckOut = Calendar.getInstance();

    // Date format
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    // Room prices map
    private Map<String, Double> roomPrices = new HashMap<>();

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_rooms);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize UI elements
        initializeUI();

        // Setup room prices (now we'll get these from the database)
        setupRoomPrices();

        // Setup spinners
        setupRoomTypeSpinner();
        setupGuestsSpinners();

        // Setup date pickers
        setupDatePickers();

        // Setup book button
        setupBookButton();
    }

    private void initializeUI() {
        roomTypeSpinner = findViewById(R.id.roomTypeSpinner);
        checkInDateEditText = findViewById(R.id.checkInDateEditText);
        checkOutDateEditText = findViewById(R.id.checkOutDateEditText);
        checkInDateButton = findViewById(R.id.checkInDateButton);
        checkOutDateButton = findViewById(R.id.checkOutDateButton);
        adultsSpinner = findViewById(R.id.adultsSpinner);
        childrenSpinner = findViewById(R.id.childrenSpinner);

        summaryRoomType = findViewById(R.id.summaryRoomType);
        summaryCheckIn = findViewById(R.id.summaryCheckIn);
        summaryCheckOut = findViewById(R.id.summaryCheckOut);
        summaryDuration = findViewById(R.id.summaryDuration);
        summaryGuests = findViewById(R.id.summaryGuests);
        summaryTotalPrice = findViewById(R.id.summaryTotalPrice);

        bookNowButton = findViewById(R.id.bookNowButton);
    }

    private void setupRoomPrices() {
        // Now we'll fetch room prices from the database instead of hardcoding them
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "rooms",
                new String[]{"room_type", "price"},
                null, null, null, null, null
        );

        roomPrices.clear();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String roomType = cursor.getString(cursor.getColumnIndex("room_type"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                roomPrices.put(roomType, price);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void setupRoomTypeSpinner() {
        // Create a list of room names
        List<String> roomNames = new ArrayList<>(roomPrices.keySet());

        // Create adapter for spinner
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, roomNames);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomTypeSpinner.setAdapter(roomAdapter);

        // Set listener to update summary when room is selected
        roomTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRoom = parent.getItemAtPosition(position).toString();
                summaryRoomType.setText(selectedRoom);
                updateTotalPrice();
                checkRoomAvailability();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                summaryRoomType.setText("Not selected");
            }
        });
    }

    private void checkRoomAvailability() {
        if (roomTypeSpinner.getSelectedItem() != null) {
            String selectedRoom = roomTypeSpinner.getSelectedItem().toString();
            String checkInDate = checkInDateEditText.getText().toString();
            String checkOutDate = checkOutDateEditText.getText().toString();

            int availableRooms = dbHelper.getRoomAvailability(selectedRoom, checkInDate, checkOutDate);

            if (availableRooms <= 0) {
                Toast.makeText(this, "No " + selectedRoom + " available for selected dates", Toast.LENGTH_LONG).show();
                bookNowButton.setEnabled(false);
            } else {
                Toast.makeText(this, availableRooms + " " + selectedRoom + "(s) available", Toast.LENGTH_SHORT).show();
                bookNowButton.setEnabled(true);
            }
        }
    }

    private void setupGuestsSpinners() {
        // Create adapters for adult and children spinners
        Integer[] guestNumbers = new Integer[]{1, 2, 3, 4, 5, 6};

        ArrayAdapter<Integer> adultAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, guestNumbers);
        adultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adultsSpinner.setAdapter(adultAdapter);

        Integer[] childrenNumbers = new Integer[]{0, 1, 2, 3, 4};
        ArrayAdapter<Integer> childrenAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, childrenNumbers);
        childrenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childrenSpinner.setAdapter(childrenAdapter);

        // Set listeners to update summary
        AdapterView.OnItemSelectedListener guestListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateGuestSummary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        };

        adultsSpinner.setOnItemSelectedListener(guestListener);
        childrenSpinner.setOnItemSelectedListener(guestListener);
    }

    private void updateGuestSummary() {
        int adults = (int) adultsSpinner.getSelectedItem();
        int children = (int) childrenSpinner.getSelectedItem();
        summaryGuests.setText(adults + " Adults, " + children + " Children");
    }

    private void setupDatePickers() {
        // Initialize dates
        calendarCheckIn.add(Calendar.DAY_OF_MONTH, 1); // Set default check-in to tomorrow
        calendarCheckOut.add(Calendar.DAY_OF_MONTH, 2); // Set default check-out to day after tomorrow

        // Display default dates
        updateDateDisplay();

        // Create check-in date picker
        checkInDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendarCheckIn.set(Calendar.YEAR, year);
                        calendarCheckIn.set(Calendar.MONTH, month);
                        calendarCheckIn.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateDisplay();

                        // If check-out date is before check-in date, adjust it
                        if (calendarCheckOut.before(calendarCheckIn)) {
                            calendarCheckOut.setTime(calendarCheckIn.getTime());
                            calendarCheckOut.add(Calendar.DAY_OF_MONTH, 1);
                            updateDateDisplay();
                        }
                    },
                    calendarCheckIn.get(Calendar.YEAR),
                    calendarCheckIn.get(Calendar.MONTH),
                    calendarCheckIn.get(Calendar.DAY_OF_MONTH)
            );

            // Set min date to today
            Calendar today = Calendar.getInstance();
            datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
            datePickerDialog.show();
        });

        // Create check-out date picker
        checkOutDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendarCheckOut.set(Calendar.YEAR, year);
                        calendarCheckOut.set(Calendar.MONTH, month);
                        calendarCheckOut.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateDisplay();
                    },
                    calendarCheckOut.get(Calendar.YEAR),
                    calendarCheckOut.get(Calendar.MONTH),
                    calendarCheckOut.get(Calendar.DAY_OF_MONTH)
            );

            // Set min date to check-in date + 1 day
            Calendar minCheckOut = (Calendar) calendarCheckIn.clone();
            datePickerDialog.getDatePicker().setMinDate(minCheckOut.getTimeInMillis());
            datePickerDialog.show();
        });

        // Make EditText click open date picker too
        checkInDateEditText.setOnClickListener(v -> checkInDateButton.performClick());
        checkOutDateEditText.setOnClickListener(v -> checkOutDateButton.performClick());
    }

    private void updateDateDisplay() {
        // Update the EditText fields
        checkInDateEditText.setText(dateFormat.format(calendarCheckIn.getTime()));
        checkOutDateEditText.setText(dateFormat.format(calendarCheckOut.getTime()));

        // Update summary
        summaryCheckIn.setText(dateFormat.format(calendarCheckIn.getTime()));
        summaryCheckOut.setText(dateFormat.format(calendarCheckOut.getTime()));

        // Calculate and display duration
        long diffInMillis = calendarCheckOut.getTimeInMillis() - calendarCheckIn.getTimeInMillis();
        long nights = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        summaryDuration.setText(nights + " night" + (nights != 1 ? "s" : ""));

        // Update total price
        updateTotalPrice();

        // Check room availability with new dates
        if (roomTypeSpinner.getSelectedItem() != null) {
            checkRoomAvailability();
        }
    }

    private void updateTotalPrice() {
        // Get selected room
        if (roomTypeSpinner.getSelectedItem() == null) {
            return;
        }

        String selectedRoom = roomTypeSpinner.getSelectedItem().toString();
        double pricePerNight = roomPrices.getOrDefault(selectedRoom, 0.0);

        // Calculate number of nights
        long diffInMillis = calendarCheckOut.getTimeInMillis() - calendarCheckIn.getTimeInMillis();
        long nights = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        // Calculate total price
        double totalPrice = pricePerNight * nights;

        // Update UI
        summaryTotalPrice.setText(String.format(Locale.US, "$%.2f", totalPrice));
    }

    private void setupBookButton() {
        bookNowButton.setOnClickListener(v -> {
            // Validate input
            if (roomTypeSpinner.getSelectedItem() == null) {
                Toast.makeText(this, "Please select a room", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedRoom = roomTypeSpinner.getSelectedItem().toString();
            String checkInDate = checkInDateEditText.getText().toString();
            String checkOutDate = checkOutDateEditText.getText().toString();

            // Recheck availability just before booking
            int availableRooms = dbHelper.getRoomAvailability(selectedRoom, checkInDate, checkOutDate);
            if (availableRooms <= 0) {
                Toast.makeText(this, "Sorry, this room is no longer available for the selected dates", Toast.LENGTH_LONG).show();
                return;
            }

            // Get the user email from shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            String userEmail = sharedPreferences.getString("user_email", "");

            if (userEmail.isEmpty()) {
                Toast.makeText(this, "Please log in to book a room", Toast.LENGTH_LONG).show();
                return;
            }

            // Get user ID from email
            int userId = dbHelper.getUserIdByEmail(userEmail);
            if (userId == -1) {
                Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
                return;
            }

            // Get booking details
            int adults = (int) adultsSpinner.getSelectedItem();
            int children = (int) childrenSpinner.getSelectedItem();
            double totalPrice = Double.parseDouble(summaryTotalPrice.getText().toString().replace("$", ""));

            // Create booking in the database
            boolean bookingSuccess = dbHelper.createBooking(
                    userId, selectedRoom, checkInDate, checkOutDate,
                    adults, children, totalPrice);

            if (bookingSuccess) {
                String message = "Booking confirmed for " + summaryRoomType.getText() +
                        " from " + summaryCheckIn.getText() +
                        " to " + summaryCheckOut.getText() +
                        ". Total: " + summaryTotalPrice.getText();

                Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                // You might want to navigate to a confirmation page or back to the main screen
                finish(); // This will just close the booking screen
            } else {
                Toast.makeText(this, "Booking failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}