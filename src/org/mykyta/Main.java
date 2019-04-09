package org.mykyta;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        RenderPanel render = new RenderPanel(400, 400, 4);
        frame.add(render);
        frame.setTitle("SDS Ray Tracing Demo by Mykyta");
        frame.pack();
        frame.setVisible(true);

        ArrayList<VisibleObject> objects = new ArrayList<>();
        RaycastRenderer raycast = new RaycastRenderer(objects, render.W, render.H, (float) Math.PI / 4);

        objects.add(new Background());

        objects.add(new SphericalObject(new Vector3(-2, -1, 0), 1f, 0x3fc9fc));
        objects.add(new SphericalObject(new Vector3(5, 0, 0), 4f, 0xf73838));
        objects.add(new SphericalObject(new Vector3(0, 2, -10), 4f, 0x00ff00));

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == ' ') {
                    render.drawView(raycast);
                }
                else if(e.getKeyChar() == 'w') {
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(0, 0, -1).rotatedY(raycast.cameraAngle));
                    render.drawView(raycast);
                }
                else if(e.getKeyChar() == 's') {
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(0, 0, 1).rotatedY(raycast.cameraAngle));
                    render.drawView(raycast);
                }
                else if(e.getKeyChar() == 'a') {
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(-1, 0, 0).rotatedY(raycast.cameraAngle));
                    render.drawView(raycast);
                }
                else if(e.getKeyChar() == 'd') {
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(1, 0, 0).rotatedY(raycast.cameraAngle));
                    render.drawView(raycast);
                }
                else if(e.getKeyChar() == 'e') {
                    raycast.cameraAngle += 0.1;
                    render.drawView(raycast);
                }
                else if(e.getKeyChar() == 'q') {
                    raycast.cameraAngle += -0.1;
                    render.drawView(raycast);
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        render.drawView(raycast);
    }
}
