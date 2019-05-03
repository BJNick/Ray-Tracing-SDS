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
        // this.material = ObjectMaterial.createOpaque(0x00FF00, 1f);
        this.material = ObjectMaterial.createMirror(1f);
    }

    public SphericalObject(Vector3 position, float radius, int color) {
        this(position, radius);
        this.material = ObjectMaterial.createOpaque(color, 1f);
    }

    @Override
    public RaycastHit checkRayCollision(Vector3 origin, Vector3 relRay) {
        Vector3 hit = CollisionEquations
                .checkRaySphereCollision(origin, relRay, position, radius, false);
        if (hit != null)
            return new RaycastHit(hit.sub(origin).signedScale(relRay),  // Depth
                    hit,  // Position
                    SphericalObject.class.getSimpleName(),
                    this,
                    hit.sub(position).normalized(),  // Normal
                    material);
        return null;
    }

}
