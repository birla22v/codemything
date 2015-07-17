import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.java.dev.colorchooser.ColorChooser;

public class CodeMyThing implements ActionListener, PropertyChangeListener,
		ChangeListener, MouseListener, ItemListener {

	// Shapes to Draw
	private static final int RECTANGLE = 0;

	private static final int OVAL = 1;

	private static final int LINE = 2;

	private static final int TRIANGLE = 3;

	// Modes
	private static final int DRAWING = 1;

	private static final int SELECTION = 2;

	private static final int COPY = 3;

	private static final int PASTE = 4;

	static final int STROKE_MIN = 0;
	static final int STROKE_MAX = 30;
	static final int STROKE_INIT = 15;

	private static final Color BG = new Color(175, 238, 238);

	private ColorChooser colorChooser;
	private ColorChooser strokeColorChooser;
	private Color fillColor;
	private Color strokeColor;
	private int strokeSize;

	private Canvas canvas;

	private JTextArea codeArea;
	private JSlider strokeSlider;

	private static final Color CODE_COLOR = new Color(8, 106, 135);

	private JButton codeButton, deleteButton, rectButton, ovalButton,
			lineButton, triangleButton, rotateCounterClockwiseButton,
			rotateClockwiseButton, expandButton, shrinkButton, moveUpButton,
			moveDownButton, clearAllButton, selectButton, copyButton,
			pasteButton, prevButton, currentMode ;
	

	private JScrollPane scrollPane;

	private JCheckBox fillCheckBox;

	private JCheckBox strokeCheckBox;

	public CodeMyThing() {

		canvas = new Canvas();

		codeArea = new JTextArea();

		Font font = new Font("Jubilat", Font.BOLD, 14);
		codeArea.setFont(font);
		codeArea.setForeground(CODE_COLOR);
		codeArea.setEditable(false);
	
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(codeArea);

		fillColor = canvas.getShapeColor();
		strokeColor = canvas.getStrokeColor();
		strokeSize = canvas.getStrokeSize();
		canvas.addMouseListener(this);

	}

	public void createPanel(Container contentPane) {

		contentPane.add(canvas, BorderLayout.CENTER);

		contentPane.add(createButtonPanel(), BorderLayout.SOUTH);

		contentPane.add(createCodePanel(), BorderLayout.EAST);

	}

	public JPanel createCodePanel() {

		JPanel codePanel = new JPanel();

		codeArea.setColumns(25);

		codePanel.setLayout(new BorderLayout());

		// Create a black border around the canvas
		Border border = BorderFactory.createLineBorder(BG, 10);
		codePanel.setBorder(border);

		codeButton = new JButton("Code My Thing!!!");
		codeButton.setFocusable(false);

		codeButton.addActionListener(this);

		codePanel.add(codeButton, BorderLayout.SOUTH);

		codePanel.add(scrollPane, BorderLayout.CENTER);


		return codePanel;
	}

	public JPanel createButtonPanel() {

		JPanel buttonPanel = new JPanel(new GridLayout(2, 1));

		JPanel topButtonPanel = new JPanel();
		topButtonPanel.setLayout(new FlowLayout());
		buttonPanel.add(topButtonPanel);

		topButtonPanel.setBackground(BG);

		JPanel bottomButtonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(bottomButtonPanel);

		bottomButtonPanel.setBackground(BG);

		// For every button, setFocusable is set to false so that focus can go
		// back to the keyboard after a button has been pressed
		rectButton = new JButton();
		rectButton.setFocusable(false);
		try {
			Image img = ImageIO.read(getClass().getResource("rectangle.gif"));
			rectButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(rectButton);
		rectButton.setOpaque(false);
		rectButton.setContentAreaFilled(false);
		rectButton.setBorderPainted(false);
		rectButton.addActionListener(this);
		rectButton.setPreferredSize(new Dimension(40, 40));
		rectButton.setToolTipText("Draw Rectangle");

		ovalButton = new JButton();
		ovalButton.setFocusable(false);
		ovalButton.setOpaque(false);
		ovalButton.setContentAreaFilled(false);
		ovalButton.setBorderPainted(false);
		ovalButton.setPreferredSize(new Dimension(40, 40));
		try {
			Image img = ImageIO.read(getClass().getResource("oval.gif"));
			ovalButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(ovalButton);
		ovalButton.addActionListener(this);
		ovalButton.setToolTipText("Draw Oval");

		triangleButton = new JButton();
		triangleButton.setFocusable(false);
		triangleButton.setOpaque(false);
		triangleButton.setContentAreaFilled(false);
		triangleButton.setBorderPainted(false);
		triangleButton.setPreferredSize(new Dimension(40, 40));

		try {
			Image img = ImageIO.read(getClass().getResource("triangle.gif"));
			triangleButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(triangleButton);
		triangleButton.addActionListener(this);
		triangleButton.setToolTipText("Draw Triangle");

		lineButton = new JButton();
		lineButton.setFocusable(false);
		lineButton.setOpaque(false);
		lineButton.setContentAreaFilled(false);
		lineButton.setBorderPainted(false);
		lineButton.setPreferredSize(new Dimension(40, 40));
		lineButton.setToolTipText("Draw line");
		try {
			Image img = ImageIO.read(getClass().getResource("line.gif"));
			lineButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(lineButton);
		lineButton.addActionListener(this);

		addPanels(7, topButtonPanel);

		rotateClockwiseButton = new JButton();
		rotateClockwiseButton.setFocusable(false);
		rotateClockwiseButton.setOpaque(false);
		rotateClockwiseButton.setContentAreaFilled(false);
		rotateClockwiseButton.setBorderPainted(false);
		rotateClockwiseButton.setPreferredSize(new Dimension(40, 40));
		rotateClockwiseButton.setToolTipText("Rotate clockwise");
		try {
			Image img = ImageIO.read(getClass().getResource(
					"rotateClockwise.gif"));
			rotateClockwiseButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(rotateClockwiseButton);
		rotateClockwiseButton.addActionListener(this);

		rotateCounterClockwiseButton = new JButton();
		rotateCounterClockwiseButton.setFocusable(false);
		rotateCounterClockwiseButton.setOpaque(false);
		rotateCounterClockwiseButton.setContentAreaFilled(false);
		rotateCounterClockwiseButton.setBorderPainted(false);
		rotateCounterClockwiseButton.setPreferredSize(new Dimension(40, 40));
		rotateCounterClockwiseButton.setToolTipText("Rotate counter-clockwise");
		try {
			Image img = ImageIO.read(getClass().getResource(
					"rotateCounterClockwise.gif"));
			rotateCounterClockwiseButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(rotateCounterClockwiseButton);
		rotateCounterClockwiseButton.addActionListener(this);

		expandButton = new JButton();
		expandButton.setFocusable(false);
		expandButton.setOpaque(false);
		expandButton.setContentAreaFilled(false);
		expandButton.setBorderPainted(false);
		expandButton.setPreferredSize(new Dimension(40, 40));
		expandButton.setToolTipText("Increase size");
		try {
			Image img = ImageIO.read(getClass().getResource("bigger.gif"));
			expandButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(expandButton);
		expandButton.addActionListener(this);

		shrinkButton = new JButton();
		shrinkButton.setFocusable(false);
		shrinkButton.setOpaque(false);
		shrinkButton.setContentAreaFilled(false);
		shrinkButton.setBorderPainted(false);
		shrinkButton.setPreferredSize(new Dimension(40, 40));
		shrinkButton.setToolTipText("Decrease size");
		try {
			Image img = ImageIO.read(getClass().getResource("smaller.gif"));
			shrinkButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(shrinkButton);
		shrinkButton.addActionListener(this);

		moveUpButton = new JButton();
		moveUpButton.setFocusable(false);
		moveUpButton.setOpaque(false);
		moveUpButton.setContentAreaFilled(false);
		moveUpButton.setBorderPainted(false);
		moveUpButton.setPreferredSize(new Dimension(40, 40));
		moveUpButton.setToolTipText("Send to front");
		try {
			Image img = ImageIO.read(getClass().getResource("sendToFront.gif"));
			moveUpButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(moveUpButton);
		moveUpButton.addActionListener(this);

		moveDownButton = new JButton();
		moveDownButton.setFocusable(false);
		moveDownButton.setOpaque(false);
		moveDownButton.setContentAreaFilled(false);
		moveDownButton.setBorderPainted(false);
		moveDownButton.setPreferredSize(new Dimension(40, 40));
		moveDownButton.setToolTipText("Send to back");
		try {
			Image img = ImageIO.read(getClass().getResource("sendToBack.gif"));
			moveDownButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(moveDownButton);
		moveDownButton.addActionListener(this);



		addPanels(6, topButtonPanel);

		selectButton = new JButton();
		selectButton.setFocusable(false);
		selectButton.setOpaque(false);
		selectButton.setContentAreaFilled(false);
		selectButton.setBorderPainted(false);
		selectButton.setPreferredSize(new Dimension(40, 40));
		selectButton
				.setToolTipText("Selection Tool; \n Right-click to enter drawing mode");
		try {
			Image img = ImageIO.read(getClass().getResource("select.gif"));
			selectButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(selectButton);
		selectButton.addActionListener(this);

		copyButton = new JButton("Copy");
		copyButton.setFocusable(false);
		copyButton.setOpaque(false);
		copyButton.setContentAreaFilled(false);
		copyButton.setBorderPainted(false);
		copyButton.setPreferredSize(new Dimension(40, 40));
		copyButton.setToolTipText("Copies selected shape");
		try {
			Image img = ImageIO.read(getClass().getResource("copy.gif"));
			copyButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(copyButton);
		copyButton.addActionListener(this);

		pasteButton = new JButton("Paste");
		pasteButton.setFocusable(false);
		pasteButton.setOpaque(false);
		pasteButton.setContentAreaFilled(false);
		pasteButton.setBorderPainted(false);
		pasteButton.setPreferredSize(new Dimension(40, 40));
		pasteButton.setToolTipText("Pastes selected shape");
		try {
			Image img = ImageIO.read(getClass().getResource("paste.gif"));
			pasteButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}

		topButtonPanel.add(pasteButton);
		pasteButton.addActionListener(this);


		addPanels(6, topButtonPanel);

		deleteButton = new JButton();
		deleteButton.setFocusable(false);
		deleteButton.setOpaque(false);
		deleteButton.setContentAreaFilled(false);
		deleteButton.setBorderPainted(false);
		deleteButton.setPreferredSize(new Dimension(40, 40));
		try {
			Image img = ImageIO.read(getClass().getResource("trash.gif"));
			deleteButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(deleteButton);
		deleteButton.addActionListener(this);
		deleteButton.setToolTipText("Deletes selected shape");

		clearAllButton = new JButton();
		clearAllButton.setFocusable(false);
		clearAllButton.setOpaque(false);
		clearAllButton.setContentAreaFilled(false);
		clearAllButton.setBorderPainted(false);
		clearAllButton.setPreferredSize(new Dimension(40, 40));
		try {
			Image img = ImageIO.read(getClass().getResource("newDoc.gif"));
			clearAllButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		topButtonPanel.add(clearAllButton);
		clearAllButton.addActionListener(this);
		clearAllButton.setToolTipText("Starts over");


		currentMode = new JButton("Mode: Drawing");
		currentMode.setFocusable(false);
		currentMode.setOpaque(false);
		currentMode.setContentAreaFilled(false);
		currentMode.setBorderPainted(false);

		bottomButtonPanel.add(currentMode);

		addPanels(3, bottomButtonPanel);

		JLabel fillLabel = new JLabel("Fill: ");
		fillLabel.setFocusable(false);
		bottomButtonPanel.add(fillLabel);

		// Color Chooser
		colorChooser = new ColorChooser(fillColor);
		colorChooser.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
		colorChooser.setMinimumSize(new Dimension(40, 40));
		colorChooser.setMaximumSize(new Dimension(40, 40));
		colorChooser.setFocusable(false);
		colorChooser.addPropertyChangeListener(this);

		// hack to make place: add an empty JPanel
		// bottomButtonPanel.add(new JPanel());
		bottomButtonPanel.add(colorChooser);
		

		addPanels(3, bottomButtonPanel);

		JLabel strokeLabel = new JLabel("Border: ");
		strokeLabel.setFocusable(false);
		bottomButtonPanel.add(strokeLabel);

		// Color Chooser
		strokeColorChooser = new ColorChooser(strokeColor);
		strokeColorChooser.setBorder(BorderFactory.createLineBorder(
				Color.BLACK, 0));
		strokeColorChooser.setMinimumSize(new Dimension(40, 10));
		strokeColorChooser.setMaximumSize(new Dimension(40, 10));
		

		strokeColorChooser.setFocusable(false);
		strokeColorChooser.addPropertyChangeListener(this);

		// hack to make place: add an empty JPanel
	
		bottomButtonPanel.add(strokeColorChooser);

		addPanels(3, bottomButtonPanel);

		JLabel strokeSizeLabel = new JLabel("Border Size: ");
		strokeSizeLabel.setFocusable(false);
		bottomButtonPanel.add(strokeSizeLabel);

		strokeSlider = new JSlider(JSlider.HORIZONTAL, STROKE_MIN, STROKE_MAX,
				STROKE_INIT);

		strokeSlider.addChangeListener(this);
		strokeSlider.setPaintTicks(true);
		strokeSlider.setFocusable(false);
		bottomButtonPanel.add(strokeSlider);
		

		addPanels(3, bottomButtonPanel);

		fillCheckBox = new JCheckBox("Fill", true);
		fillCheckBox.setOpaque(false);
		fillCheckBox.addItemListener(this);
		fillCheckBox.setFocusable(false);
		bottomButtonPanel.add(fillCheckBox);

		addPanels(1, bottomButtonPanel);

		strokeCheckBox = new JCheckBox("Border", false);
		strokeCheckBox.setOpaque(false);
		strokeCheckBox.addItemListener(this);
		strokeCheckBox.setFocusable(false);
		bottomButtonPanel.add(strokeCheckBox);
	
		return buttonPanel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == codeButton) {

			codeArea.setText(canvas.getCode());
	
		} else if (event.getSource() == rectButton) {
			
			System.out.println("pressed on rect");
			currentMode.setText("Mode: Drawing");
			canvas.setDrawShapeInt(RECTANGLE);
			canvas.changeMode(DRAWING);
					
			if(prevButton != null){
				
				prevButton.setBorderPainted(false) ;
			}
			prevButton = rectButton;
			
			prevButton.setBorderPainted(true) ;

		} else if (event.getSource() == ovalButton) {
			currentMode.setText("Mode: Drawing");
			canvas.setDrawShapeInt(OVAL);
			canvas.changeMode(DRAWING);

			
			if(prevButton != null){
				
				prevButton.setBorderPainted(false) ;
			}
			prevButton = ovalButton;
			
			prevButton.setBorderPainted(true) ;

		} else if (event.getSource() == lineButton) {
			currentMode.setText("Mode: Drawing");
			canvas.setDrawShapeInt(LINE);
			canvas.changeMode(DRAWING);

			
			if(prevButton != null){
				
				prevButton.setBorderPainted(false) ;
			}
			prevButton = lineButton;
			
			prevButton.setBorderPainted(true) ;

		} else if (event.getSource() == triangleButton) {
			currentMode.setText("Mode: Drawing");

			canvas.setDrawShapeInt(TRIANGLE);
			canvas.changeMode(DRAWING);
			currentMode.setText("Mode: Drawing");

			
			if(prevButton != null){
				
				prevButton.setBorderPainted(false) ;
			}
			prevButton = triangleButton;
			
			prevButton.setBorderPainted(true) ;
			
		} else if (event.getSource() == rotateClockwiseButton) {


			if (canvas.getSelectedShape() != null) {

				canvas.getSelectedShape().rotateClockwise();
				
				if(prevButton != null){
					
					prevButton.setBorderPainted(false) ;
				}
				prevButton = rotateClockwiseButton;
				
				prevButton.setBorderPainted(true) ;

			}

		} else if (event.getSource() == rotateCounterClockwiseButton) {

			if (canvas.getSelectedShape() != null) {

				canvas.getSelectedShape().rotateAntiClockwise();
	
				if(prevButton != null){
					
					prevButton.setBorderPainted(false) ;
				}
				prevButton = rotateCounterClockwiseButton;
				
				prevButton.setBorderPainted(true) ;

			}
		} else if (event.getSource() == expandButton) {

			if (canvas.getSelectedShape() != null) {

				canvas.getSelectedShape().expand();

	if(prevButton != null){
					
					prevButton.setBorderPainted(false) ;
				}
				prevButton = expandButton;
				
				prevButton.setBorderPainted(true) ;

			}

		} else if (event.getSource() == shrinkButton) {

			if (canvas.getSelectedShape() != null) {

				canvas.getSelectedShape().shrink();

	if(prevButton != null){
					
					prevButton.setBorderPainted(false) ;
				}
				prevButton = shrinkButton;
				
				prevButton.setBorderPainted(true) ;

			}
	
		} else if (event.getSource() == moveUpButton) {

			canvas.changeShapePosition(1);

			if(prevButton != null){
				
				prevButton.setBorderPainted(false) ;
			}
			prevButton = moveUpButton;
			
			prevButton.setBorderPainted(true) ;


		} else if (event.getSource() == moveDownButton) {

			canvas.changeShapePosition(-1);

if(prevButton != null){
				
				prevButton.setBorderPainted(false) ;
			}
			prevButton = moveDownButton;
			
			prevButton.setBorderPainted(true) ;

			
		} else if (event.getSource() == deleteButton) {
		
			canvas.deleteShape();

if(prevButton != null){
				
				prevButton.setBorderPainted(false) ;
			}
			prevButton = deleteButton;
			
			prevButton.setBorderPainted(true) ;

		} else if (event.getSource() == clearAllButton) {
			currentMode.setText("Mode: Drawing");

			canvas.clearAll();
			codeArea.setText("");

if(prevButton != null){
				
				prevButton.setBorderPainted(false) ;
			}
			prevButton = clearAllButton;
			
			prevButton.setBorderPainted(true) ;

		} else if (event.getSource() == selectButton) {

			canvas.changeMode(SELECTION);
			currentMode.setText("Mode: Selection");

if(prevButton != null){
				
				prevButton.setBorderPainted(false) ;
			}
			prevButton = selectButton;

		} else if (event.getSource() == copyButton) {

			currentMode.setText("Mode: Copy");

			canvas.changeMode(COPY);

			canvas.copyShape();

if(prevButton != null){
				
				prevButton.setBorderPainted(false) ;
			}
			prevButton = copyButton;

		} else if (event.getSource() == pasteButton) {
			currentMode.setText("Mode: Paste");

			canvas.changeMode(PASTE);

			// canvas.pasteShape();

if(prevButton != null){
				
				prevButton.setBorderPainted(false) ;
			}
			prevButton = pasteButton;

		} 
		canvas.repaint();

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		fillColor = colorChooser.getColor();
		strokeColor = strokeColorChooser.getColor();


		if (evt.getSource() == colorChooser) {

			

			canvas.setFillColor(fillColor);

		} else if (evt.getSource() == strokeColorChooser) {

		
			canvas.setStrokeColor(strokeColor);
		}

		canvas.repaint();

	}

	@SuppressWarnings("deprecation")
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
		
			if (currentMode.getText().equals("Mode: Drawing")) {

		
				currentMode.setText("Mode: Selection");
			} else if (currentMode.getText().equals("Mode: Selection")) {
				currentMode.setText("Mode: Drawing");
				 }else if (currentMode.getText().equals("Mode: Copy")) {
				 currentMode.setText("Mode: Selection");
				 }else if (currentMode.getText().equals("Mode: Paste")) {
				 currentMode.setText("Mode: Selection");
			}

		}
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateChanged(ChangeEvent event) {

		if (event.getSource() == strokeSlider) {

			strokeSize = strokeSlider.getValue();

			canvas.setStrokeSize(strokeSize);

			DrawingShape selectedShape = canvas.getSelectedShape();
			;
			if (selectedShape != null) {

				selectedShape.setStrokeSize(strokeSize);

				canvas.repaint();
			}
		}

	}

	@Override
	public void itemStateChanged(ItemEvent event) {

		if (fillCheckBox.isSelected() && strokeCheckBox.isSelected()) {

			canvas.setFill(true);
			canvas.setFillColor(colorChooser.getColor());
			canvas.setStroked(true);
			canvas.setStrokeColor(strokeColorChooser.getColor());

		} else if (fillCheckBox.isSelected() && !strokeCheckBox.isSelected()) {

			canvas.setFill(true);
			canvas.setFillColor(colorChooser.getColor());
			canvas.setStroked(false);

		} else if (!fillCheckBox.isSelected() && strokeCheckBox.isSelected()) {

			canvas.setFill(false);
			canvas.setFillColor(strokeColorChooser.getColor());
			canvas.setStroked(true);
			canvas.setStrokeColor(strokeColorChooser.getColor());

		} else if (!fillCheckBox.isSelected() && !strokeCheckBox.isSelected()) {

			canvas.setFill(false);

			canvas.setStroked(false);

			Color tempFillColor = new Color(colorChooser.getColor().getRed(),
					colorChooser.getColor().getGreen(), colorChooser.getColor()
							.getBlue(), 0);

			Color tempStrokeColor = new Color(colorChooser.getColor().getRed(),
					colorChooser.getColor().getGreen(), colorChooser.getColor()
							.getBlue(), 0);

			canvas.setFillColor(tempFillColor);
			canvas.setStrokeColor(tempStrokeColor);

		}

		canvas.repaint();
	}

	private void addPanels(int numPanels, JPanel panel) {

		for (int i = 0; i < numPanels; i++) {

			JPanel tempPanel = new JPanel();
			tempPanel.setBackground(BG);
			panel.add(tempPanel);
		}
	}

	/**
	 * this is the main method this creates the user interface by creating the
	 * JFrame
	 **/
	public static void main(String[] args) {

		// Create the frame, give it a title, and set its size.
		JFrame frame = new JFrame();
		frame.setTitle("Code My Thing");
		frame.setSize(new Dimension(1100, 800));
		frame.setResizable(false);

		// Create the panel that will display the image and text
		CodeMyThing codeMyThing = new CodeMyThing();

		codeMyThing.createPanel(frame.getContentPane());

		// Display the window
		frame.setVisible(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
