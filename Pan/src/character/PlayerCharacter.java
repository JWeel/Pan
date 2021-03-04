package character;

import foundation.Core;
import graphics.Animator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import battle.Ability;
import battle.Combatant;
import battle.Condition;
import menus.Menu;
import repository.GraphicsManager;
import repository.ItemManager;
import sorting.EquipmentComparator;

public class PlayerCharacter implements Combatant {

	// indexes for the directions a character can be facing
	public static final int INDEX_SOUTH = 0;
	public static final int INDEX_SOUTHEAST = 1;
	public static final int INDEX_EAST = 2;
	public static final int INDEX_NORTHEAST = 3;
	public static final int INDEX_NORTH = 4;
	public static final int INDEX_NORTHWEST = 5;
	public static final int INDEX_WEST = 6;
	public static final int INDEX_SOUTHWEST = 7;
	
	// indexes for the player characters
	public static final int INDEX_PAN = 0;
	public static final int INDEX_MOCA = 1;
	public static final int INDEX_SATUROS = 2;
	public static final int INDEX_MENARDI = 3;
	public static final int INDEX_FRANKERZ = 4;
	
	// size of the inventory
	public static final int INVENTORY_CAP = 15;
	
	// ID of character
	public int ID;
	
	// coordinates of character on screen
	public int x, y;
	
	// level of character
	public int level, experience;
	
	public int money;
	
	public Integer speed;
	
	// statistics
	public int maxHP, maxMP, HP, MP, armorRating, attackRating, immunity, 
	firePower, fireResistance, waterPower, waterResistance,	airPower, 
	airResistance, earthPower, earthResistance, arcanePower, arcaneResistance,
	HPRegen, MPRegen;
	
	// bonuses to statistics during battle
	public int bonusMaxHP, bonusMaxMP, bonusArmorRating, bonusAttackRating, 
	bonusFirePower, bonusFireResistance, bonusWaterPower, bonusWaterResistance, 
	bonusAirPower, bonusAirResistance, bonusEarthPower, bonusEarthResistance, 
	bonusArcanePower, bonusArcaneResistance, bonusImmunity,
	bonusHPRegen, bonusMPRegen;
	
	// amount of animations in particular movement .TODO make final ? array-list?
	public int amountOfWalkAnimations;
	// public int amountOfIdleAnimations, amountOfRunAnimations,
	// amountOfClimbAnimations, amountOfBattleIdleAnimations;
	
	// name of character
	public String name;
	
	// mug-shot of character
	public BufferedImage avatar;
	
	// over-world images used for character
	public BufferedImage spriteIdleDown, spriteIdleRight, spriteIdleLeft, 
		spriteIdleUp, spriteMoveDown1, spriteMoveDown2, spriteMoveRight1, 
		spriteMoveRight2, spriteMoveLeft1, spriteMoveLeft2, 
		spriteMoveUp1, spriteMoveUp2, spriteTouchDown, spriteTouchRight,
		spriteTouchLeft, spriteTouchUp, shadow1;
	
	// battle images used for character
	public BufferedImage spriteBattleStand1;
	
	// image used for temporarily superseding any other sprite requests
	public BufferedImage spriteOverride;
	
	// direction the character is facing.
	public int direction;
	
	// boolean for when the character is running
	public boolean isRunning;
	
	// booleans for when the character is not in the center of the screen
	public boolean offCenterHorizontal = true, offCenterVertical = true;

	// boolean for when a character is touching an object (to move)
	public boolean isTouchingObject;
	
	// an animation thread that handles switching between images
	public Animator animator;
	
	// the other characters on the character's team (only for main character)
	//public ArrayList<PlayerCharacter> allies;
	
	// the character's known spells
	public ArrayList<Ability> spellbook;
	
	// the items the character is carrying
	public ArrayList<Item> inventory;
	
	// the items the character has equipped, stored so less calculating
	public Item equippedWeapon, equippedChest, equippedHead, equippedArms,
				equippedFeet, equippedRing, equippedNeck;
	
