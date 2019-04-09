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
    private float mag = -1;
    private Vector3 normalized;

    // This method is used to create a Vector3 from three x/y/z components.
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Multiply by a constant
    public Vector3 scale(float c) {
        return new Vector3(x * c, y * c, z * c);
    }

    // Add to another vector
    public Vector3 add(Vector3 v) {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    // Find the magnitude (using Pythagorean Theorem)
    public float mag() {
        if (mag == -1)
            mag = (float) Math.sqrt(sqrMag());
        return mag;
    }

    // Find the scale factor for this vector based on it's normal vector
    public float signedScale(Vector3 to) {
        assert sqrMag() != 0;
        return to.x != 0 ? (x / to.x) : to.y != 0 ? (y / to.y) : (z / to.z);
    }

    // Get a normalized vector (with a magnitude of 1)
    public Vector3 normalized() {
        if (normalized == null)
            normalized = scale(1f / mag());
        return normalized;
    }

    // Find the squared magnitude (faster, as square roots are longer to compute)
    public float sqrMag() {
        return x * x + y * y + z * z;
    }

    // Subtract another vector
    public Vector3 sub(Vector3 v) {
        return add(v.scale(-1));
    }

    // Dot multiply
    public float dot(Vector3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    // Cross multiply
    public Vector3 cross(Vector3 v) {
        return new Vector3(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
            );
    }

    // Rotate by an angle around y axis
    public Vector3 rotatedY(float angle) {
        float newX = (float) (x * Math.cos(angle) - z * Math.sin(angle));
        float newZ = (float) (x * Math.sin(angle) + z * Math.cos(angle));
        return new Vector3(newX, y, newZ);
    }

    // Find angle between this and another vector
    public float angle(Vector3 v) {
        return (float) Math.acos(this.dot(v) / (mag() * v.mag()));
    }

    // Apply Rodrigues rotation matrix (important for reflection and refraction)
    // public float rotate(float a, Vector3 v) {}


    // Print out the vector in [x, y, z] format
    @Override
    public String toString() {
        return "[" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ']';
    }
}