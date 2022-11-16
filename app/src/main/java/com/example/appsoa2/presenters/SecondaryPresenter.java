package com.example.appsoa2.presenters;

import android.graphics.Color;

import com.example.appsoa2.interfaces.BasePresenter;
import com.example.appsoa2.interfaces.SecondaryActivityContract;
import com.example.appsoa2.models.SecondaryModel;

public class SecondaryPresenter implements SecondaryActivityContract.ModelMVP.OnSendToPresenter, SecondaryActivityContract.PresenterMVP, BasePresenter {

    private SecondaryActivityContract.ViewMVP mainView;
    private final SecondaryActivityContract.ModelMVP model;

    public SecondaryPresenter(SecondaryActivityContract.ViewMVP mainView) {
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
    public void handleShakerResult(int value, int codeColor) {
         String hexColor = String.format("#%06X", (0xFFFFFF & value));
        this.mainView.setCurrentColor(value, hexColor, codeColor);
    }
}