	// what position the character takes in battle
	public int battlePosition;
	
	// the move the character is planning to perform in battle
	public Ability move;
	
	// the item a character used in a battle
	public Item usedItem;
	
	// the status effects the character is suffering from in a battle
	public ArrayList<Condition> conditions;
	
	// creates a default character
	public PlayerCharacter(){
		this.x = 0;
		this.y = 0;
		this.level = 1;
		this.name = "Pan";
		this.ID = INDEX_PAN;
		this.avatar = GraphicsManager.panAvatar;
		this.spriteIdleDown = GraphicsManager.panDown1;
		this.spriteIdleRight = GraphicsManager.panRight1;
		this.spriteIdleLeft = GraphicsManager.panLeft1;
		this.spriteIdleUp = GraphicsManager.panUp1;
		this.spriteMoveDown1 = GraphicsManager.panMoveDown1;
		this.spriteMoveDown2 = GraphicsManager.panMoveDown2;
		this.spriteMoveRight1 = GraphicsManager.panMoveRight1;
		this.spriteMoveRight2 = GraphicsManager.panMoveRight2;
		this.spriteMoveLeft1 = GraphicsManager.panMoveLeft1;
		this.spriteMoveLeft2 = GraphicsManager.panMoveLeft2;
		this.spriteMoveUp1 = GraphicsManager.panMoveUp1;
		this.spriteMoveUp2 = GraphicsManager.panMoveUp2;
		this.spriteTouchDown = GraphicsManager.panTouchDown1;
		this.spriteTouchRight = GraphicsManager.panTouchRight1;
		this.spriteTouchLeft = GraphicsManager.panTouchLeft1;
		this.spriteTouchUp = GraphicsManager.panTouchUp1;
		this.shadow1 = GraphicsManager.shadowCharacter1;
		this.spriteBattleStand1 = GraphicsManager.panBattleStand1;
		
		this.amountOfWalkAnimations = 4;
		this.direction = 0;
		
		this.battlePosition = 0;
		
//		this.maxHP = 100;
//		this.maxMP = 50;
//		this.HP = maxHP;
//		this.MP = maxMP;
//		this.armorRating = 5;
//		this.attackRating = 5;
//		this.speed = 5;
//		this.firePower = 5;
//		this.fireResistance = 5;
//		this.waterPower = 5;
//		this.waterResistance =5;
//		this.airPower = 5;
//		this.airResistance = 5;
//		this.earthPower = 5;
//		this.earthResistance = 5;
//		this.arcanePower =5;
//		this.arcaneResistance =5;
//		this.immunity = 2;
		
		this.level = Core.random.nextInt(99);
		this.maxHP = Core.random.nextInt(999);
		this.maxMP = Core.random.nextInt(99);
		this.HP = Core.random.nextInt(maxHP+1);
		this.MP = Core.random.nextInt(maxMP+1);
//		this.HP = maxHP; this.MP = maxMP-1;
		this.armorRating = Core.random.nextInt(999);
		this.attackRating = Core.random.nextInt(999);
		this.speed = Core.random.nextInt(999);
		this.firePower = Core.random.nextInt(999);
		this.fireResistance = Core.random.nextInt(999);
		this.waterPower = Core.random.nextInt(999);
		this.waterResistance =Core.random.nextInt(999);
		this.airPower = Core.random.nextInt(999);
		this.airResistance = Core.random.nextInt(999);
		this.earthPower = Core.random.nextInt(999);
		this.earthResistance = Core.random.nextInt(999);
		this.arcanePower =Core.random.nextInt(999);
		this.arcaneResistance =Core.random.nextInt(999);
		this.immunity = Core.random.nextInt(50);
		
		
		
		//this.allies = new ArrayList<PlayerCharacter>();
		this.spellbook = new ArrayList<Ability>();
		this.inventory = new ArrayList<Item>();
		this.conditions = new ArrayList<Condition>();
	}
	
