package demo.maneater.com.arsearch;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import demo.maneater.com.arsearch.gl.Arrow;
import demo.maneater.com.arsearch.gl.Group;
import demo.maneater.com.arsearch.gl.Mesh;
import demo.maneater.com.arsearch.gl.Plane;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DisplayActivity extends AppCompatActivity implements SensorEventListener {


    private GLSurfaceView mGlSurfaceView = null;
    private DisplayRender mDisplayRender = null;

    private SensorManager mSensorManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlSurfaceView = new GLSurfaceView(this);
        setContentView(mGlSurfaceView);
        mDisplayRender = new DisplayRender();
        mGlSurfaceView.setRenderer(mDisplayRender);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }

    ////////////////////////////////////
    class DisplayGLView extends GLSurfaceView {
        public DisplayGLView(Context context) {
            super(context);
        }
    }

    class DisplayRender implements GLSurfaceView.Renderer {
        private Mesh root;
        private Arrow arrow = null;

        public DisplayRender() {
            rotationMatrix = new float[9];
            rotationMatrixResult = new float[3];

            // Initialize our cube.
            Group group = new Group();
//            Cube cube = new Cube(1, 1, 1);
//            cube.rx = 45;
//            cube.ry = 45;
//            group.add(cube);
            arrow = new Arrow(1, 0);
            group.add(new Plane(1, 1));
            group.add(arrow);
            root = group;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition
         * .khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
         */
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // Set the background color to black ( rgba ).
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
            // Enable Smooth Shading, default not really needed.
            gl.glShadeModel(GL10.GL_SMOOTH);
            // Depth buffer setup.
            gl.glClearDepthf(1.0f);
            // Enables depth testing.
            gl.glEnable(GL10.GL_DEPTH_TEST);
            // The type of depth testing to do.
            gl.glDepthFunc(GL10.GL_LEQUAL);
            // Really nice perspective calculations.
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.
         * khronos.opengles.GL10)
         */
        public void onDrawFrame(GL10 gl) {
            // Clears the screen and depth buffer.
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            // Replace the current matrix with the identity matrix
            gl.glLoadIdentity();
            // Translates 4 units into the screen.
            gl.glTranslatef(0, 0, -4);

            root.rz = mAngle;
            // Draw our scene.
            root.draw(gl);

        }

        /*
         * (non-Javadoc)
         *
         * @see
         * android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition
         * .khronos.opengles.GL10, int, int)
         */
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // Sets the current view port to the new size.
            gl.glViewport(0, 0, width, height);
            // Select the projection matrix
            gl.glMatrixMode(GL10.GL_PROJECTION);
            // Reset the projection matrix
            gl.glLoadIdentity();
            // Calculate the aspect ratio of the window
            GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
            // Select the modelview matrix
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            // Reset the modelview matrix
            gl.glLoadIdentity();
        }
    }

    ////////////////////////////////////////////////////

    private float mAngle = 0.0f;

    private float[] gravity;
    private float[] geomagnetic;

    private float[] rotationMatrix;
    private float[] rotationMatrixResult;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values.clone();
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values.clone();
        }

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            mAngle = event.values.clone()[0];
            Log.d("-----", String.format("angle1:%1$s", mAngle));
        }
        emitAzimuth();
    }

    private void emitAzimuth() {
        if (gravity != null && geomagnetic != null) {
            boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);

            if (success) {
                SensorManager.getOrientation(rotationMatrix, rotationMatrixResult);
                Log.d("-----", String.format("angle2:%1$s", Math.toDegrees(rotationMatrixResult[0])));
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
