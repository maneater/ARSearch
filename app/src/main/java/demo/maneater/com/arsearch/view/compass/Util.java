package demo.maneater.com.arsearch.view.compass;

import android.graphics.Point;
import org.apache.commons.math3.util.MathArrays;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Administrator on 2016/5/26 0026.
 */
public class Util {

    public static FloatBuffer asFloatBuffer(float[] data) {
        ByteBuffer ibb = ByteBuffer.allocateDirect(data.length * 4);
        ibb.order(ByteOrder.nativeOrder());
        return (FloatBuffer) ibb.asFloatBuffer().put(data).position(0);
    }

    public static ShortBuffer asShortBuffer(short[] data) {
        ByteBuffer ibb = ByteBuffer.allocateDirect(data.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        return (ShortBuffer) ibb.asShortBuffer().put(data).position(0);
    }

    public static double[] vectorUnit(float x1, float y1, float x2, float y2) {
        double x = x2 - x1;
        double y = y2 - y1;
        if (x == 0 && y == 0) {
            return new double[]{0, 0};
        }
        double hypot = Math.hypot(x, y);
        return new double[]{x / hypot, y / hypot};
    }

    public static double[] vectorAdd(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new double[]{x1 + x2, y1 + y2, z1 + z2};
    }

    public static double[] vectorAdd(double[] v1, double[] v2) {
        return vectorAdd(v1[0], v1[1], v1[2], v2[0], v2[1], v2[2]);
    }

    public static double[] vectorAdd(double[] v1, double[] v2, double[] v3) {
        return vectorAdd(vectorAdd(v1, v2), v3);
    }

    public static double[] vectorDec(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new double[]{x1 - x2, y1 - y2, z1 - z2};
    }

    public static double[] vectorCross(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new double[]{
                MathArrays.linearCombination(y2, z1, -z2, y1),
                MathArrays.linearCombination(z2, x1, -x2, z1),
                MathArrays.linearCombination(x2, y1, -y2, x1)};
    }

    public static double[] vectorCross(double[] v1, double[] v2) {
        return vectorCross(v1[0], v1[1], v1[2], v2[0], v2[1], v2[2]);
    }


    /**
     * 计算点绕另一个点旋转后的坐标
     */
    public static void rotatePoint(Point srcPoint, Point centerPoint, int
            angle) {
        float angleR = (float) Math.toRadians(angle);
        int x1 = srcPoint.x;
        int y1 = srcPoint.y;
        srcPoint.x = (int) ((x1 - centerPoint.x) * Math.cos(angleR)
                + (y1 - centerPoint.y) * Math.sin(angleR) + centerPoint.x);
        srcPoint.y = (int) (-(x1 - centerPoint.x) * Math.sin(angleR)
                + (y1 - centerPoint.y) * Math.cos(angleR) + centerPoint.y);
    }
}