	// creates a character with a passed name
	public PlayerCharacter(int newID, String newName){
		this.x = 0;
		this.y = 0;
		this.level = 1;
		this.name = newName;
		this.ID = newID;
		
		switch (this.ID){
		case INDEX_PAN:
			
			// this.setAllSpritesTo(INDEX_PAN);
			this.battlePosition = 0;
			break;
		case INDEX_MOCA:
			
			// this.setAllSpritesTo(INDEX_MOCA);
			this.battlePosition = 1;
			break;
		case INDEX_SATUROS:
			
			// this.setAllSpritesTo(INDEX_SATUROS);
			this.battlePosition = 2;
			break;
		case INDEX_MENARDI:
			
			// this.setAllSpritesTo(INDEX_MENARDI);
			this.battlePosition = 3;
			break;
		case INDEX_FRANKERZ:
			
			// this.setAllSpritesTo(INDEX_FRANKERZ);
			this.battlePosition = 4;
			break;
		}

		// amountOfWalkAnimations = (ArrayList<BufferedImage>) spriteMoveAnimations.size() <- like that?
		this.amountOfWalkAnimations = 4;
		this.direction = 0;
		
		this.battlePosition = 0;

		
		//this.allies = new ArrayList<PlayerCharacter>();
	}
	
	/*
	 possibly:
		 PlayerCharacter(int newId, int newName,
		 int newMaxHP, int newMaxMP, int newAttackRating, int newArmorRating,
		 int newSpeed, int newImmunity,
		 int newFirePower, int newFireResistance, etc etc etc
		 ){
		 this.stats = stats
		 
		 }
		 
		 have another method that is used when characters are newly created (no save)
		 PlayerCharacter(int newID, int newName){
			switch (newID){
			
				case INDEX_MOCA:
					this(
					
					50, // default starting maxHP
					50, // default starting maxMP
					30, // default starting etc
					etc
					
					);		
			}
		 }
	 */
	
	// moves the character's coordinates and sets up animation
	public void moveCharacter(){
		
		// TODO if (!isPulling)
		// sets direction character is facing so correct sprite will be used
		this.updateDirection();
		
		// TODO in GS when character moves somewhere inaccessible, he is pushed
		//				to nearby pixel (useful when going through small gap)
		// possibly : if (straight in front has object)
		//				check (pixel*buffer) next to it on both sides
		//				if that place is clear, move in that direction
		
		// moves character in each direction it is trying to and can move
		if(Core.upPressed) moveCharacterUp();
		if(Core.downPressed) moveCharacterDown();
		if(Core.leftPressed) moveCharacterLeft();
		if(Core.rightPressed) moveCharacterRight();
	}
	
	// y position minus height of character must be higher than 0
	private void moveCharacterUp(){
		if (this.y - (this.spriteIdleDown.getHeight()*Core.multiplier /*+1*/) > 0
				&& noObstacleInTheWay("up")){
			this.y --;
		}
	}
	
	// y position must be lower than length of screen minus 1 safety pixel
	private void moveCharacterDown(){		
		if (this.y < (Core.currentArea.rows*Core.TILE_SIZE*Core.multiplier -1)
				&& noObstacleInTheWay("down")){
			this.y ++;
		}
	}
	
	// x position minus half character minus 1 safety pixel must be higher than 0
	private void moveCharacterLeft(){
		if (this.x - this.spriteIdleDown.getWidth()/2*Core.multiplier -1 > 0
				&& noObstacleInTheWay("left")){
			this.x --;
		}
	}
	
	// x position must be lower than width minus half character minus 1 safety pixel 
	private void moveCharacterRight(){
		if (this.x < (Core.currentArea.columns*Core.TILE_SIZE*Core.multiplier - this.spriteIdleDown.getWidth()/2*Core.multiplier -1)
				&& noObstacleInTheWay("right")){
			this.x ++;}
	}
	
