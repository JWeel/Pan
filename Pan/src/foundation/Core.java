package foundation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import battle.Ability;
import battle.Battle;
import battle.Condition;
import repository.GraphicsManager;
import repository.StoryManager;
import sorting.EquipmentComparator;
import sorting.PositionComparator;
import menus.Map;
import menus.Menu;
import character.Item;
import character.Monster;
import character.PlayerCharacter;
import graphics.Screen;
import graphics.Scroller;
import areas.Area;
import areas.TileEvent;

// handler of everything that happens in the program
public class Core {

	/*=============================== PRIVATE ===============================*/
	
	// 30 frames per second = 33.3 milliseconds sleep after each frame. 60 = 16.6
	private static final int FRAMERATE = 33;
	
	// how many pixels are moved in one movement
	private static final int CHARACTER_MOVEMENT_AMOUNT = 1;
	private static final int RUNNING_ADDED_AMOUNT = 2;
	private static final int CURSOR_MOVEMENT_AMOUNT = 4;
	
	// this is the window in which everything takes place
	private static Screen screen;	

	// this handles booleans for plot-related events (avoids cluttering this file))
	StoryManager storyManager;
	
	// whether an event has occurred (cancels any movement commands)
	private static boolean inEvent;

	
	/*================================ PUBLIC ================================*/
	
	// size of a tile in pixels
	public static final int TILE_SIZE = 16;
	
	// indexes of the elements
	public static final int INDEX_FIRE 	 = 0;
	public static final int INDEX_WATER  = 1;
	public static final int INDEX_AIR 	 = 2;
	public static final int INDEX_EARTH  = 3;
	public static final int INDEX_ARCANE = 4;
	
	// name of money
	public static final String MONEYNAME = "Quartz";
	
	// this is the current character
	public static PlayerCharacter currentCharacter;
	
	// this is the current character and their allies in order of battle positions
	public static ArrayList<PlayerCharacter> team;
	
	// this is area the character is currently in
	public static Area currentArea;
	
	// this is the interaction the character is currently busy with
	public static Interaction currentInteraction;
	
	// this is the battle the character is currently in
	public static Battle currentBattle;
	
	// this checks if movement is suspended (like for cut-scenes or transition)
	public static boolean movementSuspended;
	
	// multiplier used in resizing all pixels (! buggy behavior when not #.0 !)
	public static double multiplier = 2.0;
	
	// whether a battle has started (cancels any movement commands)
	public static boolean inBattle;
	
	// gets flagged when specific button is pressed
	public static boolean upPressed, downPressed, leftPressed, rightPressed,
		primaryPressed, secondaryPressed, accessPressed, 
		portPressed, starboardPressed;
	
	// randomizer object
	public static Random random = new Random();
	
	//
	public static int DEBUGCOUNT;
	
	

	
		
	
	/*================================ METHOD ================================*/

