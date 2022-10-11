package com.example.appsoa2.interfaces;

import android.graphics.Color;

public interface SecondaryActivityContract {
    interface ViewMVP{
        void setCurrentColor(int value);
    }

    interface ModelMVP{

        interface OnSendToPresenter {
            // Metodos que debe tener el presentador para manejar lo que le el modelo le mande por parametro, en este caso un string
            void handleShakerResult(int value);
        }
        void generateColor(OnSendToPresenter presenter);
    }

    interface PresenterMVP{
        void shakeEventHandler();
    }
}
