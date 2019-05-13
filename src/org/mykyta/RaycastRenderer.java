package org.mykyta;

/*
 *   This class sends a ray for each requested pixel and returns the resultant color. It is essential for the raytracing process.
 */

public class RaycastRenderer {

    private Iterable<VisibleObject> visibleObjects;
    private Iterable<LightSource> lightSources;
    private float fieldOfView;
    public Vector3 cameraPos;
    public float cameraAngle = 0;

    private final float rasterPlaneDist = 1f;
    private final int MaxReflectionDepth = 20;

    public RaycastRenderer(Iterable<VisibleObject> visibleObjects, Iterable<LightSource> lightSources, float fieldOfView) {
        this.visibleObjects = visibleObjects;
        this.lightSources = lightSources;
        this.fieldOfView = fieldOfView;
        cameraPos = new Vector3(0, 0, 10);
    }

    public int getPixel(int u, int v, int width, int height) {
        Vector3 relRay = createRay(u, v, width, height);
        Illumination illum = traceRayIllumination(cameraPos, relRay, 0);
        if (illum != null)
            return illum.toScreenColor();
        return 0x000000;
    }

    public String getPixelDescription(int u, int v, int width, int height) {
        Vector3 relRay = createRay(u, v, width, height);
        RaycastHit hit = traceRayHit(cameraPos, relRay);
        if (hit != null)
            return hit.objectID + " " + hit.position + ", " + hit.depth + " deep";
        return "(no hit)";
    }

    // Create ray from the screen coordinates
    private Vector3 createRay(int u, int v, int width, int height) {
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
            if (vo.checkRayCollision(origin, relRay) != null)
                for (RaycastHit latestHit : vo.checkRayCollision(origin, relRay)) {
                    if (latestHit != null && latestHit.depth > 0 && (closestHit == null || latestHit.depth < closestHit.depth)) {
                        closestHit = latestHit;
                    }
            }
        }

        if (closestHit.material.reflective || closestHit.material.transparent) {
            // return traceRayHit();
            // TODO Reflection & Refraction
        }

        return closestHit;
    }

    private Illumination traceRayIllumination(Vector3 origin, Vector3 relRay, int recursionDepth) {

        if (recursionDepth > MaxReflectionDepth)
            return Illumination.AMBIENT;

        RaycastHit closestHit = null;

        // Find closest object hit
        for (VisibleObject vo : visibleObjects) {
            if (vo.checkRayCollision(origin, relRay) != null)
                for (RaycastHit latestHit : vo.checkRayCollision(origin, relRay)) {
                    if (latestHit != null && latestHit.depth > 0 && (closestHit == null || latestHit.depth < closestHit.depth)) {
                        closestHit = latestHit;
                    }
                }
        }

        Illumination base = Illumination.NO_LIGHT;

        // No object were hit, return darkness
        if (closestHit == null)
            return base;

        // Object is reflective, trace new rays
        if (closestHit.material.reflective && !closestHit.material.transparent) {
            Vector3 incident = relRay.scale(-1);
            float angle = Math.abs(closestHit.normal.angle(incident));

            Vector3 rotAxis = closestHit.normal.cross(incident).normalized();
            Vector3 newDir = closestHit.normal.rotate(-angle, rotAxis);
            base = base.combine(
                    traceRayIllumination(closestHit.position.add(newDir.scale(0.01f)), newDir, recursionDepth + 1)
                            .dim(closestHit.material.reflectiveness)
            );
        }

        if (closestHit.material.transparent) {
            Vector3 incident = relRay.scale(-1);
            float reflectedAngle = Math.abs(closestHit.normal.angle(incident));

            if (Float.isNaN(reflectedAngle))
                return Illumination.ERROR;

            float refractedAngle = (float) Math.asin(Math.sin(reflectedAngle) * (!closestHit.inside ? 1f / closestHit.material.refractionIndex : closestHit.material.refractionIndex));

            Vector3 rotAxis = closestHit.normal.cross(incident).normalized();

            Vector3 reflectedDir = closestHit.normal.rotate(-reflectedAngle, rotAxis);
            Vector3 refractedDir = closestHit.normal.scale(-1).rotate(refractedAngle, rotAxis);

            float partialReflection = Illumination.getPartialReflection(reflectedAngle, refractedAngle);
            // System.out.println(reflectedAngle + " " + refractedAngle + " " + partialReflection);

            base = base.combine(
                    traceRayIllumination(closestHit.position.add(reflectedDir.scale(0.01f)), reflectedDir, recursionDepth + 1)
                            .dim(closestHit.material.transparency).dim(partialReflection)
            );
            base = base.combine(
                    traceRayIllumination(closestHit.position.add(refractedDir.scale(0.01f)), refractedDir, recursionDepth + 1)
                            .dim(closestHit.material.transparency).dim(1f - partialReflection).applyAlbedo(closestHit.material.albedo, 1f)
            );
            if (closestHit.material.transparency < 1f) {
                base = base.combine(getIlluminationAt(closestHit).applyAlbedo(closestHit.material.albedo, 1f - closestHit.material.transparency));
            }
        }

        if (closestHit.material.opaque) {
            base = base.combine(getIlluminationAt(closestHit).applyAlbedo(closestHit.material.albedo, closestHit.material.diffusionRate));
        }

        if (closestHit.material.glows) {
            base = base.combine(closestHit.material.illumination);
        }

        return base;
    }

    private Illumination getIlluminationAt(RaycastHit point) {
        Illumination base = Illumination.AMBIENT;
        for (LightSource ls : lightSources) {
            if (checkVisibility(ls.point, point.position, point.material.castsShadow ? null : point.object)) {
                // TODO surface illumination
                base = base.combine(
                        ls.getIlluminationAt(point.position).applyAngle(point.normal.angle(ls.point.sub(point.position)))
                );
            }
        }
        return base;
    }

    private boolean checkVisibility(Vector3 origin, Vector3 endpoint, VisibleObject exception) {
        Vector3 dir = endpoint.sub(origin).normalized();
        for (VisibleObject vo : visibleObjects) {
            if (vo.checkRayCollision(origin, dir) != null)
                for (RaycastHit latestHit : vo.checkRayCollision(origin, dir)) {
                    if (latestHit != null && (exception == null || latestHit.object != exception) && latestHit.depth > 0 && latestHit.depth + 0.001f < endpoint.sub(origin).mag()) {
                        if (!latestHit.material.transparent)
                            return false;
                    }
            }
        }
        return true;
    }
}