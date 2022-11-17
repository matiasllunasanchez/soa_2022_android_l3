package com.example.appsoa2.models;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.appsoa2.interfaces.MainActivityContract;
import com.example.appsoa2.views.PrimaryActivity;
import com.example.appsoa2.views.SecondaryActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainModel implements MainActivityContract.ModelMVP {
    private static final String TAG = "MainActivity";
    // Bluetooth stuff
    private BluetoothDevice primaryDevice = null;
    private BluetoothAdapter mBluetoothAdapter;
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    private ProgressDialog localDialog;
    private Context localContext;
    //se crea un array de String con los permisos a solicitar en tiempo de ejecucion
    //Esto se debe realizar a partir de Android 6.0, ya que con verdiones anteriores
    //con solo solicitarlos en el Manifest es suficiente
    String[] permissions= new String[]{
            Manifest.permission.VIBRATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private static String MAC_ADDRESS_DEVICE = "00:21:06:BE:58:58"; // Real DEVICE

    private void initializeBluetoothModule(Context context, ProgressDialog dialog){
        //Se crea un adaptador para podermanejar el bluethoot del celular
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Se Crea la ventana de dialogo que indica que se esta buscando dispositivos bluethoot
        localDialog = dialog;
        localDialog.setMessage("Buscando dispositivos......");
        // dialog.setMessage("Buscando dispositivos...");

        if (checkPermissions(context))
        {
            enableComponent();
            initializeBroadcastReceiver(context);
        }
    }

    private void initializeBroadcastReceiver(Context context){
        //se definen un broadcastReceiver que captura el broadcast del SO cuando captura los siguientes eventos:
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); //Cambia el estado del Bluethoot (Acrtivado /Desactivado)
        filter.addAction(BluetoothDevice.ACTION_FOUND); //Se encuentra un dispositivo bluethoot al realizar una busqueda
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //Cuando se comienza una busqueda de bluethoot
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //cuando la busqueda de bluethoot finaliza
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); //Cuando se empareja o desempareja el bluethoot
        //se define (registra) el handler que captura los broadcast anterirmente mencionados.
        context.registerReceiver(mReceiver, filter);
    }

    protected String enableComponent() {
        String resultValue;
        if (mBluetoothAdapter == null)
        {
            // txtEstado.setText("Bluetooth no es soportado por el dispositivo movil");
            resultValue = "Bluetooth no es soportado por el dispositivo movil";
            Log.i(TAG, resultValue);
            return resultValue;
        }
        else
        {
            if (mBluetoothAdapter.isEnabled())
            {
                //txtEstado.setText("Bluetooth ya encendido!!");
                // showToast("Bluetooth ya encendido!!");
                Log.i(TAG, "Bluetooth ya encendido!!");

                if(!primaryDeviceIsAlreadyConnected()){
                    searchBluetoothDevices();
                }
            }
            else
            {
                Log.i(TAG, "Bluetooth apagado... Encendiendo!");
                //showToast("Bluetooth apagado... Encendiendo!");
                // txtEstado.setText("Bluetooth apagado... Encendiendo!");
                askTurnOnBluetooth();
            }
        }
        return "a";
    }

    private Intent askTurnOnBluetooth(){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        // startActivityForResult(intent, 1000);
        return intent;
    }

    private void turnOffBluetooth(){
        mBluetoothAdapter.disable();
    }

    private void searchBluetoothDevices(){
        mBluetoothAdapter.startDiscovery();
    }

    //Handler que captura los brodacast que emite el SO al ocurrir los eventos del bluethoot
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            //A traves del Intent obtengo el evento de Bluethoot que informo el broadcast del SO
            String action = intent.getAction();
            handleBluetoothEvent(intent, action);
        }
    };

    private void handleBluetoothEvent(Intent intent, String action){
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            //Obtengo el parametro, aplicando un Bundle, que me indica el estado del Bluethoot
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            //Si esta activado
            if (state == BluetoothAdapter.STATE_ON)
            {
                // showToast("Tu bluetooth ya esta activo");
                // txtEstado.setText("Bluetooth activado!");
                Log.i(TAG, "Bluetooth activado... Buscando dispositivos!");
                searchBluetoothDevices();
            }else{
                //txtEstado.setText("Necesitamos activar el bluetooth");
                //showToast("Necesitamos activar el bluetooth para funcionar.");
                Log.i(TAG, "Bluetooth apagado... Intentando encender!");
                askTurnOnBluetooth();
            }
        }
        //Si se inicio la busqueda de dispositivos bluethoot
        else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            localDialog.show(); // DIALOG BUSCANDO...
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            //se cierra el cuadro de dialogo de busqueda
            localDialog.dismiss();
            // connectPrimaryDevice();
            if(primaryDevice==null){
                // showToast("No se encontro cortina disponible");
                // txtEstado.setText("No se encontro cortina disponible");
                Log.i(TAG, "No se encontro cortina disponible");
            }

        }
        //si se encontro un dispositivo bluethoot
        else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            //Se lo agregan sus datos a una lista de dispositivos encontrados
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // showToast("Dispositivo cercano: " + device.getName());
            Log.i(TAG, "Dispositivo cercano>> " + device.getName());
            if(checkPrimaryDevice(device)){
                primaryDevice = device;
                finishBluetoothSearch();
            }
        }
        else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            //Obtengo los parametro, aplicando un Bundle, que me indica el estado del Bluethoot
            final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
            final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

            if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING)
            {
                //Si se detecto que se puedo emparejar el bluethoot
                // showToast("Proceso de conexion bt finalizado!");
                Log.i(TAG, "Proceso de conexion bt finalizado! ");
                BluetoothDevice dispositivo = (BluetoothDevice) primaryDevice;
                //se inicia el Activity de comunicacion con el bluethoot, para transferir los datos.
                //Para eso se le envia como parametro la direccion(MAC) del bluethoot Arduino
                // ESTO LO TENGO QUE AHCER CUANDO PRESIONO UN BOTON PARA IR A UNA PANTALLA

            }
            else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                // showToast("Dispositivo desemparejado");
                Log.i(TAG, "Dispositivo desemparejado ");
                primaryDevice = null;
            }
        }
    }

    private boolean primaryDeviceIsAlreadyConnected(){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices == null || pairedDevices.size() == 0)
        {
            // showToast("No se encontraron dispositivos emparejados");
            Log.i(TAG, "No se encontraron dispositivos emparejados");
            this.primaryDevice = null;
            return false;
        }
        else
        {
            for (BluetoothDevice currentDevice : pairedDevices) {
                if (currentDevice.getAddress().equals(MAC_ADDRESS_DEVICE)) {
                    if (currentDevice.getBondState() == BluetoothDevice.BOND_BONDED)
                    {
                        // txtEstado.setText("Cortina "+ currentDevice.getName()+" ya se encuentra conectada...");
                        Log.i(TAG, "Cortina "+currentDevice.getName()+" ya conectada");
                        pairDevice(currentDevice);
                        // showToast("Dispositivo ya cortina conectada!");
                    }
                    else
                    { // Caso extranio, analizar... Si estoy buscando entre dispositivos conectados y no esta enlazada, deberia ser un error.
                        // Intento reconectarla.
                        pairDevice(currentDevice);
                    }
                    this.primaryDevice = currentDevice;
                    return true;
                }
            }
            return false;
        }
    }

    private boolean checkPrimaryDevice(BluetoothDevice currentDevice){
        if (currentDevice.getAddress().equals(MAC_ADDRESS_DEVICE)) {
            if (currentDevice.getBondState() == BluetoothDevice.BOND_BONDED)
            {
                // showToast("Dispositivo cortina ya conectada!");
                //txtEstado.setText("Cortina "+ currentDevice.getName()+" ya conectada!");
                Log.i(TAG, "Cortina "+currentDevice.getName()+" ya conectada");
            }
            else
            {
                pairDevice(currentDevice);
            }
            return true;
        }
        return false;
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            // showToast("Cortina cercana... Emparejando...");
            Log.i(TAG, "Cortina cercana... Emparejando...");
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            Log.i(TAG, "Cortina "+device.getName()+" emparejada");
            // txtEstado.setText("Cortina "+ device.getName()+" conectada!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishBluetoothSearch(){
        localDialog.dismiss();
        mBluetoothAdapter.cancelDiscovery();
    }

    private boolean checkPermissions(Context context) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        //Se chequea si la version de Android es menor a la 6
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(context,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            // Revisar porque esto probablemente este mal.
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    public String grantPermissions(int requestCode, String permissions[], int[] grantResults, Context currentContext){
        localContext = currentContext;
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    enableComponent(); // Now you call here what ever you want :)
                    initializeBroadcastReceiver(currentContext);
                } else {
                    String perStr = "";
                    for (String per : permissions) {
                        perStr += "\n" + per;
                    }
                    // permissions list of don't granted permission
                    return "Esta aplicación requiere de la aceptación de todos los permisos para funcionar correctamente."
                }
                return null;
            }
        }
    }

    @Override
    public void showMessage(String string) {

    }

    @Override
    public void unregisterReceiver() {
        unregisterReceiver(mReceiver);
    }

    @Override
    public Intent prepareToNavigateToPrimaryScreen(Context from) {
        try {
            if(primaryDevice != null){
                mBluetoothAdapter.cancelDiscovery();
                Intent k = new Intent(from, PrimaryActivity.class);
                k.putExtra("Direccion_Bluethoot", primaryDevice.getAddress());
                return k;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Intent prepareToNavigateToSecondaryScreen(Context context) {
        try {
            if(primaryDevice != null){
                Intent k = new Intent(context, SecondaryActivity.class);
                k.putExtra("Direccion_Bluethoot", primaryDevice.getAddress());
                return k;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void reconnectBluetoothDevice() {
        if (primaryDevice == null) {
            if (mBluetoothAdapter == null) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            enableComponent();
        }
    }

    @Override
    public void turnOffBluetoothAdapter() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }
    }
}
