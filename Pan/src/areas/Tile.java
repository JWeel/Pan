package areas;
import java.awt.image.BufferedImage;

import foundation.Core;
import repository.GraphicsManager;

// a tile. contains an image and whether it can be walked on (collision)
public class Tile {
	
	//public TileType type;
	
	// image of the tile 
	public BufferedImage tileImage;
	//public BufferedImage obstacleImage = null; // collision is checked by (img != null)
	
	public boolean isTraversable, canContainObstacle, hasObstacle, tileObstacleIsMovable;
	
	
	public int column;

	public int row;
	
	//how it should look:
	/*
	public Tile (type){
		tileImage = GraphicsManager.getTileImage(type); // or something like that
		
		
		or use tile type ?
	}
	 */
	
	public TileEvent event;
	
	
	
	public Tile(BufferedImage tile, BufferedImage object){
		tileImage = tile;
		isTraversable = true;
		//obstacleImage = object;
		
		// possibly get from parameter ? TODO
		//if (obstacleImage != null) tileObstacleIsMovable = true;
	}
	
	
	public Tile(){
		int ran = Core.random.nextInt(9);
		if (ran < 3){
			tileImage = GraphicsManager.tileGrass1;
			this.isTraversable = true;
			this.canContainObstacle = true;
		} else if (ran < 7){
			tileImage = GraphicsManager.tileGrass2;
			this.isTraversable = true;
			this.canContainObstacle = true;
		} else if (ran == 7) {
			tileImage = GraphicsManager.tileGrass3;
			this.isTraversable = true;
			this.canContainObstacle = true;
		} else if (ran == 8){
			tileImage = GraphicsManager.tileGrass4;
			this.isTraversable = true;
			this.canContainObstacle = true;
		}
//		} else if (ran == 9){
//			tileImage = GraphicsManager.tileTestBlock1;
//			this.isTraversable = true;
//			this.canContainObstacle = false;
//		}
		
		
//		} else if (ran == 9){
//			
//			tileImage = GraphicsManager.tileGrass2;
//			//obstacleImage = GraphicsManager.objectRock1;
//			//if (obstacleImage != null) tileObstacleIsMovable = true;
//		}
//		tileImage = GraphicsManager.tileTestBlock1;
	}
	
	void addEvent(TileEvent newEvent){
		event = newEvent;
		// ^ possibly change to (ArrayList<TileEvent>) events.add(newEvent); ?
	}
	
	public boolean hasEvent(){
		return event != null;
		// return !events.isEmpty();
	}
}

