package org.mykyta;

import java.awt.*;

/*
 * Thia class stores basic information about a ray collision in 3D space
 */
class RaycastHit {

    float depth;
    Vector3 position;
    Vector3 normal;
    Color albedo;
    String objectID;
    VisibleObject object;

    boolean opaque = true;
    float diffusionRate = 1f;

    boolean transparent = false;
    float refractionCoeff = 1f;

    boolean reflective = false;
    float reflectiveness = 1f;

    RaycastHit(float depth, Vector3 position, String objectID, VisibleObject object, Vector3 normal, Color albedo) {
        this.depth = depth;
        this.position = position;
        this.objectID = objectID;
        this.object = object;
        this.normal = normal;
        this.albedo = albedo;
    }

    RaycastHit(float depth, Vector3 position, String objectID, VisibleObject object, Vector3 normal, int albedo) {
        this(depth, position, objectID, object, normal, new Color(albedo));
    }

}