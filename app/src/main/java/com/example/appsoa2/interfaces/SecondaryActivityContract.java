package com.example.appsoa2.interfaces;

public interface SecondaryActivityContract {
    interface ViewMVP {
        void setCurrentColor(int value, String hexColor);
    }

    interface ModelMVP {

        interface OnSendToPresenter {
            void handleShakerResult(int value);
        }

        void generateColor(OnSendToPresenter presenter);
    }

    interface PresenterMVP {
        void shakeEventHandler();
    }
}
