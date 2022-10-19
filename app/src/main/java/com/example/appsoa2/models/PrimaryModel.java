package com.example.appsoa2.models;

import com.example.appsoa2.interfaces.PrimaryActivityContract;
import com.example.appsoa2.presenters.PrimaryPresenter;

public class PrimaryModel implements PrimaryActivityContract.ModelMVP {

    private int currentLight = 0;

    @Override
    public void saveLightLevel(PrimaryPresenter primaryPresenter, int i) {
        // TODO:  1 - Enviar la luminosidad deseada al SE
        // TODO: Falta recibir luminosidad actual en algun lado
        this.currentLight = i;
        primaryPresenter.handleSavedResult(i);
    }
}
