package de.haw.gui;

import mikera.vectorz.Vector;

public class Vectors {

    private static final double degToRad = Math.PI / 180;

    public static Vector rotate(Vector v, double angle) {
        return rotateRadians(v, angle * degToRad);
    }

    private static Vector rotateRadians(Vector v, double radians) {
        var ca = Math.cos(radians);
        var sa = Math.sin(radians);
        return Vector.of(
                ca * v.get(0) - sa * v.get(1),
                sa * v.get(0) + ca * v.get(1)
        );
    }
}
