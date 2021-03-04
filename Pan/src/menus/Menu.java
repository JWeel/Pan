package menus;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import battle.Ability;
import character.Item;
import repository.GraphicsManager;
import repository.ItemManager;
import sorting.PositionComparator;
import foundation.Core;
import graphics.Animator;
import graphics.Scroller;

// this object stores values that concern the menu interface
public class Menu {
	
	// image of menu item selection
	private static final BufferedImage selection1 = GraphicsManager.menuItemSelection1;
	private static final BufferedImage selection2 = GraphicsManager.menuItemSelection2;
	private static final BufferedImage selection3 = GraphicsManager.menuItemSelection3;
	private static final BufferedImage selection4 = GraphicsManager.menuItemSelection4;
	private static final BufferedImage selection5a = GraphicsManager.menuItemSelection5a;
	private static final BufferedImage selection5b = GraphicsManager.menuItemSelection5b;
	private static final BufferedImage selection5c = GraphicsManager.menuItemSelection5c;
	private static final BufferedImage selection5d = GraphicsManager.menuItemSelection5d;
	private static BufferedImage selection5;

	//
	private static final int AMOUNT_OF_BROWSE_ANIMATIONS = 2;
	private static final int MENU_ANIMATION_INTERVAL = 450;
	
//	private static final int MENU_SELECTION_OFFSET_X = 7;
//	private static final int MENU_SELECTION_OFFSET_Y = 5;

	public static final int MENU_ITEM_SIZE = 24;
	
	public static final int CHARACTER_PANEL_BAR_SIZE = 32;
	
	public static final int CHARACTER_AVATAR_SIZE = 22;
	public static final int INVENTORY_ICON_SIZE = 14;
	public static final int INVENTORY_SUB_ITEM_SIZE = 12;
	
	public static final int MENU_ITEM_OFFSET_Y = 10;
	public static final int MENU_ITEM_OFFSET_X = 1;
	public static final int MENU_ITEM_LABEL_OFFSET = 10;

	public static final int MENU_FADE_OFFSET = 3;
	
	// indexes of menus
	public static final int INDEX_BASE_MENU = 1;
	
	public static final int INDEX_SPELLS_MENU = 2;	
//	public static final int INDEX_CHARACTER_SUB1 = 21;
//	public static final int INDEX_CHARACTER_SUB2 = 22;
//	public static final int INDEX_CHARACTER_SUB3 = 23;
//	public static final int INDEX_CHARACTER_SUB4 = 24;
//	public static final int INDEX_CHARACTER_SUB5 = 25;

	public static final int INDEX_ITEMS_MENU = 3;
//	public static final int INDEX_ITEMS_SUB1 = 31;
//	public static final int INDEX_ITEMS_SUB2 = 32;
//	public static final int INDEX_ITEMS_SUB3 = 33;
//	public static final int INDEX_ITEMS_SUB4 = 34;
//	public static final int INDEX_ITEMS_SUB5 = 35;
	
	public static final int INDEX_MAP_MENU = 4;
	//public static final int INDEX_LOCAL_MAP_MENU = 41; // in case it is used
	public static final int INDEX_SAVE_MENU = 5;
	public static final int INDEX_SETTINGS_MENU = 6;
	
	// indexes of fade outs
	public static final int INDEX_FADEOUT_ITEM = 1;
	public static final int INDEX_FADEOUT_MENU = 2;
	
	// checks if the secondary button was used to leave the map
	public static boolean mapMenuWasCancelled;
	
	// an animation thread that handles switching between images
	public static Animator animator;
	
	// keeps track of what item in current menu is selected
	public static int menuItem = 0;
	public static int menuCharacterIndex = 0;
	public static int subMenuItem = 0;
	
	// keeps track of in which sub-menu is being browsed
	public static int menuDepth = 0;
	
	// keeps track of how deep within a sub-menu is being browsed
	private static int subMenuDepth = 0;
	
	// keeps track of page in spell book of spell sub-menu
	public static int menuSpellbookPage = 0;
	
	// stores properties of an item so they don't have to be calculated by painter
	public static boolean inventoryItemIsEquippable;
	
	// a map object that handles the map parts of the menu
	public static Map map;
	
	//
	public static Scroller scroller;
	
