package cn.edu.zju.db.datagen.material.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

class PaintAnyTime {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PaintAnyTime();
            }
        });
    }

    final BufferedImage image = (
        new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB)
    );

    final JFrame frame = new JFrame();

    final JLabel label = new JLabel(new ImageIcon(image));

    final MouseAdapter drawer = new MouseAdapter() {
        Graphics2D g2D;

        @Override
        public void mousePressed(MouseEvent me) {
            g2D = image.createGraphics();
            g2D.setColor(Color.BLACK);
        }

        @Override
        public void mouseDragged(MouseEvent me) {
            g2D.fillRect(me.getX(), me.getY(), 3, 3);
            label.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            g2D.dispose();
            g2D = null;
        }
    };

    PaintAnyTime() {
        label.setPreferredSize(
            new Dimension(image.getWidth(), image.getHeight())
        );

        label.addMouseListener(drawer);
        label.addMouseMotionListener(drawer);

        frame.add(label);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
