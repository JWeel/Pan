package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import battle.Combatant;
import menus.Menu;
import areas.Tile;
import foundation.Core;
import foundation.KeyHandler;

//
public class Painter extends JPanel {

	private static final long serialVersionUID = 2202633650928276322L;
	private static final int REAL_TILE_SIZE = (int)(Core.TILE_SIZE * Core.multiplier);
	private static final int VIEWABLE_SCREEN_WIDTH = Core.TILE_SIZE * Screen.VIEWABLE_COLUMNS;
	private static final int VIEWABLE_SCREEN_HEIGHT = Core.TILE_SIZE * Screen.VIEWABLE_ROWS;

	// dimensions of current area and of map
	private static Dimension areaDimensions = new Dimension();
	private static Dimension mapDimensions = new Dimension();
	private static Dimension preferredDimensions = new Dimension();
	
	//private static BufferedImage worldMap;
	
	public KeyHandler keyInput;
	
	public Darkener screenDarkener;
	
	// paints the graphics on the screen. called in Core-> run(){ ... repaint(); ...}
	public void paintComponent(Graphics g){

		// possibly needed ? was necessary for coordinates instead of images
		// INTERNET SAYS: Now if your code is 100% your own drawing... 
		// 					you do not need to call super, just do your drawing
		// i.e. no JLabels or JButtons
		//super.paintComponent(g);
		
		if (Core.currentBattle != null){
			
			paintBattle(g);
			
		} else if (Core.currentArea != null){
			paintArea(g);
			
			paintPanShadow(g);
			
			if (!Core.currentArea.obstacles.isEmpty()) paintObjects(g);				

			
			// in order to create depth / simulated 3D, paint row by row
			for (int row = 0; row < Core.currentArea.tiles[0].length; row++){
				
				//int rowSize = (int)(Screen.TILE_SIZE * multiplier);
				int characterRow = Core.currentCharacter.y / REAL_TILE_SIZE;
				// check to see if character is in this row
				if (characterRow >= row && characterRow <= row + REAL_TILE_SIZE)
					paintPan(g);
				
			}
			
			if (Menu.menuDepth != 0) paintMenu(g);
			
		}
		
		paintOverlay(g);
	}
	
	
	/*================================= AREA =================================*/
	
	
	// paints all tiles
	private void paintArea(Graphics g){	
		int x =0, y =0;
		for (int i = 0; i < Core.currentArea.tiles.length; i++) {		
			for (int j = 0; j < Core.currentArea.tiles[i].length; j++){				
				Tile t = Core.currentArea.tiles[i][j];	
				
				g.drawImage(t.tileImage, 
						x* REAL_TILE_SIZE , 
						y* REAL_TILE_SIZE ,
						REAL_TILE_SIZE , REAL_TILE_SIZE , null);
				
				//g.drawString("" + i + " " + j, x*Screen.TILE_SIZE, y*Screen.TILE_SIZE);
				y++;
			}
			y=0;
			x++;
		}
	}
	
	// paints all objects located on given row
	private void paintObjects(Graphics g){	
		for (int i = 0; i < Core.currentArea.obstacles.size(); i++){
			paintObjectShadows(g, Core.currentArea.obstacles.get(i).x, 
					Core.currentArea.obstacles.get(i).y);
			
			g.drawImage(Core.currentArea.obstacles.get(i).obstacleImage, 
					(int)(Core.currentArea.obstacles.get(i).x * Core.multiplier), 
					(int)(Core.currentArea.obstacles.get(i).y * Core.multiplier),
					REAL_TILE_SIZE , REAL_TILE_SIZE , null);
		}
	}
	
	// paints the shadow of an object near its bottom
	private void paintObjectShadows(Graphics g, int x, int y){
		BufferedImage img = Core.currentCharacter.shadow1;
		
		g.drawImage( img, 
				(int)(x * Core.multiplier ),
				(int)(y * Core.multiplier + img.getHeight() * 1.2 * Core.multiplier ), 
				(int)(img.getWidth()* Core.multiplier),
				(int)(img.getHeight()* Core.multiplier), null);

	}
	
	
	/*============================== CHARACTER ===============================*/
	
	
	// paints the character of pan
	private void paintPan(Graphics g){		
		BufferedImage img = Core.currentCharacter.getAnimationSprite();
		g.drawImage(img, 
				(int)(Core.currentCharacter.x - img.getWidth() * Core.multiplier / 2) , 
				(int)(Core.currentCharacter.y - img.getHeight() *Core.multiplier ),
				(int)(img.getWidth() *Core.multiplier),
				(int)(img.getHeight()*Core.multiplier),null);
		
		//DEBUG to check where real character coordinate is
		g.setColor(Color.RED);
		g.drawOval(Core.currentCharacter.x, Core.currentCharacter.y, 1, 1);
	}
	
	// paints the shadow of a character near its feet
	private void paintPanShadow(Graphics g){
		double tempMultiplier = (Core.multiplier + 1) /2;
		BufferedImage img = Core.currentCharacter.shadow1;
		
		g.drawImage( img, 
				(int)(Core.currentCharacter.x - img.getWidth()*tempMultiplier/2),
				(int)(Core.currentCharacter.y - img.getHeight() * tempMultiplier/2 ), 
				(int)(img.getWidth()*tempMultiplier),
				(int)(img.getHeight()*tempMultiplier), null);

	}
		
	
	/*================================= MENU =================================*/

	
	// paints the menu screen
	private void paintMenu(Graphics g){		
		
		// check if normal menu or map menu
		if (Menu.menuDepth == Menu.INDEX_MAP_MENU) {
			paintMap(g);
		} else {
			BufferedImage img = Menu.getMenuImage(Menu.INDEX_BASE_MENU);
			int x = Core.getViewCoordinates().x;
			int y = Core.getViewCoordinates().y;
			
			//x += REAL_TILE_SIZE;
			if (Menu.subMenuIsNotRoot()) y-= REAL_TILE_SIZE/2;
			
			g.drawImage(img,
					x + REAL_TILE_SIZE ,
					y + REAL_TILE_SIZE * 2,
					(int)(img.getWidth() * Core.multiplier),
					(int)(img.getHeight() * Core.multiplier), null);
			
			paintMenuItems(g, x, y);
			if (Menu.menuIsAboutCharacters()) paintSubMenu(g, x, y);
			paintMenuSelection(g, x, y);
		}
	}
	
	//
	private void paintMenuItems(Graphics g, int x, int y){		
		int itemOffsetY = (int)(Menu.MENU_ITEM_OFFSET_Y * Core.multiplier);
		int initialOffsetX = (int)(Menu.MENU_ITEM_OFFSET_X * Core.multiplier / 2 + Menu.MENU_ITEM_LABEL_OFFSET * Core.multiplier);
		for (int itemIndex = 0; itemIndex <= Menu.getAmountOfMenuItems(Menu.INDEX_BASE_MENU); itemIndex++){
			
			String menuName = Menu.getMenuItemName(itemIndex);
			int characterOffsetX = initialOffsetX;
			for (int characterIndex = 0; characterIndex < menuName.length(); characterIndex++){
				
				BufferedImage characterImage = TextMaker.getTypographyImage(menuName.charAt(characterIndex));					
				g.drawImage(characterImage,
						x + REAL_TILE_SIZE + characterOffsetX,
						y + REAL_TILE_SIZE * 2 + itemOffsetY,
						(int)(characterImage.getWidth() * Core.multiplier),
						(int)(characterImage.getHeight() * Core.multiplier), null);			
				characterOffsetX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
			}
			itemOffsetY += (int)(Menu.MENU_ITEM_SIZE * Core.multiplier ); 
		}
	}