	// is flagged when moving through menu item to avoid continuous cycling
	public static boolean justCycledUpThroughMenu, justCycledDownThroughMenu,
						justCycledRightThroughMenu,justCycledLeftThroughMenu;
	
		

	
	//
	public Menu(){
		System.err.println("Hello good sir");
	}
	
	// opens menu 
	public static void openMenu(){
		menuDepth = INDEX_BASE_MENU;
	}
	
	// closes menu immediately. saves last used menu item if applicable
	//			TODO	right now will default to 0 for not-yet-set switch commands
	public static void closeMenu(){
		if (menuDepth != INDEX_BASE_MENU) menuItem = getEnteredMenuItemIndex();
		if (menuDepth == INDEX_MAP_MENU) {
			scroller.updateLastView(Scroller.INDEX_MAP);
			Core.updateMapStartPosition();
		}
		if (menuDepth == INDEX_ITEMS_MENU) clearItemComparisons();
		menuDepth = 0;
		subMenuDepth = 0;
		menuSpellbookPage = 0;
	}
	
	// executes the action of the currently selected menu item
	public static void performMenuAction(){
		switch (menuDepth){
			// root menu
			case INDEX_BASE_MENU:
				switch (menuItem){
					case 0: enterSubMenu(INDEX_SPELLS_MENU);
							break;
					case 1:	enterSubMenu(INDEX_ITEMS_MENU);
							break;
					case 2:	enterSubMenu(INDEX_MAP_MENU);
							stopAnimation();
							break;
					case 3:	// save menu
							break;
					case 4: // settings menu
							GraphicsManager.changeMenuColor();
							break;
				} break;
			// statistics menu
			case INDEX_SPELLS_MENU:
				switch (subMenuDepth){
					case 0:	enterSpellMenu();
							break;
				} break;
			case INDEX_ITEMS_MENU:
				switch (subMenuDepth){
					case 0:	enterItemsMenu();
							break;
					case 1: enterItem();
							break;
					case 2: performItemAction();
							break;
				} break;
		}
	}
	
	// sets the menu to one matching given index
	private static void enterSubMenu(int menuIndex){
		menuDepth = menuIndex;
		menuItem = 0;
	}
	
	// sets the menu to one matching given index
	private static void enterSpellMenu(){
		subMenuDepth = 1;
		//menuItem = 0;
		stopAnimation();
	}
	
	// sets the menu to one matching given index
	private static void enterItemsMenu(){
		subMenuDepth = 1;
		//menuItem = 0;
		updateInventoryItemComparisons();
		stopAnimation();
	}
	
	// sets menu to a sub-menu where actions can be performed on inventory item
	private static void enterItem(){
		if (Core.team.get(menuCharacterIndex).inventory.isEmpty()) return;
		subMenuItem = 0;
		subMenuDepth = 2;
		updateInventoryItemProperties();
		stopAnimation();
	}
	
	// performs an action on selected inventory item
	private static void performItemAction(){
		Item item = getInventoryItem();

		switch (subMenuItem){
			case 0:
				if (inventoryItemIsEquippable){
					if (!getInventoryItem().isEquipped) Core.team.get(menuCharacterIndex).equipItem(getInventoryItem());
					else Core.team.get(menuCharacterIndex).unequipItem(getInventoryItem());
					menuItem = Core.team.get(menuCharacterIndex).inventory.indexOf(item);
					updateInventoryItemComparisons();
					updateInventoryItemSelector();
					stopAnimation();
					break;
				} else if (item.usableInArea){
					
					break;
				} // else go to next case
			case 1:
				break;
			case 2:
				break;
		}
	}
	
	// changes selected menu item by going up
	public static void cycleUpThroughMenu(){
		if (getAmountOfMenuItems() <= 0) return;
		if (!justCycledUpThroughMenu) {
			if (subMenuIsItemMenuInventory()) {
				menuItem -= 5;
				if (menuItem < 0) {
					int nRows = (int)(Math.ceil((double)(getAmountOfMenuItems()+1) / 5));
					int maxPos = (nRows) * 5 + menuItem;
					menuItem = maxPos < (getAmountOfMenuItems()+1) ? maxPos : maxPos -5;
				}	

			} else if (subMenuIsSpellMenuSpellbook()){
				if (--menuItem < 0) menuItem = getAmountOfMenuItems()-1;
			} else if (subMenuItemIsSelected()) {
				if (--subMenuItem < 0) subMenuItem = getAmountOfMenuItems();
				updateInventoryItemSelector();
			} else {
				if (--menuItem < 0) menuItem = getAmountOfMenuItems();
			}
			stopAnimation();
			justCycledUpThroughMenu = true;
		}
	}
	
