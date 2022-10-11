package com.example.appsoa2.presenters;

import com.example.appsoa2.interfaces.BasePresenter;
import com.example.appsoa2.interfaces.MainActivityContract;
import com.example.appsoa2.models.MainModel;

public class MainPresenter implements MainActivityContract.ModelMVP.OnSendToPresenter, MainActivityContract.PresenterMVP, BasePresenter {

    private MainActivityContract.ViewMVP mainView;
    private final MainActivityContract.ModelMVP model;

    public MainPresenter(MainActivityContract.ViewMVP mainView) {
        // El presentador es el unico que tiene conocimiento de los dos (Vista y Modelo)
        // Cada vez que se crea requiere de una vista, y a su vez crea una nueva instancia del modelo.
        this.mainView = mainView;
        this.model = new MainModel();
    }

    @Override
    public void onSendButtonClick() {
        // Metodo que realiza una accion al click de un boton, en este caso del boton send
        // El presentador sabe que debe decirle al modelo que realice algo, en este caso processData
        // Se le manda el presentador para que luego le avise al mismo del resultado o algo para mostrar como feedback
        this.model.processDataGetResult(this);
    }

    @Override
    public void onDestroy() {
        // Este metodo se va a usar para anular la conexion con la vista
        this.mainView = null;
    }

    @Override
    public void showMessage(String string) {
        // Metodo que solo se envia a un metodo del objeto vista que esta siendo manejada por el presentador.
        // En este caso como se quiere mostrar un mensaje, se le pasa el mensaje al metodo de la vista correspondiente y la vista sabra que hacer
        this.mainView.showResultOnLabel(string);
    }

    @Override
    public void onInitialize() {
    }
}



