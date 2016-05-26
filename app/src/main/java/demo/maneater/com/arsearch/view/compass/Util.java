package demo.maneater.com.arsearch.view.compass;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Administrator on 2016/5/26 0026.
 */
public class Util {

    public static FloatBuffer asFloatBuffer(float[] data) {
        ByteBuffer ibb = ByteBuffer.allocateDirect(data.length * 4);
        ibb.order(ByteOrder.nativeOrder());
        return (FloatBuffer) ibb.asFloatBuffer().put(data).position(0);
    }
}
