package com.example.appsoa2.models;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.appsoa2.interfaces.MainActivityContract;
import com.example.appsoa2.interfaces.SecondaryActivityContract;

import java.util.Formatter;
import java.util.Random;

public class SecondaryModel implements SecondaryActivityContract.ModelMVP {
    private static final String TAG = "SecondaryModel";
    int savedColor = 0;
    final int RED_COLOR = 3;
    final int GREEN_COLOR = 4;
    final int BLUE_COLOR = 5;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void generateColor(SecondaryActivityContract.ModelMVP.OnSendToPresenter presenter) {
        Random random = new Random();
        //int newColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int newColor = new Random().nextInt(5 - 3 + 1) + 3;
        // Crear enum para ROJO VERDE Y AZUL
        int resultColor = Color.argb(255,0,0,0);;
        if(newColor == RED_COLOR)
            resultColor = Color.argb(255,255,0,0);
        else if(newColor == GREEN_COLOR)
            resultColor = Color.argb(255,0,255,0);
        else if(newColor == BLUE_COLOR)
            resultColor = Color.argb(255,0,0,255);

        this.savedColor = resultColor;
        // TODO: 1 - Enviar al SE el color a setear del led. Enviar valor 3 4 o 5
        Log.i(TAG, "Enviar al SE el color a setear para el led RGB. Valor de color: "+resultColor);
        presenter.handleShakerResult(resultColor);
    }
}
