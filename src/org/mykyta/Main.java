package org.mykyta;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        RenderPanel render = new RenderPanel(400, 400, 5);
        frame.add(render);
        frame.pack();
        frame.setVisible(true);

        ArrayList<VisibleObject> objects = new ArrayList<>();
        RaycastRenderer raycast = new RaycastRenderer(objects, render.W, render.H, (float) Math.PI / 2);

        // Test
        /*objects.add(new VisibleObject() {
            @Override
            public RaycastHit checkRayCollision(Vector3 relRay, Vector3 origin) {
                float c = (0 - origin.z) / relRay.z;
                if (origin.add(relRay.scale(c)).y > 0 && origin.add(relRay.scale(c)).y < 1)
                    return new RaycastHit(relRay.scale(c).mag(),
                            origin.add(relRay.scale(c)),
                            new Vector3(0, 0, 1),
                            0x00FF00);
            }
        });*/

        objects.add(new SphericalObject(new Vector3(0, 0, 0), 1f, 0x3fc9fc));
        // objects.add(new SphericalObject(new Vector3(0, 0, -10), 5f, 0xf73838));

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == ' ') {
                    render.drawView(raycast);
                }
                if(e.getKeyChar() == 'w') {
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(0, 0, -1));
                    render.drawView(raycast);
                }
                if(e.getKeyChar() == 's') {
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(0, 0, 1));
                    render.drawView(raycast);
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        render.drawView(raycast);
    }
}
