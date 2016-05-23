package demo.maneater.com.arsearch.gl;

public class Arrow extends Mesh {

    public Arrow(float length, float depth) {
        length /= 2;
        float s1 = length / 2.0f / 1.732f;
        float s2 = length * 1.732f - s1;
        depth /= 2;

        float vertices[] = {-length, -s1, -depth, // 0
                length, -s1, -depth, // 1
                0, s2, -depth, // 2
        };
        short indices[] = {0, 1, 2};
        setIndices(indices);
        setVertices(vertices);
        setColor(0, 1, 0, 1);
    }
}
