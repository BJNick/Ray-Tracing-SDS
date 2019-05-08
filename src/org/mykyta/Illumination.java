package org.mykyta;

import java.awt.*;

public class Illumination {

    private final float R;
    private final float G;
    private final float B;

    public final static Illumination NO_LIGHT = new Illumination(0, 0, 0);
    public final static Illumination WHITE = new Illumination(1, 1, 1);
    public final static Illumination AMBIENT = new Illumination(0.5f, 0.5f, 0.5f);
    public final static Illumination ERROR = new Illumination(1f, 0f, 1f);

    public Illumination(float R, float G, float B){
        this.R = R;
        this.G = G;
        this.B = B;
    }

    public Illumination(int rgb, float intencity){
        Color color = new Color(rgb);
        this.R = color.getRed() / 255f * intencity;
        this.G = color.getGreen() / 255f * intencity;
        this.B = color.getBlue() / 255f * intencity;
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

    // Apply the angle of a surface using cos
    public Illumination applyAngle(float angle) {
        float ratio = (float) Math.cos(angle);
        return dim(ratio);
    }

    public static float getPartialReflection(float i, float r) {

        float pp2 = (float) Math.sin(i - r);
        float pp1 = (float) Math.sin(i + r);
        float perpendicular = (pp2 * pp2) / (pp1 * pp1);

        float pl2 = (float) Math.tan(i - r);
        float pl1 = (float) Math.tan(i + r);
        float parallel = (pl2 * pl2) / (pl1 * pl1);

        if (Float.isNaN(perpendicular) || Float.isNaN(parallel))
            return 0;

        return (perpendicular + parallel) / 2;

    }

    // Truncate a number like 1.2432 to 1
    private float tr(float a) {
        return Math.max(Math.min(a, 1), 0);
    }

    // Transform 0-255 range to 0-1
    private float fl(int c) {
        return c / 255f;
    }

    @Override
    public String toString() {
        return "Illumination{" +
                "R=" + R +
                ", G=" + G +
                ", B=" + B +
                '}';
    }
}
