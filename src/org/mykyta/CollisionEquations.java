package org.mykyta;

public class CollisionEquations {

    // Check for a collision of a given ray with a sphere
    public static Vector3 checkRaySphereCollision(Vector3 p, Vector3 v, Vector3 q, float R, boolean inside) {
        assert v.sqrMag() == 1;
        Vector3 puv = v.scale( v.dot( q.sub(p) ) / v.mag() );
        Vector3 qi = p.add( puv );
        float dist = q.sub(qi) .mag();
        if (dist > R)
            return null;
        float x = (float) Math.sqrt(R*R - dist*dist);
        Vector3 pA = qi.sub( v.scale(x) );
        Vector3 pB = qi.add( v.scale(x) );
        return inside ? pB : pA;
    }

}
