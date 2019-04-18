package org.mykyta;

/*
 * Thia class stores basic information about a ray collision in 3D space
 */
class RaycastHit {

    final float depth;
    final Vector3 position;
    final Vector3 normal;
    final String objectID;
    final VisibleObject object;
    final ObjectMaterial material;

    RaycastHit(float depth, Vector3 position, String objectID, VisibleObject object, Vector3 normal, ObjectMaterial material) {
        this.depth = depth;
        this.position = position;
        this.objectID = objectID;
        this.object = object;
        this.normal = normal;
        this.material = material;
    }

    public RaycastHit changeMaterial(ObjectMaterial newmat) {
        return new RaycastHit(depth, position, objectID, object, normal, newmat);
    }

}