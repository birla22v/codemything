import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 * The interface for a shape - a square or a circle
 * 
 * @author Humaira
 * 
 */
public interface DrawingShape {

	/**
	 * The method to paint the shape
	 * 
	 * @param g
	 *            The graphics object to paint on
	 */
	public void paintShape(Graphics g);

	/**
	 * Returns the x coordinate of the shape
	 * 
	 * @return The x coordinate of the shape
	 */
	public int getX_coord();

	/**
	 * Sets the x coordinate of the shape
	 * 
	 * @param x_coord
	 *            The left coordinate of the shape
	 */
	public void setX_coord(int x_coord);

	/**
	 * Returns the y coordinate of the shape
	 * 
	 * @return The y coordinate of the shape
	 */
	public int getY_coord();

	/**
	 * Sets the y coordinate of the shape
	 * 
	 * @param y_coord
	 *            The y coordinate of the shape
	 */
	public void setY_coord(int y_coord);

	/**
	 * Get the color of the shape
	 * 
	 * @return The color of the shape
	 */
	public Color getColor();

	public int getWidth();

	public void setWidth(int width);

	public int getHeight();

	public void setHeight(int height);

	/**
	 * Set color of the shape
	 * 
	 * @param color
	 *            The color of the shape
	 */
	public void setColor(Color color);

	/**
	 * Tells whether or not the shape has been clicked on by the mouse
	 * 
	 * @param x
	 *            The x coordinate of where the mouse clicked
	 * @param y
	 *            The y coordinate of where the mouse clicked
	 * @return Whether or not the shape has been clicked on by the mouse
	 */
	public boolean contains(int x, int y);

	/**
	 * Increase the size of the shape. It expands from the center
	 */
	public void expand();

	/**
	 * Decrease the size of the shape. It shrinks from the center
	 */
	public void shrink();

	/**
	 * The rectangle enclosing the shape
	 * 
	 * @return The rectangle enclosing the shape
	 */
	public Rectangle getBoundingRectangle();

	/**
	 * Returns whether or not the shape is selected
	 * 
	 * @return
	 */
	public boolean isSelected();

	/**
	 * Allows the 'selected' state of the shape to be changed
	 * 
	 * @param selected
	 */
	public void setSelected(boolean selected);

	public boolean isStroked();

	public void setStroke(boolean stroke);

	public Color getStrokeColor();

	public void setStrokeColor(Color color);

	public void setStrokeSize(int strokeSize);

	int getStrokeSize();

	/**
	 * Makes a deep copy of this drawing shape and returns it The advantage of a
	 * deep copy is that it is an object in its own right. Making any changes to
	 * the original shape will not effect it.
	 * 
	 * @return Returns the deep copy of this shape
	 */
	public DrawingShape copy();

	public AffineTransform getTrans();

	public void setTrans(AffineTransform trans);

	public void rotateClockwise();

	public void rotateAntiClockwise();

	public double getRotate();

	public void setRotate(double rotate);
	
	public boolean isFill() ;
	
	public void setFill(boolean fill) ;

}
