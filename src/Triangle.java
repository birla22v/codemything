import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class Triangle implements DrawingShape {

	// How much the size of the shape changes
	private static final int CONSTANT = 4;

	// how much the shape rotates
	private static final int ROTATE_CONSTANT = 100;

	// the degree of current rotation
	private double rotate;

	// the current transformation of the shape
	private AffineTransform transformation = new AffineTransform();

	// The Leftmost coordinate
	private int x_coord;

	// The topmost coordinate
	private int y_coord;

	// Height of the shape
	private int height;

	// Width of the shape
	private int width;

	// The color of the shape
	private Color color;

	// Color of Stroke
	private Color strokeColor;

	private int strokeSize;

	private Polygon triangle;

	private int[] xPoints;
	private int[] yPoints;

	private boolean selected;
	private boolean stroked;
	
	private boolean fill = true ;

	/**
	 * Constructs the Square
	 * 
	 * @param x_coord
	 *            Leftmost coordiante
	 * @param y_coord
	 *            Topmost coordinate
	 * @param height
	 *            Height of the shape
	 * @param width
	 *            Width of the shape
	 * @param color
	 *            The color of the shape
	 */
	public Triangle(int x_coord, int y_coord, int height, int width,
			Color color, Color strokeColor, int strokeSize) {

		this.x_coord = x_coord;
		this.y_coord = y_coord;
		this.height = Math.abs(height);
		this.width = Math.abs(width);
		this.color = color;
		this.strokeColor = strokeColor;
		this.strokeSize = strokeSize;

		xPoints = new int[3];
		calculateXPoints();

		yPoints = new int[3];
		calculateYPoints();

		triangle = new Polygon(xPoints, yPoints, 3);

	}

	/**
	 * Calculates the X coordinates of the triangle
	 */
	private void calculateXPoints() {
		/*
		 * xPoints[0] = x_coord; xPoints[1] = x_coord + width / 2; xPoints[2] =
		 * x_coord + width;
		 */
		xPoints[0] = 0;
		xPoints[1] = width / 2;
		xPoints[2] = -width / 2;

	}

	/**
	 * Calculates the Y coordinates of the triangle
	 */
	private void calculateYPoints() {
		/*
		 * yPoints[0] = y_coord + height; yPoints[1] = y_coord; yPoints[2] =
		 * y_coord + height;
		 */

		yPoints[0] = -height / 2;
		yPoints[1] = height / 2;
		yPoints[2] = height / 2;

	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public int getX_coord() {
		return x_coord;
	}

	@Override
	public void setX_coord(int x_coord) {
		this.x_coord = x_coord;
		calculateXPoints();
		triangle = new Polygon(xPoints, yPoints, 3);
	}

	@Override
	public int getY_coord() {
		return y_coord;
	}

	@Override
	public void setY_coord(int y_coord) {
		this.y_coord = y_coord;
		calculateYPoints();
		triangle = new Polygon(xPoints, yPoints, 3);
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setHeight(int height) {

		this.height = Math.abs(height);

		calculateYPoints();
		triangle = new Polygon(xPoints, yPoints, 3);

	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setWidth(int width) {

		this.width = Math.abs(width);

		calculateXPoints();
		triangle = new Polygon(xPoints, yPoints, 3);

	}

	@Override
	public void paintShape(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.translate(x_coord + width / 2, y_coord + height / 2);
		g2d.rotate(rotate);

		transformation = g2d.getTransform();

		

		g2d.setColor(color);

		if(fill){
		g2d.fillPolygon(triangle);
		}else{
			g2d.drawPolygon(triangle);
		}
		
		if (stroked) {
			g2d.setColor(strokeColor);
			g2d.setStroke(new BasicStroke(strokeSize));
			g2d.draw(triangle);
		}

		if (selected) {

			g2d.setColor(Color.BLACK);
			Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	        g2d.setStroke(dashed);
			g2d.drawPolygon(triangle);

		}

		g2d.rotate(-rotate);
		g2d.translate(-(x_coord + width / 2), -(y_coord + height / 2));
		
		g2d.setStroke(new BasicStroke(1));

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

		return triangle.contains(inversePoint);
	}

	@Override
	public void expand() {

		height += CONSTANT;
		width += CONSTANT;
		x_coord -= CONSTANT / 2;
		y_coord -= CONSTANT / 2;

		calculateXPoints();
		calculateYPoints();
		triangle = new Polygon(xPoints, yPoints, 3);
	}

	@Override
	public void shrink() {

		height -= CONSTANT;
		width -= CONSTANT;
		x_coord += CONSTANT / 2;
		y_coord += CONSTANT / 2;

		calculateXPoints();
		calculateYPoints();
		triangle = new Polygon(xPoints, yPoints, 3);

	}

	@Override
	public Rectangle getBoundingRectangle() {

		return new Rectangle(x_coord, y_coord, width, height);
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
	public void setStrokeSize(int strokeSize) {
		this.strokeSize = strokeSize;

	}

	@Override
	public int getStrokeSize() {
		return strokeSize;
	}

	@Override
	public DrawingShape copy() {

		Triangle temp = new Triangle(x_coord, y_coord, height, width, color,
				strokeColor, strokeSize);

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
	public boolean isFill() {
		return fill;
	}

	@Override
	public void setFill(boolean fill) {
		this.fill = fill;
	}

}
