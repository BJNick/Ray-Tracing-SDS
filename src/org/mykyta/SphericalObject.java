package org.mykyta;

// Spherical object is a visible object

public class SphericalObject implements VisibleObject {

    // A sphere is defined by its origin (position) and radius
    Vector3 position;
    float radius;

    ObjectMaterial material;
    String name;

    public SphericalObject(Vector3 position, float radius) {
        this.position = position;
        this.radius = radius;
        this.material = ObjectMaterial.createOpaque(0x00FF00, 1f);
        name = SphericalObject.class.getSimpleName();
    }

    public SphericalObject(Vector3 position, float radius, int color, float reflective) {
        this(position, radius);
        this.material = ObjectMaterial.createMirror(reflective).merge(ObjectMaterial.createOpaque(color, 1f - reflective));
    }

    @Override
    public RaycastHit[] checkRayCollision(Vector3 origin, Vector3 relRay) {
        Vector3 hit[] = CollisionEquations
                .checkRaySphereCollision(origin, relRay, position, radius);
        if (hit.length == 2)
            return new RaycastHit[]{
                    new RaycastHit(hit[0].sub(origin).signedScale(relRay),  // Depth
                            hit[0],  // Position
                            name,
                            this,
                            hit[0].sub(position).normalized(),  // Normal
                            material, false),
                    new RaycastHit(hit[1].sub(origin).signedScale(relRay),  // Depth
                            hit[1],  // Position
                            name,
                            this,
                            hit[1].sub(position).normalized().scale(-1),  // Normal
                            material, true)
            };
        else if (hit.length == 1)
            return new RaycastHit[]{
                    new RaycastHit(hit[0].sub(origin).signedScale(relRay),  // Depth
                            hit[0],  // Position
                            name,
                            this,
                            hit[0].sub(position).normalized(),  // Normal
                            material, false)
            };
        return null;
    }

}
