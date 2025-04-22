package com.example.luxevista_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ViewRooms extends AppCompatActivity {

    private static final String TAG = "ViewRooms";
    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private List<Room> roomList;

    // Filter UI elements
    private Button toggleFilterButton;
    private View filterLayout;
    private Spinner roomTypeSpinner;
    private CheckBox availabilityCheckbox;
    private EditText minPriceEditText, maxPriceEditText;
    private Spinner sortBySpinner;
    private Button applyFiltersButton;

    // Room types for spinner
    private final String[] roomTypes = {"All", "Suite", "Deluxe", "Standard", "Villa"};

    // Sort options for spinner
    private final String[] sortOptions = {"Default", "Price (Low to High)", "Price (High to Low)", "Room Type"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rooms);

        Log.d(TAG, "onCreate: Starting ViewRooms activity");

        initializeViews();
        setupSpinners();
        setupRoomList();
        setupFilterToggle();
        setupApplyFiltersButton();
    }

    private void initializeViews() {
        Log.d(TAG, "initializeViews: Finding views");

        recyclerView = findViewById(R.id.roomRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toggleFilterButton = findViewById(R.id.toggleFilterButton);
        filterLayout = findViewById(R.id.filterLayout);

        if (filterLayout == null) {
            Log.e(TAG, "initializeViews: filterLayout is null!");
            return;
        }

        // Access views inside the included layout
        try {
            roomTypeSpinner = filterLayout.findViewById(R.id.roomTypeSpinner);
            availabilityCheckbox = filterLayout.findViewById(R.id.availabilityCheckbox);
            minPriceEditText = filterLayout.findViewById(R.id.minPriceEditText);
            maxPriceEditText = filterLayout.findViewById(R.id.maxPriceEditText);
            sortBySpinner = filterLayout.findViewById(R.id.sortBySpinner);
            applyFiltersButton = filterLayout.findViewById(R.id.applyFiltersButton);

            Log.d(TAG, "initializeViews: Filter views initialized successfully");

            // Check if any of the views are null
            if (roomTypeSpinner == null) Log.e(TAG, "roomTypeSpinner is null");
            if (availabilityCheckbox == null) Log.e(TAG, "availabilityCheckbox is null");
            if (minPriceEditText == null) Log.e(TAG, "minPriceEditText is null");
            if (maxPriceEditText == null) Log.e(TAG, "maxPriceEditText is null");
            if (sortBySpinner == null) Log.e(TAG, "sortBySpinner is null");
            if (applyFiltersButton == null) Log.e(TAG, "applyFiltersButton is null");
        } catch (Exception e) {
            Log.e(TAG, "Error finding filter views: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupSpinners() {
        if (roomTypeSpinner == null || sortBySpinner == null) {
            Log.e(TAG, "setupSpinners: Spinners are null, cannot set up adapters");
            return;
        }

        try {
            // Room Type Spinner
            ArrayAdapter<String> roomTypeAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, roomTypes);
            roomTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roomTypeSpinner.setAdapter(roomTypeAdapter);

            // Sort By Spinner
            ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, sortOptions);
            sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sortBySpinner.setAdapter(sortAdapter);

            Log.d(TAG, "setupSpinners: Spinners set up successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up spinners: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupRoomList() {
        Log.d(TAG, "setupRoomList: Setting up room list");

        roomList = new ArrayList<>();

        // Adding sample rooms with room type and availability
        roomList.add(new Room("Ocean View Suite",
                "Spacious suite with a breathtaking ocean view, private balcony, and king-size bed.",
                "$250 / night",
                R.drawable.ocean_suite,
                "Suite",
                true));

        roomList.add(new Room("Deluxe Room",
                "Comfortable deluxe room with modern furnishings and garden view.",
                "$180 / night",
                R.drawable.deluxe_room,
                "Deluxe",
                true));

        roomList.add(new Room("Family Villa",
                "Ideal for families, includes two bedrooms and a private pool.",
                "$320 / night",
                R.drawable.family_villa,
                "Villa",
                false));

        roomList.add(new Room("Standard Room",
                "Affordable and cozy room with all essential amenities.",
                "$120 / night",
                R.drawable.standard_room,
                "Standard",
                true));

        // Add more rooms as needed for demonstration
        roomList.add(new Room("Mountain View Suite",
                "Luxurious suite with panoramic mountain views and a fireplace.",
                "$280 / night",
                R.drawable.mountain_suite, // Using ocean_suite image as placeholder
                "Suite",
                true));

        roomList.add(new Room("Executive Suite",
                "Premium suite with separate living area and executive workspace.",
                "$350 / night",
                R.drawable.executive_suite, // Using deluxe_room image as placeholder
                "Suite",
                false));

        adapter = new RoomAdapter(this, roomList);
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "setupRoomList: Room list set up with " + roomList.size() + " rooms");
    }

    private void setupFilterToggle() {
        if (toggleFilterButton == null || filterLayout == null) {
            Log.e(TAG, "setupFilterToggle: Toggle button or filter layout is null");
            return;
        }

        toggleFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Toggle filter button clicked");

                if (filterLayout.getVisibility() == View.VISIBLE) {
                    filterLayout.setVisibility(View.GONE);
                    toggleFilterButton.setText("Show Filters");
                } else {
                    filterLayout.setVisibility(View.VISIBLE);
                    toggleFilterButton.setText("Hide Filters");
                }
            }
        });

        Log.d(TAG, "setupFilterToggle: Filter toggle set up");
    }

    private void setupApplyFiltersButton() {
        if (applyFiltersButton == null) {
            Log.e(TAG, "setupApplyFiltersButton: Apply filters button is null");
            return;
        }

        applyFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Apply Filters button clicked");
                applyFilters();
            }
        });

        Log.d(TAG, "setupApplyFiltersButton: Apply filters button set up");
    }

    private void applyFilters() {
        if (roomTypeSpinner == null || availabilityCheckbox == null ||
                minPriceEditText == null || maxPriceEditText == null ||
                sortBySpinner == null || adapter == null) {

            Log.e(TAG, "applyFilters: Some filter views are null");
            Toast.makeText(this, "Error: Could not apply filters", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String selectedRoomType = roomTypeSpinner.getSelectedItem().toString();
            boolean showAvailableOnly = availabilityCheckbox.isChecked();

            Log.d(TAG, "applyFilters: Room type: " + selectedRoomType +
                    ", Available only: " + showAvailableOnly);

            // Get price range values
            double minPrice = 0;
            double maxPrice = 0;

            try {
                String minPriceStr = minPriceEditText.getText().toString();
                if (!minPriceStr.isEmpty()) {
                    minPrice = Double.parseDouble(minPriceStr);
                }

                String maxPriceStr = maxPriceEditText.getText().toString();
                if (!maxPriceStr.isEmpty()) {
                    maxPrice = Double.parseDouble(maxPriceStr);
                }

                Log.d(TAG, "applyFilters: Price range: $" + minPrice + " - $" + maxPrice);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing price values: " + e.getMessage());
                Toast.makeText(this, "Please enter valid price values", Toast.LENGTH_SHORT).show();
                return;
            }

            // Apply filters
            adapter.filterRooms(selectedRoomType, minPrice, maxPrice, showAvailableOnly);

            // Apply sorting
            String sortBy = sortBySpinner.getSelectedItem().toString();
            if (!sortBy.equals("Default")) {
                Log.d(TAG, "applyFilters: Sorting by: " + sortBy);
                adapter.sortRooms(sortBy);
            }

            Toast.makeText(this, "Filters applied", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "applyFilters: Filters applied successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error applying filters: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error applying filters", Toast.LENGTH_SHORT).show();
        }
    }
}