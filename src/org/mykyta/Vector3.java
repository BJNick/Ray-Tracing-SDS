package org.mykyta;

public class Vector3 {

    public static Vector3 ZERO = new Vector3(0, 0, 0);
    public static Vector3 UP = new Vector3(0, 1, 0);
    public static Vector3 RIGHT = new Vector3(1, 0, 0);
    public static Vector3 FRONT = new Vector3(0, 0, -1);

    public final float x, y, z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 times(float c) {
        return new Vector3(x * c, y * c, z * c);
    }

}
