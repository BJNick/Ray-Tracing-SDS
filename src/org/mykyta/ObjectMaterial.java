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

    public static ObjectMaterial createMirror(float reflectiveness) {
        ObjectMaterial ret = new ObjectMaterial();
        ret.reflective = true;
        ret.reflectiveness = reflectiveness;
        return ret;
    }

    public static ObjectMaterial createTransparent(float refractionCoeff) {
        ObjectMaterial ret = new ObjectMaterial();
        ret.reflective = true;
        ret.reflectiveness = 1f;  // TODO partial reflectiveness based on angle
        ret.transparent = true;
        ret.refractionCoeff = refractionCoeff;
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

    public ObjectMaterial merge(ObjectMaterial mat) {
        opaque = opaque || mat.opaque;
        diffusionRate = Math.min(diffusionRate, mat.diffusionRate);
        if (albedo == null) albedo = mat.albedo;

        transparent = transparent || mat.transparent;
        refractionCoeff = Math.min(refractionCoeff, mat.refractionCoeff);

        reflective = reflective || mat.reflective;
        reflectiveness = Math.min(reflectiveness, mat.reflectiveness);

        glows = glows || mat.glows;
        if (illumination == null) illumination = mat.illumination;
        castsShadow = castsShadow && mat.castsShadow;

        return this;
    }

}