	// changes selected menu item by going down
	public static void cycleDownThroughMenu(){
		if (getAmountOfMenuItems() <= 0) return;
		if (!justCycledDownThroughMenu){
			if (subMenuIsItemMenuInventory()) {
				menuItem += 5;
				if (menuItem > getAmountOfMenuItems()) menuItem = menuItem%5;
				
			} else if (subMenuIsSpellMenuSpellbook()){			
				if (++menuItem > getAmountOfMenuItems()-1) menuItem = 0;
			} else if (subMenuItemIsSelected()) {
				if (++subMenuItem > getAmountOfMenuItems()) subMenuItem = 0;
				updateInventoryItemSelector();
			} else {
				if (++menuItem > getAmountOfMenuItems()) menuItem = 0;
			}
			stopAnimation();
			justCycledDownThroughMenu = true;
		}
	}
	
	// changes selected menu item by going right
	public static void cycleRightThroughMenu(){
		if (!menuIsAboutCharacters()) return;
		if (!justCycledRightThroughMenu) {
			if (subMenuIsCharacterRoot()) {
				if (++menuCharacterIndex > getAmountOfHorizontalMenuItems()) 
					menuCharacterIndex = 0;
				menuItem = 0;
			} else if (subMenuIsItemMenuInventory()){
				if (getAmountOfMenuItems() <= 0) return;

				int currentRow = menuItem / 5 + 1;
				int rightMost = currentRow * 5 -1;
				if (rightMost > getAmountOfMenuItems()) rightMost = getAmountOfMenuItems();
				int leftMost = (currentRow-1) * 5;
				if (++menuItem > rightMost) menuItem = leftMost;
				
			} else if (subMenuIsSpellMenuSpellbook()){
				if (++menuSpellbookPage > Core.team.get(menuCharacterIndex).spellbook.size()/5)
					menuSpellbookPage = 0;
				if (menuItem >= getAmountOfMenuItems()) menuItem = 0;
			}
			stopAnimation();
			justCycledRightThroughMenu = true;
		}
	}
	
	// changes selected menu item by going left
	public static void cycleLeftThroughMenu(){
		if (!menuIsAboutCharacters()) return;
		if (!justCycledLeftThroughMenu){
			if (subMenuIsCharacterRoot()) {
				if (--menuCharacterIndex < 0) 
					menuCharacterIndex = getAmountOfHorizontalMenuItems();
				menuItem = 0;
			} else if (subMenuIsItemMenuInventory()){
				if (getAmountOfMenuItems() <= 0) return;

				int currentRow = menuItem / 5 + 1;
				int rightMost = currentRow * 5 -1;
				if (rightMost > getAmountOfMenuItems()) rightMost = getAmountOfMenuItems();
				int leftMost = (currentRow-1) * 5;
				if (--menuItem < leftMost) menuItem = rightMost;
				
			} else if (subMenuIsSpellMenuSpellbook()){
				if (--menuSpellbookPage < 0)
					menuSpellbookPage = Core.team.get(menuCharacterIndex).spellbook.size()/5;
				if (menuItem >= getAmountOfMenuItems()) menuItem = 0;
			}
			stopAnimation();
			justCycledLeftThroughMenu = true;
		}
	}
	
	// reorders characters with current selected swapping with left
	public static void reorderCharacterToLeft(){
		if (menuCharacterIndex == 0) {
			Core.team.get(Core.team.size()-1).battlePosition = menuCharacterIndex;
			Core.team.get(menuCharacterIndex).battlePosition = Core.team.size()-1;
		} else {
			Core.team.get(menuCharacterIndex-1).battlePosition = menuCharacterIndex;
			Core.team.get(menuCharacterIndex).battlePosition = menuCharacterIndex-1;
		}
		Collections.sort(Core.team, new PositionComparator());
		justCycledLeftThroughMenu = false;
		cycleLeftThroughMenu();
	}
	
