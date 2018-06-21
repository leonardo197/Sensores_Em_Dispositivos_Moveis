package com.example.sensores.projeto_finalgps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    public TextView txLongitude;
    public TextView txLatitude;

    public Location cordenadas;
    public LocationManager localionM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        txLongitude = (TextView) findViewById(R.id.txLongitude);
        txLatitude = (TextView) findViewById(R.id.txLatitude);


//vwrificar se o gps esta premitido(obrigatorio)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //localionM=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            //cordenadas=localionM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            localionM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            cordenadas = localionM.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            double Longitude = cordenadas.getLongitude();
            double Latitude = cordenadas.getLatitude();

            txLatitude.setText("" + Longitude);
            txLongitude.setText("" + Latitude);


///////////////////////////////////////////////////////////////////
            String nome_do_arquivo = "teste.csv";
            String texto_do_arquivo = "ola ,1,234,9,";
            try {
                File root = new File(Environment.getExternalStorageDirectory(), "Download");
                if (!root.exists()) {
                    root.mkdirs();
                }
                File gpxfile = new File(root,nome_do_arquivo);
                FileWriter writer= new FileWriter(gpxfile);

                writer.append("DisplayName");
                writer.append(',');
                writer.append("Age");
                writer.append('\n');

                writer.append("MKYONG");
                writer.append(',');
                writer.append("26");
                writer.append('\n');

                writer.append("YOUR NAME");
                writer.append(',');
                writer.append("29");
                writer.append('\n');
                writer.append(texto_do_arquivo);
                writer.flush();
                writer.close();
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {

                Toast.makeText(this, "nao", Toast.LENGTH_SHORT).show();
            }

        }

    }


}