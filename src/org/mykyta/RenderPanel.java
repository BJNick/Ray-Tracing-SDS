package org.mykyta;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

class RenderPanel extends JPanel {

    private BufferedImage bufferedImage;
    private int W, H;
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

    public void drawView() {
        bufferedImage.setRGB(0, 0, 0xffff00);
        bufferedImage.setRGB(W-1, H-1, 0xff00ff);
        // bufferedImage.setRGB(0,0,2,2, new int[] {0x00ff00, 0xffff00, 0xff0000, 0xff00ff}, 0, 2);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(bufferedImage, transform, null);
    }
}
