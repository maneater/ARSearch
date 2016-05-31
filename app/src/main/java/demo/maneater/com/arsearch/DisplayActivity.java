//package demo.maneater.com.arsearch;
//
//import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.opengl.GLSurfaceView;
//import android.opengl.GLU;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import demo.maneater.com.arsearch.gl.Arrow;
//import demo.maneater.com.arsearch.gl.Group;
//import demo.maneater.com.arsearch.gl.Mesh;
//
//import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.opengles.GL10;
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
//
//public class DisplayActivity extends AppCompatActivity implements SensorEventListener {
//
//
//    private GLSurfaceView mGlSurfaceView = null;
//    private DisplayRender mDisplayRender = null;
//
//    private SensorManager mSensorManager = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mGlSurfaceView = new GLSurfaceView(this);
//        setContentView(mGlSurfaceView);
//        mDisplayRender = new DisplayRender();
//        mGlSurfaceView.setRenderer(mDisplayRender);
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
//
//        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mSensorManager.unregisterListener(this);
//    }
//
//    ////////////////////////////////////
//    class DisplayGLView extends GLSurfaceView {
//        public DisplayGLView(Context context) {
//            super(context);
//        }
//    }
//
//    class DisplayRender implements GLSurfaceView.Renderer {
//        private Mesh root;
//
//        public DisplayRender() {
//            rotationMatrix = new float[9];
//            rotationMatrixResult = new float[3];
//            Group group = new Group();
//            group.add(new Arrow(0.5f, 0.1f));
//            root = group;
//        }
//
//        /*
//         * (non-Javadoc)
//         *
//         * @see
//         * android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition
//         * .khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
//         */
//        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//
//            // Set the background color to black ( rgba ).
//            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
//            // Enable Smooth Shading, default not really needed.
//            gl.glShadeModel(GL10.GL_SMOOTH);
//            // Depth buffer setup.
//            gl.glClearDepthf(1.0f);
//            // Enables depth testing.
//            gl.glEnable(GL10.GL_DEPTH_TEST);
//
//            // The type of depth testing to do.
//            gl.glDepthFunc(GL10.GL_LEQUAL);
//            // Really nice perspective calculations.
//            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
//
//
//        }
//
//        /*
//         * (non-Javadoc)
//         *
//         * @see
//         * android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.
//         * khronos.opengles.GL10)
//         */
//        public void onDrawFrame(GL10 gl) {
//            // Clears the screen and depth buffer.
//            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
//            // Replace the current matrix with the identity matrix
//            gl.glLoadIdentity();
//            // Translates 4 units into the screen.
//            gl.glTranslatef(0, 0, -4);
//
//            root.rz = mAngle;
//            root.rx = mAngle2;
//            root.ry = mAngle3;
//            // Draw our scene.
//            root.draw(gl);
//
//        }
//
//        /*
//         * (non-Javadoc)
//         *
//         * @see
//         * android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition
//         * .khronos.opengles.GL10, int, int)
//         */
//        public void onSurfaceChanged(GL10 gl, int width, int height) {
//            // Sets the current view port to the new size.
//            gl.glViewport(0, 0, width, height);
//            // Select the projection matrix
//            gl.glMatrixMode(GL10.GL_PROJECTION);
//            // Reset the projection matrix
//            gl.glLoadIdentity();
//            // Calculate the aspect ratio of the window
//            GLU.gluPerspective(gl, 45.0f, (float) width / (float) height,
//                    0.1f, 100.0f);
//            // Select the modelview matrix
//            gl.glMatrixMode(GL10.GL_MODELVIEW);
//            // Reset the modelview matrix
//            gl.glLoadIdentity();
//
//
//            // Set the background color to black ( rgba ).
//            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
//            // Enable Smooth Shading, default not really needed.
//            gl.glShadeModel(GL10.GL_SMOOTH);
//            // Depth buffer setup.
//            gl.glClearDepthf(1.0f);
//            // Enables depth testing.
//            gl.glEnable(GL10.GL_DEPTH_TEST);
//            // The type of depth testing to do.
//            gl.glDepthFunc(GL10.GL_LEQUAL);
//            // Really nice perspective calculations.
//            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
//
//
//            float[] mat_amb = {0.2f * 1.0f, 0.2f * 0.4f, 0.2f * 0.4f, 1.0f,};
//            float[] mat_diff = {1.0f, 0.4f, 0.4f, 1.0f,};
//            float[] mat_spec = {1.0f, 1.0f, 1.0f, 1.0f,};
//
//            ByteBuffer mabb = ByteBuffer.allocateDirect(mat_amb.length * 4);
//            mabb.order(ByteOrder.nativeOrder());
//            FloatBuffer mat_ambBuf = mabb.asFloatBuffer();
//            mat_ambBuf.put(mat_amb);
//            mat_ambBuf.position(0);
//
//            ByteBuffer mdbb = ByteBuffer.allocateDirect(mat_diff.length * 4);
//            mdbb.order(ByteOrder.nativeOrder());
//            FloatBuffer mat_diffBuf = mdbb.asFloatBuffer();
//            mat_diffBuf.put(mat_diff);
//            mat_diffBuf.position(0);
//
//            ByteBuffer msbb = ByteBuffer.allocateDirect(mat_spec.length * 4);
//            msbb.order(ByteOrder.nativeOrder());
//            FloatBuffer mat_specBuf = msbb.asFloatBuffer();
//            mat_specBuf.put(mat_spec);
//            mat_specBuf.position(0);
//
//
//            gl.glClearColor(0.8f, 0.8f, 0.8f, 0.0f);
//            gl.glEnable(GL10.GL_DEPTH_TEST);
//            gl.glEnable(GL10.GL_CULL_FACE);
//            gl.glShadeModel(GL10.GL_SMOOTH);
//
//            gl.glEnable(GL10.GL_LIGHTING);
//            gl.glEnable(GL10.GL_LIGHT0);
//            gl.glEnable(GL10.GL_COLOR_MATERIAL);
//
//            FloatBuffer lightAmbient = FloatBuffer.wrap(new float[]{1f, 1f, 1f, 1f});
//            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient);
//
//
////
////            //定义漫射光
//            FloatBuffer lightDiffuse = FloatBuffer.wrap(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
////            //设置漫射光
//            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse);
////            定义光源的位置(前三个参数为坐标X,Y,Z; 第四个参数为1.0f,是告诉OpenGl这里指定的坐标就是光源的位置)
//            FloatBuffer lightPosition = FloatBuffer.wrap(new float[]{1f, 1f, 1f, 1f});
//            //设置光源的位置
//            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition);
//
//
////            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambBuf);
////            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffBuf);
////            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, mat_specBuf);
////            gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 64.0f);
//
//            gl.glLoadIdentity();
////            GLU.gluLookAt(gl, 0.0f, 0.0f, 10.0f,
////                    0.0f, 0.0f, 0.0f,
////                    0.0f, 1.0f, 0.0f);
//
//
//        }
//    }
//
//
//    ////////////////////////////////////////////////////
//
//    private float mAngle = 0.0f;
//    private float mAngle2 = 0.0f;
//    private float mAngle3 = 0.0f;
//
//    private float[] gravity;
//    private float[] geomagnetic;
//
//    private float[] rotationMatrix;
//    private float[] rotationMatrixResult;
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            gravity = event.values.clone();
//        }
//
//        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//            geomagnetic = event.values.clone();
//        }
//
//        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
//            mAngle = event.values[0];
//            mAngle2 = event.values[1];
//            mAngle3 = event.values[2];
//        }
//        emitAzimuth();
//    }
//
//    private void emitAzimuth() {
//        if (gravity != null && geomagnetic != null) {
//            boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);
//
//            if (success) {
//                SensorManager.getOrientation(rotationMatrix, rotationMatrixResult);
//
//
////                Log.d("-----", String.format("angle2:%1$s", Math.toDegrees(rotationMatrixResult[0])));
//            }
//        }
//    }
//
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//}
