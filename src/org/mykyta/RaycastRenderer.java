package org.mykyta;

/*
 *   This class sends a ray for each requested pixel and returns the resultant color. It is essential for the raytracing process.
 */

public class RaycastRenderer {

    private Iterable<VisibleObject> visibleObjects;
    private int width, height;
    private float fieldOfView;
    public Vector3 cameraPos;

    private final float rasterPlaneDist = 1f;

    public RaycastRenderer(Iterable<VisibleObject> visibleObjects, int width, int height, float fieldOfView) {
        this.visibleObjects = visibleObjects;
        this.width = width;
        this.height = height;
        this.fieldOfView = fieldOfView;
        cameraPos = new Vector3(0, 0, 10);
    }

    public int getPixel(int u, int v) {
        Vector3 relRay = createRay(u, v);
        RaycastHit hit = traceRay(cameraPos, relRay);
        if (hit != null)
            return hit.albedo;
        return 0x000000;
    }

    public String getPixelDescription(int u, int v) {
        Vector3 relRay = createRay(u, v);
        RaycastHit hit = traceRay(cameraPos, relRay);
        if (hit != null)
            return hit.objectID + " " + hit.position + ", " + hit.depth + " deep";
        return "(no hit)";
    }

    public Vector3 createRay(int u, int v) {
        float relU =  (u / (width - 1f) - 0.5f) * 2;
        float relV =  - (v / (height - 1f) - 0.5f) * 2;
        float planeHalfWidth = (float) Math.tan(fieldOfView / 2) * rasterPlaneDist;
        float planeHalfHeight = height * planeHalfWidth / width;
        return new Vector3(relU * planeHalfWidth, relV * planeHalfHeight, -rasterPlaneDist).normalized();
    }

    public RaycastHit traceRay(Vector3 origin, Vector3 relRay) {
        RaycastHit closestHit = null;
        for (VisibleObject vo : visibleObjects) {
            RaycastHit latestHit = vo.checkRayCollision(origin, relRay);
            if (latestHit != null && latestHit.depth > 0 && (closestHit == null || latestHit.depth < closestHit.depth)) {
                closestHit = latestHit;
            }
        }
        return closestHit;
    }
}