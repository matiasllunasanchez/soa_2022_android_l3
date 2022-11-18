package com.example.appsoa2.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.example.appsoa2.interfaces.MainActivityContract;
import com.example.appsoa2.interfaces.SecondaryActivityContract;
import com.example.appsoa2.views.SecondaryActivity;

import java.io.IOException;
import java.util.Formatter;
import java.util.Random;

public class SecondaryModel implements SecondaryActivityContract.ModelMVP {
    private static final String TAG = "SecondaryModel";
    public static final int ALPHA = 255;
    public static final String SENSOR_SERVICE = "sensor";
    int savedColor = 0;
    final int RED_COLOR = 3;
    final int GREEN_COLOR = 4;
    final int BLUE_COLOR = 5;
    final int WHITE_COLOR = 6;

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private static final float SHAKE_THRESHOLD = 5f;
    private float currentPositionX, currentPositionY, currentPositionZ, lastPositionX, lastPositionY, lastPositionZ;
    private boolean notFirstMove = false;
    private float xDiff, yDiff, zDiff;
    private Vibrator vibratorObj;


    @Override
    public void getReadySensors(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        this.sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.vibratorObj = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void movementDetected(SensorEvent sensorEvent, SecondaryActivityContract.ModelMVP.OnSendToPresenter presenter) {
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
                //presenter.shakeEventHandler();
                generateColor(presenter);
            }

        }
        lastPositionX = currentPositionX;
        lastPositionY = currentPositionY;
        lastPositionZ = currentPositionZ;
        notFirstMove = true;
    }

    @Override
    public void disconnectBT() {
   /*     try {
            btSocket.close();
        } catch (IOException e2) {
            Log.i(TAG, "Excepcion al intentar cerrar socket de BT" + e2);
        }*/

    }

    @Override
    public void disconnectSensors(SecondaryActivity secondaryActivity) {
            this.sensorManager.unregisterListener(secondaryActivity);
    }

    @Override
    public void reconnectSensors(SecondaryActivity secondaryActivity) {
        this.sensorManager.registerListener(secondaryActivity, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void generateColor(SecondaryActivityContract.ModelMVP.OnSendToPresenter presenter) {
        int newColor = new Random().nextInt(6 - 3 + 1) + 3;
        int resultColor = 0;
        int codeColor = 6;
        switch (newColor) {
            case RED_COLOR:
                resultColor = Color.argb(ALPHA, 255, 0, 0);
                codeColor = 3;
                break;
            case GREEN_COLOR:
                resultColor = Color.argb(255, 0, 255, 0);
                codeColor = 4;
                break;
            case BLUE_COLOR:
                resultColor = Color.argb(255, 0, 0, 255);
                codeColor = 5;
                break;
            case WHITE_COLOR:
                resultColor = Color.argb(255, 255, 255, 255);
                break;
            default:
                resultColor = Color.argb(255, 0, 0, 0);
        }

        this.savedColor = resultColor;
        Log.i(TAG, "Enviar al SE el color a setear para el led RGB. Valor de color: " + resultColor);
        presenter.handleShakerResult(resultColor, codeColor);
    }
}
