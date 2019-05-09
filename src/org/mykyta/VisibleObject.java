package org.mykyta;

import com.sun.istack.internal.NotNull;

/*
 * Any child of this class is considered a visible object, which means it interacts with light in some way
 */
public interface VisibleObject {
    @NotNull
    RaycastHit[] checkRayCollision(Vector3 origin, Vector3 relRay);
}