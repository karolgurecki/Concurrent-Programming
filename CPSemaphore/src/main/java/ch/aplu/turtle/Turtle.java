// Copyright 2002-2003 Regula Hoefer-Isenegger
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

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;

/**
   The core class for Turtles.

   For a simple example on how to use <code>Turtles</code>, cf. the 
   <a href="package-summary.html#example"> Java Turtle Package description</a>. 
   @author <a href="mailto:regula@hoefer.ch">Regula Hoefer-Isenegger</a>
   @version 0.1.1 
*/
public class Turtle 
{
  private double angle;
  private Point2D.Double position;
  private Playground playground;
  private int framesPerSecond;
  private double speed;           // Pixel/sec
  private double angleSpeed;      // Radian/sec
  private TurtleRenderer turtleRenderer;
  private int angleResolution;
  private LineRenderer lineRenderer;
  private static TurtleFactory turtleFactory;
  private boolean penUp;
  private boolean showTurtle;
  private Pen pen;
  private Color color;
  private int edgeBehaviour;
  /** Represents clip mode.	

  @see #WRAP
  @see #clip()
  */
  protected final static int CLIP = 0;
  /** Represents wrap mode.
	
  @see #CLIP
  @see #wrap()
  */
  protected final static int WRAP = 1;
  /** Represents the default edge behaviour (i.e. CLIP or WRAP).

  @see #CLIP
  @see #WRAP
  @see #clip()
  @see #wrap()
  */
  protected static int DEFAULT_EDGE_BEHAVIOUR = CLIP;
  /** Represents the default speed (velocity).

  @see #speed(double)
  */
  protected static double DEFAULT_SPEED = 200;
  /** Represents the default angle resolution. 

  It specifies how many different turtle pictures
  are generated.
  */
  protected static int DEFAULT_ANGLE_RESOLUTION = 72;
  /** Specifies how many frames per second are used for 
      turtle animation.
  */
  protected static int DEFAULT_FRAMES_PER_SECOND = 25;
  /** Specifies the default turtle color.

  @see #setColor(java.awt.Color)
  */
  protected static Color DEFAULT_TURTLE_COLOR = Color.cyan;
  /** Specifies the default pen color.

  @see #setPenColor(java.awt.Color)
  */
  protected static Color DEFAULT_PEN_COLOR = Color.blue;
  /** Create a new <code>Turtle</code> in its own new Window.
   */
  public Turtle(){
    super();
    init(new TurtleFrame(), DEFAULT_TURTLE_COLOR);
    getPlayground().paintTurtles();
  }
  /** Create a new <code>Turtle</code> with specified 
      <code>color</code> in its own new Window.
  */
  public Turtle(Color color){
    super();
    init(new TurtleFrame(), color);
    getPlayground().paintTurtles();
  }
  /** Create a new <code>Turtle</code> and puts it into the
      (already existing) <code>turtleContainer</code>
  */
  public Turtle(TurtleContainer turtleContainer) {
    super();
    init(turtleContainer, DEFAULT_TURTLE_COLOR);
    getPlayground().paintTurtles();
  }
  /** Create a new <code>Turtle</code> with specified 
      <code>color</code> in the specified <code>turtleContainer</code>.
  */
  public Turtle(TurtleContainer turtleContainer, Color color){
    super();
    init(turtleContainer, color);
    getPlayground().paintTurtles();
  }
  /** Create a new <code>Turtle</code> in the same 
      <code>TurtleContainer</code> (Window) as 
      <code>otherTurtle</code>
  */
  public Turtle(Turtle otherTurtle){
    super();
    init(otherTurtle.getPlayground(), DEFAULT_TURTLE_COLOR);
    getPlayground().paintTurtles();
  }
  /** Create a new <code>Turtle</code> with the specified 
      <code>color</code> in the same 
      <code>TurtleContainer</code> (Window) as <code>otherTurtle</code>
  */
  public Turtle(Turtle otherTurtle, Color color){
    super();
    init(otherTurtle.getPlayground(), color);
    getPlayground().paintTurtles();
  }
  /** Initialize the <code>Turtle</code>. 

  If you want to inherit from this class and add 
  new Turtle attributes, you might want to overwrite this method. 
  */
  protected void init(Playground playground, Color color){
    angle = 0;
    position = new Point2D.Double(0,0);
    setPlayground(playground);
    framesPerSecond = DEFAULT_FRAMES_PER_SECOND;
    speed(DEFAULT_SPEED); 
    setAngleResolution(this.DEFAULT_ANGLE_RESOLUTION);
    angleSpeed = getSpeed()*Math.PI*2/DEFAULT_SPEED;
    showTurtle = true;
    pen = new Pen(this.DEFAULT_PEN_COLOR);
    if(getTurtleFactory() == null){
      turtleFactory = createTurtleFactory();
    }
    internalSetColor(color);
    this.lineRenderer = createLineRenderer();
    getTurtleRenderer().setAngle(getAngle());
  }
  /** This is only a "meta-method" for calling the 
      init(Playground, Color) method.

      @see #init(Playground, Color)
  */
  protected void init(TurtleContainer turtleContainer, Color color){
    init(turtleContainer.getPlayground(), color);
  }
  /** Resets the turtle to its standard settings.

  Only the color will be kept.
  @return the turtle to allow chaining.
  */
  public Turtle reinit(){
    getPlayground().clearTurtle(this);
    init(this.getPlayground(), this.getColor());
    getPlayground().paintTurtles(this);
    getPlayground().paintComponent();
    return this;
  }
  private void paintMe(){
    if(getPlayground().getGraphics() != null){
      getPlayground().paintComponent();
    }
  }
  /** Set the angle resolution for the turtle's pictures.
	
  It specifies how many pictures are used. e.g. an angle resolution 
  of 90 means that you get one picture for every 4 degrees 
  (= 360/90 degrees). 
  @see #DEFAULT_ANGLE_RESOLUTION
  */
  public Turtle setAngleResolution(int newResolution){
    angleResolution = newResolution;
    return this;
  }
  /** Returns the <code>TurtleFactory</code> of this turtle.

  @see TurtleFactory
  */
  public TurtleFactory getTurtleFactory() {
    return this.turtleFactory;
  }
  /** Create a <code>LineRenderer</code> which is responsible
      for the correct drawing of the lines.

      @return the new <code>LineRenderer</code>
  */
  protected LineRenderer createLineRenderer() {
    return new LineRenderer(this);
  }
  /**  Create a <code>TurtleRenderer</code> which is responsible
       for the correct drawing of the <code>Turtle</code>.

       @return the new <code>TurtleRenderer</code>
  */
  protected TurtleRenderer createTurtleRenderer() {
    return new TurtleRenderer(this);
  }
  /** Create a <code>TurtleFactory</code> which provides for
      the Turtle pictures.
       
      @return the new <code>TurtleFactory</code>
  */
  protected TurtleFactory createTurtleFactory() {
    return new TurtleFactory();
  }
  /** Get the angle resolution.

  @see #setAngleResolution(int)
  */
  protected int getAngleResolution() {
    return this.angleResolution;
  }
  /** Get the <code>TurtleRenderer</code>.
   */
  TurtleRenderer getTurtleRenderer() {
    return this.turtleRenderer;
  }
  /** Get the <code>LineRenderer</code>.
   */
  private LineRenderer getLineRenderer() {
    return this.lineRenderer;
  }
  /** Get the <code>Playground</code>.
   */
  public Playground getPlayground() {
    return this.playground;
  }
  /** Set the <code>Playground</code> to the specified
      <code>playground</code>.

      The <code>Turtle</code> is removed from the old 
      <code>Playground</code> and set to the new one.
  */
  private void setPlayground(Playground playground){
    Playground pg = getPlayground();
    // 	if (playground!=null) {
    // 	    System.out.println("arg: "+playground.hashCode());
    // 	}
    if(pg != null){
      // 	    System.out.println("old: "+pg.hashCode());
      pg.clearTurtle(this);
      pg.remove(this);
      pg.paintTurtles();
    }
    this.playground = playground;
    // 	if (this.playground!=null) {
    // 	    System.out.println("new: "+this.playground.hashCode());
    // 	}
    playground.add(this);
    playground.paintTurtles(this);
  }
  /** Get the angle speed.

  (I.e. how fast the <code>Turtle</code> rotation
  animation is performed.)
  */
  private double getAngleSpeed() {
    return this.angleSpeed;
  }
  /** Set the angle speed.

  @see #getAngleSpeed()
  */
  private void setAngleSpeed(double newAngleSpeed){
    this.angleSpeed = newAngleSpeed;
  }
  /** get the current angle (heading) of the 
      <code>Turtle</code>.
  */
  private double getAngle() {
    return this.angle; // in radians
  }
  /** Get the current speed.
   */
  private double getSpeed() {
    return this.speed;
  }
  /** Query the turtle's x-position.*/
  public double getX() {
    return position.getX();
  }
  /** Query the turtle's y-position.*/
  public double getY() {
    return position.getY();
  }
  /** Query the turtle's position */
  public Point2D.Double getPos(){
    return position;
  }
  /** Sets the x-coordinate of the <code>Turtle</code>s position to the given value.

  @return the turtle to allow chaining.
  */
  public Turtle setX(double x) {
    getPlayground().clearTurtle(this);
    internalSetX(x);
    getPlayground().paintTurtles(this);
    getPlayground().paintComponent();
    return this;
  }
  /** Sets the y-coordinate of the <code>Turtle</code>s position to the given value.

  @return the turtle to allow chaining.
  */
  public Turtle setY(double y) {
    getPlayground().clearTurtle(this);
    internalSetY(y);
    getPlayground().paintTurtles(this);
    getPlayground().paintComponent();
    return this;
  }
  /** Set the <code>Turtle</code>s x-Coordinate.
	
  No repainting!!! 
  */
  protected void internalSetX(double x){
    position.setLocation(x, getY());
  }
  /** Set the <code>Turtle</code>s y-Coordinate.
	
  No repainting!!! 
  */
  protected void internalSetY(double y){
    position.setLocation(getX(), y);
  }
  /** Set the <code>Turtle</code>s Position.
	
  No repainting!!! 
  */
  protected void internalSetPos(double x, double y){
    position.setLocation(x, y);
  }
  /** Hides the turtle.

  @return the turtle to allow chaining.
  @see #st()
  */
  public Turtle ht(){
    this.internalHide();
    return this;
  }
  /** Hides the turtle.
	
  This is the same as ht().
  @return the turtle to allow chaining.
  @see #st()
  */
  public Turtle hideTurtle(){
    this.internalHide();
    return this;
  }
  /** This is the method called by the public methods ht() and hideTurtle(). 

  Here the actual hiding takes place.
  @see #ht()
  @see #hideTurtle()
  */
  protected void internalHide(){
    getPlayground().clearTurtle(this);
    showTurtle = false;
    paintMe();
  }
  /** Sets the turtle to show mode.

  That means that the <code>Turtle</code> will be drawn.
  @return the turtle to allow chaining.
  @see #ht()
  */
  public Turtle st(){
    showTurtle = true;
    getPlayground().paintTurtle(this);
    return this;
  }
  /**	The same as st().

  @see #st()
  @return the turtle to allow chaining.
  */
  public Turtle showTurtle(){
    return st();
  }
  /**	Tells wheter the <code>Turtle</code> is hidden or not.

  @return <code>true</code> if the turtle is hidden,
  <code>false</code> otherwise.
  */
  public boolean isHidden(){
    return !showTurtle;
  }
  private int getFramesPerSecond() {
    return this.framesPerSecond;
  }
  /** Only set the angle attribute. This method does not
      invoke any re-painting.
  */
  private void setAngle(double radians) {
    this.angle = radians;
  }
  /**	This is the same as setH(double degrees).

  @see #setH(double)
  @return the turtle to allow chaining.
  */
  public Turtle setHeading(double degrees){
    setAngle(Math.toRadians(degrees));
    getTurtleRenderer().setAngle(Math.toRadians(degrees));
    getPlayground().clearTurtle(this);
    getPlayground().paintTurtles(this);
    getPlayground().paintComponent();
    return this;
  }
  /**	Set the <code>Turtle</code>s heading.

  0 means facing NORTH.<br>
  the angles are measured clockwise.
  @return the turtle to allow chaining.
  @see #setHeading(double)
  */
  public Turtle setH(double degrees){
    return setHeading(degrees);
  }
  /**	Query the <code>Turtle</code>s heading.

  @see #setH(double)
  */
  public double heading(){
    return Math.toDegrees(getAngle());
  }
  /** Set the <code>Turtle</code>s heading to the new value.

  @return the old (previous) value.
  @see #setH(double)
  */
  public double heading(double degrees){
    double tmp = Math.toDegrees(getAngle());
    setHeading(degrees);
    return tmp;
  }
  /**	Set the <code>Turtle</code>s speed.

  If you try to set the speed to 0, it will be set to 1 (very slow).
  A negative speed means that moving the turtle (fd, bk)
  will not be animated.<br>
  The unit is pixels per second (up to certain bounds depending on the CPU etc.).<br>
  <i>Remark: Dashed lines will only be painted as you expect it with speed set
  to <code>-1</code>.</i>

  @return the turtle to allow chaining.
  @see #fd(double)
  @see #bk(double)
  */
  public Turtle speed(double newSpeed){
    try{
      setSpeed(newSpeed);
    }
    catch (IllegalArgumentException e){
      setSpeed(1);
    }
    //setAngleSpeed(getSpeed()/DEFAULT_SPEED*Math.PI*2);
    return this;
  }
  /** This is the internal speed setting method. 
      It sets the speed to the specified value. 
  */
  private void setSpeed(double speed){
    if (speed == 0){
      this.speed = 1;
    } 
    else {
      this.speed = speed;
    }
  }
  /** This method is responsible for the rotation animation.
   */
  private void internalRotate(double angle) {
    // angle in radians
    if(isHidden()){
      setAngle(getAngle()+angle);
      if (getTurtleRenderer().imageChanged(getAngle())) {
	getTurtleRenderer().setAngle(getAngle());
      }
      return;
    }
    if (angle != 0) {
      int iterations = getAngleIterations(angle);
       
      double sign = angle/Math.abs(angle);
      double increment = sign*getAngleSpeed()/(double)getFramesPerSecond();
      double startAngle = getAngle();

      for (int index = 0; index < iterations; index++) {	    
	long timeStamp = System.currentTimeMillis();
	
	getPlayground().clearTurtle(this);

	if (index < iterations-1) {
	  setAngle(getAngle()+increment);
	}
	else {
	  setAngle(startAngle+angle);
	}

	if (getTurtleRenderer().imageChanged(getAngle())) {
	  getTurtleRenderer().setAngle(getAngle());
	  getPlayground().paintTurtles(this);
	}
	long newTimeStamp = System.currentTimeMillis();
	Double secs = new Double(1000./getFramesPerSecond());
	long requiredTime = secs.longValue()-newTimeStamp+timeStamp;

	if (requiredTime > 0) {
	  try {
	    Thread.sleep(requiredTime);
	  }
	  catch (InterruptedException e) {
			
	  }
	}
      }
    }
    getPlayground().paintTurtles(this);
  }
  /** This method is responsible for the moving animation.
   */
  private void internalMove(double length) {
    if (getSpeed()>0){
      if (length != 0) {
	int iterations = getPathIterations(length);
	// an angle of 0 means: facing NORTH
	double startX = getX();
	double startY = getY();
	getLineRenderer().init(startX, startY);
	double dx = length * Math.sin(getAngle());
	double dy = length * Math.cos(getAngle());
	double incrementX = dx / iterations;
	double incrementY = dy / iterations;
	for (int index = 0; index < iterations; index++) {
	  long timeStamp = System.currentTimeMillis();
	  int nX = (int)getX();
	  int nY = (int)getY();
	  getPlayground().clearTurtle(this);
	  if (index < iterations-1) {
	    internalSetX(getX()+incrementX);
	    internalSetY(getY()+incrementY);
	  }
	  else { // last step: Calc the "exact" value
	    internalSetX(startX + dx);
	    internalSetY(startY + dy);
	  }
	  if (nX != (int)getX() 
	      || nY != - (int)getY() 
	      || index == iterations-1) 
	    {
	      if (!isPenUp()) {
		getLineRenderer().lineTo(getX(), getY());
	      }
	      getPlayground().paintTurtles(this);
	      getPlayground().repaint();
	    }
	  Double frames = new Double(1000./getFramesPerSecond());
	  long newTimeStamp = System.currentTimeMillis();
	  long requiredTime = frames.longValue()-newTimeStamp+timeStamp;
	  if (requiredTime > 0) {
	    try {
	      Thread.sleep(requiredTime);
	    }
	    catch (InterruptedException e) {
	    }
	  }
	}
      }
    } 
    else{ // Speed < 0, i.e. no animation
      double startX = getX();
      double startY = getY();
      getLineRenderer().init(startX, startY);
      double dx = length * Math.sin(getAngle());
      double dy = length * Math.cos(getAngle());
      getPlayground().clearTurtle(this);
      internalSetX(startX + dx);
      internalSetY(startY + dy);
      if (!isPenUp()) {
	getLineRenderer().lineTo(getX(), getY());
      }
      getPlayground().paintTurtles(this);
      //      getPlayground().repaint();
    }
    getPlayground().repaint();
  }
  /**	Turns the <code>Turtle</code> <code>degrees</code> degrees to the left.
	
  @return the turtle to allow chaining.
  @see #rt(double)
  */
  public Turtle lt(double degrees) {
    return left(degrees);
  }
  /**	Same as lt(double degrees)

  @see #lt(double)
  @return the turtle to allow chaining.
  */
  public Turtle left(double degrees){
    internalRotate(-Math.toRadians(degrees));
    return this;
  } 
  /**	Turns the <code>Turtle</code> <code>degrees</code> degrees to the right.
	
  @return the turtle to allow chaining.
  @see #rt(double)
  */
  public Turtle rt(double degrees) {
    return right(degrees);
  }
  /**	Same as rt(double degrees).

  @see #rt(double)
  @return the turtle to allow chaining.
  */
  public Turtle right(double degrees){
    internalRotate(Math.toRadians(degrees));
    return this;
  }
  /** Same as fd(double distance)

  @see #fd(double)
  @return the turtle to allow chaining.
  */
  public Turtle forward(double distance) {
    internalMove(distance);
    return this;
  }
  /** Moves the <code>Turtle</code> forwards.
	
  Negative values for <code>distance</code> are
  allowed. In that case, the <code>Turtle</code>
  will move backwards.

  @see #bk(double)
  @return the turtle to allow chaining.
  */
  public Turtle fd(double distance){
    return forward(distance);
  }
  /**	Same as bk(double distance).

  @see #bk(double)
  @return the turtle to allow chaining.
  */
  public Turtle back(double distance) {
    internalMove(-distance);
    return this;
  }
  /**	Moves the <code>Turtle</code> backwards.

  Negative values for <code>distance</code> are
  allowed. In that case, the <code>Turtle</code>
  will move forwards.

  @see #fd(double)
  @return the turtle to allow chaining.
  */
  public Turtle bk(double distance) {
    return back(distance);
  }
  //      public void clearScreen(){
  //  	if(getPlayground().countTurtles() > 1){
	    
