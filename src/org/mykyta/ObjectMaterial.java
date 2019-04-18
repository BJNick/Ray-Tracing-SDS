package org.mykyta;

/*
 * Stores information about the material properties of an object
 */

import java.awt.*;

public class ObjectMaterial {

    boolean opaque = false;
    float diffusionRate = 1f;
    Color albedo;

    boolean transparent = false;
    float refractionCoeff = 1f;

    boolean reflective = false;
    float reflectiveness = 1f;

    boolean glows = false;
    Illumination illumination;

    boolean castsShadow = true;

    private ObjectMaterial() {}

    public static ObjectMaterial createOpaque(int albedo, float diffusionRate) {
        ObjectMaterial ret = new ObjectMaterial();
        ret.opaque = true;
        ret.albedo = new Color(albedo);
        ret.diffusionRate = diffusionRate;
        ret.castsShadow = true;
        return ret;
    }

    public static ObjectMaterial createGlowing(Illumination illumination, boolean castsShadow) {
        ObjectMaterial ret = new ObjectMaterial();
        ret.glows = true;
        ret.illumination = illumination;
        ret.castsShadow = castsShadow;
        return ret;
    }

    public static ObjectMaterial createGlowing(int rgb, float intensity, boolean castsShadow) {
        return createGlowing(new Illumination(rgb, intensity), castsShadow);
    }

}