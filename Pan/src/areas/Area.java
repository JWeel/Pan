package areas;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import repository.GraphicsManager;
import repository.MonsterManager;
import foundation.Core;


// an area in the world where characters can walk and interact

// TODO : change to abstract class so each area can be hand built (hardcoded?)
public class Area {
	
	public final int columns;  // note all areas should be at least 16 by 12
	public final int rows;	 // add black (empty) tiles if not big enough
	
	public int worldCoordinatesX, worldCoordinatesY;
	
	public String areaName;
	
	public Tile[][] tiles;
	
	public ArrayList<Obstacle> obstacles;
	
	// this divided by 1000 is rate of random battles. 0 means no random battles
	public int encounterRate;
	
	// a list of all encounters, which are lists of monster IDs
	public ArrayList<ArrayList<Integer>> encounters;
	
	// an image of what this area looks like in a battle setting
	private BufferedImage battleScreen;
	
	public boolean darkensOnTransition;
	
	
	public Area(){
		
		this.columns = 20;
		this.rows = 14;
		
		this.tiles = new Tile[columns][rows];
		
		this.worldCoordinatesX = 125;
		this.worldCoordinatesY = 144;
		
		this.areaName = "Temporaria";
		
		this.obstacles = new ArrayList<Obstacle>();

		this.encounterRate = 0;
		
		this.darkensOnTransition = true;
		
		// initialize all tiles
		// TODO right now randomizes all tiles. need to make fixed areas
		for (int i = 0; i < tiles.length; i ++){
			for (int j = 0; j < tiles[i].length; j++){
				
				if (i == 0 && j == 6){
					
					tiles[i][j] = TileType.GRASS1();
					
					tiles[i][j].addEvent(TileEvent.transition(new Area(this)));
					
					tiles[i][j].column = i;
					tiles[i][j].row = j;
					
				} else {
					tiles[i][j] = new Tile();
					
					tiles[i][j].column = i;
					tiles[i][j].row = j;
					
					int ran = Core.random.nextInt(10);
					
					if (ran == 9){
						
						Obstacle newObstacle = new Obstacle();
						newObstacle.x = i * Core.TILE_SIZE;
						newObstacle.y = j * Core.TILE_SIZE;
						obstacles.add(newObstacle);
						
						tiles[i][j].hasObstacle = true;						
					}
				}
				
			}
		}
		

	}
	
	//
	public Area(Area linkedArea){
		
		this.columns = 6;
		this.rows = 12;
		
		this.tiles = new Tile[columns][rows];
		
		this.worldCoordinatesX = 100;
		this.worldCoordinatesY = 43;
		
		this.areaName = "New Temporaria";
		
		this.obstacles = new ArrayList<Obstacle>();
		
		this.encounterRate = 15;
		
		this.encounters = setUpEncounters();
		
		this.battleScreen = GraphicsManager.battleScreen1;
		
		//this.darkensOnTransition = true;
		
		// initialize all tiles
		// TODO right now randomizes all tiles. need to make fixed areas
		for (int i = 0; i < tiles.length; i ++){
			for (int j = 0; j < tiles[i].length; j++){
				
				if (i == 0 && j == 6) {
					
					tiles[i][j] = TileType.GRASS1();
					
					tiles[i][j].column = i;
					tiles[i][j].row = j;
					
					tiles[i][j].addEvent(TileEvent.transition(linkedArea));
					
				} else tiles[i][j] = new Tile();
				
					tiles[i][j].column = i;
					tiles[i][j].row = j;
				
			}
		}
	}
	
	// returns a list of all encounters in this area. create manually for each area.
	private ArrayList<ArrayList<Integer>> setUpEncounters(){
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Integer> encounter1 = new ArrayList<Integer>();
		encounter1.add(MonsterManager.ID_MINION1);
		encounter1.add(MonsterManager.ID_STEM1);
		encounter1.add(MonsterManager.ID_MINION1);

		list.add(encounter1);
		
		ArrayList<Integer> encounter2 = new ArrayList<Integer>();
		encounter2.add(MonsterManager.ID_MINION1);
		encounter2.add(MonsterManager.ID_MINION1);

		list.add(encounter2);
		
		ArrayList<Integer> encounter3 = new ArrayList<Integer>();
		encounter3.add(MonsterManager.ID_STEM1);
		encounter3.add(MonsterManager.ID_STEM1);
		encounter3.add(MonsterManager.ID_STEM1);
		encounter3.add(MonsterManager.ID_STEM1);

		list.add(encounter3);
		
		ArrayList<Integer> encounter4 = new ArrayList<Integer>();
		encounter4.add(MonsterManager.ID_STEM1);

		list.add(encounter4);
		
		ArrayList<Integer> encounter5 = new ArrayList<Integer>();
		encounter5.add(MonsterManager.ID_MINION1);
		encounter5.add(MonsterManager.ID_MINION1);
		encounter5.add(MonsterManager.ID_MINION1);
		encounter5.add(MonsterManager.ID_MINION1);
		encounter5.add(MonsterManager.ID_MINION1);

		list.add(encounter5);
		
		ArrayList<Integer> encounter6 = new ArrayList<Integer>();
		encounter6.add(MonsterManager.ID_MINION1);
		encounter6.add(MonsterManager.ID_STEM1);
		encounter6.add(MonsterManager.ID_MOLLUSCA1);
		encounter6.add(MonsterManager.ID_STEM1);
		encounter6.add(MonsterManager.ID_MINION1);

		list.add(encounter6);
		
		return list;
	}
	
	//
	public ArrayList<Integer> getEncounter(){
		
		int ran = Core.random.nextInt(1200);

		if (ran < 200) return encounters.get(0);
		else if (ran < 400) return encounters.get(1);
		else if (ran < 600) return encounters.get(2);
		else if (ran < 800) return encounters.get(3);
		else if (ran < 1000) return encounters.get(4);
		else if (ran < 1200) return encounters.get(5);
		
		
		// should never get here
		return null;
	}
	
	// returns the image matching this current area in battle setting.
	public BufferedImage getBattleScreen(){
		return battleScreen;
	}
	
	
	// returns the tile object located at given pixel coordinates
	public Tile getTileAtPixel(int pixelX, int pixelY){
		int tileX = (int) (pixelX / (Core.TILE_SIZE * Core.multiplier)) ;
		int tileY = (int) (pixelY / (Core.TILE_SIZE * Core.multiplier)) ;
		
		
		//System.err.println("in Area: " + tileX + " " + tileY);
		
		if (tileX >= 0 && tileX < columns && tileY >= 0 && tileY < rows)
			return tiles[tileX][tileY];
		else return null;
	}
	
	
	// returns the obstacle located on a given tile
	public Obstacle getObstacleAtTile(Tile tile){
		for (Obstacle obstacle : obstacles){
			if (obstacle.x / Core.TILE_SIZE == tile.column
					&& obstacle.y / Core.TILE_SIZE == tile.row){
				
//				System.err.println(obstacle.x + " " +
//						obstacle.y + " " + tile.column + " " + tile.row
//						);
				
				return obstacle;
			}
		}
		return null;
	}
	
	
//	// returns whether there is an obstacle on a tile
//	public boolean tileHasObstacle(Tile tile){
//		
//		for (int i = 0; i < obstacles.size(); i++){
//			
//			if ()
//			
//		}
//		
//	}
}
