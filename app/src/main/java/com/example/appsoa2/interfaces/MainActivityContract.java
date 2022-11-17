package com.example.appsoa2.interfaces;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.appsoa2.views.MainActivity;

public interface MainActivityContract {
    interface ViewMVP {
        void showResultOnLabel(String string); // Hace algo en la vista con el string recibido
    }

    interface ModelMVP {
        String grantPermissions(int requestCode, String[] permissions, int[] grantResults, Context currentContext);
        void showMessage(String string);

        void unregisterReceiver();

        Intent prepareToNavigateToPrimaryScreen(Context context);
        Intent prepareToNavigateToSecondaryScreen(Context context);

        void reconnectBluetoothDevice();

        void turnOffBluetoothAdapter();
    }

    interface PresenterMVP {

        String grantPermissions(int requestCode, String[] permissions, int[] grantResults, Context context);

        void initializeBluetoothModule(MainActivity mainActivity, ProgressDialog mProgressDlg);

        void unregisterReceiver();


        Intent prepareToNavigateToPrimaryScreen(Context context);

        Intent prepareToNavigateToSecondaryScreen(Context context);

        void reconnectBluetoothDevice();

        void turnOffBluetoothAdapter();
    }
}
