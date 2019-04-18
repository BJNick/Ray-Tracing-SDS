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

    private boolean printRenderDuration = true;

    private int aa2 = 1;

    public RenderPanel(int width, int height, int scale, boolean antialiasing) {
        super();
        // The size of everything is cut in half in order to enable anti-aliasing
        aa2 = antialiasing ? 2 : 1;
        bufferedImage = new BufferedImage(width * aa2 / scale, height * aa2 / scale, BufferedImage.TYPE_INT_RGB);
        W = width * aa2 / scale;
        H = height * aa2 / scale;
        this.setSize(W * scale / aa2, H * scale / aa2);
        this.setPreferredSize(this.getSize());
        transform =  AffineTransform.getScaleInstance(scale / (float) aa2, scale / (float) aa2);
        this.addMouseListener(this);
    }

    public void drawViewAA(RaycastRenderer renderer, int sc, boolean enhance) {
        drawView(renderer, sc * aa2, enhance);
    }

    public void drawView(RaycastRenderer renderer, int sc, boolean enhance) {
        this.usedRenderer = renderer;
        Graphics2D bImgGraphics = bufferedImage.createGraphics();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < W/sc; i++) {
            for (int j = 0; j < H/sc; j++) {
                int rgb = renderer.getPixel(i, j, W/sc, H/sc);
                bImgGraphics.setPaint(new Color (rgb));
                bImgGraphics.fillRect(i*sc, j*sc, sc, sc);
            }
            if (enhance)
                paintImmediately(0, 0, W, H);
        }
        repaint(0);
        long endTime = System.currentTimeMillis();
        if (printRenderDuration)
            System.out.println("Rendered in " + (endTime - startTime) + "ms");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(bufferedImage, transform, null);
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println(e.getX() * W / getWidth() + " " + e.getY() * H / getHeight());
        System.out.println(usedRenderer.getPixelDescription(e.getX() * W / getWidth(), e.getY() * H / getHeight(), W, H));
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
