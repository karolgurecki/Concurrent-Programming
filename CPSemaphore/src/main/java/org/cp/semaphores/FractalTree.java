package org.cp.semaphores;

import org.cp.semaphores.fractal.path.FractalPath;

import javax.swing.*;
import java.awt.*;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class FractalTree
        extends JFrame {

    private Integer depth;
    private Integer angle;
    private int     y2;
    private int     x1;

    public FractalTree(final Integer depth, final Integer angle) {
        super("Fractal Tree");

        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setVisible(true);

        this.depth = depth;
        this.angle = angle;
        this.x1 = 400;
        this.y2 = 500;
    }

    private void drawTree(Graphics g, int x1, int y1, double angle, int depth) {
        if (depth == 0) {
            return;
        }

        int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * depth);
        int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * depth);

        g.drawLine(x1, y1, x2, y2);

        drawTree(g, x2, y2, angle - 20, depth - 1);
        drawTree(g, x2, y2, angle + 20, depth - 1);
    }

    private void drawTree(final Graphics g, final FractalPath path) {
        if (this.depth < 0) {
            return;
        }

        for (FractalPath.FStep step : path) {
            switch (step) {
                case LEFT_TURN:
                    if (this.angle < 0) {
                        this.angle *= -1;
                    }
                    break;
                case RIGHT_TURN:
                    if (this.angle > 0) {
                        this.angle *= -1;
                    }
                    break;
                case LINE_WITH_LEAF:
                    this.depth -= 1;
                case LINE:
                    int x2 = this.x1 + (int) (Math.cos(Math.toRadians(this.angle)) * depth);
                    int y2 = this.y2 + (int) (Math.sin(Math.toRadians(this.angle)) * depth);
                    g.drawLine(this.x1, this.y2, x2, y2);

                    this.x1 = x2;
                    this.y2 = y2;

                    break;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
    }

    public void updateTree(final FractalPath path) {
        this.drawTree(
                this.getGraphics(),
                path
        );
    }
}
