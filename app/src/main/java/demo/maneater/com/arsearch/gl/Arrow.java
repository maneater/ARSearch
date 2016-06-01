package demo.maneater.com.arsearch.gl;

import demo.maneater.com.arsearch.view.compass.Util;

import javax.microedition.khronos.opengles.GL10;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Arrow {


    private float x;
    private float y;
    private float z;
    public float rx;
    public float ry;
    public float rz;
    private Buffer verticesBuffer;
    private Buffer normalBuffer;
    private Buffer indicesBuffer;
    private int numOfIndices;

    public Arrow(float innerLength, float depth) {
        float s1 = innerLength * 1.732f / 2;
        float s2 = innerLength / 2;
        depth /= 2;

        //顶点坐标
        float vertices[] = {
                0, innerLength, depth, // 0
                -s1, -s2, depth, // 1
                0, 0, depth, //2
                s1, -s2, depth, // 3
                0, innerLength, -depth, // 4
                -s1, -s2, -depth, // 5
                0, 0, -depth, //6
                s1, -s2, -depth // 7
        };
        this.verticesBuffer = Util.asFloatBuffer(vertices);

        //顶点顺序
        short indices[] = {
                0, 1, 2,
                0, 2, 3,
                0, 3, 7,
                0, 7, 4,
                0, 4, 5,
                0, 5, 1,
                2, 1, 5,
                2, 5, 6,
                2, 6, 7,
                2, 7, 3,
                4, 6, 5,
                4, 7, 6};
        this.indicesBuffer = Util.asShortBuffer(indices);
        this.numOfIndices = indices.length;

        //各个面法线

        //正面 0,0,1
        double[] noraml_1 = new double[]{0, 0, 1};
        // 左外侧 cross 4-0, 1-0
        double[] noraml_2 = Util.vectorCross(
                Util.vectorDec(0, innerLength, -depth, 0, innerLength, depth),
                Util.vectorDec(-s1, -s2, depth, 0, innerLength, depth));
        // 右外侧 cross 3-0, 4-0
        double[] noraml_3 = Util.vectorCross(
                Util.vectorDec(s1, -s2, depth, 0, innerLength, depth),
                Util.vectorDec(0, innerLength, -depth, 0, innerLength, depth));

        // 左内测 cross 1-2,6-2
        double[] noraml_4 = Util.vectorCross(
                Util.vectorDec(-s1, -s2, depth, 0, 0, depth),
                Util.vectorDec(0, 0, -depth, 0, 0, depth));
        // 右内测 cross 6-2 3-2
        double[] noraml_5 = Util.vectorCross(
                Util.vectorDec(0, 0, -depth, 0, 0, depth),
                Util.vectorDec(s1, -s2, depth, 0, 0, depth));
        //反面 0,0,-1
        double[] noraml_6 = new double[]{0, 0, -1};


        // 各顶点法线向量
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 8);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = vbb.asFloatBuffer();
        floatBuffer.put(Util.asFloat(Util.vectorAdd(noraml_1, noraml_2, noraml_3)));  //0
        floatBuffer.put(Util.asFloat(Util.vectorAdd(noraml_1, noraml_2, noraml_4)));  //1
        floatBuffer.put(Util.asFloat(Util.vectorAdd(noraml_1, noraml_4, noraml_5)));  //2
        floatBuffer.put(Util.asFloat(Util.vectorAdd(noraml_1, noraml_5, noraml_3)));  //3

        floatBuffer.put(Util.asFloat(Util.vectorAdd(noraml_6, noraml_2, noraml_3)));  //4
        floatBuffer.put(Util.asFloat(Util.vectorAdd(noraml_6, noraml_2, noraml_4)));  //5
        floatBuffer.put(Util.asFloat(Util.vectorAdd(noraml_6, noraml_4, noraml_5)));  //6
        floatBuffer.put(Util.asFloat(Util.vectorAdd(noraml_6, noraml_5, noraml_3)));  //7
        floatBuffer.position(0);
        this.normalBuffer = floatBuffer;
    }

    public void draw(GL10 gl) {
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glPushMatrix();
//        gl.glTranslatef(x, y, z);
        gl.glRotatef(rx, 1, 0, 0);
        gl.glRotatef(ry, 0, 1, 0);
        gl.glRotatef(rz, 0, 0, 1);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.verticesBuffer);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices, GL10.GL_UNSIGNED_SHORT, indicesBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glPopMatrix();
    }
}
