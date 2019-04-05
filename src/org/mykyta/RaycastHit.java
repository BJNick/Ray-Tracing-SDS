package org.mykyta;

/*
 * Thia class stores basic information about a ray collision in 3D space
 */
class RaycastHit {
    float depth;
    Vector3 position;
    Vector3 normal;
    int albedo;
    String objectID;

    RaycastHit(float depth, Vector3 position, String objectID, Vector3 normal, int albedo) {
        this.depth = depth;
        this.position = position;
        this.objectID = objectID;
        this.normal = normal;
        this.albedo = albedo;
    }
}