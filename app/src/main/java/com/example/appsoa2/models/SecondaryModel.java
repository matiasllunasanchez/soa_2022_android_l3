package com.example.appsoa2.models;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.appsoa2.interfaces.MainActivityContract;
import com.example.appsoa2.interfaces.SecondaryActivityContract;

import java.util.Formatter;
import java.util.Random;

public class SecondaryModel implements SecondaryActivityContract.ModelMVP {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void generateColor(SecondaryActivityContract.ModelMVP.OnSendToPresenter presenter) {
        /*Random rand = new Random();
        float red = rand.nextFloat();
        float green = rand.nextFloat();
        float blue = rand.nextFloat();
        Color randomColor =  Color.valueOf(red,green,blue);*/
        Random random = new Random();
        int newColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        // TODO: Aca debo mandar al SE el color a setear del led.
        presenter.handleShakerResult(newColor);
    }

}
