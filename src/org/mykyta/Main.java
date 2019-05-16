package org.mykyta;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
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

        objects.add(new Background(loadImage("/images/panoramaHD.jpg")));

        /*objects.add(new SphericalObject(new Vector3(-2, -1, 0), 1f, 0x3fc9fc, 0.3f));
        objects.add(new SphericalObject(new Vector3(5, 0, 0), 4f, 0xf73838, 0.3f));
        objects.add(new SphericalObject(new Vector3(0, 2, -10), 4f, 0x28ed28, 0.3f));*/

        /*objects.add(new SphericalMarble(new Vector3(15, 0, 6), 1f, 1.05f));
        objects.add(new SphericalMarble(new Vector3(15, 0, 3), 1f, 1.10f));
        objects.add(new SphericalMarble(new Vector3(15, 0, 0), 1f, 1.33f));
        objects.add(new SphericalMarble(new Vector3(15, 0, -3), 1f, 1.52f));
        objects.add(new SphericalMarble(new Vector3(15, 0, -6), 1f, 2.54f));*/

        //objects.add(new PlanarObject(new Vector3(0, -0.6f, -10), 0.00846668f, 0, loadImage("/images/testing-page.jpg")));
        objects.add(new SphericalMarble(new Vector3(0, 0, -3f), 2f, 1.5163f));

        /*objects.add(new SphericalMarble(new Vector3(0, 0, 0), 1f, 1.52f, 0xFF00FF, 0.9f));
        objects.add(new SphericalMarble(new Vector3(0, 0, -3), 1f, 1.52f, 0x00FFFF, 0.9f));
        objects.add(new SphericalMarble(new Vector3(0, 0, -6), 1f, 1.52f, 0xFFFF00, 0.9f));*/

        //LightSource source = new LightSource(Illumination.WHITE, new Vector3(-5, 0, 0));
        //sources.add(source);
        // objects.add(source);

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

    private static BufferedImage loadImage(String filename) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(Main.class.getResource(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

}
