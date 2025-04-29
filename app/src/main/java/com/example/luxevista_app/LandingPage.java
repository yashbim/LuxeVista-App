package com.example.luxevista_app;

import android.annotation.SuppressLint;
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
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View viewRooms = findViewById(R.id.backgroundViewRooms);
        viewRooms.setOnClickListener(view -> {

            Intent intent_viewRooms_button = new Intent(this, ViewRooms.class);
            startActivity(intent_viewRooms_button);

        });

        // go to view services
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View viewServices = findViewById(R.id.backgroundViewServices);
        viewServices.setOnClickListener(view -> {

            Intent intent_viewServices_button = new Intent(this, ViewServicesPage.class);
            startActivity(intent_viewServices_button);

        });

        // go to view attractions
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View viewLocalAttractions = findViewById(R.id.backgroundViewAttractions);
        viewLocalAttractions.setOnClickListener(view -> {

            Intent intent_viewLocalAttractions_button = new Intent(this, ViewLocalAttractions.class);
            startActivity(intent_viewLocalAttractions_button);

        });

        // go to infomration page
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        View viewInformationPage = findViewById(R.id.backgroundViewInfo);
        viewInformationPage.setOnClickListener(view -> {

            Intent intent_viewInformationPage_button = new Intent(this, InformationPage.class);
            startActivity(intent_viewInformationPage_button);

        });

    }
}