import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class Canvas extends JPanel implements MouseListener,
		MouseMotionListener, KeyListener {

	// The background color for the window.
	private static final Color BACKGROUND_COLOR = Color.WHITE;

	// Height of the frame.
	private static final int FRAME_HEIGHT = 730;

	// Width of the frame.
	private static final int FRAME_WIDTH = 805;

	private static final int MAX_STROKE_SIZE = 80;

	private static final int MIN_STROKE_SIZE = 0;

	private static final char PRINT_CODE = '4';

	private static final int RECTANGLE = 0;

	private static final int OVAL = 1;

	private static final int LINE = 2;

	private static final int TRIANGLE = 3;

	// Display list of shapes
	private ArrayList<DrawingShape> displayList;

	// The shape that is clicked on
	private DrawingShape selectedShape;

	// x coordinate of the previous selected shape
	private int previous_x;

	// y coordinate of the previous selected shape
	private int previous_y;

	// When expanding or shrinking the shape, whether or not a shape is selected
	// for the first time
	private boolean first_time;

	// The dimensions of this JComponent
	private Dimension dimension;

	private int drawShapeInt;

	// private int lastDrawnShapeInt;

	private int startX;

	private int startY;

	private int numShapes;

	private int mode;

	private boolean stroke;

	private int strokeCount;

	private static final int DRAWING = 1;

	private static final int SELECTION = 2;

	private static final int COPY = 3;

	private static final int PASTE = 4;

	private DrawingShape shapeToPaste;

	private MouseEvent pasteAtEvent;

	private StringBuilder codeStringBuilder;

	private Color shapeColor;

	private Color strokeColor;

	private int strokeSize;

	private boolean fill;

	private static final Color BORDER_COLOR = new Color(175, 238, 238);

	/**
	 * Constructor Creates a canvas to paint, manipulate and animate shapes on
	 * 
	 * @param numShapes
	 *            The total number of shapes to be printed
	 */
	public Canvas() {

		this.setLayout(new BorderLayout());

		dimension = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
		this.setSize(dimension);

		// Create a black border around the canvas
		Border border = BorderFactory.createLineBorder(BORDER_COLOR, 10);
		this.setBorder(border);

		displayList = new ArrayList<DrawingShape>();

		first_time = true;

		stroke = false;
		strokeCount = 0;
		strokeSize = 2;
		fill = true;

		
		// add listeners
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		this.setFocusable(true);

		first_time = true;

		mode = DRAWING;

		drawShapeInt = RECTANGLE;

		codeStringBuilder = new StringBuilder("");

		shapeColor = Color.YELLOW;

		strokeColor = Color.red;

	}

	/**
	 * Paint all the shapes in the display list
	 * 
	 * @param g
	 *            The graphics object to paint on
	 */
	private void paintShapesInArray(Graphics g) {


		for (int i = 0; i < numShapes; i++) {

			DrawingShape shape = displayList.get(i);

			if (shape != null) {

				shape.paintShape(g);

			}
		}
	}

	/**
	 * Paint the canvas and everything on it
	 * 
	 * @param g
	 *            The graphics object to paint on
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		dimension = getSize();

		// Make the background black by drawing a black rectangle the size of
		// the panel
		g.setColor(BACKGROUND_COLOR);

	
		g.fillRect(0, 0, dimension.width, dimension.height);

	
		// paint the shapes
		paintShapesInArray(g);

		g2d.dispose(); // release the copy's resources

	}

	@Override
	public void mouseClicked(MouseEvent event) {
		
		

		if (event.getButton() == MouseEvent.BUTTON1) {

			if (mode == SELECTION) {

				
				selectShape(event);
				

			} else if (mode == PASTE) {

				pasteAtEvent = event;
				pasteShape();
			}

		}
		// right clicking on the screen will change mode
		else if (event.getButton() == MouseEvent.BUTTON3) {

			
			selectShape(event) ;
			

			// switch from drawing mode to selecting mode
			if (mode != SELECTION) {

				mode = SELECTION;

			}
			// switch from selecting mode to drawing mode
			else if (mode == SELECTION) {

				mode = DRAWING;
			}

		}

		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent event) {

		/**
		 * WEIRD PROBLEM : IF MOUSE IS CLICKED, IT STILL ENTERS MOUSE PRESSED
		 */

		// Mode : Drawing shapes

		if (mode == DRAWING) {

			if (event.getButton() == MouseEvent.BUTTON1) {

				drawShape(event);
				
			}else if (event.getButton() == MouseEvent.BUTTON3) {
				
				
				
				selectShape(event) ;
				
				
			}

		} else if (mode == SELECTION) {

			
			selectShape(event);
			
			
		}

		repaint();

	}

	public Color getShapeColor() {
		return shapeColor;
	}


	public Color getStrokeColor() {
		return strokeColor;
	}

	public int getStrokeSize() {
		return strokeSize;
	}

	

	@Override
	public void mouseReleased(MouseEvent event) {

		// so that when the next shape is selected, we will know it is its
		// first time being selected after another shape was selected
		first_time = true;

	}

	@Override
	public void mouseDragged(MouseEvent event) {

		// Mode : Drag Shape
		if (mode == SELECTION) {
			if (selectedShape == null) {

				return;
			}

			int x = event.getX();

			int y = event.getY();
			// Drag the shape with the mouse
			if (first_time) {

				previous_x = x;
				previous_y = y;

				first_time = false;
			}

			// calculate how much the mouse has been displaced from its
			// previous
			// position
			int dist_x = x - previous_x;

			int dist_y = y - previous_y;

			// move the shape by that amount
			selectedShape.setX_coord(selectedShape.getX_coord() + dist_x);
			selectedShape.setY_coord(selectedShape.getY_coord() + dist_y);

			previous_x = x;
			previous_y = y;

			repaint();

		}
		// Mode : Draw Shape
		else if (mode == DRAWING) {
	
			if (event.getX() >= 0 && event.getX() < FRAME_WIDTH
					&& event.getY() >= 0 && event.getY() < FRAME_HEIGHT) {

				DrawingShape temp = displayList.get(numShapes - 1);

				int endX = event.getX();

				int endY = event.getY();

				int width = endX - startX;
				int height = endY - startY;

				if (drawShapeInt != LINE) {

					if (width < 0) {

						temp.setX_coord(endX);

					}

					if (height < 0) {

						temp.setY_coord(endY);

					}
				}
				temp.setHeight(height);
				temp.setWidth(width);
				temp.setFill(fill);
				temp.setStroke(stroke);

				repaint();
			}
			// }
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent event) {

		if (event.getKeyChar() == '0') {

			drawShapeInt = RECTANGLE;

		} else if (event.getKeyChar() == '1') {

			drawShapeInt = OVAL;

		} else if (event.getKeyChar() == '2') {

			drawShapeInt = LINE;

		} else if (event.getKeyChar() == '3') {

			drawShapeInt = TRIANGLE;

		} else if (event.getKeyChar() == '4') {

			

			printCode();

		} else if (event.getKeyChar() == '-') {

			if (selectedShape != null) {

				selectedShape.shrink();
				repaint();
			}
		} else if (event.getKeyChar() == '=') {

			if (selectedShape != null) {

				selectedShape.expand();
				repaint();
			}
		} else if (event.getKeyCode() == KeyEvent.VK_UP) {

			this.changeShapePosition(1);

		} else if (event.getKeyCode() == KeyEvent.VK_DOWN) {

			this.changeShapePosition(-1);

		}
		// ctrl + c gives copy option
		else if ((event.getKeyCode() == KeyEvent.VK_C)
				&& ((event.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

			mode = COPY;

			copyShape();

		}
		else if ((event.getKeyCode() == KeyEvent.VK_V)
				&& ((event.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

			mode = PASTE;


		} else if (event.getKeyChar() == 's') {

			stroke = true;

			if (selectedShape != null) {
				selectedShape.setStroke(stroke);
			}

		} else if (event.getKeyChar() == 'z') {

			if (selectedShape != null && selectedShape.isStroked()) {

				if (strokeSize > MIN_STROKE_SIZE) {
					strokeSize--;

					selectedShape.setStrokeSize(strokeSize);
				}
			}

		} else if (event.getKeyChar() == 'x') {

			if (selectedShape != null && selectedShape.isStroked()) {

				if (strokeSize < MAX_STROKE_SIZE) {
					strokeSize++;

					selectedShape.setStrokeSize(strokeSize);
				}
			}

		} else if (event.getKeyChar() == 'd') {

			mode = DRAWING;

		} else if (event.getKeyChar() == 'q') {

			mode = SELECTION;

		} else if (event.getKeyCode() == KeyEvent.VK_DELETE) {

			deleteShape();
		} else if ((event.getKeyCode() == KeyEvent.VK_RIGHT)) {

			if (selectedShape != null) {

				selectedShape.rotateClockwise();

			}

		} else if ((event.getKeyCode() == KeyEvent.VK_LEFT)) {

			if (selectedShape != null) {

				selectedShape.rotateAntiClockwise();

			}

		}

		repaint();

	}

	@Override
	public void keyReleased(KeyEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent event) {
		// TODO Auto-generated method stub

	}

	public boolean selectShape(MouseEvent event) {

		int x = event.getX();

		int y = event.getY();

		// want to select the shape at the top so start looking from the end of
		// the display list
		for (int i = numShapes - 1; i >= 0; i--) {

			if (displayList.get(i).contains(x, y)) {

				if (selectedShape != null) {
					selectedShape.setSelected(false);
				}

				selectedShape = displayList.get(i);

				// bring current shape to the front by removing it and
				// adding it to the back of the list
				displayList.remove(selectedShape);
				displayList.add(selectedShape);

				selectedShape.setSelected(true);
				
				

				repaint();

				return true;

			}
		}

		// otherwise, clicked on white space

		if (selectedShape != null) {
			selectedShape.setSelected(false);
		}

		selectedShape = null;
		
		repaint() ;

		return false;

	}

	/**
	 * Switches the two shapes in the tw0 given indices in the display list
	 * 
	 * @param index1
	 * @param index2
	 */
	private void switchShapes(int index1, int index2) {

		// Do nothing if the indices are invalid
		if (index1 < 0 || index1 >= numShapes || index2 < 0
				|| index2 >= numShapes) {

			return;
		}

		// switch the two elements
		DrawingShape temp = displayList.get(index1);
		displayList.set(index1, displayList.get(index2));
		displayList.set(index2, temp);

		repaint();

	}

	/**
	 * Changes the position of the shape in the display list
	 * 
	 * @param i
	 *            If i is less than 0, shift the shape down the display list.
	 *            Otherwise shift it up the display list
	 */
	public void changeShapePosition(int i) {

		if (mode == SELECTION) {

			if (selectedShape != null) {

				int index = displayList.indexOf(selectedShape);

				if (i < 0) {

					switchShapes(index, index - 1);

				} else {

					switchShapes(index, index + 1);
				}
			}
		}
	}

	public void copyShape() {

		if (selectedShape == null) {

			return;
		}

		shapeToPaste = selectedShape.copy();

	}

	/**
	 * Pastes the shape
	 * 
	 * @param event
	 */
	public void pasteShape() {

		if (shapeToPaste == null) {

			return;
		}

		DrawingShape temp = shapeToPaste.copy();

		temp.setX_coord(pasteAtEvent.getX() - temp.getWidth() / 2);
		temp.setY_coord(pasteAtEvent.getY() - temp.getHeight() / 2);
		temp.setSelected(false);
		displayList.add(temp);
		numShapes++;

	}

	/**
	 * this is the main method this creates the user interface by creating the
	 * JFrame
	 **/
	public void printCode() {

		codeStringBuilder = new StringBuilder("");
		
		try {
			System.setOut(new PrintStream(new FileOutputStream("output.pde")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		System.out.println("size(" + FRAME_WIDTH + "," + FRAME_HEIGHT + ");");
		System.out.println("background(255);");
		System.out.println("smooth();");

		codeStringBuilder.append("size(" + FRAME_WIDTH + "," + FRAME_HEIGHT
				+ ");\n");
		codeStringBuilder.append("background(255);\n");
		codeStringBuilder.append("smooth();\n");
		
		
		for (int i = 0; i < numShapes; i++) {
			DrawingShape shape = displayList.get(i);
			DrawingShape prev_s = null;
			Color colorPrevShape = null;
			if (i > 0) {
				prev_s = displayList.get(i - 1);
				colorPrevShape = prev_s.getColor();
			}
			Color colorOfShape = shape.getColor();
			Color colorOfStroke = shape.getStrokeColor();
			int strokeWeight = shape.getStrokeSize() / 2;
			int initialX = shape.getX_coord();
			int initialY = shape.getY_coord();
			int height = shape.getHeight();
			int width = shape.getWidth();
			double angle = shape.getRotate();
			// Rectangles
			
			commonCode(shape, prev_s);


			if (shape.getClass().toString().equals("class Rectangles")) {

				if (angle != 0) {
					System.out.println("pushMatrix();");
					codeStringBuilder.append("pushMatrix();\n");
					System.out.println("translate(" + initialX + "," + initialY
							+ ");");
					codeStringBuilder.append("translate(" + initialX + ","
							+ initialY + ");\n");

					System.out.println("translate(" + width / 2 + "," + height
							/ 2 + ");");
					codeStringBuilder.append("translate(" + width / 2 + ","
							+ height / 2 + ");\n");

					System.out.println("rotate(" + shape.getRotate() + ");");
					codeStringBuilder.append("rotate(" + shape.getRotate()
							+ ");\n");

					System.out
							.println("rect(" + -(width / 2) + ","
									+ -(height / 2) + "," + width + ","
									+ height + ");");
					codeStringBuilder.append("rect(" + -(width / 2) + ","
							+ -(height / 2) + "," + width + "," + height
							+ ");\n");
					System.out.println("popMatrix();");
					codeStringBuilder.append("popMatrix();\n");
				} else if (angle == 0) {
					System.out.println("rect(" + initialX + "," + initialY
							+ "," + width + "," + height + ");");
					codeStringBuilder.append("rect(" + initialX + ","
							+ initialY + "," + width + "," + height + ");\n");
				}
			}
			// Oval
			if (shape.getClass().toString().equals("class Oval")) {

				if (angle != 0) {
					System.out.println("pushMatrix();");
					codeStringBuilder.append("pushMatrix();\n");
					System.out.println("translate(" + initialX + "," + initialY
							+ ");");
					codeStringBuilder.append("translate(" + initialX + ","
							+ initialY + ");\n");

					System.out.println("translate(" + width / 2 + "," + height
							/ 2 + ");");
					codeStringBuilder.append("translate(" + width / 2 + ","
							+ height / 2 + ");\n");
					System.out.println("rotate(" + shape.getRotate() + ");");
					codeStringBuilder.append("rotate(" + shape.getRotate()
							+ ");\n");
					if (stroke != true) {
						System.out.println("ellipse(" + 0 + "," + 0 + ","
								+ width + "," + height + ");");
						codeStringBuilder.append("ellipse(" + 0 + "," + 0 + ","
								+ width + "," + height + ");\n");
					} else if (stroke == true) {
						System.out.println("ellipse(" + 0 + "," + 0 + ","
								+ (strokeWeight + width) + ","
								+ (strokeWeight + height) + ");");
						codeStringBuilder.append("ellipse(" + 0 + "," + 0 + ","
								+ (strokeWeight + width) + ","
								+ (strokeWeight + height) + ");\n");

					}
					System.out.println("popMatrix();");
					codeStringBuilder.append("popMatrix();\n");
				} else if (angle == 0) {
					if (stroke == true) {
						System.out.println("ellipse(" + (initialX + width / 2)
								+ "," + (initialY + height / 2) + ","
								+ (strokeWeight + width) + ","
								+ (strokeWeight + height) + ");");
						codeStringBuilder.append("ellipse("
								+ (initialX + width / 2) + ","
								+ (initialY + height / 2) + ","
								+ (strokeWeight + width) + ","
								+ (strokeWeight + height) + ");\n");
					} else if (stroke == false) {
						System.out.println("ellipse(" + (initialX + width / 2)
								+ "," + (initialY + height / 2) + "," + (width)
								+ "," + (height) + ");");
						codeStringBuilder.append("ellipse("
								+ (initialX + width / 2) + ","
								+ (initialY + height / 2) + "," + (width) + ","
								+ (height) + ");\n");

					}

				}
			}

			// Triangle

			if (shape.getClass().toString().equals("class Triangle")) {
				int x1, y1, x2, y2, x3, y3;
				x1 = initialX + width / 2;
				y1 = initialY;
				x2 = initialX;
				y2 = initialY + height;
				x3 = initialX + width;
				y3 = initialY + height;

				if (angle != 0) {
					System.out.println("pushMatrix();");
					codeStringBuilder.append("pushMatrix();\n");

					System.out.println("translate(" + initialX + "," + initialY
							+ ");");
					codeStringBuilder.append("translate(" + initialX + ","
							+ initialY + ");\n");

					System.out.println("translate(" + width / 2 + "," + height
							/ 2 + ");");
					codeStringBuilder.append("translate(" + width / 2 + ","
							+ height / 2 + ");\n");
					System.out.println("rotate(" + shape.getRotate() + ");");
					codeStringBuilder.append("rotate(" + shape.getRotate()
							+ ");\n");
					System.out.println("triangle(" + 0 + "," + (-height / 2)
							+ "," + (-width / 2) + "," + (height / 2) + ","
							+ (width / 2) + "," + (height / 2) + ");");
					codeStringBuilder.append("triangle(" + 0 + ","
							+ (-height / 2) + "," + (-width / 2) + ","
							+ (height / 2) + "," + (width / 2) + ","
							+ (height / 2) + ");\n");
					System.out.println("popMatrix();");
					codeStringBuilder.append("popMatrix();\n");
				} else if (angle == 0) {
					System.out.println("triangle(" + x1 + "," + y1 + "," + x2
							+ "," + y2 + "," + x3 + "," + y3 + ");");
					codeStringBuilder.append("triangle(" + x1 + "," + y1 + ","
							+ x2 + "," + y2 + "," + x3 + "," + y3 + ");\n");
				}
				// Line
			} else if (shape.getClass().toString().equals("class Line")) {
				if (colorOfStroke.getRed() == colorOfStroke.getBlue()
						&& colorOfStroke.getRed() == colorOfStroke.getGreen()) {
					System.out.println("stroke(" + colorOfStroke.getRed()
							+ ");");
					codeStringBuilder.append("stroke(" + colorOfStroke.getRed()
							+ ");\n");
				} else {
					System.out.println("stroke(" + colorOfStroke.getRed() + ","
							+ colorOfStroke.getGreen() + ","
							+ colorOfStroke.getBlue() + ");");
					codeStringBuilder.append("stroke(" + colorOfStroke.getRed()
							+ "," + colorOfStroke.getGreen() + ","
							+ colorOfStroke.getBlue() + ");\n");
				}
				System.out.println("strokeWeight(" + strokeWeight + ");");
				codeStringBuilder.append("strokeWeight(" + strokeWeight
						+ ");\n");
				if (angle != 0) {

					System.out.println("pushMatrix();");
					codeStringBuilder.append("pushMatrix();\n");
					System.out.println("translate(" + initialX + "," + initialY
							+ ");");
					codeStringBuilder.append("translate(" + initialX + ","
							+ initialY + ");\n");

					System.out.println("translate(" + width / 2 + "," + height
							/ 2 + ");");
					codeStringBuilder.append("translate(" + width / 2 + ","
							+ height / 2 + ");\n");
					System.out.println("rotate(" + shape.getRotate() + ");");
					codeStringBuilder.append("rotate(" + shape.getRotate()
							+ ");\n");
					System.out.println("line(" + -width / 2 + "," + -height / 2
							+ "," + width / 2 + "," + height / 2 + ");");
					codeStringBuilder.append("line(" + -width / 2 + ","
							+ -height / 2 + "," + width / 2 + "," + height / 2
							+ ");\n");
					System.out.println("popMatrix();");
					codeStringBuilder.append("popMatrix();\n");

				} else if (angle == 0) {
					System.out.println("line(" + initialX + "," + initialY
							+ "," + (initialX + width) + ","
							+ (initialY + height) + ");");
					codeStringBuilder.append("line(" + initialX + ","
							+ initialY + "," + (initialX + width) + ","
							+ (initialY + height) + ");\n");

				}
			}
			// }
		}
	}

	private void commonCode(DrawingShape currentShape, DrawingShape previousShape){
		if(currentShape.isFill() && currentShape.isStroked()){
			if (currentShape.getStrokeColor().getRed() == currentShape.getStrokeColor().getBlue()
					&& currentShape.getStrokeColor().getRed() == currentShape.getStrokeColor()
							.getGreen()) {
				System.out.println("stroke("
						+ currentShape.getStrokeColor().getRed() + ");");
				codeStringBuilder.append("stroke("
						+ currentShape.getStrokeColor().getRed() + ");\n");
				
				System.out.println("strokeWeight(" + currentShape.getStrokeSize()
						+ ");");
				codeStringBuilder.append("strokeWeight("
						+ currentShape.getStrokeSize() + ");\n");
			} else {
				System.out.println("stroke("
						+ currentShape.getStrokeColor().getRed() + ","
						+ currentShape.getStrokeColor().getGreen() + ","
						+ currentShape.getStrokeColor().getBlue() + ");");
				codeStringBuilder.append("stroke("
						+ currentShape.getStrokeColor().getRed() + ","
						+ currentShape.getStrokeColor().getGreen() + ","
						+ currentShape.getStrokeColor().getBlue() + ");\n");
				
				System.out.println("strokeWeight(" + currentShape.getStrokeSize()
						+ ");");
				codeStringBuilder.append("strokeWeight("
						+ currentShape.getStrokeSize() + ");\n");
			}
			if (currentShape.getColor().getRed() == currentShape.getColor().getBlue()
					&& currentShape.getColor().getRed() == currentShape.getColor()
							.getGreen()) {
				System.out.println("fill("
						+ currentShape.getColor().getRed() + ");");
				codeStringBuilder.append("fill("
						+ currentShape.getColor().getRed() + ");\n");
			} else {
				System.out.println("fill"
						+ currentShape.getColor().getRed() + ","
						+ currentShape.getColor().getGreen() + ","
						+ currentShape.getColor().getBlue() + ");");
				codeStringBuilder.append("fill("
						+ currentShape.getColor().getRed() + ","
						+ currentShape.getColor().getGreen() + ","
						+ currentShape.getColor().getBlue() + ");\n");
			}
				
			
		}
		
		else if(currentShape.isFill() && !currentShape.isStroked()){
			
				System.out.println("noStroke()");
				codeStringBuilder.append("noStroke();\n");
			
			if (currentShape.getColor().getRed() == currentShape.getColor().getBlue()
					&& currentShape.getColor().getRed() == currentShape.getColor()
							.getGreen()) {
				System.out.println("fill("
						+ currentShape.getColor().getRed() + ");");
				codeStringBuilder.append("fill("
						+ currentShape.getColor().getRed() + ");\n");
			} else {
				System.out.println("fill"
						+ currentShape.getColor().getRed() + ","
						+ currentShape.getColor().getGreen() + ","
						+ currentShape.getColor().getBlue() + ");");
				codeStringBuilder.append("fill("
						+ currentShape.getColor().getRed() + ","
						+ currentShape.getColor().getGreen() + ","
						+ currentShape.getColor().getBlue() + ");\n");
			}
			
		}else if(!currentShape.isFill() && currentShape.isStroked()){
			if (currentShape.getStrokeColor().getRed() == currentShape.getStrokeColor().getBlue()
					&& currentShape.getStrokeColor().getRed() == currentShape.getStrokeColor()
							.getGreen()) {
				System.out.println("stroke("
						+ currentShape.getStrokeColor().getRed() + ");");
				codeStringBuilder.append("stroke("
						+ currentShape.getStrokeColor().getRed() + ");\n");
				System.out.println("strokeWeight(" + currentShape.getStrokeSize()
						+ ");");
				codeStringBuilder.append("strokeWeight("
						+ currentShape.getStrokeSize() + ");\n");
			} 
			else {
				System.out.println("stroke("
						+ currentShape.getStrokeColor().getRed() + ","
						+ currentShape.getStrokeColor().getGreen() + ","
						+ currentShape.getStrokeColor().getBlue() + ");");
				codeStringBuilder.append("stroke("
						+ currentShape.getStrokeColor().getRed() + ","
						+ currentShape.getStrokeColor().getGreen() + ","
						+ currentShape.getStrokeColor().getBlue() + ");\n");
				
				System.out.println("strokeWeight(" + currentShape.getStrokeSize()
						+ ");");
				codeStringBuilder.append("strokeWeight("
						+ currentShape.getStrokeSize() + ");\n");
			}
			
				System.out.println("noFill();");
				codeStringBuilder.append("noFill();\n");
			
			
		}
		else if(!currentShape.isFill() && !currentShape.isStroked()){
			
			System.out.println("noStroke()");
			codeStringBuilder.append("noStroke();\n");
			
			System.out.println("noFill();");
			codeStringBuilder.append("noFill();\n");
			
		}
	}

	public String getCode() {

		// remove any shapes that has height = 0 or width = 0
		// only works if we start looking from the end of the display list
		// because each time a shape is deleted, all the shapes to after it
		// shifts to the left of the display list
		for (int i = numShapes - 1; i >= 0; i--) {

			DrawingShape shape = displayList.get(i);

			if (shape.getHeight() == 0 || shape.getWidth() == 0) {

				displayList.remove(shape);
				numShapes--;
			}
		}

		printCode();
		
		return codeStringBuilder.toString();
	}

	public void setDrawShapeInt(int x) {
		drawShapeInt = x;
	}

	public DrawingShape getSelectedShape() {
		return selectedShape;
	}

	public void clearAll() {

		numShapes = 0;
		displayList = new ArrayList<DrawingShape>();

		first_time = true;

		mode = DRAWING;

		drawShapeInt = RECTANGLE;

		stroke = false;
		strokeCount = 0;
		strokeSize = 2;

		repaint();

	}

	public void changeMode(int mode) {

		this.mode = mode;

		if (mode == DRAWING) {

			if(selectedShape != null){
				
				selectedShape.setSelected(false) ;
			}
			selectedShape = null;
		}

		repaint();
	}

	public String getMode() {
		if (mode == 0) {
			return ("Drawing");

		} /*else if (mode == 1 || mode == 3 || mode == 4) {
			return ("Selection");

		}*/
		return null;
	}

	public void deleteShape() {

		if (selectedShape != null) {

			displayList.remove(selectedShape);
			selectedShape = null;
			numShapes--;
		}
	}

	public void setStrokeSize(int strokeSize) {
		this.strokeSize = strokeSize;
	}

	public void drawShape(MouseEvent event) {

		if (event.getButton() == MouseEvent.BUTTON1) {

			startX = event.getX();
			startY = event.getY();

			if (startX >= 0 && startX < dimension.getWidth() && startY >= 0
					&& startY < dimension.getHeight()) {

				if (drawShapeInt == RECTANGLE) {

					DrawingShape temp = new Rectangles(startX, startY, 0, 0,
							shapeColor, strokeColor, strokeSize);
					displayList.add(temp);
					numShapes++;
					repaint();

				} else if (drawShapeInt == OVAL) {

					DrawingShape temp = new Oval(startX, startY, 0, 0,
							shapeColor, strokeColor, strokeSize);
					displayList.add(temp);
					numShapes++;
					repaint();

				} else if (drawShapeInt == LINE) {

					DrawingShape temp = new Line(startX, startY, 0, 0,
							strokeColor, strokeColor, strokeSize);
					displayList.add(temp);
					numShapes++;

					selectedShape = temp;
					repaint();

				} else if (drawShapeInt == TRIANGLE) {

					DrawingShape temp = new Triangle(startX, startY, 0, 0,
							shapeColor, strokeColor, strokeSize);
					displayList.add(temp);
					numShapes++;
					repaint();
				}
			}
		}

	}

	public void setFill(boolean fill) {

		this.fill = fill;

		if (selectedShape != null) {

			selectedShape.setFill(fill);

		}
	}

	public void setStroked(boolean stroked) {

		this.stroke = stroked;

		if (selectedShape != null) {

			selectedShape.setStroke(stroked);
		}
	}

	public void setFillColor(Color fillColor) {

		shapeColor = fillColor;

		if (selectedShape != null) {

			selectedShape.setColor(fillColor);
		}
	}

	public void setStrokeColor(Color strokeColor) {

		this.strokeColor = strokeColor;

		if (selectedShape != null) {

			selectedShape.setStrokeColor(strokeColor);
		}
	}
	

	
}
