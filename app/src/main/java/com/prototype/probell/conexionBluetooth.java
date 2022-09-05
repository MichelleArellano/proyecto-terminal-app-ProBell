package com.prototype.probell;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class conexionBluetooth extends AppCompatActivity {

    //Variables para interactuar
    private ListView dispositivosVinculados, dispositivosDisponibles;
    private ProgressBar barraDeProgreso;
    private ArrayAdapter<String> adaptadorDV, adaptadorDD=null;
    private Context contexto;
    private BluetoothAdapter bluetoothAdapter;
    Button escanear, detener;

    //Método para crear la vista
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_bluetooth);
        contexto = this;

        buscarIDs(); //Relaciona los elementos del XML en JAVA
        inicio(); //Configura las listas mostrando los dispositivos vinculados y en espera de una búsqueda

        //Busca dispositivos disponibles con Bluetooth que podrían vincularse
        escanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escanearDispositivos();
            }
        });

        //Detiene la búsqueda de dispositivos Bluetooth
        detener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter.cancelDiscovery();
                barraDeProgreso.setVisibility(View.GONE);
            }
        });
    }

    //Inicializa las listas de dispositivos vinculados y dispositivos disponibles
    private void inicio() {

        adaptadorDV = new ArrayAdapter<String>(contexto, R.layout.lista_de_dispositivos);
        adaptadorDD = new ArrayAdapter<String>(contexto, R.layout.lista_de_dispositivos);

        dispositivosVinculados.setAdapter(adaptadorDV);
        dispositivosDisponibles.setAdapter(adaptadorDD);

        dispositivosDisponibles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);

                Intent intent = new Intent();
                intent.putExtra("deviceAddress", address);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices != null && pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                adaptadorDV.add(device.getName() + "\n" + device.getAddress());
            }
        }

        //Filtran las peticiones
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothBroadcast, intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothBroadcast, intentFilter1);

        dispositivosVinculados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetoothAdapter.cancelDiscovery();

                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);

                Log.d("Address", address);

                Intent intent = new Intent();
                intent.putExtra("deviceAddress", address);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    //Broadcast --> Recibe peticiones Bluetooth
    private BroadcastReceiver bluetoothBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    adaptadorDD.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                barraDeProgreso.setVisibility(View.GONE);
                if (adaptadorDD.getCount() == 0) {
                    Toast.makeText(context, "No se encontraron dispositivos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Selecciona un dispositivo", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    //Empieza el proceso de escaneo de dispositivos cercanos
    private void escanearDispositivos() {
        barraDeProgreso.setVisibility(View.VISIBLE);
        adaptadorDD.clear();
        Toast.makeText(contexto, "Escaneando...", Toast.LENGTH_SHORT).show();

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.startDiscovery();
    }

    //Libera el broadcast de lo que tenga almacenado
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bluetoothBroadcast != null) {
            unregisterReceiver(bluetoothBroadcast);
        }
    }

    //Relacionar XML con JAVA
    private void buscarIDs(){
        escanear = (Button) findViewById(R.id.btescanear);
        detener = (Button) findViewById(R.id.btdetenerDis);
        dispositivosVinculados = findViewById(R.id.lvDV);
        dispositivosDisponibles = findViewById(R.id.lvDD);
        barraDeProgreso = findViewById(R.id.progress_scan_devices);
    }
}