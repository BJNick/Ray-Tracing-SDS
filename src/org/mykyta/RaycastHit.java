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

    boolean diffuse = true;
    float diffusionRate = 1f;

    boolean transparent = false;
    float refractionCoeff = 1f;

    boolean reflective = false;
    float reflectiveness = 1f;

    RaycastHit(float depth, Vector3 position, String objectID, Vector3 normal, Color albedo) {
        this.depth = depth;
        this.position = position;
        this.objectID = objectID;
        this.normal = normal;
        this.albedo = albedo;
    }

}