	// reorders characters with current selected swapping with right
	public static void reorderCharacterToRight(){
		if (menuCharacterIndex == Core.team.size()-1) {
			Core.team.get(0).battlePosition = menuCharacterIndex;
			Core.team.get(menuCharacterIndex).battlePosition = 0;
		} else {
			Core.team.get(menuCharacterIndex+1).battlePosition = menuCharacterIndex;
			Core.team.get(menuCharacterIndex).battlePosition = menuCharacterIndex+1;
		}
		Collections.sort(Core.team, new PositionComparator());
		justCycledRightThroughMenu = false;
		cycleRightThroughMenu();
	}
	
	// gets the amount of menu items in current sub-menu
	public static int getAmountOfMenuItems(){
		return getAmountOfMenuItems(menuDepth);
	}
	
	// gets the amount of menu items in given sub-menu
	public static int getAmountOfMenuItems(int depthLevel){
		switch (depthLevel) {
			case INDEX_BASE_MENU: return 4;
			
			case INDEX_SPELLS_MENU:
				switch (subMenuDepth){
					case 1:
						
						
						if ((menuSpellbookPage +1) * 5 <= Core.team.get(menuCharacterIndex).spellbook.size())
							return 5;
						else return Core.team.get(menuCharacterIndex).spellbook.size()%5;
						
				}
				
			case INDEX_ITEMS_MENU:
				switch (subMenuDepth){
					case 1: return Core.team.get(menuCharacterIndex).inventory.size()-1;
					
					case 2: if (inventoryItemIsEquippable || getInventoryItem().usableInArea) return 2;
							else return 1;
				}
			
			
			default: return 0;
		}
	}
	
	// gets the amount of menu items in current sub-menu
	private static int getAmountOfHorizontalMenuItems(){
		switch (menuDepth){
			case INDEX_SPELLS_MENU:	
			case INDEX_ITEMS_MENU: return Core.team.size()-1;
			default: return 0;
		}
	}
	
	// updates comparisons of item so the painter does not have to calculate
	private static void updateInventoryItemComparisons(){
		if (Core.team.get(menuCharacterIndex).inventory.isEmpty()) return;
		
		for (Item item : Core.team.get(menuCharacterIndex).inventory){
			if (item.isMisc()) continue;
			if (!Core.team.get(menuCharacterIndex).canEquip(item.typeIndex)) continue; 
			
			Item equipped = Core.team.get(menuCharacterIndex).getEquippedItem(item.typeIndex);
			if (equipped != null)
				item.bonusComparisons = ItemManager.getItemComparisons(item, equipped);
			
			// if nothing equipped, set all comparisons to true (meaning better)
			else {
				ArrayList<Boolean> allTrue = new ArrayList<Boolean>();
				for (int i = 0; i < item.bonusStrings.size(); i++)
					allTrue.add(true);
				item.bonusComparisons = allTrue;
			}
		}
	}
	
	// clears item comparisons because there is no reason to store them
	private static void clearItemComparisons(){
		if (Core.team.get(menuCharacterIndex).inventory.isEmpty()) return;
		for (Item item : Core.team.get(menuCharacterIndex).inventory)
			item.bonusComparisons = null;
	}
	
	// updates properties of item so the painter does not have to calculate
	private static void updateInventoryItemProperties(){
		inventoryItemIsEquippable =
			Core.team.get(menuCharacterIndex).canEquip(getInventoryItem().typeIndex);
		updateInventoryItemSelector();
	}
	
	// updates the selector for inventory item selection
	private static void updateInventoryItemSelector(){
		if (getInventoryItem().isMisc() && getInventoryItem().usableInArea){
			if (subMenuItem == 0) selection5 = selection5a;
			else selection5 = selection5d;
		} else if (inventoryItemIsEquippable && subMenuItem == 0){
			if (getInventoryItem().isEquipped) selection5 = selection5c;
			else selection5 = selection5b;
		} else selection5 = selection5d;
	}
	
	// returns image of given menu
	public static BufferedImage getMenuImage(){
		return getMenuImage(menuDepth);
	}
	