	//
	private void paintSubMenu(Graphics g, int x, int y){
		BufferedImage img = Menu.getMenuImage();
		if (img == null) return;
		int menuOffset = (int)(Menu.getMenuImage(Menu.INDEX_BASE_MENU).getWidth() * Core.multiplier);
		g.drawImage(img, x + REAL_TILE_SIZE + menuOffset, y + REAL_TILE_SIZE * 2,
				(int)(img.getWidth() * Core.multiplier),
				(int)(img.getHeight() * Core.multiplier), null);
		paintMenuFadeOut(g, x, y);
		
		if (Menu.subMenuIsNotRoot()) paintExtraMenu(g,x,y);
		if (Menu.subMenuIsSpellbook()) paintMenuSpellbook(g, x, y);
		else paintMenuInventory(g, x, y);
	}
	
	//
	private void paintMenuFadeOut(Graphics g, int x, int y){
		BufferedImage itemFadeImage = Menu.getMenuFadeImage(Menu.INDEX_FADEOUT_ITEM);
		int screenOffset = (int)(Menu.MENU_FADE_OFFSET * Core.multiplier);
		
		for (int itemIndex = 0; itemIndex <= Menu.getAmountOfMenuItems(Menu.INDEX_BASE_MENU); itemIndex++){
			if (itemIndex == Menu.getEnteredMenuItemIndex()) continue;			
			int menuItemOffset = (int)(itemIndex * Menu.MENU_ITEM_SIZE * Core.multiplier);
			g.drawImage(itemFadeImage,
					x + REAL_TILE_SIZE + screenOffset,
					y + REAL_TILE_SIZE * 2 + screenOffset + menuItemOffset,
					(int)(itemFadeImage.getWidth() * Core.multiplier),
					(int)(itemFadeImage.getHeight() * Core.multiplier), null);			
		}
		BufferedImage menuFadeImage = Menu.getMenuFadeImage(Menu.INDEX_FADEOUT_MENU);
		g.drawImage(menuFadeImage,
				x + REAL_TILE_SIZE ,
				y + REAL_TILE_SIZE * 2,
				(int)(menuFadeImage.getWidth() * Core.multiplier),
				(int)(menuFadeImage.getHeight() * Core.multiplier), null);
	}

	//
	private void paintExtraMenu(Graphics g, int x, int y){
		BufferedImage img = Menu.getExtraMenu();
		if (img == null) return;
		int menuX = x + REAL_TILE_SIZE;
		int menuY = y + (int)((REAL_TILE_SIZE*10) - 2 * Core.multiplier);
		g.drawImage(img, menuX, menuY,
				(int)(img.getWidth() * Core.multiplier),
				(int)(img.getHeight() * Core.multiplier), null);
	}
	
	//
	private void paintMenuSpellbook(Graphics g, int x, int y){
		paintMenuAvatars(g, x, y);
		paintMenuCharacterInfo(g, x, y);
		paintMenuCharacterBaseStats(g,x,y);

		if (!Menu.subMenuIsCharacterRoot()){
			paintMenuAvatarFadeOut(g,x,y);
			
			paintMenuSpellbookSpells(g,x,y);
			paintMenuSpellDescription(g,x,y);
			
			int pageX = (int)(x+ 184 * Core.multiplier);
			int pageY = (int)(y+ 86 * Core.multiplier);
			paintMenuPageNumber(g,pageX,pageY);
		}
	}
	
	//
	private void paintMenuInventory(Graphics g, int x, int y){
		paintMenuAvatars(g, x, y);
		paintMenuCharacterInfo(g, x, y);
		paintMenuCharacterBaseStats(g,x,y);

		paintMenuInventoryItemIcons(g, x, y);

		if (!Menu.subMenuIsCharacterRoot()){
			paintMenuAvatarFadeOut(g,x,y);
			
			if (!Core.team.get(Menu.menuCharacterIndex).inventory.isEmpty()){
				paintMenuItemName(g,x,y);
				paintMenuItemStats(g,x,y);
				
				paintMenuItemDescription(g,x,y);
				
				if (Menu.subMenuItemIsSelected()){
					paintMenuItemChoices(g,x,y);
				}
			}
		}
	}
	
	// paints the mug shots of each character in the team
	private void paintMenuAvatars(Graphics g, int x, int y){
		int startX = (int)(x + 90 * Core.multiplier);
		int startY = (int)(y + 38 * Core.multiplier);
		int avatarWidth = (int)(Menu.CHARACTER_AVATAR_SIZE * Core.multiplier);
		int menuAvatarSize = (int)(20 * Core.multiplier);
		
		for (int i = 0; i < Core.team.size(); i++){
			BufferedImage av = Core.team.get(i).avatar;
			int offsetX = startX + avatarWidth*i;
			g.drawImage(av, offsetX, startY, menuAvatarSize, menuAvatarSize, null);
		}
	}
	
	// paints a fade image over the mug shots of the not-selected characters
	private void paintMenuAvatarFadeOut(Graphics g, int x, int y){
		int startX = (int)(x + 90 * Core.multiplier);
		int startY = (int)(y + 38 * Core.multiplier);
		int avatarWidth = (int)(Menu.CHARACTER_AVATAR_SIZE * Core.multiplier);
		int menuAvatarSize = (int)(20 * Core.multiplier);
		
		for (int i = 0; i < Core.team.size(); i++){
			if (Menu.menuCharacterIndex == i) continue;
			BufferedImage f = Menu.getMenuAvatarFade();
			int offsetX = startX + avatarWidth*i;
			g.drawImage(f, offsetX, startY, menuAvatarSize, menuAvatarSize, null);
		}
	}
	
