package org.mykyta;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static boolean saveFrames = false;
    private static int frameCount = 0;
    private static Iterable<VisibleObject> currentScene;
    private static long lastKeyPress = -1000;
    private static boolean helpVisible = false;

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        RenderPanel render = new RenderPanel(800, 800, 1, false);
        frame.add(render);
        frame.setTitle("SDS Ray Tracing Demo by Mykyta - Help [F1]");
        frame.pack();
        frame.setLayout(null);
        frame.setFocusTraversalKeysEnabled(false);
        render.setFocusTraversalKeysEnabled(false);

        JLabel instructions = new JLabel(loadText("/explanation.html"));
        frame.add(instructions);
        instructions.setLocation(820, 5);
        instructions.setSize(200, 700);

        ArrayList<LightSource> sources = new ArrayList<>();

        ArrayList<VisibleObject> objectsA = new ArrayList<>();
        ArrayList<VisibleObject> objectsB = new ArrayList<>();
        currentScene = objectsA;

        RaycastRenderer raycast = new RaycastRenderer(currentScene, sources, (float) Math.PI / 4);

        objectsA.add(new Background(loadImage("/images/panoramaHD.jpg")));

        objectsA.add(new SphericalObject(new Vector3(-2, -3, 0), 1f, 0x3fc9fc, 0.3f));
        objectsA.add(new SphericalObject(new Vector3(5, 0, 0), 4f, 0xf73838, 0.3f));
        objectsA.add(new SphericalObject(new Vector3(0, 0, -10), 4f, 0x28ed28, 0.3f));
        objectsA.add(new SphericalObject(new Vector3(0, -3.5f, 0), 0.5f, 0xf4ee42, 0.3f));

        objectsA.add(new SphericalMarble(new Vector3(15, -3, 3), 1f, 1.05f));
        objectsA.add(new SphericalMarble(new Vector3(15, -3, 0), 1f, 1.10f));
        objectsA.add(new SphericalMarble(new Vector3(15, -3, -3), 1f, 1.33f));
        objectsA.add(new SphericalMarble(new Vector3(15, -3, -6), 1f, 1.52f));
        objectsA.add(new SphericalMarble(new Vector3(15, -3, -9), 1f, 2.54f));
        objectsA.add(new SphericalMarble(new Vector3(-10, 0, 15f), 2.5f, 1.5163f));

        objectsA.add(new HorizontalPlanarObject(new Vector3(8, -4, -5), 0.00846668f, 0, loadImage("/images/testing-page.jpg")));

        objectsB.add(new PlanarObject(new Vector3(0, -0.6f, -10), 0.00846668f, 0, loadImage("/images/testing-page.jpg")));
        objectsB.add(new SphericalMarble(new Vector3(0, 0, -2.5f), 2.5f, 1.5163f));

        // objects.add(new PlanarObject(new Vector3(0, -0.6f, -10), 20, 10, 0.1f));
        /*objects.add(new SphericalMarble(new Vector3(0, 0, 0), 1f, 1.52f, 0xFF00FF, 0.9f));
        objects.add(new SphericalMarble(new Vector3(0, 0, -3), 1f, 1.52f, 0x00FFFF, 0.9f));
        objects.add(new SphericalMarble(new Vector3(0, 0, -6), 1f, 1.52f, 0xFFFF00, 0.9f));*/

        LightSource source = new LightSource(Illumination.WHITE, new Vector3(0, 0, 5));
        sources.add(source);
        // objects.add(source);

        render.drawViewAA(raycast, 10, false);
        // render.drawView(raycast, 1, true);

        frame.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}

            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == '`') {
                    saveFrames = !saveFrames;
                    System.out.println("Save frames: " + saveFrames);
                    return;
                }

                // Movement controls
                if (e.getKeyChar() == 'w' || e.getKeyCode() == KeyEvent.VK_UP)
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(0, 0, -1).rotatedY(raycast.cameraAngle));
                else if (e.getKeyChar() == 's' || e.getKeyCode() == KeyEvent.VK_DOWN)
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(0, 0, 1).rotatedY(raycast.cameraAngle));
                else if (e.getKeyChar() == 'a' || e.getKeyCode() == KeyEvent.VK_LEFT)
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(-1, 0, 0).rotatedY(raycast.cameraAngle));
                else if (e.getKeyChar() == 'd' || e.getKeyCode() == KeyEvent.VK_RIGHT)
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(1, 0, 0).rotatedY(raycast.cameraAngle));
                else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(0, 1, 0));
                else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
                    raycast.cameraPos = raycast.cameraPos.add(new Vector3(0, -1, 0));
                else if (e.getKeyChar() == 'e')
                    raycast.cameraAngle += 0.1f;
                else if (e.getKeyChar() == 'q')
                    raycast.cameraAngle += -0.1f;
                else if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    currentScene = currentScene == objectsA ? objectsB : objectsA;
                    raycast.setVisibleObjects(currentScene, sources);
                    raycast.cameraAngle = 0;
                    raycast.setFieldOfView((float) Math.PI / 4);
                    raycast.cameraPos = new Vector3(0, 0, 10);
                }

                // Global controls
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    render.drawView(raycast, 1, true);
                    if (saveFrames)
                        saveToFile(render);
                } else if (e.getKeyCode() == KeyEvent.VK_F2) {
                    saveToFile(render);
                } else if (e.getKeyCode() == KeyEvent.VK_F1) {
                    helpVisible = !helpVisible;
                    frame.setSize(frame.getWidth() + (helpVisible ? 260 : -260), frame.getHeight());
                } else {
                    if (System.nanoTime() - lastKeyPress < 50000000)
                        render.drawViewAA(raycast, 20, false);
                    else
                        render.drawViewAA(raycast, 10, false);
                    lastKeyPress = System.nanoTime();
                }
            }

            public void keyReleased(KeyEvent e) {}
        });

        frame.addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            if (raycast.getFieldOfView() * (float) Math.pow(1.05, notches) < Math.PI)
                raycast.setFieldOfView(raycast.getFieldOfView() * (float) Math.pow(1.05, notches));
            if (System.nanoTime() - lastKeyPress < 50000000)
                render.drawViewAA(raycast, 20, false);
            else
                render.drawViewAA(raycast, 10, false);
            lastKeyPress = System.nanoTime();
        });

        frame.setVisible(true);
    }

    private static void saveToFile(RenderPanel panel) {
        boolean folderCreated = false;
        try {
            File outputFolder = new File("generated");
            folderCreated = outputFolder.mkdir();
            File outputFile = new File("generated/saved" + (frameCount / 100) + "" + (frameCount / 10 % 10) + "" + (frameCount % 10) + ".png");
            ImageIO.write(panel.bufferedImage, "png", outputFile);
        } catch (IOException exception) {
            System.out.println("Folder has been created: " + folderCreated);
            exception.printStackTrace();
        } finally {
            frameCount++;
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

    private static String loadText(String filename) {

        String text = "";

        try {
            InputStream stream = Main.class.getResourceAsStream(filename);
            if (stream != null)
                text = new Scanner(stream).useDelimiter("\\Z").next();
            else
                throw new IOException("Resource file " + filename + " not found");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

}
