package com.example.appsoa2.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appsoa2.R;
import com.example.appsoa2.contracts.SecondaryActivityContract;
import com.example.appsoa2.presenters.SecondaryPresenter;

public class SecondaryActivity extends AppCompatActivity implements  SecondaryActivityContract.ViewMVP{
    private static final String TAG = "SecondaryActivity";
    private SecondaryActivityContract.PresenterMVP presenter;

    private Button btnShake, btnBack;
    private TextView txtLedState, txtColorSelected;

    @Override // Este metodo lo dejamos fijo
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        initialize();
    }

    private void initialize(){
        initializeButtons();
        initializeLabels();
        presenter = new SecondaryPresenter(this);
        Log.i(TAG, "Paso al estado Createad");
    }

    private void initializeLabels(){
        txtLedState = findViewById(R.id.text_ledState);
        txtColorSelected = findViewById(R.id.text_ledColorSelected);
    }

    private void initializeButtons() {
        btnShake = findViewById(R.id.button_secondary_shaker);
        btnBack = findViewById(R.id.button_secondary_back);

        btnShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Click en SHAKE ");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Click en BACK ");
                try {
                    Intent k = new Intent(SecondaryActivity.this, MainActivity.class);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
