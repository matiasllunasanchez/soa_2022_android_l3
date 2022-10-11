package com.example.appsoa2.presenters;

import android.graphics.Color;

import com.example.appsoa2.interfaces.BasePresenter;
import com.example.appsoa2.interfaces.SecondaryActivityContract;
import com.example.appsoa2.models.SecondaryModel;

public class SecondaryPresenter implements SecondaryActivityContract.ModelMVP.OnSendToPresenter, SecondaryActivityContract.PresenterMVP, BasePresenter {

    private SecondaryActivityContract.ViewMVP mainView;
    private final SecondaryActivityContract.ModelMVP model;

    public SecondaryPresenter(SecondaryActivityContract.ViewMVP mainView) {
        // El presentador es el unico que tiene conocimiento de los dos (Vista y Modelo)
        // Cada vez que se crea requiere de una vista, y a su vez crea una nueva instancia del modelo.
        this.mainView = mainView;
        this.model = new SecondaryModel();
    }

    @Override
    public void onInitialize() {
    }

    @Override
    public void onDestroy() {
        this.mainView = null;
    }

    @Override
    public void shakeEventHandler() {
        this.model.generateColor(this);
    }

    @Override
    public void handleShakerResult(int value) {
        String hexColor = String.format("#%06X", (0xFFFFFF & value));
        this.mainView.setCurrentColor(value, hexColor);
    }
}

