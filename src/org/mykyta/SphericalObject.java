package org.mykyta;

// Spherical object is a visible object

import java.awt.*;

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
    public RaycastHit checkRayCollision(Vector3 origin, Vector3 relRay) {
        Vector3 hit = CollisionEquations
                .checkRaySphereCollision(origin, relRay, position, radius, false);
        if (hit != null)
            return new RaycastHit(hit.sub(origin).signedScale(relRay),  // Depth
                    hit,  // Position
                    SphericalObject.class.getSimpleName(),
                    this,
                    hit.sub(position).normalized(),  // Normal
                    color); // Albedo
        return null;
    }

    private int quickShading(int color, float depth, float maxdepth) {
        float ratio = Math.max(Math.min(1 - depth / maxdepth, 1), 0);
        Color c = new Color(color);
        c = new Color(Math.round(c.getRed() * ratio), Math.round(c.getGreen() * ratio), Math.round(c.getBlue() * ratio));
        return c.getRGB();
    }

}
