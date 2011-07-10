package org.clustering.util;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

class RunMandelbrot {
    static public void main(String[] args) {
        new RunMandelbrot();
    }
    RunMandelbrot() {
        Frame frame = new Frame("Mandelbrot Set");
        frame.add(new MyCanvas());
        frame.setSize(300, 200);
        frame.setVisible(true);
    }

    class MyCanvas extends Canvas {
        Mandelbrot mandelbrot;

        MyCanvas() {
            // Add a listener for resize events
            addComponentListener(new ComponentAdapter() {
                // This method is called when the component's size changes
                public void componentResized(ComponentEvent evt) {
                    Component c = (Component)evt.getSource();

                    // Get new size
                    Dimension newSize = c.getSize();

                    // Regenerate the image
                    mandelbrot = new Mandelbrot(newSize.width, newSize.height);
                    c.repaint();
                }
            });
        }

        public void paint(Graphics g) {
            if (mandelbrot != null) {
                mandelbrot.draw(g, 0, 0);
            }
        }
    }
}