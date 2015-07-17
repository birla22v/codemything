import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class Oval implements DrawingShape {

	// How much the size of the shape changes
	private static final int CONSTANT = 4;

	// how much the shape rotates
	private static final int ROTATE_CONSTANT = 100;

	// the degree of current rotation
	private double rotate;

	// the current transformation of the shape
	private AffineTransform transformation = new AffineTransform();

	// x coordinate of the circle's center
	private int x_coord;

	// y coordinate of the circle's center
	private int y_coord;

	// The width of the oval
	private int width;

	// The height of the oval
	private int height;

	// The color of the shape
	private Color color;

	// Color of Stroke
	private Color strokeColor;

	private Ellipse2D.Double ellipse;

	private boolean selected;

	private boolean stroked;

	private int strokeSize;
	
	private boolean fill = true ;

	/**
	 * Constructs the Circle
	 * 
	 * @param x_coord
	 *            Center x coordinate
	 * @param y_coord
	 *            Center y coordinate
	 * @param radius
	 *            Radius of the circle
	 * @param color
	 *            Color of the circle
	 */
	public Oval(int x_coord, int y_coord, int height, int width, Color color,
			Color strokeColor, int strokeSize) {

		this.x_coord = x_coord;
		this.y_coord = y_coord;
		this.width = Math.abs(width);
		this.height = Math.abs(height);
		this.color = color;
		this.strokeColor = strokeColor;
		this.strokeSize = strokeSize;

		ellipse = new Ellipse2D.Double(-width / 2, -height / 2, width, height);
	}

	@Override
	public Color getColor() {
		return color;
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
	public int getWidth() {
		return width;

	}

	@Override
	public void setWidth(int width) {
		this.width = Math.abs(width);
		ellipse = new Ellipse2D.Double(-this.width / 2, -height / 2,
				this.width, height);

	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setHeight(int height) {
		this.height = Math.abs(height);
		ellipse = new Ellipse2D.Double(-width / 2, -this.height / 2, width,
				this.height);

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
		g2d.fill(ellipse);
		}else{
			g2d.draw(ellipse);
		}
		
		if (stroked) {
			g2d.setColor(strokeColor);
			g2d.setStroke(new BasicStroke(strokeSize));
			g2d.draw(ellipse);
		}

		if (selected) {

			g2d.setColor(Color.BLACK);

			Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	        g2d.setStroke(dashed);
			g2d.draw(ellipse);
		}

		g2d.rotate(-rotate);
		g2d.translate(-(x_coord + width / 2), -(y_coord + height / 2));

		g2d.setStroke(new BasicStroke(1));
	}

	@Override
	public boolean contains(int x, int y) {

		// If the distance is less than the radius, then the circle has been
		// clicked on


		AffineTransform inverse = null;

		try {
			inverse = transformation.createInverse();
		} catch (NoninvertibleTransformException e) {
			return false;
		}

		Point2D inversePoint = inverse
				.transform(new Point2D.Double(x, y), null);

		return ellipse.contains(inversePoint);
	}

	@Override
	public void expand() {

		width += CONSTANT;
		height += CONSTANT;
		x_coord -= CONSTANT / 2;
		y_coord -= CONSTANT / 2;

		ellipse = new Ellipse2D.Double(-width / 2, -height / 2, width, height);

	}

	@Override
	public void shrink() {

		width -= CONSTANT;
		height -= CONSTANT;
		x_coord += CONSTANT / 2;
		y_coord += CONSTANT / 2;


		ellipse = new Ellipse2D.Double(-width / 2, -height / 2, width, height);
	}

	@Override
	public Rectangle getBoundingRectangle() {

		return ellipse.getBounds();

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

		Oval temp = new Oval(x_coord, y_coord, height, width, color,
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
