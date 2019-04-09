package org.mykyta;

import java.awt.*;

public class Illumination {

    private final float R;
    private final float G;
    private final float B;

    public final static Illumination NO_LIGHT = new Illumination(0, 0, 0);
    public final static Illumination WHITE = new Illumination(1, 1, 1);

    public Illumination(float R, float G, float B){
        this.R = R;
        this.G = G;
        this.B = B;
    }

    // Return a screen-pixel color
    public int toScreenColor() {
        return new Color(tr(R), tr(G), tr(B)).getRGB();
    }

    // Dim the light by a factor
    public Illumination dim(float v) {
        return new Illumination(R*v, G*v, B*v);
    }

    // Combine two illuminations
    public Illumination combine(Illumination i) {
        return new Illumination(i.R + R, i.G + G, i.B + B);
    }

    // Apply albedo of a surface
    public Illumination applyAlbedo(Color c, float p) {
        return new Illumination(R * fl(c.getRed()) * p, G * fl(c.getGreen()) * p, B * fl(c.getBlue()) * p);
    }

    // Truncate a number like 1.2432 to 1
    private float tr(float a) {
        return Math.max(Math.min(a, 1), 0);
    }

    // Transform 0-255 range to 0-1
    private float fl(int c) {
        return c / 255f;
    }

}