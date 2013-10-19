// Copyright 2002 Regula Hoefer-Isenegger
//
// This file is part of The Java Turtle Package
//
// The Java Turtle Package is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// The Java Turtle Package is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with The Java Turtle Package; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package ch.aplu.turtle;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Vector;

/**
 * A <code>Playground</code> is the <code>Turtle</code>'s home, i.e. the <code>Turtle</code> lives
 * and moves in the <code>Playground</code>.
 * <p/>
 * The<code>Playground</code> is responsible for interpreting angle and position of the
 * <code>Turtle</code> and for choosing the correct turtle image and putting it on the right
 * spot of the Playground. This means: e.g. whenever you wish to switch the x- and y-axis, you
 * should do it in this class, and not in the <code>Turtle</code> class.
 * <i>Remarks:</i>
 * <ul>
 * <li>The <code>Playground</code> needs a Window (e.g. <code>Frame</code>, <code>Applet</code>)
 * to be displayed. One possible solution for this problem is already given with the <code>TurtleFrame</code>
 * class, which implements the <code>TurtleContainer</code> interface. Applets are not yet supported.</li>
 * <li>The <code>Turtle</code> itself has only direct access to its <code>Playground</code>, but not to the
 * <code>TurtleContainer</code>.</li>
 * </ul>
 *
 * @author <a href="mailto:regula@hoefer.ch">Regula Hoefer-Isenegger</a>
 * @version 0.1.1
 */
