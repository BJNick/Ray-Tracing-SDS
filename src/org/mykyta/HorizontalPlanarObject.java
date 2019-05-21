package org.mykyta;

import java.awt.image.BufferedImage;

public class HorizontalPlanarObject extends PlanarObject {

    HorizontalPlanarObject(Vector3 position, float scale, int margin, BufferedImage image) {
        super(position, scale, margin, image);
    }

    @Override
    public RaycastHit[] checkRayCollision(Vector3 origin, Vector3 relRay) {
        float displacementY = position.y - origin.y;
        Vector3 hitPoint = origin.add(relRay.scale(displacementY / relRay.y));

        if (displacementY / relRay.y < 0 || Math.abs(hitPoint.x - position.x) > width / 2 + margin || Math.abs(hitPoint.z - position.z) > height / 2 + margin)
            return new RaycastHit[0];

        return new RaycastHit[]{
                new RaycastHit(displacementY / relRay.y,
                        hitPoint,
                        HorizontalPlanarObject.class.getSimpleName(),
                        this,
                        Vector3.UP.scale(displacementY / relRay.y).normalized(),
                        getPixel(hitPoint.x - position.x, position.z - hitPoint.z),
                        false)
        };
    }
}
