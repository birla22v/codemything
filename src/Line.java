import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D.Double;

public class Line implements DrawingShape {

	// How much the size of the shape changes
	private static final double CONSTANT = 10;

	// how much the shape rotates
	private static final int ROTATE_CONSTANT = 100;
	
	private static final int RADIUS = 30 ;

	// the degree of current rotation
	private double rotate;

	// the current transformation of the shape
	private AffineTransform transformation = new AffineTransform();

	// The Leftmost coordinate
	private int x_coord;

	// The topmost coordinate
	private int y_coord;

	private int end_X;
	private int end_Y;

	// Height of the shape
	private int height;

	// Width of the shape
	private int width;

	// The color of the shape
	private Color color;

	// Color of Stroke
	private Color strokeColor;

	private Line2D line;

	private boolean selected;

	private boolean stroked;
	private int strokeSize;

	private int x2_coord;
	private int y2_coord;
	
	private boolean fill = true ;
	
	//private Rectangles rect ;
	
	private Ellipse2D.Double ellipse ;
	
	private double scaleFactor = 1 ;


	/**
	 * Constructs the Square
	 * 
	 * @param x_coord
	 *            Leftmost coordinate
	 * @param y_coord
	 *            Topmost coordinate
	 * @param height
	 *            Height of the shape
	 * @param width
	 *            Width of the shape
	 * @param color
	 *            The color of the shape
	 */
	public Line(int x_coord, int y_coord, int height, int width, Color color,
			Color strokeColor, int strokeSize) {

		this.x_coord = x_coord;
		this.y_coord = y_coord;
		this.height = height;
		this.width = width;
		this.color = color;
		this.strokeColor = color;
		this.strokeSize = strokeSize;

		
		x2_coord = width ;
		y2_coord = height;
		
	
		line = new Line2D.Double(-width / 2, -height / 2, x2_coord , y2_coord);
	
		
		double x_c = (line.getX1() + line.getX2())/2 ;
		double y_c = (line.getY1() + line.getY2())/2 ;
		ellipse = new Ellipse2D.Double(x_c-RADIUS/2 ,y_c-RADIUS/2,RADIUS,RADIUS) ;
	

	}

	@Override
	public void paintShape(Graphics g) {
		
		

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.translate(x_coord + width / 2, y_coord + height / 2);
	

		g2d.rotate(rotate);


		transformation = g2d.getTransform();
		System.out.println("paint x2 " + x2_coord);

		double x_c = (line.getX1() + line.getX2())/2 ;
		double y_c = (line.getY1() + line.getY2())/2 ;
		ellipse = new Ellipse2D.Double(x_c-RADIUS/2 ,y_c-RADIUS/2,RADIUS,RADIUS) ;
		
		g2d.setColor(strokeColor);


		if (stroked) {
			g2d.setColor(strokeColor);
			g2d.setStroke(new BasicStroke(strokeSize));

		}


		g2d.drawLine(-width / 2, -height / 2, x2_coord , y2_coord);
		
		if (selected) {

			g2d.setColor(Color.BLACK);

			Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	        g2d.setStroke(dashed);
	        g2d.draw(ellipse) ;

		}else{
			
			g2d.setColor(Color.black);
		
			g2d.draw(ellipse) ;
		}

		g2d.rotate(-rotate);
		g2d.translate(-(x_coord + width / 2), -(y_coord + height / 2));

		
		
		g2d.setStroke(new BasicStroke(1));
		
	}

	@Override
	public Color getColor() {
		return strokeColor;
	}

	@Override
	public void setColor(Color color) {
		this.strokeColor = color;
	}

	@Override
	public int getX_coord() {
		return x_coord;
	}

	@Override
	public void setX_coord(int x_coord) {
		this.x_coord = x_coord;
	
	}

	@Override
	public int getY_coord() {
		return y_coord;
	}

	@Override
	public void setY_coord(int y_coord) {
		this.y_coord = y_coord;

	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
		y2_coord = height / 2;

	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
		x2_coord = width / 2;
		
	}

	@Override
	public boolean contains(int x, int y) {


		AffineTransform inverse = null;

		try {
			inverse = transformation.createInverse();
		} catch (NoninvertibleTransformException e) {
			return false;
		}

		Point2D inversePoint = inverse
				.transform(new Point2D.Double(x, y), null);
		

		
		double x_c = (line.getX1() + line.getX2())/2 ;
		double y_c = (line.getY1() + line.getY2())/2 ;
		ellipse = new Ellipse2D.Double(x_c-RADIUS/2 ,y_c-RADIUS/2,RADIUS,RADIUS) ;

		if (ellipse.contains(inversePoint)) {

			System.out.println("contains line");
		}
		return ellipse.contains(inversePoint);
	}

	@Override
	public void expand() {

		// COULD NOT FIGURE THIS OUT
		// THOUGHT DOING NOTHING WAS A BETTER SOLUTION THAN ROTATING THE SHAPE
		
	}

	@Override
	public void shrink() {

		// COULD NOT FIGURE THIS OUT
		// THOUGHT DOING NOTHING WAS A BETTER SOLUTION THAN ROTATING THE SHAPE

	}

	@Override
	public Rectangle getBoundingRectangle() {
		
	

		return line.getBounds();
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isStroked() {
		return stroked;
	}

	@Override
	public void setStroke(boolean stroke) {
		this.stroked = stroke;
	}

	@Override
	public Color getStrokeColor() {
		return strokeColor;
	}

	@Override
	public void setStrokeSize(int strokeSize) {
		this.strokeSize = strokeSize;

	}

	@Override
	public int getStrokeSize() {
		return strokeSize;
	}

	@Override
	public DrawingShape copy() {

		Line temp = (new Line(0, 0, height, width, color, strokeColor,
				strokeSize));

		temp.setStroke(stroked);
		temp.setRotate(rotate);

		return temp;
	}

	@Override
	public AffineTransform getTrans() {
		return transformation;
	}

	@Override
	public void setTrans(AffineTransform trans) {
		this.transformation = trans;
	}

	@Override
	public void rotateClockwise() {

		rotate += Math.PI / ROTATE_CONSTANT;

		rotate = rotate % (2 * Math.PI);

	}

	@Override
	public void rotateAntiClockwise() {

		rotate -= Math.PI / ROTATE_CONSTANT;

		if (rotate < 0) {

			rotate = -(Math.abs(rotate) % (2 * Math.PI));
		}

	}

	@Override
	public double getRotate() {
		return rotate;
	}

	@Override
	public void setRotate(double rotate) {
		this.rotate = rotate;
	}

	@Override
	public void setStrokeColor(Color color) {

		strokeColor = color;

	}

	
	@Override
	public boolean isFill() {
		return fill;
	}

	@Override
	public void setFill(boolean fill) {
		this.fill = fill;
	}
}
