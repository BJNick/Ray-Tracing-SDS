package org.mykyta;

public class LightSource {

    Illumination illum;

    public LightSource(float intencity) {
        illum = Illumination.WHITE.dim(intencity);
    }

}
