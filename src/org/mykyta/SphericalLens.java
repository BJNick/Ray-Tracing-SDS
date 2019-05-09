package org.mykyta;

public class SphericalLens implements VisibleObject {

    Vector3 position;
    Vector3 posA;
    Vector3 posB;
    float d;
    float R;
    ObjectMaterial material;

    public SphericalLens(Vector3 position, float r, float f) {
        this.position = position;
        d = 1f;
        R = 0.75f;
        posA = position.sub(Vector3.FRONT.scale(d)); // in front
        posB = position.add(Vector3.FRONT.scale(d)); // behind
        material = ObjectMaterial.createOpaque(0xFFFFFF, 1f);
    }

    private boolean isBehindCuttingPlane(Vector3 point) {
        return point.z > position.z;
    }

    public RaycastHit[] checkRayCollision(Vector3 origin, Vector3 relRay) {
        boolean rayOriginBehind = isBehindCuttingPlane(origin.sub(relRay.scale(10000)));

        Vector3[] hitsA = CollisionEquations
                .checkRaySphereCollision(origin, relRay, posA, R);
        Vector3[] hitsB = CollisionEquations
                .checkRaySphereCollision(origin, relRay, posB, R);

        if (hitsA.length == 0 || hitsB.length == 0)
            return new RaycastHit[0];

        Vector3 hit = null;
        Vector3 from = null;
        boolean inside = false;

        if (hitsA.length > 1 || hitsB.length > 1) // The ray is on the outside
            if (!rayOriginBehind) {
                hit = !isBehindCuttingPlane(hitsB[0]) ? hitsB[0] :
                        hitsB.length > 1 && !isBehindCuttingPlane(hitsB[1]) ? hitsB[1] : null;
                from = posB;
                System.out.println("B hit");
            } else {
                hit = isBehindCuttingPlane(hitsA[0]) ? hitsA[0] :
                        hitsA.length > 1 && isBehindCuttingPlane(hitsA[1]) ? hitsA[1] : null;
                from = posA;
                System.out.println("A hit");
            }
        else { // The ray is on the inside of the lens
            inside = true;
            if (hitsA[0].sub(origin).signedScale(relRay) < hitsB[0].sub(origin).signedScale(relRay)) {
                hit = hitsA[0];
                from = posA;
            } else {
                hit = hitsB[0];
                from = posB;
            }
            System.out.println("inside hit");
        }


        if (hit != null)
            return new RaycastHit[]{
                    new RaycastHit(hit.sub(origin).signedScale(relRay),  // Depth
                            hit,  // Position
                            SphericalLens.class.getSimpleName(),
                            this,
                            hit.sub(from).normalized().scale(inside ? -1 : 1),  // Normal
                            material, inside)};

        return new RaycastHit[0];
    }
}
