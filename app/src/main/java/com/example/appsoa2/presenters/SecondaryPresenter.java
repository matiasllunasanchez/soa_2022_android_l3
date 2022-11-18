package com.example.appsoa2.presenters;

import android.content.Context;
import android.hardware.SensorEvent;

import com.example.appsoa2.interfaces.BasePresenter;
import com.example.appsoa2.interfaces.SecondaryActivityContract;
import com.example.appsoa2.models.SecondaryModel;
import com.example.appsoa2.views.SecondaryActivity;

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
    public void getReadyLogic(Context context) {
        this.model.getReadySensors(context);
    }

    @Override
    public void movementDetected(SensorEvent sensorEvent) {
        this.model.movementDetected(sensorEvent, this);
    }

    @Override
    public void safeDisconnect(SecondaryActivity secondaryActivity) {
        this.model.disconnectBT();
        this.model.disconnectSensors(secondaryActivity);
    }

    @Override
    public void gerReadyLogicAgain(SecondaryActivity secondaryActivity) {
        this.model.reconnectSensors(secondaryActivity);
    }

    @Override
    public void handleShakerResult(int value, int codeColor) {
         String hexColor = String.format("#%06X", (0xFFFFFF & value));
        this.mainView.setCurrentColor(value, hexColor, codeColor);
    }
}

