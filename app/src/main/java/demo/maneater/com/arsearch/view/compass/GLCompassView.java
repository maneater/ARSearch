package demo.maneater.com.arsearch.view.compass;

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
    private float[] currentOrientation;

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
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        gl.glFrontFace(GL10.GL_CCW);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
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
        GLU.gluLookAt(gl, 0, 0, 4f, 0, 0, 0, 0, 1, 0);
        initLight(gl);
    }

    private void initLight(GL10 gl) {

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glEnable(GL10.GL_COLOR_MATERIAL);

        float sun_light_position[] = {0.0f, 0.0f, 4f, 1.0f};

        float sun_light_ambient[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float sun_light_diffuse[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float sun_light_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};

        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, Util.asFloatBuffer(sun_light_position)); //指定第0号光源的位置
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, Util.asFloatBuffer(sun_light_ambient));//环境光设置
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, Util.asFloatBuffer(sun_light_diffuse)); //漫反射后~~

//        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, Util.asFloatBuffer(sun_light_specular));//镜面反射后~~~

        //对于材质，R、G、B值为材质对光的R、G、B成分的反射率。比如，一种材质的R＝1.0，G＝0.5，B＝0.0，则材质反射全部的红色成分，一半的绿色成分，不反射蓝色成分。
        float[] mat_amb = {0.2f, 1f, 0.2f, 1.0f};
        float[] mat_diff = {0.0f, 0.04f, 0.0f, 0.0f};
        float[] mat_spec = {0.0f, 0.0f, 0.0f, 0.0f};

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, Util.asFloatBuffer(mat_amb));
//        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, Util.asFloatBuffer(mat_diff));
//        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, Util.asFloatBuffer(mat_spec));
//        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 64.0f);

    }

    private Arrow mArrow = null;


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
//            mArrow.rz = currentOrientation[0];
            mArrow.rx = currentOrientation[1];
//            mArrow.ry = currentOrientation[2];
        }
        mArrow.draw(gl);
    }

    private void initScene() {
        mArrow = new Arrow(0.5f, 0.1f);
    }

    public void setCurrentOrientation(float[] currentOrientation) {
        this.currentOrientation = currentOrientation;
    }


}