	// returns image of current menu
	public static BufferedImage getMenuImage(int depthLevel){
		switch (depthLevel){
			case INDEX_BASE_MENU:
				return GraphicsManager.menu1;
			case INDEX_SPELLS_MENU:
				return GraphicsManager.menu2;
			case INDEX_ITEMS_MENU: 
				return GraphicsManager.menu3;
			default: return null;
		}
	}
	
	// returns image of current menu
	public static BufferedImage getExtraMenu(){
		return GraphicsManager.menu4;
	}
	
	// returns image of menu fader
	public static BufferedImage getMenuFadeImage(int index){
		switch (index){
			case INDEX_FADEOUT_ITEM: return GraphicsManager.menuItemFadeOut1;
			case INDEX_FADEOUT_MENU: return GraphicsManager.menuItemFadeOut2;
			default: return null;
		}
	}
	
	// returns image of mug shot fader
	public static BufferedImage getMenuAvatarFade(){
		return GraphicsManager.menuItemFadeOut3;
	}
	
	// returns menu item from which the current sub-menu was entered (for graphics)
	public static int getEnteredMenuItemIndex(){
		switch (menuDepth){
			case INDEX_SPELLS_MENU: return 0;
			case INDEX_ITEMS_MENU: return 1;
			case INDEX_MAP_MENU: return 2;
			default: return 0;
		}
	}
	
	// possibly temporary if replace with image?
	public static String getMenuItemName(int item){
		switch (item){
			//case 0: return Core.currentCharacter.name;
			case 0: return "Spells"; 
			case 1: return "Items";
			case 2: return "Map";
			case 3: return "Save";
			case 4: return "Settings";
			default: return null;
		}
	}

	// returns whether the sub menu is the spell-book and not the inventory
	public static boolean subMenuIsSpellbook(){
		return menuDepth == INDEX_SPELLS_MENU;
	}
	
	// returns whether the menu is either spell-book or inventory
	public static boolean menuIsAboutCharacters(){
		return menuDepth == INDEX_SPELLS_MENU ||
				menuDepth == INDEX_ITEMS_MENU;
	}
	
	// returns whether the menu is either spell-book or inventory
	public static boolean subMenuIsNotRoot(){
		return (menuDepth == INDEX_SPELLS_MENU ||
				menuDepth == INDEX_ITEMS_MENU) 
				&& subMenuDepth != 0;
	}
	
	// returns index of super-menu
	private static int getSuperMenu(){
		switch (menuDepth){
			case INDEX_BASE_MENU: return 0; 
			case INDEX_SPELLS_MENU: return INDEX_BASE_MENU;
			case INDEX_ITEMS_MENU: return INDEX_BASE_MENU;
			case INDEX_MAP_MENU: return INDEX_BASE_MENU;
			default: return 0;
		}
	}
	
	// sets menu depth level to super-menu of current sub-menu
	public static void goUpOneMenuLevel(){
		if (menuDepth == INDEX_MAP_MENU) mapMenuWasCancelled = true;
		if (getSuperMenu() == 0) closeMenu();
		else {
			// base of sub-menu
			if (subMenuDepth ==0) {
				menuItem = getEnteredMenuItemIndex();
				menuDepth = getSuperMenu();
			
			// sub of sub-menu
			} else {
				
				if (subMenuIsItemMenuInventory()) clearItemComparisons();
				
				subMenuDepth --;
				menuSpellbookPage = 0;
			}
			
			stopAnimation();
		}
	}
		
	// returns the width of super-menu
	public static int getSubMenuOffset(){
		BufferedImage superMenuImage = getMenuImage(getSuperMenu());
		if (superMenuImage != null) return superMenuImage.getWidth() +2;
		else return 0;
	}
	
	// returns whether the current sub-menu is the root of a character menu
	public static boolean subMenuIsCharacterRoot(){
		if (!menuIsAboutCharacters()) return false;
		return subMenuDepth == 0;
	}
	
	// returns whether the current sub-menu is the inventory of item menu
	public static boolean subMenuIsSpellMenuSpellbook(){
		return menuDepth == INDEX_SPELLS_MENU && subMenuDepth == 1;
	}
	
