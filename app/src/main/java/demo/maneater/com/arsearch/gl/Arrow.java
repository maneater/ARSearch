package demo.maneater.com.arsearch.gl;

import javax.microedition.khronos.opengles.GL10;

public class Arrow extends Mesh {

    public Arrow(float innerLength, float depth) {
        float s1 = innerLength * 1.732f / 2;
        float s2 = innerLength / 2;
        depth /= 2;

        float vertices[] = {0, innerLength, depth, // 0
                -s1, -s2, depth, // 1
                0, 0, depth, //2
                s1, -s2, depth, // 3
                0, innerLength, -depth, // 4
                -s1, -s2, -depth, // 5
                0, 0, -depth, //6
                s1, -s2, -depth // 7
        };
        float normals[] = {0, 1, 1, // 0
                -s1, -s2, depth, // 1
                0, 0, depth, //2
                0, 0, 1, // 3
                0, innerLength, -depth, // 4
                -s1, -s2, -depth, // 5
                0, 0, -depth, //6
                s1, -s2, -depth // 7
        };
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
        setIndices(indices);
        setVertices(vertices);
        setNormals(normals);
        setColor(0, 1, 0, 1);
    }

    @Override
    public void draw(GL10 gl) {
        super.draw(gl);
    }
}
