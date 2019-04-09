package org.mykyta;

public class LightSource {

    Illumination baseIllumination;
    Vector3 point;

    public LightSource(Illumination base, Vector3 point) {
        baseIllumination = base;
        this.point = point;
    }

    public Illumination getIlluminationAt(Vector3 pos) {
        return baseIllumination;
       // return baseIllumination.dim(1f / pos.sub(point).sqrMag());
    }

}
