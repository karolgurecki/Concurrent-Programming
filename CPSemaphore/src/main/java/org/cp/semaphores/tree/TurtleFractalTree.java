package org.cp.semaphores.tree;

import ch.aplu.turtle.Turtle;
import ch.aplu.turtle.TurtleFrame;
import org.cp.semaphores.fractal.path.FractalPath;

import javax.swing.*;
import java.awt.*;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class TurtleFractalTree
        extends TurtleFrame
        implements FractalDrawable {

    private final boolean swapAngle;
    private       double  angle;
    private       int     step;
    private       Turtle  turtle;

    public TurtleFractalTree(final int step, final Integer angle, final boolean swapAngle) {
        super("Fractal", 3000, 3000);
        this.step = step;
        this.angle = angle;
        this.swapAngle = swapAngle;

        this.setVisible(true);

        this.initTurtle();
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    private void initTurtle() {
        this.turtle = new Turtle(this, Color.BLACK);
        this.turtle.setAngleResolution(90);
        this.turtle.setHeading(90);
        this.turtle.hideTurtle();
        this.turtle.setPos(-500, -1000);
    }

    @Override
    public void updateDrawing(final FractalPath path) {
        for (FractalPath.FStep fStep : path) {
            switch (fStep) {
                case RIGHT_TURN:
                    this.turtle.right(this.angle);
                    break;
                case LEFT_TURN:
                    this.turtle.left(this.angle);
                    break;
                case LINE:
                case FORWARD:
                    this.turtle.fd(this.step);
                    break;
                case BACKWARD:
                    this.turtle.bk(this.step);
                    break;
            }
        }
        if (this.swapAngle) {
            this.angle *= -1;
        }
    }
}