  //  	}
  //  	else{
  //  	    getPlayground().clear();
  //  	    paintTurtle(this);
  //  	}
  //      }
  //      public void cs(){
  //  	clearScreen();
  //      }
  protected Point2D.Double getPosition(){
    return this.position;
  }
  /** Query the distance from the current location
      to the given one.
  */
  public double distance(double x, double y){
    return this.getPosition().distance(x,y);
  }
  /** Query the distance from the current location
      to the given one.
  */
  public double distance(Point2D.Double p){
    return this.getPosition().distance(p);
  }
  /** Method to calculate the number of iterations when
      animating left or right (rotation).
  */
  private int getAngleIterations(double dAngle) {
    if(getAngleSpeed()<0){
      return 1;
    }
    if(getAngleSpeed()==0){
      setAngleSpeed(1);
    }
    double dAbsAngle = Math.abs(dAngle);
    Double dValue = new Double(Math.ceil(dAbsAngle/getAngleSpeed()*getFramesPerSecond()));
    return dValue.intValue();
  }
  /** Method to calculate the number of iterations when
      animating forwards or backwards.
  */
  private int getPathIterations(double length) {
    if(getSpeed() < 0){
      return 1;
    }
    if(getSpeed()==0){
      setSpeed(1);
    }
    double dAbsLength = Math.abs(length);
    Double dValue = new Double(Math.ceil(dAbsLength/getSpeed()*getFramesPerSecond()));
    return dValue.intValue();
  }
  /**	Lifts the <code>Turtle</code>s <code>Pen</code> up so it
	won't draw a line anymore when moving.
	
	This is the same as pu().

	@see #pu()
	@see #penDown()
	@see #pd()
	@return the turtle to allow chaining.
  */
  public Turtle penUp() {
    this.penUp = true;
    return this;
  }
  /**	Lifts the <code>Turtle</code>s <code>Pen</code> up so it
	won't draw a line anymore when moving.

	This is the same as penUp().

	@see #penUp()
	@see #penDown()
	@see #pd()
	@return the turtle to allow chaining.
  */
  public Turtle pu(){
    return this.penUp();
  }
  /**	Lowers the <code>Turtle</code>s <code>Pen</code> down  so it
	will draw a line when moving.

	This is the same as pd().

	@see #pd()
	@see #penUp()
	@see #pu()
	@return the turtle to allow chaining.
  */
  public Turtle penDown() {
    this.penUp = false;
    return this;
  }
  /**	Lowers the <code>Turtle</code>s <code>Pen</code> down  so it
	will draw a line when moving.

	This is the same as penDown().

	@see #penDown()
	@see #penUp()
	@see #pu()
	@return the turtle to allow chaining.
  */
  public Turtle pd(){
    return this.penDown();
  }
  /** Query the <code>Pen</code>s state (up or down).

  @return <code>true</code> if the <code>Pen</code> is
  up, <code>false</code> otherwise.
  @see #pu()
  @see #pd()
  */
  public boolean isPenUp() {
    return this.penUp;
  }
  /** Returns the bounds of this <code>Turtle</code>. This is required 
      by the methods that (return-)paint the Turtles.
  */
  Rectangle getBounds() {
    Rectangle rect = new Rectangle();

    Image img = getTurtleRenderer().currentImage();
    int nWidth = img.getWidth(getTurtleRenderer());
    int nHeight = img.getHeight(getTurtleRenderer());
    double x = (getX()<0)?Math.floor(getX()):Math.ceil(getX());
    double y = (getY()<0)?Math.floor(getY()):Math.ceil(getY());
    rect.setBounds((int)x-nWidth/2, (int)y + nHeight/2, nWidth, nHeight);
    return rect;
  }
  /** Get the <code>Turtle</code>s <code>Pen</code>.
	
  You need it if you want to change end caps etc.
  @see Pen
  */
  public Pen getPen(){
    return pen;
  }
  /** Set the Line Thickness.

  This works only neatly in clip mode (yet).
  @see #clip()
  @see #wrap()
  */
  public Turtle setLineWidth(double lineWidth){
    setLineWidth((float)lineWidth);
    return this;
  }
  /** Set the Line Thickness.

  This works only neatly in clip mode (yet).
  @see #clip()
  @see #wrap()
  */
  public Turtle setLineWidth(float lineWidth){
    getPen().setLineWidth(lineWidth);
    return this;
  }
  /**	Set the turtle's color to the specified one.

  @return the turtle to allow chaining.
  */
  public Turtle setColor(Color color){
    internalSetColor(color);
    getPlayground().paintTurtles();
    return this;
  }
  private void internalSetColor(Color color){
    this.color = color;
    if(getTurtleRenderer()==null){
      turtleRenderer = createTurtleRenderer();
      getTurtleRenderer().init(getTurtleFactory(), getAngleResolution());
    }
    else{
      getTurtleRenderer().init(new TurtleFactory(), 
			       this.angleResolution);
    }
  }
  /**	Set the fill color to the specified one.

  @see #fill()
  @return the turtle to allow chaining.
  */
  public Turtle setFillColor(Color color){
    getPen().setFillColor(color);
    return this;
  }
    
