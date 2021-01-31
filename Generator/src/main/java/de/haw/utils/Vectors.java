package de.haw.utils;

import mikera.vectorz.Vector;

public class Vectors {
    /**
     * Return a vector rotated by a given angle
     * @param v Vector to be rotated
     * @param angle Angle to be rotated
     * @return Rotated vector
     */
    public static Vector rotate(Vector v, double angle) {
        double degToRad = Math.PI / 180;
        return rotateRadians(v, angle * degToRad);
    }

    /**
     * Return a vector rotated by a given radian
     * @param v Vector to be rotated
     * @param radians Radian to be rotated
     * @return Rotated vector
     */
    private static Vector rotateRadians(Vector v, double radians) {
        var ca = Math.cos(radians);
        var sa = Math.sin(radians);
        return Vector.of(
                ca * v.get(0) - sa * v.get(1),
                sa * v.get(0) + ca * v.get(1)
        );
    }

    public static double distanceBetweenTwoVectors(Vector v1, Vector v2) {
        return Math.sqrt(Math.pow(v1.get(0) - v2.get(0), 2) + Math.pow(v1.get(1) - v2.get(1), 2));
    }
}
