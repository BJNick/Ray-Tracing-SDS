package org.mykyta;

import java.awt.*;

public class SphericalMarble extends SphericalObject {

    public SphericalMarble(Vector3 position, float radius, float refractionIndex) {
        super(position, radius);
        this.material = ObjectMaterial.createTransparent(refractionIndex, 1f);
        name = SphericalMarble.class.getSimpleName();
        this.material.castsShadow = true;
        this.material.shadowIntencity = 0.1f;
    }

    public SphericalMarble(Vector3 position, float radius, float refractionIndex, int color, float transparency) {
        this(position, radius, refractionIndex);
        this.material.transparency = transparency;
        this.material.albedo = new Color(color);
    }

}
