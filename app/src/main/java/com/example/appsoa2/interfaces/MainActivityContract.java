package com.example.appsoa2.interfaces;

import android.content.Context;

import com.example.appsoa2.presenters.MainPresenter;

import java.util.List;

public interface MainActivityContract {
    interface ViewMVP {
        void showResultOnToast(String message);
        void showResultOnLabel(String message);
        void requestPermissionsToUser(List<String> listPermissionsNeeded);
        void closeLoadingDialog();
        void showLoadingDialog();
        void askBTPermissions();
    }

    interface ModelMVP {
        void getReadyBluetooth(Context mainActivity, MainActivityContract.ModelMVP.OnSendToPresenter presenter);
        void stopBluetoothDiscovery();
        void permissionsGrantedProcess();
        String getConnectedMacAddress();
        void onDestroyProcess();
        void onResumeProcess();
        void onPauseProcess();
        interface OnSendToPresenter {
            void showOnToast(String message);
            void showOnLabel(String message);
            void closeLoadingDialog();
            void showLoadingDialog();
            void askBTPermission();
            void requestPermissions(List<String> listPermissionsNeeded);
        }

        void processDataGetResult(MainActivityContract.ModelMVP.OnSendToPresenter presenter);
    }

    interface PresenterMVP extends BasePresenter {
        void onSendButtonClick();

        void getReadyLogic(Context context);

        void stopBluetoothSearch();

        void permissionsGrantedProcess();

        String getConnectedDeviceAddress();
   /*     void showOnToast(String message);
        void showOnLabel(String message);*/
    }
}
