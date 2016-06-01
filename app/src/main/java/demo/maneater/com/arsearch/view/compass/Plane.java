package demo.maneater.com.arsearch.view.compass;

import javax.microedition.khronos.opengles.GL10;
import java.nio.*;

public class Plane {


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

    public Plane(float length) {
        float s1 = length / 2;
        //顶点坐标
        float vertices[] = {
                s1, s1, 0, // 0
                -s1, s1, 0, // 1
                -s1, -s1, 0, //2
                s1, -s1, 0//3
        };
        this.verticesBuffer = Util.asFloatBuffer(vertices);

        //顶点顺序
        short indices[] = {
                0, 1, 2,
                2, 3, 0};
        this.indicesBuffer = Util.asShortBuffer(indices);
        this.numOfIndices = indices.length;

        //各个面法线

        //正面 0,0,1
        float[] noraml_1 = new float[]{0, 0, 1};
        // 各顶点法线向量
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer doubleBuffer = vbb.asFloatBuffer();
        doubleBuffer.put(noraml_1);
        doubleBuffer.put(noraml_1);
        doubleBuffer.put(noraml_1);
        doubleBuffer.put(noraml_1);
        doubleBuffer.position(0);
        this.normalBuffer = doubleBuffer;
    }

    public void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glPushMatrix();
        gl.glLoadIdentity();
//        gl.glTranslatef(x, y, z);
//        gl.glRotatef(rx, 1, 0, 0);
//        gl.glRotatef(ry, 0, 1, 0);
//        gl.glRotatef(rz, 0, 0, 1);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.verticesBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices, GL10.GL_UNSIGNED_SHORT, indicesBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glPopMatrix();
    }
}
