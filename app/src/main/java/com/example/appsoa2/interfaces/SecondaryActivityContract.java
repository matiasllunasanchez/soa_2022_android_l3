package com.example.appsoa2.interfaces;

public interface SecondaryActivityContract {
    interface ViewMVP {
        void setCurrentColor(int value, String hexColor, int codeColor);
    }

    interface ModelMVP {

        interface OnSendToPresenter {
            void handleShakerResult(int resultColor, int value);
        }

        void generateColor(OnSendToPresenter presenter);
    }

    interface PresenterMVP {
        void shakeEventHandler();
    }
}
