package com.example.appsoa2.presenters;

import com.example.appsoa2.contracts.PrimaryActivityContract;
import com.example.appsoa2.contracts.SecondaryActivityContract;
import com.example.appsoa2.models.PrimaryModel;
import com.example.appsoa2.models.SecondaryModel;

public class SecondaryPresenter implements SecondaryActivityContract.ModelMVP.OnSendToPresenter, SecondaryActivityContract.PresenterMVP {

    private SecondaryActivityContract.ViewMVP mainView;
    private final SecondaryActivityContract.ModelMVP model;

    public SecondaryPresenter(SecondaryActivityContract.ViewMVP mainView){
        // El presentador es el unico que tiene conocimiento de los dos (Vista y Modelo)
        // Cada vez que se crea requiere de una vista, y a su vez crea una nueva instancia del modelo.
        this.mainView = mainView;
        this.model = new SecondaryModel();
    }

}