  /** Query the turtle's current color.
   */
  public Color getColor(){
    return color;
  }
  /**	Set the <code>Turtle</code>s <code>Pen</code> color.

  @return the turtle to allow chaining.
  */
  public Turtle setPenColor(Color color){
    getPen().setColor(color);
    return this;
  }
  /**	Move the Turtle back "home", i.e. set its position
	to the origin, facing NORTH.

	Color, PenColor etc. remain the same.
	@see #reinit()
	@return the turtle to allow chaining.
  */
  public Turtle home(){
    // first : clean the turtle!
    getPlayground().clearTurtle(this);
    position = new Point2D.Double(0,0);
    setHeading(0);
	
    return this;
  }
  /**	The <code>Turtle</code>s <code>Pen</code> is
	changed to an eraser (which is in fact a pen with
	background color).

	This is the same as pe()
	@see #pe()
	@return the turtle to allow chaining.
  */
  public Turtle penErase(){
    this.internalPenErase();
    return this;
  }
  /**	The <code>Turtle</code>s <code>Pen</code> is
	changed to an eraser (which is in fact a pen with
	background color).

	This is the same as penErase()
	@see #penErase()
	@return the turtle to allow chaining.
  */
  public Turtle pe(){
    this.internalPenErase();
    return this;
  }
  protected void internalPenErase(){
    this.setPenColor(getPlayground().getBackground());
  }
  /**	Put the turtle to a new position with specified
	x- and y-coordinates.

	@return the turtle to allow chaining.
  */
  public Turtle setPos(double x, double y){
    getPlayground().clearTurtle(this);
    internalSetPos(x, y);
    getPlayground().paintTurtles();
    getPlayground().repaint();
    return this;
  }
  /**	Put the turtle to a new position.

  @return the turtle to allow chaining.
  */
  public Turtle setPos(Point2D.Double p){
    return setPos(p.x, p.y);
  }
  //     /**	Put the turtle in the specified TurtleContainer on the specified
  // 	x- and y-coordinates.

