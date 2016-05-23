package demo.maneater.com.arsearch;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SensorActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SensorEventListener {

    private SensorManager mSensorManager = null;

    private ListView vSensorList = null;
    private TextView vTxInfo = null;
    private List<Sensor> sensorList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        vSensorList = (ListView) findViewById(R.id.vSensorList);
        vTxInfo = (TextView) findViewById(R.id.vTxInfo);
        vTxInfo.setMovementMethod(ScrollingMovementMethod.getInstance());

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        vSensorList.setOnItemClickListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        vSensorList.setAdapter(arrayAdapter);
        arrayAdapter.addAll(dumpToStringList(sensorList));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSensor != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    private static List<String> dumpToStringList(List<Sensor> sensorList) {
        if (sensorList != null) {
            List<String> stringList = new ArrayList<String>();
            for (Sensor sensor : sensorList) {
                stringList.add(sensor.toString());
            }
            return stringList;
        }
        return null;
    }

    private Sensor mSensor = null;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mSensor != null) {
            mSensorManager.unregisterListener(this);
        }
        mSensor = sensorList.get(position);
        vTxInfo.setText(sensorList.get(position).toString());
        mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("---", "onSensorChanged:" + Arrays.toString(event.values));
//        vTxInfo.append("\nonSensorChanged:" + Arrays.toString(event.values));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("---", "AccuracyChanged:\" + accuracy");
//        vTxInfo.append("\nonAccuracyChanged:" + accuracy);
    }
}
