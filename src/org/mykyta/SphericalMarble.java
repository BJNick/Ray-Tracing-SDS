package org.mykyta;

public class SphericalMarble extends SphericalObject {

    public SphericalMarble(Vector3 position, float radius) {
        super(position, radius);
        this.material = ObjectMaterial.createTransparent(1.2f);
    }

}