	// checks if no obstacle is in the way of chosen direction
	private boolean noObstacleInTheWay(String direction){
		
		// a buffer is added to allow smoother movement through gaps. ! exploit ???
		int buffer = (int)(2 * Core.multiplier);

		int halfCharacterWidth = (int)(this.spriteIdleDown.getWidth() /2 * Core.multiplier);	
		
		switch (direction){
			case "up":
				for (int column = this.x - halfCharacterWidth + buffer; 
						column <= this.x + halfCharacterWidth - buffer;
						column++){
					if (!Core.currentArea.getTileAtPixel(column,this.y-buffer*3).isTraversable
							|| Core.currentArea.getTileAtPixel(column,this.y-buffer*3).hasObstacle)
						return false;
				}
				return true;
			case "down":
				for (int column = this.x - halfCharacterWidth + buffer; 
						column <= this.x + halfCharacterWidth - buffer;
						column++){
					if (!Core.currentArea.getTileAtPixel(column,this.y+1).isTraversable
							|| Core.currentArea.getTileAtPixel(column,this.y+1).hasObstacle)
						return false;
				}
				return true;
			case "left":
				for (int row = this.y - halfCharacterWidth + buffer*2; 
						row <= this.y; row++){
					if (!Core.currentArea.getTileAtPixel(this.x-halfCharacterWidth,row).isTraversable
							|| Core.currentArea.getTileAtPixel(this.x-halfCharacterWidth,row).hasObstacle)
						return false;
				}
				return true;
			case "right":
				for (int row = this.y - halfCharacterWidth + buffer*2; 
						row <= this.y; row++){
					if (!Core.currentArea.getTileAtPixel(this.x+halfCharacterWidth,row).isTraversable
							|| Core.currentArea.getTileAtPixel(this.x+halfCharacterWidth,row).hasObstacle)
						return false;
				}
				return true;
			default: return false;
		}
	}
		
	public BufferedImage getShadow(){
		return shadow1;
	}
	
	// returns a certain sprite for animating
	public BufferedImage getAnimationSprite(){
		
		// checks if an interaction override is in place, and if so returns it
		if (spriteOverride != null){
			if (Core.primaryPressed){
				return spriteOverride;
			} else spriteOverride = null;
		}
		
		
		if (Core.inBattle) {
			
			// switch(animator.getAnimation())
			
			if (HP == 0) return GraphicsManager.minion1Down1;
			
			return spriteBattleStand1;
			
			
			
		} else {
			
			if (isTouchingObject){
				switch (direction){
					case INDEX_SOUTH: return spriteTouchDown;
					case INDEX_EAST: return spriteTouchRight;
					case INDEX_NORTH: return spriteTouchUp;
					case INDEX_WEST: return spriteTouchLeft;
				}
			}
			
			if ((Core.upPressed || Core.downPressed || Core.leftPressed || Core.rightPressed) && !Core.movementSuspended) {
				
				if (Menu.menuDepth != 0) {
					switch (direction){
						/* TODO switch(animator.getAnimation()){ */
	
						case INDEX_SOUTH: return spriteIdleDown;
						case INDEX_SOUTHEAST:
						case INDEX_EAST: return spriteIdleRight;
						case INDEX_NORTHEAST:
						case INDEX_NORTH: return spriteIdleUp;
						case INDEX_NORTHWEST:
						case INDEX_WEST: return spriteIdleLeft;
						case INDEX_SOUTHWEST:
							
						default: return spriteIdleDown;
					}
				}
				
//				 if (isRunning) {
//				
//				 } else {
				
				// SAFETY BUG FIX . NOT QUITE SURE WHY NECESSARY (BUT NECESSARY !)
				if (animator == null) return spriteIdleDown;
			
				switch (direction){
					case INDEX_SOUTH:					
						switch(animator.getAnimationIndex()){
							case 0: return spriteMoveDown1;
							case 1: return spriteIdleDown;
							case 2: return spriteMoveDown2;
							case 3: return spriteIdleDown;
						}
					case INDEX_SOUTHEAST:
					case INDEX_EAST:
						switch(animator.getAnimationIndex()){
							case 0: return spriteMoveRight1;
							case 1: return spriteIdleRight;
							case 2: return spriteMoveRight2;
							case 3: return spriteIdleRight;
						}
					case INDEX_NORTHEAST:
					case INDEX_NORTH:
						switch(animator.getAnimationIndex()){
							case 0: return spriteMoveUp1;
							case 1: return spriteIdleUp;
							case 2: return spriteMoveUp2;
							case 3: return spriteIdleUp;
						}
					case INDEX_NORTHWEST:
					case INDEX_WEST:
						switch(animator.getAnimationIndex()){
							case 0: return spriteMoveLeft1;
							case 1: return spriteIdleLeft;
							case 2: return spriteMoveLeft2;
							case 3: return spriteIdleLeft;
						}
					case INDEX_SOUTHWEST:
				}
			}
			// else not moving
			else {
				switch (direction){
					/* TODO switch(animator.getAnimation()){ */
	
					case INDEX_SOUTH: return spriteIdleDown;
					case INDEX_SOUTHEAST:
					case INDEX_EAST: return spriteIdleRight;
					case INDEX_NORTHEAST:
					case INDEX_NORTH: return spriteIdleUp;
					case INDEX_NORTHWEST:
					case INDEX_WEST: return spriteIdleLeft;
					case INDEX_SOUTHWEST:
						
					default: return spriteIdleDown;
				}
			}
		}
		// as long as this method is not complete, this final return is needed
		return spriteIdleDown;
	}
	
