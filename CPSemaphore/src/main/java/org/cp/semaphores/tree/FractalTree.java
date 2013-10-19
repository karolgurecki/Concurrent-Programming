package org.cp.semaphores.tree;

import org.cp.semaphores.fractal.path.FractalPath;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Stack;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class FractalTree
        extends JFrame
        implements FractalDrawable {

    public static final int ANGLE_FIXED = -90;
    private final Configuration configuration;
    private final int           stepLength;
    private final int           deltaAngle;
    private       State         state;
    private Stack<State> memory = new Stack<>();

    public FractalTree(final Integer depth, final Integer angle) {
        super("Fractal Tree");

        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setVisible(true);

        // TODO pass start point to draw from
        this.configuration = new Configuration(400, 500, angle, depth);
        this.stepLength = 10;
        this.deltaAngle = 45;
        this.state = new State(400, 500, 0);
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
        if (this.configuration.decrementDepth() < 0) {
            return;
        }
        this.doDrawTree(path.iterator());
    }

    private void doDrawTree(final Iterator<FractalPath.FStep> iterator) {
        while (iterator.hasNext()) {
            final FractalPath.FStep step = iterator.next();
            switch (step) {
                case LEFT_TURN:
                    pushState();
                    rotateLeft();
                    break;
                case RIGHT_TURN:
                    popState();
                    rotateRight();
                    break;
                case LINE_WITH_LEAF:
                    drawStep();
                    break;
                case LINE:
                    drawStep();
                    break;
            }
        }
    }

    private void drawStep() {
        int oldX = state.getX();
        int oldY = state.getY();
        int angle = state.getAngle();

        int newX = (int) (oldX - Math.sin(angle) * stepLength);
        int newY = (int) (oldY - Math.cos(angle) * stepLength);
        this.getGraphics().drawLine(oldX, oldY, newX, newY);
        state = new State(newX, newY, angle);
    }

    private void moveStep() {
        int oldX = state.getX();
        int oldY = state.getY();
        int angle = state.getAngle();
        int newX = (int) (oldX - Math.sin(angle) * stepLength);
        int newY = (int) (oldY - Math.cos(angle) * stepLength);
        state = new State(newX, newY, angle);
    }

    private void rotateRight() {
        int x = state.getX();
        int y = state.getY();
        int oldAngle = state.getAngle();
        int newAngle = oldAngle - deltaAngle;
        state = new State(x, y, newAngle);
    }

    private void rotateLeft() {
        int x = state.getX();
        int y = state.getY();
        int oldAngle = state.getAngle();
        int newAngle = oldAngle + deltaAngle;
        state = new State(x, y, newAngle);
    }

    private void pushState() {
        memory.push(state);
    }

    private void popState() {
        state = memory.lastElement();
        memory.pop();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
    }

    @Override
    public void updateDrawing(final FractalPath path) {
        this.drawTree(
                this.getGraphics(),
                path
        );
    }

    class Configuration {
        private Point point;
        private float angle;
        private int   depth;

        private Configuration(final int x, final int y, final float angle, final int depth) {
            this.point = new Point(x, y);
            this.angle = angle;
            this.depth = depth;
        }

        public Point getPoint() {
            return point;
        }

        public double getX() {
            return point.getX();
        }

        public double getY() {
            return point.getY();
        }

        public void setPoint(final int x, final int y) {
            this.point.setLocation(x, y);
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(final float angles) {
            this.angle = angle;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(final int depth) {
            this.depth = depth;
        }

        public int decrementDepth() {
            return --this.depth;
        }
    }

    class State {
        private int x;
        private int y;
        private int angle;

        public State(int x, int y, int angle) {
            this.x = x;
            this.y = y;
            this.angle = angle;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getAngle() {
            return angle;
        }
    }
}
