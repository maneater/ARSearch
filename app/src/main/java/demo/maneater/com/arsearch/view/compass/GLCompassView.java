package demo.maneater.com.arsearch.view.compass;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.AttributeSet;
import demo.maneater.com.arsearch.gl.Arrow;
import rx.functions.Action1;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLCompassView extends GLSurfaceView implements GLSurfaceView.Renderer {


    private OrientationProvider mOrientationProvider = null;
    private float[] currentOrientation = new float[3];

    public GLCompassView(Context context) {
        super(context);
        init(context);
    }

    public GLCompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        setRenderer(this);
        mOrientationProvider = new OrientationProvider(context.getApplicationContext());
        mOrientationProvider.asObservable().subscribe(new Action1<float[]>() {
            @Override
            public void call(float[] floats) {
                setCurrentOrientation(floats);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mOrientationProvider.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mOrientationProvider.stop();
    }

    public void enableCamera() {

    }

    public void disableCamera() {

    }

    //////////////////////////

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.8f, 0.8f, 0.8f, 0.0f);
        gl.glFrontFace(GL10.GL_CCW);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        initScene();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        //确定视图裁切范围
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        //（相机位置）（视线方向）（相机倾斜方向），
        GLU.gluLookAt(gl, 0, 0, 5f, 0, 0, 0, 0, 1, 0);
        // Select the modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        // Reset the modelview matrix
        gl.glLoadIdentity();

        initLight(gl);
    }

    private void initLight(GL10 gl) {

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
//
//        /////////////////////////
//
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, Util.asFloatBuffer(new float[]{-0f, 10f, 10f, 1.0f})); //指定第0号光源的位置
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, Util.asFloatBuffer(new float[]{1.0f, 1.0f, 1.0f, 1.0f}));//环境光设置
//        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, Util.asFloatBuffer(new float[]{1.0f, 1.0f, 1.0f, 1.0f})); //漫反射后~~
//        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, Util.asFloatBuffer(new float[]{1.0f, 1.0f, 1.0f, 1.0f}));//镜面反射后~~~
//        /////////////////////////
//
//
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, Util.asFloatBuffer(new float[]{0.0f, 0.4f, 0.0f, 1.0f,}));//材质属性中的环境光
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.asFloatBuffer(new float[]{0.0f, 1.0f, 0.0f, 1.0f}));//材质属性中的散射光
//        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, Util.asFloatBuffer(new float[]{0.0f, 1.0f, 0.0f, 1.0f}));//材质属性中的镜面反射光
//        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 64); //材质属性的镜面反射指数

        gl.glLoadIdentity();

    }

    private Arrow mArrow = null;
    private Plane plane = null;


    @Override
    public void onDrawFrame(GL10 gl) {
        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        drawScene(gl);
    }

    private void drawScene(GL10 gl) {

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        if (currentOrientation != null) {
            mArrow.rz = currentOrientation[0];
            mArrow.rx = currentOrientation[1];
            mArrow.ry = -currentOrientation[2];

            plane.rz = currentOrientation[0];
            plane.rx = currentOrientation[1];
            plane.ry = -currentOrientation[2];

        }
        mArrow.draw(gl);
//        plane.draw(gl);
    }

    private void initScene() {
        mArrow = new Arrow(0.5f, 0.1f);
        plane = new Plane(0.5f);
    }

    private ObjectAnimator mObjectAnimator = null;

    public void setCurrentOrientation(float[] currentOrientation) {
        if (mObjectAnimator != null) {
            mObjectAnimator.cancel();
        }
        mObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(this,
                PropertyValuesHolder.ofFloat("azimuth", this.currentOrientation[0], currentOrientation[0]),
                PropertyValuesHolder.ofFloat("pitch", this.currentOrientation[1], currentOrientation[1]),
                PropertyValuesHolder.ofFloat("roll", this.currentOrientation[2], currentOrientation[2]));
        mObjectAnimator.setDuration(100).start();
    }

    public void setAzimuth(float azimuth) {
        currentOrientation[0] = azimuth;
    }


    public void setPitch(float pitch) {
        currentOrientation[1] = pitch;
    }


    public void setRoll(float roll) {
        currentOrientation[2] = roll;
    }
}
