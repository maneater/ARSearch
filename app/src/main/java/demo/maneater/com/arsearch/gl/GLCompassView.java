package demo.maneater.com.arsearch.gl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import demo.maneater.com.arsearch.OrientationProvider;
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

    private void init(Context context) {
        setEGLContextClientVersion(2);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
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
        // Set the background color to black ( rgba ).
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        // Depth buffer setup.
        gl.glClearDepthf(1.0f);
        // Enables depth testing.
        gl.glEnable(GL10.GL_DEPTH_TEST);
        // The type of depth testing to do.
        gl.glDepthFunc(GL10.GL_LEQUAL);
        // Really nice perspective calculations.
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }

    public void setCurrentOrientation(float[] currentOrientation) {
        this.currentOrientation = currentOrientation;
    }
}
