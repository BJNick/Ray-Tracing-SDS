package org.mykyta;

public class LightSource {

    Illumination baseIllumination;
    Vector3 point;

    public LightSource(Illumination base, Vector3 point) {
        baseIllumination = base;
        this.point = point;
    }

    public Illumination getIlluminationAt(Vector3 pos) {
        // return baseIllumination;
        return baseIllumination.dim(Math.min(1f / pos.sub(point).scale(0.1f).sqrMag(), 1f));
    }

}
