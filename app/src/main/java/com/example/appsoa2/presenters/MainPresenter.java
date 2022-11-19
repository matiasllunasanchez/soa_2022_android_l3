package com.example.appsoa2.presenters;

import android.content.Context;

import com.example.appsoa2.interfaces.BasePresenter;
import com.example.appsoa2.interfaces.MainActivityContract;
import com.example.appsoa2.models.MainModel;

import java.util.List;

public class MainPresenter implements MainActivityContract.ModelMVP.OnSendToPresenter, MainActivityContract.PresenterMVP, BasePresenter {

    private MainActivityContract.ViewMVP mainView;
    private final MainActivityContract.ModelMVP model;

    public MainPresenter(MainActivityContract.ViewMVP mainView) {
        this.mainView = mainView;
        this.model = new MainModel();
    }

    @Override
    public void onSendButtonClick() {
        this.model.processDataGetResult(this);
    }

    @Override
    public void onDestroy() {
        this.mainView = null;
    }

    @Override
    public void getReadyLogic(Context currentContext) {
        this.model.getReadyBluetooth(currentContext, this);
    }

    @Override
    public void stopBluetoothSearch() {
        this.model.stopBluetoothDiscovery();
    }

    @Override
    public void onDestroyActivity() {
        this.model.onDestroyProcess();
    }

    @Override
    public void onResumeProcess() {
        this.model.onResumeProcess();
    }

    @Override
    public void permissionsGrantedProcess() {
        this.model.permissionsGrantedProcess();
    }

    @Override
    public void onPauseProcess() {
        this.model.onPauseProcess();
    }

    @Override
    public String getConnectedDeviceAddress() {
        return this.model.getConnnectedMacAddress();
    }

    @Override
    public void showOnToast(String message) {
        this.mainView.showResultOnToast(message);
    }

    @Override
    public void showOnLabel(String message) {
        this.mainView.showResultOnLabel(message);
    }

    @Override
    public void closeLoadingDialog() {
        this.mainView.closeDialog();
    }

    @Override
    public void showLoadingDialog() {
        this.mainView.showLoadingDialog();
    }

    @Override
    public void askBTPermission() {
        this.mainView.askBTPermissions();
    }

    @Override
    public void showMessage(String string) {
        this.mainView.showResultOnLabel(string);
    }

    @Override
    public void onInitialize() {
    }

    public void requestPermissions(List<String> listPermissionsNeeded) {
        this.mainView.requestPermissionsToUser(listPermissionsNeeded);
    }
}



