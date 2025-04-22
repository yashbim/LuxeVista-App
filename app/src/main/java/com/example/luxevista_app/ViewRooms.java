package com.example.luxevista_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ViewRooms extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private List<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rooms);

        recyclerView = findViewById(R.id.roomRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        roomList = new ArrayList<>();
        roomList.add(new Room("Ocean View Suite",
                "Spacious suite with a breathtaking ocean view, private balcony, and king-size bed.",
                "$250 / night",
                R.drawable.ocean_suite));

        roomList.add(new Room("Deluxe Room",
                "Comfortable deluxe room with modern furnishings and garden view.",
                "$180 / night",
                R.drawable.deluxe_room));

        roomList.add(new Room("Family Villa",
                "Ideal for families, includes two bedrooms and a private pool.",
                "$320 / night",
                R.drawable.family_villa));

        roomList.add(new Room("Standard Room",
                "Affordable and cozy room with all essential amenities.",
                "$120 / night",
                R.drawable.standard_room));

        adapter = new RoomAdapter(this, roomList);
        recyclerView.setAdapter(adapter);
    }
}
