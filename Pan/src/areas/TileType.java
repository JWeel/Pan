package areas;

import repository.GraphicsManager;

//import java.awt.Image;
//import java.util.Random;
//
//import javax.swing.ImageIcon;

//
public class TileType {
	
	
	static Tile TESTBLOCK1(){
		
		return new Tile(GraphicsManager.tileTestBlock1, null);
		
	}
	
	
	static Tile GRASS1(){
		
		return new Tile(GraphicsManager.tileGrass1, null);
		
	}
	
	
	
	
//	public Image img;
//	public boolean collision = false;
	
//	public Tile(){
//		int ran = new Random().nextInt(3);
//		if (ran == 1)
//		img = new ImageIcon("src/art/grass2.png").getImage();
//		else if (ran == 2)
//		img = new ImageIcon("src/art/grass1.png").getImage();
//		else img = new ImageIcon("src/art/grass3.png").getImage();
//		
//	}
}

//class grass1 extends TileType{
//	img = new ImageIcon("src/art/grass1.png".getImage());
//}