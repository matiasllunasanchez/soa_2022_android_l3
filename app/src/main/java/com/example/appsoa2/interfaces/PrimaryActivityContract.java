package com.example.appsoa2.interfaces;

import com.example.appsoa2.presenters.PrimaryPresenter;

public interface PrimaryActivityContract {
    interface ViewMVP {
        void setResultValue(int value);
    }

    interface ModelMVP {
        void saveLightLevel(PrimaryPresenter primaryPresenter, int i);

        interface OnSendToPresenter {
            void handleSavedResult(int value);
        }

    }

    interface PresenterMVP {
        void saveInputValue(int i);
    }
}
