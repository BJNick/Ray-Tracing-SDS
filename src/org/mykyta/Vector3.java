
package org.mykyta;

/*
 * This class is a representation of a 3D vector [x, y, z], it is required to
 *  store information about position of objects and direction of rays.
 */

public class Vector3 {

    static final Vector3 ZERO = new Vector3(0, 0, 0);
    static final Vector3 UP = new Vector3(0, 1, 0);
    static final Vector3 RIGHT = new Vector3(1, 0, 0);
    static final Vector3 FRONT = new Vector3(0, 0, -1);

    final float x, y, z;
    private float mag = -1;
    private Vector3 normalized;

    // This method is used to create a Vector3 from three x/y/z components.
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Multiply by a constant
    Vector3 scale(float c) {
        return new Vector3(x * c, y * c, z * c);
    }

    // Add to another vector
    Vector3 add(Vector3 v) {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    // Find the magnitude (using Pythagorean Theorem)
    float mag() {
        if (mag == -1)
            mag = (float) Math.sqrt(sqrMag());
        return mag;
    }

    // Find the scale factor for this vector based on it's normal vector
    float signedScale(Vector3 to) {
        assert sqrMag() != 0;
        return to.x != 0 ? (x / to.x) : to.y != 0 ? (y / to.y) : (z / to.z);
    }

    // Get a normalized vector (with a magnitude of 1)
    Vector3 normalized() {
        if (normalized == null)
            normalized = scale(1f / mag());
        return normalized;
    }

    // Find the squared magnitude (faster, as square roots are longer to compute)
    float sqrMag() {
        return x * x + y * y + z * z;
    }

    // Subtract another vector
    Vector3 sub(Vector3 v) {
        return add(v.scale(-1));
    }

    // Dot multiply
    float dot(Vector3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    // Cross multiply
    Vector3 cross(Vector3 v) {
        return new Vector3(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
            );
    }

    // Rotate by an angle around y axis
    Vector3 rotatedY(float angle) {
        float newX = (float) (x * Math.cos(angle) - z * Math.sin(angle));
        float newZ = (float) (x * Math.sin(angle) + z * Math.cos(angle));
        return new Vector3(newX, y, newZ);
    }

    // Find angle between this and another vector
    float angle(Vector3 v) {
        return (float) Math.acos(this.dot(v) / (mag() * v.mag()));
    }


    // Apply Rodrigues rotation matrix (important for reflection and refraction)
    Vector3 rotate(float a, Vector3 w0) {
        Matrix3x3 w = new Matrix3x3(
                0, -w0.z, w0.y,
                w0.z, 0, -w0.x,
                -w0.y, w0.x, 0);

        Matrix3x3 Rw = Matrix3x3.I
                .add(w.times((float) Math.sin(a)))
                .add(w.multiply(w).times(1f - (float) Math.cos(a)));

        return Rw.multiply(this);
    }

    // Print out the vector in [x, y, z] format
    @Override
    public String toString() {
        return "[" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ']';
    }


    // Class for matrix multiplication
    private static class Matrix3x3 {

        private final float[][] matrix;

        private final static Matrix3x3 I = new Matrix3x3(1, 0, 0, 0, 1, 0, 0, 0, 1);

        Matrix3x3(float... m) {
            matrix = new float[3][3];
            for (int i = 0; i < m.length; i++) {
                matrix[i/3][i%3] = m[i];
            }
        }

        Matrix3x3(float[][] m) {
            matrix = m;
        }

        float get(int row, int column) {
            return matrix[row][column];
        }

        Matrix3x3 multiply(Matrix3x3 other) {
            float[][] ret = new float[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    ret[i][j] = new Vector3(get(i, 0), get(i, 1), get(i, 2))
                            .dot(new Vector3(other.get(0, j), other.get(1, j), other.get(2, j)));
                }
            }
            return new Matrix3x3(ret);
        }

        Vector3 multiply(Vector3 v) {
            return new Vector3(
                    get(0, 0) * v.x + get(0, 1) * v.y + get(0, 2) * v.z,
                    get(1, 0) * v.x + get(1, 1) * v.y + get(1, 2) * v.z,
                    get(2, 0) * v.x + get(2, 1) * v.y + get(2, 2) * v.z
            );
        }

        Matrix3x3 times(float value) {
            float[][] ret = new float[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    ret[i][j] = matrix[i][j] * value;
                }
            }
            return new Matrix3x3(ret);
        }

        Matrix3x3 add(Matrix3x3 other) {
            float[][] ret = new float[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    ret[i][j] = matrix[i][j] + other.matrix[i][j];
                }
            }
            return new Matrix3x3(ret);
        }

        @Override
        public String toString() {
            return "Matrix3x3{" +
                    matrix[0][0] + ", " +
                    matrix[0][1] + ", " +
                    matrix[0][2] + ", " +
                    matrix[1][0] + ", " +
                    matrix[1][1] + ", " +
                    matrix[1][2] + ", " +
                    matrix[2][0] + ", " +
                    matrix[2][1] + ", " +
                    matrix[2][2] + "" +
                    '}';
        }
    }

}
