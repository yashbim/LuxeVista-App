package com.example.luxevista_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        View viewRooms = findViewById(R.id.backgroundViewRooms);
        viewRooms.setOnClickListener(view -> {

            Intent intent_viewRooms_button = new Intent(this, ViewRooms.class);
            startActivity(intent_viewRooms_button);

        });

    }
}