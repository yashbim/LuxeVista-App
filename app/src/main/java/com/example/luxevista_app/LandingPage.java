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

        // go to view rooms
        View viewRooms = findViewById(R.id.backgroundViewRooms);
        viewRooms.setOnClickListener(view -> {

            Intent intent_viewRooms_button = new Intent(this, ViewRooms.class);
            startActivity(intent_viewRooms_button);

        });

        // go to view rooms
        View viewLocalAttractions = findViewById(R.id.backgroundViewAttractions);
        viewLocalAttractions.setOnClickListener(view -> {

            Intent intent_viewLocalAttractions_button = new Intent(this, ViewLocalAttractions.class);
            startActivity(intent_viewLocalAttractions_button);

        });

        // go to infomration page
        View viewInformationPage = findViewById(R.id.backgroundViewInfo);
        viewInformationPage.setOnClickListener(view -> {

            Intent intent_viewInformationPage_button = new Intent(this, InformationPage.class);
            startActivity(intent_viewInformationPage_button);

        });

    }
}