package com.example.appsoa2.models;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.example.appsoa2.interfaces.MainActivityContract;
import com.example.appsoa2.interfaces.SecondaryActivityContract;

import java.util.Formatter;
import java.util.Random;

public class SecondaryModel implements SecondaryActivityContract.ModelMVP {
    private static final String TAG = "SecondaryModel";
    public static final int ALPHA = 255;
    int savedColor = 0;
    final int RED_COLOR = 3;
    final int GREEN_COLOR = 4;
    final int BLUE_COLOR = 5;
    final int WHITE_COLOR = 6;

    @Override
    public void generateColor(SecondaryActivityContract.ModelMVP.OnSendToPresenter presenter) {
        int newColor = new Random().nextInt(6 - 3 + 1) + 3;
        int resultColor = 0;
        switch (newColor) {
            case RED_COLOR:
                resultColor = Color.argb(ALPHA, 255, 0, 0);
                break;
            case GREEN_COLOR:
                resultColor = Color.argb(255, 0, 255, 0);
                break;
            case BLUE_COLOR:
                resultColor = Color.argb(255, 0, 0, 255);
                break;
            case WHITE_COLOR:
                resultColor = Color.argb(255, 255, 255, 255);
                break;
            default:
                resultColor = Color.argb(255, 0, 0, 0);
        }

        this.savedColor = resultColor;
        // TODO: 1 - Enviar al SE el color a setear del led. Enviar valor 3 4 o 5
        Log.i(TAG, "Enviar al SE el color a setear para el led RGB. Valor de color: " + resultColor);
        presenter.handleShakerResult(resultColor);
    }
}