	// creates animation thread and starts it
	public void startAnimation(){
		animator = new Animator(amountOfWalkAnimations);
		animator.thread = new Thread(animator);
		animator.thread.start();
	}
	
	// destroys the animation thread
	public void stopAnimation(){
		if (animator != null){
			animator.thread.interrupt();
			animator = null;
		}
	}
	
	// updates the direction the character is facing
	// S=0 , SE=1 , E=2 , NE=3 , N=4 , NW=5 , W=6 , SW=7
	public void updateDirection(){
		if (Core.downPressed  && !Core.rightPressed && !Core.leftPressed && !Core.upPressed) direction = INDEX_SOUTH;
		if (Core.downPressed  && Core.rightPressed  && !Core.leftPressed && !Core.upPressed) direction = INDEX_SOUTHEAST;
		if (!Core.downPressed && Core.rightPressed  && !Core.leftPressed && !Core.upPressed) direction = INDEX_EAST;
		if (!Core.downPressed && Core.rightPressed  && !Core.leftPressed && Core.upPressed)  direction = INDEX_NORTHEAST;
		if (!Core.downPressed && !Core.rightPressed && !Core.leftPressed && Core.upPressed)  direction = INDEX_NORTH;
		if (!Core.downPressed && !Core.rightPressed && Core.leftPressed  && Core.upPressed)  direction = INDEX_NORTHWEST;
		if (!Core.downPressed && !Core.rightPressed && Core.leftPressed  && !Core.upPressed) direction = INDEX_WEST;
		if (Core.downPressed  && !Core.rightPressed && Core.leftPressed  && !Core.upPressed) direction = INDEX_SOUTHWEST;
	}
	
