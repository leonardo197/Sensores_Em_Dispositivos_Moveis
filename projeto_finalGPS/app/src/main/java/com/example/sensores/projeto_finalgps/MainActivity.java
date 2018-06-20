package com.example.sensores.projeto_finalgps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    public TextView txLongitude;
    public TextView txLatitude;

    public Location cordenadas;
    public LocationManager localionM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txLongitude=(TextView)findViewById(R.id.txLongitude);
        txLatitude=(TextView)findViewById(R.id.txLatitude);



//vwrificar se o gps esta premitido(obrigatorio)
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
          localionM=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
          cordenadas=localionM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }else{localionM=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
          cordenadas=localionM.getLastKnownLocation(LocationManager.GPS_PROVIDER);


       double Longitude=cordenadas.getLongitude();
        double Latitude=cordenadas.getLatitude();

        txLatitude.setText(""+Longitude+" "+cordenadas.getSpeed());
        txLongitude.setText(""+Latitude);




          File arquivo = new File("teste.txt");
          try{
              FileWriter fw = new FileWriter(arquivo);
              fw.write('2');
              fw.write(Longitude+" "+Longitude);
              fw.flush();
          }catch(IOException ex){
              txLongitude.setText("ola");
          }



    }

    }
}
