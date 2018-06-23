package com.example.sensores.projeto_finalgps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener, OnMapReadyCallback {
    TextView txLongitude;
    TextView txLatitude;
    TextView textViewCounter;
    Location cordenadas;
    LocationManager localionM;
    SensorManager sensorManager;
    public float passos = 0, t;
    Button btnStart;
    Button btnStop;
    boolean contador_de_passos = false;
    boolean gravar = false;
    public double Longitude;
    public double Latitude;
    String texto_do_arquivo;
    File root;
    public FileWriter writer;
    double X = 0, Z = 0, Y = 0;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txLongitude = (TextView) findViewById(R.id.txLongitude);
        txLatitude = (TextView) findViewById(R.id.txLatitude);
        textViewCounter = (TextView) findViewById(R.id.textViewCounter);

        btnStart = (Button) findViewById(R.id.buttonStart);
        btnStop = (Button) findViewById(R.id.buttonStop);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        btnStart.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        criar_ficheiro_csv();
                    }
                });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guarda_ficheiro_csv();
            }
        });
        txLongitude = (TextView) findViewById(R.id.txLongitude);
        txLatitude = (TextView) findViewById(R.id.txLatitude);
    }

    public void criar_ficheiro_csv() {
        passos = 0;
        String nome_do_arquivo = "teste.csv";
        textViewCounter.setText(String.valueOf(passos));
        root = new File(Environment.getExternalStorageDirectory(), "Download");
        if (!root.exists()) {
            root.mkdirs();
        }
        File gpxfile = new File(root, nome_do_arquivo);
        try {
            writer = new FileWriter(gpxfile);
            Toast.makeText(getApplicationContext(), "a gravar", Toast.LENGTH_SHORT).show();
            writer.append("Longitude;Latitude;Pedometro;Posicao X;Posicao Y;Posicao Z");
            writer.append('\n');
            gravar = true;

        } catch (IOException e) {

            Toast.makeText(getApplicationContext(), "n'Saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void guarda_ficheiro_csv() {
        try {
            writer.flush();
            writer.close();
            Toast.makeText(getApplicationContext(), "ficheiro guardado", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "erro na a gravar ficheiro", Toast.LENGTH_SHORT).show();
        }
        gravar=false;
    }
    public void testa_sensor_stps(){
        contador_de_passos = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor acelerometroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(new AcelerometroSensor(), acelerometroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Sensor não encontrado", Toast.LENGTH_SHORT).show();
        }
    }
    public void guarda_dados(){
        try {
            texto_do_arquivo = Longitude + ";" + Latitude + ";" + passos + ";" + X + ";" + Y + ";" + Z;
            writer.append(texto_do_arquivo);
            writer.append('\n');
        } catch (IOException e) {

            Toast.makeText(this, "erro na escrita do ficheiro", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onResume() {
        super.onResume();
        testa_sensor_stps();
    }
    protected void onPause() {
        super.onPause();
        contador_de_passos = false;
    }


    public void onSensorChanged(SensorEvent event) {
        if (contador_de_passos) {
            //textViewCounter.setText(String.valueOf(passos));
            if (t < event.values[0]) {
                passos++;
                textViewCounter.setText(String.valueOf(passos));
            } else {
                textViewCounter.setText(String.valueOf(passos));
            }
            t = event.values[0];
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "E necessário dar permissões para esta app", Toast.LENGTH_SHORT).show();
            } else {
                localionM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                cordenadas = localionM.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                Longitude = cordenadas.getLongitude();
                Latitude = cordenadas.getLatitude();

                txLatitude.setText("" + Longitude);
                txLongitude.setText("" + Latitude);

                if (gravar == true) {
                    guarda_dados();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class AcelerometroSensor implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (gravar == true) {
                X = event.values[0];
                Y = event.values[1];
                Z = event.values[2];
            }
        }
    }

    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(-25.443150, -49.238243)).title("Jardim Botânico"));
    }
}