package foundation;

import graphics.Scroller;
import character.PlayerCharacter;
import areas.Obstacle;
import areas.Tile;

public class Interaction {
	
	
	/* === INDEX OF INTERACTION TYPES === */

	
	public static final int INDEX_TOUCH_OBJECT = 1;
	public static final int INDEX_MOVE_OBJECT = 2;
	
	
	/* === USED BY ALL TYPES === */

	
	private int interactionTypeIndex;
	
	public int interactionProgression;
	
	public static Scroller scroller;

	
	/* === USED BY OBJECT MOVING === */
	
	public boolean isPushing;
	
	public Obstacle touchedObstacle;

	
	
	
	/* =========================== METHOD =========================== */

	//
	public Interaction(int typeIndex) {
		this.interactionTypeIndex = typeIndex;
	}
	
	//
	public int getInteractionType(){
		return this.interactionTypeIndex;
	}
	
	// sets up an interaction
	static void createInteraction(){
		
		/* procedure:
		  > check what the character is trying to interact with
		  > create Interaction object for matching interaction
		  > if nothing can be found to interact with, no object is created  */
		
		// get direction character is facing
		int direction;
		
		switch (Core.currentCharacter.direction){
			// NW and NE are converted to N. SW and SE are converted to S.
			case PlayerCharacter.INDEX_SOUTH:
			case PlayerCharacter.INDEX_SOUTHEAST:
				direction = PlayerCharacter.INDEX_SOUTH; break;
			case PlayerCharacter.INDEX_EAST:
				direction = PlayerCharacter.INDEX_EAST; break;
			case PlayerCharacter.INDEX_NORTHEAST:
			case PlayerCharacter.INDEX_NORTH:
			case PlayerCharacter.INDEX_NORTHWEST:
				direction = PlayerCharacter.INDEX_NORTH; break;
			case PlayerCharacter.INDEX_WEST:
				direction = PlayerCharacter.INDEX_WEST; break;
			case PlayerCharacter.INDEX_SOUTHWEST:
				direction = PlayerCharacter.INDEX_SOUTH; break;
			default: direction = 0;
		}
		
		// get tile in front of direction character is facing. must be in range.
		int targetTileX = Core.currentCharacter.x, targetTileY = Core.currentCharacter.y;
		switch (direction){
			case PlayerCharacter.INDEX_SOUTH: 
				targetTileY += Core.TILE_SIZE / 4 *Core.multiplier; break;
			case PlayerCharacter.INDEX_EAST: 
				targetTileX += Core.TILE_SIZE / 2 *Core.multiplier; break;
			case PlayerCharacter.INDEX_NORTH: 
				targetTileY -= Core.TILE_SIZE / 2 *Core.multiplier; break;
			case PlayerCharacter.INDEX_WEST: 
				targetTileX -= Core.TILE_SIZE / 2 *Core.multiplier; break;
		}
			
		Tile targetTile = Core.currentArea.getTileAtPixel(targetTileX, targetTileY);
		
		// check what is located on the tile
		
		/* ========== OBJECT ========== */
		
		if (targetTile != null && targetTile.hasObstacle){
			if (Core.currentArea.getObstacleAtTile(targetTile).isMovable){
				Core.currentCharacter.isTouchingObject = true;
				Core.currentInteraction = new Interaction(Interaction.INDEX_TOUCH_OBJECT);
				Core.currentInteraction.touchedObstacle = Core.currentArea.getObstacleAtTile(targetTile);
				
				// center character position on tile (pretty and avoids glitches)
				Tile currentTile = Core.currentArea.getTileAtPixel(Core.currentCharacter.x,Core.currentCharacter.y);
				switch (direction){
					case PlayerCharacter.INDEX_SOUTH:
						Core.currentCharacter.x = (int)((currentTile.column * Core.TILE_SIZE * Core.multiplier) + Core.TILE_SIZE / 2 * Core.multiplier);
						Core.currentCharacter.y = (int)((currentTile.row * Core.TILE_SIZE * Core.multiplier) + Core.TILE_SIZE/16*15 * Core.multiplier);
						break;
					case PlayerCharacter.INDEX_EAST:
						Core.currentCharacter.x = (int)((currentTile.column * Core.TILE_SIZE * Core.multiplier) + Core.TILE_SIZE / 2 * Core.multiplier);
						Core.currentCharacter.y = (int)((currentTile.row * Core.TILE_SIZE * Core.multiplier) + Core.TILE_SIZE/4*3 * Core.multiplier);
						break;
					case PlayerCharacter.INDEX_NORTH:
						Core.currentCharacter.x = (int)((currentTile.column * Core.TILE_SIZE * Core.multiplier) + Core.TILE_SIZE / 2 * Core.multiplier);
						Core.currentCharacter.y = (int)((currentTile.row * Core.TILE_SIZE * Core.multiplier) + Core.TILE_SIZE/8*3 * Core.multiplier);
						break;
					case PlayerCharacter.INDEX_WEST:
						Core.currentCharacter.x = (int)((currentTile.column * Core.TILE_SIZE * Core.multiplier) + Core.TILE_SIZE/16*15 / 2 * Core.multiplier);
						Core.currentCharacter.y = (int)((currentTile.row * Core.TILE_SIZE * Core.multiplier) + Core.TILE_SIZE/4*3 * Core.multiplier);
						break;
				}
				// if method gets to this point, interaction has been created
				return;
			}
		}
		
		/* ============ NPC =========== */
		
		// NonPlayerCharacter NPC = NonPlayerCharacter.getCharacterOnTile()
		// if (NPC == null)
		
		
		// if method gets to this point, then no interaction found/created
	}
	
	
	//
	static void handleInteraction(){
		
		switch (Core.currentInteraction.getInteractionType() ) {
		
			// this interaction lets a character attempt to move an object
			case Interaction.INDEX_TOUCH_OBJECT:
				
				// this interaction can be cancelled
				if (!Core.primaryPressed) {
					Core.currentInteraction = null;
					Core.currentCharacter.isTouchingObject = false;
					break; 
				}
					
				// adjusts characters direction
				switch (Core.currentCharacter.direction){
					// NW and NE are converted to N. SW and SE are converted to S.
					case PlayerCharacter.INDEX_SOUTH:
					case PlayerCharacter.INDEX_SOUTHEAST: 
						Core.currentCharacter.direction = PlayerCharacter.INDEX_SOUTH;
						break;
					case PlayerCharacter.INDEX_EAST: 
						break;
					case PlayerCharacter.INDEX_NORTHEAST:
					case PlayerCharacter.INDEX_NORTH:
					case PlayerCharacter.INDEX_NORTHWEST: 
						Core.currentCharacter.direction = PlayerCharacter.INDEX_NORTH;
						break;
					case PlayerCharacter.INDEX_WEST: 
						break;
					case PlayerCharacter.INDEX_SOUTHWEST: 
						Core.currentCharacter.direction = PlayerCharacter.INDEX_SOUTH;
						break;
					default: Core.currentCharacter.direction = 0;
				}
				
				switch (Core.currentCharacter.direction){
					case PlayerCharacter.INDEX_SOUTH: 
						if (Core.downPressed){
							// push down

							// check if object and character can be moved to destination
							int destinationX = Core.currentCharacter.x ;
							int destinationY = (int)(Core.currentCharacter.y + (Core.TILE_SIZE * 2 * Core.multiplier) );
							Tile destinationTile = Core.currentArea.getTileAtPixel(destinationX, destinationY);
							
							int currentX = Core.currentCharacter.x;
							int currentY = (int)(Core.currentCharacter.y + Core.TILE_SIZE * Core.multiplier);
							Tile currentTile = Core.currentArea.getTileAtPixel(currentX,currentY);

							if (destinationTile != null && !destinationTile.hasObstacle && destinationTile.canContainObstacle && currentTile.isTraversable) {
								
								// if they can, create moving interaction
								Obstacle obstacle = Core.currentInteraction.touchedObstacle;	
								Core.currentInteraction = null;
								Core.currentInteraction = new Interaction(Interaction.INDEX_MOVE_OBJECT);
								Core.currentInteraction.interactionProgression = Core.TILE_SIZE;
								Core.currentInteraction.touchedObstacle = obstacle;
								Core.currentInteraction.isPushing = true;
								
								// update tiles
								currentTile.hasObstacle = false;
								destinationTile.hasObstacle = true;
							}
						} else if (Core.upPressed){
							// pull up
							
							// check if object and character can be moved to destination
							int characterDestinationX = Core.currentCharacter.x;
							int characterDestinationY = (int)(Core.currentCharacter.y - (Core.TILE_SIZE * Core.multiplier));
							Tile characterDestinationTile = Core.currentArea.getTileAtPixel(characterDestinationX, characterDestinationY);
							
							int destinationX = Core.currentCharacter.x;
							int destinationY = Core.currentCharacter.y;
							Tile objectDestinationTile = Core.currentArea.getTileAtPixel(destinationX,destinationY);
							
							if (characterDestinationTile != null && characterDestinationTile.isTraversable && !characterDestinationTile.hasObstacle && objectDestinationTile.canContainObstacle) {
								
								// WARNING: error when going too far north
								// which is not supposed to be possible with correct MANUAL Area creation
								
								// if they can, create moving interaction
								Obstacle obstacle = Core.currentInteraction.touchedObstacle;	
								Core.currentInteraction = null;
								Core.currentInteraction = new Interaction(Interaction.INDEX_MOVE_OBJECT);
								Core.currentInteraction.interactionProgression = Core.TILE_SIZE;
								Core.currentInteraction.touchedObstacle = obstacle;
								Core.currentInteraction.isPushing = false;
								
								// update tiles
								int currentX = Core.currentCharacter.x;
								int currentY = (int)(Core.currentCharacter.y + Core.TILE_SIZE * Core.multiplier);
								
								Core.currentArea.getTileAtPixel(currentX, currentY).hasObstacle = false;
								objectDestinationTile.hasObstacle = true;
							}
						}
						break;
					case PlayerCharacter.INDEX_EAST:
						if (Core.rightPressed){
							// push right
							
							// check if object and character can be moved to destination
							int destinationX = (int)(Core.currentCharacter.x + (Core.TILE_SIZE * 2 * Core.multiplier) );
							int destinationY = Core.currentCharacter.y ;
							Tile destinationTile = Core.currentArea.getTileAtPixel(destinationX, destinationY);
							
							int currentX = (int)(Core.currentCharacter.x + Core.TILE_SIZE * Core.multiplier);
							int currentY = Core.currentCharacter.y;
							Tile currentTile = Core.currentArea.getTileAtPixel(currentX,currentY);
							
							if (destinationTile != null && !destinationTile.hasObstacle && destinationTile.canContainObstacle && currentTile.isTraversable) {
								
								// if they can, create moving interaction
								Obstacle obstacle = Core.currentInteraction.touchedObstacle;	
								Core.currentInteraction = null;
								Core.currentInteraction = new Interaction(Interaction.INDEX_MOVE_OBJECT);
								Core.currentInteraction.interactionProgression = Core.TILE_SIZE;
								Core.currentInteraction.touchedObstacle = obstacle;
								Core.currentInteraction.isPushing = true;
								
								// update tiles
								currentTile.hasObstacle = false;
								destinationTile.hasObstacle = true;
							}
						} else if (Core.leftPressed){
							// pull left
							
							// check if object and character can be moved to destination
							int characterDestinationX = (int)(Core.currentCharacter.x - (Core.TILE_SIZE * Core.multiplier));
							int characterDestinationY = Core.currentCharacter.y;
							Tile characterDestinationTile = Core.currentArea.getTileAtPixel(characterDestinationX, characterDestinationY);
							
							int destinationX = Core.currentCharacter.x;
							int destinationY = Core.currentCharacter.y;
							Tile objectDestinationTile = Core.currentArea.getTileAtPixel(destinationX,destinationY);

							if (characterDestinationTile != null && characterDestinationTile.isTraversable && !characterDestinationTile.hasObstacle && objectDestinationTile.canContainObstacle) {
								
								// if they can, create moving interaction
								Obstacle obstacle = Core.currentInteraction.touchedObstacle;	
								Core.currentInteraction = null;
								Core.currentInteraction = new Interaction(Interaction.INDEX_MOVE_OBJECT);
								Core.currentInteraction.interactionProgression = Core.TILE_SIZE;
								Core.currentInteraction.touchedObstacle = obstacle;
								Core.currentInteraction.isPushing = false;
								
								// update tiles
								int currentX = (int)(Core.currentCharacter.x + Core.TILE_SIZE * Core.multiplier);
								int currentY = Core.currentCharacter.y;
								Core.currentArea.getTileAtPixel(currentX, currentY).hasObstacle = false;
								objectDestinationTile.hasObstacle = true;
							}
						}
						break;
					case PlayerCharacter.INDEX_NORTH:
						if (Core.upPressed){
							// push up
							
							// check if object and character can be moved to destination
							int destinationX = Core.currentCharacter.x ;
							int destinationY = (int)(Core.currentCharacter.y - (Core.TILE_SIZE * 2 * Core.multiplier) );
							Tile destinationTile = Core.currentArea.getTileAtPixel(destinationX, destinationY);
							
							int currentX = Core.currentCharacter.x;
							int currentY = (int)(Core.currentCharacter.y - Core.TILE_SIZE * Core.multiplier);
							Tile currentTile = Core.currentArea.getTileAtPixel(currentX,currentY);

							if (destinationTile != null && !destinationTile.hasObstacle && destinationTile.canContainObstacle && currentTile.isTraversable) {
								
								// if they can, create moving interaction
								Obstacle obstacle = Core.currentInteraction.touchedObstacle;	
								Core.currentInteraction = null;
								Core.currentInteraction = new Interaction(Interaction.INDEX_MOVE_OBJECT);
								Core.currentInteraction.interactionProgression = Core.TILE_SIZE;
								Core.currentInteraction.touchedObstacle = obstacle;
								Core.currentInteraction.isPushing = true;
								
								// update tiles
								currentTile.hasObstacle = false;
								destinationTile.hasObstacle = true;
							}
						} else if (Core.downPressed){
							// pull down
							
							// check if object and character can be moved to destination
							int characterDestinationX = Core.currentCharacter.x;
							int characterDestinationY = (int)(Core.currentCharacter.y + (Core.TILE_SIZE * Core.multiplier));
							Tile characterDestinationTile = Core.currentArea.getTileAtPixel(characterDestinationX, characterDestinationY);
							
							int destinationX = Core.currentCharacter.x;
							int destinationY = Core.currentCharacter.y;
							Tile objectDestinationTile = Core.currentArea.getTileAtPixel(destinationX,destinationY);

							if (characterDestinationTile != null && characterDestinationTile.isTraversable && !characterDestinationTile.hasObstacle && objectDestinationTile.canContainObstacle) {
								
								// if they can, create moving interaction
								Obstacle obstacle = Core.currentInteraction.touchedObstacle;	
								Core.currentInteraction = null;
								Core.currentInteraction = new Interaction(Interaction.INDEX_MOVE_OBJECT);
								Core.currentInteraction.interactionProgression = Core.TILE_SIZE;
								Core.currentInteraction.touchedObstacle = obstacle;
								Core.currentInteraction.isPushing = false;
								
								// update tiles
								int currentX = Core.currentCharacter.x;
								int currentY = (int)(Core.currentCharacter.y - Core.TILE_SIZE * Core.multiplier);
								
								Core.currentArea.getTileAtPixel(currentX, currentY).hasObstacle = false;
								objectDestinationTile.hasObstacle = true;
							}
						}
						break;
					case PlayerCharacter.INDEX_WEST:
						if (Core.leftPressed){
							// push left
							
							// check if object and character can be moved to destination
							int destinationX = (int)(Core.currentCharacter.x - (Core.TILE_SIZE * 2 * Core.multiplier) );
							int destinationY = Core.currentCharacter.y ;
							Tile destinationTile = Core.currentArea.getTileAtPixel(destinationX, destinationY);
							
							int currentX = (int)(Core.currentCharacter.x - Core.TILE_SIZE * Core.multiplier);
							int currentY = Core.currentCharacter.y;
							Tile currentTile = Core.currentArea.getTileAtPixel(currentX,currentY);
							
							if (destinationTile != null && !destinationTile.hasObstacle && destinationTile.canContainObstacle && currentTile.isTraversable) {
								
								// if they can, create moving interaction
								Obstacle obstacle = Core.currentInteraction.touchedObstacle;	
								Core.currentInteraction = null;
								Core.currentInteraction = new Interaction(Interaction.INDEX_MOVE_OBJECT);
								Core.currentInteraction.interactionProgression = Core.TILE_SIZE;
								Core.currentInteraction.touchedObstacle = obstacle;
								Core.currentInteraction.isPushing = true;
								
								// update tiles
								currentTile.hasObstacle = false;
								destinationTile.hasObstacle = true;
							}
						} else if (Core.rightPressed){
							// pull right
							
							// check if object and character can be moved to destination
							int characterDestinationX = (int)(Core.currentCharacter.x + (Core.TILE_SIZE * Core.multiplier));
							int characterDestinationY = Core.currentCharacter.y;
							Tile characterDestinationTile = Core.currentArea.getTileAtPixel(characterDestinationX, characterDestinationY);
							
							int destinationX = Core.currentCharacter.x;
							int destinationY = Core.currentCharacter.y;
							Tile objectDestinationTile = Core.currentArea.getTileAtPixel(destinationX,destinationY);

							if (characterDestinationTile != null && characterDestinationTile.isTraversable && !characterDestinationTile.hasObstacle && objectDestinationTile.canContainObstacle) {
								
								// if they can, create moving interaction
								Obstacle obstacle = Core.currentInteraction.touchedObstacle;	
								Core.currentInteraction = null;
								Core.currentInteraction = new Interaction(Interaction.INDEX_MOVE_OBJECT);
								Core.currentInteraction.interactionProgression = Core.TILE_SIZE;
								Core.currentInteraction.touchedObstacle = obstacle;
								Core.currentInteraction.isPushing = false;
								
								// update tiles
								int currentX = (int)(Core.currentCharacter.x - Core.TILE_SIZE * Core.multiplier);
								int currentY = Core.currentCharacter.y;
								Core.currentArea.getTileAtPixel(currentX, currentY).hasObstacle = false;
								objectDestinationTile.hasObstacle = true;
							}
						}
						break;
				}				
				break;

			// this interaction moves an object and the character
			case Interaction.INDEX_MOVE_OBJECT:
				
				if (Core.currentInteraction.interactionProgression-- != 0){
					switch (Core.currentCharacter.direction){
						case PlayerCharacter.INDEX_SOUTH:
							if (Core.currentInteraction.isPushing) {
								Core.currentCharacter.y += 1 * Core.multiplier;
								Core.currentInteraction.touchedObstacle.y ++;
							} else {
								Core.currentCharacter.y -= 1 * Core.multiplier;
								Core.currentInteraction.touchedObstacle.y --;
							}
							break;
						case PlayerCharacter.INDEX_EAST:
							if (Core.currentInteraction.isPushing) {
								Core.currentCharacter.x += 1 * Core.multiplier;
								Core.currentInteraction.touchedObstacle.x ++;
							} else {
								Core.currentCharacter.x -= 1 * Core.multiplier;
								Core.currentInteraction.touchedObstacle.x --;
							}
							break;
						case PlayerCharacter.INDEX_NORTH:
							if (Core.currentInteraction.isPushing) {
								Core.currentCharacter.y -= 1 * Core.multiplier;
								Core.currentInteraction.touchedObstacle.y --;
							} else {
								Core.currentCharacter.y += 1 * Core.multiplier;
								Core.currentInteraction.touchedObstacle.y ++;
							}
							break;
						case PlayerCharacter.INDEX_WEST:
							if (Core.currentInteraction.isPushing) {
								Core.currentCharacter.x -= 1 * Core.multiplier;
								Core.currentInteraction.touchedObstacle.x --;
							} else {
								Core.currentCharacter.x += 1 * Core.multiplier;
								Core.currentInteraction.touchedObstacle.x ++;
							}
							break;
					}
				} else {
					Core.currentCharacter.spriteOverride = Core.currentCharacter.getAnimationSprite();
					Core.currentInteraction = null;
					Core.currentCharacter.isTouchingObject = false;
				}
				break;
			// TODO make more interactions ^_^
			// case Interaction.OTHER_INTERACTION
		}
		scroller.centerViewAroundCharacter();
	}
}
