package org.mykyta;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Background implements VisibleObject {

    private float R = 50;
    private Vector3 pos = Vector3.ZERO;

    BufferedImage texture = null;

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

        Vector3 absCol = new Vector3(col.x, origin.y + relRay.y * col.sub(flatOrigin).mag(), col.z);

        float angleZ = Vector3.FRONT.angle(col.sub(pos)) * ((col.sub(pos).x < 0) ? -1 : 1);

        return new RaycastHit(col.sub(flatOrigin).signedScale(relRay),
                absCol,
                Background.class.getSimpleName() + " at " + angleZ,
                pos.sub(col).normalized(),
                new Color(0, 0,0.5f + Math.min(Math.max(1 * angleZ, -0.5f), 0.5f)).getRGB());
    }

}
