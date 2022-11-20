package com.example.appsoa2.interfaces;

import android.content.Context;
import android.hardware.SensorEvent;

import com.example.appsoa2.views.SecondaryActivity;

public interface SecondaryActivityContract {
    interface ViewMVP {
        void setCurrentColor(int value, String hexColor, int codeColor);
        void consoleLog(String label, String msg);
    }

    interface ModelMVP {

        void getReadySensors(Context context);
        void movementDetected(SensorEvent sensorEvent, SecondaryActivityContract.ModelMVP.OnSendToPresenter presenter);

        void disconnectBT();

        void disconnectSensors(SecondaryActivity secondaryActivity);

        void reconnectSensors(SecondaryActivity secondaryActivity);

        void getReadyBluetooth(Context context);

        void reconnectBluetoothDevice(String address);

        void sendLedColorValue(String valueOf);

        interface OnSendToPresenter {
            void handleShakerResult(int resultColor, int value);
        }

        void generateColor(OnSendToPresenter presenter);
    }

    interface PresenterMVP {
        void shakeEventHandler();

        void getReadyLogic(Context context);

        void movementDetected(SensorEvent sensorEvent);

        void safeDisconnect(SecondaryActivity secondaryActivity);

        void getReadyLogicAgain(SecondaryActivity secondaryActivity);

        void reconnectDevice(String address);

        void sendColorToDevice(String valueOf);
    }
}
