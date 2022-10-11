package com.example.appsoa2.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appsoa2.R;
import com.example.appsoa2.interfaces.SecondaryActivityContract;
import com.example.appsoa2.presenters.SecondaryPresenter;

import java.util.Random;

public class SecondaryActivity extends AppCompatActivity implements  SecondaryActivityContract.ViewMVP{
    private static final String TAG = "SecondaryActivity";
    private SecondaryActivityContract.PresenterMVP presenter;

    private Button btnShake, btnBack;
    private TextView txtLedState, txtColorSelected;
    private ImageView imgCurrentLed;

    @Override // Este metodo lo dejamos fijo
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        initialize();
    }

    private void initialize(){
        initializeButtons();
        initializeLabels();
        initializeOther();
        presenter = new SecondaryPresenter(this);
        Log.i(TAG, "Paso al estado Createad");
    }

    private void initializeLabels(){
        txtLedState = findViewById(R.id.text_ledState);
        txtColorSelected = findViewById(R.id.text_ledColorSelected);
        txtLedState.setText("ENCENDIDO");
        txtColorSelected.setText("#FFFFFF");
    }

    private void initializeButtons() {
        btnShake = findViewById(R.id.button_secondary_shaker);
        btnBack = findViewById(R.id.button_secondary_back);

        btnShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Click en SHAKE ");
                presenter.shakeEventHandler();
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

    private void initializeOther(){
        imgCurrentLed = findViewById(R.id.image_secondary_led);
    }

    @SuppressLint("ResourceType")
    @Override
    public void setCurrentColor(int value) {
        String hexColor = String.format("#%06X", (0xFFFFFF & value));
        txtColorSelected.setText(hexColor);
        imgCurrentLed.setColorFilter(value, PorterDuff.Mode.SRC_ATOP);
    }
}
