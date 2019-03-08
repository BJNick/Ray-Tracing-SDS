package org.mykyta;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        RenderPanel render = new RenderPanel(400, 400, 20);
        frame.add(render);
        frame.pack();
        frame.setVisible(true);

        render.drawView();
    }
}

class RenderPanel extends JPanel {

    private BufferedImage bufferedImage;
    private int W, H;
    private AffineTransform transform;

    public RenderPanel(int width, int height, int scale) {
        super();
        this.setSize(width, height);
        this.setPreferredSize(this.getSize());
        bufferedImage = new BufferedImage(width / scale, height / scale, BufferedImage.TYPE_INT_RGB);
        W = width / scale;
        H = height / scale;
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
