package com.example.appsoa2.interfaces;

import android.content.Context;

import com.example.appsoa2.presenters.MainPresenter;

import java.util.List;

public interface MainActivityContract {
    interface ViewMVP {
        void showResultOnToast(String message);
        void showResultOnLabel(String message);
        void requestPermissionsToUser(List<String> listPermissionsNeeded);
        void closeDialog();
        void showLoadingDialog();

        void askBTPermissions();
    }

    interface ModelMVP {
        void getReadyBluetooth(Context mainActivity, MainPresenter presenter);

        void stopBluetoothDiscovery();

        void onDestroyProcess();

        void onResumeProcess();

        void permissionsGrantedProcess();

        void onPauseProcess();

        String getConnnectedMacAddress();

        interface OnSendToPresenter {
            void showMessage(String string);
            void showOnToast(String message);
            void showOnLabel(String message);
            void closeLoadingDialog();
            void showLoadingDialog();
            void askBTPermission();
        }

        void processDataGetResult(MainActivityContract.ModelMVP.OnSendToPresenter presenter);
    }

    interface PresenterMVP {
        void onSendButtonClick();

        void onDestroy();

        void getReadyLogic(Context context);

        void stopBluetoothSearch();

        void onDestroyActivity();

        void onResumeProcess();

        void permissionsGrantedProcess();

        void onPauseProcess();

        String getConnectedDeviceAddress();
   /*     void showOnToast(String message);
        void showOnLabel(String message);*/
    }
}
