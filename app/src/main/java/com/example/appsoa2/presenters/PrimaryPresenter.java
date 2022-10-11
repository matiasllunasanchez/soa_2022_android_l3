package com.example.appsoa2.presenters;

import com.example.appsoa2.contracts.PrimaryActivityContract;
import com.example.appsoa2.models.MainModel;
import com.example.appsoa2.models.PrimaryModel;

public class PrimaryPresenter implements PrimaryActivityContract.ModelMVP.OnSendToPresenter, PrimaryActivityContract.PresenterMVP {

    private PrimaryActivityContract.ViewMVP mainView;
    private final PrimaryActivityContract.ModelMVP model;

    public PrimaryPresenter(PrimaryActivityContract.ViewMVP mainView){
        // El presentador es el unico que tiene conocimiento de los dos (Vista y Modelo)
        // Cada vez que se crea requiere de una vista, y a su vez crea una nueva instancia del modelo.
        this.mainView = mainView;
        this.model = new PrimaryModel();
    }

}