	// returns whether an item class can be equipped by this character
	public boolean canEquip(int itemTypeIndex){
		if (itemTypeIndex == ItemManager.INDEX_MISCELLANEOUS) return false; 
		switch (this.ID){
			case INDEX_PAN:
				return
					itemTypeIndex == ItemManager.INDEX_SWORD_SHORT ||
					itemTypeIndex == ItemManager.INDEX_SWORD_LONG ||
					itemTypeIndex == ItemManager.INDEX_STAFF ||
					itemTypeIndex == ItemManager.INDEX_POLE ||
					itemTypeIndex == ItemManager.INDEX_SHIRT ||
					itemTypeIndex == ItemManager.INDEX_ARMOR ||
					itemTypeIndex == ItemManager.INDEX_CAP ||
					itemTypeIndex == ItemManager.INDEX_HELM ||
					itemTypeIndex == ItemManager.INDEX_GLOVE ||
					itemTypeIndex == ItemManager.INDEX_SHIELD ||
					itemTypeIndex == ItemManager.INDEX_BOOT ||
					itemTypeIndex == ItemManager.INDEX_GREAVE||
					itemTypeIndex == ItemManager.INDEX_RING ||
					itemTypeIndex == ItemManager.INDEX_NECK;
			case INDEX_MOCA:
				return
					itemTypeIndex == ItemManager.INDEX_SWORD_LONG ||
					itemTypeIndex == ItemManager.INDEX_AXE ||
					itemTypeIndex == ItemManager.INDEX_MACE ||
					itemTypeIndex == ItemManager.INDEX_FLAIL ||
					itemTypeIndex == ItemManager.INDEX_SHIRT ||
					itemTypeIndex == ItemManager.INDEX_ARMOR ||
					itemTypeIndex == ItemManager.INDEX_CAP ||
					itemTypeIndex == ItemManager.INDEX_HELM ||
					itemTypeIndex == ItemManager.INDEX_GLOVE ||
					itemTypeIndex == ItemManager.INDEX_SHIELD ||
					itemTypeIndex == ItemManager.INDEX_BOOT ||
					itemTypeIndex == ItemManager.INDEX_GREAVE||
					itemTypeIndex == ItemManager.INDEX_RING ||
					itemTypeIndex == ItemManager.INDEX_NECK;
			
			default: return false;
		}
	}
	
	// returns the item of the same type as a given item that is currently equipped
	public Item getEquippedItem(int itemTypeIndex){
		switch (itemTypeIndex){
			case ItemManager.INDEX_SWORD_SHORT:
			case ItemManager.INDEX_SWORD_LONG:
			case ItemManager.INDEX_AXE:
			case ItemManager.INDEX_MACE:
			case ItemManager.INDEX_FLAIL:
			case ItemManager.INDEX_STAFF:
			case ItemManager.INDEX_POLE:	 return equippedWeapon;
			
			case ItemManager.INDEX_SHIRT:
			case ItemManager.INDEX_ARMOR:
			case ItemManager.INDEX_ROBE:	 return equippedChest;
			
			case ItemManager.INDEX_CAP:
			case ItemManager.INDEX_HELM:
			case ItemManager.INDEX_HEADBAND: return equippedHead;
			
			case ItemManager.INDEX_GLOVE:
			case ItemManager.INDEX_SHIELD:
			case ItemManager.INDEX_BRACELET: return equippedArms;
			
			case ItemManager.INDEX_BOOT:
			case ItemManager.INDEX_GREAVE:
			case ItemManager.INDEX_SANDAL:	 return equippedFeet;
			
			case ItemManager.INDEX_RING:	 return equippedRing;
			
			case ItemManager.INDEX_NECK:	 return equippedNeck;
				
			default: return null;
		}
	}
	
