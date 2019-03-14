package org.mykyta;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/*
 * Separate from RaycastRenderer, this class makes sure to fit the image in a window on the screen.
 */

class RenderPanel extends JPanel {

    private BufferedImage bufferedImage;
    public final int W, H;
    private AffineTransform transform;

    public RenderPanel(int width, int height, int scale) {
        super();
        bufferedImage = new BufferedImage(width / scale, height / scale, BufferedImage.TYPE_INT_RGB);
        W = width / scale;
        H = height / scale;
        this.setSize(W * scale, H * scale);
        this.setPreferredSize(this.getSize());
        transform =  AffineTransform.getScaleInstance(scale, scale);
    }

    public void drawView(RaycastRenderer renderer) {
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                bufferedImage.setRGB(i, j, renderer.getPixel(i, j));
                repaint();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(bufferedImage, transform, null);
    }
}
