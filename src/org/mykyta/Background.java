package org.mykyta;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Background implements VisibleObject {

    private float R = 500;
    private Vector3 pos = Vector3.ZERO;
    private float heightSize = 700;

    private BufferedImage texture;

    public Background() {
        texture = new BufferedImage(1558, 300,
                BufferedImage.TYPE_INT_RGB);

        // Reading input file
        try {
            texture = ImageIO.read(this.getClass().getResource("/images/panorama.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RaycastHit checkRayCollision(Vector3 origin, Vector3 relRay) {

        Vector3 flatOrigin = new Vector3(origin.x,0, origin.z);
        Vector3 flatD = new Vector3(relRay.x,0, relRay.z);

        Vector3 col = CollisionEquations.checkRaySphereCollision(flatOrigin, flatD, pos, R, true);

        if (col == null)
            return null;

        Vector3 absCol = new Vector3(col.x, origin.y + relRay.y * col.sub(flatOrigin).signedScale(relRay), col.z);

        float angleZ = (col.sub(pos).x >= 0) ? Vector3.FRONT.angle(col.sub(pos)) : (float) Math.PI * 2 - Vector3.FRONT.angle(col.sub(pos));

        if (Math.abs(absCol.y) > heightSize / 2)
            return null;

        return new RaycastHit(col.sub(flatOrigin).signedScale(relRay),
                absCol,
                Background.class.getSimpleName() + " at " + angleZ,
                this,
                pos.sub(col).normalized(),
                new Color(getPixel(angleZ, absCol.y)));
    }

    private int getPixel(float angleZ, float height) {

        float fraction = angleZ / ( 2 * (float) Math.PI);
        float v = Math.max(Math.min(-height / heightSize + 0.5f, 1), 0);
        return texture.getRGB(Math.round((texture.getWidth()-1) * fraction), Math.round((texture.getHeight()-1) * v));

    }

}
