package org.mykyta;

// Spherical object is a visible object

public class SphericalObject implements VisibleObject {

    // A sphere is defined by its origin (position) and radius
    Vector3 position;
    float radius;

    ObjectMaterial material;

    public SphericalObject(Vector3 position, float radius) {
        this.position = position;
        this.radius = radius;
        this.material = ObjectMaterial.createOpaque(0x00FF00, 1f);
    }

    public SphericalObject(Vector3 position, float radius, int color, boolean reflective) {
        this(position, radius);
        if (reflective)
            this.material = ObjectMaterial.createMirror(0.4f).merge(ObjectMaterial.createOpaque(color, 0.8f));
        else
            this.material = ObjectMaterial.createOpaque(color, 1f);
    }

    @Override
    public RaycastHit[] checkRayCollision(Vector3 origin, Vector3 relRay) {
        Vector3 hit[] = CollisionEquations
                .checkRaySphereCollision(origin, relRay, position, radius);
        if (hit != null)
            return new RaycastHit[]{
                    new RaycastHit(hit[0].sub(origin).signedScale(relRay),  // Depth
                            hit[0],  // Position
                            SphericalObject.class.getSimpleName(),
                            this,
                            hit[0].sub(position).normalized(),  // Normal
                            material, false),
                    new RaycastHit(hit[1].sub(origin).signedScale(relRay),  // Depth
                            hit[1],  // Position
                            SphericalObject.class.getSimpleName(),
                            this,
                            hit[1].sub(position).normalized().scale(-1),  // Normal
                            material, true)
            };
        return null;
    }

}
