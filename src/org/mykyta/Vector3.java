package org.mykyta;

/*
 * This class is a representation of a 3D vector [x, y, z], it is required to store information about position of objects and direction of rays.
 */

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

    public Vector3 add(Vector3 v) {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    public float mag() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public String toString() {
        return "[" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ']';
    }
}