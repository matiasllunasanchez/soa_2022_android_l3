package com.example.appsoa2.interfaces;

public interface MainActivityContract {
    interface ViewMVP {
        void showResultOnLabel(String string); // Hace algo en la vista con el string recibido
    }

    interface ModelMVP {
        interface OnSendToPresenter {
            void showMessage(String string);
        }

        void processDataGetResult(MainActivityContract.ModelMVP.OnSendToPresenter presenter);
    }

    interface PresenterMVP {
        void onSendButtonClick();

        void onDestroy();
    }
}
