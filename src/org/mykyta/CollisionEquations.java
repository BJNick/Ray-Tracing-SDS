package org.mykyta;

class CollisionEquations {

    // Check for a collision of a given ray with a sphere
    static Vector3[] checkRaySphereCollision(Vector3 p, Vector3 v, Vector3 q, float R) {
        assert v.sqrMag() == 1;
        Vector3 puv = v.scale( v.dot( q.sub(p) ) / v.mag() );
        Vector3 qi = p.add( puv );
        float distSqr = q.sub(qi).sqrMag();

        if (distSqr > R * R)
            return new Vector3[0];

        if (p.sub(q).sqrMag() < R * R) { // Inside the sphere
            /*float x = (float) Math.sqrt(R * R - distSqr); // pc-i1
            Vector3 pC = qi.sub(v.scale(x)); // Not tested
            float di1 = x - Math.abs(p.sub(puv).mag());
            Vector3 pD = p.add(v.scale(di1));
            return new Vector3[]{pD};*/
            float x = (float) Math.sqrt(R * R - distSqr);
            Vector3 pB = qi.add(v.scale(x));
            return new Vector3[]{pB};
        }

        float x = (float) Math.sqrt(R * R - distSqr);
        Vector3 pA = qi.sub( v.scale(x) );
        Vector3 pB = qi.add( v.scale(x) );
        return new Vector3[]{pA, pB};
    }

}
