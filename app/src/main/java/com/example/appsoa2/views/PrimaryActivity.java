package com.example.appsoa2.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appsoa2.R;
import com.example.appsoa2.contracts.MainActivityContract;
import com.example.appsoa2.contracts.PrimaryActivityContract;
import com.example.appsoa2.presenters.PrimaryPresenter;
import com.example.appsoa2.presenters.SecondaryPresenter;

public class PrimaryActivity extends AppCompatActivity implements PrimaryActivityContract.ViewMVP{
    private static final String TAG = "PrimaryActivity";
    private PrimaryActivityContract.PresenterMVP presenter;

    private Button btnSave, btnBack;
    private TextView txtCurrentLightLevel, txtLightSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        initialize();
    }

    private void initialize(){
     initializeButtons();
     initializeLabels();
        presenter = new PrimaryPresenter(this);
        Log.i(TAG, "Paso al estado Createad");
    }

    private void initializeButtons(){
        btnSave = findViewById(R.id.button_primary_save);
        btnBack = findViewById(R.id.button_primary_back);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Click en BACK ");
                try {
                    Intent k = new Intent(PrimaryActivity.this, MainActivity.class);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeLabels(){
        txtCurrentLightLevel = findViewById(R.id.text_primary_currentLightLevel);
        txtLightSelected = findViewById(R.id.input_primary_finalLightLevel);
    }
}
