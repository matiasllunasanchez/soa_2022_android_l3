package com.example.appsoa2.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.appsoa2.R;
import com.example.appsoa2.interfaces.SecondaryActivityContract;
import com.example.appsoa2.presenters.SecondaryPresenter;

import java.util.Random;

public class SecondaryActivity extends AppCompatActivity implements SecondaryActivityContract.ViewMVP, SensorEventListener {
    private static final String TAG = "SecondaryActivity";
    private SecondaryActivityContract.PresenterMVP presenter;

    private Button btnShake, btnBack;
    private TextView txtLedState, txtColorSelected;
    private ImageView imgCurrentLed;
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private static final float SHAKE_THRESHOLD = 5f;
    private float currentPositionX, currentPositionY, currentPositionZ, lastPositionX, lastPositionY, lastPositionZ;
    private boolean notFirstMove = false;
    private float xDiff, yDiff, zDiff;
    private Vibrator vibratorObj;
    private static final String RED_COLOR_HEX = "#FF0000";
    private static final String GREEN_COLOR_HEX = "#00FF00";
    private static final String BLUE_COLOR_HEX = "0000FF";
    private ImageView lampImg;

    @Override // Este metodo lo dejamos fijo
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_secondary);
        this.initialize();
    }

    private void initialize() {
        this.initializeButtons();
        this.initializeLabels();
        initializeOthers();
        presenter = new SecondaryPresenter(this);
        Log.i(TAG, "Paso al estado Createad");
    }

    private void initializeLabels() {
        this.txtLedState = this.findViewById(R.id.text_ledState);
        this.txtColorSelected = this.findViewById(R.id.text_ledColorSelected);
        this.txtLedState.setText("ENCENDIDO");
        this.txtColorSelected.setText("#FFFFFF");
    }

    private void initializeButtons() {
        this.btnShake = this.findViewById(R.id.button_secondary_shaker);
        this.btnBack = this.findViewById(R.id.button_secondary_back);

        this.btnShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Click en SHAKE ");
                presenter.shakeEventHandler();
            }
        });
        this.btnBack.setOnClickListener(new View.OnClickListener() {
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

    private void initializeOthers() {
        this.imgCurrentLed = findViewById(R.id.image_secondary_led);
        this.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.vibratorObj = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        this.lampImg = (ImageView) this.findViewById(R.id.image_secondary_led);
    }

    @Override
    public void setCurrentColor(int value, String hexColor) {
        this.txtColorSelected.setText(hexColor);
        this.setLampColor(hexColor);
        this.imgCurrentLed.setColorFilter(value, PorterDuff.Mode.SRC_ATOP);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        currentPositionX = sensorEvent.values[0];
        currentPositionY = sensorEvent.values[1];
        currentPositionZ = sensorEvent.values[2];

        if (notFirstMove) {

            xDiff = Math.abs(lastPositionX - currentPositionX);
            yDiff = Math.abs(lastPositionY - currentPositionY);
            zDiff = Math.abs(lastPositionZ - currentPositionZ);

            if ((xDiff > SHAKE_THRESHOLD && yDiff > SHAKE_THRESHOLD) || (yDiff > SHAKE_THRESHOLD && zDiff > SHAKE_THRESHOLD) || (xDiff > SHAKE_THRESHOLD && zDiff > SHAKE_THRESHOLD)) {
                // SHAKE EVENT!

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibratorObj.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibratorObj.vibrate(500);
                }
                presenter.shakeEventHandler();
            }

        }
        lastPositionX = currentPositionX;
        lastPositionY = currentPositionY;
        lastPositionZ = currentPositionZ;
        notFirstMove = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this);
    }

    private void setLampColor(String value) {
        switch (value) {
            case RED_COLOR_HEX:
                this.lampImg.setImageResource(R.drawable.lamp_red);
                break;
            case GREEN_COLOR_HEX:
                this.lampImg.setImageResource(R.drawable.lamp_green);
                break;
            case BLUE_COLOR_HEX:
                this.lampImg.setImageResource(R.drawable.lamp_blue);
                break;
            default:
                this.lampImg.setImageResource(R.drawable.lamp_values);
                break;
        }
    }
}
