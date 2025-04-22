package com.example.luxevista_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InformationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_page);

        // go to account settings
        View viewAccSettings = findViewById(R.id.account_settings_button);
        viewAccSettings.setOnClickListener(view -> {

            Intent intent_viewAccSettings_button = new Intent(this, Reset_Password.class);
            startActivity(intent_viewAccSettings_button);

        });



    }
}