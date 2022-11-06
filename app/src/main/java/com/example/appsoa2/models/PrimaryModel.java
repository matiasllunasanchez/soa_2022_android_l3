package com.example.appsoa2.models;

import android.util.Log;

import com.example.appsoa2.interfaces.PrimaryActivityContract;
import com.example.appsoa2.presenters.PrimaryPresenter;

public class PrimaryModel implements PrimaryActivityContract.ModelMVP {
    private static final String TAG = "PrimaryModel";
    private int currentLight = 0;

    @Override
    public void saveLightLevel(PrimaryPresenter primaryPresenter, int i) {
        this.currentLight = i;
        Log.i(TAG, "Enviar al SE la luminosidad requierida para la habitacion. Valor de luminosidad en porcentaje: "+i);
        // Hay que enviar data limitada, valores de 10 a 90.
        primaryPresenter.handleSavedResult(i);
    }

    // TODO: Falta requerir periodicamente la luminosidad actual y mantener actualizado el porcentaje de luz.
}
