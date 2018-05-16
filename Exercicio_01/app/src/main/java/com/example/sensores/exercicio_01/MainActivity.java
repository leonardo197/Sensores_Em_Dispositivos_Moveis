package com.example.sensores.exercicio_01;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView listView;
    private SensorManager mSensorManager;
    private List<Sensor> deviceSensors = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = ((ListView) findViewById(R.id.listView));
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        listView.setAdapter(new ArrayAdapter<Sensor>(this, android.R.layout.simple_list_item_1, deviceSensors));

    }
}
