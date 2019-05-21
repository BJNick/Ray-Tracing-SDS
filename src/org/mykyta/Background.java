package org.mykyta;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Background implements VisibleObject {

    private Vector3 pos = Vector3.ZERO;
    private float heightSize = 650;

    private BufferedImage texture;

    Background(BufferedImage texture) {
        this.texture = texture;
    }

    public RaycastHit[] checkRayCollision(Vector3 origin, Vector3 relRay) {

        Vector3 flatOrigin = new Vector3(origin.x,0, origin.z);
        Vector3 flatD = new Vector3(relRay.x,0, relRay.z);

        float r = 500;
        Vector3[] collisions = CollisionEquations.checkRaySphereCollision(flatOrigin, flatD, pos, r);

        if (collisions.length == 0)
            return null;

        Vector3 col = collisions[0];

        if (col == null)
            return null;

        Vector3 absCol = new Vector3(col.x, origin.y + relRay.y * col.sub(flatOrigin).signedScale(relRay), col.z);

        float angleZ = (col.sub(pos).x >= 0) ? Vector3.FRONT.angle(col.sub(pos)) : (float) Math.PI * 2 - Vector3.FRONT.angle(col.sub(pos));

        if (Math.abs(absCol.y) > heightSize / 2) {
            //if (absCol.y > 0) {
            absCol = new Vector3(absCol.x, Math.max(Math.min(absCol.y, heightSize / 2), -heightSize / 2), absCol.z);
            /*} else {
                // return null;
                RaycastHit hit = new RaycastHit(col.sub(flatOrigin).signedScale(relRay),
                        absCol,
                        Background.class.getSimpleName() + " at " + angleZ,
                        this,
                        pos.sub(col).normalized(),
                        ObjectMaterial.createGlowing(Illumination.WHITE.applyAlbedo(new Color(BOTTOM_COLOR), 1f), false));
                hit.material.castsShadow = false;
                return hit;
            }*/
        }

        RaycastHit hit = new RaycastHit(col.sub(flatOrigin).signedScale(relRay),
                absCol,
                Background.class.getSimpleName() + " at " + angleZ,
                this,
                pos.sub(col).normalized(),
                ObjectMaterial.createGlowing(Illumination.WHITE.applyAlbedo(new Color(getPixel(angleZ, absCol.y)), 1f), false),
                false);
        hit.material.castsShadow = false;
        return new RaycastHit[]{hit};
    }

    private int getPixel(float angleZ, float height) {

        float fraction = angleZ / ( 2 * (float) Math.PI);
        float v = Math.max(Math.min(-height / heightSize + 0.5f, 1), 0);
        return texture.getRGB(Math.round((texture.getWidth()-1) * fraction), Math.round((texture.getHeight()-1) * v));

    }

}
