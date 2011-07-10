package org.clustering.util;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

// Instantiate this class and then use the draw() method to draw the
// generated on the graphics context.
public class Mandelbrot {
    // Holds the generated image
    Image image;

    // 16-color model
    ColorModel colorModel = generateColorModel();

    public Mandelbrot(int width, int height) {
        // Initialize with default location
        this(width, height, new Rectangle2D.Float(-2.0f, -1.2f, 3.2f, 2.4f));
    }

    public Mandelbrot(int width, int height, Rectangle2D.Float loc) {
        image = Toolkit.getDefaultToolkit().createImage(
            new MemoryImageSource(width, height,
            colorModel, generatePixels(width, height, loc), 0, width));
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x, y, null);
    }

    private byte[] generatePixels(int w, int h, Rectangle2D.Float loc) {
        float xmin = loc.x;
        float ymin = loc.y;
        float xmax = loc.x+loc.width;
        float ymax = loc.y+loc.height;

        byte[] pixels = new byte[w * h];
        int pIx = 0;
        float[] p = new float[w];
        float q = ymin;
        float dp = (xmax-xmin)/w;
        float dq = (ymax-ymin)/h;

        p[0] = xmin;
        for (int i=1; i<w; i++) {
            p[i] = p[i-1] + dp;
        }

        for (int r=0; r<h; r++) {
            for (int c=0; c<w; c++) {
                int color = 1;
                float x = 0.0f;
                float y = 0.0f;
                float xsqr = 0.0f;
                float ysqr = 0.0f;
                do {
                    xsqr = x*x;
                    ysqr = y*y;
                    y = 2*x*y + q;
                    x = xsqr - ysqr + p[c];
                    color++;
                } while (color < 512 && xsqr + ysqr < 4);
                pixels[pIx++] = (byte)(color % 16);
            }
            q += dq;
        }
        return pixels;
    }

    private static ColorModel generateColorModel() {
        // Generate 16-color model
        byte[] r = new byte[16];
        byte[] g = new byte[16];
        byte[] b = new byte[16];

        r[0] = 0; g[0] = 0; b[0] = 0;
        r[1] = 0; g[1] = 0; b[1] = (byte)192;
        r[2] = 0; g[2] = 0; b[2] = (byte)255;
        r[3] = 0; g[3] = (byte)192; b[3] = 0;
        r[4] = 0; g[4] = (byte)255; b[4] = 0;
        r[5] = 0; g[5] = (byte)192; b[5] = (byte)192;
        r[6] = 0; g[6] = (byte)255; b[6] = (byte)255;
        r[7] = (byte)192; g[7] = 0; b[7] = 0;
        r[8] = (byte)255; g[8] = 0; b[8] = 0;
        r[9] = (byte)192; g[9] = 0; b[9] = (byte)192;
        r[10] = (byte)255; g[10] = 0; b[10] = (byte)255;
        r[11] = (byte)192; g[11] = (byte)192; b[11] = 0;
        r[12] = (byte)255; g[12] = (byte)255; b[12] = 0;
        r[13] = (byte)80; g[13] = (byte)80; b[13] = (byte)80;
        r[14] = (byte)192; g[14] = (byte)192; b[14] = (byte)192;
        r[15] = (byte)255; g[15] = (byte)255; b[15] = (byte)255;

        return new IndexColorModel(4, 16, r, g, b);
    }
}