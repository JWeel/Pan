package areas;

import java.awt.image.BufferedImage;

import repository.GraphicsManager;

public class Obstacle {

	
	public BufferedImage obstacleImage = null; // collision is checked by (img != null)

	public boolean isMovable;
	
	public int x, y;

	
	public Obstacle() {

		
		this.obstacleImage = GraphicsManager.objectRock1;
				
		this.isMovable = true;
		
		
	}

}
