package org.mykyta;

// Spherical object is a visible object

public class SphericalObject implements VisibleObject {

    // A sphere is defined by its origin (position) and radius
    Vector3 position;
    float radius;

    int color;

    public SphericalObject(Vector3 position, float radius) {
        this.position = position;
        this.radius = radius;
        color = 0x00FF00;
    }

    public SphericalObject(Vector3 position, float radius, int color) {
        this(position, radius);
        this.color = color;
    }

    @Override
    public RaycastHit checkRayCollision(Vector3 relRay, Vector3 origin) {
        Vector3 hit = CollisionEquations
                .checkRaySphereCollision(origin, relRay, position, radius, false);
        if (hit != null)
            return new RaycastHit(hit.sub(origin).mag(),  // Depth
                    hit,  // Position
                    SphericalObject.class.getSimpleName(),
                    hit.sub(position).normalized(),  // Normal
                    color); // Albedo
        return null;
    }

}