	// initializes the core-mechanics handler and all its components
	public Core(String windowName){
		
		//new GraphicsManager();
		
		screen = new Screen(windowName);
		
		// creates character and area
		// TODO load from file ?
		currentArea = new Area();
		currentCharacter = new PlayerCharacter();

		//
		Menu.map = new Map();
		Menu.map.multiplier = multiplier;
		Menu.scroller = screen.scroller;
		
		
		// let paint object know what to paint
		screen.painter.updateDimensions();
				
		//DEBUG start character at this location
		//currentCharacter.x = (int)(currentArea.COLUMNS*Screen.TILE_SIZE*multiplier/2) -currentCharacter.spriteIdleDown.getWidth();
		//currentCharacter.y = (int)(currentArea.COLUMNS*Screen.TILE_SIZE*multiplier/2) -currentCharacter.spriteIdleDown.getHeight()*2;
		currentCharacter.x = 172;
		currentCharacter.y = 172;
		
		screen.scroller.updateLastView(Scroller.INDEX_AREA);
		screen.scroller.updateLastView(Scroller.INDEX_MAP);
		
		updateMapStartPosition();
		
		Interaction.scroller = screen.scroller;
		
		storyManager = new StoryManager();
		
		
		
		
		team = new ArrayList<PlayerCharacter>();
		team.add(currentCharacter);
		
		// TEMPORARY STUFF:
		
		team.add(new PlayerCharacter());
		team.get(1).battlePosition=1;
		team.get(1).name = "Moca";
		team.get(1).avatar = GraphicsManager.mocaAvatar;
		team.get(1).spellbook.add(new Ability(6));
		team.get(1).spellbook.add(new Ability(10));
		//team.get(1).inventory.add(new Item(13));
		team.get(1).MPRegen = 8;
		team.get(1).conditions.add(new Condition(Condition.INDEX_BURN, 2));
//		team.get(1).HP = 1;
				
//		team.add(new PlayerCharacter());
//		team.get(2).battlePosition = 4;
//		team.get(2).name = "Joe";
//		team.get(2).level = 8;
//		team.add(new PlayerCharacter());
//		team.get(3).battlePosition = 3;
//		team.get(3).name = "FrankerZ";
//		team.get(3).level = 12;
//		team.add(new PlayerCharacter());
//		team.get(4).battlePosition = 2;
//		team.get(4).level = 16;
		
		
		Collections.sort(team, new PositionComparator());
		

		currentCharacter.HPRegen = 5;
		currentCharacter.name = "Pandoro";
		
		currentCharacter.conditions.add(new Condition(Condition.INDEX_CURSE, 2));
		currentCharacter.conditions.add(new Condition(Condition.INDEX_DISARM, 2));

		
		currentCharacter.spellbook.add(new Ability(2));
//		currentCharacter.spellbook.add(new Ability(3));
		currentCharacter.spellbook.add(new Ability(400));
		currentCharacter.spellbook.add(new Ability(450));
		currentCharacter.spellbook.add(new Ability(750));
//		currentCharacter.spellbook.add(new Ability(4));
		currentCharacter.spellbook.add(new Ability(5));
//		currentCharacter.spellbook.add(new Ability(6));
//		currentCharacter.spellbook.add(new Ability(7));
//		currentCharacter.spellbook.add(new Ability(8));
		currentCharacter.spellbook.add(new Ability(9));
//		currentCharacter.spellbook.add(new Ability(10));
		currentCharacter.spellbook.add(new Ability(11));
//		currentCharacter.spellbook.add(new Ability(12));
//		currentCharacter.spellbook.add(new Ability(13));
		
		for (PlayerCharacter pc : team){
			int r = random.nextInt(13);
			for (int i = 0; i < r; i++)
				pc.inventory.add(new Item(random.nextInt(14)));
			pc.inventory.add(new Item(2000));
			pc.inventory.add(new Item(5));
			pc.inventory.add(new Item(6));
//			for (Item i : pc.inventory)
//				i.isEquipped = 
//						//random.nextBoolean();
//						pc.canEquip(i.typeIndex);
			
			Collections.sort(pc.inventory, new EquipmentComparator());
		}
		
//		currentCharacter.inventory.add(new Item(0));
//		currentCharacter.inventory.add(new Item(1));
//		currentCharacter.inventory.add(new Item(2));
//		currentCharacter.inventory.add(new Item(3));
//		currentCharacter.inventory.get(3).isEquipped = true;
//		currentCharacter.inventory.add(new Item(4));
//		currentCharacter.inventory.add(new Item(5));
//		currentCharacter.inventory.add(new Item(6));
		
//		currentCharacter.inventory.add(new Item(7));
//		currentCharacter.inventory.add(new Item(8));
//		currentCharacter.inventory.add(new Item(9));
//		currentCharacter.inventory.add(new Item(10));
//		currentCharacter.inventory.add(new Item(11));
		
//		currentCharacter.inventory.add(new Item(12));
//		currentCharacter.inventory.add(new Item(13));
//		currentCharacter.inventory.add(new Item(13));
////		currentCharacter.inventory.get(14).quantity = 55;
//		currentCharacter.inventory.add(new Item(13));
////		currentCharacter.inventory.get(15).quantity = 6455;
//		currentCharacter.inventory.add(new Item(13));
////		currentCharacter.inventory.get(16).quantity = 145;
//		currentCharacter.inventory.add(new Item(13));
//		currentCharacter.inventory.add(new Item(2000));

	}

	
	//
	public void run(){

		// possibly put entire loop in another loop
		// change current loop condition to (notInTransition)
		
		while (true) { // TODO change to boolean (like isPaused ?)
			try {
				Thread.sleep(FRAMERATE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			handleButtonInput();
			
			if (inBattle) {
				loopBattle();
				
			} else {
				// TODO if inMenu start idle character animations
				if (Menu.menuDepth==0){
					
					if (currentInteraction != null) Interaction.handleInteraction();
					else loopCharacter();
					
					// both interaction and looping(walking) may trigger an event
					if (inEvent) handleEvent();
				} else {
					loopMenu();
				
					// TODO if (saving) save();
				}			
			}
			
			endIrrelevantAnimations();
			screen.repaint();	
		}
	}
	
	// sets booleans depending on what buttons are being pressed
	private void handleButtonInput(){
		
		/* ========== UP ========== */
		if (upPressed) {
			if (Menu.menuDepth != 0) Menu.cycleUpThroughMenu();
			if (inBattle) currentBattle.cycleUpThroughSub();
		}
		if (!upPressed) {
			Menu.justCycledUpThroughMenu = false;
			if (inBattle) currentBattle.justCycledUp = false;
		}
		
		/* ========= DOWN ========= */
		if (downPressed) {
			if (Menu.menuDepth != 0) Menu.cycleDownThroughMenu();
			if (inBattle) currentBattle.cycleDownThroughSub();
		}
		if (!downPressed) {
			Menu.justCycledDownThroughMenu = false;
			if (inBattle) currentBattle.justCycledDown = false;
		}
		
		/* ========= RIGHT ======== */
		if (rightPressed){
			if (Menu.menuDepth != 0) Menu.cycleRightThroughMenu();
			if (currentBattle != null) currentBattle.cycleRightThroughMenu();
		}
		if (!rightPressed){
			Menu.justCycledRightThroughMenu = false;
			if (currentBattle != null) currentBattle.justCycledRight = false;
		}
		
		/* ========= LEFT ========= */
		if (leftPressed){
			if (Menu.menuDepth != 0) Menu.cycleLeftThroughMenu();
			if (currentBattle != null) currentBattle.cycleLeftThroughMenu();
		}
		if (!leftPressed){
			Menu.justCycledLeftThroughMenu = false;
			if (currentBattle != null) currentBattle.justCycledLeft = false;
		}

		/* ======== PRIMARY ======= */
		if (primaryPressed) {
			if (Menu.menuDepth != 0) Menu.performMenuAction();
			else if (!inBattle){
				if (currentInteraction == null) Interaction.createInteraction();
			} else {
				if (currentBattle.getStageIndex() >= 0){
					currentBattle.performBattleAction();
				}
			}
			if (currentInteraction == null) primaryPressed = false;
		}
		
		/* ======= SECONDARY ====== */
		if (secondaryPressed) {
			if (Menu.menuDepth != 0) Menu.goUpOneMenuLevel();
			if (Menu.menuDepth ==0) currentCharacter.isRunning = true;
			if (inBattle) {
				if (currentBattle.stageIsPreparation())
					currentBattle.retrogressStage();
				else //if (currentBattle.stageIsEngagement())
					currentBattle.performBattleAction();
			}
			if (!currentCharacter.isRunning || inBattle) secondaryPressed = false;
		}
		
		/* ======== ACCESS ======== */
		if (accessPressed) {
			if (Menu.menuDepth ==0) { 
				Menu.openMenu();
				secondaryPressed = false;
			}
			else Menu.closeMenu();
			accessPressed = false;
		}
		
		/* ======== PORT ======== */
		if (portPressed) {
			if (Menu.subMenuIsCharacterRoot()) { 
				Menu.reorderCharacterToLeft();
				//portPressed = false;
			}
			portPressed = false;
		}
		
		/* ======== STARBOARD ======== */
		if (starboardPressed) {
			if (Menu.subMenuIsCharacterRoot()) { 
				Menu.reorderCharacterToRight();
				//starboardPressed = false;
			}
			starboardPressed = false;
		}
	}
	
	// moves the PlayerCharacter's coordinates and sets up animation
	private void loopCharacter(){
		
		if (movementSuspended) return;
		
		// starts new animation thread if none exists
		if (currentCharacter.animator == null) {
			currentCharacter.startAnimation();
		} // TODO maybe should be started in getAnimation() method ??
			// NO don't start in separate (paint) thread! but maybe somewhere else
		
		// sets direction character is facing so correct sprite will be used
		currentCharacter.updateDirection();
		
		// TODO in GS when character moves somewhere inaccessible, he is pushed
		//				to nearby pixel (useful when going through small gap)
		// possibly : if (straight in front has object)
		//				check (pixel*buffer) next to it on both sides
		//				if that place is clear, move in that direction
		
		int runModifier = 0;
		if (currentCharacter.isRunning) runModifier = RUNNING_ADDED_AMOUNT;
		
		for (int i = 0; i < (CHARACTER_MOVEMENT_AMOUNT + runModifier)* multiplier; i++){
			currentCharacter.moveCharacter();
			screen.scroller.centerViewAroundCharacter();
			
			checkForEvent();
			if (inEvent) break;
			
			checkForBattle();
			if (inBattle) break;
		}		
	}
	
	// checks if event was triggered on entered tile
	private void checkForEvent(){
		inEvent = currentArea.getTileAtPixel(currentCharacter.x, currentCharacter.y).hasEvent();
	}
	
	// performs actions based on type of event
	private void handleEvent(){
		
		inEvent = false;
		
		TileEvent currentEvent = currentArea.getTileAtPixel(currentCharacter.x, currentCharacter.y).event;
		switch (currentEvent.typeIndex){
			case TileEvent.INDEX_TRANSITION:
								
				movementSuspended = true;
				//currentCharacter.spriteOverride = currentCharacter.getAnimationSprite();
				currentCharacter.stopAnimation();
				
//				upPressed = false;
//				downPressed = false;
//				leftPressed = false;
//				rightPressed = false;
				
				currentArea = currentEvent.transitionArea;
				
				currentCharacter.x = 150;
				currentCharacter.y = 350;
				//TODO currentCharacter.x= currentEvent.transitionArea.startPosition
				//		possibly >> .startPositionFrom(currentArea)
				// or make index of integers, call them linked areas, make method
				// that switches index and returns coordinates based on linked area
				// also make sure to set direction
				
				updateMapStartPosition();
				screen.scroller.updateLastView(Scroller.INDEX_AREA);
				screen.scroller.centerViewAroundCharacter();

				if (currentArea.darkensOnTransition) screen.painter.darkenScreen();
				else movementSuspended = false;

				break;
			// case TileEvent.INDEX_FORCED_DIALOGUE:
				// create Interaction
			// case TileEvent.INDEX_FORCED_BATTLE:
				// create Battle
			// case TileEvent.INDEX_MODIFY_AREA:
				// i.e. when object is pushed on something, opening locked door
		}
	}
	
	// operates according to what menu is being browsed
	private void loopMenu(){
		
		// map menu has additional menu mechanics
		if (Menu.menuDepth == Menu.INDEX_MAP_MENU){
			for (int i = 0; i < CURSOR_MOVEMENT_AMOUNT * multiplier; i++){
				Menu.map.moveMap();
				screen.scroller.centerViewAroundMapCursor();
			}
		}
		
		// fix for camera view when leaving map with secondary button
		if (Menu.mapMenuWasCancelled) { 
			Menu.mapMenuWasCancelled = false;
			screen.scroller.centerViewAroundCharacter();
			screen.scroller.updateLastView(Scroller.INDEX_MAP);
			updateMapStartPosition();
		}
		
		// if (saving) ?
	
		// starts new animation thread if none exists
		if (Menu.animator == null) {
			Menu.startAnimation();
		}
	}
	
	// sets the world map starting position
	public static void updateMapStartPosition(){
		Menu.map.matchCoordinatesToLocalArea(
				(int)(currentArea.worldCoordinatesX * multiplier),
				(int)(currentArea.worldCoordinatesY * multiplier));
	}
	
	// checks if an encounter can be randomly created (and creates it if so)
	private void checkForBattle(){
		if (currentArea.encounterRate != 0 //)
				&& (downPressed || upPressed || rightPressed || leftPressed)
				
				// DEBUG extra condition
				&& currentCharacter.isRunning){
			
			int ran = random.nextInt(1000);
			if (ran < currentArea.encounterRate) {
				// get encounter from area to create new battle
				currentBattle = new Battle(currentArea.getEncounter(), currentCharacter);
				
				movementSuspended = true;
				
				// set boolean to true so the main loop will focus on this battle
				inBattle = true;
			}			
		}		
	}
	
	//
	private void loopBattle(){
		
//		if (!inBattle) {
//			currentBattle = null;
//			screen.painter.currentBattle = null;
//			
//			return;
//		}
		
		
		screen.scroller.getViewport().setViewPosition(new Point(0,0));
		
		
		for (Monster monster: currentBattle.monsters){
			if (monster.animator == null) monster.startAnimation();
		}
		

		if (currentBattle.getStageIndex() == -2){
			currentBattle.advanceStage();
			screen.painter.darkenScreen();
		}
		
		if (!movementSuspended && currentBattle.getStageIndex() == -1)
			currentBattle.advanceStage();
		
		if (currentBattle.stageIsPreparation()){
			if (currentBattle.cursorAnimator == null 
					|| currentBattle.pointerAnimator == null) 
						currentBattle.startAnimation();
		} else {
			if (currentBattle.cursorAnimator != null 
					|| currentBattle.pointerAnimator != null) 
						currentBattle.stopAnimation();
		}
		
		
//		if (currentBattle.stageIsEngagement()){
//			
//		}
		
		
//		if (currentBattle.monsters != null)
//			System.err.println(currentBattle.monsters.get(currentBattle.battleMenuTarget).name);
		
	}
	
	//
	public static void endBattle(){
		inBattle = false;
		currentBattle = null;
		screen.painter.darkenScreen();
	}
	
	// ends animations that are no longer necessary
	private void endIrrelevantAnimations(){
		// if no longer walking, end animation thread
		// TODO this means only one idle sprite! change in future
		if ((!upPressed 
				&& !downPressed 
					&& !leftPressed 
						&& !rightPressed)
				|| Menu.menuDepth != 0) {
			currentCharacter.stopAnimation();
		}
		
		// if menu is closed, end animation thread
		if (Menu.menuDepth == 0) Menu.stopAnimation();
	}
	
	// returns the top-left corner coordinates of the current viewed area
	public static Point getViewCoordinates(){
		return screen.scroller.getViewport().getViewPosition();
	}
	
	// returns what is used as abbreviation for the health statistic
	public static String getHealthName(){
		return "H";
	}
	
	// returns what is used as abbreviation for the MANA statistic
	public static String getManaName(){
		return "M";
	}
}