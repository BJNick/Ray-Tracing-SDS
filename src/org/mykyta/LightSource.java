package org.mykyta;

public class LightSource extends SphericalObject {

    private Illumination baseIllumination;
    Vector3 point;
    private ObjectMaterial material;

    LightSource(Illumination base, Vector3 point) {
        super(point, 0.1f, 0xFFFFFF, 0f);
        baseIllumination = base;
        material = ObjectMaterial.createGlowing(base, false);
        this.point = point;
    }

    Illumination getIlluminationAt(Vector3 pos) {
        // return baseIllumination;
        return baseIllumination.dim(Math.min(1f / pos.sub(point).scale(0.1f).sqrMag(), 1f));
    }

    @Override
    public RaycastHit[] checkRayCollision(Vector3 origin, Vector3 relRay) {
        RaycastHit[] hit = super.checkRayCollision(origin, relRay);
        if (hit == null)
            return null;
        return new RaycastHit[]{hit[0].changeMaterial(material)};
    }

}
