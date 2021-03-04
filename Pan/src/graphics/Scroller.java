package graphics;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JScrollPane;

import menus.Menu;
import foundation.Core;

// this object handles the scroll pane. it was only made to override a get method
public class Scroller extends JScrollPane{
	
	private static final long serialVersionUID = 1645275040168332370L;
	
	public static final int INDEX_AREA = 0;
	public static final int INDEX_MAP = 1;
	public static final int INDEX_BATTLE = 2;
	
	// these are the coordinates of the last known legal camera position
	private Point lastAreaViewCoords;
	private Point lastMapViewCoords;
	
	public Scroller(Painter painter){
		super(painter);
	}
	
	//
	public void updateLastView(int index){
		switch (index){
			case INDEX_AREA:
				lastAreaViewCoords = findClosestViewCoordinates(Core.currentCharacter.x, Core.currentCharacter.y,INDEX_AREA);
				break;
			case INDEX_MAP:
				lastMapViewCoords = findClosestViewCoordinates(Core.currentArea.worldCoordinatesX, Core.currentArea.worldCoordinatesY,INDEX_MAP);
				break;
			case INDEX_BATTLE:
		}
	}
	
	// moves view towards where character is
	public void centerViewAroundCharacter(){
		// get coordinates of character
		Point characterCoords = new Point(Core.currentCharacter.x,Core.currentCharacter.y);
		
		// calculate what one half of the viewed area is for following math
		int halfViewHorizontal = (int)(Screen.VIEWABLE_COLUMNS * Core.TILE_SIZE /2 * Core.multiplier);
		int halfViewVertical = (int)(Screen.VIEWABLE_ROWS * Core.TILE_SIZE /2 * Core.multiplier);
		
		// calculate top left point for viewport
		Point viewCoords = new Point(characterCoords.x - halfViewHorizontal,characterCoords.y - halfViewVertical);
		
		// check if it is inside the window
		boolean horizontalViewIsLegal = viewCoords.x >= 0 && (characterCoords.x +halfViewHorizontal) <= Core.currentArea.columns*Core.TILE_SIZE*Core.multiplier; //screen.painter.getWidth();
		boolean verticalViewIsLegal = viewCoords.y >= 0 && (characterCoords.y + halfViewVertical) <= Core.currentArea.rows*Core.TILE_SIZE*Core.multiplier; //screen.painter.getHeight();
		
		// make backup of coordinates when legal. use backup when illegal
		if (horizontalViewIsLegal) lastAreaViewCoords.x = viewCoords.x;
		else viewCoords.x = lastAreaViewCoords.x;
		if (verticalViewIsLegal) lastAreaViewCoords.y = viewCoords.y;
		else viewCoords.y = lastAreaViewCoords.y;

		// set viewport to coordinates
		this.getViewport().setViewPosition(viewCoords);
	}
	
	// moves map view towards where character wants it to go
	public void centerViewAroundMapCursor(){
		// get coordinates of character
		Point mapCursorCoords = new Point(Menu.map.x,Menu.map.y);
		
		// calculate what one half of the viewed area is for following math
		int halfViewHorizontal = (int)(Screen.VIEWABLE_COLUMNS * Core.TILE_SIZE /2 * Core.multiplier);
		int halfViewVertical = (int)(Screen.VIEWABLE_ROWS * Core.TILE_SIZE /2 * Core.multiplier);
		
		// calculate top left point for viewport
		Point viewCoords = new Point(mapCursorCoords.x - halfViewHorizontal,mapCursorCoords.y - halfViewVertical);
		
		// check if it is inside the window
		boolean horizontalViewIsLegal = viewCoords.x >= 0 && (mapCursorCoords.x +halfViewHorizontal) <= Menu.map.getMapImage().getWidth() *Core.multiplier; //screen.painter.getWidth();
		boolean verticalViewIsLegal = viewCoords.y >= 0 && (mapCursorCoords.y + halfViewVertical) <= Menu.map.getMapImage().getHeight() *Core.multiplier; //screen.painter.getHeight();
				
		// make backup of coordinates when legal. use backup when illegal
		if (horizontalViewIsLegal) lastMapViewCoords.x = viewCoords.x;
		else viewCoords.x = lastMapViewCoords.x;
		if (verticalViewIsLegal) lastMapViewCoords.y = viewCoords.y;
		else viewCoords.y = lastMapViewCoords.y;

		// set viewport to coordinates
		this.getViewport().setViewPosition(viewCoords);
	}
	
	// finds closest view point for given coordinates
	public Point findClosestViewCoordinates(int x, int y, int viewTypeIndex){
		// get coordinates of center
		Point centerCoords = new Point(x, y);
		
		// prepare  variables
		Point viewCoords = new Point();
		int maxViewTypeWidth, maxViewTypeHeight;
		
		// get max size of viewable feature
		switch (viewTypeIndex){
			case INDEX_AREA:
				maxViewTypeWidth = (int)(Core.currentArea.columns*Core.TILE_SIZE*Core.multiplier);
				maxViewTypeHeight = (int)(Core.currentArea.rows*Core.TILE_SIZE*Core.multiplier);
				break;
			case INDEX_MAP:
				maxViewTypeWidth = (int)(Menu.map.getMapImage().getWidth() *Core.multiplier);
				maxViewTypeHeight = (int)(Menu.map.getMapImage().getHeight() *Core.multiplier);
				break;
			case INDEX_BATTLE:
			default: return null;
		}
			
		// calculate what one half of viewable screen is for following math
		int halfViewHorizontal = (int)(Screen.VIEWABLE_COLUMNS * Core.TILE_SIZE /2 * Core.multiplier);
		int halfViewVertical = (int)(Screen.VIEWABLE_ROWS * Core.TILE_SIZE /2 * Core.multiplier);

		// set appropriate x coordinate
		if (centerCoords.x - halfViewHorizontal < 0) viewCoords.x = 0;
		else if (centerCoords.x + halfViewHorizontal > maxViewTypeWidth) viewCoords.x = maxViewTypeWidth - halfViewHorizontal * 2;
		else viewCoords.x = centerCoords.x - halfViewHorizontal;
		
		// set appropriate y coordinate
		if (centerCoords.y - halfViewVertical < 0) viewCoords.y = 0;
		else if (centerCoords.y + halfViewVertical > maxViewTypeHeight) viewCoords.y = maxViewTypeHeight - halfViewVertical * 2;
		else viewCoords.y = centerCoords.y - halfViewVertical;
		
		// return created view point coordinates
		return viewCoords;
	}
	
	
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int)(Screen.VIEWABLE_COLUMNS*Core.TILE_SIZE*Core.multiplier),
							 (int)(Screen.VIEWABLE_ROWS*Core.TILE_SIZE*Core.multiplier));
	}
	
}