  // 	@return the turtle to allow chaining.
  //      */
  //     public Turtle setPos(TurtleContainer container, double x, double y){
  // 	return setPos(container.getPlayground(), x, y);
  //     }
  //     /**	Put the turtle in the specified Playground on the specified
  // 	x- and y-coordinates.

  // 	@return the turtle to allow chaining.
  //      */
  //     public Turtle setPos(Playground playground, double x, double y){
  // 	internalSetPos(x, y);
  // 	setPlayground(playground);
  // 	return this;
  //     }
  //     /**	Put the turtle in the specified TurtleContainer on the specified
  // 	position.

  // 	@return the turtle to allow chaining.
  //      */
  //     public Turtle setPos(TurtleContainer container, Point2D.Double p){
  // 	return setPos(container, p.x, p.y);
  //     }
  //     /**	Put the turtle in the specified Playground on the specified
  // 	position.

  // 	@return the turtle to allow chaining.
  //      */
  //     public Turtle setPos(Playground playground, Point2D.Double p){
  // 	return setPos(playground, p.x, p.y);
  //     }
  //     /**	Put the turtle in the specified TurtleContainer (with the same 
  // 	position as before)

  // 	@return the turtle to allow chaining.
  //      */
  //     public Turtle setPos(TurtleContainer container){
  // 	return setPos(container, this.getX(), this.getY());
  //     }
  //     /**	Put the turtle in the specified Playground (with the same 
  // 	position as before)

