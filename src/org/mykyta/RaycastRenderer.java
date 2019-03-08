package org.mykyta;

import java.util.ArrayList;
import java.util.List;

public class RaycastRenderer {

    private Iterable<VisibleObject> visibleObjects;
    private int width, height;
    private float fieldOfView;
    private Vector3 cameraPos;

    private final float rasterPlaneDist = 1f;

    public RaycastRenderer(Iterable<VisibleObject> visibleObjects, int width, int height, float fieldOfView) {
        this.visibleObjects = visibleObjects;
        this.width = width;
        this.height = height;
        this.fieldOfView = fieldOfView;
        cameraPos = new Vector3(0,0,-10);
    }

    public int getPixel(int u, int v) {
        // TODO implement raycast
        return 0x000000;
    }

    public Vector3 createRay(int u, int v) {
        float relU =  (u / (width - 1f) - 0.5f) * 2;
        float relV =  (v / (height - 1f) - 0.5f) * 2;
        float planeHalfWidth = (float) Math.tan(fieldOfView / 2) * rasterPlaneDist;
        float planeHalfHeight = height * planeHalfWidth / width;
        return new Vector3(relU * planeHalfWidth, relV * planeHalfHeight, -rasterPlaneDist);
    }

    public int traceRay(Vector3 relRay) {
        List<CastIntersection> intersections = new ArrayList<>();
        for (VisibleObject vo : visibleObjects) {
            vo.checkRayCollision(relRay, cameraPos);
        }
        return 0x000000;
    }
}

class CastIntersection implements Comparable<CastIntersection> {
    public float depth;
    public Vector3 position;
    public Vector3 normal;

    @Override
    public int compareTo(CastIntersection o) {
        return depth > o.depth ? 1 : -1;
    }
}

interface VisibleObject {
    CastIntersection checkRayCollision(Vector3 relRay, Vector3 origin);
}