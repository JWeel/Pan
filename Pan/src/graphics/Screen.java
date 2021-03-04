package graphics;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import foundation.KeyHandler;

//
public class Screen extends JFrame {

	private static final long serialVersionUID = 8149185007544249557L;
	
	public static final int VIEWABLE_COLUMNS = 16;
	public static final int VIEWABLE_ROWS = 12;

	public final Painter painter;
	public final Scroller scroller;
	
	public Screen(String windowName){
		// creates default JFrame object
		super(windowName);

		// when the red x at top right of window is clicked, program ends
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// whether the screen can be resized. 
		// TODO will need to make method to change pixel multiplier if enable resize
		this.setResizable(false);
		
		//DEBUG set start location of window
		this.setLocation(1000, 300);
		
		// sets black background for in rare case when area is smaller than window
		/* Note: because of how scroll pane is set up, this has ugly result
		with actual area being in top left and black being right and bottom border
		So really, area should always be at least 16 by 12. */
		this.setBackground(Color.BLACK);

		// object that paints everything in the window
		painter = new Painter();
		painter.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));

		// object that handles key-bindings
		painter.keyInput = new KeyHandler(painter);

		// painter is added into scroll pane
		scroller = new Scroller(painter);

		// scroll bars are set to never be shown and border is removed
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scroller.setBorder(null);
		
		// the scroll pane and the painter inside it is added to the window
		this.add(scroller);

		// makes the window the correct size and displays it
		this.pack();
		this.setVisible(true);
	}
}