public class Playground
        extends JPanel
        implements ImageObserver {
  /* ********** ATTRIBUTES ********** */
    /**
     * The default background color.
     * <p/>
     * White.
     */
    protected java.awt.Color DEFAULT_BACKGROUND_COLOR = java.awt.Color.white;
    /**
     * Holds the <code>Turtle</code>s of this Playground.
     */
    Vector          turtles;
    /**
     * Holds the Canvas.
     * <p/>
     * I.e. the buffer where the lines are drawn.
     */
    Image           canvasBuffer;
    /**
     * Holds the <code>Turtle</code>s images.
     * <p/>
     * I.e. the buffer where the <code>Turtle</code>s are drawn.
     */
    Image           turtleBuffer;
    /**
     * This is the window (or applet or ...) which holds this <code>Playground</code>.
     */
    TurtleContainer rootpane;

  /* ********** CONSTRUCTORS ********** */

    /**
     * Create a new Playground inside the given <code>TurtleContainer</code> and with
     * standard size (400 x 400 pixels).
     */
    public Playground(TurtleContainer rootpane) {
        super();
        init(rootpane, new Dimension(400, 400));
    }

    /**
     * Create a new Playground inside the given <code>TurtleContainer</code> and
     * <code>size</code>.
     */
    public Playground(TurtleContainer rootpane, Dimension size) {
        super();
        init(rootpane, size);
    }

  /* ********** METHODS ********** */

    /**
     * Initializes everything,
     * <p/>
     * e.g. creates a new vector (which holds the
     * <code>Turtle</code>s), the offscreen buffers, and sets the size and
     * background color.
     *
     * @see #DEFAULT_BACKGROUND_COLOR
     */
    protected void init(TurtleContainer rootpane, Dimension size) {
        this.rootpane = rootpane;
        turtles = new Vector();
        canvasBuffer = new BufferedImage(size.width,
                size.height,
                BufferedImage.TYPE_INT_ARGB);
        getCanvasGraphics().setColor(DEFAULT_BACKGROUND_COLOR);
        getCanvasGraphics().fillRect(0, 0, size.width, size.height);
        turtleBuffer = new BufferedImage(size.width,
                size.height,
                BufferedImage.TYPE_INT_ARGB);
        //     clearBuffer = new BufferedImage(size.width,
        // 				    size.height,
        // 				    BufferedImage.TYPE_INT_ARGB);
        setBackground(DEFAULT_BACKGROUND_COLOR);
        setPreferredSize(size);
    }

    /**
     * Adds a new <code>Turtle</code> to the Playground.
     */
    public void add(Turtle turtle) {
        turtles.add(turtle);
        toTop(turtle);
    }

    /**
     * Removes a <code>Turtle</code> from the Playground.
     */
    public void remove(Turtle turtle) {
        turtles.remove(turtle);
    }

    /**
     * Tells how many <code>Turtle</code>s are now in this Playground.
     */
    public int countTurtles() {
        return turtles.size();
    }

    /**
     * Returns the <code>Turtle</code> at index <code>index</code>.
     */
    public Turtle getTurtle(int index) {
        return (Turtle) turtles.elementAt(index);
    }

    /**
     * Moves the given <code>Turtle</code> above all the others, then
     * paints all turtles.
     *
     * @see #toTop
     */
    public void paintTurtles(Turtle turtle) {
        toTop(turtle);
        paintTurtles();
    }

    /**
     * Just paint all turtles.
     */
    public void paintTurtles() {
        Graphics tg = getTurtleGraphics();
        tg.setColor(new Color(0, 0, 0, 0));
        tg.fillRect(0, 0, getWidth(), getHeight());
        for (int i = 0 ; i < countTurtles() ; i++) {
            Turtle aTurtle = getTurtle(i);
            if (!aTurtle.isHidden()) {
                paintTurtle(aTurtle);
            }
        }
    }

    /**
     * Paints only the given <code>Turtle</code>.
     */
    void paintTurtle(Turtle turtle) {
        if (turtleBuffer == null) {
            turtleBuffer = new BufferedImage(getWidth(),
                    getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D turtleGraphics = getTurtleGraphics();
        turtle.getTurtleRenderer().paint(turtle.getX(),
                turtle.getY(),
                turtleGraphics);
        turtleGraphics.dispose();
    }

    /**
     * Leave an imprint of the given <code>Turtle</code> on the canvas.
     */
    void stampTurtle(Turtle turtle) {
        Graphics2D canvasGraphics = getCanvasGraphics();
        turtle.getTurtleRenderer().paint(turtle.getX(),
                turtle.getY(),
                canvasGraphics);
        canvasGraphics.dispose();
        paintComponent();
    }

    /**
     * Draws a line from the point <code>(x0, y0)</code> to <code>(x1, y1)</code>
     * with the color of the given <code>Pen</code>.
     */
    void lineTo(double x0, double y0, double x1, double y1, Pen pen) {
        int ix0 = (int) Math.round(x0);
        int iy0 = (int) Math.round(y0);
        int ix1 = (int) Math.round(x1);
        int iy1 = (int) Math.round(y1);
        Graphics2D offGraphics = getOffscreenGraphics();
        Color color = pen.getColor();

        offGraphics.setColor(color);
        offGraphics.setStroke(pen.getStroke());
        offGraphics.drawLine(ix0, iy0, ix1, iy1);
        RepaintManager.currentManager(this).
                addDirtyRegion(this, ix0, iy0, ix1 - ix0, iy1 - iy0);
        offGraphics.dispose();

        if (canvasBuffer == null) {
            canvasBuffer = new BufferedImage(getWidth(),
                    getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D canvasGraphics = getCanvasGraphics();
        canvasGraphics.setColor(color);
        canvasGraphics.setStroke(pen.getStroke());
        canvasGraphics.drawLine(ix0, iy0, ix1, iy1);
        canvasGraphics.dispose();
    }

    /**
     * Fills a region.
     * <p/>
     * The region is defined by the <code>Turtle</code>s actual position and
     * is bounded by any other color than the background color.<br>
     * <i>Remark:</i> If the <code>Turtle</code> is already standing on a colored
     * Pixel, nothing happens.
     */
    public void fill(Turtle t) {
        final Point[] diff = {
                new Point(0, -1),
                new Point(-1, 0),
                new Point(1, 0),
                new Point(0, 1)};
        final int N = 0;
        final int W = 1;
        final int E = 2;
        final int S = 3;
        int bgcolor = getBackground().getRGB();
        int fillColor = t.getPen().getFillColor().getRGB();
        Vector list = new Vector();
        Point2D.Double p1 = toScreenCoords(t.getPos());
        int startX = (int) Math.round(p1.getX());
        int startY = (int) Math.round(p1.getY());
        Point p = new Point(startX, startY);
        if (((BufferedImage) canvasBuffer).getRGB(startX, startY) == bgcolor) {
            ((BufferedImage) canvasBuffer).setRGB(startX, startY, fillColor);
            list.addElement(new Point(startX, startY));
            int d = N;
            int back;
            while (list.size() > 0) {
                while (d <= S) { // forward
                    Point tmp = p.add(diff[d]);
                    try {
                        if (((BufferedImage) canvasBuffer).getRGB(tmp.x, tmp.y) == bgcolor) {
                            p.translate(diff[d]);
                            ((BufferedImage) canvasBuffer).setRGB(p.x, p.y, fillColor);
                            list.addElement(new Integer(d));
                            d = N;
                        } else {
                            d++;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        d++;
                    }
                }
                Object obj = list.remove(list.size() - 1);
                try {
                    d = ((Integer) obj).intValue(); // last element
                    back = S - d;
                    p.translate(diff[back]);
                } catch (ClassCastException e) {
                    // the first (zeroest) element in list is the start-point
                    // just do nothing with it
                }

            }
        }
        paintComponent();
    }

    /**
     * Clears the canvas (i.e. the buffer where all turtle lines are drawn).
     */
    public void clear() {
        clear(getCanvasGraphics());
    }

    /**
     * Clears the buffer with the given Graphics.
     */
    public void clear(Graphics g) {
        if (countTurtles() == 1) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.dispose();
            getRepaintManager().markCompletelyDirty(this);
            paintComponent();
        }
    }
    /** Clears the Playground.

     A Turtle can only clear the Playground if she is the only Turtle.
     */

    /**
     * Paints the Playground.
     * <p/>
     * This is Just a method for convenience.
     */
    public void paintComponent() {
        paintComponent(getGraphics());
    }

    /**
     * Draws the canvas and turtle buffers.
     */
    public void paintComponent(Graphics g) {
        g.drawImage(canvasBuffer, 0, 0, this);
        g.drawImage(turtleBuffer, 0, 0, this);
    }

    /**
     * Draws the whole canvas and turtle buffers into the offscreen buffer.
     */
    public void blitToOffscreenBuffer() {
        blitToOffscreenBuffer(0, 0, getWidth(), getHeight());
    }

    /**
     * Draws part of canvas and turtle buffers into the offscreen buffer,
     * according to the rectangle definded by the parameters.
     *
     * @param x
     *         the rectangle's x-coordinate
     * @param y
     *         the rectangle's y-coordinate
     * @param w
     *         the rectangle's width
     * @param h
     *         the rectangle's height
     */
    public void blitToOffscreenBuffer(int x, int y, int w, int h) {
        blitToOffscreenBuffer(canvasBuffer, x, y, w, h);
        blitToOffscreenBuffer(turtleBuffer, x, y, w, h);
    }

    /**
     * Draws part of canvas and turtle buffers into the offscreen buffer,
     * according to the given rectangle.
     */
    public void blitToOffscreenBuffer(Rectangle r) {
        blitToOffscreenBuffer(r.x, r.y, r.width, r.height);
    }

    /**
     * Draws an image into the offscreen buffer.
     *
     * @param buffer
     *         The image to be drawn.
     */
    public void blitToOffscreenBuffer(Image buffer) {
        blitToOffscreenBuffer(buffer, 0, 0, getWidth(), getHeight());
    }

    /**
     * Draws part (a rectangle defined by the parameters) of an image into
     * the offscreen buffer.
     *
     * @param buffer
     *         The image to be drawn.
     * @param x
     *         the rectangle's x-coordinate
     * @param y
     *         the rectangle's y-coordinate
     * @param w
     *         the rectangle's width
     * @param h
     *         the rectangle's height
     */
    public void blitToOffscreenBuffer(Image buffer,
                                      int x, int y, int w, int h) {
        Graphics2D off = getOffscreenGraphics();
        clear(off);
        if (buffer == null) {
            buffer = new BufferedImage(getWidth(),
                    getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            clear((Graphics2D) buffer.getGraphics());
        }
        off.drawImage(buffer, x, y, w, h, x, y, w, h, this);
    }

    /**
     * Removes the image of all turtles from the turtle buffer.
     * <p/>
     * This is used for repainting.
     */
    public void clearTurtles() {
        for (int i = 0 ; i < countTurtles() ; i++) {
            Turtle turtle = getTurtle(i);
            clearTurtle(turtle);
        }
    }

    /**
     * Removes the image of the given turtle from the turtle buffer.
     * <p/>
     * This is used for repainting.<br>
     * Only override this method if you have added a new behaviour (like
     * wrap or clip) to the turtle.
     */
    public void clearTurtle(Turtle turtle) {
        if (turtle != null) {
            if (!turtle.isHidden()) {
                if (turtle.clips()) {
                    clearClipTurtle(turtle);
                } else if (turtle.wraps()) {
                    clearWrapTurtle(turtle);
                }
            }
        }
    }

    /**
     * This method is called when the given <code>Turtle</code> is in wrap mode.
     *
     * @see ch.aplu.turtle.Turtle#wrap
     */
    protected void clearWrapTurtle(Turtle turtle) {
        clearWrapTurtle(turtle, turtleBuffer);
    }

    /**
     * Here the actual clearing of a <code>Turtle</code> in wrap mode from the
     * given image is performed.
     */
    protected void clearWrapTurtle(Turtle turtle, Image im) {
        Rectangle bounds = getBounds(turtle);
        int pWidth = getWidth();
        int pHeight = getHeight();
        int x = bounds.x;
        int y = bounds.y;
        while (x > pWidth) {
            x -= pWidth;
        }
        while (x < 0) {
            x += pWidth;
        }
        while (y > pHeight) {
            y -= pHeight;
        }
        while (y < 0) {
            y += pHeight;
        }
        x = x % pWidth;
        y = y % pHeight;
        toAlphaNull(im, new Rectangle(x, y, bounds.width, bounds.height)); // OK
        boolean right = (x + bounds.width > getWidth());
        boolean bottom = (y + bounds.height > getHeight());
        if (right) {
            toAlphaNull(im, new Rectangle(x - pWidth, y, bounds.width, bounds.height));
        }
        if (bottom) {
            toAlphaNull(im, new Rectangle(x, y - pHeight, bounds.width, bounds.height));
        }
        if (right && bottom) {
            toAlphaNull(im, new Rectangle(x - pWidth, y - pHeight, bounds.width, bounds.height));
        }
    }

    /**
     * Just to copy and translate a given Rectangle.
     */
    private Rectangle copyAndTranslate(Rectangle rect, int dx, int dy) {
        return new Rectangle(rect.x + dx, rect.y + dy,
                rect.width, rect.height);
    }

    /**
     * This method is called when the given <code>Turtle</code> is in clip mode.
     *
     * @see ch.aplu.turtle.Turtle#clip
     */
    protected void clearClipTurtle(Turtle turtle) {
        clearClipTurtle(turtle, turtleBuffer);
    }

    /**
     * Here the actual clearing of a <code>Turtle</code> in clip mode from the
     * given image is performed.
     */
    protected void clearClipTurtle(Turtle turtle, Image im) {
        Rectangle bounds = getBounds(turtle);
        toAlphaNull(im, bounds);
    }

    /**
     * Sets the alpha channel of all pixels in the given image
     * in the given Rectangle to zero (i.e. totally transparent).
     * <p/>
     * This method is used byte the clearXXXTurtle methods.
     */
    private void toAlphaNull(Image im, Rectangle rect) {
        Rectangle rim = new Rectangle(0, 0,
                im.getWidth(this),
                im.getHeight(this));
        Rectangle r = new Rectangle();
        if (rect.intersects(rim)) {
            r = rect.intersection(rim);
        }
        int size = r.width * r.height;
        float[] alphachannel = new float[r.width * r.height];
        ((BufferedImage) im).getAlphaRaster().setPixels(r.x,
                r.y,
                r.width,
                r.height,
                alphachannel);
    }

    /**
     * Puts a Turtle above all others.
     */
    public Turtle toTop(Turtle turtle) {
        if (turtles.removeElement(turtle)) {
            turtles.add(turtle);
        }
        return turtle;
    }

    /**
     * Puts a Turtle beyond all others.
     */
    public Turtle toBottom(Turtle turtle) {
        if (turtles.removeElement(turtle)) {
            turtles.add(0, turtle);
        }
        return turtle;
    }

    public boolean imageUpdate(Image img, int infoflags,
                               int x, int y, int width, int height) {
        return false;
    }

    /**
     * Calculates the screen coordinates of the given point.
     */
    Point2D.Double toScreenCoords(Point2D.Double p) {
        return internalToScreenCoords(p.x, p.y);
    }

    /**
     * Calculates the screen coordinates of the point given by the parameters.
     */
    Point2D.Double toScreenCoords(double x, double y) {
        return internalToScreenCoords(x, y);
    }

    /**
     * Calculates the screen coordinates.
     * <p/>
     * I.e. the interpretation of the coordinates.
     */
    protected Point2D.Double internalToScreenCoords(double x, double y) {
        // reflect at x-axis, then translate to center of Playground
        double newX = getWidth() / 2 + x;
        double newY = getHeight() / 2 - y;
        return new Point2D.Double(newX, newY);
    }

    /**
     * Calculates the screen angle.
     * <p/>
     * I.e. the interpretation of angle.
     *
     * @param radians
     *         The angle in radians.
     */
    double toScreenAngle(double radians) {
        double sa = radians;
        if (sa < Math.PI / 2) {
            sa += 2 * Math.PI;
        }
        sa -= Math.PI / 2;
        if (sa != 0) {
            sa = Math.PI * 2 - sa;
        }
        return sa;
    }

    /**
     * Calculates the bounds of the <code>Turtle</code>s picture on the screen.
     */
    protected Rectangle getBounds(Turtle turtle) {
        Rectangle bounds = turtle.getBounds();
        Point2D.Double tmp = toScreenCoords
                (new Point2D.Double(bounds.getX(), bounds.getY()));
        bounds.setRect(tmp.x - 2, tmp.y - 2, bounds.width + 4, bounds.height + 4);
        return bounds;
    }

    /**
     * The graphics of the turtleBuffer.
     */
    Graphics2D getTurtleGraphics() {
        return (Graphics2D) turtleBuffer.getGraphics();
    }

    /**
     * The graphics of the canvasBuffer.
     */
    Graphics2D getCanvasGraphics() {
        return (Graphics2D) canvasBuffer.getGraphics();
    }

    /**
     * The graphics of the offscreenBuffer.
     */
    Graphics2D getOffscreenGraphics() {
        return (Graphics2D) getOffscreenBuffer().getGraphics();
    }

    /**
     * Get the offscreen buffer.
     */
    Image getOffscreenBuffer() {
        Image off = getRepaintManager().getOffscreenBuffer(this,
                getWidth(),
                getHeight());
        return off;
    }

    /**
     * Get the canvasBuffer.
     */
    Image getCanvasBuffer() {
        return canvasBuffer;
    }

    /**
     * Get the RepaintManager.
     */
    RepaintManager getRepaintManager() {
        return RepaintManager.currentManager(this);
    }

    /**
     * Cleans the Canvas.
     * <p/>
     * All turtles stay how and where they are, only lines, text and stamps will be removed.
     */
    void clean() {
        Graphics g = getCanvasGraphics();
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.dispose();
        getRepaintManager().markCompletelyDirty(this);
        paintComponent();
    }

    /**
     * Draws the <code>text</code> at the current position of the Turtle <code>t</code>.
     * <p/>
     * Drawing a text at some coordinates <code>(x,y)</code> we mean that the bottom left corner of
     * the text will be at these coordinates.
     * Font and colour are specified by the Turtle's Pen.
     */
    public void label(String text, Turtle t) {
        Point2D.Double sc = toScreenCoords(t.getPos());
        int x = (int) Math.round(sc.x);
        int y = (int) Math.round(sc.y);
        Graphics2D g2 = getCanvasGraphics();

        FontRenderContext frc = g2.getFontRenderContext();
        Font f = t.getFont();
        TextLayout tl = new TextLayout(text, f, frc);
        g2.setColor(t.getPen().getColor());
        tl.draw(g2, x, y);
        paintComponent();
    }

    /**
     * Just a class for convenience.
     */
    private class Point
            extends java.awt.Point {
        Point(int x, int y) {
            super(x, y);
        }

        Point() {
            super();
        }

        Point(Point p) {
            super(p.x, p.y);
        }

        /**
         * Get a new Point with coordinates (this.x+p.x, this.y+p.y).
         */
        Point add(Point p) {
            return new Point(this.x + p.x, this.y + p.y);
        }

        /**
         * Translate by the amounts dx = p.x, dy = p.y.
         */
        void translate(Point p) {
            translate(p.x, p.y);
        }

        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }
}



















