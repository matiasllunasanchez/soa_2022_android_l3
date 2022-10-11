package com.example.appsoa2.interfaces;

public interface PrimaryActivityContract {
    interface ViewMVP{

    }

    interface ModelMVP{
        interface OnSendToPresenter {
            // Metodos que debe tener el presentador para manejar lo que le el modelo le mande por parametro, en este caso un string
        }

    }

    interface PresenterMVP{

    }
}
