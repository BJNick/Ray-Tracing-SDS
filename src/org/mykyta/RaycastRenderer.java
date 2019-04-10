package org.mykyta;

/*
 *   This class sends a ray for each requested pixel and returns the resultant color. It is essential for the raytracing process.
 */

public class RaycastRenderer {

    private Iterable<VisibleObject> visibleObjects;
    private Iterable<LightSource> lightSources;
    private int width, height;
    private float fieldOfView;
    public Vector3 cameraPos;
    public float cameraAngle = 0;

    private final float rasterPlaneDist = 1f;

    public RaycastRenderer(Iterable<VisibleObject> visibleObjects, Iterable<LightSource> lightSources, int width, int height, float fieldOfView) {
        this.visibleObjects = visibleObjects;
        this.lightSources = lightSources;
        this.width = width;
        this.height = height;
        this.fieldOfView = fieldOfView;
        cameraPos = new Vector3(0, 0, 10);
    }

    public int getPixel(int u, int v) {
        Vector3 relRay = createRay(u, v);
        Illumination illum = traceRayIllumination(cameraPos, relRay);
        if (illum != null)
            return illum.toScreenColor();
        return 0x000000;
    }

    public String getPixelDescription(int u, int v) {
        Vector3 relRay = createRay(u, v);
        RaycastHit hit = traceRayHit(cameraPos, relRay);
        if (hit != null)
            return hit.objectID + " " + hit.position + ", " + hit.depth + " deep";
        return "(no hit)";
    }

    private Vector3 createRay(int u, int v) {
        float relU =  (u / (width - 1f) - 0.5f) * 2;
        float relV =  - (v / (height - 1f) - 0.5f) * 2;
        float planeHalfWidth = (float) Math.tan(fieldOfView / 2) * rasterPlaneDist;
        float planeHalfHeight = height * planeHalfWidth / width;
        Vector3 ret = new Vector3(relU * planeHalfWidth, relV * planeHalfHeight, -rasterPlaneDist).normalized();
        if (cameraAngle == 0)
            return ret;
        else {
            ret = ret.rotatedY(cameraAngle);
            return ret;
        }
    }

    private RaycastHit traceRayHit(Vector3 origin, Vector3 relRay) {
        RaycastHit closestHit = null;

        for (VisibleObject vo : visibleObjects) {
            RaycastHit latestHit = vo.checkRayCollision(origin, relRay);
            if (latestHit != null && latestHit.depth > 0 && (closestHit == null || latestHit.depth < closestHit.depth)) {
                closestHit = latestHit;
            }
        }

        if (closestHit.reflective || closestHit.transparent) {
            // return traceRayHit();
            // TODO Reflection & Refraction
        }

        return closestHit;
    }

    private Illumination getIllumination(RaycastHit point) {
        Illumination base = Illumination.AMBIENT;
        for (LightSource ls : lightSources) {
            if (checkVisibility(ls.point, point.position, point.castsShadow ? null : point.object)) {
                // TODO surface illumination
                base = base.combine(ls.getIlluminationAt(point.position));
            }
        }
        return base;
    }

    private boolean checkVisibility(Vector3 origin, Vector3 endpoint, VisibleObject exception) {
        Vector3 dir = endpoint.sub(origin).normalized();
        for (VisibleObject vo : visibleObjects) {
            RaycastHit latestHit = vo.checkRayCollision(origin, dir);
            if (latestHit != null && (exception == null || latestHit.object != exception) && latestHit.depth > 0 && latestHit.depth + 0.001f < endpoint.sub(origin).mag()) {
                if (!latestHit.transparent)
                    return false;
            }
        }
        return true;
    }

    private Illumination traceRayIllumination(Vector3 origin, Vector3 relRay) {
        RaycastHit closestHit = null;

        // Find closest object hit
        for (VisibleObject vo : visibleObjects) {
            RaycastHit latestHit = vo.checkRayCollision(origin, relRay);
            if (latestHit != null && latestHit.depth > 0 && (closestHit == null || latestHit.depth < closestHit.depth)) {
                closestHit = latestHit;
            }
        }

        Illumination base = Illumination.NO_LIGHT;

        // No object were hit, return darkness
        if (closestHit == null)
            return base;

        // Object is reflective, trace new rays
        if (closestHit.reflective || closestHit.transparent) {
            // TODO Reflection & Refraction
            // base = base.combine(traceRayIllumination());
        }

        // Object is opaque
        else if (closestHit.opaque) {
            return base.combine(getIllumination(closestHit)).applyAlbedo(closestHit.albedo, closestHit.diffusionRate);
        }

        return base;
    }
}