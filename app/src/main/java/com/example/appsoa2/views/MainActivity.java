package com.example.appsoa2.views;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import android.app.Activity;
import android.app.ProgressDialog;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.example.appsoa2.interfaces.MainActivityContract;
import com.example.appsoa2.presenters.MainPresenter;

import net.londatiga.android.bluetooth.R;

public class MainActivity extends Activity implements MainActivityContract.ViewMVP {

	private MainActivityContract.PresenterMVP presenter;
	private static final String TAG = "MainActivity";
	private TextView textView;

	private TextView txtEstado;
	private ProgressDialog mProgressDlg;

	// Bluetooth stuff
	private BluetoothDevice primaryDevice = null;
	private BluetoothAdapter mBluetoothAdapter;
	public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

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

	private static String MAC_ADDRESS_DEVICE = "58:96:00:00:09:97";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// bloquear
		//AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.setContentView(R.layout.activity_main);
		this.initialize();
	}

	private void initialize() {

		Button btnPrimary = findViewById(R.id.button_primary);
		Button btnSecondary = findViewById(R.id.button_secondary);

		btnPrimary.setOnClickListener(this.btnListener);
		btnSecondary.setOnClickListener(this.btnListener);

		this.textView = this.findViewById(R.id.textView);
		this.presenter = new MainPresenter(this);
		this.txtEstado = (TextView) findViewById(R.id.txtEstado);

		this.initializeBluetoothModule();
		Log.i(TAG, "Paso al estado Createad");
	}

	private void initializeBluetoothModule(){
		//Se crea un adaptador para podermanejar el bluethoot del celular
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		//Se Crea la ventana de dialogo que indica que se esta buscando dispositivos bluethoot
		mProgressDlg = new ProgressDialog(this);

		mProgressDlg.setMessage("Buscando dispositivos...");
		mProgressDlg.setCancelable(false);

		//se asocia un listener al boton cancelar para la ventana de dialogo ue busca los dispositivos bluethoot
		// mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", btnCancelarDialogListener);

		//
		if (checkPermissions())
		{
			enableComponent();
			initializeBroadcastReceiver();
		}
	}

	private void initializeBroadcastReceiver(){
		//se definen un broadcastReceiver que captura el broadcast del SO cuando captura los siguientes eventos:
		IntentFilter filter = new IntentFilter();

		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); //Cambia el estado del Bluethoot (Acrtivado /Desactivado)
		filter.addAction(BluetoothDevice.ACTION_FOUND); //Se encuentra un dispositivo bluethoot al realizar una busqueda
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //Cuando se comienza una busqueda de bluethoot
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //cuando la busqueda de bluethoot finaliza
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); //Cuando se empareja o desempareja el bluethoot
		//se define (registra) el handler que captura los broadcast anterirmente mencionados.
		registerReceiver(mReceiver, filter);
	}

	protected  void enableComponent() {
		if (mBluetoothAdapter == null)
		{
			txtEstado.setText("Bluetooth no es soportado por el dispositivo movil");
			showToast("Bluetooth no es soportado por el dispositivo movil");
		}
		else
		{
			if (mBluetoothAdapter.isEnabled())
			{
				txtEstado.setText("Bluetooth ya encendido!!");
				showToast("Bluetooth ya encendido!!");
				if(!primaryDeviceIsAlreadyConnected()){
					searchBluetoothDevices();
				}
			}
			else
			{
				showToast("Bluetooth apagado... Encendiendo!");
				txtEstado.setText("Bluetooth apagado... Encendiendo!");
				askTurnOnBluetooth();
			}
		}
	}

	private void askTurnOnBluetooth(){
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(intent, 1000);
	}

	private void turnOffBluetooth(){
		mBluetoothAdapter.disable();
	}

	private void searchBluetoothDevices(){
		mBluetoothAdapter.startDiscovery();
	}

	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	//Handler que captura los brodacast que emite el SO al ocurrir los eventos del bluethoot
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {

			//Atraves del Intent obtengo el evento de Bluethoot que informo el broadcast del SO
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
				showToast("Tu bluetooth ya esta activo");
				txtEstado.setText("Bluetooth activado!");
				searchBluetoothDevices();
			}else{
				txtEstado.setText("Necesitamos activar el bluetooth");
				showToast("Necesitamos activar el bluetooth para funcionar.");
				askTurnOnBluetooth();
			}
		}
		//Si se inicio la busqueda de dispositivos bluethoot
		else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
			mProgressDlg.show(); // DIALOG BUSCANDO...
		}
		else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			//se cierra el cuadro de dialogo de busqueda
			mProgressDlg.dismiss();
			// connectPrimaryDevice();
			if(primaryDevice==null){
				showToast("No se encontro cortina disponible");
				txtEstado.setText("No se encontro cortina disponible");
			}

		}
		//si se encontro un dispositivo bluethoot
		else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			//Se lo agregan sus datos a una lista de dispositivos encontrados
			BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			showToast("Dispositivo cercano: " + device.getName());
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
				showToast("Proceso de conexion bluetooth ha finalizado!");
				BluetoothDevice dispositivo = (BluetoothDevice) primaryDevice;
				//se inicia el Activity de comunicacion con el bluethoot, para transferir los datos.
				//Para eso se le envia como parametro la direccion(MAC) del bluethoot Arduino
				// ESTO LO TENGO QUE AHCER CUANDO PRESIONO UN BOTON PARA IR A UNA PANTALLA

					/*
					String direccionBluethoot = dispositivo.getAddress();
					Intent i = new Intent(MainActivity.this, activity_comunicacion.class);
					i.putExtra("Direccion_Bluethoot", direccionBluethoot);

					startActivity(i);
					 */

			}  //si se detrecto un desaemparejamiento
			else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
				showToast("Dispositivo desemparejado");
				primaryDevice = null;
			}
		}
	}

	private boolean primaryDeviceIsAlreadyConnected(){
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		if (pairedDevices == null || pairedDevices.size() == 0)
		{
			showToast("No se encontraron dispositivos emparejados");
			this.primaryDevice = null;
			return false;
		}
		else
		{
			for (BluetoothDevice currentDevice : pairedDevices) {
				if (currentDevice.getAddress().equals(MAC_ADDRESS_DEVICE)) {
					if (currentDevice.getBondState() == BluetoothDevice.BOND_BONDED)
					{
						//  unpairDevice(device);
						txtEstado.setText("Cortina "+ currentDevice.getName()+" ya se encuentra conectada...");
						pairDevice(currentDevice);
						showToast("Dispositivo ya cortina conectada!");
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
				showToast("Dispositivo cortina ya conectada!");
				txtEstado.setText("Cortina "+ currentDevice.getName()+" ya conectada!");
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
			showToast("Cortina cercana... Emparejando...");
			Method method = device.getClass().getMethod("createBond", (Class[]) null);
			method.invoke(device, (Object[]) null);
			txtEstado.setText("Cortina "+ device.getName()+" conectada!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void unpairDevice(BluetoothDevice device) {
		try {
			Method method = device.getClass().getMethod("removeBond", (Class[]) null);
			method.invoke(device, (Object[]) null);
			txtEstado.setText("Cortina "+ device.getName()+" desconectada!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void finishBluetoothSearch(){
		mProgressDlg.dismiss();
		mBluetoothAdapter.cancelDiscovery();
	}

	private  boolean checkPermissions() {
		int result;
		List<String> listPermissionsNeeded = new ArrayList<>();

		//Se chequea si la version de Android es menor a la 6
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}

		for (String p:permissions) {
			result = ContextCompat.checkSelfPermission(this,p);
			if (result != PackageManager.PERMISSION_GRANTED) {
				listPermissionsNeeded.add(p);
			}
		}
		if (!listPermissionsNeeded.isEmpty()) {
			ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
			return false;
		}
		return true;
	}

	private View.OnClickListener btnListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.button_primary:
					Log.i(TAG, "Click en PRIMARY ");
					try {
						if(primaryDevice != null){
							Intent k = new Intent(MainActivity.this, PrimaryActivity.class);
							k.putExtra("Direccion_Bluethoot", primaryDevice);
							startActivity(k);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case R.id.button_secondary:
					Log.i(TAG, "Click en SECONDARY ");
					try {
						if(primaryDevice != null){
							Intent k = new Intent(MainActivity.this, SecondaryActivity.class);
							k.putExtra("Direccion_Bluethoot", primaryDevice);
							startActivity(k);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					Log.i(TAG, "Default de switch botones ");
					throw new IllegalStateException("Unexpected value " + view.getId());
			}
		}
	};

	@Override
	public void onPause() {
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isDiscovering()) {
				mBluetoothAdapter.cancelDiscovery();
			}
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		showToast("Vuelvo!!");
		if(primaryDevice == null){
			if(mBluetoothAdapter == null){
				mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			}
			enableComponent();
		}
		super.onResume();
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MULTIPLE_PERMISSIONS: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permissions granted.
					enableComponent(); // Now you call here what ever you want :)
					initializeBroadcastReceiver();
				} else {
					String perStr = "";
					for (String per : permissions) {
						perStr += "\n" + per;
					}
					// permissions list of don't granted permission
					Toast.makeText(this,"Esta aplicación requiere de la aceptación de todos los permisos para funcionar correctamente.", Toast.LENGTH_LONG).show();
				}
				return;
			}
		}
	}

	@Override
	public void showResultOnLabel(String string) {
		Log.i(TAG, "Se ejecuta metodo para setear el string");
		this.textView.setText(string);
	}

}

