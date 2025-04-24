package com.example.luxevista_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewServicesPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_services_page);
        // go to view rooms
        View spa_treatment_button = findViewById(R.id.spa_treatment_button);
        spa_treatment_button.setOnClickListener(view -> {

            Intent intent_spa_treatment_button = new Intent(this, ReserveServiceActivity.class);
            startActivity(intent_spa_treatment_button);

        });

        // go to view services
        View poolside_cabana_button = findViewById(R.id.poolside_cabana_button);
        poolside_cabana_button.setOnClickListener(view -> {

            Intent intent_spa_treatment_button = new Intent(this, ReserveServiceActivity.class);
            startActivity(intent_spa_treatment_button);

        });

    }
}