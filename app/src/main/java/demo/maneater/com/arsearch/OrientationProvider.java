package demo.maneater.com.arsearch;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import rx.Observable;
import rx.subjects.PublishSubject;

public class OrientationProvider implements SensorEventListener {

    private Context mContext = null;

    private SensorManager mSensorManager = null;
    private PublishSubject<float[]> orientationPublish = PublishSubject.create();


    private float[] gravity;
    private float[] geomagnetic;

    private float[] rotationMatrix = new float[9];
    private float[] rotationMatrixResult = new float[3];


    public OrientationProvider(Context mContext) {
        this.mContext = mContext;
        this.mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
    }

    public void start() {
        this.mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        this.mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
    }

    public void stop() {
        this.mSensorManager.unregisterListener(this);
    }

    public Observable<float[]> asObservable() {
        return orientationPublish.asObservable();
    }

    private void emitResult() {
        if (gravity != null && geomagnetic != null) {
            boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);
            if (success) {
                SensorManager.getOrientation(rotationMatrix, rotationMatrixResult);

                float azimuth = (float) Math.toDegrees(rotationMatrixResult[0]);
                float pitch = rotationMatrixResult[1];
                float roll = rotationMatrixResult[2];
                if (azimuth < 0.0f) {
                    azimuth = 360 + azimuth;
                }
                orientationPublish.onNext(new float[]{azimuth, pitch, roll});
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values.clone();
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values.clone();
        }
        emitResult();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