	// returns whether the current sub-menu is the inventory of item menu
	public static boolean subMenuIsItemMenuInventory(){
		return menuDepth == INDEX_ITEMS_MENU && subMenuDepth == 1;
	}
	
	// returns whether something has been selected in the sub-menu of spell/items
	public static boolean subMenuItemIsSelected(){
		return subMenuDepth == 2;
	}
	
	// returns the spell currently selected in spell-menu spellbook
	public static Ability getSpellbookSpell(){
		if (menuDepth != INDEX_SPELLS_MENU) return null;
		else if (Core.team.get(menuCharacterIndex).spellbook.isEmpty()) return null;
		else return Core.team.get(menuCharacterIndex).spellbook.get(menuItem + menuSpellbookPage*5);
	}
	
	// returns the item currently selected in item-menu inventory
	public static Item getInventoryItem(){
		if (menuDepth != INDEX_ITEMS_MENU) return null;
		else if (Core.team.get(menuCharacterIndex).inventory.isEmpty()) return null;
		else return Core.team.get(menuCharacterIndex).inventory.get(menuItem);
	}

	// returns the elemental badge for spell
	public static BufferedImage getElementBadge(int elementIndex){
		switch (elementIndex){
			case Core.INDEX_FIRE:	return GraphicsManager.badgeFire;
			case Core.INDEX_WATER:	return GraphicsManager.badgeWater;
			case Core.INDEX_AIR:	return GraphicsManager.badgeAir;
			case Core.INDEX_EARTH:	return GraphicsManager.badgeEarth;
			case Core.INDEX_ARCANE:	return GraphicsManager.badgeArcane;
			default: return null;
		}
	}
	
	// returns the elemental power badge for spell
	public static BufferedImage getElementPowerBadge(int elementIndex){
		switch (elementIndex){
			case Core.INDEX_FIRE:	return GraphicsManager.badgeFirePower;
			case Core.INDEX_WATER:	return GraphicsManager.badgeWaterPower;
			case Core.INDEX_AIR:	return GraphicsManager.badgeAirPower;
			case Core.INDEX_EARTH:	return GraphicsManager.badgeEarthPower;
			case Core.INDEX_ARCANE:	return GraphicsManager.badgeArcanePower;
			default: return null;
		}
	}
	
	// returns the elemental resistance badge for spell
	public static BufferedImage getElementResistanceBadge(int elementIndex){
		switch (elementIndex){
			case Core.INDEX_FIRE:	return GraphicsManager.badgeFireResistance;
			case Core.INDEX_WATER:	return GraphicsManager.badgeWaterResistance;
			case Core.INDEX_AIR:	return GraphicsManager.badgeAirResistance;
			case Core.INDEX_EARTH:	return GraphicsManager.badgeEarthResistance;
			case Core.INDEX_ARCANE:	return GraphicsManager.badgeArcaneResistance;
			default: return null;
		}
	}
	
	// returns the reach of an ability shown in spell-book tool-tip
	public static BufferedImage getSpellbookSpellReachIcon(){
		switch (getSpellbookSpell().reach){
			case 0: return GraphicsManager.reachSingle;
			case 1: return GraphicsManager.reachDimThree;
			case 2: return GraphicsManager.reachDimFive;
			case 3: return GraphicsManager.reachPureThree;
			case 4: return GraphicsManager.reachPureFive;
			case 5: return GraphicsManager.reachRightTwo;
			case 6: return GraphicsManager.reachRightFour;
			case 7: return GraphicsManager.reachLeftTwo;
			case 8: return GraphicsManager.reachLeftFour;

			default: return null;
		}
	}
	
	// returns the attack rating badge
	public static BufferedImage getAttackBadge(){
		return GraphicsManager.badgeAttack;
	}	
	
	// returns the armor rating badge
	public static BufferedImage getArmorBadge(){
		return GraphicsManager.badgeArmor;
	}	
	
	// returns the speed rating badge
	public static BufferedImage getSpeedBadge(){
		return GraphicsManager.badgeSpeed;
	}	
	
	// returns the immunity rating badge
	public static BufferedImage getImmunityBadge(){
		return GraphicsManager.badgeImmunity;
	}	
	
	// returns a check-mark image for equipped items
	public static BufferedImage getCheckmark(){
		return GraphicsManager.menuCheckmark;
	}	
	
