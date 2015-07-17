import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ColorPalette extends JComponent {

	private static final int COLUMNS = 64;
	private static final int ROWS = 64;
	private static final int CONSTANT = 8;

	// private JPanel[][] colorBoxes;

	public ColorPalette() {

		this.setLayout(new GridLayout(ROWS, COLUMNS));

		// colorBoxes = new JPanel[ROWS][COLUMNS];

		createPalette();

		// findF() ;
	}

	private void createPalette() {

	}

	public void createPanel(Container contentPane) {

		contentPane.add(this);
	}

	/*
	 * private void createPalette(){
	 * 
	 * for(int r = 0 ; r < 16 ; r++){
	 * 
	 * for(int g = 0 ; g < 16 ; g++){
	 * 
	 * for(int b = 0 ; b < 16 ; b++){
	 * 
	 * JPanel colorPanel = new JPanel(new BorderLayout()); Border border =
	 * BorderFactory.createLineBorder(Color.BLACK, 0);
	 * colorPanel.setBorder(border); colorPanel.setBackground(new
	 * Color(r*16,g*16,b*16)); this.add(colorPanel); } } }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * private void createPalette() {
	 * 
	 * for (int row = 0; row < ROWS; row++) {
	 * 
	 * for (int col = 0; col < COLUMNS; col++) {
	 * 
	 * colorBoxes[row][col] = new JPanel(); colorBoxes[row][col].setLayout(new
	 * BorderLayout()); // Create a black border around the canvas Border border
	 * = BorderFactory.createLineBorder(Color.BLACK, 0);
	 * colorBoxes[row][col].setBorder(border); this.add(colorBoxes[row][col]);
	 * 
	 * Color color;
	 * 
	 * if (col < COLUMNS / 2) { color = new Color((col * 8), (row * 8), (col *
	 * 8)); } else { color = new Color(((COLUMNS-col) * 8) % ROWS, ((ROWS-row) *
	 * 8) % ROWS, (col * 8) % ROWS);
	 * 
	 * }
	 * 
	 * 
	 * if (col < COLUMNS/2) {
	 * 
	 * //color = new Color(col * 8, row * 8, col * 8);
	 * 
	 * color = new Color(col*8 , row*8 , (((COLUMNS-col)*8)%ROWS)) ;
	 * //colorBoxes[row][col].setForeground(color);
	 * 
	 * } else {
	 * 
	 * //color = new Color(col * 4, row * 8, (col % 32) * 8);
	 * 
	 * color = new Color((((COLUMNS-col)*8)%ROWS) , (((ROWS-row)*8)%ROWS) ,
	 * (((col%ROWS))*8)) ; //colorBoxes[row][col].setForeground(color); }
	 * 
	 * 
	 * 
	 * colorBoxes[row][col].setBackground(color);
	 * 
	 * } } }
	 * 
	 * 
	 * public void findF(){
	 * 
	 * for(int i = 1; i < (32*32*32)/2 ; i++){
	 * 
	 * if((32*32*32)%i==0){
	 * 
	 * System.out.println(i);
	 * 
	 * } } } public void createPanel(Container contentPane) {
	 * 
	 * contentPane.add(this); }
	 */

	/**
	 * this is the main method this creates the user interface by creating the
	 * JFrame
	 **/
	public static void main(String[] args) {

		// Create the frame, give it a title, and set its size.
		JFrame f = new JFrame();
		f.setTitle("Code My Thing");
		f.setSize(new Dimension(1100, 800));
		f.setResizable(false);

		// Create the panel that will display the image and text
		ColorPalette codeMyThing = new ColorPalette();

		codeMyThing.createPanel(f.getContentPane());

		// Display the window
		f.setVisible(true);
	}

}
