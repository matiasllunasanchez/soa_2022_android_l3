package com.example.appsoa2.models;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.appsoa2.interfaces.MainActivityContract;
import com.example.appsoa2.interfaces.SecondaryActivityContract;

import java.util.Formatter;
import java.util.Random;

public class SecondaryModel implements SecondaryActivityContract.ModelMVP {

    int savedColor = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void generateColor(SecondaryActivityContract.ModelMVP.OnSendToPresenter presenter) {
        Random random = new Random();
        int newColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        this.savedColor = newColor;

        // TODO: 1 - Enviar al SE el color a setear del led.
        // TODO: 2 - Quiza persistirlo en el device o me lo manda el SE siempre
        presenter.handleShakerResult(newColor);
    }
}