	// returns an arrow pointing up
	public static BufferedImage getUpArrow(){
		return GraphicsManager.menuArrow1;
	}	
	
	// returns an arrow pointing down
	public static BufferedImage getDownArrow(){
		return GraphicsManager.menuArrow2;
	}
	
	// returns image of bar outline
	public static BufferedImage getOutlineBar(){
		return GraphicsManager.battleMenuBar;
	}
	
	// returns image of life bar
	public static BufferedImage getLifeBar(){
		return GraphicsManager.battleMenuBarLife;
	}
	
	// returns image of MANA bar
	public static BufferedImage getManaBar(){
		return GraphicsManager.battleMenuBarMana;
	}
	
	// returns image of damage bar
	public static BufferedImage getDamageBar(){
		return GraphicsManager.battleMenuBarDamage;
	}

	// returns a panel that displays the page number of the sub menu
	public static BufferedImage getMenuPagePanel(){
		if (!subMenuIsSpellMenuSpellbook()) return null;
		if (Core.team.get(menuCharacterIndex).spellbook.size()/5 == 0) return null;
		else return GraphicsManager.menu5;
	}
	
	// returns a certain sprite for animating
	public static BufferedImage getSelectionSprite(){
		if (animator == null) return null;
		
		BufferedImage selector = null;
		switch (menuDepth){
			case INDEX_BASE_MENU: selector = selection1;
				break;
			case INDEX_SPELLS_MENU:
				switch (subMenuDepth){
					case 0: selector = selection2; break;
					case 1: selector = selection3; break;
				} break;
				
			case INDEX_ITEMS_MENU:
				switch (subMenuDepth){
					case 0: selector = selection2; break;
					case 1: selector = selection4; break;
					case 2: selector = selection5; break;
				} break;
		}
		
		switch(animator.getAnimationIndex()){
			case 0: return selector;
			case 1: return null;
			// case 2: return enlarged ( selection1 ); ??
			default: return null;
		}
	}
	
	// returns the x coordinate for the selector sprite
	public static int getSelectionX(){
		switch (menuDepth){
			case INDEX_BASE_MENU: 
				return (int)(23 * Core.multiplier);
			case INDEX_SPELLS_MENU:
				switch (subMenuDepth){
					case 0:	return (int)((89 + menuCharacterIndex * CHARACTER_AVATAR_SIZE)* Core.multiplier);
					case 1: return (int)((87)* Core.multiplier);
				}
			case INDEX_ITEMS_MENU: 
				switch (subMenuDepth){
					case 0:	return (int)((89 + menuCharacterIndex * CHARACTER_AVATAR_SIZE)* Core.multiplier);
					case 1: return (int)((130 + menuItem%5 * INVENTORY_ICON_SIZE)* Core.multiplier);
					case 2: return (int)(156 * Core.multiplier);
				}
			default: return 0;
		}
	}
	
	// returns the y coordinate for the selector sprite
	public static int getSelectionY(){
		switch (menuDepth){
			case INDEX_BASE_MENU: 
				return (int)((37 + menuItem * MENU_ITEM_SIZE) * Core.multiplier);
			case INDEX_SPELLS_MENU:
				switch (subMenuDepth){
					case 0:	return (int)(37 * Core.multiplier);
					case 1: return (int)((103 + menuItem * MENU_ITEM_LABEL_OFFSET)* Core.multiplier);
				}
			case INDEX_ITEMS_MENU: 
				switch (subMenuDepth){
					case 0:	return (int)(37* Core.multiplier);
					case 1: return (int)((107 + menuItem/5 * INVENTORY_ICON_SIZE) * Core.multiplier);
					case 2: return (int)((110 + subMenuItem * INVENTORY_SUB_ITEM_SIZE) * Core.multiplier);
				}

			default: return 0;
		}
	}
	
	// creates animation thread and starts it
	public static void startAnimation(){
		animator = new Animator(AMOUNT_OF_BROWSE_ANIMATIONS, MENU_ANIMATION_INTERVAL);
		animator.thread = new Thread(animator);
		animator.thread.start();
	}
	
	// destroys the animation thread
	public static void stopAnimation(){
		if (animator != null){
			animator.thread.interrupt();
			animator = null;
		}
	}
}