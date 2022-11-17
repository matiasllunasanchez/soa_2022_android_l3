package com.example.appsoa2.presenters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.appsoa2.interfaces.BasePresenter;
import com.example.appsoa2.interfaces.MainActivityContract;
import com.example.appsoa2.models.MainModel;
import com.example.appsoa2.views.MainActivity;

public class MainPresenter implements MainActivityContract.ModelMVP, MainActivityContract.PresenterMVP, BasePresenter {

    private MainActivityContract.ViewMVP mainView;
    private final MainActivityContract.ModelMVP model;

    public MainPresenter(MainActivityContract.ViewMVP mainView) {
        this.mainView = mainView;
        this.model = new MainModel();
    }


    @Override
    public void onDestroy() {
        this.mainView = null;
    }

    @Override
    public void showMessage(String string) {
        this.mainView.showResultOnLabel(string);
    }

    @Override
    public void onInitialize() {
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    public String grantPermissions(int requestCode, String permissions[], int[] grantResults, Context context) {
        return this.model.grantPermissions(requestCode, permissions, grantResults, context);
    }

    @Override
    public void initializeBluetoothModule(MainActivity mainActivity, ProgressDialog mProgressDlg) {

    }

    @Override
    public void unregisterReceiver() {
        this.model.unregisterReceiver();
    }

    @Override
    public Intent prepareToNavigateToPrimaryScreen(Context context) {
        return this.model.prepareToNavigateToPrimaryScreen(context);
    }

    @Override
    public Intent prepareToNavigateToSecondaryScreen(Context context) {
        return this.model.prepareToNavigateToSecondaryScreen(context);
    }

    @Override
    public void reconnectBluetoothDevice() {
        this.model.reconnectBluetoothDevice();
    }

    @Override
    public void turnOffBluetoothAdapter() {
        this.model.turnOffBluetoothAdapter();
    }
}



