package com.example.appsoa2.presenters;

import com.example.appsoa2.interfaces.BasePresenter;
import com.example.appsoa2.interfaces.PrimaryActivityContract;
import com.example.appsoa2.models.PrimaryModel;

public class PrimaryPresenter implements PrimaryActivityContract.ModelMVP.OnSendToPresenter, PrimaryActivityContract.PresenterMVP, BasePresenter {

    private PrimaryActivityContract.ViewMVP mainView;
    private final PrimaryActivityContract.ModelMVP model;

    public PrimaryPresenter(PrimaryActivityContract.ViewMVP mainView) {
        this.mainView = mainView;
        this.model = new PrimaryModel();
    }

    @Override
    public void handleSavedResult(int value) {
        this.mainView.setResultValue(value);
    }

    @Override
    public void saveInputValue(int i) {
        this.model.saveLightLevel(this, i);
    }

    @Override
    public void getReadyLogic() {
        this.model.getReadyBluetooth(this);
    }

    @Override
    public void reconnectDevice(String macAddress) {
        this.model.reconnectBluetoothDevice(macAddress);
    }

    @Override
    public void sendLightLevelValue(int lightValue) {
        this.model.sendLevelValueToDevice(lightValue);
    }

    @Override
    public void getCurrentLevelLight() {
        this.model.getCurrentLightLevel();
    }

    @Override
    public void onCreatedProcess() {

    }

    @Override
    public void onStartProcess() {

    }

    @Override
    public void onResumeProcess() {

    }

    @Override
    public void onPauseProcess() {

    }

    @Override
    public void onStopProcess() {

    }

    @Override
    public void onRestartProcess() {

    }

    @Override
    public void onDestroyProcess() {
        this.mainView = null;
    }

    @Override
    public void consoleLog(String label, String msg) {
    this.mainView.consoleLog(label,msg);
    }
}