	// paints the name and level of the highlighted character
	private void paintMenuCharacterInfo(Graphics g, int x, int y){
		int startX = (int)(x + 88 * Core.multiplier);
		int startY = (int)(y + 63 * Core.multiplier);

		// first print character name
		String name = Core.team.get(Menu.menuCharacterIndex).name;
		int nameX = startX;
		for (int characterIndex = 0; characterIndex < name.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(name.charAt(characterIndex));					
			g.drawImage(characterImage,	nameX,startY,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);			
			nameX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
		
		// next print level
		String level = "Lvl " + Integer.toString(Core.team.get(Menu.menuCharacterIndex).level);
		int levelX = startX + (int)(2 * Core.multiplier);
		int levelY = startY + (int)(11 * Core.multiplier);
		for (int characterIndex = 0; characterIndex < level.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(level.charAt(characterIndex));					
			g.drawImage(characterImage,	levelX,levelY,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);			
			levelX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}
	
	// print base info (health and MANA to the right of name, STATS below name)
	private void paintMenuCharacterBaseStats(Graphics g, int x, int y){
		//if (!Menu.subMenuIsCharacterRoot()) return;
		
		int barX = (int)(x + 156 * Core.multiplier);
		int barY = (int)(y + 63 * Core.multiplier);
		
		// print health name and paint health bar
		paintHealth(g,barX,barY,Menu.menuCharacterIndex);
		paintHealthBar(g,barX,barY,Menu.menuCharacterIndex);

		barY += 10 * Core.multiplier;
		
		// print health name and paint health bar
		paintMana(g,barX,barY,Menu.menuCharacterIndex);
		paintManaBar(g,barX,barY,Menu.menuCharacterIndex);	
		
		if (!Menu.subMenuIsCharacterRoot()) return;

		
		if (Menu.subMenuIsSpellbook()) paintMenuCharacterSpellStats(g,x,y);
		else paintMenuCharacterFightStats(g,x,y);
	}
	
	// print elemental powers and resistances
	private void paintMenuCharacterSpellStats(Graphics g, int x, int y){
		int startX = (int)(x + 98 * Core.multiplier);
		int startY = (int)(y + 106 * Core.multiplier);
		
		int statY = startY;
		for (int i = 0; i < 10; i ++){
			String stat =null;
			switch (i){
				case 0: stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).firePower); break;
				case 1: stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).waterPower); break;
				case 2: stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).airPower); break;
				case 3: stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).earthPower); break;
				case 4: stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).arcanePower); break;
				case 5: stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).fireResistance); break;
				case 6: stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).waterResistance); break;
				case 7: stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).airResistance); break;
				case 8: stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).earthResistance); break;
				case 9: stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).arcaneResistance); break;
			}			
			int statX = startX;
			if (i > 4) statX += TextMaker.CHARACTER_WIDTH * 6 *Core.multiplier;
			
			// paint statistic badge
			BufferedImage badge;
			if (i < 5) badge = Menu.getElementPowerBadge(i%5);
			else badge = Menu.getElementResistanceBadge(i%5);
			int badgeX = (int)(statX - 8 * Core.multiplier);
			g.drawImage(badge, badgeX,statY,
					(int)(badge.getWidth() * Core.multiplier),
					(int)(badge.getHeight() * Core.multiplier), null);		

			// paint statistic
			for (int characterIndex = 0; characterIndex < stat.length(); characterIndex++){
				BufferedImage characterImage = TextMaker.getTypographyImage(stat.charAt(characterIndex));					
				g.drawImage(characterImage,	statX,statY,
						(int)(characterImage.getWidth() * Core.multiplier),
						(int)(characterImage.getHeight() * Core.multiplier), null);			
				statX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
			}
			statY += 9 * Core.multiplier;
			if (i == 4) statY = startY;
		}
	}
	
	// print attack rating, armor rating, speed and immunity
	private void paintMenuCharacterFightStats(Graphics g, int x, int y){
		int startX = (int)(x + 98 * Core.multiplier);
		int startY = (int)(y + 107 * Core.multiplier);
		
		int statY = startY;
		for (int i = 0; i < 4; i ++){
			String stat ="";
			BufferedImage badge=null;
			switch (i){
				case 0: 
					stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).attackRating);
					badge = Menu.getAttackBadge(); break;
				case 1: 
					stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).armorRating);
					badge = Menu.getArmorBadge(); break;
				case 2: 
					stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).speed);
					badge = Menu.getSpeedBadge(); break;
				case 3: 
					stat = Integer.toString(Core.team.get(Menu.menuCharacterIndex).immunity);
					badge = Menu.getImmunityBadge(); break;
			}			
			int statX = startX;
			
			// paint statistic badge
			int badgeX = (int)(statX - 8 * Core.multiplier);
			g.drawImage(badge, badgeX,statY,
					(int)(badge.getWidth() * Core.multiplier),
					(int)(badge.getHeight() * Core.multiplier), null);			

			// paint statistic
			for (int characterIndex = 0; characterIndex < stat.length(); characterIndex++){
				BufferedImage characterImage = TextMaker.getTypographyImage(stat.charAt(characterIndex));					
				g.drawImage(characterImage,	statX,statY,
						(int)(characterImage.getWidth() * Core.multiplier),
						(int)(characterImage.getHeight() * Core.multiplier), null);			
				statX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
			}
			statY += 11 * Core.multiplier;
		}		
	}
	
	// paints the spells in the current page of a character's spell-book
	private void paintMenuSpellbookSpells(Graphics g, int x, int y){
		int startX = x + (int)(93 * Core.multiplier);
		int startY = y + (int)(104 * Core.multiplier);
		
		for (int i = 0; i < Menu.getAmountOfMenuItems(); i++){
			
			int realIndex = Menu.menuSpellbookPage*5 + i;
			
			BufferedImage icon = Menu.getElementBadge(Core.team.get(Menu.menuCharacterIndex).spellbook.get(realIndex).elementIndex);
			if (icon != null){
				int iconX = startX - (int)(5 * Core.multiplier);
				int iconY = startY + (int)(2 * Core.multiplier);
				int iconSize = (int)(icon.getWidth()  * Core.multiplier);
				g.drawImage(icon, iconX, iconY,	iconSize,iconSize, null);
			}
			
			String spell = Core.team.get(Menu.menuCharacterIndex).spellbook.get(realIndex).name;
			int spellX = startX;
			for (int characterIndex = 0; characterIndex < spell.length(); characterIndex++){
				BufferedImage characterImage = TextMaker.getTypographyImage(spell.charAt(characterIndex));					
				g.drawImage(characterImage,	spellX, startY,
						(int)(characterImage.getWidth() * Core.multiplier),
						(int)(characterImage.getHeight() * Core.multiplier), null);
				spellX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
			}
			
			String cost;
			if (Core.team.get(Menu.menuCharacterIndex).spellbook.get(i).HPCost > 0)
				cost = Integer.toString(Core.team.get(Menu.menuCharacterIndex).spellbook.get(i).HPCost) + Core.getHealthName();
			else cost = Integer.toString(Core.team.get(Menu.menuCharacterIndex).spellbook.get(i).MPCost) + Core.getManaName();
			
			int costX = x + (int)((200 - (cost.length() * TextMaker.CHARACTER_WIDTH)) * Core.multiplier);
			for (int characterIndex = 0; characterIndex < cost.length(); characterIndex++){
				BufferedImage characterImage = TextMaker.getTypographyImage(cost.charAt(characterIndex));					
				g.drawImage(characterImage,	costX, startY,
						(int)(characterImage.getWidth() * Core.multiplier),
						(int)(characterImage.getHeight() * Core.multiplier), null);
				costX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
			}
			
			startY += (int)(10 * Core.multiplier);
		}
	}
	
	// paints the description for the selected spell in the menu
	private void paintMenuSpellDescription(Graphics g, int x, int y){
		int descX = (int)(x + 34  * Core.multiplier);
		int descY = (int)(y + 164 * Core.multiplier);
		
		BufferedImage icon = Menu.getSpellbookSpell().icon;
		if (icon != null) {
			int iconX = descX - (int)(11 * Core.multiplier);
			int iconY = descY + (int)(0.5 * Core.multiplier);
			int iconSize = (int)(icon.getWidth() * 0.67 * Core.multiplier);
			g.drawImage(icon, iconX,iconY, iconSize, iconSize, null);			
		}
		
		String desc = Menu.getSpellbookSpell().description;
		for (int characterIndex = 0; characterIndex < desc.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(desc.charAt(characterIndex));					
			g.drawImage(characterImage,	descX, descY,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);
			descX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
		
		BufferedImage reach = Menu.getSpellbookSpellReachIcon();
		if (reach != null) {
			int reachX = x + (int)(193 * Core.multiplier);
			int reachY = descY + (int)(0.5 * Core.multiplier);
			int reachSize = (int)(reach.getWidth() * 0.67 * Core.multiplier);
			g.drawImage(reach, reachX,reachY, reachSize, reachSize, null);			
		}
	}
	
	// paints the icons of all items in an inventory
	private void paintMenuInventoryItemIcons(Graphics g, int x, int y){
		if (Menu.subMenuItemIsSelected()) return;
		int startX = x + (int)(131 * Core.multiplier);
		int startY = y + (int)(108 * Core.multiplier);
		
		int iconX = startX;
		for (int i = 0; i < Core.team.get(Menu.menuCharacterIndex).inventory.size(); i++){
			int iconY = startY + (int)((i / 5 * Menu.INVENTORY_ICON_SIZE) * Core.multiplier);
			
			BufferedImage img = Core.team.get(Menu.menuCharacterIndex).inventory.get(i).icon;
			if (img != null) {
				g.drawImage(img, iconX,iconY,
					(int)(img.getWidth() * Core.multiplier),
					(int)(img.getHeight() * Core.multiplier), null);			
			}
			
			// paint check-mark for equipped items
			if (Core.team.get(Menu.menuCharacterIndex).inventory.get(i).isEquipped){
				BufferedImage check = Menu.getCheckmark();
				int checkX = iconX + (int)(4 * Core.multiplier);
				int checkY = iconY + (int)(5 * Core.multiplier);
				g.drawImage(check, checkX,checkY,
						(int)(check.getWidth() * Core.multiplier),
						(int)(check.getHeight() * Core.multiplier), null);
			}
			
			if ((i+1) % 5 == 0) iconX = startX;
			else iconX += Menu.INVENTORY_ICON_SIZE * Core.multiplier;
		}
	}
	
	// paints the name for the selected item in the menu
	private void paintMenuItemName(Graphics g, int x, int y){
		String name = Menu.getInventoryItem().name;
		int nameX = (int)(x + (200 - name.length()*TextMaker.CHARACTER_WIDTH)  * Core.multiplier);
		int nameY = (int)(y + 90 * Core.multiplier);

		for (int characterIndex = 0; characterIndex < name.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(name.charAt(characterIndex));					
			g.drawImage(characterImage,	nameX, nameY,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);
			nameX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}
	
	// paints the description for the selected item in the menu
	private void paintMenuItemDescription(Graphics g, int x, int y){
		String desc = Menu.getInventoryItem().description;
		int descX = (int)(x + 23  * Core.multiplier);
		int descY = (int)(y + 164 * Core.multiplier);

		for (int characterIndex = 0; characterIndex < desc.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(desc.charAt(characterIndex));					
			g.drawImage(characterImage,	descX, descY,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);
			descX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}

	// paints the statistics for the item
	private void paintMenuItemStats(Graphics g, int x, int y){		
		int statX = (int)(x + 88 * Core.multiplier);
		int statY = (int)(y + 110 * Core.multiplier);
		
		// if miscellaneous, print quantity
		if (Menu.getInventoryItem().isMisc()){
			String q = "Amount";
			
			int stringX = statX;
			for (int characterIndex = 0; characterIndex < q.length(); characterIndex++){
				BufferedImage characterImage = TextMaker.getTypographyImage(q.charAt(characterIndex));					
				g.drawImage(characterImage,	stringX, statY,
						(int)(characterImage.getWidth() * Core.multiplier),
						(int)(characterImage.getHeight() * Core.multiplier), null);
				stringX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
			}
			
			q = Integer.toString(Menu.getInventoryItem().quantity);
			stringX = statX + (int)(15 * Core.multiplier);
			int stringY = statY + (int)(12 * Core.multiplier);
			for (int characterIndex = 0; characterIndex < q.length(); characterIndex++){
				BufferedImage characterImage = TextMaker.getTypographyImage(q.charAt(characterIndex));					
				g.drawImage(characterImage,	stringX, stringY,
						(int)(characterImage.getWidth() * Core.multiplier),
						(int)(characterImage.getHeight() * Core.multiplier), null);
				stringX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
			}
			
		
		// else print character statistic bonuses (should never have more than 5)
		} else {
			
			int bonusY = y + (int)((124 - ((Menu.getInventoryItem().bonusStrings.size()-1)* TextMaker.CHARACTER_HEIGHT/2 )) * Core.multiplier);
			for (int i = 0; i < Menu.getInventoryItem().bonusStrings.size(); i++){
				int bonusX = statX;

				BufferedImage b = Menu.getInventoryItem().bonusBadges.get(i);
				g.drawImage(b,	bonusX, bonusY,
						(int)(b.getWidth() * Core.multiplier),
						(int)(b.getHeight() * Core.multiplier), null);
				bonusX += 9 * Core.multiplier;
				
				String s = Menu.getInventoryItem().bonusStrings.get(i);
				for (int characterIndex = 0; characterIndex < s.length(); characterIndex++){
					BufferedImage characterImage = TextMaker.getTypographyImage(s.charAt(characterIndex));					
					g.drawImage(characterImage,	bonusX, bonusY,
							(int)(characterImage.getWidth() * Core.multiplier),
							(int)(characterImage.getHeight() * Core.multiplier), null);
					bonusX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
				}
				
				// print arrow for improvement/diminishing
				if (Menu.getInventoryItem().bonusComparisons != null && Menu.getInventoryItem().bonusComparisons.get(i) != null){
					BufferedImage a;
					if (Menu.getInventoryItem().bonusComparisons.get(i)) a = Menu.getUpArrow();
					else a = Menu.getDownArrow();
					
					int arrowX = bonusX + (int)(2 * Core.multiplier);
					int arrowY = bonusY + (int)(1 * Core.multiplier);
					g.drawImage(a, arrowX, arrowY,
							(int)(a.getWidth() * Core.multiplier),
							(int)(a.getHeight() * Core.multiplier), null);
				}
				bonusY += TextMaker.CHARACTER_HEIGHT * Core.multiplier;
			}
		}
	}

	// paints the choices for the selected item in the inventory
	private void paintMenuItemChoices(Graphics g, int x, int y){
		int iconX = x + (int)(131 * Core.multiplier);
		int iconY = y + (int)(108 * Core.multiplier);
		
		BufferedImage img = Menu.getInventoryItem().icon;
		if (img != null) {
			g.drawImage(img, iconX,iconY,
				(int)(img.getWidth() * Core.multiplier),
				(int)(img.getHeight() * Core.multiplier), null);			
		}
		if (Menu.getInventoryItem().isEquipped){
			BufferedImage check = Menu.getCheckmark();
			int checkX = iconX + (int)(4 * Core.multiplier);
			int checkY = iconY + (int)(5 * Core.multiplier);
			g.drawImage(check, checkX,checkY,
					(int)(check.getWidth() * Core.multiplier),
					(int)(check.getHeight() * Core.multiplier), null);
		}
		
		int startX = x + (int)(158 * Core.multiplier);
		int startY = y + (int)(112 * Core.multiplier);
		
		if (Menu.getInventoryItem().isMisc() && Menu.getInventoryItem().usableInArea
				|| !Menu.getInventoryItem().isMisc() && Menu.inventoryItemIsEquippable){
			int stringX = startX;

			// print USE
			if (Menu.getInventoryItem().isMisc()){
				String use = "Use";
				for (int characterIndex = 0; characterIndex < use.length(); characterIndex++){
					BufferedImage characterImage = TextMaker.getTypographyImage(use.charAt(characterIndex));					
					g.drawImage(characterImage,	stringX, startY,
							(int)(characterImage.getWidth() * Core.multiplier),
							(int)(characterImage.getHeight() * Core.multiplier), null);
					stringX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
				}
			// print EQUIP / UNEQUIP
			} else {
				String equip;
				if (Menu.getInventoryItem().isEquipped)	equip = "Unequip";
				else equip = "Equip";
					
				for (int characterIndex = 0; characterIndex < equip.length(); characterIndex++){
					BufferedImage characterImage = TextMaker.getTypographyImage(equip.charAt(characterIndex));					
					g.drawImage(characterImage,	stringX, startY,
							(int)(characterImage.getWidth() * Core.multiplier),
							(int)(characterImage.getHeight() * Core.multiplier), null);
					stringX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
				}
			}
			startY += 12 * Core.multiplier;
		}
		
		int stringX = startX;

		// print MOVE
		String move = "Move";
		for (int characterIndex = 0; characterIndex < move.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(move.charAt(characterIndex));					
			g.drawImage(characterImage,	stringX, startY,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);
			stringX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
		
		// important items cannot be dropped
		if (Menu.getInventoryItem().isImportant) return;
		
		startY += 12 * Core.multiplier;
		stringX = startX;
		
		// print DROP
		String drop = "Drop";
		for (int characterIndex = 0; characterIndex < drop.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(drop.charAt(characterIndex));					
			g.drawImage(characterImage,	stringX, startY,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);
			stringX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
		
		// print CAN'T EQUIP if not miscellaneous and not equip-able
		if (!Menu.getInventoryItem().isMisc() && !Menu.inventoryItemIsEquippable){
			startY += 16 * Core.multiplier;
			stringX = x + (int)(130 * Core.multiplier);

			String cannot = "Can't equip.";
			for (int characterIndex = 0; characterIndex < cannot.length(); characterIndex++){
				BufferedImage characterImage = TextMaker.getTypographyImage(cannot.charAt(characterIndex));					
				g.drawImage(characterImage,	stringX, startY,
						(int)(characterImage.getWidth() * Core.multiplier),
						(int)(characterImage.getHeight() * Core.multiplier), null);
				stringX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
			}
		}
	}
	
	// 
	private void paintMenuSelection(Graphics g, int x, int y){
		BufferedImage img = Menu.getSelectionSprite();
		if (img == null) return;

		int selectionX = x + Menu.getSelectionX();
		int selectionY = y + Menu.getSelectionY();		
		g.drawImage(img, selectionX, selectionY,
				(int)(img.getWidth() * Core.multiplier),
				(int)(img.getHeight() * Core.multiplier), null);
	}
	
	//
	private void paintMap(Graphics g){
		BufferedImage img = Menu.map.getMapImage();
		g.drawImage(img, 0,	0,
					(int)(img.getWidth() * Core.multiplier),
					(int)(img.getHeight() * Core.multiplier), null);
		paintMapCursor(g);
		// TODO for (ArrayList)Menu.map.significantAreas
		// >> if (areaHasBeenVisited) (possibly get from StoryManager) paintAreaIcon
		// store area coordinates somewhere and have area and map get them
		// add check to cursor to see if hovering over icon, if so paint area name
		paintAreaName(g, Core.currentArea.areaName, Core.currentArea.worldCoordinatesX, Core.currentArea.worldCoordinatesY);
	}
	
	//
	private void paintMapCursor(Graphics g){
		BufferedImage img = Menu.map.getMapCursorImage();
		int cursorWidth = (int)(img.getWidth() * Core.multiplier);
		int cursorHeight = (int)(img.getHeight() * Core.multiplier);
		g.drawImage(img, Menu.map.x - cursorWidth/2, Menu.map.y - cursorHeight/2,
					cursorWidth,cursorHeight, null);
	}
	
	// paints the name of an area exactly one character size above its location
	private void paintAreaName(Graphics g, String areaName, int x, int y){
		int characterOffsetX = (int)((x - (TextMaker.getStringWidth(areaName)/2) )* Core.multiplier);
		int positionY = (int)((y - (TextMaker.CHARACTER_HEIGHT ) )* Core.multiplier);
		for (int characterIndex = 0; characterIndex < areaName.length(); characterIndex++){
			
			BufferedImage characterImage = TextMaker.getTypographyImage(areaName.charAt(characterIndex));					
			g.drawImage(characterImage,	characterOffsetX, positionY,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);			
			characterOffsetX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}
	
	// paints the health string
	private void paintHealth(Graphics g, int x, int y, int index){
		String health = Core.getHealthName();
		int offsetX = x + (int)(2 * Core.multiplier);
		for (int characterIndex = 0; characterIndex < health.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(health.charAt(characterIndex));					
			g.drawImage(characterImage,	offsetX, y,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);	
			offsetX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}
	
	// paints the health bar, its value and potential damage
	private void paintHealthBar(Graphics g, int x, int y, int index){
		x += 9 * Core.multiplier;
		y += 3 * Core.multiplier;
		paintBarOutline(g,x,y);
		
		x += 1 * Core.multiplier;
		y += 1 * Core.multiplier;
		BufferedImage bar = Menu.getLifeBar();
		g.drawImage(bar, x, y,
				(int)(bar.getWidth() * Core.multiplier),
				(int)(bar.getHeight() * Core.multiplier), null);	
		
		boolean isHealth = true;
		paintDamageBar(g,x,y,index, isHealth);
		
		y -= 4 * Core.multiplier;
		String value = Integer.toString(Core.team.get(index).HP);
		paintBarValue(g,x,y, value);
	}

	// paints the MANA string
	private void paintMana(Graphics g, int x, int y, int index){		
		String mana = Core.getManaName();
		int offsetX = x + (int)(2 * Core.multiplier);
		for (int characterIndex = 0; characterIndex < mana.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(mana.charAt(characterIndex));					
			g.drawImage(characterImage,	offsetX, y,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);
			offsetX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}
	
	// paints the MANA bar, its value, and potential damage
	private void paintManaBar(Graphics g, int x, int y, int index){
		x += 9 * Core.multiplier;
		y += 3 * Core.multiplier;
		paintBarOutline(g,x,y);
		
		x += 1 * Core.multiplier;
		y += 1 * Core.multiplier;
		BufferedImage bar = Menu.getManaBar();
		g.drawImage(bar, x, y,
				(int)(bar.getWidth() * Core.multiplier),
				(int)(bar.getHeight() * Core.multiplier), null);
		
		boolean isHealth = false;
		paintDamageBar(g,x,y,index, isHealth);
		
		y -= 4 * Core.multiplier;
		String value = Integer.toString(Core.team.get(index).MP);
		paintBarValue(g,x,y, value);

	}
	
	// paints the damage bar. width depends on percentage damage
	private void paintDamageBar(Graphics g, int x, int y, int index, boolean isHealth){
		BufferedImage damage = Menu.getDamageBar();
		
		// get damage percentage
		double percentageDamage;
		if (isHealth) 
			percentageDamage= 1.0 - ((double)Core.team.get(index).HP / (double)Core.team.get(index).maxHP);
		else 
			percentageDamage= 1.0 - ((double)Core.team.get(index).MP / (double)Core.team.get(index).maxMP);
		if (percentageDamage == 0.0) return;
		
		
		int startX = x + (int)((Math.ceil((1-percentageDamage) * Menu.CHARACTER_PANEL_BAR_SIZE) * Core.multiplier));
		int width = (int)(damage.getWidth() * percentageDamage * Core.multiplier);
		if (width == 0) width = 1;
		g.drawImage(damage,	startX, y, width,
				(int)(damage.getHeight() * Core.multiplier), null);
	}
	
	//
	private void paintBarValue(Graphics g, int x, int y, String value){
		x += ((33 - value.length()*TextMaker.CHARACTER_WIDTH) - 3)* Core.multiplier;
		
		for (int characterIndex = 0; characterIndex < value.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(value.charAt(characterIndex));					
			g.drawImage(characterImage,	x, y,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);	
			x += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}
	
	//
	private void paintBarOutline(Graphics g, int x, int y){
		BufferedImage img = Menu.getOutlineBar();
		g.drawImage(img, x, y,
				(int)(img.getWidth() * Core.multiplier),
				(int)(img.getHeight() * Core.multiplier), null);
	}
	
	// paints the number of the current page of the sub menu
	private void paintMenuPageNumber(Graphics g, int x, int y){
		BufferedImage img;
		String number;
		if (Core.inBattle) {
			img = Core.currentBattle.getBattleMenuSubPagePanel();
			number = Integer.toString(Core.currentBattle.battleSubPage + 1);
		}
		else {
			img = Menu.getMenuPagePanel();
			number = Integer.toString(Menu.menuSpellbookPage);
		}
		if (img == null) return;

		g.drawImage(img, x, y,
				(int)(img.getWidth() * Core.multiplier),
				(int)(img.getHeight() * Core.multiplier), null);
		
		x += 6 * Core.multiplier;
		y += 4 * Core.multiplier;
		
		BufferedImage characterImage = TextMaker.getTypographyImage(number.charAt(0));					
		g.drawImage(characterImage, x, y,
				(int)(characterImage.getWidth() * Core.multiplier),
				(int)(characterImage.getHeight() * Core.multiplier), null);
	}
	
	
	/*================================ BATTLE ===============================*/

	
	//
	private void paintBattle(Graphics g){
		if (Core.currentBattle.getStageIndex()==-2) return;
		paintBattleScreen(g);
		paintFoes(g);
		paintTeam(g);
		paintBattleText(g);
		paintBattleMenu(g);
		paintCharacterBar(g);
		paintExtraBar(g);
	}
	
	// paints the background of the battle
	private void paintBattleScreen(Graphics g){
		BufferedImage img = Core.currentArea.getBattleScreen();
		g.drawImage(img, 0,	0,
				(int)(img.getWidth() * Core.multiplier),
				(int)(img.getHeight() * Core.multiplier), null);
	}
	
	// paints the enemies
	private void paintFoes(Graphics g){
		for (int i = 0; i < Core.currentBattle.monsters.size(); i++){
			
			int x = Core.currentBattle.getMonsterX(i);
			int y = Core.currentBattle.getMonsterY(i);
			
			BufferedImage img = Core.currentBattle.monsters.get(i).getAnimationSprite();
			paintFoeShadow(g, x, y, i, img);
			paintConditions(g,x,y,Core.currentBattle.monsters.get(i), img);

			g.drawImage(img, x,	y,
					(int)(img.getWidth() * Core.multiplier),
					(int)(img.getHeight() * Core.multiplier), null);
		}
	}
	
	// paints a foe's shadow
	private void paintFoeShadow(Graphics g, int x, int y, int i, BufferedImage img){
		BufferedImage shadow = Core.currentBattle.monsters.get(i).getShadow();
		if (shadow == null) return;
		
		int shadowX = (int)(x + ((img.getWidth()/2 - shadow.getWidth()) * Core.multiplier));
		int shadowY = (int)(y + ((img.getHeight() - shadow.getHeight()+1) * Core.multiplier));
		g.drawImage(shadow, shadowX, shadowY,
				(int)(shadow.getWidth() * 2 * Core.multiplier),
				(int)(shadow.getHeight() * Core.multiplier), null);
	}
	
	// paints the members of the team
	private void paintTeam(Graphics g){
		for (int i = 0; i < Core.team.size(); i++){
			
			int x = Core.currentBattle.getTeamX(i);
			int y = Core.currentBattle.getTeamY(i);
			
			BufferedImage img = Core.team.get(i).getAnimationSprite();
			paintTeamShadow(g,x,y, img);
			paintConditions(g,x,y,Core.team.get(i), img);

			g.drawImage(img, x,	y,
					(int)(img.getWidth() * Core.multiplier),
					(int)(img.getHeight() * Core.multiplier), null);
		}
	}
	
	// paints a team member's shadow
	private void paintTeamShadow(Graphics g, int x, int y, BufferedImage img){
		BufferedImage shadow = Core.currentCharacter.getShadow();
		if (shadow == null) return;
		
		int shadowX = (int)(x + (img.getWidth()/32*9 * Core.multiplier));
		int shadowY = (int)(y + (img.getHeight()/32*27 * Core.multiplier));
		g.drawImage(shadow, shadowX, shadowY,
				(int)(shadow.getWidth() * 1.5 * Core.multiplier),
				(int)(shadow.getHeight() * Core.multiplier), null);
	}
	
	// paints icons for conditions that combatant is suffering from
	private void paintConditions(Graphics g, int x, int y, Combatant c, BufferedImage img){
		if (!Core.currentBattle.showConditions()) return;
		if (c.getConditions().size() == 0) return;		
		int totalHeight = (int)(c.getConditions().size() * 7);
		
		
		y += (25 - totalHeight) * Core.multiplier;
		
		x += (15) * Core.multiplier;
		
		//x -= 10 * Core.multiplier;
		//y -= totalHeight;
		
		for (int i = 0; i < c.getConditions().size(); i++){
			
			BufferedImage icon = c.getConditions().get(i).getIcon();
			if (icon == null) continue;
			
			g.drawImage(icon, x, y,
					(int)(icon.getWidth() * Core.multiplier),
					(int)(icon.getHeight() * Core.multiplier), null);
			
			y += 7 * Core.multiplier;
		}
	}
	
	//
	private void paintBattleText(Graphics g){
		
		// get text from currentBattle
		String battleText = Core.currentBattle.getBattleText();
		// if none, don't paint
		if (battleText == null) return;
		
		int offsetX = (int)(VIEWABLE_SCREEN_WIDTH / 48 * Core.multiplier);
		int startY = (int)(VIEWABLE_SCREEN_HEIGHT /64*53 * Core.multiplier);
		for (int characterIndex = 0; characterIndex < battleText.length(); characterIndex++){
			
			BufferedImage characterImage = TextMaker.getTypographyImage(battleText.charAt(characterIndex));					
			g.drawImage(characterImage,
					offsetX, startY,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);			
			offsetX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}
	
	//
	private void paintBattleMenu(Graphics g){
		if (!Core.currentBattle.stageIsPreparation()) return;
		
		BufferedImage img = Core.currentBattle.getMainBattleMenu();
		g.drawImage(img, 0, (int)(156 * Core.multiplier),
				(int)(img.getWidth()*Core.multiplier),
				(int)(img.getHeight()*Core.multiplier), null);
		
		paintBattleMenuText(g);
		paintBattleMenuIcons(g);
		paintBattleMenuAvatar(g);
		paintBattleMenuTooltip(g);
		paintBattleMenuSub(g);
		paintBattleMenuSelection(g);
		paintBattleMenuFadeOut(g);
		paintBattleMenuPointer(g);
	}
	
	//
	private void paintBattleMenuText(Graphics g){
		
		// get text from currentBattle
		String battleMenuText = Core.currentBattle.getBattleMenuText();
		// if none, don't paint
		if (battleMenuText == null) return;
		
		int offsetX = (int)(7 * Core.multiplier);
		int startY = (int)(169 * Core.multiplier);
		for (int characterIndex = 0; characterIndex < battleMenuText.length(); characterIndex++){
			
			BufferedImage characterImage = TextMaker.getTypographyImage(battleMenuText.charAt(characterIndex));					
			g.drawImage(characterImage,
					offsetX, startY,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);			
			offsetX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}
	
	//
	private void paintBattleMenuIcons(Graphics g){

		int offsetX = (int)(62 * Core.multiplier);
		int startY = (int)(158 * Core.multiplier);
		
		for (int i = 0; i < Core.currentBattle.getAmountOfMenuItems(); i++){
			BufferedImage img = Core.currentBattle.getBattleMenuIcon(i);
			if (img != null) {
				g.drawImage(img,
						offsetX, startY,
						(int)(img.getWidth() * Core.multiplier),
						(int)(img.getHeight() * Core.multiplier), null);
			}
			offsetX += (int)(32 *Core.multiplier);
		}
		
	}
	
	// paints the avatar of the current character
	private void paintBattleMenuAvatar(Graphics g){
		BufferedImage img = Core.currentBattle.getBattleMenuAvatar();
		if (img == null) return;
		
		int x = (int)(222 * Core.multiplier);
		int y = (int)(158 * Core.multiplier);
		g.drawImage(img,x, y,
					(int)(img.getWidth() * Core.multiplier),
					(int)(img.getHeight() * Core.multiplier), null);
	}
	
	//
	private void paintBattleMenuSelection(Graphics g){
		BufferedImage img = Core.currentBattle.getSelectionSprite();
		if (img == null) return;
				
		int x = Core.currentBattle.getSelectionX();
		int y = Core.currentBattle.getSelectionY();
		
		g.drawImage(img, x, y,
				(int)(img.getWidth() * Core.multiplier),
				(int)(img.getHeight() * Core.multiplier), null);
	}
	
	//
	private void paintBattleMenuTooltip(Graphics g){
		BufferedImage img = Core.currentBattle.getBattleMenuTooltip();
		if (img == null) return;
		
		int x = (int)(59 * Core.multiplier);
		int y;
		if (Core.currentBattle.getStageProgression() == 2){
			y = (int)(48 * Core.multiplier);
		} else {
			y = (int)(136 * Core.multiplier);
		}
		g.drawImage(img, x, y,
				(int)(img.getWidth() * Core.multiplier),
				(int)(img.getHeight() * Core.multiplier), null);
		
		paintBattleMenuTooltipText(g, y);
		paintBattleMenuTooltipIcon(g, x, y);
		paintBattleMenuTooltipCost(g, x, y);
	}
	
	//
	private void paintBattleMenuTooltipText(Graphics g, int y){
		// get text
		String tooltipText = Core.currentBattle.getBattleMenuTooltipText();
		// if none, don't paint
		if (tooltipText == null) return;
		
		int x = Core.currentBattle.getBattleMenuTooltipTextX();
		y+= 6 * Core.multiplier;
		
		for (int characterIndex = 0; characterIndex < tooltipText.length(); characterIndex++){
			
			BufferedImage characterImage = TextMaker.getTypographyImage(tooltipText.charAt(characterIndex));					
			g.drawImage(characterImage,	x, y,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);			
			x += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}
	
	//
	private void paintBattleMenuTooltipIcon(Graphics g, int x, int y){
		BufferedImage icon = Core.currentBattle.getBattleMenuTooltipIcon();
		if (icon == null) return;
		
		x += 7 * Core.multiplier;
		y += 4 * Core.multiplier;
		
		g.drawImage(icon, x, y,
				(int)(icon.getWidth() * Core.multiplier),
				(int)(icon.getHeight() * Core.multiplier), null);	
	}
	
	//
	private void paintBattleMenuTooltipCost(Graphics g, int x, int y){
		String cost = Core.currentBattle.getBattleMenuTooltipCost();
		if (cost == null) return;
		
		if (cost.length() == 2)	x += 143 * Core.multiplier;
		else x += 137 * Core.multiplier;
		
		y += 6*Core.multiplier;
		
		for (int characterIndex = 0; characterIndex < cost.length(); characterIndex++){
			
			BufferedImage characterImage = TextMaker.getTypographyImage(cost.charAt(characterIndex));					
			g.drawImage(characterImage,	x, y,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);			
			x += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}	
	}
	
	//
	private void paintBattleMenuSub(Graphics g){
		BufferedImage img = Core.currentBattle.getBattleMenuSub();
		if (img == null) return;
		
		int x = (int)(59 * Core.multiplier);
		int y = (int)(67 * Core.multiplier);
		g.drawImage(img, x, y,
				(int)(img.getWidth() * Core.multiplier),
				(int)(img.getHeight() * Core.multiplier), null);
		
		paintBattleMenuSubItem(g,x,y);
		
		int pageX = (int)(41 * Core.multiplier);
		int pageY = (int)(72 * Core.multiplier);		
		paintMenuPageNumber(g, pageX, pageY);
	}
	
	// paints a sub-menu item's text as well as extra details
	private void paintBattleMenuSubItem(Graphics g, int x, int y){
		int offsetY = y + (int)(8 * Core.multiplier);
		for (int i = 0; i < Core.currentBattle.getAmountOfMenuItems(); i++){
		
			int offsetX = x + (int)(8 * Core.multiplier);
			int iconY = offsetY - (int)(2*Core.multiplier);
			int index = Core.currentBattle.getRealSubIndex(i);
			
			paintBattleMenuSubItemIcon(g, offsetX, iconY, index);
			paintBattleMenuSubItemCost(g, x, offsetY, index);
			paintBattleMenuSubItemReachIcon(g, x, iconY, index);
			paintBattleMenuSubItemQuantity(g, x, offsetY, index);
			
			offsetX += 15 * Core.multiplier;

			// get text
			String subMenuItemText = Core.currentBattle.getSubMenuText(index);
			// if none, don't paint
			if (subMenuItemText == null) return;
			
			for (int characterIndex = 0; characterIndex < subMenuItemText.length(); characterIndex++){
				BufferedImage characterImage = TextMaker.getTypographyImage(subMenuItemText.charAt(characterIndex));					
				g.drawImage(characterImage,	offsetX, offsetY,
						(int)(characterImage.getWidth() * Core.multiplier),
						(int)(characterImage.getHeight() * Core.multiplier), null);			
				offsetX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
			}
			
			// paint fade for spells for which there is insufficient energy
			paintBattleMenuSubItemFade(g,x,offsetY, index);
			
			// increase Y coordinate to paint next item
			offsetY += (int)(16 * Core.multiplier);
		}
	}
	
	// paints the icon of an ability or item
	private void paintBattleMenuSubItemIcon(Graphics g, int x, int y, int index){
		BufferedImage icon = Core.currentBattle.getSubMenuIcon(index);
		if (icon == null) return;
		
		g.drawImage(icon, x, y,
				(int)(icon.getWidth() * Core.multiplier),
				(int)(icon.getHeight() * Core.multiplier), null);	
	}
	
	// paints the cost of an ability
	private void paintBattleMenuSubItemCost(Graphics g, int x, int y, int index){
		String cost = Core.currentBattle.getBattleMenuSubCost(index);
		if (cost == null) return;
		
		if (cost.length() == 2)	x += 126 * Core.multiplier;
		else x += 120 * Core.multiplier;
		
		for (int characterIndex = 0; characterIndex < cost.length(); characterIndex++){
			
			BufferedImage characterImage = TextMaker.getTypographyImage(cost.charAt(characterIndex));					
			g.drawImage(characterImage,	x, y,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);			
			x += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}	
	}
	
	// paints the icon for an ability's reach
	private void paintBattleMenuSubItemReachIcon(Graphics g, int x, int y, int index){
		BufferedImage img = Core.currentBattle.getBattleMenuSubItemReachIcon(index);
		if (img == null) return;
		
		x += 142 * Core.multiplier;
		
		g.drawImage(img, x, y,
				(int)(img.getWidth() * Core.multiplier),
				(int)(img.getHeight() * Core.multiplier), null);	
	}
	
	// paints the icon for an ability's reach
	private void paintBattleMenuSubItemQuantity(Graphics g, int x, int y, int index){
		String quantity = Core.currentBattle.getBattleMenuSubItemQuantity(index);
		if (quantity == null) return;
		
		x += (150 - quantity.length()*TextMaker.CHARACTER_WIDTH) * Core.multiplier;
		
		for (int characterIndex = 0; characterIndex < quantity.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(quantity.charAt(characterIndex));					
			g.drawImage(characterImage,	x, y,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);			
			x += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}
	
	//
	private void paintBattleMenuSubItemFade(Graphics g, int x, int y, int index){
		BufferedImage fade = Core.currentBattle.getBattleMenuSubItemFade(index);
		if (fade == null) return;
		
		y -= 4 * Core.multiplier;
		x += 6 * Core.multiplier;
		
		g.drawImage(fade, x, y,
				(int)(fade.getWidth() * Core.multiplier),
				(int)(fade.getHeight() * Core.multiplier), null);
	}

	// paints the fade out of certain menu items
	private void paintBattleMenuFadeOut(Graphics g){
		BufferedImage img = Core.currentBattle.getBattleMenuFadeOut();
		if (img == null) return;
		
		int x = (int)(62 * Core.multiplier);
		int y = (int)(158 * Core.multiplier);
		for (int i = 0; i < 5; i++){
			if (Core.currentBattle.battleMenuItemIsLocked(i)) {
				g.drawImage(img, x, y,
						(int)(img.getWidth() * Core.multiplier),
						(int)(img.getHeight() * Core.multiplier), null);
			}
			x += 32 * Core.multiplier;
		}
	}
	
	// paints the pointer and if necessary also side pointers
	private void paintBattleMenuPointer(Graphics g){
		BufferedImage img = Core.currentBattle.getPointerSprite();
		if (img == null) return;
		
		int x = Core.currentBattle.getPointerX();
		int y = Core.currentBattle.getPointerY();
		
		g.drawImage(img, x, y,
				(int)(img.getWidth() * Core.multiplier),
				(int)(img.getHeight() * Core.multiplier), null);	
		
		if (Core.currentBattle.sidePointersAllowed())
			paintBattleMenuSidePointers(g, x, y, img);
		paintBattleMenuPointerTitle(g, x, y);
	}
	
	// sets up painting of any needed side pointers, first left side then right.
	private void paintBattleMenuSidePointers(Graphics g, int x, int y, BufferedImage img){
		double leftM = 0.75;
		int leftY = (int)(y + 3 * Core.multiplier);
		boolean sideIsLeft = true;
		
		for (int i=0; i < Core.currentBattle.getSidePointerAmount(sideIsLeft); i++){
			int newX = Core.currentBattle.getSidePointerX(x, i, sideIsLeft);
			paintBattleMenuSidePointer(g, newX, leftY, leftM, img);
			leftM -= 0.1;
			leftY += 1 * Core.multiplier;
		}
		double rightM = 0.75;
		int rightY = (int)(y + 3 * Core.multiplier);
		sideIsLeft = false;
		
		for (int i=0; i < Core.currentBattle.getSidePointerAmount(sideIsLeft); i++){
			int newX = Core.currentBattle.getSidePointerX(x, i, sideIsLeft);
			paintBattleMenuSidePointer(g, newX, rightY, rightM, img);
			rightM -= 0.1;
			rightY += 1 * Core.multiplier;
		}
	}
	
	// paints the actual image of the side pointers during targeting
	private void paintBattleMenuSidePointer(Graphics g, int x, int y, double m, BufferedImage img){
		g.drawImage(img, x, y,
				(int)(img.getWidth() * m * Core.multiplier),
				(int)(img.getHeight() * m * Core.multiplier), null);
	}
	
	// paints the actual image of the side pointers during targeting
	private void paintBattleMenuPointerTitle(Graphics g, int x, int y){
		String title = Core.currentBattle.getPointerTitle();
		if (title == null) return;
		
		y -= 10 * Core.multiplier;
		x -= (((title.length()*TextMaker.CHARACTER_WIDTH) / 2) - TextMaker.CHARACTER_WIDTH/2) * Core.multiplier;
		
		for (int characterIndex = 0; characterIndex < title.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(title.charAt(characterIndex));					
			g.drawImage(characterImage,	x, y,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);			
			x += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}
	
	// paints the entire character bar and panel for all characters
	private void paintCharacterBar(Graphics g){
		BufferedImage img = Core.currentBattle.getBattleMenuCharacterBar();
		if (img == null) return;
		
		int x = 0, y = 0;
		for (int i = 0; i < Core.team.size(); i++){
			g.drawImage(img, x, y,
					(int)(img.getWidth() * Core.multiplier),
					(int)(img.getHeight() * Core.multiplier), null);
			paintBattleMenuCharacterPanel(g, x, i);
			x += 46 * Core.multiplier;
		}
	}
	
	// paints the insides of the character panel for a character
	private void paintBattleMenuCharacterPanel(Graphics g, int x, int i){
		int y = (int)(2 * Core.multiplier);
		paintCharacterName(g,x,y,i);
		y += (int)(TextMaker.CHARACTER_HEIGHT * Core.multiplier);
		paintHealth(g,x,y,i);
		paintHealthBar(g,x,y,i);
		if (!Core.currentBattle.useLargerCharacterBar()) return;
		y += (int)(TextMaker.CHARACTER_HEIGHT * Core.multiplier);
		paintMana(g,x,y,i);
		paintManaBar(g,x,y,i);
	}
	
	// paints the name of the character
	private void paintCharacterName(Graphics g, int x, int y, int index){
		String characterName = Core.team.get(index).name;
		int offsetX = x + (int)(2 * Core.multiplier);
		for (int characterIndex = 0; characterIndex < characterName.length(); characterIndex++){
			BufferedImage characterImage = TextMaker.getTypographyImage(characterName.charAt(characterIndex));					
			g.drawImage(characterImage,	offsetX, y,
					(int)(characterImage.getWidth() * Core.multiplier),
					(int)(characterImage.getHeight() * Core.multiplier), null);			
			offsetX += (int)(TextMaker.CHARACTER_WIDTH * Core.multiplier);
		}
	}	
	
	//
	private void paintExtraBar(Graphics g){
		BufferedImage img = Core.currentBattle.getBattleMenuExtraBar();
		if (img == null) return;
	}
	
	
	
	/*=============================== OVERLAY ===============================*/

	
	//
	private void paintOverlay(Graphics g){
		if (screenDarkener == null) return;
		BufferedImage img = screenDarkener.getDarkenedScreen();
		if (img != null) 
			g.drawImage(img, 0,0, this.getWidth(), this.getHeight(), null);
		else endDarkenedScreen();
	}
	
	
	
	/*============================ OTHER METHODS =============================*/
	
	
	//
	public void darkenScreen(){
		screenDarkener = new Darkener();
		screenDarkener.thread = new Thread(screenDarkener);
		screenDarkener.setAnimationID(0);
		screenDarkener.thread.start();
	}
	
	//
	private void endDarkenedScreen(){
		screenDarkener = null;
		Core.movementSuspended = false;
	}
	
	// updates dimensions of the painter (meaning where it is allowed to paint)
	public void updateDimensions(){
		updateAreaDimensions();
		updateMapDimensions();
		updatePreferredDimensions();
	}
	
	// sets the paint dimensions to be equal to current area columns and rows
	private void updateAreaDimensions(){
		areaDimensions.width = (int)(Core.currentArea.columns * REAL_TILE_SIZE);
		areaDimensions.height= (int)(Core.currentArea.rows * REAL_TILE_SIZE);
	}
	
	// sets the paint dimensions to be equal to the size of current world map
	private void updateMapDimensions(){
		if (Menu.map.getMapImage() == null) return;
		mapDimensions.width = (int)(Menu.map.getMapImage().getWidth() * Core.multiplier);
		mapDimensions.height= (int)(Menu.map.getMapImage().getHeight() * Core.multiplier);
	}
		
	// chooses between area and map dimensions to set preferred dimensions
	private void updatePreferredDimensions(){
		if (Menu.map.getMapImage() == null) {
			preferredDimensions.width = areaDimensions.width;
			preferredDimensions.height = areaDimensions.height;
		} else {
			if (areaDimensions.width > mapDimensions.width) preferredDimensions.width = areaDimensions.width;
			else preferredDimensions.width = mapDimensions.width;
			
			if (areaDimensions.height > mapDimensions.height) preferredDimensions.height = areaDimensions.height;
			else preferredDimensions.height = mapDimensions.height;
		}
	}
	
	// sets size of paintable area for the JFrame
	@Override
	public Dimension getPreferredSize() {
		return preferredDimensions;
	}
}
