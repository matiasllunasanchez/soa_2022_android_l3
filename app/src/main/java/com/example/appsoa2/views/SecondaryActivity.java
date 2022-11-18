package com.example.appsoa2.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.londatiga.android.bluetooth.R;
import com.example.appsoa2.interfaces.SecondaryActivityContract;
import com.example.appsoa2.presenters.SecondaryPresenter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class SecondaryActivity extends Activity implements SecondaryActivityContract.ViewMVP, SensorEventListener {
    private static final String TAG = "SecondaryActivity";
    private SecondaryActivityContract.PresenterMVP presenter;

    private Button  btnBack;
    private TextView txtColorSelected;
    private ImageView imgCurrentLed;
    private static final String RED_COLOR_HEX = "#FF0000";
    private static final String GREEN_COLOR_HEX = "#00FF00";
    private static final String BLUE_COLOR_HEX = "0000FF";
    private static final String WHITE_COLOR_HEX = "FFFFFF";
    private ImageView lampImg;

/*
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private static final float SHAKE_THRESHOLD = 5f;
    private float currentPositionX, currentPositionY, currentPositionZ, lastPositionX, lastPositionY, lastPositionZ;
    private boolean notFirstMove = false;
    private float xDiff, yDiff, zDiff;
    private Vibrator vibratorObj;
*/

    // Bluetooth Stuff
    Handler bluetoothIn;
    final int handlerState = 0; //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    private SecondaryActivity.ConnectedThread mConnectedThread;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_secondary);
        this.initialize();
    }

    private void initialize() {
        this.initializeButtons();
        this.initializeRest();
        presenter = new SecondaryPresenter(this);
        presenter.getReadyLogic(this);
        this.initializeBluetoothModule();
        Log.i(TAG, "Paso al estado Createad");
    }

    private void initializeRest() {
        this.txtColorSelected = this.findViewById(R.id.text_ledColorSelected);
        this.txtColorSelected.setText("-");
        this.lampImg = (ImageView) this.findViewById(R.id.image_secondary_led);
    }

    private void initializeButtons() {
        this.btnBack = this.findViewById(R.id.button_secondary_back);

        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Click en BACK ");
                try {
                    Intent k = new Intent(SecondaryActivity.this, MainActivity.class);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeOthers() {
        // this.imgCurrentLed = findViewById(R.id.image_secondary_led);
/*
        this.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.vibratorObj = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
*/
    }

    @Override
    public void setCurrentColor(int value, String hexColor, int codeColor) {
        this.txtColorSelected.setText(hexColor);
        this.setLampColor(hexColor);
        this.lampImg.setColorFilter(value, PorterDuff.Mode.SRC_ATOP);

        // Se manda por BT el valor del color cambiado
        Log.i(TAG,"Color a mandar "+codeColor);
         this.mConnectedThread.write(String.valueOf(codeColor));
        Context context = getApplicationContext();
        CharSequence text = "¡Cambió el color del led!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        this.presenter.movementDetected(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void setLampColor(String value) {
        switch (value) {
            case RED_COLOR_HEX:
                this.lampImg.setImageResource(R.drawable.lamp_red);
                break;
            case GREEN_COLOR_HEX:
                this.lampImg.setImageResource(R.drawable.lamp_green);
                break;
            case BLUE_COLOR_HEX:
                this.lampImg.setImageResource(R.drawable.lamp_blue);
                break;
            case WHITE_COLOR_HEX:
                this.lampImg.setImageResource(R.drawable.lamp_white);
                break;
            default:
                this.lampImg.setImageResource(R.drawable.lamp_values);
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Bluetooth zone
    private void initializeBluetoothModule(){
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    //Cuando se ejecuta el evento onPause se cierra el socket Bluethoot, para no recibiendo datos
    public void onPause() {
        super.onPause();
        this.presenter.safeDisconnect(this);
    }

    @Override
    public void onResume() {
        this.presenter.gerReadyLogicAgain(this);

        //Obtengo el parametro, aplicando un Bundle, que me indica la Mac Adress del HC05
        Intent intent=getIntent();
        Bundle extras=intent.getExtras();
        address= extras.getString("Direccion_Bluethoot");

        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        btSocket = creationSocketByDevice(address);

        mConnectedThread = new SecondaryActivity.ConnectedThread(btSocket);
        mConnectedThread.start();
        mConnectedThread.write("6");
        Log.i(TAG,"Seteo blanco color inicial de led al SE");
        super.onResume();

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        // Crear el socket para comunicacion por BT
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //Constructor de la clase del Thread Secondary
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        // Metodo para enviar / escribir en el dispositivo
        public void write(String input) {
            Log.i(TAG,"Write con color: "+ input);
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes

            try {
                Log.i(TAG, "Paso al estado Createad");
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                showToast("Error al mandar datos al SE");
                finish();

            }
        }
    }

    private BluetoothSocket creationSocketByDevice(String address){
        BluetoothSocket socketResult = null;

        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        try {
            socketResult = device.createRfcommSocketToServiceRecord(BTMODULEUUID);
            socketResult.connect();
            Log.i("[BLUETOOTH]","Connected to: "+device.getName());
        }catch(IOException e){
            try{socketResult.close();}catch(IOException c){return socketResult;}
        }
        return socketResult;
    }
}