	// sets an item to be equipped, while updating the character's statistics
	public void equipItem(Item newItem){
		
		// if already has item equipped, remove its STAT bonuses
		Item oldItem = getEquippedItem(newItem.typeIndex);
		if (oldItem != null) unequipItem(oldItem);
		
		newItem.isEquipped = true;
		
		this.maxHP += newItem.HP;
		this.maxMP += newItem.MP;
		this.attackRating += newItem.attackRating;
		this.armorRating += newItem.armorRating;
		this.speed += newItem.speed;
		this.immunity += newItem.immunity;
		this.firePower += newItem.firePower;
		this.fireResistance += newItem.fireResistance;
		this.waterPower += newItem.waterPower;
		this.waterResistance +=newItem.waterResistance;
		this.airPower += newItem.airPower;
		this.airResistance += newItem.airResistance;
		this.earthPower += newItem.earthPower;
		this.earthResistance += newItem.earthResistance;
		this.arcanePower += newItem.arcanePower;
		this.arcaneResistance += newItem.arcaneResistance;
		this.HPRegen += newItem.HPRegen;
		this.MPRegen += newItem.MPRegen;
		
		switch (newItem.typeIndex){
			case ItemManager.INDEX_SWORD_SHORT:
			case ItemManager.INDEX_SWORD_LONG:
			case ItemManager.INDEX_AXE:
			case ItemManager.INDEX_MACE:
			case ItemManager.INDEX_FLAIL:
			case ItemManager.INDEX_STAFF:
			case ItemManager.INDEX_POLE:	 equippedWeapon = newItem; break;
			
			case ItemManager.INDEX_SHIRT:
			case ItemManager.INDEX_ARMOR:
			case ItemManager.INDEX_ROBE:	 equippedChest = newItem; break;
			
			case ItemManager.INDEX_CAP:
			case ItemManager.INDEX_HELM:
			case ItemManager.INDEX_HEADBAND: equippedHead = newItem; break;
			
			case ItemManager.INDEX_GLOVE:
			case ItemManager.INDEX_SHIELD:
			case ItemManager.INDEX_BRACELET: equippedArms = newItem; break;
			
			case ItemManager.INDEX_BOOT:
			case ItemManager.INDEX_GREAVE:
			case ItemManager.INDEX_SANDAL:	 equippedFeet = newItem; break;
			
			case ItemManager.INDEX_RING:	 equippedRing = newItem; break;
			
			case ItemManager.INDEX_NECK:	 equippedNeck = newItem; break;		
		}
		Collections.sort(inventory, new EquipmentComparator());
	}
	
	// sets an item to no longer be equipped, updating the character's statistics
	public void unequipItem(Item oldItem){
		this.maxHP -= oldItem.HP;
		this.maxMP -= oldItem.MP;
		this.attackRating -= oldItem.attackRating;
		this.armorRating -= oldItem.armorRating;
		this.speed -= oldItem.speed;
		this.immunity -= oldItem.immunity;
		this.firePower -= oldItem.firePower;
		this.fireResistance -= oldItem.fireResistance;
		this.waterPower -= oldItem.waterPower;
		this.waterResistance -= oldItem.waterResistance;
		this.airPower -= oldItem.airPower;
		this.airResistance -= oldItem.airResistance;
		this.earthPower -= oldItem.earthPower;
		this.earthResistance -= oldItem.earthResistance;
		this.arcanePower -= oldItem.arcanePower;
		this.arcaneResistance -= oldItem.arcaneResistance;
		this.HPRegen -= oldItem.HPRegen;
		this.MPRegen -= oldItem.MPRegen;
		oldItem.isEquipped = false;
		
		switch (oldItem.typeIndex){
			case ItemManager.INDEX_SWORD_SHORT:
			case ItemManager.INDEX_SWORD_LONG:
			case ItemManager.INDEX_AXE:
			case ItemManager.INDEX_MACE:
			case ItemManager.INDEX_FLAIL:
			case ItemManager.INDEX_STAFF:
			case ItemManager.INDEX_POLE:	 equippedWeapon = null; break;
			
			case ItemManager.INDEX_SHIRT:
			case ItemManager.INDEX_ARMOR:
			case ItemManager.INDEX_ROBE:	 equippedChest = null; break;
			
			case ItemManager.INDEX_CAP:
			case ItemManager.INDEX_HELM:
			case ItemManager.INDEX_HEADBAND: equippedHead = null; break;
			
			case ItemManager.INDEX_GLOVE:
			case ItemManager.INDEX_SHIELD:
			case ItemManager.INDEX_BRACELET: equippedArms = null; break;
			
			case ItemManager.INDEX_BOOT:
			case ItemManager.INDEX_GREAVE:
			case ItemManager.INDEX_SANDAL:	 equippedFeet = null; break;
			
			case ItemManager.INDEX_RING:	 equippedRing = null; break;
			
			case ItemManager.INDEX_NECK:	 equippedNeck = null; break;		
		}
	}
	
	
	
	
	
	
	
	
	/* ========= COMBATANT METHODS ========== */

	// these methods adjust health and MANA during battle
	@Override
	public void damageHP(int damage){
		this.HP -= damage;
		if (this.HP <= 0) {
			this.HP = 0;
			this.conditions.clear();
		}
	}
	
