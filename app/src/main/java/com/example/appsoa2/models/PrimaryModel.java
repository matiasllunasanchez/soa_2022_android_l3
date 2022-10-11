package com.example.appsoa2.models;

import com.example.appsoa2.interfaces.PrimaryActivityContract;
import com.example.appsoa2.presenters.PrimaryPresenter;

public class PrimaryModel implements PrimaryActivityContract.ModelMVP {

    private int currentLight = 0;

    @Override
    public void saveLightLevel(PrimaryPresenter primaryPresenter, int i) {
        // TODO:  1 - Enviar la luminosidad deseada al SE
        // TODO:  2 - Persistir este valor en algun archivo real del device, al menos hasta que el led del SE alcance la luminosidad deseada
        this.currentLight = i;
        primaryPresenter.handleSavedResult(i);
    }
}
