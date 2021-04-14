package com.example.quileia_technical_test.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quileia_technical_test.R;

public class MainActivity extends AppCompatActivity {

    private Button patients;
    private Button medics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        patients = findViewById(R.id.button_Patients);
        medics = findViewById(R.id.button_Medics);

        patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PatientsActivity.class);
                startActivity(intent);
            }
        });
        medics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MedicsActivity.class);
                startActivity(intent);
            }
        });

        this.setTitle("Menu principal");
    }
}