	@Override
	public void damageMP(int damage){
		this.MP -= damage;
		if (this.MP <= 0) this.MP = 0;
	}
	
	@Override
	public void restoreHP(int amount){
		this.HP += amount;
		if (this.HP > this.maxHP) this.HP = this.maxHP;
	}
	
	@Override
	public void restoreMP(int amount){
		this.MP += amount;
		if (this.MP > this.maxMP) this.MP = this.maxMP;
	}
	
	// these methods return objects used during battle
	@Override
	public Ability getMove() {
		return this.move;
	}
	
	@Override
	public Item getUsedItem(){
		return this.usedItem;
	}
	
	@Override
	public ArrayList<Condition> getConditions() {
		return this.conditions;
	}
	
	// these methods return statistics for use during battle
	@Override
	public Integer getSpeed() {
		return this.speed;
	}

	@Override
	public boolean isMonster() {
		return false;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getHP() {
		return this.HP;
	}

	@Override
	public int getMP() {
		return this.MP;
	}

	@Override
	public int getArmor() {
		return this.armorRating;
	}

	@Override
	public int getAttack() {
		return this.attackRating;
	}

	@Override
	public int getFirePower() {
		return this.fireResistance;
	}

	@Override
	public int getFireResistance() {
		return this.fireResistance;
	}

	@Override
	public int getWaterPower() {
		return this.waterPower;
	}

	@Override
	public int getWaterResistance() {
		return this.waterResistance;
	}

	@Override
	public int getAirPower() {
		return this.airPower;
	}

	@Override
	public int getAirResistance() {
		return this.airResistance;
	}

	@Override
	public int getEarthPower() {
		return this.earthPower;
	}

	@Override
	public int getEarthResistance() {
		return this.earthResistance;
	}

	@Override
	public int getArcanePower() {
		return this.arcanePower;
	}

	@Override
	public int getArcaneResistance() {
		return this.arcaneResistance;
	}

	@Override
	public int getBonusMaxHP() {
		return this.bonusMaxHP;
	}

	@Override
	public int getBonusMaxMP() {
		return this.bonusMaxMP;
	}

	@Override
	public int getBonusArmorRating() {
		return this.bonusArmorRating;
	}

	@Override
	public int getBonusAttackRating() {
		return this.bonusAttackRating;
	}

	@Override
	public int getBonusFirePower() {
		return this.bonusFirePower;
	}

	@Override
	public int getBonusFireResistance() {
		return this.bonusFireResistance;
	}

	@Override
	public int getBonusWaterPower() {
		return this.bonusWaterPower;
	}

	@Override
	public int getBonusWaterResistance() {
		return this.bonusWaterResistance;
	}

	@Override
	public int getBonusAirPower() {
		return this.bonusAirPower;
	}

	@Override
	public int getBonusAirResistance() {
		return this.bonusAirResistance;
	}

	@Override
	public int getBonusEarthPower() {
		return this.bonusEarthPower;
	}

	@Override
	public int getBonusEarthResistance() {
		return this.bonusEarthResistance;
	}

	@Override
	public int getBonusArcanePower() {
		return this.bonusArcanePower;
	}

	@Override
	public int getBonusArcaneResistance() {
		return this.bonusArcaneResistance;
	}
	
	@Override
	public int getImmunity() {
		return this.immunity;
	}
	
	@Override
	public int getBonusImmunity() {
		return this.bonusImmunity;
	}

	@Override
	public int getHPRegen() {
		return this.HPRegen;
	}

	@Override
	public int getMPRegen() {
		return this.MPRegen;
	}

	@Override
	public int getBonusHPRegen() {
		return this.bonusHPRegen;
	}

	@Override
	public int getBonusMPRegen() {
		return this.bonusMPRegen;
	}

	@Override
	public int getMaxHP() {
		return this.maxHP;
	}

	@Override
	public int getMaxMP() {
		return this.maxMP;
	}
}