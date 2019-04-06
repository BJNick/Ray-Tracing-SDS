package org.mykyta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/*
 * Separate from RaycastRenderer, this class makes sure to fit the image in a window on the screen.
 */

class RenderPanel extends JPanel implements MouseListener {

    private BufferedImage bufferedImage;
    public final int W, H;
    private AffineTransform transform;
    private RaycastRenderer usedRenderer;

    public boolean printRenderDuration = true;

    public RenderPanel(int width, int height, int scale) {
        super();
        bufferedImage = new BufferedImage(width / scale, height / scale, BufferedImage.TYPE_INT_RGB);
        W = width / scale;
        H = height / scale;
        this.setSize(W * scale, H * scale);
        this.setPreferredSize(this.getSize());
        transform =  AffineTransform.getScaleInstance(scale, scale);
        this.addMouseListener(this);
    }

    public void drawView(RaycastRenderer renderer) {
        this.usedRenderer = renderer;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                bufferedImage.setRGB(i, j, renderer.getPixel(i, j));
                repaint();
            }
        }
        long endTime = System.currentTimeMillis();
        if (printRenderDuration)
            System.out.println("Rendered in " + (endTime - startTime) + "ms");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(bufferedImage, transform, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e.getX() * W / getWidth() + " " + e.getY() * H / getHeight());
        System.out.println(usedRenderer.getPixelDescription(e.getX() * W / getWidth(), e.getY() * H / getHeight()));
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
