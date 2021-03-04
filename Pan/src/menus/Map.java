package menus;

import java.awt.image.BufferedImage;

import foundation.Core;
import repository.GraphicsManager;

public class Map {
	
	// image that displays the world
	private BufferedImage currentWorldMap = GraphicsManager.worldMap1;
	
	// half the size of the map cursor
	private int halfCursorSize;
	
	// coordinates of cursor on map
	public int x, y; 
	
	//
	public double multiplier;
	
	//
	public BufferedImage getMapImage(){
		return currentWorldMap;
	}
	
	//
	public BufferedImage getMapCursorImage(){
		return GraphicsManager.worldMapCursor1;
	}
	
	
	
	
	// moves the world map view coordinates
	// TODO and adds visited cities + names? character current location? animation?
	public void moveMap(){
		
//		// starts new animation thread if none exists
//		if (animator == null) {
//			startAnimation();
//		}
		
		halfCursorSize = (int)(getMapCursorImage().getWidth() * multiplier / 2);

		if(Core.upPressed) moveMapUp();
		if(Core.downPressed) moveMapDown();
		if(Core.leftPressed) moveMapLeft();
		if(Core.rightPressed) moveMapRight();

			
//		check if "cursor" is over a location (if so display name ? start animation?)
			
			
	}
	
	
	// y position must be higher than 0 plus half cursor size
	private void moveMapUp(){
		if (y  > 0 + halfCursorSize){
			y --;
		}
	}
	
	// y position must be lower than length of screen minus half cursor size
	private void moveMapDown(){		
		if (y < currentWorldMap.getHeight() * multiplier - halfCursorSize){
			y ++;
		}
	}
	
	// x position 0 plus half cursor size
	private void moveMapLeft(){
		if (x > 0 + halfCursorSize){
			x --;
		}
	}
	
	// x position must be lower than width minus half cursor size 
	private void moveMapRight(){
		if (x < currentWorldMap.getWidth() * multiplier - halfCursorSize){
			x ++;}
		//System.err.println(x + " " + currentWorldMap.getWidth() * multiplier);
	}
	
	// sets map cursor coordinates to coordinates of currently visited Area
	public void matchCoordinatesToLocalArea(int localAreaX, int localAreaY){
		this.x = localAreaX;
		this.y = localAreaY;
	}
	
	
	
	
	
	// public change world map . not sure when necessary though ?
	//							>> gives ability to change world map
	//							>> you never know if you need it
	// also make sure to painter.updateDimensions
}
