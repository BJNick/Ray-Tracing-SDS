package org.mykyta;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    static boolean saveFrames = false;
    static int frameCount = 0;

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        RenderPanel render = new RenderPanel(800, 800, 1, false);
        frame.add(render);
        frame.setTitle("SDS Ray Tracing Demo by Mykyta");
        frame.pack();
        frame.setVisible(true);

        ArrayList<VisibleObject> objects = new ArrayList<>();
        ArrayList<LightSource> sources = new ArrayList<>();

        RaycastRenderer raycast = new RaycastRenderer(objects, sources, (float) Math.PI / 4);

        objects.add(new Background());
        objects.add(new SphericalObject(new Vector3(-2, -1, 0), 1f, 0x3fc9fc, true));
        objects.add(new SphericalObject(new Vector3(5, 0, 0), 4f, 0xf73838, true));
        objects.add(new SphericalObject(new Vector3(0, 2, -10), 4f, 0x28ed28, true));

        objects.add(new SphericalMarble(new Vector3(0, 0, 5), 1f));

        LightSource source = new LightSource(Illumination.WHITE, new Vector3(-10, 0, 0));
        sources.add(source);
        objects.add(source);

        render.drawViewAA(raycast, 10, false);
        // render.drawView(raycast, 1, true);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == '`') {
                    saveFrames = !saveFrames;
                    System.out.println("Save frames: " + saveFrames);
                    return;
                }
                if(e.getKeyChar() == 'w') {
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(0, 0, -1).rotatedY(raycast.cameraAngle));
                }
                else if(e.getKeyChar() == 's') {
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(0, 0, 1).rotatedY(raycast.cameraAngle));
                }
                else if(e.getKeyChar() == 'a') {
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(-1, 0, 0).rotatedY(raycast.cameraAngle));
                }
                else if(e.getKeyChar() == 'd') {
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(1, 0, 0).rotatedY(raycast.cameraAngle));
                }
                else if(e.getKeyChar() == 'e') {
                    raycast.cameraAngle += 0.1;
                }
                else if(e.getKeyChar() == 'q') {
                    raycast.cameraAngle += -0.1;
                }
                if(e.getKeyChar() == ' ') {
                    render.drawView(raycast, 1, true);
                    if (saveFrames)
                        saveToFile(render);
                } else {
                    render.drawViewAA(raycast, 10, false);
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

    private static void saveToFile(RenderPanel panel) {
        if(saveFrames) {
            try {
                File outputFile = new File("generated/saved" + (frameCount/100) + "" + (frameCount/10%10) + "" + (frameCount%10) + ".png");
                ImageIO.write(panel.bufferedImage, "png", outputFile);
            } catch (IOException exception) {
            } finally {
                frameCount++;
            }
        }
    }

}
