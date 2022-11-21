package com.example.appsoa2.interfaces;

import com.example.appsoa2.presenters.PrimaryPresenter;

public interface PrimaryActivityContract {
    interface ViewMVP {
        void setResultValue(int value);
        void consoleLog(String label, String msg);
    }

    interface ModelMVP {
        void saveLightLevel(PrimaryPresenter primaryPresenter, int i);
        void getReadyBluetooth(PrimaryPresenter presenter);
        void reconnectBluetoothDevice(String macAddress);
        void sendLevelValueToDevice(int lightValue);
        void getCurrentLightLevel();
        void closeSocket();

        interface OnSendToPresenter {
            void handleSavedResult(int value);
        }
    }

    interface PresenterMVP extends BasePresenter {
        void saveInputValue(int i);
        void getReadyLogic();
        void reconnectDevice(String macAddress);
        void sendLightLevelValue(int lightValue);
        void getCurrentLevelLight();
        void onPauseProcess();
    }
}
