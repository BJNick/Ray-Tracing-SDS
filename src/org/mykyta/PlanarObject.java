package org.mykyta;

import java.awt.image.BufferedImage;

// A plane perpendicular to the z-direction (Vector3.FRONT)
public class PlanarObject implements VisibleObject {

    Vector3 position;
    float width, height, margin;
    private float scale;
    private BufferedImage texture;

    private PlanarObject(Vector3 position, float width, float height, float margin) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.margin = margin;
        texture = null;
    }

    PlanarObject(Vector3 position, float scale, int margin, BufferedImage image) {
        this(position, image.getWidth() * scale, image.getHeight() * scale, margin * scale);
        this.scale = scale;
        texture = image;
    }

    ObjectMaterial getPixel(float posX, float posY) {
        if (Math.abs(posX) > width / 2 || Math.abs(posY) > height / 2)
            return ObjectMaterial.createOpaque(0xFFFFFF, 0.7f);

        if (texture == null)
            return ObjectMaterial.createMirror(1f);

        return ObjectMaterial.createOpaque(
                texture.getRGB((int) Math.min(Math.floor((posX + width / 2) / scale), texture.getWidth() - 1),
                        (int) Math.min(Math.floor((-posY + height / 2) / scale), texture.getHeight() - 1)),
                1f);
    }

    public RaycastHit[] checkRayCollision(Vector3 origin, Vector3 relRay) {

        float displacementZ = position.z - origin.z;
        Vector3 hitPoint = origin.add(relRay.scale(displacementZ / relRay.z));

        if (displacementZ / relRay.z < 0 || Math.abs(hitPoint.x - position.x) > width / 2 + margin || Math.abs(hitPoint.y - position.y) > height / 2 + margin)
            return new RaycastHit[0];

        return new RaycastHit[]{
                new RaycastHit(displacementZ / relRay.z,
                        hitPoint,
                        PlanarObject.class.getSimpleName(),
                        this,
                        Vector3.FRONT.scale(-displacementZ / relRay.z).normalized(),
                        getPixel(hitPoint.x - position.x, hitPoint.y - position.y),
                        false)
        };
    }
}
