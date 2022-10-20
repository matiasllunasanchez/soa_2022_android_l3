package com.example.appsoa2.presenters;

import com.example.appsoa2.interfaces.BasePresenter;
import com.example.appsoa2.interfaces.MainActivityContract;
import com.example.appsoa2.models.MainModel;

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
    public void showMessage(String string) {
        this.mainView.showResultOnLabel(string);
    }

    @Override
    public void onInitialize() {
    }
}



