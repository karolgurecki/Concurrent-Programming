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

import ch.aplu.turtle.*;
import java.awt.image.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

/** This class provides functionality for generating images (<code>java.awt.Image</code>) 
    of a <code>Turtle</code> for any angle, color (<code>java.awt.Color</code>) and size.<br>

    The following picture shows turtles for every 10 degrees in standard size and color:<br>
    <img src="pics/turtlefactory2.gif"></img><br>
    Keep in mind that the angle for the standardTurtle methods are measured in radians and 
    anticlockwise, where 0 means facing east, in contrast to the 
    Playground class. The reason for this is that the Playground itself interprets the angle 
    and position of the turtle. 

    @author <a href="mailto:regula@hoefer.ch">Regula Hoefer-Isenegger</a>
    @version 0.1
*/
public class TurtleFactory
{
    private int size = 29;
    private Toolkit tk = Toolkit.getDefaultToolkit();

    /** Generates the Picture of a Turtle with angle <code>angle</code>, standard size and standard color.
     */
    public Image standardTurtle(double angle){
	return standardTurtle(angle, this.size);
    }

    /** Generates the Picture of a Turtle with angle <code>angle</code>, <code>size</code> and standard Color.
     */
    public Image standardTurtle(double angle, int size){
	return standardTurtle(Color.cyan, angle, size);
    }

    /** Generates the Picture of a Turtle with <code>color</code>, angle <code>angle</code> and standard size.
     */
    public Image standardTurtle(Color color, double angle){
	return standardTurtle(color, angle, this.size);
    }

    /** Generates the Picture of a Turtle with <code>color</code>, angle <code>angle</code> and <code>size</code>.
     */
    public Image standardTurtle(Color color, double angle, int size){
	return standardTurtle(color, angle, size, size);
    }
    /** Generates the Picture of a Turtle with <code>color</code>, angle <code>angle</code>, width <code>w</code> 
	and height <code>h</code>.

	If you want any other standard Turtle, overwrite ONLY this method.
     */
    protected Image standardTurtle(Color color, double angle, int w, int h){
	BufferedImage img = new BufferedImage(w, h, 
					      BufferedImage.TYPE_4BYTE_ABGR );
	Graphics2D g = (Graphics2D)img.getGraphics();
	// origin is in the middle
  	g.translate(w/2, h/2);
	// an angle of 0 (zero) means facing east
	g.rotate(Math.PI/2);
  	g.rotate(-angle);
	g.setColor(color);

	// body
	g.fillOval((int)(-0.35*w),(int)(-0.35*h), (int)(0.7*w), (int)(0.7*h));

	// head
  	g.fillOval((int)(-0.1*w),(int)(-0.5*h),(int)(0.2*w),(int)(0.2*h));
	
	// tail
	int[] xcoords = {(int)(-0.05*w),0,(int)(0.05*w)};
	int[] ycoords = {(int)(0.35*h),(int)(0.45*h),(int)(0.35*h)};
	g.fillPolygon(xcoords,ycoords,3);

	// feet
	for(int i=0; i<4; i++){
	    g.rotate(Math.PI/2);
	    g.fillOval((int)(-0.35*w),(int)(-0.35*h), 
		       (int)(0.125*w), (int)(0.125*h));
	}
	g.dispose();
	return (Image)img;
    }

}