  // 	@return the turtle to allow chaining.
  //      */
  //     public Turtle setPos(Playground playground){
  // 	return this;
  //     }
  /**	Leave an imprint of the Turtle on the "canvas".

  @return the turtle to allow chaining.
  */
  public Turtle stampTurtle(){
    this.getPlayground().stampTurtle(this);
    return this;
  }
  /** Calculates the direction to a given point.

  @return the direction from the current turtle position
  towards the given point, measured in degrees and clockwise
  from the vertical upwards position.
  */
  public double towards(double x, double y){
    double dx = x - getX();
    double dy = y - getY();
    double result = Math.toDegrees(Math.atan2(dx, dy));
    return (result < 0)?result+360:result;
  }
  /** Calculates the direction to a given point.

  @return the direction from the current turtle position
  towards the given point, measured in degrees and clockwise
  from the vertical upwards position.
  */
  public double towards(Point2D.Double p){
    return towards(p.getX(), p.getY());
  }
  /** Query the current x-coordinate.
   */
  public double curX(){
    return getX();
  }
  /** Query the current y-coordinate.
   */
  public double curY(){
    return getY();
  }
  /** Puts the Turtle to the top (i.e. above any other
      Turtle).
	
      Just invokes the toTop-Method in Playground.
      @see Playground#toTop
      @see #internalToBottom
  */
  void internalToTop(){
    this.getPlayground().toTop(this);
  }
  /** Puts the Turtle to the bottom (i.e. under any other
      Turtle).
	
      Just invokes the toBottom-Method in Playground.
      @see Playground#toBottom
      @see #internalToTop
  */
  void internalToBottom(){
    this.getPlayground().toBottom(this);
  }
  /**	Put this turtle to the bottom.

  So any other turtle in the same Playground will be drawn over it.
  @return the turtle to allow chaining.
  */
  public Turtle toBottom(){
    //  	this.getPlayground().toBottom(this);

    internalToBottom();
    getPlayground().paintTurtles();
    return this;
  }
  /**	Put this turtle to the top.

  So it will be drawn over any other turtle in the same Playground.
  @return the turtle to allow chaining.
  */
  public Turtle toTop(){
    //  	this.getPlayground().toTop(this);
    this.getPlayground().paintTurtles(this);
    return this;
  }
  /**	Set the pen width.

  @return the turtle to allow chaining.
  */
  public Turtle penWidth(int newWidth){
    return penWidth((float)newWidth);
  }
  /** Internal Method for setting the penWidth.
   */
  private Turtle penWidth(float newWidth){
    setLineWidth(newWidth);
    return this;
  }
  /** Query the pen width.
   */
  public int penWidth(){
    return (int)this.getPen().getLineWidth();
  }
  /** Returns the current edge behaviour.

  @see #CLIP
  @see #WRAP
  */
  protected int getEdgeBehaviour(){
    return edgeBehaviour;
  }
  /** Sets the edge behaviour to the specified value;

  @see #CLIP
  @see #WRAP
  */
  protected void setEdgeBehaviour(int edgeBehaviour){
    this.edgeBehaviour = edgeBehaviour;
  }
  /**	Set the turtle to clip-mode.

  It could be that the turtle moves beyond the edge of the window. You won't see it
  anymore unless it finds its way back :-)<br>
  The default edge behaviour is clipping.

  @see #wrap()
  @see #clips()
  @return the turtle to allow chaining.
  */
  public Turtle clip(){
    setEdgeBehaviour(CLIP);
    return this;
  }
  /** Causes the turtle to wrap around the edges.

  This means: as soon as the turtle leaves the Window on one side, it re-
  appears on the opposite side. 
  
  @return the turtle to allow chaining.
  @see #clip()
  */
  public Turtle wrap(){
    setEdgeBehaviour(WRAP);
    return this;
  }
  /** Tells wheter the turtle is in clip mode.

  @return true if in clip mode, false otherwise.     
  @see #clip()
  @see #wraps()
  */
  public boolean clips(){
    return (getEdgeBehaviour() == CLIP);
  }
  /** Tells wheter the turtle is in wrap mode.
     
  @return true if in wrap mode, false otherwise.
  @see #wrap()
  @see #clips()
  */
  public boolean wraps(){
    return (getEdgeBehaviour() == WRAP);
  }
  /** Fills the region the Turtle is in. 

  A Region is bounded by lines 
  of any other color than the background color and by the border of 
  the Window. <br>
	  
  @return the turtle to allow chaining.
  */
  public Turtle fill(){
    getPlayground().fill(this);
    return this;
  }
  /** Fills the region with coordinates <code>x</code> and <code>y</code>. 

  A Region is bounded by lines 
  of any other color than the background color and by the border of 
  the Window. <br>
	  
  @return the turtle to allow chaining.
  */
  public Turtle fill(double x, double y){
    double oldX = getX();
    double oldY = getY();
    boolean hidden = isHidden();
    ht().setPos(x,y);
    getPlayground().fill(this);
    setPos(oldX, oldY);
    if(!hidden){
      st();
    }
    return this;
  }
  /** Clears away all that was painted in some way by a turtle (such as lines, fillings, text, stamps etc.) 
	
  @return the turtle to allow chaining.
  */
  public Turtle clean(){
    getPlayground().clean();
    return this;
  }
  /** Paints the specified Text at the current Turtle position.

  @return the turtle to allow chaining.
  */
  public Turtle label(String text){
    getPlayground().label(text, this);
    return this;
  }
  /** Sets the current Font to the specified one.

  @see java.awt.Font
  @return the turtle to allow chaining.
  */
  public Turtle setFont(Font font){
    getPen().setFont(font);
    return this;
  }
  /** Change the current font to the specified one. If you want to know what fonts are available on your system,
      call #getAvailableFontFamilies() .
 
      @see #getAvailableFontFamilies
      @see java.awt.Font  more information about fontName, style and size.
  */
  public Turtle setFont(String fontName, int style, int size){
    getPen().setFont(new Font(fontName, style, size));
    return this;
  }
  /** Sets the Font size.

      @see #setFontStyle  changing  the font style
      @see #setFont  changing  the whole font.
      @return the turtle to allow chaining.
  */
  public Turtle setFontSize(int size){
    getPen().setFontSize(size);
    return this;
  }
  /** Sets the Font style.

      This is either <code>java.awt.Font.PLAIN, java.awt.Font.BOLD, java.awt.Font.ITALIC</code> or <code>java.awt.Font.BOLD+java.awt.Font.ITALIC</code>
      @see #setFontSize  changing  the font size
      @see #setFont  changing  the whole font.
      @return the turtle to allow chaining.
  */
  public Turtle setFontStyle(int style){
    getPen().setFontStyle(style);
    return this;
  }
  /** Provides information about all font families (e.g. Roman) currently available on your system.

      This might be useful when you want to change the font.
      @see #setFont(java.awt.Font)
      @see #setFont(java.lang.String, int, int) 
  */
   public String[] getAvailableFontFamilies(){
     return getPen().getAvailableFontFamilies();
   }
  /** Returns the current Font. 

      @see #setFontSize  changing  the font size
      @see #setFontStyle  changing  the font style
      @see #setFont  changing  the whole font.
  */
  public Font getFont(){
    return getPen().getFont();
  }

}



















